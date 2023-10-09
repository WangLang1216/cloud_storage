package com.hq.minio.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hq.minio.advice.BusinessException;
import com.hq.minio.dto.FileNameDto;
import com.hq.minio.dto.ShareDto;
import com.hq.minio.entity.ObjectItem;
import com.hq.minio.service.FileService;
import com.hq.minio.service.MinioService;
import com.hq.minio.utils.*;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author damon
 * @data 2023/9/8 14:47
 */
@Service
public class MinioServiceImpl implements MinioService {
    @Resource
    private MinioClient minioClient;
    @Resource
    private FileService fileService;
    @Resource
    private MinioUtils minioUtils;
    @Value("${minio.endpoint}")
    private String address;
    @Value("${minio.bucketName}")
    private String bucketName;


    @Override
    public String selectUser() {
//        try {
        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
        String name=currentUserId.toString();
            if (StringUtils.isBlank(name)) {
                throw new BusinessException(ResponeCode.ParamLost.value, "查询用户：未查询成功");
            }
            return name;
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询用户未成功请重试");
//        }
    }

    @Override
    public String exist() {
//        try {
        String name = selectUser();
        String result=minioUtils.existBucket(name);
        return result;
//    }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：创建桶未成功请重试");
//        }
    }

    @Override
    public JSONObject upload(Long catalogId, MultipartFile[] file) {
        if (catalogId==null||file==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"上传文件：文件不能为空");
        }
//        try {
        String name = selectUser();
        List<String> upload = minioUtils.upload(name,catalogId,file);
        String s="上传成功";
        JSONObject json = JsonResultUtil.getJson(s);
        return json;
//    }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：上传未成功请重试");
//        }
    }

    @Override
    public ResponseEntity<byte[]> download(String fileName) {
        if (StringUtils.isBlank(fileName)){
            throw new BusinessException(ResponeCode.ParamLost.value,"下载文件：文件名不能为空");
        }
//        try {
        String name = selectUser();
        ResponseEntity<byte[]> responseEntity=minioUtils.download(name,fileName);
        return responseEntity;
//    }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：下载未成功请重试");
//        }
    }

    @Override
    public void downloadMultiFileToMinIO(List<String> fileNameDtoList, HttpServletResponse response) {
        if (fileNameDtoList==null||response==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"下载多文件：传参存在空值");
        }
        try {
            String bname = selectUser();

        List<String> fileUrlList = new ArrayList<>();
//        for (ShareDto shareDto:fileNameDtoList
//             ) {
//            fileUrlList = shareDto.getFiles();
////            String name = file.getFileName();
////            System.out.println(name);
////            fileUrlList.add(name);
//        }
        fileUrlList=fileNameDtoList;
        fileUrlList.forEach(System.out::println);

        //被压缩文件InputStream
        InputStream[] srcFiles = new InputStream[fileUrlList.size()];
        //被压缩文件名称
        String[] srcFileNames = new String[fileUrlList.size()];
        for (int i = 0; i < fileUrlList.size(); i++) {
            String fileUrl = fileUrlList.get(i);

            InputStream inputStream = minioUtils.getObject(minioClient, fileUrl,bname);
            if (inputStream == null) {
                continue;
            }
            srcFiles[i] = inputStream;
            String[] splitFileUrl = fileUrl.split("/");
            srcFileNames[i] = splitFileUrl[splitFileUrl.length - 1];
        }
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("test.zip", "utf-8"));
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
//        response.setCharacterEncoding("GBK");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"Content-Disposition");
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("test.zip", "GBK"));
        //多个文件压缩成压缩包返回
        ZipUtil.zip(response.getOutputStream(), srcFileNames, srcFiles);
//        return ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (Exception e){
            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：多文件下载未成功请重试");
        }
    }

    @Override
    public List<ObjectItem> list() {
//        try {
        String name = selectUser();
        List<ObjectItem> list = minioUtils.selectList(name);
        return list;
//    } catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询桶文件失败请重试");
//        }
    }

    @Override
    public JSONObject deleteFiles(List<FileNameDto> list) {
        if (list==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"删除文件：删除文件名不能为空");
        }
        try {
            String name = selectUser();

        Long userId=Long.valueOf(name);
        List<String>listFileName=new ArrayList<>();
        //需前端传回json fileName : value
        for (FileNameDto s:list
        ) {
            String fileName = s.getFileName();
            fileService.deleteFile(fileName,userId);
            listFileName.add(fileName);
        }
        String result=minioUtils.deleteFiles(name,listFileName);
        JSONObject json = JsonResultUtil.getJson(result);
        return json;
        }catch (Exception e){
            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：删除失败请重试");
        }
    }

    @Override
    public R<String> shareFiles(ShareDto shareDto) {
        if (ObjectUtil.isEmpty(shareDto)){
            throw new BusinessException(ResponeCode.ParamLost.value,"分享文件:文件为空");
        }
//        try {
        String currentUserId= selectUser();
        Long sourceId= Long.valueOf(currentUserId);

        List<String> file = shareDto.getFiles();
        Long userId = shareDto.getUserId();
        System.out.println(userId);
        file.forEach(System.out::println);


        for (String fileName:file
        ) {
            fileService.insertShare(userId,fileName,sourceId);
        }

        String result="分享成功";
        JSONObject json = JsonResultUtil.getJson(result);
            R<String> r = R.success(result);
            return r;
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：分享失败请重试");
//        }
    }
}

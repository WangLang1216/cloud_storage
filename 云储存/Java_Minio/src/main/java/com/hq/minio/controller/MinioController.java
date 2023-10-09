package com.hq.minio.controller;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import com.hq.minio.dto.FileNameDto;
import com.hq.minio.dto.ShareDto;
import com.hq.minio.entity.ObjectItem;
import com.hq.minio.service.FileService;
import com.hq.minio.service.MinioService;
import com.hq.minio.utils.JsonResultUtil;
import com.hq.minio.utils.MinioUtils;
import com.hq.minio.utils.R;
import com.hq.minio.utils.SecurityUserHolder;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author damon
 * @data 2023/8/3 13:45
 */
@RestController
@Slf4j
@RequestMapping("/minio")
@CrossOrigin
public class MinioController {
    @Resource
    private MinioClient minioClient;
    @Resource
    private FileService fileService;
    @Resource
    private MinioUtils minioUtils;
    @Resource
    private MinioService minioService;
    @Value("${minio.endpoint}")
    private String address;
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 创建用户存储空间
     * 查询桶是否存在 若不在则创建
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/exist")
    public String exist(HttpServletRequest httpServletRequest){
        //       这个地方需要从前端登录时拿到id 再查询得到userId 并作为bucketName创建用户唯一桶
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        String name=fileService.selectUserId(token);
//        String result=minioUtils.existBucket(name);
        String result = minioService.exist();
        return result;
    }

    /**
     * 上传文件
     * @param catalogId
     * @param file
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/upload")
    public R<JSONObject> upload(@RequestParam Long catalogId, @RequestParam(value = "files",required = false)
            MultipartFile[] file, HttpServletRequest httpServletRequest) {
        //      前端点击上传文件时  这个地方从redis中获取id 将文件上传到用户唯一桶
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        String name=fileService.selectUserId(token);
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        String name=currentUserId.toString();

//        List<String> upload = minioUtils.upload(name,catalogId,file);
//        String s="上传成功";
//        JSONObject json = JsonResultUtil.getJson(s);
        JSONObject json = minioService.upload(catalogId, file);
        return R.success(json);
    }

    /**
     * 下载单个文件
     * @param fileName
     * @param httpServletRequest
     * @return
     * @throws IOException
     */
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable("fileName") String fileName
            ,HttpServletRequest httpServletRequest)throws IOException{
        // 前端点击下载文件时  获取id和文件名称 根据文件名称 在本用户bucket中找到文件并下载
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        String name=fileService.selectUserId(token);
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        String name=currentUserId.toString();
//        ResponseEntity<byte[]> responseEntity=minioUtils.download(name,fileName);
        System.out.println(fileName);
        ResponseEntity<byte[]> responseEntity= minioService.download(fileName);
        return responseEntity;

    }

    /**
     * 下载批量文件
     * @param fileNameDtoList
     * @param response
     * @param httpServletRequest
     * @throws IOException
     */
    @PostMapping("/downloadMultiFileToMinIO")
    public void downloadMultiFileToMinIO(@RequestBody List<String>fileNameDtoList
            ,HttpServletResponse response,HttpServletRequest httpServletRequest) throws IOException {
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        String bname=fileService.selectUserId(token);
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        String bname=currentUserId.toString();
//
//        List<String> fileUrlList = new ArrayList<>();
////        for (ShareDto shareDto:fileNameDtoList
////             ) {
////            fileUrlList = shareDto.getFiles();
//////            String name = file.getFileName();
//////            System.out.println(name);
//////            fileUrlList.add(name);
////        }
//        fileUrlList=fileNameDtoList;
//        fileUrlList.forEach(System.out::println);
//
//        //被压缩文件InputStream
//        InputStream[] srcFiles = new InputStream[fileUrlList.size()];
//        //被压缩文件名称
//        String[] srcFileNames = new String[fileUrlList.size()];
//        for (int i = 0; i < fileUrlList.size(); i++) {
//            String fileUrl = fileUrlList.get(i);
//
//            InputStream inputStream = minioUtils.getObject(minioClient, fileUrl,bname);
//            if (inputStream == null) {
//                continue;
//            }
//            srcFiles[i] = inputStream;
//            String[] splitFileUrl = fileUrl.split("/");
//            srcFileNames[i] = splitFileUrl[splitFileUrl.length - 1];
//        }
//        response.setCharacterEncoding("utf-8");
//        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("test.zip", "utf-8"));
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
////        response.setCharacterEncoding("GBK");
////        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"Content-Disposition");
////        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("test.zip", "GBK"));
//        //多个文件压缩成压缩包返回
//        ZipUtil.zip(response.getOutputStream(), srcFileNames, srcFiles);
//
////        return ResponseEntity.status(HttpStatus.CREATED).build();
        minioService.downloadMultiFileToMinIO(fileNameDtoList,response);
    }

    //已废弃
//    @GetMapping("/download2/{s1}/{s2}")
//    public void download2(@PathVariable String s1,@PathVariable String s2
//            ,HttpServletResponse response,HttpServletRequest httpServletRequest) throws IOException {
//        // 从请求头中获取token
////        String token = httpServletRequest.getHeader("token");
////        String bname=fileService.selectUserId(token);
//
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        String bname=currentUserId.toString();
//
//        List<String> fileUrlList = new ArrayList<>();
//        fileUrlList.add(s1);
//        fileUrlList.add(s2);
////        for (ShareDto shareDto:fileNameDtoList
////             ) {
////            fileUrlList = shareDto.getFiles();
//////            String name = file.getFileName();
//////            System.out.println(name);
//////            fileUrlList.add(name);
////        }
//
//        fileUrlList.forEach(System.out::println);
//
//        //被压缩文件InputStream
//        InputStream[] srcFiles = new InputStream[fileUrlList.size()];
//        //被压缩文件名称
//        String[] srcFileNames = new String[fileUrlList.size()];
//        for (int i = 0; i < fileUrlList.size(); i++) {
//            String fileUrl = fileUrlList.get(i);
//
//            InputStream inputStream = minioUtils.getObject(minioClient, fileUrl,bname);
//            if (inputStream == null) {
//                continue;
//            }
//            srcFiles[i] = inputStream;
//            String[] splitFileUrl = fileUrl.split("/");
//            srcFileNames[i] = splitFileUrl[splitFileUrl.length - 1];
//        }
//        response.setCharacterEncoding("utf-8");
//        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("test.zip", "utf-8"));
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
////        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("test.zip", "GBK"));
//        //多个文件压缩成压缩包返回
//        ZipUtil.zip(response.getOutputStream(), srcFileNames, srcFiles);
//
////        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    /**
     * 查询桶里所有文件
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/selectAllFile")
    public List<ObjectItem> list(HttpServletRequest httpServletRequest){
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        String name=fileService.selectUserId(token);
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        String name=currentUserId.toString();
//
//        List<ObjectItem> list = minioUtils.selectList(name);
        List<ObjectItem> result = minioService.list();
        return result;
    }

    /**
     * 删除桶
     * @param name
     * @return
     */
    @DeleteMapping("/deleteBucket")
    public R<String> deleteBucket(@RequestParam String name){
        String result=minioUtils.removeBucket(name);
        R<String> r = R.success(result);
        return r;
    }

    /**
     * 批量删除文件
     * @param list
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
//    @PostMapping("/deleteFiles")
    @DeleteMapping("/Files")
    public R<JSONObject> deleteFiles(@RequestBody List<FileNameDto> list, HttpServletRequest httpServletRequest)
            throws Exception {
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        String name = fileService.selectUserId(token);
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        String name=currentUserId.toString();
//
//        Long userId=Long.valueOf(name);
//        List<String>listFileName=new ArrayList<>();
//        //需前端传回json fileName : value
//        for (FileNameDto s:list
//        ) {
//            String fileName = s.getFileName();
//            fileService.deleteFile(fileName,userId);
//            listFileName.add(fileName);
//        }
//
//        String result=minioUtils.deleteFiles(name,listFileName);
//        JSONObject json = JsonResultUtil.getJson(result);
        JSONObject json = minioService.deleteFiles(list);
        return R.success(json);
    }

    /**
     * 文件分享
     * @param shareDto
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/shareFiles")
    public R<String> shareFiles(@RequestBody ShareDto shareDto
            ,HttpServletRequest httpServletRequest){
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        Long sourceId = Long.valueOf(fileService.selectUserId(token));

//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        Long sourceId= Long.valueOf(currentUserId);
//
//        List<String> file = shareDto.getFiles();
//        Long userId = shareDto.getUserId();
//        System.out.println(userId);
//        file.forEach(System.out::println);
//
//
//        for (String fileName:file
//        ) {
//            fileService.insertShare(userId,fileName,sourceId);
//        }
//
//        String result="分享成功";
//        JSONObject json = JsonResultUtil.getJson(result);
        R<String> result = minioService.shareFiles(shareDto);
        return result;
    }





}



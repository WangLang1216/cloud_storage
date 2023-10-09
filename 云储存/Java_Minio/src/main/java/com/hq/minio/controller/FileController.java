package com.hq.minio.controller;

import com.alibaba.fastjson.JSONObject;
import com.hq.minio.entity.Catalog;
import com.hq.minio.entity.File;
import com.hq.minio.service.FileService;
import com.hq.minio.utils.JsonResultUtil;
import com.hq.minio.utils.MinioUtils;
import com.hq.minio.utils.R;
import com.hq.minio.utils.SecurityUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author damon
 * @data 2023/8/8 11:02
 */
@RestController
@Slf4j
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    @Resource
    private FileService fileService;
    @Resource
    private MinioUtils minioUtils;


    /**
     * 文件查询
     * @param catalogId
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/selectFileAndCatalog/{catalogId}")
    public R<JSONObject> selectFileAndCatalog(@PathVariable("catalogId") Long catalogId, HttpServletRequest httpServletRequest){
        // 从请求头中获取token 现改为从拦截器中获取
//        String token = httpServletRequest.getHeader("token");
//        Long userId = Long.valueOf(fileService.selectUserId(token));
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        Long userId= Long.valueOf(currentUserId);
//        List<File> files = fileService.selectFile(catalogId,userId);
//        List<Catalog> sonlogs = fileService.selectSonlog(catalogId,userId);
//
//        JSONObject data = new JSONObject();
//        data.put("file",files);
//        data.put("catalog",sonlogs);
//        JSONObject json = JsonResultUtil.getJson(data); //JsonResultUtil统一返回类型
        R<JSONObject> result = fileService.selectFileAndCatalog(catalogId);
        return result;
    }

    /**
     * 搜索文件 模糊查询
     * @param file
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/selectLikeFile/{fileName}")
    public R<List<String>> selectLikeFile(@PathVariable("fileName") String file,HttpServletRequest httpServletRequest){
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        Long userId = Long.valueOf(fileService.selectUserId(token));
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        Long userId= Long.valueOf(currentUserId);

//        List<File> files = fileService.selectLikeFile(file,userId);
//        List<String> fileName=new ArrayList<>();
//        for (File f:files
//        ) {
//            String name = f.getFileName();
//            fileName.add(name);
//        }
//        JSONObject json = JsonResultUtil.getJson(fileName);
        R<List<String>> result = fileService.selectLikeFile(file);
        return result;
    }

    /**
     * 新建目录
     * @param dadlogId
     * @param catalogName
     * @param httpServletRequest
     * @return
     */
//    @GetMapping("/createCatalog/{dadlogId}/{catalogName}")
    @PostMapping("/Catalog/{dadlogId}/{catalogName}")
    public R<JSONObject> createCatalog(@PathVariable("dadlogId") Long dadlogId
            , @PathVariable("catalogName") String catalogName, HttpServletRequest httpServletRequest){
        //前端新建目录时 获取父目录id 以及创建目录的名字
        // 从请求头中获取token
//        String token = httpServletRequest.getHeader("token");
//        Long userId = Long.valueOf(fileService.selectUserId(token));
//        Integer currentUserId = SecurityUserHolder.getCurrentUserId();
//        Long userId= Long.valueOf(currentUserId);
        String s= fileService.createCatalog(catalogName,dadlogId);
        JSONObject json = JsonResultUtil.getJson(s);
        return R.success(json);

    }

    /**
     * 更新目录
     * @param catalogId
     * @param newName
     * @return
     */
//    @GetMapping("/updateCatalog/{catalogId}/{newName}")
    @PutMapping("/Catalog/{catalogId}/{newName}")
    public R<String> updateCatalog(@PathVariable("catalogId") Long catalogId
            ,@PathVariable("newName") String newName){
        String s= fileService.updateCatalogName(catalogId, newName);
        R<String> result = R.success(s);
        return result;
    }

    /**
     * 删除目录
     * @param catalogId
     * @return
     */
//    @GetMapping("/deleteCatalog/{catalogId}")
    @DeleteMapping("/Catalog/{catalogId}")
    public R<JSONObject> deleteCatalog(@PathVariable("catalogId") Long catalogId){
        String s = fileService.deleteCatalog(catalogId);
        JSONObject json=JsonResultUtil.getJson(s);
        return R.success(json);
    }





    @GetMapping("/test/{name}")
    public String test(@PathVariable("name") String name1){
//        fileService.selectLikeFile("jpg",104L);
        System.out.println(name1);
        return name1+"11111111";
    }

    @GetMapping("/test2/{userName}")
    public List<String> test2(@PathVariable("userName") String userName){

        List<File> file = fileService.selectFile(0L, 104L);
        List<String> result=new ArrayList<>();
        for (File s:file
        ) {
            String name = s.getFileName();
            result.add(name);
        }
        int i=1/0;
        return result;
    }

}

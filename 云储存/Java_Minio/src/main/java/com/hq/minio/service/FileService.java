package com.hq.minio.service;

import com.alibaba.fastjson.JSONObject;
import com.hq.minio.entity.Catalog;
import com.hq.minio.entity.File;
import com.hq.minio.utils.R;

import java.util.List;

/**
 * @author damon
 * @data 2023/8/7 15:29
 */

public interface FileService {

    /**
     * 根据id查询userId
     * @param token
     * @return
     */
    String selectUserId(String token);

    /**
     * 模糊搜索文件
     * @param fileName
     * @return
     */
    R<List<String>> selectLikeFile(String fileName);

    /**
     * 查询目录
     * @param catalogId
     * @return
     */
    Catalog selectCatalog(Long catalogId);

    /**
     * 创建目录
     * @param catalogName
     * @param dadLogId

     * @return
     */
    String createCatalog(String catalogName,Long dadLogId);

    /**
     * 查询文件
     * @param catalogId
     * @param userId
     * @return
     */
    List<File> selectFile(Long catalogId,Long userId);

    /**
     * 查询子目录
     * @param catalogId
     * @param userId
     * @return
     */
    List<Catalog> selectSonlog(Long catalogId, Long userId);

    /**
     * 更新目录名
     * @param catalogId
     * @param newName
     * @return
     */
    String updateCatalogName(Long catalogId,String newName);

    /**
     * 删除目录
     * @param catalogId
     * @return
     */
    String deleteCatalog(Long catalogId);

    /**
     * 插入文件
     * @param fileName
     * @param catalog
     * @param userId
     * @return
     */
    String insertFile(String fileName,Long catalog,Long userId);

    /**
     * 删除文件
     * @param fileName
     * @param userId
     * @return
     */
    String deleteFile(String fileName,Long userId);

    /**
     * 查询目录名
     * @param catalogId
     * @return
     */
    Catalog selectCatalogName(Long catalogId);

    /**
     * 创建初始目录
     * @param userId
     * @return
     */
    String createDefaultCatalog(Long userId);

    /**
     * 分享文件
     * @param id
     * @param fileName
     * @param sourceId
     * @return
     */
    String insertShare(Long id,String fileName,Long sourceId);

    /**
     * 查询用户所有文件
     * @param userId
     * @return
     */
    List<File>selectUserFiles(Long userId);

    /**
     * tcp插入文件
     * @param userId
     * @param fileName
     * @param fileSize
     * @return
     */
    String insertTcpFile(Long userId,String fileName,String fileSize);

    /**
     * 查询目录下的 子目录和文件
     * @param catalogId
     * @return
     */
    R<JSONObject> selectFileAndCatalog(Long catalogId);

    /**
     * 从拦截器获取用户id
     * @return
     */
    Long selectUser();
}

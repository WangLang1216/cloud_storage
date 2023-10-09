package com.hq.minio.service;

import com.alibaba.fastjson.JSONObject;
import com.hq.minio.dto.FileNameDto;
import com.hq.minio.dto.ShareDto;
import com.hq.minio.entity.ObjectItem;
import com.hq.minio.utils.R;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author damon
 * @data 2023/9/8 14:46
 */

public interface MinioService {
    /**
     * 从拦截器获取用户id
     * @return
     */
    String selectUser();
    /**
     * 创建用户桶
     * @return
     */
    String exist();
    /**
     * 上传文件
     * @param catalogId
     * @param file
     * @return
     */
    JSONObject upload(Long catalogId, MultipartFile[] file);

    /**
     * 下载单个文件
     * @param fileName
     * @return
     */
    ResponseEntity<byte[]> download(String fileName);

    /**
     * 下载多个文件
     * @param fileNameDtoList
     * @param response
     */
    void downloadMultiFileToMinIO(List<String> fileNameDtoList, HttpServletResponse response);

    /**
     * 查询桶里所有文件
     * @return
     */
    List<ObjectItem> list();

    /**
     * 删除文件
     * @param list
     * @return
     */
    JSONObject deleteFiles(List<FileNameDto> list);

    /**
     * 分享文件
     * @param shareDto
     * @return
     */
    R<String> shareFiles(ShareDto shareDto);
}

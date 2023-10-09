package com.hq.minio.utils;


import com.hq.minio.entity.ObjectItem;
import com.hq.minio.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.*;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author damon
 * @data 2023/8/3 11:12
 */
@Slf4j
@Component
public class MinioUtils {
    
    @Resource
    private FileService fileService;
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;
    
    /**
     * 判断bucket是否存在
     * 不存在则创建
     */
    @SneakyThrows
    public String existBucket(String name){
        boolean exist=minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        if (!exist){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            return "bucket不存在 已创建成功";
        }
        return "bucket已存在";
    }

    /**
     * 删除bucket
     */
    @SneakyThrows
    public String removeBucket(String name){
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build());
        return "删除成功";
    }

    /**
     * 上传文件
     */
    public List<String> upload(String name,Long catalogId, MultipartFile[] multipartFile) {
        List<String> names = new ArrayList<>(multipartFile.length);
        for (MultipartFile file : multipartFile) {
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);

            InputStream in = null;
            try {
                in = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(name)
                        .object(fileName)
                        .stream(in, in.available(), -1)
                        .contentType(file.getContentType())
                        .build()
                );
                Long userId= Long.valueOf(name);
                fileService.insertFile(fileName, catalogId,userId);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            names.add(fileName);
        }
        return names;
    }


    /**
     * 下载文件
     */
    public ResponseEntity<byte[]> download(String name,String filename){
        ResponseEntity<byte[]> responseEntity=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;
        try{

            in=minioClient.getObject(GetObjectArgs.builder().bucket(name).object(filename).build());
            out= new ByteArrayOutputStream();
            IOUtils.copy(in,out);
            //封装返回值
            byte[] bytes=out.toByteArray();
            HttpHeaders httpHeaders=new HttpHeaders();

            try {
                httpHeaders.add("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            }catch (Exception e){
                e.printStackTrace();
            }
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            /**
             * application/octet-stream是应用程序文件的默认值。意思是未知的应用程序文件 ，
             * 浏览器一般不会自动执行或询问执行。
             * 浏览器会像对待，设置了HTTP头Content-Disposition 值为 attachment 的文件一样来对待这类文件，即浏览器会触发下载行为。
             */
            httpHeaders.setAccessControlExposeHeaders(Arrays.asList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if (in!=null){
                    try{
                        in.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                if (out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseEntity;
    }


    /**
     * 得到指定文件的InputStream
     *
     * @param originalName 文件路径
     * @return
     */
    public InputStream getObject(MinioClient minioClient, String originalName,String bname) {
        try {

            return minioClient.getObject(GetObjectArgs.builder().bucket(bname).object(originalName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 查看bucket中所有文件
     */

    public List<ObjectItem> selectList(String name){
        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs.builder().bucket(name).build());
        List<ObjectItem> list=new ArrayList<>();
        try{
            for (Result<Item> result: results) {
                Item item=result.get();
                ObjectItem objectItem=new ObjectItem();
                objectItem.setObjectName(item.objectName());
                objectItem.setSize(item.size());
                list.add(objectItem);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * 批量删除bucket中的文件
     */
    public String deleteFiles(String name,List<String> listFileName)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
            InternalException {

        List<DeleteObject> objects=new LinkedList<>();
        for (String s:listFileName
             ) {
            objects.add(new DeleteObject(s));
        }

        Iterable<Result<DeleteError>> results =
                minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(name).objects(objects).build());
        for (Result<DeleteError> result: results) {
            DeleteError error=result.get();
            System.out.println("Error in deleting object " + error.objectName() + "; " + error.message());
        }
        return "批量删除成功";
    }


}

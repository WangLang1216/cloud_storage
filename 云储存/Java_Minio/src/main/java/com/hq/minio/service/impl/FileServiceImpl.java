package com.hq.minio.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hq.minio.advice.BusinessException;
import com.hq.minio.entity.Catalog;
import com.hq.minio.entity.File;
import com.hq.minio.entity.Sonlog;
import com.hq.minio.entity.UserInfo;
import com.hq.minio.mapper.CatalogMapper;
import com.hq.minio.mapper.FileMapper;
import com.hq.minio.mapper.SonlogMapper;
import com.hq.minio.service.FileService;
import com.hq.minio.service.UserInfoService;
import com.hq.minio.utils.JsonResultUtil;
import com.hq.minio.utils.R;
import com.hq.minio.utils.ResponeCode;
import com.hq.minio.utils.SecurityUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author damon
 * @data 2023/8/7 15:30
 */
@Component
@Slf4j
public class FileServiceImpl implements FileService {
    @Resource
    private  FileMapper fileMapper;
    @Resource
    private CatalogMapper catalogMapper;
    @Resource
    private SonlogMapper sonlogMapper;
    @Autowired
    private UserInfoService userInfoService;

    //根据id查询userId
    @Override
    public String selectUserId(String token) {
        if (StringUtils.isBlank(token)){
            throw new BusinessException(ResponeCode.UNAUTHORIZED.value,"查询userid：token已失效");
        }
//        try {
            //从token拿userid
            UserInfo userinfo = userInfoService.queryUserNameByToken(token);
            Long userId = userinfo.getuId();
            String s = userId.toString();
            return s;
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询userId失败");
//        }

    }

    //模糊查询  %filename%
    @Override
    public R<List<String>> selectLikeFile(String fileName) {
        Long userId = selectUser();
        if (StringUtils.isBlank(fileName)|| userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"模糊搜索文件：文件名不能为空");
        }
//        try{
            QueryWrapper<File> wrapper = new QueryWrapper<>();
            wrapper
                    .eq("user_id",userId)
                    .like("file_name",fileName);
            List<File> files = fileMapper.selectList(wrapper);
            files.forEach(System.out::println);
//            int i=1/0;
            List<String> fileN=new ArrayList<>();
            for (File f:files
            ) {
                String name = f.getFileName();
                fileN.add(name);
            }
            JSONObject json = JsonResultUtil.getJson(fileN);
            R<List<String>> result = R.success(fileN);
            return result;

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询文件失败请重试");
//        }

    }

    //根据目录id查询catalog
    @Override
    public Catalog selectCatalog(Long catalogId) {
        if (catalogId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"查询目录：目录不能为空");
        }
//        try {
            QueryWrapper<Catalog>wrapper=new QueryWrapper<>();
            wrapper.eq("catalog_id",catalogId);
            Catalog catalog = catalogMapper.selectOne(wrapper);

            return catalog;
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询目录失败请重试");
//        }
    }

    //新建目录
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String createCatalog(String catalogName,Long dadLogId) {
        Long userId = selectUser();
        if (StringUtils.isBlank(catalogName)||dadLogId==null||userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"新建目录：目录名不能为空");
        }
//        try{

            Catalog catalog=new Catalog();
            Long id = IdWorker.getId();
            catalog.setCatalogId(id);
            catalog.setCatalogName(catalogName);
            catalog.setDadLogId(dadLogId);
            catalog.setUserId(userId);

            int insert = catalogMapper.insert(catalog);

            Sonlog sonlog = new Sonlog();
            sonlog.setCatalogId(dadLogId);
            sonlog.setSonLogId(id);
            sonlog.setUserId(userId);

            int insert2=sonlogMapper.insert(sonlog);

            if (insert!=0&&insert2!=0){
                return "新建目录成功";
            }else {
                return "新建目录成功";
            }

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：新建目录失败请重试");
//        }
    }



    //查询目录下的file
    @Override
    public List<File> selectFile(Long catalogId,Long userId) {
        if (catalogId==null||userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"查询子文件：目录id或用户id为空");
        }
//        try {
            QueryWrapper<File> wrapper = new QueryWrapper<>();
            //如果查询根目录还需判断userid
            if (catalogId==0){
                wrapper
                        .eq("catalog_id",catalogId)
                        .eq("user_id",userId);
            }else{
                wrapper.eq("catalog_id",catalogId);
            }
            List<File> files = fileMapper.selectList(wrapper);

            List<String> fileName=new ArrayList<>();
            for (int i=0;i<files.size();i++){
                String name = files.get(i).getFileName();
                fileName.add(name);
            }
            return files;

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询文件失败请重试");
//        }
    }

    //查询目录下的子目录
    @Override
    public List<Catalog> selectSonlog(Long catalogId,Long userId) {
        if (catalogId==null||userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"查询子目录：目录id或用户id为空");
        }
//        try {
            QueryWrapper<Sonlog> wrapper = new QueryWrapper<>();
            //如果查询根目录还需判断userid
            if (catalogId==0){
                wrapper
                        .eq("catalog_id",catalogId)
                        .eq("user_id",userId);
            }else{
                wrapper.eq("catalog_id",catalogId);
            }
            List<Sonlog> sonlogs = sonlogMapper.selectList(wrapper);

            List<Catalog> catalog=new ArrayList<>();
            for (int i=0;i<sonlogs.size();i++){
                Long sonLogId = sonlogs.get(i).getSonLogId();

                Catalog catalog1 = selectCatalogName(sonLogId);
                catalog.add(catalog1);
            }
            return catalog;
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询目录失败请重试");
//        }
    }

    //修改目录名称
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String updateCatalogName(Long catalogId,String newName) {
        if (StringUtils.isBlank(newName)||newName==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"更新目录名：目录名不能为空");
        }
//        try {

            Catalog catalog=new Catalog();
            catalog.setCatalogName(newName);
            QueryWrapper<Catalog>wrapper=new QueryWrapper<>();
            wrapper.eq("catalog_id",catalogId);

            int update1 = catalogMapper.update(catalog, wrapper);

            if (update1!=0){
                return "更新目录名成功";
            }
            return "更新目录名失败";
//        }catch (Exception e){
////        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：修改目录失败请重试");
//        }
    }

    //删除目录
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String deleteCatalog(Long catalogId) {
        if (catalogId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"目录删除：目录id不能为空");
        }
//        try {

            QueryWrapper<Catalog> wrapper1 = new QueryWrapper<>();
            wrapper1
                    .eq("catalog_id",catalogId);
            catalogMapper.delete(wrapper1);

            QueryWrapper<Catalog> wrapper2 = new QueryWrapper<>();
            wrapper2
                    .eq("catalog_pid",catalogId);
            catalogMapper.delete(wrapper2);

            QueryWrapper<File> wrapper3 = new QueryWrapper<>();
            wrapper3
                    .eq("catalog_id",catalogId);
            fileMapper.delete(wrapper3);

            QueryWrapper<Sonlog> wrapper4 = new QueryWrapper<>();
            wrapper4
                    .eq("sonlog_id",catalogId);
            List<Sonlog> sonlogs = sonlogMapper.selectList(wrapper4);
            sonlogMapper.delete(wrapper4);
            //递归
            ArrayList<Long> arrayList = new ArrayList<>();
            for (int i=0;i<sonlogs.size();i++){
                Long sonLogId = sonlogs.get(i).getSonLogId();
                arrayList.add(sonLogId);
            }
            if (arrayList.size()!=0){
                for (int i=0;i<arrayList.size();i++){
                    Long id = arrayList.get(i);
                    deleteCatalog(id);
                }
            }
            return "删除目录成功";

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：删除目录失败请重试");
//        }
    }

    //文件内容插入数据库
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String insertFile(String fileName, Long catalog,Long userId) {
        if (StringUtils.isBlank(fileName)||catalog==null||userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"文件新增：目录名不能为空");
        }
//        try {

            File file = new File();
            long fileId = IdWorker.getId();
            file.setFileId(fileId);
            file.setCatalogId(catalog);
            file.setFileName(fileName);
            file.setUserId(userId);

            int insert = fileMapper.insert(file);
            if (insert!=0){
                return "插入成功";
            }
            return "插入失败";

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：插入文件失败请重试");
//        }
    }

    //tcp通信
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String insertTcpFile(Long userId,String fileName,String fileSize){
        if (StringUtils.isBlank(fileName)||StringUtils.isBlank(fileName)||userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"tcp：传递参数不完整");
        }

//        try {
        File file = new File();
        long fileId = IdWorker.getId();
        file.setFileId(fileId);
        file.setCatalogId(0L);
        file.setFileName(fileName);
        file.setFileSize(fileSize);
        file.setUserId(userId);

        int insert = fileMapper.insert(file);
        if (insert==1){
            return "true";
        }
        return "false";
//    }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：插入文件失败请重试");
//        }
    }

    //删除数据库中文件信息
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String deleteFile(String fileName,Long userId) {
        if (StringUtils.isBlank(fileName)||userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"删除文件：未得到删除的文件名或userId");
        }
//        try {

            QueryWrapper<File> wrapper = new QueryWrapper<>();
            wrapper
                    .eq("user_id",userId)
                    .eq("file_name",fileName);
            int delete = fileMapper.delete(wrapper);
            if (delete!=0){
                return "删除成功";
            }
            return null;
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：删除文件失败请重试");
//        }
    }

    //根据目录id查询目录name
    @Override
    public Catalog selectCatalogName(Long catalogId) {
        if (catalogId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"查询目录名：未得到目录id");
        }
//        try {

            QueryWrapper<Catalog> wrapper = new QueryWrapper<>();

            wrapper
                    .eq("catalog_id",catalogId);
            Catalog catalog = catalogMapper.selectOne(wrapper);

            return catalog;

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询目录名失败请重试");
//        }
    }

    //创建用户时 建立默认目录:全部
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String createDefaultCatalog(Long userId) {
        if (userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"创建初始目录：用户id为空");
        }
//        try {

            String catalogName="全部";
            Catalog catalog=new Catalog();
            Long id = 0L;
            catalog.setCatalogId(id);
            catalog.setCatalogName(catalogName);
            catalog.setUserId(userId);
            int insert = catalogMapper.insert(catalog);

            if (insert!=0){
                return "新建目录成功";
            }else {
                return "新建失败";
            }
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：新建初始目录失败请重试");
//        }
    }
    //文件分享
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String insertShare(Long userId,String fileName,Long sourceId) {
        if (StringUtils.isBlank(fileName)||userId==null||sourceId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"文件分享：传递参数不完整");
        }
//        try {

            Long id=IdWorker.getId();
            File file = new File();
            file.setFileId(id);
            file.setCatalogId(0L);
            file.setUserId(userId);
            file.setFileName(fileName);
            file.setFileSource(sourceId);
            int insert=fileMapper.insert(file);

            if (insert!=0){
                return "分享成功";
            }
            return "分享失败";

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：文件分享失败请重试");
//        }
    }

    //根据userId查询所有用户文件
    @Override
    public List<File> selectUserFiles(Long userId) {
        if (userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"查询所有用户文件：userId为空");
        }
//        try {

            QueryWrapper<File> wrapper = new QueryWrapper<>();
            wrapper
                    .eq("user_id",userId);

            List<File> files = fileMapper.selectList(wrapper);
            files.forEach(System.out::println);
            return files;

//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询文件失败请重试");
//        }
    }

    //查询目录下的子目录和文件
    @Override
    public R<JSONObject> selectFileAndCatalog(Long catalogId){
        Long userId = selectUser();
        if (catalogId==null||userId==null){
            throw new BusinessException(ResponeCode.ParamLost.value,"查询目录：传递参数不完整");
        }
//        try {
        List<File> files = selectFile(catalogId,userId);
        List<Catalog> sonlogs = selectSonlog(catalogId,userId);
        JSONObject data = new JSONObject();
        data.put("file",files);
        data.put("catalog",sonlogs);
            R<JSONObject> result = R.success(data);
            JSONObject json = JsonResultUtil.getJson(data);
        return result;
//    }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询目录失败请重试");
//        }
    }

    @Override
    public Long selectUser() {
//        try {
            Integer currentUserId = SecurityUserHolder.getCurrentUserId();
            Long userId= Long.valueOf(currentUserId);
            if (userId==null) {
                throw new BusinessException(ResponeCode.ParamLost.value, "查询用户：未查询成功");
            }
            return userId;
//        }catch (Exception e){
//            throw new BusinessException(ResponeCode.INTERNAL_SERVER_ERROR.value,"服务器错误：查询用户未成功请重试");
//        }
    }
}

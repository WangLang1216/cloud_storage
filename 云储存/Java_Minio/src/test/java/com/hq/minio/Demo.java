package com.hq.minio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hq.minio.dto.FileNameDto;
import com.hq.minio.entity.Catalog;
import com.hq.minio.entity.File;
import com.hq.minio.entity.Sonlog;
import com.hq.minio.entity.UserInfo;
import com.hq.minio.mapper.CatalogMapper;
import com.hq.minio.mapper.FileMapper;
import com.hq.minio.mapper.SonlogMapper;
import com.hq.minio.mapper.UserInfoMapper;
import com.hq.minio.utils.JsonResultUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author damon
 * @data 2023/8/4 15:35
 */
@SpringBootTest
public class Demo {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    SonlogMapper sonlogMapper;
    @Resource
    FileMapper fileMapper;
    @Resource
    CatalogMapper catalogMapper;

    @Test
    void test01(){
        List<FileNameDto> list=new ArrayList<>();
        FileNameDto fileNameDto = new FileNameDto("宝塔.txt");
        FileNameDto fileNameDto2 = new FileNameDto("12112.txt");
        list.add(fileNameDto);
        list.add(fileNameDto2);
        String json= JSON.toJSONString(list);
        System.out.println(json);

        List<String> files = new ArrayList<>();
        List<String> sonlogs = new ArrayList<>();
        files.add("a");
        files.add("b");
        sonlogs.add("A");
        sonlogs.add("B");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("file",files);
        jsonObject.put("catalog",sonlogs);
        JSONObject json1 = JsonResultUtil.getJson(jsonObject);
        String s = json1.toJSONString();
        System.out.println(s);

        Long userId=111L;
        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("userId",userId);
        jsonObject1.put("file",files);
        System.out.println(jsonObject1.toJSONString());

    }
    @Test
    void test02(){
        File file = new File();
        //乐观锁 修改version
        file=fileMapper.selectById(6);
        File file1=fileMapper.selectById(6);

        file.setFileName("jay1");
        file1.setFileName("jay2");
        //手动生成雪花id
//        Long id = IdWorker.getId();
//        System.out.println(id);
//        file.setFileId(id);

        fileMapper.updateById(file);
        fileMapper.updateById(file1);

    }
    @Test
    void test03(){
        Long catalogId=1L;
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog_id",catalogId);
        List<File> files = fileMapper.selectList(wrapper);
        files.forEach(System.out::println);
    }
    @Test
    void test04(){
        String catalogName="java";
        QueryWrapper<Catalog> wrapper = new QueryWrapper<>();
        wrapper.eq("catalog_name",catalogName);
        Long catalogId = catalogMapper.selectOne(wrapper).getCatalogId();
//        int delete = catalogMapper.delete(wrapper);

        QueryWrapper<Sonlog> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("catalog_id",catalogId);
        sonlogMapper.delete(wrapper1);

    }
    @Test
    void demo(){
        Long catalogId=333L;
        test05(catalogId);
    }

    @Test
    void test05(Long catalogId){
        //有bug 需修正
        QueryWrapper<Catalog> wrapper1 = new QueryWrapper<>();
        wrapper1
                .eq("catalog_id",catalogId);
        int delete2 = catalogMapper.delete(wrapper1);

        QueryWrapper<Catalog> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("dadlog_id",catalogId);
        int delete1 = catalogMapper.delete(wrapper2);


        QueryWrapper<File> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("catalog_id",catalogId);
        int delete = fileMapper.delete(wrapper3);

        QueryWrapper<Sonlog> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("catalog_id",catalogId);
        List<Sonlog> sonlogs = sonlogMapper.selectList(wrapper4);
        int delete3 = sonlogMapper.delete(wrapper4);
        ArrayList<Long> arrayList = new ArrayList<>();
        for (int i=0;i<sonlogs.size();i++){
            Long sonLogId = sonlogs.get(i).getSonLogId();
            arrayList.add(sonLogId);
        }
        if (arrayList.size()!=0){
            for (int i=0;i<arrayList.size();i++){
                Long id = arrayList.get(i);
                test05(id);
            }
        }
    }
    @Test
    void test06(){
        File file = new File();
        long fileId = IdWorker.getId();
        file.setFileId(333L);
        file.setCatalogId(333L);
        file.setFileName("abc");
        int insert = fileMapper.insert(file);
        if (insert!=0){
            System.out.println("插入成功");
        }

    }
    @Test
    void test07(){


    }


}

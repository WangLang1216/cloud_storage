package com.hq.minio;

/**
 * @author damon
 * @data 2023/8/22 10:14
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import com.hq.minio.dto.tcp.*;
import com.hq.minio.entity.File;
import com.hq.minio.entity.UserInfo;
import com.hq.minio.mapper.UserInfoMapper;
import com.hq.minio.mapper.UserRoleMapper;
import com.hq.minio.service.FileService;
import com.hq.minio.service.UserInfoService;
import com.hq.minio.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


/**
 * nio socket服务端
 */
@Slf4j
@Component
public class SocketServer implements CommandLineRunner {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private FileService fileService;
    @Resource
    private MinioUtils minioUtils;
    @Resource
    private UserInfoService userInfoService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Resource
    private UserRoleMapper userRoleMapper;
    // 端口号
    int port=1111;
    // 解码buffer
    private Charset cs = Charset.forName("UTF-8");

    // 接受数据缓冲区
    private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);

    // 发送数据缓冲区
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);

    // 选择器
    private static Selector selector;

    // 响应消息
    String responseMsg;

    /**
     * 启动socket服务，开启监听
     *
     */
    @Override
    public void run(String... args) throws Exception {
//    public void startSocketServer(){
        try {
            // 打开通信通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 获取套接字
            ServerSocket serverSocket = serverSocketChannel.socket();
            // 绑定端口号
            serverSocket.bind(new InetSocketAddress(port));
            // 打开监听
            selector = Selector.open();
            // 将通信信道注册到监听器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,null);

//                SocketChannel socketChannel = serverSocketChannel.accept();
//                socketChannel.configureBlocking(false);
//                socketChannel.register(selector, SelectionKey.OP_READ);
//                System.out.println("客户端连接成功");

            // 监听器一直监听，如果客户端有请求就会进入响应的时间处理
            while (true) {
                selector.select(); // select()一直阻塞直到相关事件发生或超时
                Set<SelectionKey> selectionKeys = selector.selectedKeys(); // 监听到事件
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    handle(key);
                }
//                for (SelectionKey key : selectionKeys) {
//                    handle(key);
//                }
                selectionKeys.clear(); // 清除处理过的事件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//    }
    }
    /**
     * 处理不同事件
     *
     * @param selectionKey
     * @throws IOException
     */
    private void handle(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        String requestMsg = "";
        int count = 0;


        try {
            if (selectionKey.isAcceptable()) {
                // 每有客户端连接，即注册通信信道为可读
                serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                System.out.println("客户端连接成功");
            }
            else if (selectionKey.isReadable()) {
                socketChannel = (SocketChannel) selectionKey.channel();
                rBuffer.clear();
                count = socketChannel.read(rBuffer);
                // 读取数据
//                if (count == -1) {
//                    selectionKey.cancel();
//                    socketChannel.close();
//                }
                if (count > 0) {
                    rBuffer.flip();
                    requestMsg = String.valueOf(cs.decode(rBuffer).array());
                    System.out.println("requestMsg:"+requestMsg);
//                    log.debug("requestMsg:{}",requestMsg);

                    OrderDto orderDto = JSON.parseObject(requestMsg, OrderDto.class);
                    String order = orderDto.getOrder();

                    if (!("View".equals(order) )&& !("Upload".equals(order))) {
                        LoginDto loginDto = JSON.parseObject(requestMsg, LoginDto.class);

                        if (loginDto.getOrder().equals("Login")) {
                            ResultDto resultDto = userLogin(loginDto.getUserName(), loginDto.getPassword());
                            responseMsg = JSON.toJSONString(resultDto);
                            System.out.println("responseMsg:"+responseMsg);
                        } else if (loginDto.getOrder().equals("RegisterVerify")) {
                            ResultDto resultDto = userRegisterVerify(loginDto.getUserName());
                            responseMsg = JSON.toJSONString(resultDto);
                            System.out.println("responseMsg:"+responseMsg);
                        } else if (loginDto.getOrder().equals("Register")) {

                            ResultDto resultDto = userRegister(loginDto.getUserName(), loginDto.getPassword());
                            responseMsg = JSON.toJSONString(resultDto);
                            System.out.println("responseMsg:"+responseMsg);
                        }
                    }
                    if (("View").equals(order)) {
                            log.debug("view");
                            ContentDto contentDto = JSON.parseObject(requestMsg, ContentDto.class);
                            String content = contentDto.getContent();
                            List<FileDto> fileDtos = fileView(content);
                            for (FileDto f : fileDtos
                            ) {
                                responseMsg = JSON.toJSONString(f);
                                sBuffer = ByteBuffer.allocate(responseMsg.getBytes().length);
                                sBuffer.put(responseMsg.getBytes());
                                sBuffer.flip();

                                socketChannel.write(sBuffer);
//                                socketChannel.close();
                                sBuffer.clear();
                                TimeUnit.MILLISECONDS.sleep(100);
                            }
                            return;
                        }

                    log.debug("order:{}",order);
                    if (("Upload").equals(order)){

                            FileDto fileDto = JSON.parseObject(requestMsg, FileDto.class);
                            ContentDto contentDto = uploadFile(fileDto);
                            responseMsg = JSON.toJSONString(contentDto);
                            System.out.println("responseMsg:"+responseMsg);

                        }


                    // 返回数据
                    sBuffer = ByteBuffer.allocate(responseMsg.getBytes().length);
                    sBuffer.put(responseMsg.getBytes());
                    sBuffer.flip();

                    socketChannel.write(sBuffer);
//                    socketChannel.close();
                    sBuffer.clear();
                }

            }

        }catch (Exception e){
            selectionKey.cancel();
            socketChannel.close();
        }
    }

    public ResultDto userLogin(String userName,String userPassword){
        System.out.println(userName+":"+userPassword);
        ResultDto resultDto = new ResultDto();
        resultDto.setUserName(userName);
        resultDto.setPassword(userPassword);
        resultDto.setOrder("Login");


        System.out.println("userInfo");
        // 根据账户名查询
        UserInfo userInfo = userInfoMapper.userLogin(userName);
        System.out.println("pwd:"+userInfo.getuPassword());
        if (userInfo == null || userInfo.getuId() == null){
            resultDto.setResult("false");
            return resultDto;
        }

        boolean isPwd = encoder.matches(userPassword, userInfo.getuPassword());
        System.out.println("isPwd:"+isPwd);
        if (!isPwd){
            resultDto.setResult("false");
            return resultDto;
        }

//        String s = userInfoService.userLogin(userName, userPassword);
//        if (s.isEmpty()) {
//            resultDto.setResult("false");
//        }else {
//            resultDto.setResult("true");
//        }
        resultDto.setResult("true");
        return resultDto;
    }

    public ResultDto userRegisterVerify(String userName){
        ResultDto resultDto = new ResultDto();
        resultDto.setUserName(userName);
        resultDto.setOrder("RegisterVerify");
        UserInfo userInfo = userInfoMapper.queryUserInfoByName(userName);
        if (userInfo == null || userInfo.getuName() == null || userInfo.getuName().equals("")) {
            resultDto.setResult("true");
        }else {
            resultDto.setResult("false");
        }

        return resultDto;
    }

    public ResultDto userRegister(String userName,String userPassword){
        ResultDto resultDto = new ResultDto();
        resultDto.setUserName(userName);
        resultDto.setPassword(userPassword);
        resultDto.setOrder("Register");
//        UserInfo userInfo2 = userInfoMapper.queryUserInfoByName(userName);
//        if (userInfo2!=null||userInfo2.getuId()!=null){
//            resultDto.setResult("false");
//            return resultDto;
//        }

        UserInfo userInfo = new UserInfo();
        userInfo.setuName(userName);
        userInfo.setuPassword(userPassword);

        // 加密
        userInfo.setuPassword(encoder.encode(userInfo.getuPassword()));
        // 创建账户
        userInfoMapper.userRegister(userInfo);
        // 创建角色
        userRoleMapper.addUserRole(userInfo.getuName(), userInfo.getuPassword());
        // 初始存储信息
        UserInfo userInfo1 = userInfoService.queryUserInfoByName(userInfo.getuName());
        Long userId = userInfo1.getuId();
        fileService.createDefaultCatalog(userId);
        minioUtils.existBucket(userId.toString());
        resultDto.setResult("true");
//        if (i==1){
//            UserInfo userInfo1 = userInfoMapper.userLogin(userName);
//            Long aLong = userInfo1.getuId();
//            String bucketName=String.valueOf(aLong);
//            minioUtils.existBucket(bucketName);
//            resultDto.setResult("true");
//        }else {
//            resultDto.setResult("false");
//        }
        return resultDto;
    }

    public List<FileDto> fileView(String userName){

        UserInfo userInfo = userInfoMapper.queryUserInfoByName(userName);

        Long userId = userInfo.getuId();

        List<File> files = fileService.selectUserFiles(userId);
        List<FileDto>fileDtos=new ArrayList<>();
        for (File f:files
             ) {
            FileDto fileDto = new FileDto();
            fileDto.setOrder("View");
            fileDto.setUserName(userName);
            fileDto.setFileName(f.getFileName());
            fileDto.setFileSize(f.getFileSize());
            fileDto.setCreateTime(f.getGmtCreate());
            fileDtos.add(fileDto);
        }

        return fileDtos;
    }

    public  ContentDto uploadFile(FileDto fileDto){
        ContentDto contentDto = new ContentDto();
        contentDto.setOrder("Upload");


        String userName = fileDto.getUserName();
        UserInfo userInfo = userInfoMapper.queryUserInfoByName(userName);
        Long userId = userInfo.getuId();
        System.out.println(userId);

        String s = fileService.insertTcpFile(userId, fileDto.getFileName(), fileDto.getFileSize());
        if (s.equals("true")){
            contentDto.setContent("true");
        }else {
            contentDto.setContent("false");
        }
        return contentDto;
    }


}

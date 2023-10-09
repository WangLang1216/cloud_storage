package com.hq.minio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author damon
 * @data 2023/8/3 10:28
 */
@SpringBootApplication
@MapperScan("com.hq.minio.mapper")

public class MinioApplication {


    public static void main(String[] args) {
//        SocketServer server = new SocketServer();
//        server.startSocketServer();
        SpringApplication.run(MinioApplication.class,args);
    }
}

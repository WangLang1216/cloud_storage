package com.hq.minio.dto.tcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author damon
 * @data 2023/8/23 10:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private String order;
    private String userName;
    private String password;
    private String fileName;
    private String fileSize;
    private Date createTime;
    private String content;
}

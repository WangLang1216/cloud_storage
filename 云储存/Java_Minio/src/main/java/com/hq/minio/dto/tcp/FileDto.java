package com.hq.minio.dto.tcp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author damon
 * @data 2023/8/23 10:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileDto {
    private String order;
    private String userName;
    private String fileName;
    private String fileSize;
    private Date createTime;
}

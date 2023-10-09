package com.hq.minio.entity;

import lombok.Data;

/**
 * @author damon
 * @data 2023/8/4 14:00
 */
@Data
public class ObjectItem {
    private String objectName;
    private Long size;
}

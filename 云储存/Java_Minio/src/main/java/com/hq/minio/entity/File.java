package com.hq.minio.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author damon
 * @data 2023/8/7 9:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file")
public class File {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "file_name")
    private String fileName;
    @TableField(value = "file_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileId;
    @TableField(value = "catalog_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long catalogId;
    @TableField(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    @TableField(value = "file_size")
    private String fileSize;
    @TableField(value = "file_unit")
    private String fileUnit;
    @TableField(value = "file_type")
    private String fileType;
    @TableField(value = "file_source")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileSource;
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;
    @Version
    private Integer version;




}

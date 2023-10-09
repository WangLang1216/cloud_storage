package com.hq.minio.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Date;

/**
 * @author damon
 * @data 2023/8/7 9:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("catalog")
public class Catalog {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "catalog_name")
    private String catalogName;
    @TableField(value = "catalog_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long catalogId;
    @TableField(value = "catalog_pid")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dadLogId;
    @TableField(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    @TableField(value = "catalog_level")
    private Long catalogLevel;
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;

}

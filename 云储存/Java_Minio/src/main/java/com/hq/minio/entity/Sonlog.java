package com.hq.minio.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author damon
 * @data 2023/8/8 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sonlog")
public class Sonlog {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "sonlog_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sonLogId;
    @TableField(value = "catalog_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long catalogId;
    @TableField(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

}

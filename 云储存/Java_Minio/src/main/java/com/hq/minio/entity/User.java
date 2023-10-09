package com.hq.minio.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author damon
 * @data 2023/8/8 9:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class User {

    @TableId(type = IdType.AUTO,value = "u_id")
    private Long id;
    @TableField(value = "u_name")
    private String uName;
    @TableField(value = "u_password")
    private String uPassword;
    @TableField(value = "u_state")
    private Long uState;
    @TableField(value = "gmt_create",fill = FieldFill.INSERT)
    private Date gmtCreate;
    @TableField(value = "gmt_modified",fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;



}

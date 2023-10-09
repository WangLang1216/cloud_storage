package com.hq.minio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Role {
    //todo 字段名称要写注释，不要随意简写英文
    private Long rId;

    private String rName;

    private String rDesc;

}

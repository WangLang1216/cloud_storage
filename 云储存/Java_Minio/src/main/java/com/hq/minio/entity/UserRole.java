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
public class UserRole {

    private Long urId;

    private Long uId;

    private Long rId;

}

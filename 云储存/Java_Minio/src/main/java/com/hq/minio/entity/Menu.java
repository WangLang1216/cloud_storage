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
public class Menu {

    private Long mId;

    private String mName;

    private String mDesc;

    private int mLevel;

    private Long mPid;

    private String mPath;

    private int mState;

}

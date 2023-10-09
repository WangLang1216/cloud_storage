package com.hq.minio.utils;

import java.util.UUID;

/**
 * @author damon
 * @data 2023/8/15 11:37
 */

public class IDCommon {
    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

package com.hq.minio.exception;

import com.hq.minio.utils.R;

public class RespGenerator {

    // 正常调用返回
    public static R success(Object data) {
        return new R(BaseErrorEnum.SUCCESS.getCode(), "接口调用成功！", data);
    }

    // 失败调用，入参枚举
    public static R<Object> fail(BaseErrorEnum errorEnum) {
        return new R<>(errorEnum.getCode(), errorEnum.getMsg(), null);
    }

    // 失败调用，提供GlobalExceptionHandler使用
    public static R<Object> fail(Integer code, String msg) {
        return new R<>(code, msg, null);
    }
}

package com.hq.minio.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException{

    private Integer code;

    private String msg;

    public BaseException() {
        super();
    }

    public BaseException(BaseErrorEnum baseErrorEnum) {
        super(baseErrorEnum.getCode().toString());
        this.code = baseErrorEnum.getCode();
        this.msg = baseErrorEnum.getMsg();
    }
}

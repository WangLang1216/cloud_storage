package com.hq.minio.exception;

public enum BaseErrorEnum implements BaseErrorInfoInterface{

    SUCCESS(200, "成功"),
    BODY_NOT_MATCH(400, "数据格式不匹配"),
    NOT_FOUND(404, "访问资源不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    USER_NOT_EXISTS(-1, "该用户不存在"),
    PASSWORD_ERROR(-1, "密码错误");


    private Integer code;

    private String msg;

    BaseErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

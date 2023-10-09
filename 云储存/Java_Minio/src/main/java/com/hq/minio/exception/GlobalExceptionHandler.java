package com.hq.minio.exception;

import com.hq.minio.controller.UserInfoController;
import com.hq.minio.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {UserInfoController.class})
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 处理自定义异常
    @ExceptionHandler(value = BaseException.class)
    public R<Object> baseExceptionHandler(BaseException e) {
        logger.error("发生业务异常：" + e.getMsg());
        return RespGenerator.fail(e.getCode(), e.getMsg());
    }

    // 空指针
    @ExceptionHandler(value = NullPointerException.class)
    public R<Object> exceptionHandler(NullPointerException e) {
        logger.error("发生空指针异常：" + e);
        return RespGenerator.fail(BaseErrorEnum.BODY_NOT_MATCH);
    }

    // 其他异常
    @ExceptionHandler(value = Exception.class)
    public R<Object> exceptionHandler(Exception e) {
        logger.error("未知异常：" + e);
        return RespGenerator.fail(BaseErrorEnum.INTERNAL_SERVER_ERROR);
    }

    // 运行时异常
    @ExceptionHandler(value = RuntimeException.class)
    public R<Object> exceptionHandler(RuntimeException e){
        logger.error("未知异常：" + e);
        return RespGenerator.fail(BaseErrorEnum.INTERNAL_SERVER_ERROR);
    }
}

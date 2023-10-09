package com.hq.minio.advice;

import com.alibaba.fastjson.JSONObject;
import com.hq.minio.controller.FileController;
import com.hq.minio.controller.MinioController;
import com.hq.minio.utils.JsonResultUtil;
import com.hq.minio.utils.ResponeCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author damon
 * @data 2023/9/7 14:03
 */

@RestControllerAdvice(assignableTypes = {FileController.class, MinioController.class})
@Slf4j
@SuppressWarnings("ALL")
public class GlobalExceptionHandler2 {

    /**
     * 处理运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public JSONObject handlerRuntimeException(RuntimeException e){
        String message = e.getMessage();
        log.error("RuntimeException:{}",message);
        return JsonResultUtil.getJson(ResponeCode.FAIL.value,null,message);
    }

    /**
     * 处理所有未知异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public JSONObject handlerException(Exception e){
        String message = e.getMessage();
        log.error("Exception:{}",message);
        return JsonResultUtil.getJson(ResponeCode.INTERNAL_SERVER_ERROR.value,null,"server error:"+message);
    }

    /**
     * 处理所有业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public JSONObject handlerBusinessException(BusinessException e){
        String message = e.getMessage();
        log.error("BusinessException:{}",message);
        return JsonResultUtil.getJson(e.getCode(),null,message);
    }


}

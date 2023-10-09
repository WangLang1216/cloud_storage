package com.hq.minio.advice;

import com.hq.minio.exception.BaseErrorEnum;
import com.hq.minio.utils.ResponeCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author damon
 * @data 2023/9/7 14:04
 */
@Data
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    private Integer code;


    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }



}
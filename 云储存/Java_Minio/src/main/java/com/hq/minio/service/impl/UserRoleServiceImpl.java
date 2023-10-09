package com.hq.minio.service.impl;


import com.hq.minio.exception.BaseErrorEnum;
import com.hq.minio.exception.BaseException;
import com.hq.minio.exception.GlobalExceptionHandler;
import com.hq.minio.mapper.UserRoleMapper;
import com.hq.minio.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Integer updateUserRoleById(String uId, String rId) {
        if ("".equals(uId) || "".equals(rId)) {
            throw new BaseException(BaseErrorEnum.BODY_NOT_MATCH);
        }
        return userRoleMapper.updateUserRoleById(uId, rId);
    }
}

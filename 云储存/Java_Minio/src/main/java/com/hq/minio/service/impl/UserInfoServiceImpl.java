package com.hq.minio.service.impl;


import com.hq.minio.dto.UserInfoRole;
import com.hq.minio.entity.UserInfo;
import com.hq.minio.exception.BaseErrorEnum;
import com.hq.minio.exception.BaseException;
import com.hq.minio.exception.GlobalExceptionHandler;
import com.hq.minio.mapper.UserInfoMapper;
import com.hq.minio.mapper.UserRoleMapper;
import com.hq.minio.service.FileService;
import com.hq.minio.service.UserInfoService;
import com.hq.minio.utils.JwtTokenUtil;
import com.hq.minio.utils.MinioUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private FileService fileService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    @Transactional
    public Boolean userRegister(UserInfo userInfo) {
        if (userInfo == null || "".equals(userInfo.getuName()) || "".equals(userInfo.getuPassword())) {
            throw new BaseException(BaseErrorEnum.BODY_NOT_MATCH);
        }
        // 加密
        userInfo.setuPassword(encoder.encode(userInfo.getuPassword()));
        // 创建账户
        userInfoMapper.userRegister(userInfo);
        logger.info("创建用户成功：" + userInfo);
        // 创建角色
        userRoleMapper.addUserRole(userInfo.getuName(), userInfo.getuPassword());
        logger.info("创建角色成功：" + userInfo);
        // 初始存储信息
        UserInfo userInfo1 = queryUserInfoByName(userInfo.getuName());
        Long userId = userInfo1.getuId();
        fileService.createDefaultCatalog(userId);
        minioUtils.existBucket(userId.toString());
        logger.info("创建桶成功：" + userId);
        return true;
    }

    @SneakyThrows
    @Override
    public String userLogin(String uName, String uPassword) {
        String token = "";
        if (StringUtils.isEmpty(uName) || StringUtils.isEmpty(uPassword)) {
            throw new BaseException(BaseErrorEnum.BODY_NOT_MATCH);
        }
        // 根据账户名查询
        UserInfo userInfo = null;
        userInfo = userInfoMapper.userLogin(uName);
        // 未查询到改用户
        if (userInfo == null || "".equals(userInfo.getuName())) {
            throw new BaseException(BaseErrorEnum.USER_NOT_EXISTS);
        }
        // 解密对比
        boolean isPwd = encoder.matches(uPassword, userInfo.getuPassword());
        if (!isPwd) {
            throw new BaseException(BaseErrorEnum.PASSWORD_ERROR);
        }
        // 设置token
        token = JwtTokenUtil.createJwt(userInfo);
        return token;
    }

    @Override
    public List<UserInfoRole> userInfoList() {
        return userInfoMapper.userInfoList();
    }

    @Override
    public Boolean userInfoUpdate(UserInfo userInfo) {
        if (userInfo.getuId() == null) {
            throw new BaseException(BaseErrorEnum.BODY_NOT_MATCH);
        }
        return userInfoMapper.userInfoUpdate(userInfo) > 0;
    }

    @Override
    public Integer userDeleteById(String uId, String uState) {
        if ("".equals(uId)) {
            throw new BaseException(BaseErrorEnum.USER_NOT_EXISTS);
        }
        if (!"1".equals(uState) && !"0".equals(uState)) {
            throw new BaseException(BaseErrorEnum.BODY_NOT_MATCH);
        }
        return userInfoMapper.userDeleteById(uId, uState);
    }

    @Override
    public UserInfo queryUserInfoByName(String uName) {
        if ("".equals(uName)) {
            throw new BaseException(BaseErrorEnum.USER_NOT_EXISTS);
        }
        return userInfoMapper.queryUserInfoByName(uName);
    }

    @Override
    public UserInfo queryUserInfoById(String uId) {
        if ("".equals(uId)) {
            throw new BaseException(BaseErrorEnum.USER_NOT_EXISTS);
        }
        return userInfoMapper.queryUserInfoById(uId);
    }

    @SneakyThrows
    @Override
    public UserInfo queryUserNameByToken(String token) {
        if ("".equals(token)) {
            throw new BaseException(BaseErrorEnum.BODY_NOT_MATCH);
        }
        UserInfo userInfo = null;
        userInfo = JwtTokenUtil.getUserIdByToken(token);
        return userInfo;
    }

    @Override
    public Boolean queryUserByName(String uName) {
        if ("".equals(uName)) {
            throw new BaseException(BaseErrorEnum.USER_NOT_EXISTS);
        }
        UserInfo userInfo = null;
        userInfo = userInfoMapper.queryUserInfoByName(uName);
        return userInfo == null;
    }
}

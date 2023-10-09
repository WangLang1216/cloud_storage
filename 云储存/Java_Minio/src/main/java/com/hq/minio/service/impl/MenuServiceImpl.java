package com.hq.minio.service.impl;


import com.hq.minio.entity.Menu;
import com.hq.minio.entity.UserInfo;
import com.hq.minio.exception.BaseErrorEnum;
import com.hq.minio.exception.BaseException;
import com.hq.minio.exception.GlobalExceptionHandler;
import com.hq.minio.mapper.MenuMapper;
import com.hq.minio.service.MenuService;
import com.hq.minio.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserInfoService userInfoService;

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public List<Menu> queryMenuById(String token) {
        if ("".equals(token)) {
            throw new BaseException(BaseErrorEnum.BODY_NOT_MATCH);
        }
        UserInfo userInfo = userInfoService.queryUserNameByToken(token);
        logger.info("信息查询成功：" + userInfo);
        return menuMapper.queryMenuById(userInfo.getuId().toString());
    }
}

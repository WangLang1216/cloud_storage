package com.hq.minio.service.impl;


import com.hq.minio.entity.Role;
import com.hq.minio.exception.BaseErrorEnum;
import com.hq.minio.exception.BaseException;
import com.hq.minio.exception.GlobalExceptionHandler;
import com.hq.minio.mapper.RoleMapper;
import com.hq.minio.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> queryRoleList() {
        return roleMapper.queryRoleList();
    }
}

package com.hq.minio.service;



import com.hq.minio.entity.Role;

import java.util.List;

public interface RoleService {

    /**
     * 查询角色信息
     * @return List
     */
    List<Role> queryRoleList();

}

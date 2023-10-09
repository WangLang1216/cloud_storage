package com.hq.minio.mapper;


import com.hq.minio.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Component("roleMapper")
@Repository
public interface RoleMapper {

    /**
     * 查询角色信息
     * @return List
     */
    public List<Role> queryRoleList();

}

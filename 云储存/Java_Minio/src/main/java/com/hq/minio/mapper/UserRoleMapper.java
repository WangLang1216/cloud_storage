package com.hq.minio.mapper;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//@Component("userRoleMapper")
@Repository
public interface UserRoleMapper {


    /**
     * 创建用户角色，初始为普通用户
     */
    //todo 接口方法默认为public 不用加
    public int addUserRole(String uName, String uPassword);

    /**
     * 修改用户角色，更改权限
     * @param uId
     * @param rId
     * @return int
     */
    public int updateUserRoleById(String uId, String rId);

}

package com.hq.minio.service;


public interface UserRoleService {

    /**
     * 修改用户角色，更改权限
     * @param uId
     * @param rId
     * @return Integer
     */
    Integer updateUserRoleById(String uId, String rId);

}

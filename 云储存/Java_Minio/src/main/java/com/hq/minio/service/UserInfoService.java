package com.hq.minio.service;



import com.hq.minio.dto.UserInfoRole;
import com.hq.minio.entity.UserInfo;

import java.util.List;

public interface UserInfoService {

    /**
     * 用户注册
     * @param userInfo
     * @return Boolean
     */
    Boolean userRegister(UserInfo userInfo);

    /**
     * 用户登录
     * @param uName
     * @return String
     */
    String userLogin(String uName, String uPassword);

    /**
     * 用户列表查看
     * @return List
     */
    List<UserInfoRole> userInfoList();

    /**
     * 用户信息修改
     * @param userInfo
     * @return Boolean
     */
    Boolean userInfoUpdate(UserInfo userInfo);

    /**
     * 用户删除
     * @param uId
     * @return Integer
     */
    Integer userDeleteById(String uId, String uState);

    /**
     * 根据用户用户名查询
     * @param uName
     * @return UserInfo
     */
    UserInfo queryUserInfoByName(String uName);

    /**
     * 根据id查询用户
     * @param uId
     * @return UserInfo
     */
    UserInfo queryUserInfoById(String uId);

    /**
     * 根据Token查询用户信息
     * @param token
     * @return UserInfo
     */
    UserInfo queryUserNameByToken(String token);

    /**
     * 根据用户账号查询用户
     * @param uName
     * @return Boolean
     */
    Boolean queryUserByName(String uName);

}

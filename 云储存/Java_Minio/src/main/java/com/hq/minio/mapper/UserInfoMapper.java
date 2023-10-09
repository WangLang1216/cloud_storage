package com.hq.minio.mapper;


import com.hq.minio.dto.UserInfoRole;
import com.hq.minio.entity.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Component("userInfoMapper")// 用于处理service.impl注入显示下红线
@Repository
public interface UserInfoMapper {

    /**
     * 用户注册
     * @param userInfo
     * @return int
     */
    int userRegister(UserInfo userInfo);

    /**
     * 用户登录
     * @param uName
     * @return UserInfo
     */
    UserInfo userLogin(String uName);

    /**
     * 用户列表查看
     * @return List
     */
    List<UserInfoRole> userInfoList();

    /**
     * 用户信息修改
     * @param userInfo
     * @return int
     */
    int userInfoUpdate(UserInfo userInfo);

    /**
     * 用户删除
     * @param uId
     * @return int
     */
    int userDeleteById(String uId, String uState);

    /**
     * 根据用户用户名查询
     * @param uName
     * @return
     */
    UserInfo queryUserInfoByName(String uName);

    /**
     * 根据id查询用户
     * @param uId
     * @return UserInfo
     */
    UserInfo queryUserInfoById(String uId);
}

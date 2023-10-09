package com.hq.minio.service;



import com.hq.minio.entity.Menu;

import java.util.List;

public interface MenuService {

    /**
     * 根据用户id查询菜单信息
     * @param uId
     * @return List
     */
    List<Menu> queryMenuById(String uId);

}

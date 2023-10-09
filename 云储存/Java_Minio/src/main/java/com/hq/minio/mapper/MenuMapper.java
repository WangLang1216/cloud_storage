package com.hq.minio.mapper;


import com.hq.minio.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Component("menuMapper")
@Repository
public interface MenuMapper {

    /**
     * 根据用户id查询菜单信息
     * @param uId
     * @return
     */
    public List<Menu> queryMenuById(String uId);

}

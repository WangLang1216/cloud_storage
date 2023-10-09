package com.hq.minio.controller;


import com.hq.minio.dto.UserInfoRole;
import com.hq.minio.entity.Menu;
import com.hq.minio.entity.Role;
import com.hq.minio.entity.UserInfo;
import com.hq.minio.service.MenuService;
import com.hq.minio.service.RoleService;
import com.hq.minio.service.UserInfoService;
import com.hq.minio.service.UserRoleService;

import com.hq.minio.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@CrossOrigin
@RestController
//@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    // 返回成功
    private static final Integer SUCCESS = 1;
    // 返回失败
    private static final Integer FAIL = 0;

    /**
     * 用户注册
     * @param userInfo
     * @return int
     */
    @PostMapping("/register")
    public R<String> userRegister(@RequestBody @Validated(UserInfo.class) UserInfo userInfo) {
        boolean result = userInfoService.userRegister(userInfo);
        return result ? R.success("注册成功") : R.error("账户注册失败或已存在");
    }

    /**
     * 用户登录
     * @param userInfo
     * @return String
     */
    @PostMapping("/login")
        public R<String> userLogin(@RequestBody @Validated(UserInfo.class) UserInfo userInfo) {
            String result = userInfoService.userLogin(userInfo.getuName(), userInfo.getuPassword());
            return result.equals("") ? R.error("用户名或密码不正确") : R.success(result).setMsg("登录成功");
    }

    /**
     * 用户注销
     * @return String
     */
    @DeleteMapping("/logout")
    public R<String> userLogOffByName() {
        // 返回错误的token
        return R.success("");
    }

    /**
     * 用户列表查询，同时返回用户角色信息
     * @return List
     */
    @GetMapping("/users")
    public R<List<UserInfoRole>> userInfoList() {
        List<UserInfoRole> list = userInfoService.userInfoList();
        return list.size() == FAIL ? R.error("查询失败") : R.success(list).setMsg("查询成功");
    }

    /**
     * 修改用户信息
     * @param userInfo
     * @return String
     */
    @PutMapping("/users/{uId}")
    public R<String> userInfoUpdate(@PathVariable String uId , @RequestBody @Validated(UserInfo.class) UserInfo userInfo) {
        return userInfoService.userInfoUpdate(userInfo) ? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 用户状态修改，逻辑删除
     * @param uId
     * @param uState
     * @return String
     */
    @PatchMapping("/users/{uId}/{uState}")
    public R<String> userDeleteById(@PathVariable String uId, @PathVariable String uState) {
        int r = userInfoService.userDeleteById(uId, uState);
        return r == SUCCESS ? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 通过token返回用户名
     * @param request
     * @return String
     */
    @GetMapping("/userName")
    public R<String> queryUserNameByToken(HttpServletRequest request) {
        UserInfo userInfo = userInfoService.queryUserNameByToken(request.getHeader("Token"));
        return userInfo == null ? R.error("查询失败") : R.success(userInfo.getuName());
    }

/**
 * 根据用户令牌查询菜单
 * @param request
 * @return List
 */
    @GetMapping("/menus")
    public R<List<Menu>> queryMenuById(HttpServletRequest request) {
        List<Menu> list = menuService.queryMenuById(request.getHeader("Token"));
        return list.size() == FAIL ? R.error("查询失败") : R.success(list).setMsg("查询成功");
    }

    /**
     * 角色信息查询
     * @return List
     */
    @GetMapping("/roles")
    public R<List<Role>> queryRoleList() {
        List<Role> list = roleService.queryRoleList();
        return list.size() == FAIL ? R.error("查询失败") : R.success(list).setMsg("查询成功");
    }

    /**
     * 用户角色修改
     * @param uId
     * @param rId
     * @return String
     */
    @PatchMapping("/roles/{uId}/{rId}")
    public R<String> updateUserRoleById(@PathVariable String uId, @PathVariable String rId){
        int r = userRoleService.updateUserRoleById(uId, rId);
        return r == FAIL ? R.error("修改失败") : R.success("修改成功");
    }

    /**
     * 根据账号名判断该账号是否存在
     * @param uName
     * @return Boolean
     */
    @GetMapping("/userName/{uName}")
    public R<Boolean> queryUserNameByName(@PathVariable String uName) {
        return userInfoService.queryUserByName(uName) ? R.success(true) : R.error("账号已存在");
    }
}

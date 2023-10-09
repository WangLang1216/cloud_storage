import { get, post, getDynamicynamic, put, Delete, getFileUseBlobByPost } from './index'

// 用户登录
export const userLogin = p => post('user/userLogin', p);

// 用户注册
export const userRegister = p => post('user/userRegister', p);

// 根据token获取用户账户
export const queryUserNameById = () => get('user/queryUserNameById');

// 注销
export const userLogOffByName = () => Delete('user/userLogOffByName');

// 用户列表查询，同时返回用户角色信息
export const userInfoList = () => get('user/userInfoList');

// 修改用户信息
export const userInfoUpdate = p => post('user/userInfoUpdate', p);

// 用户状态修改，逻辑删除
export const userDeleteById = p => put('user/userDeleteById', p);

// 获取菜单列表
export const queryMenuById = () => get('user/queryMenuById');

// 角色信息查询
export const queryRoleList = () => get('user/queryRoleList');

// 用户角色修改
export const updateUserRoleById = p => getDynamicynamic('user/updateUserRoleById', p);


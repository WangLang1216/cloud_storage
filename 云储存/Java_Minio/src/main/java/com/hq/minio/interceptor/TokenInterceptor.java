package com.hq.minio.interceptor;


import com.hq.minio.entity.UserInfo;
import com.hq.minio.service.UserInfoService;
import com.hq.minio.utils.JwtTokenUtil;
import com.hq.minio.utils.SecurityUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {//这里通过判断请求的方法，判断此次是否是预检请求，如果是，立即返回一个204状态吗，标示，允许跨域；预检后，正式请求，这个方法参数就是我们设置的post了
            response.setStatus(HttpStatus.NO_CONTENT.value()); //HttpStatus.SC_NO_CONTENT = 204
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");//当判定为预检请求后，设定允许请求的方法
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, Token"); //当判定为预检请求后，设定允许请求的头部类型
            response.addHeader("Access-Control-Max-Age", "1");
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 从 http 请求头中取出 token
        String token = request.getHeader("Token");
        if (StringUtils.isEmpty(token)) {
            SecurityUserHolder.removeCurrentUser();
            // 用户未登录
            return false;
        }

        String userId = "";
        if (!StringUtils.isEmpty(token)) {
            // 请求头中没有token
            if ("".equals(token)) {
                return false;
            }
            // token已过期
            boolean verity = JwtTokenUtil.verify(token);
            if (!verity) {
                return false;
            }
            // 获取用户id
            userId = JwtTokenUtil.getUserIdByToken(token).getuId().toString();
        }
        UserInfo user = userInfoService.queryUserInfoById(userId);
        // 用户不存在
        if (user == null) {
            return false;
        }
        // 存入userId
        SecurityUserHolder.setCurrentUserId(Integer.valueOf(userId));
        return true;
    }
}
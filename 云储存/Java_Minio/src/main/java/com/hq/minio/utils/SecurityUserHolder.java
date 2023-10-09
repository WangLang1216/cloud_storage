package com.hq.minio.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SecurityUserHolder {

    public static String prefix = "user_token";

    //新增头部参数
    private static String outfallTypePrefix = "outfall_type";

    /**
     * 获取当前的用户id
     * @return Integer
     */
    public static Integer getCurrentUserId() {
        Integer userId = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            try {
                userId = (Integer) request.getSession().getAttribute(prefix);
            } catch (Exception e) {
                request.getSession().removeAttribute(prefix);
                return null;
            }
        }
        return userId;
    }

    /**
     * 获取类型
     * @return Integer
     */
    public static Integer getOutfallType() {
        Integer outfallType = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            try {
                outfallType = (Integer) request.getSession().getAttribute(outfallTypePrefix);
            } catch (Exception e) {
                request.getSession().removeAttribute(outfallTypePrefix);
                return null;
            }
        }
        return outfallType;
    }

    /**
     * 更新当前用户id
     * @param userId
     */
    public static void setCurrentUserId(Integer userId) {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            request.getSession().setAttribute(prefix, userId);
        }
    }

    /**
     * 更新类型
     * @param outfallType
     */
    public static void setOutfallType(Integer outfallType) {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            request.getSession().setAttribute(outfallTypePrefix, outfallType);
        }
    }

    /**
     * 移除当前用户
     */
    public static void removeCurrentUser() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            request.getSession().setAttribute(prefix, null);
            request.getSession().removeAttribute(prefix);
        }
    }

    /**
     * 移除类型
     */
    public static void removeOutfallType() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            request.getSession().setAttribute(outfallTypePrefix, null);
            request.getSession().removeAttribute(outfallTypePrefix);
        }
    }
}

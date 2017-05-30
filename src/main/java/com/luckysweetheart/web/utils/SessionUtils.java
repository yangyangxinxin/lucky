package com.luckysweetheart.web.utils;

import com.luckysweetheart.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by yangxin on 2017/5/22.
 */
public class SessionUtils {

    public static final String loginUserKey = "userInfo";

    public static UserDTO getLoginUser(HttpServletRequest request) {
        Object obj = getAttribute(loginUserKey, request);
        if (obj != null) {
            return (UserDTO) obj;
        }
        return null;
    }

    public static boolean isLogin(HttpServletRequest request) {
        return getLoginUser(request) != null;
    }

    public static Long getLoginUserId(HttpServletRequest request) {
        return isLogin(request) ? getLoginUser(request).getUserId() : 0L;
    }

    public static void setAttribute(String key, Object value, HttpServletRequest request) {
        getSession(request).setAttribute(key, value);
    }

    public static Object getAttribute(String key, HttpServletRequest request) {
        return getSession(request).getAttribute(key);
    }

    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(true);
    }

    public static void login(HttpServletRequest request,UserDTO userVO){
        setAttribute(loginUserKey,userVO, request);
    }

    public static void logout(HttpServletRequest request){
        getSession(request).removeAttribute(loginUserKey);
    }

}

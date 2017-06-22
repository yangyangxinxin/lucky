package com.luckysweetheart.web.utils;

import com.luckysweetheart.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by yangxin on 2017/5/22 13:56
 */
public class SessionUtils {

    private static Logger logger = LoggerFactory.getLogger(SessionUtils.class);

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

    public static void login(HttpServletRequest request, UserDTO userVO) {
        setAttribute(loginUserKey, userVO, request);
        logger.info("登录成功，登录用户id：" + userVO.getUserId() + "，用户名：" + userVO.getUsername());
    }

    public static void removeAttribute(HttpServletRequest request, String key) {
        getSession(request).removeAttribute(key);
    }

    public static void logout(HttpServletRequest request) {
        UserDTO user = getLoginUser(request);
        //removeAttribute(request, loginUserKey);
        getSession(request).invalidate();
        if (user != null) {
            logger.info("注销成功，注销用户id：" + user.getUserId() + "，用户名：" + user.getUsername());
        }
    }

}

package com.luckysweetheart.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * Created by wlinguo on 14-6-24.
 */
public class CookieUtil {

    /**
     * 默认Cookie的有效期
     */
    private static final int DEFAULT_COOKIE_AGE = 7 * 24 * 60 * 60;

    /**
     * 获得指定cookie中的值
     *
     * @param request
     * @param cookieName 要查找的cookie的名字
     * @return 返回指定Cookie中的字符串值
     */
    public static String getCookie(HttpServletRequest request, String cookieName) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 找到指定的cookie
                if (cookie != null && cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 保存cookie的值
     *
     * @param request
     * @param response
     * @param cookieName 要保存的cookie的名字
     * @param value      保存到cookie中的值
     */
    public static void saveCookie(HttpServletRequest request,
                                  HttpServletResponse response, String cookieName, String value) {
        saveCookie(request, response, cookieName, value, DEFAULT_COOKIE_AGE);
    }

    /**
     * 删除指定的cookie
     *
     * @param request
     * @param response
     * @param cookieName 要删除的cookie
     */
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String cookieName) {
        saveCookie(request, response, cookieName, "", 0);
    }

    /**
     * 保存cookie 并设置cookie存活的时间
     *
     * @param request
     * @param response
     * @param cookieName 要保存的cookie的名字
     * @param value      cookie中要存放的值
     * @param age        cookie存活时间
     */
    public static void saveCookie(HttpServletRequest request,
                                  HttpServletResponse response, String cookieName, String value,
                                  int age) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(age);// 设置Cookie的存活时间
        cookie.setPath("/");
        response.addCookie(cookie);// 保存cookie
    }
}

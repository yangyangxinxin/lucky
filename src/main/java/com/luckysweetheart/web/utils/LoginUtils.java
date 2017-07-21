package com.luckysweetheart.web.utils;

import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yangxin on 2017/7/21.
 */
public class LoginUtils {

    private static Logger logger = LoggerFactory.getLogger(LoginUtils.class);

    public static void writeJWT(Long userId, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("t", System.currentTimeMillis());
        Cookie cookie = new Cookie(Const.COOKIE_TOKEN, JwtUtils.encode(json));
        response.addCookie(cookie);
    }

    public static Long getLoginTime(HttpServletRequest request) {
        //从jwt中获取
        try {
            JSONObject _jzqctk = null;
            Cookie[] cookies = request.getCookies();
            Long t = null;
            if (cookies != null && cookies.length > 0) {
                for (Cookie c : cookies) {
                    if (c != null && Const.COOKIE_TOKEN.equals(c.getName())) {
                        _jzqctk = JwtUtils.decode(c.getValue());
                        if (_jzqctk != null) {
                            t = _jzqctk.getLong("t");
                        }
                    }
                }
            }
            return t;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

}

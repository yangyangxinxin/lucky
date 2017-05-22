package com.luckysweetheart.web;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by yangxin on 2017/5/22.
 */
public class SessionUtils {

    public static final String loginUserKey = "userInfo";

    public static UserVO getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Object obj  = session.getAttribute(loginUserKey);
        if(obj != null){
            return (UserVO)obj;
        }
        return null;
    }

    public static boolean isLogin(HttpServletRequest request) {
        return getLoginUser(request) != null;
    }

    public static Long getLoginUserId(HttpServletRequest request) {
        return isLogin(request) ? getLoginUser(request).getUserId() : 0L;
    }

}

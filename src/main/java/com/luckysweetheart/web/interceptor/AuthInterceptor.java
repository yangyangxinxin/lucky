package com.luckysweetheart.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.common.Const;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.SpringUtil;
import com.luckysweetheart.web.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxin on 2017/5/25.
 */
public class AuthInterceptor extends AbstractInterceptor {

    private UserService userService;

    private void init() {
        if (userService == null) {
            userService = SpringUtil.getBean(UserService.class);
        }
    }

    /**
     * 不需要登录的URL
     *
     * @return
     */
    private List<String> unLoginList() {
        List<String> list = new ArrayList<>();
        list.add("/");
        list.add("/m/");
        list.add("/m/index");
        list.add("/m/setNightShift");
        list.add("/index");
        list.add("/account/*");
        list.add("/m/account/*");
        list.add("/download");
        list.add("/m/download");
        list.add("/article/list");
        list.add("/article/detail");
        list.add("/article/getDetail");
        list.add("/m/article/list");
        list.add("/m/article/detail");
        list.add("/m/article/detail");
        list.add("/photo/list");
        list.add("/m/photo/list");
        list.add("/photo/queryPage");
        list.add("/m/photo/queryPage");
        list.add("/test/*");
        list.add("/m/test/*");
        list.add("/verification/*");
        list.add("/m/violation/*");
        return list;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        boolean flag = false;
        String requestUrl = urlHelper.getLookupPathForRequest(request);
        if (matches(requestUrl)) {
            flag = true;
        }
        UserDTO userDTO = SessionUtils.getLoginUser(request);

        if (userDTO != null) {
            Long t = LoginUtils.getLoginTime(request);
            if (t == null || System.currentTimeMillis() - 1000 * 60 * 60L > t) {//超过1小时，重新更新cookice
                //生新生成jwt
                LoginUtils.writeJWT(userDTO.getUserId(), response);
            }
            flag = true;
        } else {
            //从jwt中获取
            JSONObject _jzqctk = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie c : cookies) {
                    if (c != null && Const.COOKIE_TOKEN.equals(c.getName())) {
                        try {
                            String value = c.getValue();
                            if (StringUtils.isNotBlank(value)) {
                                _jzqctk = JwtUtils.decode(c.getValue());
                                if (_jzqctk != null) {
                                    Long t = _jzqctk.getLong("t");
                                    Long userId = _jzqctk.getLong("userId");
                                    if (t != null && System.currentTimeMillis() - 1000 * 60 * 60L > t) {//超过1小时，发现有cookie清空
                                        //jwt清空
                                        Cookie cookie = new Cookie(Const.COOKIE_TOKEN, null);
                                        response.addCookie(cookie);
                                    } else if (userId != null) {
                                        init();
                                        UserDTO dto = userService.findById(userId);
                                        SessionUtils.login(request, dto);
                                        flag = true;
                                    }
                                }
                            }
                        } catch (RuntimeException e) {
                            logger.error("_jzqctk获取错误：" + e.getMessage(), e);
                        }
                    }
                }
            }
        }

        if (flag) {
            return true;
        } else {
            boolean isAjax = RequestUtils.isAjaxRequest(request);
            if (isAjax) {
                //处理ajax情况
                AjaxResult ajaxResult = AjaxResult.createFailedResult("请先登录");
                ajaxResult.setResultCode(401);
                writeJsonResponse(response, ajaxResult);
                return false;
            }
            String queryStr = urlHelper.getOriginatingQueryString(request);
            String returnURL = requestUrl;
            if (StringUtils.isNotBlank(queryStr)) {
                if (returnURL.contains("?")) {
                    returnURL = returnURL + "&" + queryStr;
                } else {
                    returnURL = returnURL + "?" + queryStr;
                }
            }
            if (UserAgentUtil.isMobile(request)) {
                response.sendRedirect("/m/account/loginPage?returnUrl=" + URLEncoder.encode(returnURL, "UTF-8"));
            } else {
                response.sendRedirect("/account/loginPage?returnUrl=" + URLEncoder.encode(returnURL, "UTF-8"));
            }
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private boolean matches(String path) {
        return super.match(this.unLoginList(), path);
    }

}

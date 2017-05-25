package com.luckysweetheart.web.interceptor;

import com.luckysweetheart.config.InterceptorsConfiguration;
import com.luckysweetheart.web.utils.AjaxResult;
import com.luckysweetheart.web.utils.RequestUtils;
import com.luckysweetheart.web.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yangxin on 2017/5/25.
 */
public class AuthInterceptor extends AbstractInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestUrl = urlHelper.getLookupPathForRequest(request);
        if (matches(requestUrl)) {
            return true;
        }
        boolean isLogin = SessionUtils.isLogin(request);
        boolean isAjax = RequestUtils.isAjaxRequest(request);
        if (!isLogin) {
            if (isAjax) {
                //处理ajax情况
                if (RequestUtils.isAjaxRequest(request)) {
                    AjaxResult ajaxResult = AjaxResult.createFailedResult("请先登录");
                    ajaxResult.setResultCode(401);
                    writeJsonResponse(response, ajaxResult);
                    return false;
                }
            }
            response.sendRedirect("/account/loginPage");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private List<String> unLoginList() {
        List<String> list = new ArrayList<>();
        list.add("/index");
        list.add("/account/loginPage");
        return list;
    }

    private boolean matches(String path) {
        return super.match(unLoginList(), path);
    }

}

package com.luckysweetheart.web.interceptor;

import com.luckysweetheart.utils.SpringUtil;
import com.luckysweetheart.web.utils.AjaxResult;
import com.luckysweetheart.web.utils.DomainUtils;
import com.luckysweetheart.web.utils.RequestUtils;
import com.luckysweetheart.web.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxin on 2017/5/25.
 */
public class AuthInterceptor extends AbstractInterceptor {


    /**
     * 不需要登录的URL
     *
     * @return
     */
    private List<String> unLoginList() {
        List<String> list = new ArrayList<>();
        list.add("/");
        list.add("/index");
        list.add("/account/*");
        list.add("/download");
        list.add("/article/list");
        list.add("/article/detail");
        list.add("/photo/list");
        list.add("/photo/queryPage");
        list.add("/test/*");
        return list;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestUrl = urlHelper.getLookupPathForRequest(request);
        boolean isLogin = SessionUtils.isLogin(request);
        if (matches(requestUrl)) {
            return true;
        }
        boolean isAjax = RequestUtils.isAjaxRequest(request);
        if (!isLogin) {
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
            response.sendRedirect("/account/loginPage?returnUrl=" + URLEncoder.encode(returnURL, "UTF-8"));
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

    private boolean matches(String path) {
        return super.match(this.unLoginList(), path);
    }

}

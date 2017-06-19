package com.luckysweetheart.web;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.utils.DomainService;
import com.luckysweetheart.web.utils.SessionUtils;
import com.luckysweetheart.web.utils.UserAgentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by yangxin on 2017/5/22.
 */
@Controller
public abstract class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    @Resource
    private DomainService domainService;

    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        setAttribute("domainUtils", domainService);
    }

    protected void setSessionAttribute(String key, Object value) {
        SessionUtils.setAttribute(key, value, request);
    }

    protected Object getSessionAttribute(String key) {
        return SessionUtils.getAttribute(key, request);
    }

    protected Long getLoginUserId() {
        return SessionUtils.getLoginUserId(request);
    }

    protected UserDTO getLoginUser() {
        return SessionUtils.getLoginUser(request);
    }

    protected void setAttribute(String name, Object value) {
        this.request.setAttribute(name, value);
    }

    protected Object getAttribute(String name) {
        return this.request.getAttribute(name);
    }

    protected String getString(String name) {
        Object obj = this.getAttribute(name);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    protected String getString(String name, String defaultValue) {
        Object obj = this.getAttribute(name);
        if (obj != null) {
            return obj.toString();
        }
        return defaultValue;
    }

    protected Long getLong(String name) {
        Object obj = this.getAttribute(name);
        try {
            if (obj != null) {
                return Long.valueOf(obj.toString());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    protected Long getLong(String name, Long defaultValue) {
        Object obj = this.getAttribute(name);
        try {
            if (obj != null) {
                return Long.valueOf(obj.toString());
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }

    protected Integer getInteger(String name) {
        Object obj = this.getAttribute(name);
        try {
            if (obj != null) {
                return Integer.valueOf(obj.toString());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    protected Integer getInteger(String name, Integer defaultValue) {
        Object obj = this.getAttribute(name);
        try {
            if (obj != null) {
                return Integer.valueOf(obj.toString());
            }
        } catch (Exception e) {
            return defaultValue;
        }
        return defaultValue;
    }

    protected List<MultipartFile> getMultipartFile(String name) {
        return ((MultipartHttpServletRequest) request).getFiles(name);
    }

    protected boolean isMobile() {
        return UserAgentUtil.UA_TYPE_MOBILE.equalsIgnoreCase(UserAgentUtil.uaType(request));
    }

    protected boolean isPc() {
        return !isMobile();
    }

    protected <T> ResultInfo<T> getFailResult() {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        return resultInfo.fail();
    }

    protected <T> ResultInfo<T> getFailResult(String message) {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        resultInfo.fail(message);
        return resultInfo;
    }

    protected <T> ResultInfo<T> getFailResult(String message, Class<T> clazz) {
        return ResultInfo.create(clazz).fail(message);
    }

    protected <T> ResultInfo<T> getFailResult(String message, String resultCode) {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        return resultInfo.fail(message).setResultCode(resultCode);
    }

    protected <T> ResultInfo<T> getSuccessResult() {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        return resultInfo.success();
    }

    protected <T> ResultInfo<T> getSuccessResult(T data) {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        return resultInfo.success(data);
    }

    protected boolean isLogin() {
        return SessionUtils.isLogin(request);
    }
}

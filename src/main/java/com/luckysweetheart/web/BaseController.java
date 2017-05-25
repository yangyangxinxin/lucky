package com.luckysweetheart.web;

import com.luckysweetheart.web.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yangxin on 2017/5/22.
 */
@Controller
public abstract class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
    }

    protected void setSeesionAttribute(String key, Object value) {
        SessionUtils.setAttribute(key, value, request);
    }

    protected Object getSessionAttribute(String key) {
        return SessionUtils.getAttribute(key, request);
    }

    protected Long getLoginUserId() {
        return SessionUtils.getLoginUserId(request);
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

    /**
     * 异常页面控制
     *
     * @param
     * @return
     *//*
    @ExceptionHandler(Exception.class)
    public String runtimeExceptionHandler(Exception e, ModelMap modelMap) {
        logger.error(e.getLocalizedMessage());
        modelMap.put("status", 500);
        if (e instanceof BusinessException) {
            modelMap.addAttribute("message", e.getMessage());
        }
        return "exception";
    }*/

}

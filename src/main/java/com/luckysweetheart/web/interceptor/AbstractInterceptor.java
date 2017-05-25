package com.luckysweetheart.web.interceptor;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AbstractInterceptor extends HandlerInterceptorAdapter {

	protected List<String> unInterceptUrls; // 不被该拦截器拦截的URL
	
	protected List<String> interceptUrls; // 该拦截器拦截的URL

	protected UrlPathHelper urlHelper = new UrlPathHelper();

	protected PathMatcher pathMatcher = new AntPathMatcher();
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected boolean match(List<String> patterns,String path){
		for(String pattern : patterns){
			if(pathMatcher.match(pattern, path)){
				return true;
			}
		}
		return false;
	}
	
	protected void writeJsonResponse(HttpServletResponse response, Object target) {
		PrintWriter writer;
		try {
			response.setCharacterEncoding("UTF-8");
			writer = response.getWriter();
			writer.write(JSON.toJSONString(target));
		} catch (IOException e) {
			logger.error("writeJsonResponse error",e);
			//do nothing
		}
		
	}
	
	protected void sendRedirect(HttpServletResponse response, String url){
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error("sendRedirect error",e);
		}
	}

	public void setUnInterceptUrls(List<String> unInterceptUrls) {
		this.unInterceptUrls = unInterceptUrls;
	}
	
	public void setInterceptUrls(List<String> interceptUrls) {
		this.interceptUrls = interceptUrls;
	}

}

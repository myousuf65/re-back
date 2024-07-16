package com.hkmci.csdkms.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class StaticResourceFilter implements HandlerInterceptor {
	public static final String MCI_ERROR_VIEW = "error";
	private static Logger logger = LoggerFactory.getLogger(StaticResourceFilter.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		String url = request.getRequestURL().toString();
		String referrer = request.getHeader("referer");
        String type = url.substring(url.toString().lastIndexOf(".") + 1,url.toString().length());
        logger.info("Request Type is: {}",type);
        if(type.toLowerCase().equals("pdf") && referrer == null) {
        	response.sendError(HttpServletResponse.SC_FORBIDDEN,"Can not access to this resource.");
        	//response.sendRedirect("some-url");
        	return false; // return blank page
        }
		logger.info("interceptor....Before filtering...url:{}", request.getRequestURL());
		
		return true; 
	}
 
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//logger.info("interceptor.......url:{}", request.getRequestURL());
	}
 
 
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		throws Exception {
		
	}
	
}
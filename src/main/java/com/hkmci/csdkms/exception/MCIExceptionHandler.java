package com.hkmci.csdkms.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.hkmci.csdkms.controller.JsonResult;
import com.hkmci.csdkms.filter.StaticResourceFilter;

import org.springframework.core.Ordered;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MCIExceptionHandler extends RuntimeException{

	public static final String MCI_ERROR_VIEW = "error";
	private static Logger logger = LoggerFactory.getLogger(MCIExceptionHandler.class);
//	@ExceptionHandler(value = Exception.class)
//    public Object errorHandler(HttpServletRequest reqest, 
//    		HttpServletResponse response, Exception e) throws Exception {
//    	
//    	e.printStackTrace();
//    	
//		ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", e);
//        mav.addObject("url", reqest.getRequestURL());
//        mav.setViewName(IMOOC_ERROR_VIEW);
//        return mav;
//    }
	  @ExceptionHandler(CustomException.class)
	  public final ResponseEntity<Object> handleAllExceptions(CustomException ex) {
	    logger.info("GoIntoCustomGlobal: ");
//	    CustomExceptionSchema exceptionResponse =
//	        new CustomExceptionSchema(
//	            ex.getTimestamp(), ex.getStatus(), ex.getError(), ex.getMessage(), ex.getPath());
	    CustomExceptionSchema exceptionResponse = new CustomExceptionSchema(ex.getMessage());
	    
	    logger.info("GoIntoCustomGlobal222: ", ex.getTimestamp());
	    return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	  }  
	
	@ExceptionHandler(value = Exception.class)
    public Object errorHandler(HttpServletRequest reqest, 
    		HttpServletResponse response, Exception e) throws Exception {
    	
    	e.printStackTrace();
    	logger.info("Request: {}", reqest);
    	logger.info("Response: {}", response);
    	
    	if (isAjax(reqest)) {
    		return JsonResult.errorException(e.getMessage());
    	} else {
    		logger.info("Catch Error: {}",e);
    		ModelAndView mav = new ModelAndView();
            mav.addObject("exception", e);
            mav.addObject("url", reqest.getRequestURL().toString());
            mav.setViewName(MCI_ERROR_VIEW);
            return mav;
    	}
	}	
	/**
	 * 
	 * @Title: MCIExceptionHandler.java
	 * @Package com.imooc.exception
	 * @Description: is Ajax
	 * Copyright: Copyright (c) 2017
	 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
	 * 
	 * @author Holfer ZHANG
	 * @date 2019-4-3 1:40:39
	 * @version V1.0
	 */
	public static boolean isAjax(HttpServletRequest httpRequest){
		return  (httpRequest.getHeader("X-Requested-With") != null  
					&& "XMLHttpRequest"
						.equals( httpRequest.getHeader("X-Requested-With").toString()) );
	}
}

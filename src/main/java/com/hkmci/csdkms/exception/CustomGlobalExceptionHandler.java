package com.hkmci.csdkms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(CustomGlobalExceptionHandler.class);

  @ExceptionHandler(CustomException.class)
  public final ResponseEntity<Object> handleAllExceptions(CustomException ex) {
    logger.info("GoIntoCustomGlobal: ");
//    CustomExceptionSchema exceptionResponse =
//        new CustomExceptionSchema(
//            ex.getTimestamp(), ex.getStatus(), ex.getError(), ex.getMessage(), ex.getPath());
    CustomExceptionSchema exceptionResponse = new CustomExceptionSchema(ex.getMessage());
    
    logger.info("GoIntoCustomGlobal222: ", ex.getTimestamp());
    return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }  
  
//  public void springHandleNotFound(HttpServletResponse response) throws IOException {
//	    logger.info("GoIntoCustomGlobal: ");
//	    response.sendError(HttpStatus.UNAUTHORIZED.value());
//	  }  

}
*/
package com.hkmci.csdkms.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.controller.AuthController;
import com.hkmci.csdkms.controller.JsonResult;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.exception.CustomException;
import com.hkmci.csdkms.exception.MCIExceptionHandler;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.security.CustomAuthenticationToken;
import com.hkmci.csdkms.security.JwtTokenProvider;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.util.AppConstants;
import com.hkmci.csdkms.util.MessageConstants;

public class CustomAuthenticationFilter extends OncePerRequestFilter {

//    private final LogService reportLogger;
	
    private final Common common;
    
    private  AuthController authController;
	
    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

    private final ObjectMapper mapper = new ObjectMapper();
    
//	@Resource
//    private UserService userService;

    @Value("${app.intranetHost}")
    private String intranetHost;
	
    @Value("${app.remoteKMSHost}")
    private String remoteKMSHost;
    
    @Autowired
    public CustomAuthenticationFilter(Common theCommon, LogService theReportLogger) {
		this.common = theCommon;
		//this.reportLogger = theReportLogger;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, CustomException {
    	System.out.println("-----doFilterInternal----" );

        String theToken=null, xAuth=null;
        
        logger.info("Start CustomAuthenticationFilter...");
        logger.info("Host: "+request.getRequestURL().toString() + " -- " + intranetHost);
		logger.info("Request Token: " + request.getHeader("accessToken") + " -- " + (request.getHeader("accessToken") == null)) ;
		String failPath = "api/auth/logout";
		// if (request.getHeader("Host").equalsIgnoreCase(AppConstants.INTRANET_HOST)){
		// 		// 1 - dp.csd.hksarg; 2 - access.csd.gov.hk but masked by dp.csd.hksarg
		// 		// ONLY access.csd.gov.hk has defined request.getHeader("remote-access")
		// 		failPath = "";
		// }else{
		// 		failPath =  "/auth/failout";
		// }					

//        if (request.getHeader("Host").equalsIgnoreCase(intranetHost)) {
//            xAuth = request.getHeader("employeenumber");
//        }else {
        try{
        	logger.info("First line in try ");
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			logger.info("Auth = "+auth.isAuthenticated());
			if( !auth.isAuthenticated()) {
				System.out.println("When AUTH is NULL ");
				authController.REauthenticateUserDP(request);
			}
			
			
			logger.info( " Is Authenticated = "+ auth.isAuthenticated());
			logger.info(" Request Header - Host + " +request.getHeader("Host"));
			
			
		if (auth != null && auth.isAuthenticated() && request.getHeader("Host") !=null){
			logger.info("auth !=null &&  auth.isAuthenticated is true && getHeader HOST not null");
					
				if( !request.isRequestedSessionIdValid())
				{
					logger.info("isAuth in Filter:" + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
					//comes here when session is invalid.
					// redirect to a clean error page "Your session has expired. Please login again."
					logger.info("No Session!");
					throw new CustomException(HttpStatus.REQUEST_TIMEOUT.value(), MessageConstants.REQUEST_TIMEOUT);
				}
				logger.info("Inside Auth Filter: ");
			
	        // if (request.getHeader("accessToken") !=null && request.getHeader("accesshost") !=null ){
// ---------------------------------------------------------------------------------------------
	           theToken = request.getHeader("accessToken").toString();
	           logger.info("Can get the token  = "+ theToken);
	           
	            if (!theToken.equalsIgnoreCase("null")) {
			        logger.info("RequestTken: " + request.getHeader("accessToken"));
			        Integer theResult = common.validateToken(theToken);
			        logger.info("ValidateToken: " + theResult);
			        if (StringUtils.hasText(theToken) && theResult==0) {
			        	logger.info("Start get username from JWT...");
			            xAuth = common.getStaffNoByToken(theToken);
			        }else {
//-----------------------------------------------------------------------------------------------
						logger.info("Throw 1");
						throw new CustomException(theResult, MessageConstants.INVALID_TOKEN);
			        }
	
			        logger.info("Authenticating..."+ xAuth);
	
					filterChain.doFilter(request, response);
					logger.info("After filterChain");
				}
	           
			}else {
				logger.info("User did NOT login! " + request.getHeader("Referer")+ failPath);
				// response.sendRedirect(request.getHeader("Referer")+ failPath);	
				// response.sendRedirect("/auth/failout");		
			    JsonResult errorResponse = new  JsonResult(HttpStatus.UNAUTHORIZED.value(), "User did not login!!!",null);
			    response.setStatus(HttpStatus.UNAUTHORIZED.value());
			    response.getWriter().write(convertObjectToJson(errorResponse));
	        }

		}catch (AuthenticationException e){
			logger.info("Throw 4: " + e.getMessage());
			// response.sendRedirect("/auth/failout");		
			 response.sendRedirect(request.getHeader("Referer")+ failPath);		
			 logger.info("Referered 3");
		}catch  (CustomException e){
			
			
			
			logger.info("Throw 2: " + e.getMessage());
			
			if (e.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
			    JsonResult errorResponse = new  JsonResult(HttpStatus.UNAUTHORIZED.value(), e.getMessage(),null);
			    response.setStatus(HttpStatus.UNAUTHORIZED.value());
			    response.getWriter().write(convertObjectToJson(errorResponse));
			}else if (e.getStatus() == 440) {
			    JsonResult errorResponse = new  JsonResult(440, "Session Expired!",null);
			    response.setStatus(440);
			    response.getWriter().write(convertObjectToJson(errorResponse));
			}
			logger.info("Error code: "+ e.getStatus());
			 response.sendRedirect(request.getHeader("Referer")+ failPath);
			 logger.info("Referered 2:"+ request.getHeader("Referer")+ failPath);
		
		}catch  (RuntimeException e){
			logger.info("Throw 3: " + e.getMessage());
			  JsonResult errorResponse = new  JsonResult(440, "Runtime Exception",null);
			  response.setStatus(440);
			  response.getWriter().write(convertObjectToJson(errorResponse));
			 response.sendRedirect(request.getHeader("Referer")+ failPath);		
			 logger.info("Referered 3");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.info("Throw 6: " + e.getMessage());
			e.printStackTrace();
			 response.sendRedirect(request.getHeader("Referer")+ failPath);		
			 
		
			 logger.info("Referered 3");
		}
	
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
    
//	private boolean isValid(String value) {
//		// TODO Auto-generated method stub
//		if (value == null)
//			return false;
//		else
//			return true;
//	}
}

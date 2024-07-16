//For RestAPI Auth
package com.hkmci.csdkms.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.LogMessageProperties;
import com.hkmci.csdkms.entity.CustomUserDetails;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.payload.ApiResponse;
import com.hkmci.csdkms.payload.JwtAuthenticationResponse;
import com.hkmci.csdkms.payload.LoginRequest;
import com.hkmci.csdkms.payload.ValidateTokenRequest;
import com.hkmci.csdkms.security.CurrentUser;
import com.hkmci.csdkms.security.CustomAuthenticationToken;
import com.hkmci.csdkms.security.JwtTokenProvider;
import com.hkmci.csdkms.service.MobileVersionService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.storage.StorageProperties;
import com.hkmci.csdkms.storage.StorageService;
import com.hkmci.csdkms.util.AppConstants;
import com.hkmci.csdkms.util.MessageConstants;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin()
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    
    private final StorageService storageService;
    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private Common common;

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;


	@Autowired
	@Resource
	private MobileVersionService mobileVersionSercice;
	
    @Autowired
    JwtTokenProvider tokenProvider;

    @Value("${app.intranetHost}")
    private String intranetHost;

    @Value("${app.devLocalHost}")
    private String devLocalHost;

    @Value("${app.devNamedHost}")
    private String devNamedHost;

    @Value("${app.accessCodeMobileApp}")
    private String accessCodeMobileApp;

    @Value("${app.accessCodeProxy}")
    private String accessCodeProxy;
    @Autowired
	public AuthController(StorageService storageService) {
        this.storageService = storageService;
       
    }
    
    
    
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/mobile/IOSipa")
	public String returnIpa(HttpServletResponse response)throws IOException {
		Path mobilePath = storageService.getMobileLocation();
		String file_path =null;
		file_path = mobilePath.toString() + "/ios/app.ipa" ;
		String version = mobileVersionSercice.findAppVersion("ios");
		
		File file = new File(file_path);
		System.out.println("File: " + file);
		InputStream fis = new BufferedInputStream(new FileInputStream(file_path));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=iosversion" );
		response.addHeader("Content-Length",""+ file.length());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//		response.setContentType("application/vnd.android.package-archive");
		response.setContentType("application/octet-stream");
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
		
		System.out.println("IOS Version = "+ version+ "File path = "+ file_path);
		return file_path;
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping("/sisionly")
    public String sisionly( HttpServletResponse response)  throws IOException {
//    	---------WORKING DOWNLOAD PDF ----------------------------------------------
    	
    	Path resourcePath = storageService.getResourceLocation();
    	String file_path =null;
    	file_path = resourcePath.toString() + "/mobile/ios/manifest.plist" ;
		System.out.println("File path: " + file_path);

		return file_path;
    }
    
    
    
//    For kmst, mobile app login (1st api)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/generatetoken")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        logger.info("Start generatetoken for Remote Access  " );

        HttpSession session1 = request.getSession();
        session1.setAttribute("hello", "world");
//        session1.invalidate();
        
        if (loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            return new ResponseEntity(new ApiResponse(false, MessageConstants.USERNAME_OR_PASSWORD_INVALID),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            // Check Access Channel
            String theAccessChannel;
            logger.info("Start generatetoken , login name = " + loginRequest.getUsername() );
            if (loginRequest.getAccessCode().matches(accessCodeMobileApp)) {
            	//mobile app access channel = 4
                theAccessChannel = "4";
            } else if (loginRequest.getAccessCode().matches(accessCodeProxy)) {
            	//kmst access channel = 3 
                theAccessChannel = "3";
            } else {
                throw new BadCredentialsException("MessageConstants.USERNAME_OR_PASSWORD_INVALID");

            }
            
            // Check User whether locked
            logger.info("Login Request = "+loginRequest.getUsername());
            User user = userService.findByUsername(loginRequest.getUsername());
            if (user.getLoginFailAttempt() >= AppConstants.MAX_LOGIN_FAIL_ATTEMPT) {
            	logger.info("Fail time > 3 " + user.getLoginFailAttempt() + " **" + AppConstants.MAX_LOGIN_FAIL_ATTEMPT);
            	checkLoginTries(loginRequest.getUsername());
                return new ResponseEntity(new ApiResponse(false, MessageConstants.USER_LOCKED), HttpStatus.LOCKED);
            }

            // Check user's credential. If not valid, pass to catch
            logger.info("Start checking user's credentail..."+ theAccessChannel);
            Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            
            
           //System.out.println("User id = "+ user.getId());
            logger.info(
                    "user: " + user.getStaffNo() + "-- LoginFailAttempt: " + user.getLoginFailAttempt());
           // login_fail_attempt <3
            if (user.getLoginFailAttempt() < AppConstants.MAX_LOGIN_FAIL_ATTEMPT) {
            	logger.info("Going to Generate Token1...");
//                SecurityContextHolder.getContext().setAuthentication(authentication);

//                UserDetails userDetails = User.builder()
//                        .username("hardcodedUser")
//                        .password("password")
//                        .roles("ROLE_USER")
//                        .build();
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // checking if user added to authnticated

                CustomUserDetails u  = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                System.out.println("authenticated user, --------------"+u);

                logger.info("Going to Generate Token...");
                // Generate Token
                String jwt = tokenProvider.generateToken(authentication);
                JwtAuthenticationResponse jwtAuthResponse = new JwtAuthenticationResponse(jwt);

                if (loginRequest.getAccessChannel() != null) {
                    theAccessChannel = loginRequest.getAccessChannel();
                }
                jwtAuthResponse.setAccessChannel(theAccessChannel);

                // Reset Login fail attempt to Zero
//                if (user.getLoginFailAttempt() > 0) {
//                    user.setLoginFailAttempt(0L);
//                    userService.updateUserById(user);
//                    logger.info("user: " + user.getStaffNo() + "-- LoginFailAttempt: Reset to 0");
//                }

            	
           	 	String staffNo = user.getStaffNo();
                this.createSession(staffNo, request, Integer.parseInt(theAccessChannel));
                
              
                
                return ResponseEntity.ok(jwtAuthResponse);
            } else {
            	logger.info("Fail time > 3 " + user.getLoginFailAttempt() + " **" + AppConstants.MAX_LOGIN_FAIL_ATTEMPT);
                return new ResponseEntity(new ApiResponse(false, MessageConstants.USER_LOCKED), HttpStatus.LOCKED);
            }
        } catch (BadCredentialsException e) {
            // Record login try
        	logger.info("Auth Controller : Bad Credential Exception "+ e);
            int result = this.checkLoginTries(loginRequest.getUsername());
            System.out.println("Check Login Tries Result  =  "+ result);
            if (result <= AppConstants.MAX_LOGIN_FAIL_ATTEMPT) {
                return new ResponseEntity(new ApiResponse(false, MessageConstants.USERNAME_OR_PASSWORD_INVALID,MessageConstants.USERNAME_OR_PASSWORD_INVALID_TC ,0),
                        HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(new ApiResponse(false, MessageConstants.USER_LOCKED), HttpStatus.LOCKED);
            }

        }

    }
    
    /*
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/generatetoken")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        logger.info("Start generatetoken for Remote Access  " );

        HttpSession session1 = request.getSession();
        session1.invalidate();
        
        if (loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            return new ResponseEntity(new ApiResponse(false, MessageConstants.USERNAME_OR_PASSWORD_INVALID),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            // Check Access Channel
            String theAccessChannel;
            logger.info("Start generatetoken , login name = " + loginRequest.getUsername() );
            if (loginRequest.getAccessCode().matches(accessCodeMobileApp)) {
            	//mobile app access channel = 4
                theAccessChannel = "4";
            } else if (loginRequest.getAccessCode().matches(accessCodeProxy)) {
            	//kmst access channel = 3 
                theAccessChannel = "3";
            } else {
                throw new BadCredentialsException("MessageConstants.USERNAME_OR_PASSWORD_INVALID");

            }

            // Check user's credential. If not valid, pass to catch
            logger.info("Start checking user's credentail..."+ theAccessChannel);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // Check User whether locked
            logger.info("Login Request = "+loginRequest.getUsername());
           User user = userService.findByUsername(loginRequest.getUsername());
           //System.out.println("User id = "+ user.getId());
            logger.info(
                    "user: " + user.getStaffNo() + "-- LoginFailAttempt: " + user.getLoginFailAttempt());
           // login_fail_attempt <3
            if (user.getLoginFailAttempt() < AppConstants.MAX_LOGIN_FAIL_ATTEMPT) {
            	logger.info("Going to Generate Token1...");
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Going to Generate Token...");
                // Generate Token
                String jwt = tokenProvider.generateToken(authentication);
                JwtAuthenticationResponse jwtAuthResponse = new JwtAuthenticationResponse(jwt);

                if (loginRequest.getAccessChannel() != null) {
                    theAccessChannel = loginRequest.getAccessChannel();
                }
                jwtAuthResponse.setAccessChannel(theAccessChannel);

                // Reset Login fail attempt to Zero
//                if (user.getLoginFailAttempt() > 0) {
//                    user.setLoginFailAttempt(0L);
//                    userService.updateUserById(user);
//                    logger.info("user: " + user.getStaffNo() + "-- LoginFailAttempt: Reset to 0");
//                }

            	
           	 	String staffNo = user.getStaffNo();
                this.createSession(staffNo, request, Integer.parseInt(theAccessChannel));
                
              
                
                return ResponseEntity.ok(jwtAuthResponse);
            } else {
            	logger.info("Fail time > 3 " + user.getLoginFailAttempt() + " **" + AppConstants.MAX_LOGIN_FAIL_ATTEMPT);
                return new ResponseEntity(new ApiResponse(false, MessageConstants.USER_LOCKED), HttpStatus.LOCKED);
            }
        } catch (BadCredentialsException e) {
            // Record login try
        	logger.info("Auth Controller : Bad Credential Exception "+ e);
            int result = this.checkLoginTries(loginRequest.getUsername());
            System.out.println("Check Login Tries Result  =  "+ result);
            if (result <= AppConstants.MAX_LOGIN_FAIL_ATTEMPT) {
                return new ResponseEntity(new ApiResponse(false, MessageConstants.USERNAME_OR_PASSWORD_INVALID,MessageConstants.USERNAME_OR_PASSWORD_INVALID_TC ,0),
                        HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(new ApiResponse(false, MessageConstants.USER_LOCKED), HttpStatus.LOCKED);
            }

        }

    } */

    
//    Login from DP 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/generatetokendp")
    public ResponseEntity<?> authenticateUserDP(HttpServletRequest request, @RequestBody LoginRequest loginRequest,
            HttpSession session) throws JSONException {
        logger.info("Start generatetoken for DP...");

        String xAuth = null;
        logger.info("Header - Host (Line 272): " + request.getHeader("host") + " -- " + intranetHost + " -- "
                + loginRequest.getClientHost());
        logger.info("Header - employeenum (Line 274): " + request.getHeader("employeenumber"));
       
        if ((loginRequest.getClientHost().equalsIgnoreCase(intranetHost))
                && (request.getHeader("employeenumber") != null)) {
            xAuth = request.getHeader("employeenumber");
            // Create our Authentication and let Spring know about it
            Authentication auth = new CustomAuthenticationToken(xAuth);
            User user = userService.findByStaffNo(xAuth);
            logger.info("After call findByStaffNo API ");
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                    AuthorityUtils.createAuthorityList("ROLE_USER"));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateTokenDP(user.getUsername());

            // Create New Session in server after login (1 for dp, 2 for remote access)
            Integer accessChannel = request.getHeader("remote-access") == null ? 1 : 2;
            
            logger.info("Access Channel for DP = "+ accessChannel);
            
            this.createSession(xAuth, request, accessChannel);
            logger.info(
                    "isAuth in Controller:" + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

            JwtAuthenticationResponse jwtAuthResponse = new JwtAuthenticationResponse(jwt);
            jwtAuthResponse.setUsername(user.getUsername());
            logger.info("GenerateToken:" + jwtAuthResponse.getUsername() + " -- " + jwtAuthResponse.getAccessToken());
            jwtAuthResponse.setAccessChannel(accessChannel.toString());
            return ResponseEntity.ok(jwtAuthResponse);
        } else {
            return new ResponseEntity(new ApiResponse(false, MessageConstants.USERNAME_OR_PASSWORD_INVALID),
                    HttpStatus.BAD_REQUEST);
        }
    }
    
    public ResponseEntity<?> REauthenticateUserDP(HttpServletRequest request
            ) throws JSONException {
        logger.info("Start generatetoken for DP, line 312 ...");

        String xAuth = null;
        logger.info("Header - Host (LIne 315): " + request.getHeader("host") );
        logger.info("Header - employeenum: " + request.getHeader("employeenumber"));
       
        if ( (request.getHeader("employeenumber") != null)) {
            xAuth = request.getHeader("employeenumber");
            // Create our Authentication and let Spring know about it
            Authentication auth = new CustomAuthenticationToken(xAuth);
            User user = userService.findByStaffNo(xAuth);
            logger.info("After call findByStaffNo API ");
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                    AuthorityUtils.createAuthorityList("ROLE_USER"));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateTokenDP(user.getUsername());

            // Create New Session in server after login (1 for dp, 2 for remote access)
            Integer accessChannel = request.getHeader("remote-access") == null ? 1 : 2;
            
            logger.info("Access Channel for DP = "+ accessChannel);
            
            this.createSession(xAuth, request, accessChannel);
            logger.info(
                    "isAuth in Controller:" + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

            JwtAuthenticationResponse jwtAuthResponse = new JwtAuthenticationResponse(jwt);
            jwtAuthResponse.setUsername(user.getUsername());
            logger.info("GenerateToken:" + jwtAuthResponse.getUsername() + " -- " + jwtAuthResponse.getAccessToken());
            jwtAuthResponse.setAccessChannel(accessChannel.toString());
            return ResponseEntity.ok(jwtAuthResponse);
        } else {
            return new ResponseEntity(new ApiResponse(false, MessageConstants.USERNAME_OR_PASSWORD_INVALID),
                    HttpStatus.BAD_REQUEST);
        }
    }


    
    
    
    
    
    
    
    
    

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/generatetokendev")
    public ResponseEntity<?> authenticateUserDev(HttpServletRequest request, @RequestBody LoginRequest loginRequest,
            HttpSession session) throws JSONException {
        logger.info("Start generatetoken for Local Dev...");

        String xAuth = null;
        logger.info("Header - Host: " + request.getHeader("host") + " -- " + intranetHost + " -- "
                + loginRequest.getClientHost());
        logger.info("Header - employeenum: " + request.getHeader("employeenumber"));
        if ((loginRequest.getClientHost().equalsIgnoreCase(devLocalHost)
                || loginRequest.getClientHost().equalsIgnoreCase(devNamedHost))
                && (request.getHeader("employeenumber") != null)) {
            xAuth = request.getHeader("employeenumber");
            // Create our Authentication and let Spring know about it
            Authentication auth = new CustomAuthenticationToken(xAuth);
            User user = userService.findByStaffNo(xAuth);
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = tokenProvider.generateTokenDP(user.getUsername());

            JwtAuthenticationResponse jwtAuthResponse = new JwtAuthenticationResponse(jwt);
            jwtAuthResponse.setUsername(user.getUsername());
            logger.info("GenerateToken:" + jwtAuthResponse.getUsername() + " -- " + jwtAuthResponse.getAccessToken());
            return ResponseEntity.ok(jwtAuthResponse);
        } else {
            return new ResponseEntity(new ApiResponse(false, MessageConstants.USERNAME_OR_PASSWORD_INVALID),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/validatetoken")
    public ResponseEntity<?> getTokenByCredentials(@Valid @RequestBody ValidateTokenRequest validateToken) {
        String username = null;
        String jwt = validateToken.getToken();
        logger.info("Start Validate Token: " + jwt);
        // if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == 0) {
                username = tokenProvider.getUsernameFromJWT(jwt);
            User user = userService.findByUsername(username);
            String xAuth = user.getStaffNo();

            // Create our Authentication and let Spring know about it
            logger.info("Start Authentication: " + xAuth);
            Authentication auth = new CustomAuthenticationToken(xAuth);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // If required we can have one more check here to load the user from LDAP server
            return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, MessageConstants.VALID_TOKEN + username));
        } else {
            return new ResponseEntity(new ApiResponse(false, MessageConstants.INVALID_TOKEN), HttpStatus.UNAUTHORIZED);
        }

    }
    
    
    //To gen random questions for remote login (2nd api)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/randomquestion")
    public ResponseEntity<?> RandomQuestion(HttpServletRequest request, HttpSession session) {

//    	  String theAccessChannel;
//          if (loginRequest.getAccessCode().matches(accessCodeMobileApp)) {
//              theAccessChannel = "4";
//          } else if (loginRequest.getAccessCode().matches(accessCodeProxy)) {
//              theAccessChannel = "3";
//          } else {
//              throw new BadCredentialsException("MessageConstants.USERNAME_OR_PASSWORD_INVALID");
//
//          }
    	
    	
        String jwt = request.getHeader("accessToken");
        logger.info("Start Validate Token 2 : " + jwt);
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)==0) {
            Random ran = new Random();
            Integer I = ran.nextInt(3);

            HashMap<String, String> retrun_data = new HashMap<String, String>();

            if (I == 0) {
                retrun_data.put("qID", "1");
                retrun_data.put("questionEN", "Please enter your date of birth (DDMMYYYY)");
                retrun_data.put("questionTC", "請輸入出生日期 (DDMMYYYY)");
                common.storeQuestionSession("1", session);

            } else if (I == 1) {
                retrun_data.put("qID", "2");
                retrun_data.put("questionEN", "Please enter year of joining government");
                retrun_data.put("questionTC", "請輸入入職年份");
                common.storeQuestionSession("2", session);

            } else if (I == 2) {
                retrun_data.put("qID", "3");
                retrun_data.put("questionEN", "Please enter your staff number");
                retrun_data.put("questionTC", "請輸入職員編號");
                common.storeQuestionSession("3", session);

            }

            return ResponseEntity.ok(retrun_data);
        } else {
            return new ResponseEntity(new ApiResponse(false, MessageConstants.HAS_NOT_LOGIN), HttpStatus.UNAUTHORIZED);

        }
    }

    
//    Second auth , will send new token (3rd api)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/twofactorauth")
    public ResponseEntity<?> twoFactorAuth(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
            HttpSession session) {
        String username = null;
//        String question = session.getAttribute("question").toString();
        //System.out.println("Question Id = " + question);
//        String question = request.getHeader("question");

        System.out.println(session.getAttribute("hello"));
        String question = loginRequest.getqID();


        logger.info("Start Two factor Auth");

        String jwt = request.getHeader("accessToken");
        logger.info("Start Validate Token 3 : " + jwt);


        //added by yousuf to bypass
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken("ben", "benspassword"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails uD = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)==0) {
                username = tokenProvider.getUsernameFromJWT(jwt);
                User user = userService.findByUsername(username);
                //System.out.println("User Login Fail Attempt : " +user.getLoginFailAttempt());
                if (user.getLoginFailAttempt() < AppConstants.MAX_LOGIN_FAIL_ATTEMPT) {

                    // Create our Authentication and let Spring know about it
                    logger.info("Start Authentication: " + user.getStaffNo());
                    CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                    if (loginRequest.getqID() != null && loginRequest.getAnswer() != null) {
                        if (isAnsCorrect(loginRequest.getqID(), loginRequest.getAnswer(), question, userDetails)) {

                            // Reset Login fail attempt to Zero
                            if (user.getLoginFailAttempt() > 0) {
                                user.setLoginFailAttempt(0L);
                                userService.updateUserById(user);
                                logger.info("user: " + user.getStaffNo() + "-- LoginFailAttempt: Reset to 0");
                            }
                            //System.out.println("New Log in = "+isNotMultipleLogin(request.getSession().getId(), userDetails));
                            if (isNotMultipleLogin(request.getSession().getId(), userDetails) == true ) {
                                sessionRegistry.registerNewSession(request.getSession().getId(), userDetails);
                                // Create New Session in server after login

                                //Re-generate Token after 2 factor auth
                                logger.info("Going to re-generate Token after 2 factor auth...");

                                // return ResponseEntity
                                //         .ok(new ApiResponse(Boolean.TRUE, MessageConstants.TWO_FACTOR_AUTH_SUCCESS));
                                // Generate Token
                                jwt = tokenProvider.generateTokenDP(userDetails.getUsername());
                                JwtAuthenticationResponse jwtAuthResponse = new JwtAuthenticationResponse(jwt);
                                jwtAuthResponse.setUserId(user.getId());
                                System.out.println("The New Token - "+ jwt + "  - -  User Id  = "+jwtAuthResponse.getUserId());
                                return ResponseEntity.ok(jwtAuthResponse);
                            } else {
                            	logger.info("Before Return Multiple Login (line 449)  -  "+ userDetails.getUsername()+ " Authentication :  "+jwt); 
                                return new ResponseEntity(new ApiResponse(false, MessageConstants.MULTIPLE_LOGIN),
                                        HttpStatus.CONFLICT);
                            }
                        } else {
                            // Record login try
                            int result = this.checkLoginTries(user.getUsername());
                            if (result <= AppConstants.MAX_LOGIN_FAIL_ATTEMPT) { 
//                                return new ResponseEntity(new ApiResponse(false, Integer.toString(result)),
                            	 return new ResponseEntity(new ApiResponse(false, "Incorrect answer","密碼錯誤", Integer.valueOf(user.getLoginFailAttempt().toString()) ),

                                        HttpStatus.UNAUTHORIZED);
                            } else {
                                return new ResponseEntity(new ApiResponse(false, MessageConstants.USER_LOCKED),
                                        HttpStatus.LOCKED);
                            }

                        }
                    }
                }
            }
        }
        return new ResponseEntity(new ApiResponse(false, MessageConstants.HAS_NOT_LOGIN), HttpStatus.UNAUTHORIZED);

    }

    
//    Logout api 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Start logout" + session.getAttribute("user_session"));
        
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                logger.info("Logout Auth... ");
                new SecurityContextLogoutHandler().logout(request, response, auth);
                request.getSession().invalidate();
            }
            /* Getting session and then invalidating it */
            Object user_session = session.getAttribute("user_session");
            User user_session_info = (User) user_session;
            logger.info("Logout user: " + user_session_info.getStaffNo());
            session.invalidate();

            /*
             * This method would edit the cookie information and make JSESSIONID empty while
             * responding to logout. This would further help in order to. This would help to
             * avoid same cookie ID each time a person logs in
             */
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, MessageConstants.LOGOUT_SUCCESS));
        } catch (RuntimeException e) {
          
        	 Object user_session = session.getAttribute("user_session");
             User user_session_info = (User) user_session;
             logger.info("Logout user: " + user_session_info.getStaffNo());
             session.invalidate();

             /*
              * This method would edit the cookie information and make JSESSIONID empty while
              * responding to logout. This would further help in order to. This would help to
              * avoid same cookie ID each time a person logs in
              */
             Cookie[] cookies = request.getCookies();
             for (Cookie cookie : cookies) {
                 cookie.setMaxAge(0);
                 cookie.setValue(null);
                 cookie.setPath("/");
                 response.addCookie(cookie);
             }
             
        	logger.info("Logout fail = "+ e);
        	  return new ResponseEntity(new ApiResponse(false, MessageConstants.LOGOUT_FAIL), HttpStatus.BAD_REQUEST);
        }
    }

    
//    To create new Session
    private void createSession(String staffNo, HttpServletRequest request, Integer access) {
        HttpSession session = request.getSession(true);
        System.out.println("Auth Controller , line 614 ; Create Session (Access Channel) =  "+ access);
    	common.checkApiSession(staffNo, session,access);

    }
    
    
//    To record user login unsuccess
    private Integer checkLoginTries(String username) {
		User user = userService.findByUsername(username);
//		if (user.isPresent()) {
		if(user != null) {
			long loginFailAttempt = user.getLoginFailAttempt();
			logger.info("auth controller : line 625 = " +loginFailAttempt + "  max login fail attempt = "+ AppConstants.MAX_LOGIN_FAIL_ATTEMPT);
			if (loginFailAttempt < AppConstants.MAX_LOGIN_FAIL_ATTEMPT) {
				loginFailAttempt++;
				user.setLoginFailAttempt(loginFailAttempt);
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			    Date loginFailAt = new Date();  
				user.setLoginFailAt(loginFailAt);
				userService.updateUserById(user);
				logger.info("user: " + user.getStaffNo() + "-- Added +1: " + user.getLoginFailAttempt());
						//Has this user and login fail tries less than Max. Login Fail Tries
			}else {
				logger.info("user: " + user.getStaffNo() + "OVER:" + user.getLoginTries());
						//Has this user and login fail tries over than Max. Login Fail Tries
            }
            return (Math.toIntExact(loginFailAttempt));
		}else {
			logger.info("Auth Controller , line642: No such User = " + username);
			return 0;			//No such user
		}
		
	}
    
    
    
//    To check random question has the correct answer
    private Boolean isAnsCorrect(String userQID, String userAns, String qID, CustomUserDetails userDetails){
        String correctAns = "";
//        System.out.println("Auth Controller , line 653 : 1: " + userDetails.getDateOfBirth() + " - 2: " + userDetails.getYearJoiningGovernment() + " ; 3 = "+ userDetails.getStaffNo());
        switch(qID){
            case "1": 
                correctAns = userDetails.getDateOfBirth();
                break;
            case "2": 
                correctAns = userDetails.getYearJoiningGovernment();
                break;
            case "3": 
                correctAns = userDetails.getStaffNo();
                break;
        }
        
        //System.out.println("UserQID: " + userQID + " - SessionQID: " + qID + " ; Anwser = "+ userAns + " == "+ correctAns);

        if(userQID.equalsIgnoreCase(qID)) {
            if (userAns.equalsIgnoreCase(correctAns))
                return true;
            else
                return false;
        }else{
            return false;
        }

    }
    
    
//    To Check User is multiple login or not
    private Boolean isNotMultipleLogin(String sessionID, CustomUserDetails userDetails){
        List<Object> principals = sessionRegistry.getAllPrincipals();
        //System.out.println("Principals Size:"  + principals.size());

        String username = userDetails.getUsername();
        Boolean result = true;
        for (Object principal: principals) {
            if (principal instanceof CustomUserDetails) {
                //System.out.println("Has User");
                String theUsername =  ((CustomUserDetails) principal).getUsername();
                theUsername = theUsername.replaceAll(" ","" );
                theUsername = theUsername.toLowerCase();
                
                username = username.replaceAll(" ", "");
                username = username.toLowerCase();
                
                //System.out.println("theUsername = " + theUsername + " username = "+ username);
                // Do something with user
                //if (theUsername.matches(username)) {
                if(username.equals(theUsername)) {
                    logger.info("Duplicated Login: " + theUsername +" username " + username);
                    result = false;
                    //System.out.println("Result for Duplicated "+ result);
                    break;
                }else {
                    logger.info("New login: " + theUsername);
                   // result = true;
                }
            }else {
                //System.out.println("Not User");
                logger.info("Username: "+ ((CustomUserDetails) principal).getUsername());
               // result = true;
            }
        }
        //System.out.println("Before Return result = "+ result);
        return result;
    }

        //Just For Testing
    //     @RequestMapping(value = "/loggineduser")
    //     public void manageActiveUsers(HttpServletRequest request) {
    
    //         //TODO: Find a better way to get the remote IP Address according to each client call
    //         String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
    //            .getRequest().getLocalAddr();
            
    //         logger.info("RemoteAddress:" + remoteAddress);
            
    //         String username = "ben";
    
    
    //         List<Object> principals = sessionRegistry.getAllPrincipals();
    //         System.out.println("Principals Size:"  + principals.size());
    
    //         for (Object principal: principals) {
    //             if (principal instanceof CustomUserDetails) {
    //                 System.out.println("Has User");
    //                 String theUsername =  ((CustomUserDetails) principal).getUsername();
    //                 // Do something with user
    //                 if (theUsername.matches(username)) {
    //                     logger.info("Duplicated Login: " + theUsername);
    //                     break;
    //                 }else {
    //                     logger.info("New login: " + theUsername);
    //                 }
    
    //             }else {
    //                 System.out.println("Not User");
    //                 System.out.println("principal: "+ principal.toString());
    //                 logger.info("Username: "+ ((CustomUserDetails) principal).getUsername());
    //             }
    //         }
    
            
    //         Authentication authentication = authenticationManager.authenticate(
    //                 new UsernamePasswordAuthenticationToken(
    //                         username,
    //                         "benspassword"
    //                 )
    //         );
    //         SecurityContextHolder.getContext().setAuthentication(authentication);
    // //        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
            
    //         logger.info("Going to Generate Token...");
    //         //Generate Token
    //         String jwt = tokenProvider.generateToken(authentication);
          
    //         JwtAuthenticationResponse jwtAuthResponse =  new JwtAuthenticationResponse(jwt);
    //         System.out.println("token: "  + jwt);
            
    //         if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
    //             CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //             if (isNotMultipleLogin(request.getSession().getId(), userDetails)){
    //                 logger.info("Function - New login: " + username);
    //             }else{
    //                 logger.info("Function - Multiple login: " + username);
    
    //             }
    //             sessionRegistry.registerNewSession(request.getSession().getId(), userDetails);
    //             logger.info("DOB: " +userDetails.getDateOfBirth());
    //         }else {
    //             logger.info("Not loggined");
                
    //         }
    //     }
}
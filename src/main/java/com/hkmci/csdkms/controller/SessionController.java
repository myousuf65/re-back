package com.hkmci.csdkms.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/sessions")
public class SessionController {

	private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

/* Valid function but used for debug ONLY	
	@GetMapping("/listheaders")
	public ResponseEntity<String> listAllHeaders(@RequestHeader Map<String, String> headers) {
	    headers.forEach((key, value) -> {
	        logger.info(String.format("Header '%s' = %s", key, value));
	    });
	    System.out.println("Header = "+ headers);
	    return new ResponseEntity<String>(String.format("Listed %d headers", headers.size()), HttpStatus.OK);
	}	
	
	
	@RequestMapping("/all")
    public ResponseEntity<JsonResult> getAllSession(HttpServletRequest request,HttpSession session) {
		
		HashMap<String, Object> session_data = getSessions(request);
		session_data.forEach((key,value)->{
	        logger.info(String.format("Kenny Header '%s' = %s", key, value));
	    });
		
		return JsonResult.ok(session_data,session);
         //return JsonResult.ok();
    }
*****/
	
	 @GetMapping("/set")
	 @ResponseBody
	 public ResponseEntity<JsonResult> query(HttpServletRequest request,@RequestParam("key") String key,@RequestParam("value") String value) {
		 HttpSession session = request.getSession();
		 Object sessionKey = session.getAttribute(key);
		if (sessionKey == null) {
			session.setAttribute(key,value);
			return JsonResult.ok("Session: " + key + " set successfully.",session);
		} else {
			return JsonResult.ok("Session: " + key + " is already exist.",session);
		}
	 }
	 
	 @RequestMapping("/test/cookie")
	    public ResponseEntity<JsonResult> cookie(@RequestParam("browser") String browser, HttpServletRequest request, HttpSession session) {
	        //get key from session
	        Object sessionBrowser = session.getAttribute("browser");
	        if (sessionBrowser == null) {
	            //System.out.println("No session，set browser=" + browser);
	            session.setAttribute("browser", browser);
	        } else {
	            //System.out.println("Session exist，browser=" + sessionBrowser.toString());
	        }
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null && cookies.length > 0) {
	            for (Cookie cookie : cookies) {
	                //System.out.println(cookie.getName() + " : " + cookie.getValue());
	            }
	        }
	        HashMap<String, Object> session_data = getSessions(request);
	        return JsonResult.ok(session_data,session);
	    }

	 private HashMap<String, Object> getSessions(HttpServletRequest request){
		//Get Session  
		HttpSession session = request.getSession();
		//Get Session Keys 
		Enumeration<String> attrs = session.getAttributeNames();  
		//Loop all  Session
		HashMap<String, Object> session_data = new HashMap<>();
		//List<HashMap<String, Object>> session_data_list = new ArrayList<HashMap<String, Object>>();
		while(attrs.hasMoreElements()){
		// Get session Keys 
			String name = attrs.nextElement().toString();
			// Get Key - Values
			Object value = session.getAttribute(name);
			session_data.put(name, value);
			// Print
			//System.out.println("------" + name + ":" + value +"--------\n");
		}
		return session_data;
	 }

}

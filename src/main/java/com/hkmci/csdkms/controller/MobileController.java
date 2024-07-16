package com.hkmci.csdkms.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.LogMessageProperties;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.MobileVersionService;
import com.hkmci.csdkms.storage.StorageProperties;
import com.hkmci.csdkms.storage.StorageService;










@RestController
@RequestMapping("/mobile")
public class MobileController {
	private final StorageService storageService;
	private final LogMessageProperties logMessage;

	
	@Autowired
	@Resource
    private  StorageService thestorageService;
	
	@Autowired
	public MobileController(StorageService storageService,StorageProperties properties,LogMessageProperties logMessage) {
        this.storageService = storageService;
        this.logMessage = logMessage;
    }
	
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource 
	private AuthController authController;
	
	 
	@Autowired
	@Resource
	private LogService logger;
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@RequestMapping("/IOSipa")
//	public String returnIpa(HttpServletResponse response)throws IOException {
//		Path resourcePath = storageService.getResourceLocation();
//		String file_path =null;
//		file_path = resourcePath.toString() + "/mobile/ios/app.ipa" ;
//		
//		
//		
//		return file_path;
//	}
	
	@Autowired
	@Resource
	private MobileVersionService mobileVersionSercice;
	public static final String uploadDir = System.getProperty("user.dir")+ "/mobile_download";
	

	@RequestMapping("/create/{userId}")
	public ResponseEntity<JsonResult> mobileVersion(@PathVariable Long userId,@RequestBody JsonNode jsonNode,
//			@RequestParam(value="app",required= false) String app,@RequestParam(value="version",required= false) String version,
			HttpSession session ){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			System.out.println("In create mobile version = "+ jsonNode.get("app").asText());
			User user = (User) session.getAttribute("user_session");
			if(user.getId() == 5317L ||user.getId() == 7105L ||user.getId() == 6997L ||user.getId() == 8645L || user.getId() ==5L || user.getId() == 6748L ) {
//				System.out.println("Mobile upload to = "+ uploadDir);
				String app = jsonNode.get("app").asText();
				String version = jsonNode.get("version").asText();
//				mobileVersionSercice.createNewVersion(user, version, app);
//				String filename = "resources/"+thestorageService.storeMobile(file1, app);
//				if(app.contains("ios")) {
//					String filename2 = "resources/"+thestorageService.storeMobile(file2, app);
//				}
//		   	    System.out.println("Mobile upload path = "+ filename);
//				
				
				return JsonResult.ok(mobileVersionSercice.createNewVersion(user, version, app),session);
			} else {
				return JsonResult.errorMsg("You have no right to upload ");
			}
			
		}
		
	}
	
	
	@RequestMapping("/check/{userId}/{app}")
	public ResponseEntity<JsonResult> mobileOS(@PathVariable Long userId, @PathVariable String app, HttpSession session) {
			HashMap<String, String> user_check = common.checkUser(userId, session);
		
			if(user_check.get("msg")!= "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
			
				app = app.toLowerCase();
				System.out.println("Mobile Controller, line 130 : In create mobile version = "+app);
				Integer check = mobileVersionSercice.checkMobileVersion(userId, app);
				System.out.println("Mobile Controller, line 132 , Result = "+ check);
				return JsonResult.ok(check,session);
			}
	}
	
	@RequestMapping("/download/android/{userId}")
	public HttpServletResponse downloadAndriod(@PathVariable Long userId,HttpSession session,HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			//return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			Integer channel = (Integer) session.getAttribute("channel");
			System.out.println("See the channel = "+channel);
			String version = mobileVersionSercice.findAppVersion("android");
			System.out.println(version);
			String file_path = null;
			Path mobilePath = storageService.getMobileLocation();
			file_path = mobilePath.toString() +"/android/"+version+".apk";
			System.out.println(file_path);
			File file = new File(file_path);
			String filename =version+".apk";
			InputStream fis = new BufferedInputStream(new FileInputStream(file_path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename));
			response.addHeader("Content-Length",""+ file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//			response.setContentType("application/vnd.android.package-archive");
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			logger.mobiledownload(user, 1L,version, "success", channel);
			
			
//			System.out.println("File: " + file);
//			InputStream in = new FileInputStream(file);
//			 HttpHeaders headers = new HttpHeaders();
//			 headers.add("Content-Type", "application/vnd.android.package-archive");
//			 
//			
			
//			 
//			return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
			
			
			
	
		}
		   Object user_session = session.getAttribute("user_session");
		   session.invalidate();
           Cookie[] cookies = request.getCookies();
           for (Cookie cookie : cookies) {
               cookie.setMaxAge(0);
               cookie.setValue(null);
               cookie.setPath("/");
               response.addCookie(cookie);
           }

		return response;
		
	}	
	
	

	@RequestMapping("/download/ios/{userId}")
	public String downloadIOS(@PathVariable Long userId,HttpSession session) throws IOException {
		System.out.println("Can get into download IOS ");
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			//return JsonResult.errorMsg(user_check.get("msg").toString());
			return "";
		}else{
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			Integer channel = (Integer) session.getAttribute("channel");
			System.out.println("See the channel = "+channel);
			
			String version = mobileVersionSercice.findAppVersion("ios");
			String file_path = "Test test";
			Path mobilePath = storageService.getMobileLocation();
	    	//file_path = mobilePath.toString() + "/ios/manifest.plist" ;
			System.out.println("File path: " + file_path);

//			String file_path = null;
//			Path resourcePath = storageService.getResourceLocation();
//			file_path = resourcePath.toString() +"/mobile/"+version+".plist";
//			File file = new File(file_path);
//			String filename =version+".plist";
//			InputStream fis = new BufferedInputStream(new FileInputStream(file_path));
//			byte[] buffer = new byte[fis.available()];
//			fis.read(buffer);
//			fis.close();
//			response.reset();
//			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename));
//			response.addHeader("Content-Length",""+ file.length());
//			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
////			response.setContentType("application/vnd.android.package-archive");
//			response.setContentType("application/octet-stream");
//			toClient.write(buffer);
//			toClient.flush();
//			toClient.close();
			logger.mobiledownload(user, 2L, version, "success", channel);
			
			
//			System.out.println("File: " + file);
//			InputStream in = new FileInputStream(file);
//			 HttpHeaders headers = new HttpHeaders();
//			 headers.add("Content-Type", "application/vnd.android.package-archive");
//			 
//			
			
//			 
//			return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
			
			
			return file_path;
	
		}
		
		
	}	

	
	@RequestMapping("/notification/{userId}")
	public ResponseEntity<JsonResult> sentNotification(@PathVariable Long user, @RequestBody JsonNode jsonNode, 
			HttpSession session){
		HashMap<String,String> user_check = common.checkUser(user, session);
		if(user_check.get("msg") !="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else { 
			String message = jsonNode.get("Message ").toString();
			String app = jsonNode.get("App").toString();
			
			String appKeyToken = "YOUR_APP_KEY";
			String userKeyToken = "YOUR_USER_TOKEN";
			
			
			
			
			return null;
		}
	}
	/*@RequestMapping("/notification/{userId}")
	public ResponseEntity<JsonResult> sentNotification(@PathVariable Long userId,@RequestBody JsonNode jsonNode,
			HttpSession session ){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			
			String message = jsonNode.get("Title").toString();
			
			String app = jsonNode.get("app").asText();
			
			String appKeyToken = "M2UwZDAxNzEtMjg4Mi00ODg1LTk4NGYtNDc0OWZhNTA4MzY4";
			System.out.println("Mobile Controller , line 288 : App - "+ app + " message - " + message);
			
			
			ApiClient defaultClient = Configuration.getDefaultApiClient();
			    defaultClient.setBasePath("https://onesignal.com/api/v1");
			   
			    DefaultApi apiInstance = new DefaultApi(defaultClient);
			    
			    
			    String app_key = "edd3bc33-b82c-4a58-84c2-fc41bbaeff0d";
			    HttpBearerAuth appKey = (HttpBearerAuth) defaultClient.getAuthentication(appKeyToken);
								    
			    System.out.println("Mobile Controller ,line 304:  app Key = "+appKey);
			    
			    
			    
			    
			    
			    Notification notification = new Notification(); // Notification | 
			    try {
			      CreateNotificationSuccessResponse result = apiInstance.createNotification(notification);
			      System.out.println(result);
			    } catch (ApiException e) {
			      System.err.println("Exception when calling DefaultApi#createNotification");
			      System.err.println("Status code: " + e.getCode());
			      System.err.println("Reason: " + e.getResponseBody());
			      System.err.println("Response headers: " + e.getResponseHeaders());
			      e.printStackTrace();
			    }
			
			
			
			
			
			
			return null;
		}
	}*/
	
}

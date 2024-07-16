package com.hkmci.csdkms.controller;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.Banner;
import com.hkmci.csdkms.entity.Pin;
//import com.hkmci.csdkms.entity.Pin;
import com.hkmci.csdkms.model.PinReturnModel;
import com.hkmci.csdkms.service.PinService;
import com.hkmci.csdkms.storage.StorageService;

@CrossOrigin
@RestController
@RequestMapping("/pin")
public class PinController {

	
//	
//	
	@Autowired
	@Resource
    private  StorageService thestorageService;
	
	
	@Autowired
	@Resource
	private Common common;
	public static final String uploadDir = System.getProperty("user.dir")+"/pin_icon/";
	
	@Autowired
	@Resource
	private PinService thepinService;
	
	
	
	@RequestMapping("/update/{userId}")
		public ResponseEntity<JsonResult> editPin(@RequestParam(value="file",required= false)  MultipartFile file, 
				@PathVariable Long userId, @RequestParam(value="name", required = false, defaultValue ="") String name,
				@RequestParam(value="nameTc", required = false, defaultValue="") String nameTc, @RequestParam(value="description", required=false ,defaultValue="") String description,
				@RequestParam(value="staffNo", required = false, defaultValue ="") String staffNo,@RequestParam(value="id", required = false, defaultValue ="") Long pinId,
				HttpSession session ) throws Exception,Throwable{
			
			
//			     Banner return_data = bannerService.findById(bannerId).get();
				Pin return_data = thepinService.findById(pinId).get();
				if (file != null ) {
					//System.out.println(" file is not null ");
		   	    	String filename = "resources/"+thestorageService.storePin(file);
		   	    	//System.out.println("filename = "+ filename);
		   	    	
		   	    	return_data.setImageUrl(filename);
				} else {
					//System.out.println("file = null " );
				}
				return_data.setDescription(description);				
				return_data.setName(name);
				return_data.setNameTc(nameTc);
//				return_data.setAccessRuleId(accessRule);
				
				String staff_mc = thepinService.updateStaffNo(pinId, staffNo, userId,session);
				
				Pin saved_pin = thepinService.savePin(return_data);
			return JsonResult.ok(saved_pin,session);
		}
	
	
	
	
	
	@RequestMapping("/check/{userId}")
	public ResponseEntity<JsonResult> displayPin(@PathVariable Long userId, HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			return JsonResult.ok(thepinService.displayPin(userId) ,session);
			
		}
	}
	
	
	
	
	@RequestMapping("/create/{userId}")
	public ResponseEntity<JsonResult> createPin(@RequestParam("file") MultipartFile file, 
			@PathVariable Long userId, @RequestParam(value="name", required = false, defaultValue ="") String name,
			@RequestParam(value="nameTc", required = false, defaultValue="") String nameTc, @RequestParam(value="description", required=false ,defaultValue="") String description,
			@RequestParam(value="staffNo", required = false, defaultValue ="") String staffNo,
			HttpSession session ) throws Exception,Throwable{
		
		HashMap<String, String> user_check = common.checkUser(userId,session);
		 
	    	String filename = "resources/"+thestorageService.storePin(file);
	    	
	    	
	    	if(user_check.get("msg") != "") {
   				return JsonResult.errorMsg(user_check.get("msg").toString());
   			} else {
   				   				
   				PinReturnModel new_pin = new PinReturnModel();
   				new_pin.setCreatedAt(new Date());
   				new_pin.setCreatedBy(userId);
   				new_pin.setDescription(description);
   				new_pin.setImageUrl(filename);
   				new_pin.setIsDeleted(0);
   				new_pin.setName(name);
   				new_pin.setNameTc(nameTc);
   				new_pin.setStaffNo(staffNo);
   				
   				PinReturnModel saved_pin = thepinService.save(new_pin,session);
   				return JsonResult.ok(saved_pin, session);
   			} 
	}
	

	@RequestMapping("/delete/{userId}/{pinId}")
	public ResponseEntity<JsonResult> deletePin(@PathVariable Long userId,@PathVariable Long pinId,HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId,session);
    	if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
				thepinService.deletepin(pinId, userId);
				return JsonResult.ok(null, session);
			}
		
	}
	
	
	
	@RequestMapping("/all/{userId}")
	public ResponseEntity<JsonResult> showPin(@PathVariable Long userId,HttpSession session){
		
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
				
				
				return JsonResult.ok(thepinService.show(session),session);
			}
		
	}

}

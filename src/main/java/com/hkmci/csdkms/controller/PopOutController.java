package com.hkmci.csdkms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.hkmci.csdkms.entity.PopOut;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.PopOutService;
import com.hkmci.csdkms.storage.StorageService;
;

@CrossOrigin
@RestController
@RequestMapping("/popout")
public class PopOutController {
	@Autowired
	@Resource
    private  StorageService thestorageService;
	
	
	@Autowired
	@Resource
	private Common common;
	public static final String uploadDir = System.getProperty("user.dir")+"/pop_out/";
	
	@Autowired
	@Resource
	private PopOutService popOutService;
	


	@Autowired
	@Resource
	private AccessRuleService accessRuleService;
	
	// get user access rule from session, if find and assign it into session 
		@SuppressWarnings("unchecked")
		private List<Long> getAccessRuleIds(HashMap<String, String> user_check,HttpSession session) {
			Object user_access_rule_session = session.getAttribute("user_access_rule_session");
			
			Long section = Long.parseLong(user_check.get("user_section").toString());
			Long institution = Long.parseLong(user_check.get("user_institution").toString());
			Long rank = Long.parseLong(user_check.get("user_rank").toString());
			
			
	       if (user_access_rule_session == null) {
	       	List<Long> accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
	           session.setAttribute("user_access_rule_session", accessRuleId);
	           return accessRuleId;
	       } else {
	           return (List<Long>) user_access_rule_session;
	       }
		}
	
		
		
	@RequestMapping("/homepage/{user_id}")
	public ResponseEntity<JsonResult> getHoPopOut(@PathVariable Long user_id, HttpSession session){
			
		HashMap<String,String> user_check = common.checkUser(user_id, session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Integer channel = (Integer) session.getAttribute("channel");
				
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
				
			return JsonResult.ok(popOutService.getPopOut(accessRuleId,channel),session);
		}
			
		}
		
	
	@RequestMapping("/all/{user_id}")
	public ResponseEntity<JsonResult> getPopOut(@PathVariable Long user_id, HttpSession session){
		
		HashMap<String,String> user_check = common.checkUser(user_id, session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			
		
			
			return JsonResult.ok(popOutService.getAllPopOut(),session);
		}
		
	}
	
	
	
	@RequestMapping("/create/{userId}")
	public ResponseEntity<JsonResult> createPopOut(@RequestParam("file") MultipartFile file, @RequestParam("accessChannel") String accessChannel,
			@PathVariable Long userId, @RequestParam(value="accessRule", required = false, defaultValue ="") String AccessRule,
			@RequestParam(value="hypryLink", required = false, defaultValue="") String hypryLink,
			HttpSession session ) throws Exception,Throwable{
		
		HashMap<String, String> user_check = common.checkUser(userId,session);
		 
	    	String imageUrl = "resources/"+thestorageService.storePopOut(file);
	    	
	    	
	    	
	    	if(user_check.get("msg") != "") {
   				return JsonResult.errorMsg(user_check.get("msg").toString());
   			} else {
   				   				
   				PopOut new_popOut = new PopOut();
   				new_popOut.setAccessRule(AccessRule);
   				new_popOut.setHypryLink(hypryLink);
   				new_popOut.setImageUrl(imageUrl);
   				new_popOut.setIsDeleted(0);
   				new_popOut.setCreateBy(userId);
   				new_popOut.setCreateAt(new Date());
   				new_popOut.setAccessChannel(accessChannel);
   				PopOut saved_popout = popOutService.savePopOut(new_popOut,session);
   				return JsonResult.ok(saved_popout, session);
   			} 
	}
	
	
	@RequestMapping("/delete/{userId}/{pinId}")
	public ResponseEntity<JsonResult> deletePopOut(@PathVariable Long userId,@PathVariable Long pinId,HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId,session);
    	if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
				popOutService.deletePopOut(pinId, userId);
				return JsonResult.ok(null, session);
			}
		
	}
	
	
	
	@RequestMapping("/update/{userId}")
		public ResponseEntity<JsonResult> editPopOut(@RequestParam(value="file",required= false)  MultipartFile file, 
				@PathVariable Long userId, @RequestParam(value="accessRule", required = false, defaultValue ="") String AccessRule,
				@RequestParam("accessChannel") String accessChannel, @RequestParam(value="hypryLink", required = false, defaultValue="") String hypryLink, @RequestParam(value="id", required = false, defaultValue ="") Long popOutId,
				HttpSession session ) throws Exception,Throwable{
			
			
//			     Banner return_data = bannerService.findById(bannerId).get();
				PopOut return_data = popOutService.findById(popOutId).get();
							
				
				if (file != null ) {
					//System.out.println(" file is not null ");
					String filename = "resourcs/"+ thestorageService.storePopOut(file);
		   	    	
		   	    	return_data.setImageUrl(filename);
				} else {
					//System.out.println("file = null " );
				}
				return_data.setAccessRule(AccessRule);
				return_data.setHypryLink(hypryLink);
				return_data.setAccessChannel(accessChannel);
				return_data.setIsDeleted(0);
				return_data.setModifiedAt(new Date());
				return_data.setModifiedBy(userId);
				
				PopOut saved_pin = popOutService.savePopOut(return_data, session);
			return JsonResult.ok(saved_pin,session);
		}
	
	


}

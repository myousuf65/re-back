package com.hkmci.csdkms.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.Banner;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.BannerModel;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.BannerAccessRuleService;
import com.hkmci.csdkms.service.BannerService;
import com.hkmci.csdkms.storage.StorageService;

@CrossOrigin
@RestController
@RequestMapping("/banner")
public class BannerController {
	@Autowired
	@Resource
	private BannerService bannerService;
	
	@Autowired
	@Resource
	private FileUploadController fileUploadController;
	@Autowired
	@Resource
	private BannerAccessRuleService bannerAccessRuleService;
	@Autowired
	@Resource
    private  StorageService thestorageService;
	
	@Autowired
	@Resource
	private Common common;
	
	@Autowired
	@Resource
	private AccessRuleService accessRuleService;
	
// get service path (D://csdkms/backend/ +/upload_banner
   public static final String uploadDir = System.getProperty("user.dir") + "/upload_banner/";
   
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
	
	
   		@RequestMapping("/all/{userId}/{level}")
   			public ResponseEntity<JsonResult> findAll(@PathVariable Long userId,@PathVariable Integer level,HttpSession session){

			
   			HashMap<String, String> user_check = common.checkUser(userId,session);
   			if(user_check.get("msg") != "") {
   				return JsonResult.errorMsg(user_check.get("msg").toString());
   			}else {
   				User user = (User) session.getAttribute("user_session");
   				if( user.getUsergroup() == 5) {
   					//System.out.println("level" + level);
   					List<Banner> return_data= bannerService.findAll(level);
   					return JsonResult.ok(return_data,session);
   				}else {
   					return JsonResult.errorMsg("Dun have right to get ");
   				}
   			}
   			
   			
   		}
   		
   		@RequestMapping("/all/top/{userId}")
   			public ResponseEntity<JsonResult> findTop(@PathVariable Long userId,HttpSession session){
   				Integer channel = (Integer) session.getAttribute("channel");
			
   				return  JsonResult.ok(bannerService.findTop(channel),session);
   			}

   		@RequestMapping("/all/general/{userId}")
   			public ResponseEntity<JsonResult> findGeneral(@PathVariable Long userId,HttpSession session){
   			Integer channel = (Integer) session.getAttribute("channel");
			
   			HashMap<String, String> user_check = common.checkUser(userId,session);
   			
   			if(user_check.get("msg") != "") {
   				return JsonResult.errorMsg(user_check.get("msg").toString());
   			}else{
   				List<Long> accessRuleId = getAccessRuleIds(user_check,session);
   				User user = (User) session.getAttribute("user_session");
   				Integer is_admin = common.checkAdmin(user, session);
   				
   				List<Banner> banner_list = bannerService.findGeneral(channel);
   				//System.out.println("Banner List Size = "+ banner_list.size());
   				List<BannerModel> return_banner = banner_list.stream()
   												  .filter(
   														  (n) -> {
   															  List<Long> banner_access_rule = Arrays.asList(n.getAccessRuleId().split(","))
   																	  							.stream()
   																	  							.map((l) -> {return Long.parseLong(l);})
   																	  							.collect(Collectors.toList());
   															  List<Long> return_result = accessRuleId.stream()
   																	  					 .filter(ar -> banner_access_rule.contains(ar))
   																	  					 .collect(Collectors.toList());
   															  if(is_admin.equals(1)) {
   																  return true;
   															  }else {
   																  if(return_result != null && return_result.size() > 0) {
   																	  return true;
   																  }else {
   																	  return false;
   																  }
   															  }
   														  }
   												  )
   												  .map(
   														  (n) -> {
   															  BannerModel return_temp = new BannerModel();
   															  return_temp.setId(n.getOrderby());
   															  return_temp.setImgUrl(n.getImgUrl());
   															  return_temp.setTarget(n.getName());
   															  return_temp.setTargetTc(n.getName_tc());
   															  return_temp.setTargetUrl(n.getLinkTo());
   															  return return_temp;
   														  }
   												  )
   												  .collect(Collectors.toList());
   				//.sorted(Comparator.comparing(CatAllModel::getParentCatId))
   				return JsonResult.ok(return_banner,session);
   			}
   		}
//   		@RequestMapping("/")
//   			public BannerModel updateBannerModel(@RequestBody BannerModel TheModel) {
//   				bannerService.save(TheModel);
//   				return TheModel;
//   		}
   		
//   		@RequestMapping("/")
//   		public String uploadFiles (@RequestBody JsonNode jsonNode) throws IOException{
//   			BannerModel new_banner = new BannerModel();
//   			
//   			
//   			
//   			for (MultipartFile f : TheModel) {
//   				File file = new File (uploadDir + f.getOriginalFilename());
//   				f.transferTo(file);
//   			}
//   		}

   		@RequestMapping("/{Id}")
   			public Optional<Banner> getBannerController(@PathVariable Long Id) {
   				return bannerService.findById(Id);
   			}
   		
   		
   		@RequestMapping("/delete/{userId}/{bannerId}")
   		public ResponseEntity<JsonResult> deleteBanner(@PathVariable Long userId, @PathVariable Long bannerId, HttpSession session)
   		throws  Exception, Throwable{
   			HashMap<String, String> user_check = common.checkUser(userId,session);
   			if(user_check.get("msg") != "") {
   				return JsonResult.errorMsg(user_check.get("msg").toString());
   			} else {
   				User user = (User) session.getAttribute("user_session");
   				if(user.getUsergroup() == 5) {
   					bannerService.deleteBanner(bannerId, userId);
   					return JsonResult.ok(session);
   				}else {
   					return JsonResult.errorMsg("You dun have right to delete banner");
   				}
   			}
   		}
   		
   		@RequestMapping("/update/{userId}")
   		public ResponseEntity<JsonResult> editBanner(@RequestParam(value="file",required= false) MultipartFile file 
   				, @PathVariable Long userId,@RequestParam("id") Long bannerId, @RequestParam("categoryId") String categoryId, @RequestParam("orderBy") Integer orderBy,
   				@RequestParam("accessChannel") String accessChannel, @RequestParam("level") String level,@RequestParam(value ="name", required = false ,defaultValue ="") String name,
   				@RequestParam(value="nameTc",required= false, defaultValue ="")  String nameTc, @RequestParam(value = "accessRule", required = false , defaultValue ="") String accessRule,  HttpSession session  ) throws Exception, Throwable{
   				
   			
   			     Banner return_data = bannerService.findById(bannerId).get();
   			
   				if (file != null ) {
   					//System.out.println(" file is not null ");
   		   	    	String filename = "resources/"+thestorageService.storeBanner(file);
   		   	    	//System.out.println("filename = "+ filename);
   		   	    	return_data.setImgUrl(filename);
   				} else {
   					//System.out.println("file = null " );
   				}
   				return_data.setLinkTo(categoryId);
   				return_data.setOrderby(orderBy);
   				return_data.setAccessChannel(accessChannel);
   				return_data.setLevel(level);
   				return_data.setName(name);
   				return_data.setName_tc(nameTc);
   				return_data.setAccessRuleId(accessRule);
   				
   				
   				Banner saved_banner = bannerService.save(return_data);
   			return JsonResult.ok(saved_banner,session);
   		}
   		
   		@RequestMapping("/orderupdate/{userId}")
   		public ResponseEntity<JsonResult> updateOreser(@PathVariable Long userId, @RequestBody JsonNode jsonNode,
   				HttpSession session ) throws Exception, Throwable{
   			   HashMap<String, String> user_check = common.checkUser(userId, session);
   			   if(user_check.get("msg") != "") {
   				return JsonResult.errorMsg(user_check.get("msg").toString());
   			   } else {
   				   for(Integer i = 0 ; i < jsonNode.get("order").size() ; i++) {
   					  Banner return_data = bannerService.findById(jsonNode.get("order").get(i).get("id").asLong()).get();
   					 
   					  return_data.setOrderby(jsonNode.get("order").get(i).get("orderBy").asInt());
   					bannerService.save(return_data);
   				   }
   				   
   				   return JsonResult.ok(session);
   			   }
   			   
   			
   		}
   		
   		
   		@RequestMapping("/create/{userId}")
   		public ResponseEntity<JsonResult> createBanner(@RequestParam("file") MultipartFile file,
   				@PathVariable Long userId ,@RequestParam("categoryId") String categoryId,@RequestParam("accessChannel") String accessChannel,
   				@RequestParam("level") String level,@RequestParam("orderBy") Integer orderBy, @RequestParam(value ="name", required = false ,defaultValue ="") String name,
   				@RequestParam(value="nameTc",required= false, defaultValue ="")  String nameTc, @RequestParam(value = "accessRule", required = false , defaultValue ="") String accessRule,
   				HttpSession session) throws Exception, Throwable{
   			
   			//System.out.println("Upload Dir Path = "+ uploadDir);
   			
   			HashMap<String, String> user_check = common.checkUser(userId,session);
 
   	    	String filename = "resources/"+thestorageService.storeBanner(file);
   	    	//System.out.println("filename = "+ filename);
   			if(user_check.get("msg") != "") {
   				return JsonResult.errorMsg(user_check.get("msg").toString());
   			} else {
   				Banner new_banner = new Banner();
   				
   				new_banner.setAccessRuleId(null);
   				//System.out.println("Link to ="+ categoryId);
   				new_banner.setLinkTo(categoryId);
   				new_banner.setCreateAt(new Date());
   				new_banner.setCreateBy(userId);
   				new_banner.setIsDeleted(0);
   				new_banner.setImgUrl(filename);
   				new_banner.setAccessRuleId(accessRule);
   				new_banner.setName(name);
   				//System.out.println("name Tc = " + nameTc);
   				new_banner.setName_tc(nameTc);
   				
   				new_banner.setLevel(level );
   				
   				new_banner.setOrderby(orderBy);
   				
   				new_banner.setAccessChannel(accessChannel);
   				
   				
   				Banner saved_banner = bannerService.save(new_banner);
   				
   				
   				//System.out.println("Saved Banner = " + saved_banner.getId());
   				
   	   			return JsonResult.ok(saved_banner, session);
   				
   			}
   		
   		}

}

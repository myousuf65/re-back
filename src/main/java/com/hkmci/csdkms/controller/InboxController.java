package com.hkmci.csdkms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.arjuna.common.logging.commonI18NLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.EmailUtil;
import com.hkmci.csdkms.entity.Uinbox;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserDog;
import com.hkmci.csdkms.model.ListBoxReturn;
import com.hkmci.csdkms.service.ResourceService;
import com.hkmci.csdkms.service.UinboxService;
import com.hkmci.csdkms.service.UserDogService;
import com.hkmci.csdkms.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/inbox")
public class InboxController {
	
	
	
	@Autowired
	@Resource
	private Common common;
	
	@Autowired
	@Resource
	private UinboxService uinboxService;
	
	
	@Autowired
	@Resource
	private ResourceService resourceService;
	
	
	@Autowired
	@Resource
	private UserDogService userDogService;
	
	
	@RequestMapping("/myList/read/{userId}")
	private ResponseEntity<JsonResult> readMail(@PathVariable Long userId, @RequestBody JsonNode jsonNode, HttpSession session ){
		HashMap<String, String> user_check =common.checkUser(userId, session);
		if(user_check.get("msg") != "" ) {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			List<Uinbox> save_data = new ArrayList<>();
			for(Integer i = 0 ; i < jsonNode.get("refs").size(); i++) {
				Long id = jsonNode.get("refs").get(i).asLong();
				Uinbox return_data = uinboxService.getInboxInfo(id);
				return_data.setIsRead(1);
				return_data.setReadTime(new Date());
				save_data.add(return_data);
			} 
			return JsonResult.ok(uinboxService.saveAll(save_data),session);
			
		}
		
	}
	
	@RequestMapping("/myList/delete/{userId}")
	private ResponseEntity<JsonResult> deleteMail(@PathVariable Long userId,@RequestBody JsonNode jsonNode, HttpSession session){
		
		HashMap<String, String> user_check = common.checkUser(userId, session);
		List<Uinbox> save_data = new ArrayList<>();
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else { 
			for(Integer i = 0 ; i<jsonNode.get("refs").size();i++) {
				Long id = jsonNode.get("refs").get(i).asLong();
				Uinbox return_data = uinboxService.getInboxInfo(id);
				return_data.setIsDeleted(1);
				return_data.setDeletedAt(new Date());
				return_data.setDeletedBy(userId);
				save_data.add(return_data);
			}
			return JsonResult.ok(uinboxService.saveAll(save_data),session);
			
		}
	}
	
	
	
	@RequestMapping("/notice/{userId}")
	private ResponseEntity<JsonResult> noticeMail(@PathVariable Long userId, HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId, session);
		
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			
			User user = (User) session.getAttribute("user_session"); 
			UserDog userDog = new UserDog();
			if (user.getScore()>499) {
			String dog = "";
			dog=userDogService.getUserDog(userId);
			
			if(dog=="" || dog==null) {
				
				
				System.out.println("Inbox Controller : line 119 -- No dog dog");
				  Random rand = new Random();
				  int upperbound = 4;
				  int int_random = rand.nextInt(upperbound); 
				  
//				  
//				  if (int_random ==1 ) {
//					  userDog.setCreatedAt(new Date());
//					  userDog.setCreatedBy(userId);
//					  userDog.setUserDog("A");
//					  userDog.setDogLevel1("A");
////				  }
				  System.out.println("Inbox Controller : line 134 = int random : "+ int_random);
				  switch (int_random) {
				  	case 1:
				  		userDog.setCreatedAt(new Date());
				  		userDog.setCreatedBy(userId);
				  		userDog.setUserDog("A");
				  		userDog.setDogLevel1("A");
					  break;
				  	case 2:
				  		 userDog.setCreatedAt(new Date());
						 userDog.setCreatedBy(userId);
						 userDog.setUserDog("B");
						 userDog.setDogLevel1("B");
						break;
				  	case 3: 
				  		 userDog.setCreatedAt(new Date());
						 userDog.setCreatedBy(userId);
						 userDog.setUserDog("C");
						 userDog.setDogLevel1("C");
						break;
				  	case 4: 
				  		 userDog.setCreatedAt(new Date());
						 userDog.setCreatedBy(userId);
						 userDog.setUserDog("D");
						 userDog.setDogLevel1("D");
						break;
				  }
				  
				System.out.println("Inbox Controller : line 162 : "+ userDog.getDogLevel1());
				userDogService.saveLevelDog(userDog);
				
				
				HashMap<String, String> return_data = new HashMap<>();
				return_data.put("dog", userDog.getUserDog());
				
				
				Integer mailNumber = uinboxService.numberMail(userId);
				
				
				return JsonResult.ok(mailNumber,return_data,session);
				
			} else {
				HashMap<String, String> return_data = new HashMap<>();
				return_data.put("dog",dog);
				Integer mailNumber = uinboxService.numberMail(userId);
				return JsonResult.ok(mailNumber,return_data,session);
			}
			
			
			
			
			
			} else {
				
				
				Integer mailNumber = uinboxService.numberMail(userId);
				
				
				return JsonResult.ok(mailNumber,"",session);
				
			}
				
		}
	}
	
	
	
	@RequestMapping("/myList/{userId}")
	private ResponseEntity<JsonResult> myListBox(@PathVariable Long userId, HttpSession session ){
		
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			List<User> user_session_list = (List<User>) session.getAttribute("user_list");
			List<Object []> return_data = uinboxService.getMail(userId);
			List<ListBoxReturn> final_data = new ArrayList<>();
			for(Integer j = 0 ; j < return_data.size() ; j++) {
				ListBoxReturn list_return = new ListBoxReturn();
				Object[] data = (Object []) return_data.get(j);
			
				list_return.setId(Long.valueOf(data[0].toString()));
				list_return.setCreatedAt((Date)data[2]);
				list_return.setIsRead(Integer.valueOf(data[5].toString()));
				
				 System.out.println(" Data [0] = "+data[0]+" Data [1] = "+ data[1] + " Data [7] = "+ data[7]+ " Data [10] " +  data[10]);
	 			if ( data[10].toString().equals("1") ) {  
	 				System.out.println("data [1] not equal null : data[1]  = "+ data[1]);
	 				list_return.setResourceId(Long.valueOf(data[1].toString()));
					list_return.setTitleEn(data[7].toString());
					System.out.println("Resource Title ENG = "+data[7].toString());
					System.out.println("Resource Title CHINESE = "+data[6].toString());
					list_return.setTitleTc(data[6].toString());
	 				list_return.setType("resource");
				} else if  ( data[10].toString().equals("2") )  {

	 				list_return.setResourceId(Long.valueOf(data[8].toString()));
					list_return.setTitleEn(data[9].toString());
					list_return.setTitleTc(data[9].toString());
					list_return.setType("blog");
	 				
				} else {
					list_return.setResourceId(Long.valueOf(data[8].toString()));
					list_return.setTitleEn(data[9].toString());
					list_return.setTitleTc(data[9].toString());
					list_return.setType("cocktail");
				}
	 	
				System.out.println("Send By data[3] = "+data[3].toString() + "  user_session_list size = "+ user_session_list.size());
//				List<String> user_name =  user_session_list.stream().filter((n)->n.getId() == Long.valueOf(data[3].toString()))
//				.map((n) -> {return n.getFullname();})
				List<String> user_name =  user_session_list.stream().filter((n)->n.getId().equals(Long.valueOf(data[3].toString())) )
						.map((n) -> {return n.getFullname();})
				.collect(Collectors.toList());
				System.out.println("Send By : "+ user_name);
				list_return.setSendBy(user_name.get(0));
				final_data.add(list_return);
				
			}
			return JsonResult.ok(final_data,session);
		}
	}
	
	
	
	
	@RequestMapping("/share/{userId}")
	private ResponseEntity<JsonResult> shareDocument(@PathVariable Long userId, 
			@RequestParam("resourceId") Long resourceId, @RequestParam("sendTo") List<String> sendTo, 
			HttpSession session) throws Exception, Throwable{
			HashMap<String,String> user_check = common.checkUser(userId, session);
			if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
				List<User> user_session_list = (List<User>) session.getAttribute("user_list");
				User user = (User) session.getAttribute("user_session"); 
				String resource_title = resourceService.getResourceNameById(resourceId);
				Integer resource_access_channel = resourceService.getResourceAccessChannelById(resourceId);
				List<Long> user_list = dealWithSentTo(sendTo, user_session_list);
//				System.out.println("user list : " + user_list);
				List<Uinbox> shareToList = new ArrayList<>();
				for(Integer i = 0 ; i < user_list.size() ; i++ ) {
//					System.out.println("Send to = "+ user_list.get(i));
					Uinbox new_share = new Uinbox();
					new_share.setCreatedAt(new Date());
					new_share.setSendBy(userId);
					new_share.setIsDeleted(0);
					new_share.setIsRead(0);
					new_share.setResourceId(resourceId);
					new_share.setSendTo(user_list.get(i));
					uinboxService.save(new_share);
					
					

				}
				
				List<User> sendEmaiList = user_list.stream()
						  .map((u) -> {
							  User temp_user = user_session_list
									  		  .stream()
									  		  .filter(n -> n.getId().equals(u))
									  		  .collect(Collectors.toList()).get(0);
//							System.out.println("Temp_User:" + temp_user.getStaffNo() + "--" + temp_user.getEmail() + "--" + temp_user.getNotesAccount());
							
							temp_user.setEmail((user_session_list.stream()
									 .filter((n) -> n.getId().equals(temp_user.getId()))
									 .map(User::getEmail)
									 .collect(Collectors.toList())
									 .get(0)));
//							System.out.println("Temp_User222:" + temp_user.getStaffNo() + "--" + temp_user.getEmail());
							  return temp_user;
						  })
						  .collect(Collectors.toList());
				
				EmailUtil.sendEmailToShareResource(sendEmaiList, resource_title, user.getFullname(),resource_access_channel,resourceId);
				return JsonResult.ok(session);
				
			}
			
	}
	
	List<Long> dealWithSentTo(List<String> sendTo, List<User> user_session_list){
//		System.out.println("sendTo size "+sendTo.size());
		List<Long> return_data = new ArrayList<>();
		for(Integer i = 0 ; i<sendTo.size() ;i++ ) {
			Integer x = i;
			List<Long> userId = new ArrayList<>();
			
			 userId = user_session_list.stream()
						.filter((n) -> n.getStaffNo().equals(sendTo.get(x)))
						.map((n) -> {return n.getId();})
						.collect(Collectors.toList());
				
			return_data.add(userId.get(0));		
		}
		return return_data;
	}
	

}

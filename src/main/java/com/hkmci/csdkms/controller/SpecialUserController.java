package com.hkmci.csdkms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.SpecialUserGroupModel;
import com.hkmci.csdkms.model.SpecialUserGroupUserModel;
import com.hkmci.csdkms.service.SpecialUserGroupService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.service.UserSpecialGroupService;

@CrossOrigin
@RestController
@RequestMapping("/splusrgrp")
public class SpecialUserController {

	@Autowired
	@Resource
	private UserService userService;
	
	@Autowired
	@Resource
	private SpecialUserGroupService specialUserGroupService;
	
	
	@Autowired
	@Resource
	private UserSpecialGroupService userSpecialGroupService;
	
	
	@Autowired
	@Resource
	private Common common;
	
	
	@RequestMapping("/getAll/{userId}")
	public ResponseEntity<JsonResult> getAll(@PathVariable Long userId, HttpSession session){
		
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!= "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			User user = (User) session.getAttribute("user_session");
			if(user.getUsergroup() == 5) {
				return JsonResult.ok(specialUserGroupService.getAll(),session);
			} else {
				
				List <SpecialUserGroupModel> return_data = specialUserGroupService.getSameInstAndSession(user.getInstitutionId(), user.getSectionId());
				//System.out.println("return_data = " + return_data.size());
				return JsonResult.ok(return_data,session);
			}
		}
	}
	
	
	

	@RequestMapping("/search/{userId}")
	public ResponseEntity<JsonResult> search(@PathVariable Long userId, 
											@RequestParam(value="page",required = true)Integer page,
											@RequestParam(value="searchId",required= false, defaultValue="0") Long groupId,
											@RequestParam(value="searchName",required=false, defaultValue="") String groupName,
											@RequestParam(value="searchStaff",required=false, defaultValue="") String staffNo,
											HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			User user = (User) session.getAttribute("user_session");
			//System.out.println("User Inst id = "+ user.getInstitutionId()+ " Session Id "+user.getSectionId());
			if(user.getUsergroup() == 5) {
				
				return JsonResult.ok(specialUserGroupService.search(userId, page, groupName, staffNo,groupId),session);
			} else {
				return JsonResult.errorMsg("You have no right to search special user group. ");
			}
			 
			
		}
		
	}
	
	@RequestMapping("/create/{userId}")
	public ResponseEntity<JsonResult> create(@RequestBody JsonNode jsonNode, @PathVariable Long userId,
			HttpSession session){
		Integer checkUser = common.checkUserSession(userId,session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error ");
		} else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer)session.getAttribute("channel");
			SpecialUserGroupModel new_userGroup = new SpecialUserGroupModel();
			SpecialUserGroupModel return_userGroup = new SpecialUserGroupModel();

			new_userGroup.setCreatedAt(new Date());
			new_userGroup.setCreatedBy(userId);
			new_userGroup.setIsDeleted(0);
			new_userGroup.setGroupName(jsonNode.get("groupName").asText());
			
			
			
			return_userGroup = specialUserGroupService.save(new_userGroup);
			////System.out.println("Special user group id = "+ return_userGroup.getId());
			
			List<String> SpecialUserList = getSpecialUser(jsonNode);
			//System.out.println("Special User List = "+ SpecialUserList);
				
			
			List<SpecialUserGroupUserModel> specialUser = new ArrayList<>();
			
			for(Integer i = 0 ; i<SpecialUserList.size();i++) {
				SpecialUserGroupUserModel special_user = new SpecialUserGroupUserModel();
				special_user.setCreatedAt(new Date());
				special_user.setCreatedBy(userId);
				special_user.setIsDeleted(0);
				special_user.setSpecialUserGroupId(return_userGroup.getId());
				special_user.setUserId(SpecialUserList.get(i));
				specialUser.add(special_user);
			}
			userSpecialGroupService.save(specialUser);
			return JsonResult.ok(return_userGroup,session);
		}
		
		
	}
	
	@RequestMapping("/delete/{userId}")
	public ResponseEntity<JsonResult> delete (@PathVariable Long userId, @RequestBody JsonNode jsonNode,HttpSession session ){
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error ");
		} else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer)session.getAttribute("channel");
			for(Integer i = 0 ; i<jsonNode.get("id").size();i++ ) {
				SpecialUserGroupModel new_userGroup = specialUserGroupService.findById(jsonNode.get("id").get(i).asLong());
				new_userGroup.setIsDeleted(1);
				new_userGroup.setDeletedAt(new Date());
				new_userGroup.setDeletedBy(userId);
				specialUserGroupService.save(new_userGroup);
			}
			return JsonResult.ok(null,session);
		}
	}
	
	
	
	@RequestMapping("/update/{userId}")
	public ResponseEntity<JsonResult> update(@RequestBody JsonNode jsonNode, @PathVariable Long userId,
			HttpSession session){
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error ");
		} else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer)session.getAttribute("channel");
			SpecialUserGroupModel new_userGroup = specialUserGroupService.findById(jsonNode.get("id").asLong());
			SpecialUserGroupModel return_userGroup = new SpecialUserGroupModel();
			
			new_userGroup.setModifiedAt(new Date());
			new_userGroup.setModifiedBy(userId);
			if(jsonNode.get("groupName").asText() !="") {
				new_userGroup.setGroupName(jsonNode.get("groupName").asText());
			}
			
			
			return_userGroup = specialUserGroupService.save(new_userGroup);
			
			
			dealWithSpecialUser(jsonNode, userId);
			
			
			return JsonResult.ok(return_userGroup,session);
		}
		
		
	}
	
	
	private void dealWithSpecialUser(JsonNode jsonNode, Long userId) {
		List<String> specialUserList_db = userSpecialGroupService.getStaffNo(jsonNode.get("id").asLong())
				.stream().map(
						(n) -> {
							if(n == null) {
								return "0";
							}else {
								return n;
							}
						}
						).collect(Collectors.toList());
		//System.out.println("Special User List in Database = "+ specialUserList_db);
		List<String> specialUserList_update = getSpecialUser(jsonNode);
		List<String> specialUser_add = specialUserList_update.stream()
									   .filter(
											   (n)-> specialUserList_db.indexOf(n) == -1).collect
									   (Collectors.toList()
									);
		
		List<String> specialUser_delete = specialUserList_db.stream().
										  filter(
												 (n)->specialUserList_update.indexOf(n) == -1).collect
												 (Collectors.toList());
		//System.out.println("Special User Add = "+ specialUser_add);
		//System.out.println("Special User Delete = "+ specialUser_delete);
		
		List<SpecialUserGroupUserModel> specialUser = new ArrayList<>();
		for(Integer j = 0 ; j < specialUser_add.size() ; j++) {
			SpecialUserGroupUserModel special_user = new SpecialUserGroupUserModel();
			special_user.setCreatedAt(new Date());
			special_user.setCreatedBy(userId);
			special_user.setIsDeleted(0);
			special_user.setSpecialUserGroupId(jsonNode.get("id").asLong());
			special_user.setUserId(specialUser_add.get(j));
			specialUser.add(special_user);
		}
		
		userSpecialGroupService.save(specialUser);
		
		if(specialUser_delete.size()==0) {
			specialUser_delete.add("0");
		}
		
		userSpecialGroupService.deleteSpeicalUser(specialUser_delete, jsonNode.get("id").asLong(), userId, new Date());
		
		
		
	}	
									    
	
	
	
	private List<String> getSpecialUser(JsonNode jsonNode){
		List<String> return_data = new ArrayList<>();
		Integer special_user_size = jsonNode.get("userList") == null ? 0 : jsonNode.get("userList").size();
		for(Integer i = 0 ; i <special_user_size;i++) {
			return_data.add(jsonNode.get("userList").get(i).asText());
		}
		return return_data;
	}
	
}

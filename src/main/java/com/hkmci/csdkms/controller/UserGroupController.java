package com.hkmci.csdkms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.service.UserGroupService;

@CrossOrigin
@RestController
@RequestMapping("/usergroup")
public class UserGroupController {
	
	@Autowired
	@Resource
	private UserGroupService userGroupService;
	
	
	
	@RequestMapping("/all")
	public ResponseEntity<JsonResult> findAll(HttpSession session){
		return JsonResult.ok(userGroupService.findAll(),session);
	}
	
	

}

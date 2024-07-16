package com.hkmci.csdkms.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.service.BlogService;
import com.hkmci.csdkms.service.UinboxService;
import com.hkmci.csdkms.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/mail")
public class MailController {

	
	@Autowired
	@Resource
	private UinboxService uinboxService;
	
	@Autowired
	@Resource 
	private UserService userService;
	
	@Autowired
	@Resource
	private BlogService blogService;
	
	
	

	
	@RequestMapping("/{userId}")
	public ResponseEntity<JsonResult> getInbox(@PathVariable Long userId,
			HttpSession session){
		  Integer no = uinboxService.numberMail(userId);
		return JsonResult.ok(no,session);
	}
	
	@RequestMapping("/inbox/{userId}")
	public ResponseEntity<JsonResult> getMail(@PathVariable Long userId, HttpSession session){
		  List<Object []> mailList = uinboxService.getMail(userId);
		for(Integer i =0; i<mailList.size(); i++) {
			System.out.println("Id "+ mailList.get(i));
		}
		  
		  
		System.out.println("_______   "+mailList.size());
		return JsonResult.ok(mailList,session);
	}
	
	
}

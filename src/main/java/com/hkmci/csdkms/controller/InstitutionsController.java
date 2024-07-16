package com.hkmci.csdkms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.service.InstitutionService;

@CrossOrigin
@RestController
@RequestMapping("/institutions")
public class InstitutionsController {
	@Autowired
	@Resource
	private InstitutionService institutionService;
	
	@RequestMapping("/all")
	public ResponseEntity<JsonResult> getList(HttpSession session){
		return JsonResult.ok(institutionService.getAll(),session);
	}
	
	@RequestMapping("/report/all")
	public ResponseEntity<JsonResult> getReportList(HttpSession session){
		User user = (User) session.getAttribute("user_session");
		return JsonResult.ok(institutionService.getAllByUser(user,session),session);
	}

}

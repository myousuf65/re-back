package com.hkmci.csdkms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.service.RanksService;

@CrossOrigin
@RestController
@RequestMapping("/ranks")
public class RanksController {
	@Autowired
	@Resource
	private RanksService ranksService;
	
	@RequestMapping("/all")
	public ResponseEntity<JsonResult> getList(HttpSession session){
		
		return JsonResult.ok(ranksService.getAll(),session);
		
	}

}

package com.hkmci.csdkms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.service.SitefuncService;

@CrossOrigin
@RestController
@RequestMapping("/sitefunc")
public class SitefuncsController {
	

	@Autowired
	@Resource
	private SitefuncService sitefuncsService;
	
	
	@RequestMapping("/id")
	public ResponseEntity<JsonResult> findById(@RequestParam (value="id", required=false, defaultValue="")Long id,HttpSession session){
		return JsonResult.ok(sitefuncsService.getById(id),session);
	}

	
	
	
}

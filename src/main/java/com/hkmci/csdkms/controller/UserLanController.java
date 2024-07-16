package com.hkmci.csdkms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.model.UserLanModel;
import com.hkmci.csdkms.service.UserLanService;


@RestController
@RequestMapping("/lan")
public class UserLanController {
	private UserLanService userLanService;
	
	@Autowired
	public UserLanController(UserLanService theUserService) {
		userLanService = theUserService;
		
	}
	
	@GetMapping("/all")
	public List<UserLanModel>findAll(){
		return userLanService.findAll();
	}

	@GetMapping("/user/{UserId}")
	public UserLanModel getUserLanController(@PathVariable int UserId) {
		UserLanModel theUserLanModel = userLanService.findByUserId(UserId);
		if(theUserLanModel ==null) {
			throw new RuntimeException("No the user id "+ UserId);
		}
		return theUserLanModel;
	}
	
	@PostMapping("/user")
	public UserLanModel addUserLanModel(@RequestBody UserLanModel TheMdoel) {
		TheMdoel.setId(0);
		userLanService.save(TheMdoel);
		return TheMdoel;
	}
	
	@PutMapping("/user")
	public UserLanModel updateUserLanModel(@RequestBody UserLanModel TheModel) {
		userLanService.save(TheModel);
		return TheModel;
	}
	
	
}

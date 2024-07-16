package com.hkmci.csdkms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.hkmci.csdkms.exception.MCIExceptionHandler;

@RestController
public class MainController {
	@RequestMapping("/")
	public void goToRoot() throws MCIExceptionHandler{
		throw new MCIExceptionHandler();
//	    RedirectView redirectView = new RedirectView();
//	    redirectView.setUrl("/#/home");
//	    return redirectView;
	}
	
}

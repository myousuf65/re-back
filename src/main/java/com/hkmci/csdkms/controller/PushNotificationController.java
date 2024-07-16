package com.hkmci.csdkms.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.security.JwtTokenProvider;


@RestController
@RequestMapping("/Notification")
public class PushNotificationController {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
    @Value("${app.accessCodeProxy}")
    private String accessCodeProxy;
    
    
	
}

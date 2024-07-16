package com.hkmci.csdkms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


// Get values from application.yml
@Component
@ConfigurationProperties(prefix="app")

public class AppProperties {
	
	private String loginPageURL;

	
//  @Max(5)
//  @Min(0)
//  private int threadPool;
//
//  @NotEmpty
//  private String email;
//

//  public String getEmail() {
//  	logger.info("000getYAML - Global:" + email);    	
//      return email;
//  }
//
//  public void setEmail(String email) {
//  	logger.info("000setYAML- Global:" + email);    	
//      this.email = email;
//  }
    
    public String getLoginPageURL() {
    	return loginPageURL;
    }	

    public void setLoginPageURL(String loginPageURL) {
        this.loginPageURL = loginPageURL;
    }	

}


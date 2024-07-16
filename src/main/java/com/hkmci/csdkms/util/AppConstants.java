//For RestAPI Auth
package com.hkmci.csdkms.util;

import org.springframework.beans.factory.annotation.Value;


public interface AppConstants {
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "30";
    int MAX_PAGE_SIZE = 50;
    
//    @Value("${app.intranetHost}")
    String INTRANET_HOST="dsp.csd.hksarg";
    
//	@Value("${app.remoteKMSHost}")
	String REMOTE_KMST_HOST = "kmst.csd.gov.hk";
	String REMOTE_KMS_HOST = "kms.csd.gov.hk";

	//Maximum login fail time = 3 (in user table login_fail_attempt)
    int MAX_LOGIN_FAIL_ATTEMPT = 3;
	
}
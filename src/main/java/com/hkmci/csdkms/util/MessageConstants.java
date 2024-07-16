//For RestAPI Auth
package com.hkmci.csdkms.util;

public interface MessageConstants {
	
	String INVALID_TOKEN = "Invalid Token";
	String VALID_TOKEN = "Valid token for user ";
	String USERNAME_OR_PASSWORD_INVALID = "Incorrect Username or Password";
	String USERNAME_OR_PASSWORD_INVALID_TC = "用戶名或密碼錯誤";
	String LOGOUT_SUCCESS = "Logout Successfully!";
	String LOGOUT_FAIL = "Logout Failed!";
	String REQUEST_TIMEOUT = "Request timeout!";
	String USER_LOCKED = "This user account is locked";
	String TWO_FACTOR_AUTH_SUCCESS = "Two facor authentication success!";
	String TWO_FACTOR_AUTH_FAIL = "Two facor authentication fail!";
	String HAS_NOT_LOGIN = "The user has not login";
	String MULTIPLE_LOGIN = "The user has multiple login";
}

package com.hkmci.csdkms.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("error-message")
public class LogMessageProperties {
	
	//Log Message
	private String No_Permission_To_Resource = "No permission to access this resource";
	private String No_Permission_To_Download = "No permission to download";
	private String No_Access_Right = "User with no access right";
	private String Try_To_Access_Others = "Try to access with other user";
	private String Create_Blog_Without_UI = "Try to create a blog without UI";
	private String Create_Blog_With_Others = "Try to create a blog with other accounts";
	private String Try_To_Use_Invalid_Assistant = "Try to use invalid assistant";
	private String Try_To_Update_Blog_Without_Permission = "Try to update blog without permission";
	private String Update_Blog_With_Others = "Update blog with other users";

	public String No_Permission_To_Resource() {
		return No_Permission_To_Resource;
	}
	
	public String No_Permission_To_Download() {
		return No_Permission_To_Download;
	}
	
	public String No_Access_Right() {
		return No_Access_Right;
	}
	
	public String Try_To_Access_Others() {
		return Try_To_Access_Others;
	}
	
	public String Create_Blog_Without_UI() {
		return Create_Blog_Without_UI;
	}
	
	public String Try_To_Use_Invalid_Assistant() {
		return Try_To_Use_Invalid_Assistant;
	}
	
	public String Create_Blog_With_Others() {
		return Create_Blog_With_Others;
	}

	public String Try_To_Update_Blog_Without_Permission() {
		return Try_To_Update_Blog_Without_Permission;
	}
	
	public String Update_Blog_With_Others() {
		return Update_Blog_With_Others;
	}
	


}
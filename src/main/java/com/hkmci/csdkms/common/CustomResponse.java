package com.hkmci.csdkms.common;

import java.util.List;

public class CustomResponse {
	  private String message;
	  private List<String> output;
	  private int statusCode;

	  public CustomResponse(String message, List<String> output, int statusCode) {
	    this.message = message;
	    this.output = output;
	    this.statusCode = statusCode;
	  }
	  
	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }

	  public List<String> getOutput() {
	    return output;
	  }

	  public void setOutput(List<String> output) {
	    this.output = output;
	  }

	  public int getStatusCode() {
	    return statusCode;
	  }

	  public void setStatusCode(int statusCode) {
	    this.statusCode = statusCode;
	  }
}

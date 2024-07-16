package com.hkmci.csdkms.payload;

public class ApiResponse {
    private Boolean success;
    private String message;
    private String message1;
    private Integer count;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public ApiResponse(Boolean success, String message, String message1,Integer count) {
        this.success = success;
        this.message = message;
        this.message1 = message1;
        this.count = count ;
    }
    public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
	public String getMessage1() {
		return message1;
	}
	public void setMessage1(String message1) {
		this.message1 = message1;
	}
    
}
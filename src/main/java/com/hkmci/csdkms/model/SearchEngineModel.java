package com.hkmci.csdkms.model;

import java.util.Map;

public class SearchEngineModel {

	private Integer status;
	private Map<String, Object> payload;
	private String responseTime;
	

	public Integer getStatus(){
		return status;
	}

	public Integer setStatus(){
		return status;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	
}

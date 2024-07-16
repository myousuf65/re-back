package com.hkmci.csdkms.model;

import java.util.List;


public class PopOutModel {

	
	
	
	
	private Long id;
	
	
	private String imageUrl;
	

	private String hypryLink;
	

	private Integer isDeleted;
	

	private String accessRule;
	
	private String accessChannel;
	
	private List<Long> accessRuleId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getHypryLink() {
		return hypryLink;
	}

	public void setHypryLink(String hypryLink) {
		this.hypryLink = hypryLink;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAccessRule() {
		return accessRule;
	}

	public void setAccessRule(String accessRule) {
		this.accessRule = accessRule;
	}

	public List<Long> getAccessRuleId() {
		return accessRuleId;
	}

	public void setAccessRuleId(List<Long> accessRuleId) {
		this.accessRuleId = accessRuleId;
	}

	public String getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}
	
	
	
}

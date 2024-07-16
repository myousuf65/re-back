package com.hkmci.csdkms.model;

import java.io.Serializable;
import java.util.Date;

public class ListBoxReturn implements Serializable  {
	
	private static final long serialVersionUID = -8906734455133971892L;
	
	private Long id;
	
	private Long resourceId;
	
	private Date createdAt; 
	
	private String sendBy;
	
	private Integer isRead;
	
	private String titleEn;
	
	private String titleTc;
	
	private String Type;
	
	
	

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getSendBy() {
		return sendBy;
	}

	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getTitleTc() {
		return titleTc;
	}

	public void setTitleTc(String titleTc) {
		this.titleTc = titleTc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
}

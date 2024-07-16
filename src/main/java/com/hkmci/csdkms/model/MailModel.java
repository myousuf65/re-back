package com.hkmci.csdkms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MailModel {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long Id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String titleEn;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String titleTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String sendBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Boolean aRead;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
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

	public String getSendBy() {
		return sendBy;
	}

	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getaRead() {
		return aRead;
	}

	public void setaRead(Boolean aRead) {
		this.aRead = aRead;
	}

	
	
	
	

}


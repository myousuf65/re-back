package com.hkmci.csdkms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SearchEngineRelatedDocModel {
	private Long id;
	
	@JsonInclude(Include.NON_NULL)
	private String title;
	
	@JsonInclude(Include.NON_NULL)
	private String titleTc;
	
	@JsonInclude(Include.NON_NULL)
	private String titleEn;
	
	@JsonInclude(Include.NON_NULL)
	private String thumbnail;
	
	@JsonInclude(Include.NON_NULL)
	private Date publishAt;
	
	@JsonInclude(Include.NON_NULL)
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitleTc() {
		return titleTc;
	}

	public void setTitleTc(String titleTc) {
		this.titleTc = titleTc;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SearchEngineRelatedDocModel(Long id, String titleTc, String titleEn, String thumbnail) {
		super();
		this.id = id;
		this.titleTc = titleTc;
		this.titleEn = titleEn;
		this.thumbnail = thumbnail;
	}

	
	public SearchEngineRelatedDocModel(){
		
	}
}

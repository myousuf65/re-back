package com.hkmci.csdkms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table (name="log_page_name")
public class PageNameModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name ="page_name_en")
	private String pageNameEn;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "page_name_tc")
	private String pageNameTc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPageNameEn() {
		return pageNameEn;
	}

	public void setPageNameEn(String pageNameEn) {
		this.pageNameEn = pageNameEn;
	}

	public String getPageNameTc() {
		return pageNameTc;
	}

	public void setPageNameTc(String pageNameTc) {
		this.pageNameTc = pageNameTc;
	}

	public PageNameModel(Long id, String pageNameEn, String pageNameTc) {
		super();
		this.id = id;
		this.pageNameEn = pageNameEn;
		this.pageNameTc = pageNameTc;
	}

	public PageNameModel() {
		
	}
	
}

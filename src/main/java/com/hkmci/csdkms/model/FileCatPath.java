package com.hkmci.csdkms.model;

import javax.persistence.Id;

public class FileCatPath {
	@Id
	private Long id;
	
	private String CatEn;
	
	private String CatCn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCatEn() {
		return CatEn;
	}

	public void setCatEn(String catEn) {
		CatEn = catEn;
	}

	public String getCatCn() {
		return CatCn;
	}

	public void setCatCn(String catCn) {
		CatCn = catCn;
	}
	
	public FileCatPath() {
		
		
	}
	
	
	
	
	
	
	
	
	@Override
	public String toString() {
		return "FileCatPath [id=" + id + ", CatEn=" + CatEn + ", CatCn=" + CatCn + "]";
	}

	
	
	
	
}

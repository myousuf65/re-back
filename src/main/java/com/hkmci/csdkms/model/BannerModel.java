package com.hkmci.csdkms.model;

public class BannerModel {

	private Integer id;
	
	private String imgUrl;
	
	private String target;
	
	private String targetTc;
	
	private String targetUrl;
	
	private Integer orderBy;
	
	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getTargetTc() {
		return targetTc;
	}
	
	public void setTargetTc(String targetTc) {
		this.targetTc = targetTc;
	}
	
	public String getTargetUrl() {
		return targetUrl;
	}
	
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public BannerModel(Integer id, String imgUrl, String target, String targetTc, String targetUrl) {
		super();
		this.id = id;
		this.imgUrl = imgUrl;
		this.target = target;
		this.targetTc = targetTc;
		this.targetUrl = targetUrl;
	}
	
	public BannerModel() {
		
	}
}

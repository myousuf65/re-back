package com.hkmci.csdkms.model;

import java.io.Serializable;
import java.util.List;

public class ForumAdminCateReturnModel implements Serializable {
	private static final long serialVersionUID = -8906734455133971892L;
	
	private Long id;
	
	private String nameEN;
	
	private String nameTc;
	
	private String imgUrl;
	
	private Long parentForumId;
	
	private Integer allowComment;
	
	private Integer accessChannel;
	
	private String tabStyle;
	
	
	public String getTabStyle() {
		return tabStyle;
	}

	public void setTabStyle(String tabStyle) {
		this.tabStyle = tabStyle;
	}

	private Integer showInfo;
	
	private Integer orderBy;
	
	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	private List<Long> admin;
	
	private List<Long> accessRules_access;
	
	private List<Long> accessRules_writer;
	
	private List<Long> specialUser_access;
	
	private List<Long> specialUser_writer;
	
	
	
	private List<Integer> specialGroup_access;
	private List<Integer> specialGroup_writer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getNameTc() {
		return nameTc;
	}

	public void setNameTc(String nameTc) {
		this.nameTc = nameTc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getParentForumId() {
		return parentForumId;
	}

	public void setParentForumId(Long parentForumId) {
		this.parentForumId = parentForumId;
	}

	public Integer getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Integer allowComment) {
		this.allowComment = allowComment;
	}

	public Integer getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(Integer accessChannel) {
		this.accessChannel = accessChannel;
	}

	public List<Long> getAdmin() {
		return admin;
	}

	public void setAdmin(List<Long> admin) {
		this.admin = admin;
	}

	public List<Long> getAccessRules_access() {
		return accessRules_access;
	}

	public void setAccessRules_access(List<Long> accessRules_access) {
		this.accessRules_access = accessRules_access;
	}

	public List<Long> getAccessRules_writer() {
		return accessRules_writer;
	}

	public void setAccessRules_writer(List<Long> accessRules_writer) {
		this.accessRules_writer = accessRules_writer;
	}

	public List<Long> getSpecialUser_access() {
		return specialUser_access;
	}

	public void setSpecialUser_access(List<Long> specialUser_access) {
		this.specialUser_access = specialUser_access;
	}

	public List<Long> getSpecialUser_writer() {
		return specialUser_writer;
	}

	public void setSpecialUser_writer(List<Long> specialUser_writer) {
		this.specialUser_writer = specialUser_writer;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getShowInfo() {
		return showInfo;
	}

	public void setShowInfo(Integer showInfo) {
		this.showInfo = showInfo;
	}

	public List<Integer> getSpecialGroup_access() {
		return specialGroup_access;
	}

	public void setSpecialGroup_access(List<Integer> specialGroup_access) {
		this.specialGroup_access = specialGroup_access;
	}

	public List<Integer> getSpecialUserList_writer() {
		return specialGroup_writer;
	}

	public void setSpecialGroup_writer(List<Integer> specialGroup_writer) {
		this.specialGroup_writer = specialGroup_writer;
	}
	
	
	
	

}

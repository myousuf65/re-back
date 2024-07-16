package com.hkmci.csdkms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SpecialGroupUser  implements Serializable{
	private static final long serialVersionUID = -8906734455133971892L;
	private Long id;
	private String groupName;
	private List<String> userList;
	
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date modifiedAt;
	
	
	
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<String> getUserList() {
		return userList;
	}
	public void setUserList(List<String> userList) {
		this.userList = userList;
	}
	
	
	
	
	
}

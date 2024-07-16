package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usergroup")
public class UserGroup {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;

	@Column(name = "name", nullable = true, length = 50)
	private String name;
	
	@JsonIgnore
	@Column(name = "permission", nullable = true, length = 100)
	private String permission;
	
	@JsonIgnore
	@Column(name = "request_mode", nullable = true)
	private Integer requestMode;
	
	@JsonIgnore
	@Column(name = "allow_registration_selection", nullable = true)
	private Integer allowRegistrationSelection;
	
	@JsonIgnore
	@Column(name = "is_deleted", nullable = true)
	private Boolean isDeleted;
	
	
	@JsonIgnore
	@Column(name = "order", nullable = true)
	private Boolean order;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getRequestMode() {
		return requestMode;
	}

	public void setRequestMode(Integer requestMode) {
		this.requestMode = requestMode;
	}

	public Integer getAllowRegistrationSelection() {
		return allowRegistrationSelection;
	}

	public void setAllowRegistrationSelection(Integer allowRegistrationSelection) {
		this.allowRegistrationSelection = allowRegistrationSelection;
	}

	public UserGroup(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
	
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public UserGroup(Long id, String name, String permission, Integer requestMode, Integer allowRegistrationSelection, Boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.permission = permission;
		this.requestMode = requestMode;
		this.allowRegistrationSelection = allowRegistrationSelection;
		this.isDeleted = isDeleted;
	}

	public UserGroup() {
		
	}

}

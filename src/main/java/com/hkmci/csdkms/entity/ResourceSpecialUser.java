package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "resource_special_user")
public class ResourceSpecialUser {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;

	@Column(name = "resource_id", nullable = false)
	private Long resourceId;
	
	@Column(name = "staff_no", nullable = false)
	private String staffNo;
	
	
	@JsonIgnore
	@Column(name = "deleted_at", nullable = false)
	private Date deletedAt;
	
	@JsonIgnore
	@Column(name = "deleted_by", nullable = false)
	private Long deletedBy;
	

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



	public String getStaffNo() {
		return staffNo;
	}



	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}



	public Date getDeletedAt() {
		return deletedAt;
	}



	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}



	public Long getDeletedBy() {
		return deletedBy;
	}



	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}



	public ResourceSpecialUser() {
		
	}

}

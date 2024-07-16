package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="user_profile")
public class UserProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="picture", nullable = false )
	private String picture;
	
	@Column(name="created_by", nullable = false )
	private Long createdBy;
	
	@Column(name="created_at", nullable = false)
	private Date createdAt;
	
	@Column(name="approved", nullable = false )
	private int approved;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="approved_by")
	private Long approvedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="approved_at")
	private Date approvedAt;
	
	@Column(name="is_deleted", nullable = false)
	private int isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_at")
	private Date deletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_by")
	private Long deletedBy;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="type")
	private Integer type;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Date approvedAt) {
		this.approvedAt = approvedAt;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	

}

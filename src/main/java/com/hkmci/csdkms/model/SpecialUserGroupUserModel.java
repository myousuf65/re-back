package com.hkmci.csdkms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="special_user_group_user")
public class SpecialUserGroupUserModel {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="special_user_group_id")
	private Long specialUserGroupId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="created_by")
	private Long createdBy;
	
	@Column(name="created_at")
	private Date createdAt;
	
	@Column(name="modified_by")
	private Long modifiedBy;
	
	@Column(name="modified_at")
	private Date modifiedAt;
	
	@Column(name="is_deleted")
	private Integer isDeleted;
	
	@Column(name="deleted_by")
	private Long deletedBy;
	
	@Column(name="deleted_at")
	private Date deletedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpecialUserGroupId() {
		return specialUserGroupId;
	}

	public void setSpecialUserGroupId(Long specialUserGroupId) {
		this.specialUserGroupId = specialUserGroupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	
	
	

}

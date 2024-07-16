package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="resource_special_group")
public class ResourceSpeicalGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private Long id ;
	
	
	@Column(name="resource_id", nullable = false)
	private Long resourceId;
	
	@Column(name="special_group_id", nullable = false )
	private Long specialGroupId;
	
	@Column(name="created_at", nullable = false )
	private Date createdAt;
	
	@Column(name="created_by", nullable = false)
	private Long createdBy;
	
	@Column(name="is_deleted", nullable= false)
	private Integer isDeleted;
	
	@Column(name="deleted_at", nullable = false)
	private Date deletedAt;
	
	@Column(name="deleted_by", nullable = false)
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

	public Long getSpecialGroupId() {
		return specialGroupId;
	}

	public void setSpecialGroupId(Long specialGroupId) {
		this.specialGroupId = specialGroupId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
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
	
	
	
	
	
	

}

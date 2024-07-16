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
@Table(name = "pin")
public class Pin {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;

	@Column(name = "name", nullable = true)
	private String name;
	
	@Column(name="description", nullable =false)
	private String description;
	
	@Column(name="image_url", nullable=false)
	private String imageUrl;
	
	@JsonIgnore
	@Column(name="created_at", nullable = false)
	private Date createdAt;
	
	@Column(name="created_by", nullable = false)
	private Long createdBy;
	
	@Column(name="is_deleted", nullable = false)
	private Integer isDeleted;
	
	@Column(name="deleted_by",nullable = false)
	private Long deletedBy;
	
	@Column(name="deleted_at", nullable = false)
	private Date deletedAt;
	
	@Column(name="modified_by", nullable = false)
	private Long modifiedBy;
	
	@Column(name="modified_at", nullable = false)
	private Date modifiedAt;

	@Column(name="name_tc", nullable = false)
	private String nameTc;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getNameTc() {
		return nameTc;
	}

	public void setNameTc(String nameTc) {
		this.nameTc = nameTc;
	}
	
	
	
	
}

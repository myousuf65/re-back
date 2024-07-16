package com.hkmci.csdkms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PinReturnModel {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String staffNo;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String nameTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String imageUrl;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private Long createdBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long modifiedBy;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date modifiedAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long deletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date deletedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameTc() {
		return nameTc;
	}

	public void setNameTc(String nameTc) {
		this.nameTc = nameTc;
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

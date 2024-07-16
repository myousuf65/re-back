package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="FAQ")
public class Faq {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name="id")
	private Long id;
	

	@JsonIgnore
	@Column(name="qa")
	private Integer qa;
	
	@JsonIgnore
	@Column(name ="chinese")
	private String chinese;
	
	@JsonIgnore
	@Column(name="english")
	private String english;
	
	@JsonIgnore
	@Column(name="link_to")
	private Long linkTo;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@Column(name="created_at")
	private Date createdAt;
	
	@JsonIgnore
	@Column(name="created_by")
	private Long createdBy;
	
	
	@JsonIgnore
	@Column(name="is_deleted")
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@Column(name="deleted_at")
	private Date deletedAt;
	
	@JsonIgnore
	@Column(name="deleted_by")
	private Long deletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@Column(name="modified_at")
	private Date modifiedAt;
	

	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="title_level")
	private Integer titleLevel;
	
	@JsonIgnore
	@Column(name="modified_by")
	private Long modifiedBy;
	
	@JsonIgnore
	@Column(name="parent_id")
	private Long parentId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQa() {
		return qa;
	}

	public void setQa(Integer qa) {
		this.qa = qa;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public Long getLinkTo() {
		return linkTo;
	}

	public void setLinkTo(Long linkTo) {
		this.linkTo = linkTo;
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

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	

	public Integer getTitleLevel() {
		return titleLevel;
	}

	public void setTitleLevel(Integer titleLevel) {
		this.titleLevel = titleLevel;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	 
	

}

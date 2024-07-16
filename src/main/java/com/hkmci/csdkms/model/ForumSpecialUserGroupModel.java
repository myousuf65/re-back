package com.hkmci.csdkms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="forum_special_usergroup")
public class ForumSpecialUserGroupModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",nullable = false)
	private Long id;
	
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="forum_cat_id", nullable = false)
	private Long forumCatId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="special_usergroup_id",nullable = false)
	private Long specialUsergroupId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="rule_right", nullable = false)
	private Integer ruleRight;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="created_by", nullable = false)
	private Long createdBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="created_at", nullable = false)
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_by")
	private Long modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_at")
	private Date modifiedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="is_deleted")
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_by")
	private Long deletedBy;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_at")
	private Date deletedAt;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getForumCatId() {
		return forumCatId;
	}


	public void setForumCatId(Long forumCatId) {
		this.forumCatId = forumCatId;
	}


	public Long getSpecialUsergroupId() {
		return specialUsergroupId;
	}


	public void setSpecialUsergroupId(Long specialUsergroupId) {
		this.specialUsergroupId = specialUsergroupId;
	}


	public Integer getRuleRight() {
		return ruleRight;
	}


	public void setRuleRight(Integer ruleRight) {
		this.ruleRight = ruleRight;
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

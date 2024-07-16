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
@Table(name = "forum_access_rule")
public class ForumAccessRule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private Long id;
	
	@Column(name = "forum_cat_id" ,nullable = false )
	private Long forumCatId;
	
	@Column(name ="access_rule_id", nullable = false )
	private Long accessRuleId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="rule_right")
	private Integer ruleRight;
	
	@Column(name="created_by", nullable = false)
	private Long createdBy;
	
	@Column(name="created_at", nullable = false)
	private Date createdAt;
	
	@Column(name="modified_at", nullable=false )
	private Date modifiedAt;
	
	@Column(name="modified_by", nullable =false)
	private Long modifiedBy;
	
	@Column(name="is_deleted", nullable = false)
	private Boolean isDeleted;
	
	@Column(name="deleted_at",nullable = false)
	private Date deletedAt;
	
	@Column(name="deleted_by",nullable=false)
	private Long deletedBy;

	public ForumAccessRule() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public Long getAccessRuleId() {
		return accessRuleId;
	}

	public void setAccessRuleId(Long accessRuleId) {
		this.accessRuleId = accessRuleId;
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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
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

	public Integer getRuleRight() {
		return ruleRight;
	}

	public void setRuleRight(Integer ruleRight) {
		this.ruleRight = ruleRight;
	}
	
	
	
	

}

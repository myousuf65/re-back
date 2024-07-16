package com.hkmci.csdkms.entity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.model.ForumCategoryModel;

@Entity
@Table(name="forum_post")

public class ForumPost {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",nullable = false)
	private Long id;
	
	
	@Column(name = "post_title", nullable = false)
	private String postTitle;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "content", nullable = false)
	private String content;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name= "can_comment", nullable = false )
	private Integer canComment;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="order_by", nullable = false )
	private Integer orderBy;
	
	@Column(name="hit" , nullable =false )
	private Integer hit;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="show_as_alias" , nullable=false)
	private Integer showAsAlias;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="alias" , nullable =false )
	private String alias;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "forum_id", nullable = false )
	private Long forumId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="created_by", nullable = false)
	private Long createdBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="created_at" , nullable =false )
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_by", nullable = false)
	private Long modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_at", nullable =false)
	private Date modifiedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="is_deleted", nullable =false)
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_by", nullable =false)
	private Long deletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_at", nullable = false)
	private Date deletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="allow_comment", nullable = false)
	private Integer allowComment;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Transient
	ForumCategoryModel subCate;
    
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Transient
	private String postCreater;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="hidden_field")
	private String hiddenField;

	
	public ForumCategoryModel getSubCate() {
		return subCate;
	}


	public void setSubCate(ForumCategoryModel cat) {
		this.subCate = cat;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCanComment() {
		return canComment;
	}

	public void setCanComment(Integer canComment) {
		this.canComment = canComment;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getHit() {
		return hit;
	}

	public void setHit(Integer hit) {
		this.hit = hit;
	}

	public Integer getShowAsAlias() {
		return showAsAlias;
	}

	public void setShowAsAlias(Integer showAsAlias) {
		this.showAsAlias = showAsAlias;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
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

	public void setIsDeleted(Integer i) {
		this.isDeleted = i;
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
	
	
	
	public Integer getAllowComment() {
		return allowComment;
	}


	public void setAllowComment(Integer allowComment) {
		this.allowComment = allowComment;
	}


	public String getPostCreater() {
		return postCreater;
	}


	public void setPostCreater(String postCreater) {
		this.postCreater = postCreater;
	}

	
	
	public String getHiddenField() {
		return hiddenField;
	}


	public void setHiddenField(String hiddenField) {
		this.hiddenField = hiddenField;
	}


	public ForumPost() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public ForumPost(Long id, String postTitle, Long forumId) {
		this.id = id;
		this.postTitle = postTitle;
		this.forumId = forumId;
		
	}


	
	
	
}

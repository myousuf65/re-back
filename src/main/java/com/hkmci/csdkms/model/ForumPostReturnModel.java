package com.hkmci.csdkms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.entity.ForumPost;
import com.hkmci.csdkms.entity.User;

public class ForumPostReturnModel implements Serializable {

	private static final long serialVersionUID = -8906734455133971892L;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String title;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer comments;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String content;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer likes;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer views;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date lastUpdatedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String lastUpdateBy;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer order;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer hit;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User lastUpdatedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer isAlias;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alias;
    
  
    private String hiddenField;
    
    
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ForumCategoryModel  category;
    
    
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long  categoryId;
    
    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date modifiedAt;
    
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer allowComment;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer canEdit;
    
    
    
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long galleryId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}


	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public User getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(User lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	

	public Integer getIsAlias() {
		return isAlias;
	}

	public void setIsAlias(Integer isAlias) {
		this.isAlias = isAlias;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getHit() {
		return hit;
	}

	public void setHit(Integer integer) {
		this.hit = integer;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

	public Integer getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Integer allowComment) {
		this.allowComment = allowComment;
	}

	
	
	public ForumCategoryModel getCategory() {
		return category;
	}

	public void setCategory(ForumCategoryModel category) {
		this.category = category;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Integer canEdit) {
		this.canEdit = canEdit;
	}

	public Long getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(Long galleryId) {
		this.galleryId = galleryId;
	}

	public String getHiddenField() {
		return hiddenField;
	}

	public void setHiddenField(String hiddenField) {
		this.hiddenField = hiddenField;
	}
    
    
	
	
}

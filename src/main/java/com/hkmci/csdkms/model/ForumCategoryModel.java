package com.hkmci.csdkms.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "forum_category")
public class ForumCategoryModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="name_en", nullable= false)
	private String nameEn;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="name_tc", nullable = false)
	private String nameTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="icon_path", nullable = false)
	private String imgUrl;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="level", nullable = false)
	private Integer level;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="parent_forum_id", nullable =false)
	private Long parentForumId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="created_by", nullable = false )
	private Long createdBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="created_at", nullable =false)
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_by", nullable = false)
	private Long modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_at", nullable =false)
	private Date modifiedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="is_deleted", nullable =false)
	private Boolean isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_by", nullable = false)
	private Long deletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_at", nullable = false)
	private Date deletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="access_channel" , nullable= false)
	private String accessChannel;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="order_by", nullable =false)
	private Integer orderBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="tab_style",nullable=false)
	private String tabStyle;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="show_info",nullable=false)
	private Integer showInfo;

	public Integer getShowInfo() {
		return showInfo;
	}

	public void setShowInfo(Integer showInfo) {
		this.showInfo = showInfo;
	}

	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	List<ForumCategoryModel> subCate;
	
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer canCreate;

	
	
	
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameTc() {
		return nameTc;
	}

	public void setNameTc(String nameTc) {
		this.nameTc = nameTc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getParentForumId() {
		return parentForumId;
	}

	public void setParentForumId(Long parentForumId) {
		this.parentForumId = parentForumId;
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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	public List<ForumCategoryModel> getsubCate() {
		return subCate;
	}

	public void setsubCate(List<ForumCategoryModel> subCate) {
		this.subCate = subCate;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public String getAccessChannel() {
		return accessChannel;
	}

	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getTabStyle() {
		return tabStyle;
	}

	public void setTabStyle(String tabStyle) {
		this.tabStyle = tabStyle;
	}

	public Integer getCanCreate() {
		return canCreate;
	}

	public void setCanCreate(Integer canCreate) {
		this.canCreate = canCreate;
	}


	
	

}

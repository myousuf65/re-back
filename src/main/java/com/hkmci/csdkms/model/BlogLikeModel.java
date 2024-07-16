package com.hkmci.csdkms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="blog_like")
public class BlogLikeModel {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="id")
private Long Id;

@Column (name="func_id")
private Integer funcId;

@Column (name="pkid")
private Long pkid;

@Column (name="created_by")
private Long createdBy;

@Column (name="created_at")
private Date createdAt;

@Column (name="rank_id")
private Long rankId;

@Column (name="inst_id")
private Long instId;

@Column (name="section_id")
private Long sectionId;

@Column (name="is_deleted")
private Integer isDeleted;

public BlogLikeModel() {
	
}

public Long getId() {
	return Id;
}

public void setId(Long id) {
	Id = id;
}

public int getFuncId() {
	return funcId;
}

public void setFuncId(Integer funcId) {
	this.funcId = funcId;
}

public Long getPkid() {
	return pkid;
}

public void setPkid(Long pkid) {
	this.pkid = pkid;
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

public Long getRankId() {
	return rankId;
}

public void setRankId(Long rankId) {
	this.rankId = rankId;
}

public Long getInstId() {
	return instId;
}

public void setInstId(Long instId) {
	this.instId = instId;
}

public Long getSectionId() {
	return sectionId;
}

public void setSectionId(Long sectionId) {
	this.sectionId = sectionId;
}

public Integer getIsDeleted() {
	return isDeleted;
}

public void setIsDeleted(Integer isDeleted) {
	this.isDeleted = isDeleted;
}

@Override
public String toString() {
	return "BlogLikeModel [Id=" + Id + ", funcId=" + funcId + ", pkid=" + pkid + ", createdBy=" + createdBy
			+ ", createdAt=" + createdAt + ", rankId=" + rankId + ", instId=" + instId + ", sectionId=" + sectionId
			+ ", isDeleted=" + isDeleted + "]";
}

public BlogLikeModel(Long id, Integer funcId, Long pkid, Long createdBy, Date createdAt, Long rankId, Long instId, Long sectionId,
		Integer isDeleted) {
	super();
	Id = id;
	this.funcId = funcId;
	this.pkid = pkid;
	this.createdBy = createdBy;
	this.createdAt = createdAt;
	this.rankId = rankId;
	this.instId = instId;
	this.sectionId = sectionId;
	this.isDeleted = isDeleted;
}

public BlogLikeModel(Integer funcId, Long pkid, Long createdBy, Date createdAt, Long rankId, Long instId, Long sectionId,
		Integer isDeleted) {
	super();
	this.funcId = funcId;
	this.pkid = pkid;
	this.createdBy = createdBy;
	this.createdAt = createdAt;
	this.rankId = rankId;
	this.instId = instId;
	this.sectionId = sectionId;
	this.isDeleted = isDeleted;
}


	
}

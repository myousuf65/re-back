package com.hkmci.csdkms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="newscorner2_bookmark")
public class NewsCorner2BookmarkModel{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column (name="post_id")
	private Integer postId;
	
	@Column (name="user_ref")
	private Integer userRef;
	
	@Column (name="created_at")
	private Date createdAt;
	
	@Column (name="rank_id")
	private Integer rankId;
	
	@Column (name="inst_id")
	private Integer instId;
	
	@Column (name="section_id")
	private Integer sectionId;
	
	@JsonIgnore
	@Column (name="is_deleted")
	private Integer isDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getUserRef() {
		return userRef;
	}

	public void setUserRef(Integer userRef) {
		this.userRef = userRef;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getRankId() {
		return rankId;
	}

	public void setRankId(Integer rankId) {
		this.rankId = rankId;
	}

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public Integer getSectionId() {
		return sectionId;
	}

	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public NewsCorner2BookmarkModel(Integer id, Integer postId, Integer userRef, Date createdAt, Integer rankId, Integer instId, Integer sectionId,
			Integer isDeleted) {
		super();
		this.id = id;
		this.postId = postId;
		this.userRef = userRef;
		this.createdAt = createdAt;
		this.rankId = rankId;
		this.instId = instId;
		this.sectionId = sectionId;
		this.isDeleted = isDeleted;
	}
	
	public NewsCorner2BookmarkModel(Integer postId, Integer userRef, Date createdAt, Integer rankId, Integer instId, Integer sectionId,
			Integer isDeleted) {
		super();
		this.postId = postId;
		this.userRef = userRef;
		this.createdAt = createdAt;
		this.rankId = rankId;
		this.instId = instId;
		this.sectionId = sectionId;
		this.isDeleted = isDeleted;
	}
	
	public NewsCorner2BookmarkModel() {
		
	}
	
	
}


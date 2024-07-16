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
import com.hkmci.csdkms.entity.User;

@Entity
@Table(name = "forum_comment")
public class ForumCommentModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long Id;
	
	@Column(name="post_id")
	private Long postId;
	
	@Column(name="is_reply2cmnt")
	private Long isReply2cmnt;
	
	@Column(name= "content")
	private String content;
	
	@Column(name ="show_as_alias")
	private Integer showAsAlias;
	
	@Column(name="alias")
	private String alias;
	
	@Column(name="created_by")
	private Long createdBy;
	
	@Column(name="created_at")
	private Date createdAt;
	
	@Column(name="modified_by")
	private Long modifiedBy;
	
	@Column(name="modified_at")
	private Date modifiedAt;
	
	@Column(name="is_deleted")
	private Integer isDeleted;
	
	@Column(name="deleted_at")
	private Date deletedAt;
	
	@Column(name="deleted_by")
	private Long deletedBy;
	
	
	@Transient  
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ForumCommentModel> subComments;

	@Transient
	private Integer liked;
	
	@Transient
	private Long likes;
	
	
	@Transient
	private User user;
	

	public ForumCommentModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getIsReply2cmnt() {
		return isReply2cmnt;
	}

	public void setIsReply2cmnt(Long isReply2cmnt) {
		this.isReply2cmnt = isReply2cmnt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public List<ForumCommentModel> getSubComments() {
		return subComments;
	}

	public void setSubComments(List<ForumCommentModel> subComments) {
		this.subComments = subComments;
	}

	public Integer getLiked() {
		return liked;
	}

	public void setLiked(Integer liked) {
		this.liked = liked;
	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	

}

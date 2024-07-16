package com.hkmci.csdkms.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.entity.User;

@Entity
@Table(name="newscorner2_comment")
public class NewsCorner2CommentModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long Id;
	
	@Column(name="post_id")
	private Long PostId;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by")
	private User CreatedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name="created_at")
	private Date CreatedAt;
	
	@Column(name="is_reply2cmnt")
	private Long IsRely2cmnt;
	
	@Column(name="content")
	private String Content;
	
	@JsonIgnore
	@Column(name="is_deleted")
	private Integer IsDeleted;
	
	@JsonIgnore
	@Column(name="deleted_by")
	private Long DeletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column (name="deleted_at")
	private Date DeletedAt;
	
	@JsonIgnore
	@Column (name="modified_by")
	private Long ModifiedBy;
	
	@JsonIgnore
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column (name="modified_at")
	private Date ModifiedAt;
	
	@JsonIgnore
	@Column(name="rand_id")
	private Long RandId;
	
	@JsonIgnore
	@Column(name="inst_id")
	private Long InstId;
	
	@JsonIgnore
	@Column(name="section_id")
	private Long SectionId;
	
	@Column (name="show_as_alias")
	private Long ShowAsAlias;
	
	@Column (name="alias")
	private String Alias;
	
	@Transient  
    private List<NewsCorner2CommentModel> subComments;
	
	@Transient
	private Integer liked;
	
	@Transient
	private Long likes;
	
	public NewsCorner2CommentModel() {
		
	}
	
	
	public NewsCorner2CommentModel(Long id, Long postId, User createdBy, Date createdAt, Long isRely2cmnt, String content,
			Integer isDeleted, Long deletedBy, Date deletedAt, Long modifiedBy, Date modifiedAt, Long randId, Long instId,
			Long sectionId, Long showAsAlias, String alias, List<NewsCorner2CommentModel> subComments, Integer liked, Long likes) {
		
		Id = id;
		PostId = postId;
		CreatedBy = createdBy;
		CreatedAt = createdAt;
		IsRely2cmnt = isRely2cmnt;
		Content = content;
		IsDeleted = isDeleted;
		DeletedBy = deletedBy;
		DeletedAt = deletedAt;
		ModifiedBy = modifiedBy;
		ModifiedAt = modifiedAt;
		RandId = randId;
		InstId = instId;
		SectionId = sectionId;
		ShowAsAlias = showAsAlias;
		Alias = alias;
		this.subComments = subComments;
		this.liked = liked;
		this.likes = likes;
	}


	public Long getId() {
		return Id;
	}


	public void setId(Long id) {
		Id = id;
	}


	public Long getPostId() {
		return PostId;
	}


	public void setPostId(Long postId) {
		PostId = postId;
	}


	public User getCreatedBy() {
		return CreatedBy;
	}


	public void setCreatedBy(User createdBy) {
		CreatedBy = createdBy;
	}


	public Date getCreatedAt() {
		return CreatedAt;
	}


	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}


	public Long getIsRely2cmnt() {
		return IsRely2cmnt;
	}


	public void setIsRely2cmnt(Long is_reply2cmnt) {
		IsRely2cmnt = is_reply2cmnt;
	}


	public String getContent() {
		return Content;
	}


	public void setContent(String content) {
		Content = content;
	}


	public Integer getIsDeleted() {
		return IsDeleted;
	}


	public void setIsDeleted(Integer isDeleted) {
		IsDeleted = isDeleted;
	}


	public Long getDeletedBy() {
		return DeletedBy;
	}


	public void setDeletedBy(Long deletedBy) {
		DeletedBy = deletedBy;
	}


	public Date getDeletedAt() {
		return DeletedAt;
	}


	public void setDeletedAt(Date deletedAt) {
		DeletedAt = deletedAt;
	}


	public Long getModifiedBy() {
		return ModifiedBy;
	}


	public void setModifiedBy(Long modifiedBy) {
		ModifiedBy = modifiedBy;
	}


	public Date getModifiedAt() {
		return ModifiedAt;
	}


	public void setModifiedAt(Date modifiedAt) {
		ModifiedAt = modifiedAt;
	}


	public Long getRandId() {
		return RandId;
	}


	public void setRandId(Long randId) {
		RandId = randId;
	}


	public Long getInstId() {
		return InstId;
	}


	public void setInstId(Long instId) {
		InstId = instId;
	}


	public Long getSectionId() {
		return SectionId;
	}


	public void setSectionId(Long sectionId) {
		SectionId = sectionId;
	}


	public Long getShowAsAlias() {
		return ShowAsAlias;
	}


	public void setShowAsAlias(Long showAsAlias) {
		ShowAsAlias = showAsAlias;
	}


	public String getAlias() {
		return Alias;
	}


	public void setAlias(String alias) {
		Alias = alias;
	}


	@Transient
	public List<NewsCorner2CommentModel> getSubComments() {
		return subComments;
	}


	public void setSubComments(List<NewsCorner2CommentModel> subComments) {
		this.subComments = subComments;
	}

	@Transient
	public Integer getLiked() {
		return liked;
	}


	public void setLiked(Integer liked) {
		this.liked = liked;
	}
	
	@Transient
	public Long getLikes() {
		return likes;
	}


	public void setLikes(Long likes) {
		this.likes = likes;
	}


	@Override
	public String toString() {
		return "NewsCorner2CommentModel [Id=" + Id + ", PostId=" + PostId + ", CreatedBy=" + CreatedBy + ", CreatedAt="
				+ CreatedAt + ", IsRely2cmnt=" + IsRely2cmnt + ", Content=" + Content + ", IsDeleted=" + IsDeleted
				+ ", DeletedBy=" + DeletedBy + ", DeletedAt=" + DeletedAt + ", ModifiedBy=" + ModifiedBy
				+ ", ModifiedAt=" + ModifiedAt + ", RandId=" + RandId + ", InstId=" + InstId + ", SectionId="
				+ SectionId + ", ShowAsAlias=" + ShowAsAlias + ", Alias=" + Alias + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

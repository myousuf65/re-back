package com.hkmci.csdkms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class BlogCommentReturn implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4690575345237408979L;

	private Integer Id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer PostId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer CreatedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private Date CreatedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer IsRely2cmnt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String Content;
	
	@JsonIgnore
	private Integer IsDeleted;
	
	@JsonIgnore
	private Integer DeletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private Date DeletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer ModifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private Date ModifiedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer RandId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer InstId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer SectionId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer ShowAsAlias;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String Alias;
	
	private List<BlogCommentModel> subComments;
	
	private Integer liked;
	
public BlogCommentReturn() {
		
	}
	
	
	public BlogCommentReturn(Integer id, Integer postId, Integer createdBy, Date createdAt, Integer isRely2cmnt, String content,
			Integer isDeleted, Integer deletedBy, Date deletedAt, Integer modifiedBy, Date modifiedAt, Integer randId, Integer instId,
			Integer sectionId, Integer showAsAlias, String alias,List<BlogCommentModel> subComments, Integer liked) {
		
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
	}


	public Integer getId() {
		return Id;
	}


	public void setId(Integer id) {
		Id = id;
	}


	public Integer getPostId() {
		return PostId;
	}


	public void setPostId(Integer postId) {
		PostId = postId;
	}


	public Integer getCreatedBy() {
		return CreatedBy;
	}


	public void setCreatedBy(Integer createdBy) {
		CreatedBy = createdBy;
	}


	public Date getCreatedAt() {
		return CreatedAt;
	}


	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}


	public Integer getIsRely2cmnt() {
		return IsRely2cmnt;
	}


	public void setIsRely2cmnt(Integer isRely2cmnt) {
		IsRely2cmnt = isRely2cmnt;
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


	public Integer getDeletedBy() {
		return DeletedBy;
	}


	public void setDeletedBy(Integer deletedBy) {
		DeletedBy = deletedBy;
	}


	public Date getDeletedAt() {
		return DeletedAt;
	}


	public void setDeletedAt(Date deletedAt) {
		DeletedAt = deletedAt;
	}


	public Integer getModifiedBy() {
		return ModifiedBy;
	}


	public void setModifiedBy(Integer modifiedBy) {
		ModifiedBy = modifiedBy;
	}


	public Date getModifiedAt() {
		return ModifiedAt;
	}


	public void setModifiedAt(Date modifiedAt) {
		ModifiedAt = modifiedAt;
	}


	public Integer getRandId() {
		return RandId;
	}


	public void setRandId(Integer randId) {
		RandId = randId;
	}


	public Integer getInstId() {
		return InstId;
	}


	public void setInstId(Integer instId) {
		InstId = instId;
	}


	public Integer getSectionId() {
		return SectionId;
	}


	public void setSectionId(Integer sectionId) {
		SectionId = sectionId;
	}


	public Integer getShowAsAlias() {
		return ShowAsAlias;
	}


	public void setShowAsAlias(Integer showAsAlias) {
		ShowAsAlias = showAsAlias;
	}


	public String getAlias() {
		return Alias;
	}


	public void setAlias(String alias) {
		Alias = alias;
	}
	
	public Integer getLiked() {
		return liked;
	}


	public void setAlias(Integer liked) {
		this.liked = liked;
	}
	
	public List<BlogCommentModel> getSubComments() {
		return this.subComments;
	}
	
	public void setSubComments(List<BlogCommentModel> subComments) {
		this.subComments = subComments;
	}


	@Override
	public String toString() {
		return "BlogCommentModel [Id=" + Id + ", PostId=" + PostId + ", CreatedBy=" + CreatedBy + ", CreatedAt="
				+ CreatedAt + ", IsRely2cmnt=" + IsRely2cmnt + ", Content=" + Content + ", IsDeleted=" + IsDeleted
				+ ", DeletedBy=" + DeletedBy + ", DeletedAt=" + DeletedAt + ", ModifiedBy=" + ModifiedBy
				+ ", ModifiedAt=" + ModifiedAt + ", RandId=" + RandId + ", InstId=" + InstId + ", SectionId="
				+ SectionId + ", ShowAsAlias=" + ShowAsAlias + ", Alias=" + Alias + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

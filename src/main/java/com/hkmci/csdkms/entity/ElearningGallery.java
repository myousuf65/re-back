package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "elearning_gallery")
public class ElearningGallery {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "gallery_name", nullable = false)
	private String galleryName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "post_id", nullable = true)
	private Long postId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "modified_at", nullable = true)
	private Date modifiedAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "modified_by", nullable = true)
	private Long modifiedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "is_finished", nullable = true)
	private int isFinished;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "finished_at", nullable = true)
	private Date finishedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGalleryName() {
		return galleryName;
	}

	public void setGalleryName(String galleryName) {
		this.galleryName = galleryName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
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

	public int getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(int isFinished) {
		this.isFinished = isFinished;
	}
	
	public Date getFinishedAt() {
		return finishedAt;
	}

	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}
	
	

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public ElearningGallery(Long id, String galleryName, Long userId, Date createdAt, Long createdBy, Date modifiedAt,
			Long modifiedBy, int isFinished) {
		super();
		this.id = id;
		this.galleryName = galleryName;
		this.userId = userId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.modifiedAt = modifiedAt;
		this.modifiedBy = modifiedBy;
		this.isFinished = isFinished;
	}

	public ElearningGallery() {

	}
    

}

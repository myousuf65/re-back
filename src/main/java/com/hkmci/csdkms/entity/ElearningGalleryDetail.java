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
@Table(name = "elearning_gallery_detail")
public class ElearningGalleryDetail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "gallery_id", nullable = false)  
	private Long galleryId;
	
//	@JsonInclude(JsonInclude.Include.NON_NULL)
//	@Column(name = "post_id", nullable = true)  
//	private Long postId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "ofilename", nullable = false)
	private String ofilename;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "nfilename", nullable = false)
	private String nfilename;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "thumbnail", nullable = true)
	private String thumbnail;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "label", nullable = true)
	private String label;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "ordering", nullable = false)
	private int ordering;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "modified_at", nullable = true)
	private Date modifiedAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "modified_by", nullable = true)
	private Long modifiedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "deleted_by", nullable = true)
	private Long deletedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "deleted", nullable = false)
	private int deleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Long getPostId() {
//		return postId;
//	}
//
//	public void setPostId(Long postId) {
//		this.postId = postId;
//	}

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

	public String getOfilename() {
		return ofilename;
	}

	public void setOfilename(String ofilename) {
		this.ofilename = ofilename;
	}

	public String getNfilename() {
		return nfilename;
	}

	public void setNfilename(String nfilename) {
		this.nfilename = nfilename;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
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
	
	

	public Long getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(Long galleryId) {
		this.galleryId = galleryId;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public ElearningGalleryDetail(Long id, Date createdAt, Long createdBy, String ofilename, String nfilename,
			String thumbnail, String label, int ordering, Date modifiedAt, Long modifiedBy, Date deletedAt,
			Long deletedBy) {
		super();
		this.id = id;
		//this.postId = postId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.ofilename = ofilename;
		this.nfilename = nfilename;
		this.thumbnail = thumbnail;
		this.label = label;
		this.ordering = ordering;
		this.modifiedAt = modifiedAt;
		this.modifiedBy = modifiedBy;
		this.deletedAt = deletedAt;
		this.deletedBy = deletedBy;
	}

	public ElearningGalleryDetail() {
		// TODO Auto-generated constructor stub
	}

    
}

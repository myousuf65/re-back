package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "favorites")
public class Favorites {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name="id", nullable =false)
	private Long id;
	
	@Column( name="resource_id", nullable = false)
	private Long resourceId;
	
	@Column( name="created_by", nullable = false)
	private Long createdBy;
	
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name ="created_at", nullable = false)
	private Date createdAt;
	
	@JsonIgnore
	@Column(name="deleted_by", nullable= false)
	private Long deletedBy;
	
	@JsonIgnore
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name="deleted_at", nullable=false)
	private Date deletedAt;
	
	@JsonIgnore
	@Column(name="is_deleted", nullable = false)
	private Long isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
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

	public Long getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Favorites() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Favorites(Long id, Long resourceId, Long createdBy, Date createdAt, Long deletedBy, Date deletedAt,
			Long isDeleted) {
		
		this.id = id;
		this.resourceId = resourceId;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.deletedBy = deletedBy;
		this.deletedAt = deletedAt;
		this.isDeleted = isDeleted;
	}
	
	
	
	
	

}

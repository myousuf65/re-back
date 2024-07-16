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
@Table(name="mobile_version")
public class mobileVersion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonIgnore
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;
	
	@JsonIgnore
	@Column(name = "deleted_by", nullable = true)
	private Long deletedBy;
	
	@JsonIgnore
	@Column(name = "is_deleted", nullable = true)
	private Long isDeleted;
	
	@JsonIgnore
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
	
	@Column(name="created_at", nullable= true)
	private Date createdAt;
	@JsonIgnore
	@Column(name = "version", nullable = false)
	private String version;
	@JsonIgnore
	@Column(name = "app", nullable = false)
	private String app;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	
}

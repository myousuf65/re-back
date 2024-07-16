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
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name ="uinbox")
public class Uinbox {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name ="id" )
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="resource_id")
	private Long resourceId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="sent_by")
	private Long sendBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="sent_to")
	private Long sendTo;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name="created_at")
	private Date createdAt;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="is_deleted")
	private Integer isDeleted;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name="deleted_at")
	private Date deletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="deleted_by")
	private Long deletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="is_read")
	private Integer isRead;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="read_time")
	private Date readTime;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="mail_type")
	private Long mailType;

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

	public Long getSendBy() {
		return sendBy;
	}

	public void setSendBy(Long sendBy) {
		this.sendBy = sendBy;
	}

	public Long getSendTo() {
		return sendTo;
	}

	public void setSendTo(Long sendTo) {
		this.sendTo = sendTo;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Long getMailType() {
		return mailType;
	}

	public void setMailType(Long mailType) {
		this.mailType = mailType;
	}
	
	
	
	
	
	
	
}
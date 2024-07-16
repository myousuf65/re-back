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
@Table(name="banner_access_rule")
public class BannerAccessRule {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private Long id;
	
	@Column(name="banner_id", nullable= false)
	private Long bannerId;
	
	
	@Column(name="access_rule_id", nullable =false )
	private Long accessRuleId;
	
	@Column (name="created_by", nullable= false)
	private Long createdBy;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@Column (name="created_at" , nullable = false )
	private Date createdAt;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="modified_by", nullable= true)
	private Long modifiedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@Column(name="modified_at", nullable = true)
	private Date modifiedAt;
	
	@Column(name="deleted_by",nullable =false)
	private Long deletedBy;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name="deleted_at", nullable =true)
	private Date deletedAt;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getBannerId() {
		return bannerId;
	}


	public void setBannerId(Long bannerId) {
		this.bannerId = bannerId;
	}


	public Long getAccessRuleId() {
		return accessRuleId;
	}


	public void setAccessRuleId(Long accessRuleId) {
		this.accessRuleId = accessRuleId;
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


	public BannerAccessRule() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}

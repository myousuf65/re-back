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
@Table(name = "resource_access_rule")
public class ResourceAccessRule {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@Column(name = "resource_id", nullable = false)  
    private Long resource;
	
	
	@Column(name = "access_rule_id", nullable = false)  
    private Long accessRule;
	
	@Column(name = "deleted_by", nullable = true,columnDefinition="tinyint(3) default 0")  
    private Long deletedBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResource() {
		return resource;
	}

	public void setResource(Long resource) {
		this.resource = resource;
	}

	public Long getAccessRule() {
		return accessRule;
	}

	public void setAccessRule(Long accessRule) {
		this.accessRule = accessRule;
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

	public ResourceAccessRule(Long id, Long resource, Long accessRule, Long deletedBy, Date deletedAt) {
		this.id = id;
		this.resource = resource;
		this.accessRule = accessRule;
		this.deletedBy = deletedBy;
		this.deletedAt = deletedAt;
	}
	
	public ResourceAccessRule( Long resource, Long accessRule) {
		super();
		this.resource = resource;
		this.accessRule = accessRule;
		this.deletedBy = 0L;
	}
	
	public ResourceAccessRule() {
		
	}
	
	
}

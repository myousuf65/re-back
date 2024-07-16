package com.hkmci.csdkms.model;

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
@Table(name="resource_category")
public class ResourceCategoryModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="resource_id", nullable = false)
	private Long resourceId;
	
	@Column(name="category_id", nullable = false)
	private Long categoryId;  
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "deleted_by", nullable = true)
	private Long deletedBy;
	
	
	
	public ResourceCategoryModel() {
		
	}
	
	public ResourceCategoryModel(Integer id, Long resourceId, Long categoryId, Date deletedAt, Long deletedBy) {
		super();
		this.id = id;
		this.resourceId = resourceId;
		this.categoryId = categoryId;
		this.deletedAt = deletedAt;
		this.deletedBy = deletedBy;
	}




	public Integer getId() {
		return id;
	}




	public void setId(Integer id) {
		this.id = id;
	}




	public Long getResourceId() {
		return resourceId;
	}




	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}




	public Long getCategoryId() {
		return categoryId;
	}




	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	
	
	public Date getDeletedAt() {
		return deletedAt;
	}
	
	
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt =deletedAt;
	}
	
	
	
	public Long getDeletedBy() {
		return deletedBy;
	}
	
	
	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	@Override
	public String toString() {
		return "ResourceCategoryModel [id=" + id + ", resourceId=" + resourceId + ", categoryId=" + categoryId+
				", deletedBy "+ deletedBy + ", deletedAt"+ deletedAt + "]";
	}
	
	
	

}

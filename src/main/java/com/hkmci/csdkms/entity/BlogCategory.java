package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "blog_category")
public class BlogCategory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@Column(name = "category", nullable = false)
	private String category;
	
	@Column(name = "category_c", nullable = false)
	private String categoryC;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "is_public", nullable = false)
	private Integer isPublic;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "is_deleted", nullable = false)
	private Integer isDeleted;
	
	@JsonIgnore
	@Column(name = "category_photo", nullable = true)
	private String categoryPhoto;
    
	@JsonIgnore
	@Column(name = "allow_comment", nullable = false)
	private Integer allowComment;
	
	@JsonIgnore
	@Column(name = "admin", nullable = true)
	private String admin;
    
	@Column(name = "ordering", nullable = false)
	private Integer ordering;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryC() {
		return categoryC;
	}

	public void setCategoryC(String categoryC) {
		this.categoryC = categoryC;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCategoryPhoto() {
		return categoryPhoto;
	}

	public void setCategoryPhoto(String categoryPhoto) {
		this.categoryPhoto = categoryPhoto;
	}

	public Integer getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Integer allowComment) {
		this.allowComment = allowComment;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	public BlogCategory(Long id, String category, String categoryC, Integer isPublic, Integer isDeleted,
			String categoryPhoto, Integer allowComment, String admin, Integer ordering) {
		super();
		this.id = id;
		this.category = category;
		this.categoryC = categoryC;
		this.isPublic = isPublic;
		this.isDeleted = isDeleted;
		this.categoryPhoto = categoryPhoto;
		this.allowComment = allowComment;
		this.admin = admin;
		this.ordering = ordering;
	}

	public BlogCategory() {
		
	}
    

}

package com.hkmci.csdkms.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="category")
public class CategoryModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id" , nullable = true)
	private Long Id;
	
	@Column(name="name_en")
	private String NameEn;
	
	@Column(name="name_tc")
	private String NameTc;
	
	@Column(name="level")
	private Long Level;
	
	@Column(name="parent_cat_id" , nullable = true)
	private Long parentCatId;
	
	@Column(name="order_cat", nullable = true)
	private Integer orderCat;
	
	@Column(name="show_info", nullable = true)
	private Boolean showInfo;

	
	@JsonIgnore
	@Column(name="created")
	private Date created;
	
	@JsonIgnore
	@Column(name="modified_at")
	private Date modifiedAt;
	
	@JsonIgnore
	@Column(name="modified_by")
	private Long modifiedBy;
	
	@JsonIgnore
	@Column(name="is_deleted")
	private Boolean isDeleted;
	
	@JsonIgnore
	@Column(name="deleted_by")
	private Long deletedBy;
	
	@JsonIgnore
	@Column(name="deleted_at")
	private Date deleted_at;
	
//	@OneToMany(mappedBy = "parentCatId",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
//	 List<CategoryModel> children ;
	@Transient
	List<CategoryModel> children ;
	
	
	public CategoryModel() {
		
	}

  

	public CategoryModel(Long id, String name, String name_tc, Long level, Long parentCatId,Boolean showInfo, Integer orderCat, List<CategoryModel> children, Boolean isDeleted) {
		
		this.Id = id;
		this.NameEn = name;
		this.NameTc = name_tc;
		this.Level = level;
		this.parentCatId = parentCatId;
		this.showInfo= showInfo;
		this.orderCat = orderCat;
		this.isDeleted=isDeleted;
		//this.Child=child;
	}

	


	@ManyToOne
	public Long getId() {
		return Id;
	}



	public void setId(Long id) {
		this.Id = id;
	}



	public String getNameEn() {
		return NameEn;
	}



	public void setNameEn(String name) {
		this.NameEn = name;
	}


	
	public String getNameTc() {
		return NameTc;
	}

	public void setNameTc(String nameC) {
		this.NameTc=nameC;
	}
	public Long getLevel() {
		return Level;
	}



	public void setLevel(Long level) {
		Level = level;
	}
    
	
	public Integer getorderCat() {
		return orderCat;
	}
    
	
	public void setorderCat(Integer OrderCat) {
		 orderCat = OrderCat;
	}
	
	
	public Boolean getshowInfo() {
		return showInfo;
	}
	
	public void setShowInfo(Boolean ShowInfo) {
		showInfo = ShowInfo;
	}
	
	

	public Long getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId (Long ParentCatId) {
		parentCatId = ParentCatId;
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



	public Boolean getIsDeleted() {
		return isDeleted;
	}



	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}



	public Long getDeletedBy() {
		return deletedBy;
	}



	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}



	public Date getDeleted_at() {
		return deleted_at;
	}



	public void setDeleted_at(Date deleted_at) {
		this.deleted_at = deleted_at;
	}



	@Transient
	public List<CategoryModel> getChildren(){
		return  children;
	}

	public void setChildren(List<CategoryModel> children) {
		this.children = children;
	}
	
	
	
	
	public Date getCreated() {
		return created;
	}



	public void setCreated(Date created) {
		this.created = created;
	}



	@Override
	public String toString() {
		return "CategoryModel [Id=" + Id + ", NameEN=" + NameEn +", NameTc= "+ NameTc +", Level=" + Level + ", parentCatId=" + parentCatId +
				", showInfo="+ showInfo +"]";
	}
	
	
	
	
	
	
	
}

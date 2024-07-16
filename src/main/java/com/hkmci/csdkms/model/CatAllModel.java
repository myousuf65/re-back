package com.hkmci.csdkms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="category")
public class CatAllModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long Id;
	
	@Column(name="name_en")
	private String NameEn;
	
	@Column(name="name_tc")
	private String NameTc;
	
	@JsonInclude(value=Include.NON_NULL)
	@Column(name="parent_cat_id")
	private Long parentCatId;
	
	@JsonInclude(value=Include.NON_NULL)
	@Column(name="level")
	private Long level;
	
	
	
	@Column(name="order_cat")
	private Integer orderCat;
	
	@Column(name="show_info")
	private Boolean showInfo;
	

	@JsonIgnore
	@Column(name="modified_at")
	private Date modifiedAt;
	
	@JsonIgnore
	@Column(name="modified_by")
	private Long modifiedBy;
	
	
	@Column(name="is_deleted")
	private Boolean isDeleted;
	
	@JsonIgnore
	@Column(name="deleted_by")
	private Long deletedBy;
	
	@JsonIgnore
	@Column(name="deleted_at")
	private Date deleted_at;
	
	@JsonIgnore
	@Column( name="created")
	private Date created;
	
	@JsonIgnore
	@Column( name="created_by")
	private Long createdBy;
	
	
	public CatAllModel() {
		
	}

  

	public CatAllModel(Long id, String name, String name_tc, Long level, Integer orderCat, Boolean showInfo,Boolean isDeleted) {
		
		this.Id = id;
		this.NameEn = name;
		this.NameTc = name_tc;
		this.orderCat = orderCat;
		this.showInfo = showInfo;
		this.isDeleted= isDeleted;

		//this.Child=child;
	}

	


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
	
	public Long getLevel() {
		return level;
	}
	
	
	public void setLevel (Long Level) {
		level =Level;
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
	
	
	


	public Date getCreated() {
		return created;
	}



	public void setCreated(Date created) {
		this.created = created;
	}



	@Override
	public String toString() {
		return "CategoryModel [Id=" + Id + ", Name=" + NameEn +", nameC"+ NameTc+ ", parentCatId=" + parentCatId + ", showInfo "+ showInfo+" ]";
	}
		
		

		


}

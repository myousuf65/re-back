package com.hkmci.csdkms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name="sitefunction")
public class SitefuncsModel {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column( name="func_name")
	private String funcName;

	@Column( name="func_name_c")
	private String funcNameC;
	
	
	@JsonIgnore
	@Column( name="func_desc")
	private String funcDesc;
	
	@JsonIgnore
	@Column( name="is_admin")
	private Long isAdmin;
	
	@JsonIgnore
	@Column( name="sort_order")
	private Long sortOrder;
	
	@JsonIgnore
	@Column( name="is_deleted")
	private Boolean isDeleted;
	
	@JsonIgnore
	@Column( name="deleted_at")
	private Date deletedAt;
	
	@JsonIgnore
	@Column( name="deleted_by")
	private Long deletedBy;
	
	
	
//	@OneToMany(mappedBy = "sitefuncId", cascade = CascadeType.ALL)
	//@Column( name ="id")
	//private List<ACLModel> aclModel;
	
	
	public SitefuncsModel(Integer id,String funcName,String funcNameC) {
		this.id = id;
		this.funcName = funcName;
		this.funcNameC = funcNameC;
	}
	
	
	public SitefuncsModel(Integer id, String funcName,String funcNameC, String funcDesc, Long isAdmin, Long sortOrder,
			Boolean isDeleted, Date deletedAt, Long deletedBy) {
		super();
		this.id = id;
		this.funcName = funcName;
		this.funcDesc = funcDesc;
		this.funcNameC = funcNameC;
		this.isAdmin = isAdmin;
		this.sortOrder = sortOrder;
		this.isDeleted = isDeleted;
		this.deletedAt = deletedAt;
		this.deletedBy = deletedBy;
	
	}

	public SitefuncsModel() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getFuncDesc() {
		return funcDesc;
	}

	public void setFuncDesc(String funcDesc) {
		this.funcDesc = funcDesc;
	}

	public Long getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Long isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
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


	public String getFuncNameC() {
		return funcNameC;
	}

	public void setFuncNameC(String funcNameC) {
		this.funcNameC = funcNameC;
	}

	@Override
	public String toString() {
		return "SitefuncsModel  id="+id+" funcName=" + funcName +", funcNameC="+ funcNameC + ", funcDesc=" + funcDesc + ", isAdmin=" + isAdmin
				+ ", sortOrder=" + sortOrder + ", isDeleted=" + isDeleted + ", deletedAt=" + deletedAt + ", deletedBy="
				+ deletedBy + "]";
	}



	
	
}

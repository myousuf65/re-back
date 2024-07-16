package com.hkmci.csdkms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="institutions")
public class InstitutionsModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="inst_name")
	private String instName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="inst_desc")
	private String instDesc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="sort_order")
	private Integer sortOrder;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="z_inst_id")
	private Integer zInstId;

	public InstitutionsModel(Long id, String instName, String instDesc, Integer sortOrder, Integer zInstId) {
		super();
		this.id = id;
		this.instName = instName;
		this.instDesc = instDesc;
		this.sortOrder = sortOrder;
		this.zInstId = zInstId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getInstDesc() {
		return instDesc;
	}

	public void setInstDesc(String instDesc) {
		this.instDesc = instDesc;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getzInstId() {
		return zInstId;
	}

	public void setzInstId(Integer zInstId) {
		this.zInstId = zInstId;
	}

	@Override
	public String toString() {
		return "InstitutionsModel [id=" + id + ", instName=" + instName + ", instDesc=" + instDesc + ", sortOrder="
				+ sortOrder + ", zInstId=" + zInstId + "]";
	}
	
	public InstitutionsModel() {
		
	}
	
	public InstitutionsModel(Long id, String inst_name) {
		super();
		this.id=id;
		this.instName = inst_name;
		
	}
}

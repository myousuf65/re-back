package com.hkmci.csdkms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table (name="sections")
public class SectionModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	@Column(name = "id")
	private Long id;
	
	@Column(name ="section_name")
	private String sectionName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "section_desc")
	private String sectionDesc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name= "sort_order")
	private Integer sortOrder;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name="z_section_id")
	private Integer zSectionId;

	public SectionModel(Long id, String sectionName, String sectionDesc, Integer sortOrder, Integer zSectionId) {
		super();
		this.id = id;
		this.sectionName = sectionName;
		this.sectionDesc = sectionDesc;
		this.sortOrder = sortOrder;
		this.zSectionId = zSectionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionDesc() {
		return sectionDesc;
	}

	public void setSectionDesc(String sectionDesc) {
		this.sectionDesc = sectionDesc;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getzSectionId() {
		return zSectionId;
	}

	public void setzSectionId(Integer zSectionId) {
		this.zSectionId = zSectionId;
	}

	@Override
	public String toString() {
		return "SectionModel [id=" + id + ", sectionName=" + sectionName + ", sectionDesc=" + sectionDesc
				+ ", sortOrder=" + sortOrder + ", zSectionId=" + zSectionId + "]";
	}
	
	public SectionModel() {
		
	}
	
	public SectionModel(Long id, String section_name) {
		super();
		this.id = id;
		this.sectionName = section_name;
	}
	
}

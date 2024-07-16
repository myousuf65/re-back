package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Sections {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "section_name", nullable = false)
	private String sectionName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "section_desc", nullable = false)
	private String sectionDesc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "sort_order", nullable = false)
	private Integer sortOrder;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "z_section_id", nullable = false)
	private Integer zSectionId;
	
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

    public Integer getZSectionId() {
        return zSectionId;
    }

    public void setZSectionId(Integer zSectionId) {
        this.zSectionId = zSectionId;
    }
}

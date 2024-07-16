package com.hkmci.csdkms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "institutions")
public class Institutions {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "inst_name", nullable = false)
	private String instName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "inst_desc", nullable = false)
	private String instDesc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "sort_order", nullable = false)
	private Integer sortOrder;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "z_inst_id", nullable = false)
	private Integer zInstId;
	
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

    public Integer getZInstId() {
        return zInstId;
    }

    public void setZInstId(Integer zInstId) {
        this.zInstId = zInstId;
    }
}

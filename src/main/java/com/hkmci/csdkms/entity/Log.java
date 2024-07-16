package com.hkmci.csdkms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "log")
public class Log {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "staff_no", nullable = false)
	private String staffNo;
    
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "logtype_id")
    @Column(name = "logtype_id", nullable = false)  
    private Long logtype;
    
    @Column(name = "channel", nullable = false)  
    private Integer channel;
    
    @Column(name = "table_id", nullable = false)  
	private Long tableId;
    
    @Column(name = "pkid", nullable = false)  
	private Long pkid;
    
    @Column(name = "urank_id", nullable = false)  
	private Long urankId;
    
    @Column(name = "uinst_id", nullable = false)  
	private Long uinstId;
    
    @JsonIgnore
    @Column(name = "category_id", nullable = false)  
	private String categoryId;
    
    @Transient 
    List<Integer> categoryList;
    
    @Column(name = "usection_id", nullable = false)  
	private Long usectionId;
    
    @Column(name = "result", nullable = false)  
	private String result;
    
    @Column(name = "remark", nullable = true)
    private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLogtype() {
		return logtype;
	}

	public void setLogtype(Long logtype) {
		this.logtype = logtype;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public Long getUrankId() {
		return urankId;
	}

	public void setUrankId(Long urankId) {
		this.urankId = urankId;
	}

	public Long getUinstId() {
		return uinstId;
	}

	public void setUinstId(Long uinstId) {
		this.uinstId = uinstId;
	}

	public Long getUsectionId() {
		return usectionId;
	}

	public void setUsectionId(Long usectionId) {
		this.usectionId = usectionId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

	public String getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	
	

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	

	public List<Integer> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Integer> categoryList) {
		this.categoryList = categoryList;
	}

	public Log(Long id, Date createdAt, Long createdBy, String staffNo, Long logtype, Long tableId, Long pkid,
			Long urankId, Long uinstId, Long usectionId, String result, String remark) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.staffNo = staffNo;
		this.logtype = logtype;
		this.tableId = tableId;
		this.pkid = pkid;
		this.urankId = urankId;
		this.uinstId = uinstId;
		this.usectionId = usectionId;
		this.result = result;
		this.remark = remark;
	}
    
    public Log() {
    	
    }
    
    public Log(User user, Long pkid, String remark, String result, Long table_id, Long logType) {
		this.categoryId = "0";
    	this.createdBy = user.getId();
    	this.createdAt = new Date();
    	this.staffNo = user.getStaffNo();
    	this.uinstId = user.getInstitutionId();
    	this.urankId = user.getRankId();
    	this.usectionId = user.getSectionId();
    	this.pkid = pkid;
    	this.remark = remark;
    	this.result = result;
    	this.tableId = table_id;
    	this.logtype = logType;
    }

}

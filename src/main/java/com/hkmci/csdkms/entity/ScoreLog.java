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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "score_log")
public class ScoreLog {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "log_id", nullable = false)
	private Long logId;
	
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "user_id", nullable = false)
	private Long userId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "score", nullable = false)
	private Integer score;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="urank_id",nullable = false)
    private Long urankId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="uinst_id", nullable = false)
    private Long uinstId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="usection_id",nullable = false)
    private Long usectionId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="staff_no",nullable = false)
    private String staffNo;
    
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

//    
//    @Transient
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private Long institutionId;
//    
//    
    
    
    
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
//	
//	public Long getInstId(){
//		return institutionId;
//	}
//	
//	public void setInstId(Long institutionId) {
//		this.institutionId = institutionId;
//	}
//	
//	
//	public ScoreLog(Integer score , Long institutionId) {
//		this.score = score;
//		this.institutionId = institutionId;
//	}
	
	
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
	
	public String getStaffNo() {
		return staffNo;
	}
	
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
	public ScoreLog(Long logId,User user,Integer score,Date createdAt) {
		
		this.logId = logId;
		this.score = score;
		this.userId = user.getId();
		
		this.uinstId = user.getInstitutionId();
		this.urankId = user.getRankId();
		this.usectionId = user.getSectionId();
		this.staffNo = user.getStaffNo();
		this.createdAt=createdAt;
		
		
		
		
		
	}
	public ScoreLog(Long logId,User user,Integer score) {
		
		this.logId = logId;
		this.score = score;
		this.userId = user.getId();
		this.uinstId = user.getInstitutionId();
		this.urankId = user.getRankId();
		this.usectionId = user.getSectionId();
		this.staffNo = user.getStaffNo();
		this.createdAt=new Date();
		
		
	}
	public ScoreLog(Long id, Long logId, Long userId, Integer score) {
		super();
		this.id = id;
		this.logId = logId;
		this.userId = userId;
		this.score = score;
		this.createdAt = new Date();
	}
	
	public ScoreLog(Long logId, Long userId, Integer score) {
		super();
		this.logId = logId;
		this.userId = userId;
		this.score = score;
		this.createdAt = new Date();
	}

	public ScoreLog() {
		
	}

    
}
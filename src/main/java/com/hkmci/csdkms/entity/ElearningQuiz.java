package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "elearning_quiz")
public class ElearningQuiz {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "cat_id", nullable = false)
	private Long catId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "title", nullable = false)
	private String title;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "access_rule_id", nullable = true)  
    private String accessRuleId;
    
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "access_channel", nullable = true)  
    private String accessChannel;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "limit_time", nullable = false)  
    private int limitTime;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "pass_mark", nullable = false)  
    private int passMark;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "repeat_time", nullable = false)  
    private int repeatTime;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "result", nullable = false)  
    private int result;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "is_public", nullable = false)  
    private long isPublic;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "publish_at", nullable = true)
	private Date publishAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
    

    @Column(name = "is_deleted", nullable = false)  
	private Long isDeleted;
    

	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "deleted_at", nullable = true)
	private Date deletedAt;
    

	@Column(name = "deleted_by", nullable = true)
	private Long deletedBy;


	@DateTimeFormat(pattern="yyyy-mm-dd")
	@Column(name = "modified_at", nullable = true)
	private Date modifiedAt;
    
  
	@Column(name = "modified_by", nullable = true)
	private Long modifiedBy;
	
	private int runJoin;
	
	private int runResult;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccessRuleId() {
        return accessRuleId;
    }

    public void setAccessRuleId(String accessRuleId) {
        this.accessRuleId = accessRuleId;
    }

    public String getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(String accessChannel) {
        this.accessChannel = accessChannel;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }

    public int getPassMark() {
        return passMark;
    }

    public void setPassMark(int passMark) {
        this.passMark = passMark;
    }

    public int getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(int repeatTime) {
        this.repeatTime = repeatTime;
    }
    
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(long isPublic) {
        this.isPublic = isPublic;
    }

    public Date getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Long isDeleted) {
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
    
    public int getRunJoin() {
        return runJoin;
    }

    public void setRunJoin(int runJoin) {
        this.runJoin = runJoin;
    }
    
    public int getRunResult() {
        return runResult;
    }

    public void setRunResult(int runResult) {
        this.runResult = runResult;
    }
}

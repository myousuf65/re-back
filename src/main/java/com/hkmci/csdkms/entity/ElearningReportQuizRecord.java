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
@Table(name = "elearning_report_quiz_record")
public class ElearningReportQuizRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
	@Column(name = "id", nullable = false)  
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "course_id", nullable = false)
	private Long courseId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "quiz_id", nullable = false)
	private Long quizId;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "quiz_conduct_code", nullable = false)
    private String quizConductCode;
	
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
    @Column(name = "time_use", nullable = false)  
    private int timeUse;
    
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
    @Column(name = "score", nullable = false)  
    private int score;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "total_score", nullable = false)  
    private int totalScore;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "score_percentage", nullable = false)  
    private int scorePercentage;
    
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
	
	@Column(name = "staff_no", nullable = true)
	private String staffNo;
	
	@Column(name = "fullname", nullable = true)
	private String fullname;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCourseId() {
        return quizId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
    
    public String getQuizConductCode() {
        return quizConductCode;
    }

    public void setQuizConductCode(String quizConductCode) {
        this.quizConductCode = quizConductCode;
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
    
    public int getTimeUse() {
        return timeUse;
    }

    public void setTimeUse(int timeUse) {
        this.timeUse = timeUse;
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
    

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
    public int getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(int scorePercentage) {
        this.scorePercentage = scorePercentage;
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
    
    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}

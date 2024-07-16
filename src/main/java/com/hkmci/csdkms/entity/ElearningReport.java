package com.hkmci.csdkms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "elearning_report")
public class ElearningReport {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Increment
    @Column(name = "id", nullable = false)  
    private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "quiz_id", nullable = false)
    private Long quizId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "question_id", nullable = false)
    private Long questionId;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "quiz_conduct_code", nullable = false)
    private String quizConductCode;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "question_title", nullable = false)
    private String questionTitle;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "answer", nullable = false)
    private String answer;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "correct_answer", nullable = false)
    private Integer correctAnswer;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "user_answer", nullable = false)
    private Integer userAnswer;
   
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "is_deleted", nullable = false)
    private Long isDeleted;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "deleted_at", nullable = true)
    private Date deletedAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "deleted_by", nullable = true)
    private Long deletedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created_at", nullable = true)
    private Date createdAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created_by", nullable = true)
    private Long createdBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "modified_at", nullable = true)
    private Date modifiedAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "modified_by", nullable = true)
    private Long modifiedBy;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "post_video_link", nullable = true)
    private String postVideoLink;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizConductCode() {
        return quizConductCode;
    }

    public void setQuizConductCode(String quizConductCode) {
        this.quizConductCode = quizConductCode;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
    
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }
    
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(Integer userAnswer) {
        this.userAnswer = userAnswer;
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
    
    public String getPostVideoLink() {
        return postVideoLink;
    }
    
    public void setPostVideoLink(String postVideoLink) {
        this.postVideoLink = postVideoLink;
    }
}

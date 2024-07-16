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
@Table(name = "elearning_quiz_question")
public class ElearningQuizQuestion {
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
	@Column(name = "order_by", nullable = false)
	private Integer orderBy;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "created_by", nullable = false)
	private Long createdBy;
	
	// Getter 方法
    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    // Setter 方法
    public void setId(Long id) {
        this.id = id;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}

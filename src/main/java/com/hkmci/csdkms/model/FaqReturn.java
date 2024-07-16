package com.hkmci.csdkms.model;

import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.entity.Faq;

public class FaqReturn {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String QuestionTc ;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String QuestionEn;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String AnswerTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String AnswerEn;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String TitleTc;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String TitleEn;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer Level;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long parentId;
	
	
	
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	List<FaqReturn> faqQuestion;
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionTc() {
		return QuestionTc;
	}

	public void setQuestionTc(String questionTc) {
		QuestionTc = questionTc;
	}

	public String getQuestionEn() {
		return QuestionEn;
	}

	public void setQuestionEn(String questionEn) {
		QuestionEn = questionEn;
	}

	public String getAnswerTc() {
		return AnswerTc;
	}

	public void setAnswerTc(String answerTc) {
		AnswerTc = answerTc;
	}

	public String getAnswerEn() {
		return AnswerEn;
	}

	public void setAnswerEn(String answerEn) {
		AnswerEn = answerEn;
	}

	public String getTitleTc() {
		return TitleTc;
	}

	public void setTitleTc(String titleTc) {
		TitleTc = titleTc;
	}

	public String getTitleEn() {
		return TitleEn;
	}

	public void setTitleEn(String titleEn) {
		TitleEn = titleEn;
	}

	public Integer getLevel() {
		return Level;
	}

	public void setLevel(Integer level) {
		Level = level;
	}

	public List<FaqReturn> getFaqQuestion() {
		return faqQuestion;
	}

	public void setFaqQuestion(List<FaqReturn> faqQuestion) {
		this.faqQuestion = faqQuestion;
	}
	
	
}

package com.hkmci.csdkms.model;
import java.util.List;

import com.hkmci.csdkms.entity.ElearningQuestion;
public class ManageQuestionRequest {
	  private Long quizId;
	  private Long userId;
	  private List<ElearningQuestion> addedQuestionList;

	  public Long getQuizId() {
	    return quizId;
	  }
	  
	  public Long getUserId() {
		    return userId;
		  }

	  public void setQuizId(Long quizId) {
	    this.quizId = quizId;
	  }
	  
	  public void setUserId(Long userId) {
		    this.userId = userId;
		  }

	  public List<ElearningQuestion> getAddedQuestionList() {
	    return addedQuestionList;
	  }

	  public void setAddedQuestionList(List<ElearningQuestion> addedQuestionList) {
	    this.addedQuestionList = addedQuestionList;
	  }
}

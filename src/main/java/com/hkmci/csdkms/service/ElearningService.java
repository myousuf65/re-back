package com.hkmci.csdkms.service;
import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.ElearningCategory;
import com.hkmci.csdkms.entity.ElearningCourse;
import com.hkmci.csdkms.entity.ElearningQuestion;
import com.hkmci.csdkms.entity.ElearningQuiz;
import com.hkmci.csdkms.entity.ElearningQuizQuestion;
import com.hkmci.csdkms.entity.ElearningReport;
import com.hkmci.csdkms.entity.ElearningReportQuizRecord;
import com.hkmci.csdkms.model.ElearningQuizAndQuestion;

public interface ElearningService {
	public List<ElearningQuiz> searchQuiz(Long Id, Long page, String search, Long catId);
	public Integer searchQuizTotal(Long Id ,  String search, Long catId);
	public ElearningQuiz addQuiz(ElearningQuiz elearningQuiz);
	public ElearningQuiz updateQuiz(ElearningQuiz elearningQuiz);
	public ElearningQuestion updateQuestion(ElearningQuestion elearningQuestion);
	public Optional<ElearningQuiz> findQuizById(Long Id);
	public Optional<ElearningQuestion> findQuestionById(Long Id);
	public ElearningCategory addCategory(ElearningCategory elearningCategory);
	public List<ElearningCategory> categorySearch(Long Id, Long page, String search);
	public Integer categorySearchTotal(Long Id, Long page, String search);
	public List<ElearningCategory> categoryAllSearch(Long Id);
	public Integer categoryAllSearchTotal(Long Id);
	public ElearningQuestion addQuestion(ElearningQuestion elearningQuestion);
	public List<ElearningQuestion> questionSearch(Long Id,Long catId, Long page, String search);
	public Integer questionSearchTotal(Long Id,Long catId, String search);
	public ElearningQuizQuestion updateQuizQuestion(ElearningQuizQuestion elearningQuizQuestion);
	public void cleanQuizQuestion(Long quizId);
	public void cleanCourseQuiz(Long courseId);
	public ElearningQuizAndQuestion searchAccess2(Long id, List<Long> accessRuleId, long accessChannel);
	public ElearningQuiz searchAccessQuiz(Long id, List<Long> accessRuleId, List<String> channel);
	public ElearningReport addReport(ElearningReport elearningReport);
	public void addQuizReport(ElearningReportQuizRecord quizRecord);
	public List<ElearningQuiz> searchAccessQuizClient(Long id, Long page, List<Long> accessRuleId, List<String> Channel);
	public Integer searchTotalClient(Long id, List<Long> accessRuleId, List<String> channel);
	public List<ElearningCourse> courseSearch(Long id, Long page, String search);
	public Integer courseSearchTotal(Long id, String search);
	public List<ElearningCourse> searchAccessCourseClient(String staffNo, Long page, List<String> channel, List<Long> accessRuleId);
	public Integer searchAccessCourseClientTotal(String staffNo, List<String> channel, List<Long> accessRuleId);
	public ElearningCourse findByIdAndStaffList(Long courseId, String staffNo, List<Long> accessRuleId);
}

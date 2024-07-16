package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.ElearningCategory;
import com.hkmci.csdkms.entity.ElearningCourse;
import com.hkmci.csdkms.entity.ElearningQuestion;
import com.hkmci.csdkms.entity.ElearningQuiz;
import com.hkmci.csdkms.entity.ElearningQuizQuestion;
import com.hkmci.csdkms.entity.ElearningReport;
import com.hkmci.csdkms.entity.ElearningReportQuizRecord;
import com.hkmci.csdkms.model.ElearningQuizAndQuestion;
import com.hkmci.csdkms.repository.ElearningCategoryRepository;
import com.hkmci.csdkms.repository.ElearningCourseQuizRepository;
import com.hkmci.csdkms.repository.ElearningCourseRepository;
import com.hkmci.csdkms.repository.ElearningQuestionRepository;
import com.hkmci.csdkms.repository.ElearningQuizQuestionRepository;
import com.hkmci.csdkms.repository.ElearningQuizRepository;
import com.hkmci.csdkms.repository.ElearningReportQuizRecordRepository;
import com.hkmci.csdkms.repository.ElearningReportRepository;

@Service
@Transactional
public class ElearningServiceImp implements ElearningService{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Resource
	private ElearningCourseQuizRepository elearningCourseQuizRepository;
	
	@Autowired
	@Resource
	private ElearningCourseRepository elearningCourseRepository;
	
	@Autowired
	@Resource
	private ElearningQuizRepository elearningQuizRepository;
	
	@Autowired
	@Resource
	private ElearningCategoryRepository elearningCategoryRepository;
	
	@Autowired
	@Resource
	private ElearningQuestionRepository elearningQuestionRepository;
	
	@Autowired
	@Resource
	private ElearningQuizQuestionRepository elearningQuizQuestionRepository;
	
	@Autowired
	@Resource
	private ElearningReportRepository elearningReportRepository;
	
	@Autowired
	@Resource
	private ElearningReportQuizRecordRepository elearningReportQuizRecordRepository;
	
	@Autowired
	@Resource
    private Common common;
	
	@Override
	public List<ElearningQuiz> searchQuiz(Long Id,Long page, String search, Long catId) {
//		List<String> channel = new ArrayList<>();
//		if(String.valueOf(accessChannel).equals("1")) {
//	      	channel.add("0");
//	      	channel.add("1");
//	      	channel.add("2");
//	    }else {
//	    	channel.add("2");
//	      	channel.add("4");
//	    } 
//		
		List<ElearningQuiz> QuizList = new ArrayList<ElearningQuiz>();
		QuizList = searchQuiz2(Id,page,search,catId);
		return QuizList;
	}
	
	@Override
	public ElearningQuizAndQuestion searchAccess2(Long id, List<Long> accessRuleId, long accessChannel) {
		List<String> channel = new ArrayList<>();
		if(String.valueOf(accessChannel).equals("1")) {
	      	channel.add("0");
	      	channel.add("1");
	      	channel.add("2");
	    }else {
	    	channel.add("2");
	      	channel.add("4");
	    } 
		ElearningQuiz quiz = new ElearningQuiz();
		quiz = searchAccessQuiz(id,accessRuleId,channel);
		List<ElearningQuizQuestion> quizQuestions = elearningQuizQuestionRepository.findByQuizId(quiz.getId());
		List<ElearningQuestion> questions = new ArrayList<>();

		for (ElearningQuizQuestion quizQuestion : quizQuestions) {
		    Optional<ElearningQuestion> questionOptional  = elearningQuestionRepository.findById(quizQuestion.getQuestionId());
		    if (questionOptional.isPresent()) {
		        ElearningQuestion question = questionOptional.get();
		        questions.add(question);
		    }
		}
		ElearningQuizAndQuestion quizQuestion = new ElearningQuizAndQuestion();
		quizQuestion.setQuestions(questions);
		quizQuestion.setQuiz(quiz);
		return quizQuestion;
	}
	
	@Override
	public List<ElearningCategory> categorySearch(Long Id, Long page, String search) {
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 10;
		Integer end_num = 10;
		
		String sql = "select * from elearning_category"
				+ " where ( id = :id or :id = 0)"
				+ " AND (title LIKE CONCAT('%', :search, '%')  OR :search = '')"
				+ " AND is_deleted = 0 ORDER BY id desc"
				+ " limit :startNum , :endNum";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",Id);
		query.setParameter("search",search);
		query.setParameter("startNum",start_num);
		query.setParameter("endNum",end_num);
		List<Object[]> result = query.getResultList(); 
		System.out.println("ElearningQuizSqlList:"+sql);
		List<ElearningCategory> cats = new ArrayList<>();
		for (Object[] row : result) {		    
			ElearningCategory cat = new ElearningCategory();
		    cat.setId(((Integer) row[0]).longValue());
		    cat.setTitle((String) row[1]);
		    cat.setCreatedAt((Date) row[2]);
		    cat.setCreatedBy(((Integer) row[3]).longValue());
		    cats.add(cat);
		}
		return cats;
		
	}
	
	public Integer categorySearchTotal(Long Id, Long page, String search) {
		String sql = "select * from elearning_category"
				+ " where ( id = :id or :id = 0)"
				+ " AND (title LIKE CONCAT('%', :search, '%')  OR :search = '')"
				+ " AND is_deleted = 0 ORDER BY created_at DESC";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",Id);
		query.setParameter("search",search);
		List<Object[]> result = query.getResultList();
		return result.size();
	}
	
	public List<ElearningCategory> categoryAllSearch(Long Id) {
		String sql = "select * from elearning_category"
				+ " where ( id = :id or :id = 0)"
				+ " and is_deleted = 0 ORDER BY title ASC";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",Id);
		List<Object[]> result = query.getResultList(); 
		System.out.println("ElearningQuizSqlList:"+sql);
		List<ElearningCategory> cats = new ArrayList<>();
		for (Object[] row : result) {		    
			ElearningCategory cat = new ElearningCategory();
		    cat.setId(((Integer) row[0]).longValue());
		    cat.setTitle((String) row[1]);
		    cat.setCreatedAt((Date) row[2]);
		    cat.setCreatedBy(((Integer) row[3]).longValue());
		    cats.add(cat);
		}
		return cats;
	}
	public Integer categoryAllSearchTotal(Long Id) {
		String sql = "select * from elearning_category"
				+ " where ( id = :id or :id = 0)"
				+ " and is_deleted = 0 ORDER BY created_at DESC";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",Id);
		List<Object[]> result = query.getResultList();
		return result.size();
	}
	
	public List<ElearningQuestion> questionSearch(Long Id,Long catId, Long page, String search) {
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 10;
		Integer end_num = 10;
		
		List<ElearningQuestion> query_data = elearningQuestionRepository.questionSearch(catId,start_num,end_num,search);
		return query_data;
	}
	
	public Integer questionSearchTotal(Long Id,Long catId, String search) {
		String sql = "select * from elearning_question"
				+ " where ( cat_id = :catId or :catId = 0)"
				+ " AND is_deleted = 0"
				+ " AND (question_title LIKE CONCAT('%', :search, '%')  OR :search = '') ";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("catId",catId);
		query.setParameter("search", search);
		List<Object[]> result = query.getResultList();
		return result.size();
	}
	
	@Override
	public List<ElearningCourse> courseSearch(Long id, Long page, String search) {
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 10;
		Integer end_num = 10;
		List<ElearningCourse> query_data = elearningCourseRepository.courseSearch(start_num,end_num,search);
		return query_data;
	}
	
	@Override
	public Integer courseSearchTotal(Long id, String search) {
		String sql = "select * from elearning_course"
				+ " where is_deleted = 0"
				+ " AND (course_name LIKE CONCAT('%', :search, '%')  OR :search = '')";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("search", search);
		List<Object[]> result = query.getResultList();
		return result.size();
	}
	
	@Override
	public Optional<ElearningQuiz> findQuizById(Long Id) {
		return elearningQuizRepository.findByIdAndIsDeleted_owner(Id);
	}
	
	@Override
	public Optional<ElearningQuestion> findQuestionById(Long Id) {
		return elearningQuestionRepository.findById(Id);
	}
	
	public List<ElearningQuiz> searchQuiz2(Long Id,Long page,String search,Long catId) {
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 10;
		Integer end_num = 10;
/*		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select * from elearning_quiz b"
				+ " where ( id = :id or :id = 0)"
				+ " and (cat_id = :catId or :catId = 0)"
				+ " and is_deleted = 0 and is_public = 1 and access_channel IN (:channels) and ("+accessRuleIdString
		    		+	")"
				+ " limit :startNum , :endNum"; */
//		String sql = "select eq.* from elearning_quiz eq"
//				+ " LEFT JOIN elearning_course_quiz ecq ON ecq.quiz_id=eq.id" 
//				+ " where (ecq.course_id = :courseId OR :courseId = 0)"
//				+ " and ( eq.id = :id or :id = 0)"
//				+ " and ( eq.title LIKE CONCAT('%', :search, '%') or :search = '')"
//				+ " and eq.is_deleted = 0 ORDER BY eq.created_at DESC"
//				+ " limit :startNum , :endNum";
		
		
		String sql = "select eq.* from elearning_quiz eq" 
		+ " where"
		+ " ( eq.id = :id or :id = 0)"
		+ " and ( eq.title LIKE CONCAT('%', :search, '%') or :search = '')"
		+ " and eq.is_deleted = 0 ORDER BY eq.created_at DESC"
		+ " limit :startNum , :endNum";
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",Id);
//		query.setParameter("courseId",catId);
		query.setParameter("search",search);
		query.setParameter("startNum",start_num);
		query.setParameter("endNum",end_num);
//		for (int i = 0; i < accessRuleId.size(); i++) {
//    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
//    	}
		List<Object[]> result = query.getResultList(); 
		System.out.println("ElearningQuizSqlList:"+sql);
		List<ElearningQuiz> quizs = new ArrayList<>();
		for (Object[] row : result) {		    
			ElearningQuiz quiz = new ElearningQuiz();
		    quiz.setId(((Integer) row[0]).longValue());
		    quiz.setCatId(((Integer) row[1]).longValue());
		    quiz.setTitle((String) row[2]);
		    quiz.setAccessChannel((String) row[3]);
		    quiz.setAccessRuleId((String) row[4]);
		    quiz.setLimitTime(((Integer) row[5]).intValue());
		    quiz.setPassMark(((Integer) row[6]).intValue());
		    quiz.setRepeatTime(((Integer) row[7]).intValue());
		    quiz.setResult(((Integer) row[8]).intValue());
		    quiz.setIsPublic(Byte.valueOf((byte) row[9]).longValue());
		    quiz.setPublishAt((Date) row[10]);
		    quiz.setCreatedBy(((Integer) row[11]).longValue());
		    quiz.setCreatedAt((Date) row[12]);
		    quiz.setIsDeleted(Byte.valueOf((byte) row[13]).longValue());
		    if(row[14]!=null) {
		    	quiz.setDeletedBy(((Integer) row[14]).longValue());
		    }
		    quiz.setModifiedAt((Date) row[15]);
		    if(row[16]!=null) {
		    	quiz.setModifiedBy(((Integer) row[16]).longValue());
		    }
		    quizs.add(quiz);
		}
    	
    	return quizs;
	}
	
	@Override
	public List<ElearningCourse> searchAccessCourseClient(String staffNo, Long page, List<String> channels, List<Long> accessRuleId) {
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 10;
		Integer end_num = 10;
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select * from elearning_course b"
				+ " where is_deleted = 0 and is_publish = 1"
				+ " AND access_channel IN (:channels) AND (("+accessRuleIdString
			    +	")"
				+ " OR FIND_IN_SET(:staffNo, REPLACE(staff_no_list, '\\n', ',')) > 0 )"
				+ " AND DATE(start_date) <= CURDATE() "
				+ " AND DATE(end_date) >= CURDATE()"
		    		+ " ORDER BY start_date DESC"
		    		+ " limit :startNum , :endNum";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("channels",channels);
		for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		query.setParameter("staffNo",staffNo);
		query.setParameter("startNum",start_num);
		query.setParameter("endNum",end_num);
		List<Object[]> result = query.getResultList();
		List<ElearningCourse> courses = new ArrayList<>();
		for (Object[] row : result) {
			ElearningCourse course = new ElearningCourse();
			course.setId(((Integer) row[0]).longValue());
			course.setCourseName((String) row[1]);
		    courses.add(course);
		}
    	
    	return courses;
	}
	
	public ElearningCourse findByIdAndStaffList(Long courseId, String staffNo, List<Long> accessRuleId) {
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select * from elearning_course b"
				+ " where is_deleted = 0 and is_Publish = 1 "
				+ " and id= :id and (FIND_IN_SET(:staffNo, REPLACE(staff_no_list, '\\n', ',')) > 0"
				+ " OR ("+accessRuleIdString+"))"
				+ " AND DATE(start_date) <= CURDATE() "
				+ " AND DATE(end_date) >= CURDATE()";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);

		for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		query.setParameter("staffNo",staffNo);
		query.setParameter("id",courseId);
		List<Object[]> result = query.getResultList();
		ElearningCourse course= new ElearningCourse();
		for (Object[] row : result) {
			course.setId(((Integer) row[0]).longValue());
			course.setCourseName((String) row[1]);
		}
    	
    	return course;
	}
	
	public ElearningQuiz searchAccessQuiz(Long Id,List<Long> accessRuleId,List<String> channel) {

		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
//		String sql = "select * from elearning_quiz b"
//				+ " where ( id = :id )"
//				+ " and is_deleted = 0 and is_public = 1 and access_channel IN (:channels) and ("+accessRuleIdString
//		    		+	")";
		String sql = "select * from elearning_quiz b"
				+ " where ( id = :id )"
				+ " and is_deleted = 0 and is_public = 1";
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",Id);
//		query.setParameter("channels",channel);
//		for (int i = 0; i < accessRuleId.size(); i++) {
//    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
//    	}
		List<Object[]> result = query.getResultList(); 
		ElearningQuiz quiz = new ElearningQuiz();
		for (Object[] row : result) {		    
		    quiz.setId(((Integer) row[0]).longValue());
		    quiz.setCatId(((Integer) row[1]).longValue());
		    quiz.setTitle((String) row[2]);
		    quiz.setAccessChannel((String) row[3]);
		    quiz.setAccessRuleId((String) row[4]);
		    quiz.setLimitTime(((Integer) row[5]).intValue());
		    quiz.setPassMark(((Integer) row[6]).intValue());
		    quiz.setRepeatTime(((Integer) row[7]).intValue());
		    quiz.setResult(((Integer) row[8]).intValue());
		    quiz.setIsPublic(Byte.valueOf((byte) row[9]).longValue());
		    quiz.setPublishAt((Date) row[10]);
		    quiz.setCreatedBy(((Integer) row[11]).longValue());
		    quiz.setCreatedAt((Date) row[12]);
		    quiz.setIsDeleted(Byte.valueOf((byte) row[13]).longValue());
		    if(row[14]!=null) {
		    	quiz.setDeletedBy(((Integer) row[14]).longValue());
		    }
		    quiz.setModifiedAt((Date) row[15]);
		    if(row[16]!=null) {
		    	quiz.setModifiedBy(((Integer) row[16]).longValue());
		    }
		}
    	
    	return quiz;
	}
	
	public Integer searchQuizTotal2(Long Id,String search,Long catId) {
//		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
//		String sql = "select * from elearning_quiz b"
//				+ " where ( id = :id or :id = 0)"
//				+ " and (cat_id = :catId or :catId = 0)"
//				+ " and is_deleted = 0 and is_public = 1 and access_channel IN (:channels) and ("+accessRuleIdString
//		    		+	")";
		String sql = "select eq.* from elearning_quiz eq"
				+ " LEFT JOIN elearning_course_quiz ecq ON ecq.quiz_id=eq.id" 
				+ " where (ecq.course_id = :courseId OR :courseId = 0)"
				+ " and ( eq.id = :id or :id = 0)"
				+ " and ( eq.title LIKE CONCAT('%', :search, '%') or :search = '')"
				+ " and eq.is_deleted = 0";

//				+ " limit :startNum , :endNum";
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",Id);
		query.setParameter("courseId",catId);
		query.setParameter("search",search);
//		query.setParameter("channels",channel);
//		for (int i = 0; i < accessRuleId.size(); i++) {
//    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
//    	}
		List<Object[]> result = query.getResultList(); 	
    	return result.size();
	}

	@Override
	public Integer searchQuizTotal(Long Id, String search, Long catId) {
//		List<String> channel = new ArrayList<>();
//		if(String.valueOf(accessChannel).equals("1")) {
//	      	channel.add("0");
//	      	channel.add("1");
//	      	channel.add("2");
//	    }else {
//	    	channel.add("2");
//	      	channel.add("4");
//	    }
		return searchQuizTotal2(Id,search,catId);
	}
	
	@Override
	public List<ElearningQuiz> searchAccessQuizClient(Long id, Long page, List<Long> accessRuleId, List<String> channel) {
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 10;
		Integer end_num = 10;
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
//		String sql = "select * from elearning_quiz b"
//				+ " where ( id = :id or :id = 0)"
//				+ " and is_deleted = 0 and is_public = 1 and access_channel IN (:channels) and ("+accessRuleIdString
//		    		+	") ORDER BY publish_at DESC"
//		    		+ " limit :startNum , :endNum";
		String sql = "select * from elearning_quiz b"
				+ " where ( id = :id or :id = 0)"
				+ " and is_deleted = 0 and is_public = 1 ORDER BY publish_at DESC"
		    		+ " limit :startNum , :endNum";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",id);
//		query.setParameter("channels",channel);
		query.setParameter("startNum",start_num);
		query.setParameter("endNum",end_num);
//		for (int i = 0; i < accessRuleId.size(); i++) {
//    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
//    	}
		List<Object[]> result = query.getResultList();
		List<ElearningQuiz> quizs = new ArrayList<>();
		for (Object[] row : result) {
			ElearningQuiz quiz = new ElearningQuiz();
		    quiz.setId(((Integer) row[0]).longValue());
		    quiz.setCatId(((Integer) row[1]).longValue());
		    quiz.setTitle((String) row[2]);
		    quiz.setAccessChannel((String) row[3]);
		    quiz.setAccessRuleId((String) row[4]);
		    quiz.setLimitTime(((Integer) row[5]).intValue());
		    quiz.setPassMark(((Integer) row[6]).intValue());
		    quiz.setRepeatTime(((Integer) row[7]).intValue());
		    quiz.setResult(((Integer) row[8]).intValue());
		    quiz.setIsPublic(Byte.valueOf((byte) row[9]).longValue());
		    quiz.setPublishAt((Date) row[10]);
		    quiz.setCreatedBy(((Integer) row[11]).longValue());
		    quiz.setCreatedAt((Date) row[12]);
		    quiz.setIsDeleted(Byte.valueOf((byte) row[13]).longValue());
		    if(row[14]!=null) {
		    	quiz.setDeletedBy(((Integer) row[14]).longValue());
		    }
		    quiz.setModifiedAt((Date) row[15]);
		    if(row[16]!=null) {
		    	quiz.setModifiedBy(((Integer) row[16]).longValue());
		    }
		    quizs.add(quiz);
		}
    	
    	return quizs;
	}
	
	@Override
	public Integer searchAccessCourseClientTotal(String staffNo, List<String> channels, List<Long> accessRuleId) {
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select * from elearning_course b"
				+ " where is_deleted = 0 and is_publish = 1"
				+ " AND access_channel IN (:channels) AND (("+accessRuleIdString
			    +	")"
				+ " OR FIND_IN_SET(:staffNo, REPLACE(staff_no_list, '\\n', ',')) > 0 )"
				+ " AND DATE(start_date) <= CURDATE() "
				+ " AND DATE(end_date) >= CURDATE()";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		query.setParameter("staffNo",staffNo);
		query.setParameter("channels",channels);
		List<Object[]> result = query.getResultList(); 	
    	return result.size();
	}
	
	@Override
	public Integer searchTotalClient(Long id, List<Long> accessRuleId, List<String> channel) {
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select * from elearning_quiz b"
				+ " where ( id = :id or :id = 0)"
				+ " and is_deleted = 0 and is_public = 1 and access_channel IN (:channels) and ("+accessRuleIdString
		    		+	")";

    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("id",id);
		query.setParameter("channels",channel);
		for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		List<Object[]> result = query.getResultList(); 	
    	return result.size();
	}
	
	@Override
	public ElearningQuiz addQuiz(ElearningQuiz elearningQuiz) {
/*		String sql = "INSERT INTO elearning_quiz(cat_id,title,access_channel,access_rule_id,limit_time,pass_mark,repeat_time,result,is_public,publish_at) VALUES (:catId,:title,:accessChannel,:accessRuleId,:limitTime,:passMark,:repeatTime,:result,:isPublic,:publishAt)";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("catId",elearningQuiz.getCatId());
		query.setParameter("title",elearningQuiz.getTitle());
		query.setParameter("accessChannel",elearningQuiz.getAccessChannel());
		query.setParameter("accessRuleId",elearningQuiz.getAccessRuleId());
		query.setParameter("limitTime",elearningQuiz.getLimitTime());
		query.setParameter("passMark",elearningQuiz.getPassMark());
		query.setParameter("repeatTime",elearningQuiz.getRepeatTime());
		query.setParameter("result",elearningQuiz.getResult());
		query.setParameter("isPublic",elearningQuiz.getIsPublic());
		query.setParameter("publishAt",elearningQuiz.getPublishAt());
		int rowsAffected = query.executeUpdate();

		// 获取插入行的 ID
		if (rowsAffected > 0) {
		    BigInteger generatedId = (BigInteger) entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult();
		    Long return_id = generatedId.longValue();
		    // 返回插入行的 ID
		    System.out.println("Inserted row ID: " + return_id);
		    elearningQuiz.setId(return_id);
		}
		return elearningQuiz;*/
		return elearningQuizRepository.saveAndFlush(elearningQuiz);
	}
	
	@Override
	public ElearningQuiz updateQuiz(ElearningQuiz elearningQuiz) {
		return elearningQuizRepository.saveAndFlush(elearningQuiz);
	}
	
	@Override
	public ElearningCategory addCategory(ElearningCategory elearningCategory) {
		return elearningCategoryRepository.saveAndFlush(elearningCategory);
	}
	
	@Override
	public ElearningQuestion addQuestion(ElearningQuestion elearningQuestion) {
		return elearningQuestionRepository.saveAndFlush(elearningQuestion);
	}
	
	@Override
	public ElearningQuestion updateQuestion(ElearningQuestion elearningQuestion) {
		return elearningQuestionRepository.saveAndFlush(elearningQuestion);
	}
	
	@Override
	public ElearningQuizQuestion updateQuizQuestion(ElearningQuizQuestion elearningQuizQuestion) {
		return elearningQuizQuestionRepository.saveAndFlush(elearningQuizQuestion);
	}
	
	@Override
	public void cleanQuizQuestion(Long quizId) {
		elearningQuizQuestionRepository.deleteByQuizId(quizId);
	}
	
	@Override
	public void cleanCourseQuiz(Long courseId) {
		elearningCourseQuizRepository.deleteByCourseId(courseId);
	}
	
	@Override
	public ElearningReport addReport(ElearningReport elearningReport) {
		return elearningReportRepository.saveAndFlush(elearningReport);
	}
	
	@Override
	public void addQuizReport(ElearningReportQuizRecord quizRecord) {
		elearningReportQuizRecordRepository.saveAndFlush(quizRecord);
	}
}


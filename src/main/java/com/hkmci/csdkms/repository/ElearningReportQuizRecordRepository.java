package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningQuestion;
import com.hkmci.csdkms.entity.ElearningReportQuizRecord;

public interface ElearningReportQuizRecordRepository extends JpaRepository<ElearningReportQuizRecord, Long>{
	@Query(value = "select * from elearning_report_quiz_record"
			+ " where quiz_id = :quizId AND created_by = :clientId order by created_at DESC" , nativeQuery = true)
	List<ElearningReportQuizRecord> reportQuizRecordSearch(Long clientId,Long quizId);
	
	@Query(value = "select * from elearning_report_quiz_record"
			+ " where quiz_id = :quizId AND course_id = :courseId AND created_by = :clientId order by created_at DESC" , nativeQuery = true)
	List<ElearningReportQuizRecord> reportQuizRecordSearch(Long clientId, Long courseId, Long quizId);


//	@Query(value = "select * from elearning_report_quiz_record "	
//			+ " WHERE quiz_id = :quizId order by fullname ASC" , nativeQuery=true)
	

	List<ElearningReportQuizRecord> findByQuizConductCode(String quizConductCode);

	@Query(value = "select * from elearning_report_quiz_record"
			+ " where quiz_id = :quizId AND course_id = :courseId order by FullName", nativeQuery = true)
	List<ElearningReportQuizRecord> findByCourseIdAndQuizID(Long courseId,Long quizId);

	@Query(value = "SELECT t.* FROM elearning_report_quiz_record t "
			+ "INNER JOIN (SELECT created_by, MAX(created_at) AS max_created_at "
			+ "FROM elearning_report_quiz_record WHERE quiz_id = :quizId GROUP BY created_by) t2 "
			+ "ON t.created_by = t2.created_by AND t.created_at = t2.max_created_at "
			+ "WHERE t.quiz_id = :quizId ORDER BY t.fullname ASC limit 0,1", nativeQuery = true)
	List<ElearningReportQuizRecord> findByQuizWithUser(Long quizId);
	
	@Query(value = "SELECT t.* FROM elearning_report_quiz_record t "
			+ "INNER JOIN (SELECT created_by, MAX(created_at) AS max_created_at "
			+ "FROM elearning_report_quiz_record WHERE quiz_id = :quizId GROUP BY created_by) t2 "
			+ "ON t.created_by = t2.created_by AND t.created_at = t2.max_created_at "
			+ "WHERE t.quiz_id = :quizId AND t.course_id= :courseId ORDER BY t.fullname ASC limit 0,1", nativeQuery = true)
	List<ElearningReportQuizRecord> findByQuizCourseWithUser(Long quizId, Long courseId);


}

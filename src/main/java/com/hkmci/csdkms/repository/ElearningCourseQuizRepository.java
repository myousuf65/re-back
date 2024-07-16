package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningCourseQuiz;

public interface ElearningCourseQuizRepository extends JpaRepository<ElearningCourseQuiz, Long>{
	void deleteByCourseId(Long courseId);
	
	@Query(value = "select * from elearning_course_quiz"
			+ " where course_id = :courseId"
			+ " ORDER BY order_by ASC"
			+ " limit :startNum , :endNum", nativeQuery = true)
	List<ElearningCourseQuiz> findByCourseIdPage(Long courseId, Integer startNum, Integer endNum);
	
	List<ElearningCourseQuiz> findByCourseId(Long courseId);

	@Query(value = "select * from elearning_course_quiz"
			+ " where course_id = :courseId"
			+ " AND quiz_id = :quizId", nativeQuery = true)
	Optional<ElearningCourseQuiz> findByCourseIdQuizId(Long courseId, Long quizId);
}

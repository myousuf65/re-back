package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningQuestion;

public interface ElearningQuestionRepository extends JpaRepository<ElearningQuestion, Long>{
	@Query(value = "select eq.*, ec.title from elearning_question eq"
			+ " LEFT JOIN elearning_category ec ON eq.cat_id=ec.id "
			+ " where ( eq.cat_id = :catId or :catId = 0)"
			+ " AND (eq.question_title LIKE CONCAT('%', :search, '%')  OR :search = '')"
			+ " AND eq.is_deleted = 0 ORDER BY eq.id DESC"
			+ " limit :startNum , :endNum" , nativeQuery = true)
	List<ElearningQuestion> questionSearch(Long catId,Integer startNum,Integer endNum, String search);

	
	@Query(value = "select * from elearning_question"
			+ " where ( cat_id = :catId or :catId = 0)"
			+ " and is_deleted = 0 ORDER BY id DESC"
			+ " limit :startNum , :endNum" , nativeQuery = true)
	List<ElearningQuestion> questionSearch2(Long catId,Integer startNum,Integer endNum);

}

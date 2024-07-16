package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkmci.csdkms.entity.ElearningQuizQuestion;

public interface ElearningQuizQuestionRepository extends JpaRepository<ElearningQuizQuestion, Long>{
	void deleteByQuizId(Long quizId);
	
	List<ElearningQuizQuestion> findByQuizId(Long quizId);
}

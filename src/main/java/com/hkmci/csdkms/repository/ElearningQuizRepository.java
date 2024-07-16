package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningQuiz;

public interface ElearningQuizRepository extends JpaRepository<ElearningQuiz, Long>{
	@Query(value = "select * from elearning_quiz"
			+ " where id = ?1 and is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningQuiz> findByIdAndIsDeleted_owner(Long elearningQuizId);
	
	@Query(value = "select * from elearning_quiz"
			+ " where id = :elearningQuizId and is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningQuiz> findByIdIsNotDelete(Long elearningQuizId);

	@Query(value = "select * from elearning_quiz"
			+ " where title = :title and is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningQuiz> findByTitle(String title);

	@Query(value = "select * from elearning_quiz"
			+ " where title = :title and id!= :elearningQuizId and is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningQuiz> findByIdTitle(Long elearningQuizId, String title);
}

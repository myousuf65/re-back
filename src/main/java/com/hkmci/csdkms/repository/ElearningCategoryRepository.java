package com.hkmci.csdkms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningCategory;
import com.hkmci.csdkms.entity.ElearningQuiz;

public interface ElearningCategoryRepository extends JpaRepository<ElearningCategory, Long>{
	@Query(value = "select * from elearning_category"
			+ " where title = :category and is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningCategory> findByCategoryTitle(String category);

	@Query(value = "select * from elearning_category"
			+ " where title = :category and id!=:elearningCatId and is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningCategory> findByIdCategoryTitle(Long elearningCatId, String category);

}

package com.hkmci.csdkms.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkmci.csdkms.entity.BlogCategory;


public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {

	//@Query("select bg from BlogGallery bg where bg.userId = ?1 and bg.isFinished = 0")
	List<BlogCategory> findByIsDeletedAndIsPublic(Integer isDeleted, Integer isPublic);
	
	
}

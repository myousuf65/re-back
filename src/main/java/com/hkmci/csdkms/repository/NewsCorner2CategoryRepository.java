package com.hkmci.csdkms.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkmci.csdkms.entity.NewsCorner2Category;


public interface NewsCorner2CategoryRepository extends JpaRepository<NewsCorner2Category, Long> {

	//@Query("select bg from NewsCorner2Gallery bg where bg.userId = ?1 and bg.isFinished = 0")
	List<NewsCorner2Category> findByIsDeletedAndIsPublic(Integer isDeleted, Integer isPublic);
	
	
}

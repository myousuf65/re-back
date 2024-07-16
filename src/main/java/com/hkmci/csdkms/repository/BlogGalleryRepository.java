package com.hkmci.csdkms.repository;


import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.entity.BlogGalleryDetail;


public interface BlogGalleryRepository extends JpaRepository<BlogGallery, Long> {

	//@Query("select bg from BlogGallery bg where bg.userId = ?1 and bg.isFinished = 0")
	Optional<BlogGallery> findByUserIdAndIsFinished(Long userId, int isFinished);
	
	Optional<BlogGallery> findByIsFinishedAndPostId(Integer isFinished, Long userId);
	
	@Modifying
	@Transactional
	@Query("update BlogGalleryDetail bgd set bgd.modifiedAt = ?2,"
			+ " modifiedBy = ?3,"
			+ " deletedAt = ?2,"
			+ " deletedBy = ?3,"
			+ " deleted = 1"
			+ " where id = ?1")
	Optional<BlogGalleryDetail> deleteDetailById(Long detailId, Date timeNow, Long userId);
	
}

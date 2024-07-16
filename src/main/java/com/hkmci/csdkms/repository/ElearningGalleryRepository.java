package com.hkmci.csdkms.repository;


import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningGallery;
import com.hkmci.csdkms.entity.ElearningGalleryDetail;


public interface ElearningGalleryRepository extends JpaRepository<ElearningGallery, Long> {

	//@Query("select bg from ElearningGallery bg where bg.userId = ?1 and bg.isFinished = 0")
	Optional<ElearningGallery> findByUserIdAndIsFinished(Long userId, int isFinished);
	
	Optional<ElearningGallery> findByIsFinishedAndPostId(Integer isFinished, Long userId);
	
	@Modifying
	@Transactional
	@Query("update ElearningGalleryDetail bgd set bgd.modifiedAt = ?2,"
			+ " modifiedBy = ?3,"
			+ " deletedAt = ?2,"
			+ " deletedBy = ?3,"
			+ " deleted = 1"
			+ " where id = ?1")
	Optional<ElearningGalleryDetail> deleteDetailById(Long detailId, Date timeNow, Long userId);
	
}

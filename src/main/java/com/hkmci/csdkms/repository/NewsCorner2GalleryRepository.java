package com.hkmci.csdkms.repository;


import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.NewsCorner2Gallery;
import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;


public interface NewsCorner2GalleryRepository extends JpaRepository<NewsCorner2Gallery, Long> {

	//@Query("select bg from NewsCorner2Gallery bg where bg.userId = ?1 and bg.isFinished = 0")
	Optional<NewsCorner2Gallery> findByUserIdAndIsFinished(Long userId, int isFinished);
	
	Optional<NewsCorner2Gallery> findByIsFinishedAndPostId(Integer isFinished, Long userId);
	
	@Modifying
	@Transactional
	@Query("update NewsCorner2GalleryDetail bgd set bgd.modifiedAt = ?2,"
	//@Query("update newscorner2_gallery_detail bgd set bgd.modifiedAt = ?2,"
			+ " modifiedBy = ?3,"
			+ " deletedAt = ?2,"
			+ " deletedBy = ?3,"
			+ " deleted = 1"
			+ " where id = ?1")
	Optional<NewsCorner2GalleryDetail> deleteDetailById(Long detailId, Date timeNow, Long userId);
	
}

package com.hkmci.csdkms.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;


public interface NewsCorner2GalleryDetailRepository extends JpaRepository<NewsCorner2GalleryDetail, Long> {

	//@Query("select bg from NewsCorner2Gallery bg where bg.userId = ?1 and bg.isFinished = 0")
	List<NewsCorner2GalleryDetail> findByGalleryIdAndDeleted(Long galleryId, Integer deleted);

	@Query(value = " select bgd.* from ( "
				 + " 	select * from newscorner2_gallery_detail where deleted = 0 order by created_at asc"
				 + " ) as bgd"
				 + " group by bgd.gallery_id", nativeQuery = true)
	List<NewsCorner2GalleryDetail> getAllThumb();
	
	
	
}

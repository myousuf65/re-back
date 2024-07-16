package com.hkmci.csdkms.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.BlogGalleryDetail;


public interface BlogGalleryDetailRepository extends JpaRepository<BlogGalleryDetail, Long> {

	//@Query("select bg from BlogGallery bg where bg.userId = ?1 and bg.isFinished = 0")
	List<BlogGalleryDetail> findByGalleryIdAndDeleted(Long galleryId, Integer deleted);

	@Query(value = " select bgd.* from ( "
				 + " 	select * from blog_gallery_detail where deleted = 0 order by created_at asc"
				 + " ) as bgd"
				 + " group by bgd.gallery_id", nativeQuery = true)
	List<BlogGalleryDetail> getAllThumb();
	
	
	
}

package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.ForumGalleryDetail;

public interface ForumGalleryDetailRepository extends JpaRepository<ForumGalleryDetail, Long> {

	
	List<ForumGalleryDetail> findByGalleryIdAndIsDeleted(Long galleryId, Integer deleted);

	
}

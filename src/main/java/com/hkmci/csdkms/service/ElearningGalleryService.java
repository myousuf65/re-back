package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.entity.ElearningGallery;
import com.hkmci.csdkms.entity.ElearningGalleryDetail;

public interface ElearningGalleryService {

	public List<ElearningGallery> findAll();
	
	public ElearningGallery newElearningGallery(ElearningGallery elearningGallery);
	
	public ElearningGallery updateElearningGallery(ElearningGallery elearningGallery);
	
	public Optional<ElearningGallery> findById(Long id);
	
	public Optional<ElearningGalleryDetail> findDetailById(Long detailId);
	
	public Optional<ElearningGallery> findActive(Long userId);
	
	public Optional<ElearningGallery> findByPostId(Long postId);
	
	public List<ElearningGalleryDetail> findByUserId(Long userId);
	
	public List<ElearningGalleryDetail> findByGalleryId(ElearningGallery galleryId);
	
	public ElearningGalleryDetail storeToUserFolder(MultipartFile file, Long galleryId);
	
	public Optional<ElearningGalleryDetail> findElearningGalleryDetail(Long detailId, Long userId);
	
	public ElearningGalleryDetail saveDetail(ElearningGalleryDetail detailId);
	
}

package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.entity.NewsCorner2Gallery;
import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;

public interface NewsCorner2GalleryService {

	public List<NewsCorner2Gallery> findAll();
	
	public NewsCorner2Gallery newNewsCorner2Gallery(NewsCorner2Gallery newscorner2Gallery);
	
	public NewsCorner2Gallery updateNewsCorner2Gallery(NewsCorner2Gallery newscorner2Gallery);
	
	public Optional<NewsCorner2Gallery> findById(Long id);
	
	public Optional<NewsCorner2GalleryDetail> findDetailById(Long detailId);
	
	public Optional<NewsCorner2Gallery> findActive(Long userId);
	
	public Optional<NewsCorner2Gallery> findByPostId(Long postId);
	
	public List<NewsCorner2GalleryDetail> findByUserId(Long userId);
	
	public List<NewsCorner2GalleryDetail> findByGalleryId(NewsCorner2Gallery galleryId);
	
	public NewsCorner2GalleryDetail storeToUserFolder(MultipartFile file, Long galleryId);
	
	public Optional<NewsCorner2GalleryDetail> findNewsCorner2GalleryDetail(Long detailId, Long userId);
	
	public NewsCorner2GalleryDetail saveDetail(NewsCorner2GalleryDetail detailId);
	
}

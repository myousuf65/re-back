package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.entity.BlogGalleryDetail;

public interface BlogGalleryService {

	public List<BlogGallery> findAll();
	
	public BlogGallery newBlogGallery(BlogGallery blogGallery);
	
	public BlogGallery updateBlogGallery(BlogGallery blogGallery);
	
	public Optional<BlogGallery> findById(Long id);
	
	public Optional<BlogGalleryDetail> findDetailById(Long detailId);
	
	public Optional<BlogGallery> findActive(Long userId);
	
	public Optional<BlogGallery> findByPostId(Long postId);
	
	public List<BlogGalleryDetail> findByUserId(Long userId);
	
	public List<BlogGalleryDetail> findByGalleryId(BlogGallery galleryId);
	
	public BlogGalleryDetail storeToUserFolder(MultipartFile file, Long galleryId);
	
	public Optional<BlogGalleryDetail> findBlogGalleryDetail(Long detailId, Long userId);
	
	public BlogGalleryDetail saveDetail(BlogGalleryDetail detailId);
	
}

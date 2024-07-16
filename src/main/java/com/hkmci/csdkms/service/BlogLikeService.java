package com.hkmci.csdkms.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.model.BlogLikeModel;

public interface BlogLikeService {
	public BlogLikeModel save (BlogLikeModel TheModel);
	
	public List<BlogLikeModel> findById(Long CreatedBy);
	
	public Path del (int id);

	public Path del (int pkId,int createdBy);

	public List<BlogLikeModel> findAll();
	
	public List<BlogLikeModel> findPost();
	
	public BlogGallery Post(int PkId);
	
	public Optional<BlogLikeModel> findPostLikeByUser(Long PkId, Long userId);
	
	void unlike(Long PkId, Long userId);
	
	void commentUnlike(Long pkId, Long createdBy);

	public Optional<BlogLikeModel> findCommentsLikeByUser(Long PkId, Long userId);
}

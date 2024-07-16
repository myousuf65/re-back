package com.hkmci.csdkms.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.NewsCorner2Gallery;
import com.hkmci.csdkms.model.NewsCorner2LikeModel;

public interface NewsCorner2LikeService {
	public NewsCorner2LikeModel save (NewsCorner2LikeModel TheModel);
	
	public List<NewsCorner2LikeModel> findById(Long CreatedBy);
	
	public Path del (int id);

	public Path del (int pkId,int createdBy);

	public List<NewsCorner2LikeModel> findAll();
	
	public List<NewsCorner2LikeModel> findPost();
	
	public NewsCorner2Gallery Post(int PkId);
	
	public Optional<NewsCorner2LikeModel> findPostLikeByUser(Long PkId, Long userId);
	
	void unlike(Long PkId, Long userId);
	
	void commentUnlike(Long pkId, Long createdBy);

	public Optional<NewsCorner2LikeModel> findCommentsLikeByUser(Long PkId, Long userId);
}

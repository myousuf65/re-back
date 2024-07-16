package com.hkmci.csdkms.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.model.BlogLikeModel;
import com.hkmci.csdkms.repository.BlogLikeRepository;


@Service
public class BlogLikeServiceImp implements BlogLikeService {

		private BlogLikeRepository blogLikeRepository;
		
		@Autowired
		public BlogLikeServiceImp(BlogLikeRepository theBlogLikeRepository) {
			blogLikeRepository = theBlogLikeRepository;
	
		}
		
		
		
		
	@Override
	public BlogLikeModel save(BlogLikeModel TheModel) {
		return blogLikeRepository.saveAndFlush(TheModel);

	}

	@Override
	public List<BlogLikeModel> findById(Long CreatedBy) {
		// TODO Auto-generated method stub
		return blogLikeRepository.findByCreatedByAndIsDeleted(CreatedBy,0);
	}

	@Override
	public Path del(int pkId,int createdBy) {
		// TODO Auto-generated method stub
		blogLikeRepository.delby(pkId,createdBy);
		return null;
	}




	@Override
	public List<BlogLikeModel> findAll() {
		// TODO Auto-generated method stub
		return blogLikeRepository.findAll();
	}





	@Override
	public BlogGallery Post(int PkId) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<BlogLikeModel> findPost() {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public Optional<BlogLikeModel> findPostLikeByUser(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		
		return blogLikeRepository.findByCreatedByAndPkidAndFuncIdAndIsDeleted(userId, PkId, 1,0);
	}
	
	@Override
	public Optional<BlogLikeModel> findCommentsLikeByUser(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		
		return blogLikeRepository.findByCreatedByAndPkidAndFuncIdAndIsDeleted(userId, PkId, 2,0);
	}




	@Override
	public void unlike(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		blogLikeRepository.unlike(PkId, userId);
		return;
		
	}
	
	@Override
	public void commentUnlike(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		blogLikeRepository.commentUnlike(PkId, userId);
		return;
		
	}




	@Override
	public Path del(int id) {
		// TODO Auto-generated method stub
		return null;
	}




	
}

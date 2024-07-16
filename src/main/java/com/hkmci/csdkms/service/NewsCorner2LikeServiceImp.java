package com.hkmci.csdkms.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.NewsCorner2Gallery;
import com.hkmci.csdkms.model.NewsCorner2LikeModel;
import com.hkmci.csdkms.repository.NewsCorner2LikeRepository;


@Service
public class NewsCorner2LikeServiceImp implements NewsCorner2LikeService {

		private NewsCorner2LikeRepository newscorner2LikeRepository;
		
		@Autowired
		public NewsCorner2LikeServiceImp(NewsCorner2LikeRepository theNewsCorner2LikeRepository) {
			newscorner2LikeRepository = theNewsCorner2LikeRepository;
	
		}
		
		
		
		
	@Override
	public NewsCorner2LikeModel save(NewsCorner2LikeModel TheModel) {
		return newscorner2LikeRepository.saveAndFlush(TheModel);

	}

	@Override
	public List<NewsCorner2LikeModel> findById(Long CreatedBy) {
		// TODO Auto-generated method stub
		return newscorner2LikeRepository.findByCreatedByAndIsDeleted(CreatedBy,0);
	}

	@Override
	public Path del(int pkId,int createdBy) {
		// TODO Auto-generated method stub
		newscorner2LikeRepository.delby(pkId,createdBy);
		return null;
	}




	@Override
	public List<NewsCorner2LikeModel> findAll() {
		// TODO Auto-generated method stub
		return newscorner2LikeRepository.findAll();
	}





	@Override
	public NewsCorner2Gallery Post(int PkId) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<NewsCorner2LikeModel> findPost() {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public Optional<NewsCorner2LikeModel> findPostLikeByUser(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		
		return newscorner2LikeRepository.findByCreatedByAndPkidAndFuncIdAndIsDeleted(userId, PkId, 1,0);
	}
	
	@Override
	public Optional<NewsCorner2LikeModel> findCommentsLikeByUser(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		
		return newscorner2LikeRepository.findByCreatedByAndPkidAndFuncIdAndIsDeleted(userId, PkId, 2,0);
	}




	@Override
	public void unlike(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		newscorner2LikeRepository.unlike(PkId, userId);
		return;
		
	}
	
	@Override
	public void commentUnlike(Long PkId, Long userId) {
		// TODO Auto-generated method stub
		newscorner2LikeRepository.commentUnlike(PkId, userId);
		return;
		
	}




	@Override
	public Path del(int id) {
		// TODO Auto-generated method stub
		return null;
	}




	
}

package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.NewsCorner2BookmarkModel;
import com.hkmci.csdkms.repository.NewsCorner2BookmarkRepository;


@Service
public class NewsCorner2BookmarkServiceImp implements NewsCorner2BookmarkService {

	private NewsCorner2BookmarkRepository newscorner2BookmarkRepository;
	
	@Autowired
	public NewsCorner2BookmarkServiceImp(NewsCorner2BookmarkRepository theNewsCorner2BookmarkRepository) {
		newscorner2BookmarkRepository = theNewsCorner2BookmarkRepository;
	}
	
	@Override
	public NewsCorner2BookmarkModel save(NewsCorner2BookmarkModel Model) {
		return newscorner2BookmarkRepository.saveAndFlush(Model);
			
	}

	@Override
	public List<NewsCorner2BookmarkModel> findById(Integer UserRef) {
		// TODO Auto-generated method stub
		return newscorner2BookmarkRepository.findByUserId(UserRef);
	}

	@Override
	public void del (Integer UserRefs, Integer PostId) {
		// TODO Auto-generated method stub
		newscorner2BookmarkRepository.delby(UserRefs,PostId);
		return;
	}

	@Override
	public List<NewsCorner2BookmarkModel> findAll() {
		// TODO Auto-generated method stub
		return newscorner2BookmarkRepository.findAll();
		
		
	}

	@Override
	public Optional<NewsCorner2BookmarkModel> findByPostAndUser(Integer PostId, Integer UserRefs) {
		// TODO Auto-generated method stub
		
		return newscorner2BookmarkRepository.findByPostIdAndUserRef(PostId, UserRefs);
	}

}

package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.BlogBookmarkModel;
import com.hkmci.csdkms.repository.BlogBookmarkRepository;


@Service
public class BlogBookmarkServiceImp implements BlogBookmarkService {

	private BlogBookmarkRepository blogBookmarkRepository;
	
	@Autowired
	public BlogBookmarkServiceImp(BlogBookmarkRepository theBlogBookmarkRepository) {
		blogBookmarkRepository = theBlogBookmarkRepository;
	}
	
	@Override
	public BlogBookmarkModel save(BlogBookmarkModel Model) {
		return blogBookmarkRepository.saveAndFlush(Model);
			
	}

	@Override
	public List<BlogBookmarkModel> findById(Integer UserRef) {
		// TODO Auto-generated method stub
		return blogBookmarkRepository.findByUserId(UserRef);
	}

	@Override
	public void del (Integer UserRefs, Integer PostId) {
		// TODO Auto-generated method stub
		blogBookmarkRepository.delby(UserRefs,PostId);
		return;
	}

	@Override
	public List<BlogBookmarkModel> findAll() {
		// TODO Auto-generated method stub
		return blogBookmarkRepository.findAll();
		
		
	}

	@Override
	public Optional<BlogBookmarkModel> findByPostAndUser(Integer PostId, Integer UserRefs) {
		// TODO Auto-generated method stub
		
		return blogBookmarkRepository.findByPostIdAndUserRef(PostId, UserRefs);
	}

}

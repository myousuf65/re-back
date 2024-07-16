package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.BlogCategory;
import com.hkmci.csdkms.repository.BlogCategoryRepository;




@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {

	@Autowired
	@Resource
	private BlogCategoryRepository blogCategoryRepository;
	
	
	
	@Override
	public List<BlogCategory> findAll() {
		return blogCategoryRepository.findByIsDeletedAndIsPublic(0, 1);
	}



	@Override
	public BlogCategory newBlogGallery(BlogCategory blogCategory) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BlogCategory updateBlogGallery(BlogCategory blogCategory) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Optional<BlogCategory> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	

	
}
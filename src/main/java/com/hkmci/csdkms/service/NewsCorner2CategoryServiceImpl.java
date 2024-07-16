package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.NewsCorner2Category;
import com.hkmci.csdkms.repository.NewsCorner2CategoryRepository;




@Service
public class NewsCorner2CategoryServiceImpl implements NewsCorner2CategoryService {

	@Autowired
	@Resource
	private NewsCorner2CategoryRepository newscorner2CategoryRepository;
	
	
	
	@Override
	public List<NewsCorner2Category> findAll() {
		return newscorner2CategoryRepository.findByIsDeletedAndIsPublic(0, 1);
	}



	@Override
	public NewsCorner2Category newNewsCorner2Gallery(NewsCorner2Category newscorner2Category) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public NewsCorner2Category updateNewsCorner2Gallery(NewsCorner2Category newscorner2Category) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Optional<NewsCorner2Category> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	

	
}
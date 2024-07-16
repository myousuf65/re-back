package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.NewsCorner2Category;

public interface NewsCorner2CategoryService {

	public List<NewsCorner2Category> findAll();
	
	public NewsCorner2Category newNewsCorner2Gallery(NewsCorner2Category newscorner2Category);
	
	public NewsCorner2Category updateNewsCorner2Gallery(NewsCorner2Category newscorner2Category);
	
	public Optional<NewsCorner2Category> findById(Long id);
	
}

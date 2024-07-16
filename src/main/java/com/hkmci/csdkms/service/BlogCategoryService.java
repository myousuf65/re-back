package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.BlogCategory;

public interface BlogCategoryService {

	public List<BlogCategory> findAll();
	
	public BlogCategory newBlogGallery(BlogCategory blogCategory);
	
	public BlogCategory updateBlogGallery(BlogCategory blogCategory);
	
	public Optional<BlogCategory> findById(Long id);
	
}

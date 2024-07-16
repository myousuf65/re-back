package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.model.BlogBookmarkModel;

public interface BlogBookmarkService {
	
	public BlogBookmarkModel save (BlogBookmarkModel TheModel);
	
	public List<BlogBookmarkModel> findById(Integer UserRef);
	
	public void del (Integer UserRefs, Integer PostId);
	
	public List<BlogBookmarkModel> findAll();
	
	public Optional<BlogBookmarkModel> findByPostAndUser(Integer PostId, Integer UserRefs);
	
}

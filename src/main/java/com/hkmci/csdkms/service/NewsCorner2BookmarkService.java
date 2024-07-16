package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.model.NewsCorner2BookmarkModel;

public interface NewsCorner2BookmarkService {
	
	public NewsCorner2BookmarkModel save (NewsCorner2BookmarkModel TheModel);
	
	public List<NewsCorner2BookmarkModel> findById(Integer UserRef);
	
	public void del (Integer UserRefs, Integer PostId);
	
	public List<NewsCorner2BookmarkModel> findAll();
	
	public Optional<NewsCorner2BookmarkModel> findByPostAndUser(Integer PostId, Integer UserRefs);
	
}

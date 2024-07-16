package com.hkmci.csdkms.service;

import java.util.List;

import com.hkmci.csdkms.entity.BlogGalleryDetail;

public interface BlogGalleryDetailService {

	public List<BlogGalleryDetail> findAll();
	
	public List<BlogGalleryDetail> getAllThumb();
	
}

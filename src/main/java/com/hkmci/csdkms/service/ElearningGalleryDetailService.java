package com.hkmci.csdkms.service;

import java.util.List;

import com.hkmci.csdkms.entity.ElearningGalleryDetail;

public interface ElearningGalleryDetailService {

	public List<ElearningGalleryDetail> findAll();
	
	public List<ElearningGalleryDetail> getAllThumb();
	
}

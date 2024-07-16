package com.hkmci.csdkms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.ElearningGalleryDetail;
import com.hkmci.csdkms.repository.ElearningGalleryDetailRepository;




@Service
public class ElearningGalleryDetailServiceImpl implements ElearningGalleryDetailService {

	
	@Autowired
	@Resource
	private ElearningGalleryDetailRepository elearningGalleryDetailRepository;

	@Override
	public List<ElearningGalleryDetail> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElearningGalleryDetail> getAllThumb() {
		List<ElearningGalleryDetail> return_data = elearningGalleryDetailRepository.getAllThumb();
		return return_data;
	}
	
	
	
}
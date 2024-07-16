package com.hkmci.csdkms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.repository.BlogGalleryDetailRepository;




@Service
public class BlogGalleryDetailServiceImpl implements BlogGalleryDetailService {

	
	@Autowired
	@Resource
	private BlogGalleryDetailRepository blogGalleryDetailRepository;

	@Override
	public List<BlogGalleryDetail> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BlogGalleryDetail> getAllThumb() {
		List<BlogGalleryDetail> return_data = blogGalleryDetailRepository.getAllThumb();
		return return_data;
	}
	
	
	
}
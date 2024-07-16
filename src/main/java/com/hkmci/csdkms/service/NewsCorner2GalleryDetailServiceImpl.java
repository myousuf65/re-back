package com.hkmci.csdkms.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;
import com.hkmci.csdkms.repository.NewsCorner2GalleryDetailRepository;




@Service
public class NewsCorner2GalleryDetailServiceImpl implements NewsCorner2GalleryDetailService {

	
	@Autowired
	@Resource
	private NewsCorner2GalleryDetailRepository newscorner2GalleryDetailRepository;

	@Override
	public List<NewsCorner2GalleryDetail> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NewsCorner2GalleryDetail> getAllThumb() {
		List<NewsCorner2GalleryDetail> return_data = newscorner2GalleryDetailRepository.getAllThumb();
		return return_data;
	}
	
	
	
}
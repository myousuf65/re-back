package com.hkmci.csdkms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.NewsCorner2Tag;
import com.hkmci.csdkms.repository.NewsCorner2TagRepository;


@Service
public class NewsCorner2TagServiceImp implements NewsCorner2TagService {

		private NewsCorner2TagRepository newscorner2TagRepository;
		
		@Autowired
		public NewsCorner2TagServiceImp(NewsCorner2TagRepository theNewsCorner2TagRepository) {
			newscorner2TagRepository = theNewsCorner2TagRepository;
	
		}
		
		
		
		
	@Override
	public NewsCorner2Tag save(NewsCorner2Tag newscorner2Tag) {
		return newscorner2TagRepository.saveAndFlush(newscorner2Tag);

	}


	
}

package com.hkmci.csdkms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.BlogTag;
import com.hkmci.csdkms.repository.BlogTagRepository;


@Service
public class BlogTagServiceImp implements BlogTagService {

		private BlogTagRepository blogTagRepository;
		
		@Autowired
		public BlogTagServiceImp(BlogTagRepository theBlogTagRepository) {
			blogTagRepository = theBlogTagRepository;
	
		}
		
		
		
		
	@Override
	public BlogTag save(BlogTag blogTag) {
		return blogTagRepository.saveAndFlush(blogTag);

	}


	
}

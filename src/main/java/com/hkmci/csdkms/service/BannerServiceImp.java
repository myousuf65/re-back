package com.hkmci.csdkms.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.Banner;
import com.hkmci.csdkms.repository.BannerRepository;



@Service
public class BannerServiceImp implements BannerService {
	
	private BannerRepository bannerRepository;
	
	
	@Autowired
	public BannerServiceImp(BannerRepository theBannerRepository) {
		bannerRepository = theBannerRepository;
	}
	
	@Override
	public Banner save(Banner TheModel) {
		// TODO Auto-generated method stub
		return bannerRepository.save(TheModel);
		
	}
	@Override 
	public void deleteBanner(Long bannerId,Long userId) {
		
		bannerRepository.deleteBanner(bannerId, new Date(), userId);
	}
	@Override
	public List<Banner> findTop(Integer access_channel ) {
		// TODO Auto-generated method stub
		

		 List<String> channel = new ArrayList<>() ;
		 if(String.valueOf(access_channel).equals("1")) {
	        	channel.add("0");
	        	channel.add("1");
	        	channel.add("2");
	        }else {
	        	channel.add("2"); 
	        	channel.add("4"); 
	        }
		return bannerRepository.findTop(channel);
	}

	@Override
	public List<Banner> findGeneral(Integer access_channel) {
		// TODO Auto-generated method stub'
		
		 List<String> channel = new ArrayList<>() ;
		 if(String.valueOf(access_channel).equals("1")) {
	        	channel.add("0");
	        	channel.add("1");
	        	channel.add("2");
	        }else {
	        	channel.add("2");
	        	channel.add("4");
	        }
		return bannerRepository.findGeneral(channel);
	}

	@Override
	public Optional<Banner> findById(Long Id) {
		// TODO Auto-generated method stub
		return bannerRepository.findById(Id);
	}

	@Override
	public Path del(int Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Banner> findAll(Integer level) {
		// TODO Auto-generated method stub
		return bannerRepository.findAll(level.toString());
	}

}

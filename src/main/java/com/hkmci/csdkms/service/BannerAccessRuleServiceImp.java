package com.hkmci.csdkms.service;

import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.BannerAccessRule;
import com.hkmci.csdkms.repository.BannerAccessRuleRepository;

@Service
public class BannerAccessRuleServiceImp implements BannerAccessRuleService {
	
	
	private BannerAccessRuleRepository bannerAccessRuleRepository;

	@Override
	public BannerAccessRule save(BannerAccessRule TheModel) {
		// TODO Auto-generated method stub
		return bannerAccessRuleRepository.saveAndFlush(TheModel);
	}
}

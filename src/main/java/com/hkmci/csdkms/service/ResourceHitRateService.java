package com.hkmci.csdkms.service;

import com.hkmci.csdkms.entity.ResourceHitRate;

public interface ResourceHitRateService {

	
	public void countAndSaveResourceHitRate();
	
	public Integer CountResourceHitRate(Long resourceId);
	
	public ResourceHitRate updateResourceHitRate(Integer hitRate, Long resourceId);
}

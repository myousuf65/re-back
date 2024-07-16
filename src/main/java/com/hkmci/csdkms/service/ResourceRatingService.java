package com.hkmci.csdkms.service;

import org.springframework.stereotype.Service;

@Service
public interface ResourceRatingService {

	float userRatingResource(Long userId, Long resourceId, int rating);

	float AverageResourceRate(Long resourceId);

	Integer ResourceRate(Long resourceId, Long userId);

}

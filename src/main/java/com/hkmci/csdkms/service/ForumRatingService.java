package com.hkmci.csdkms.service;

public interface ForumRatingService {

	float userRatingResource(Long userId, Long resourceId, int rating);

	float AverageResourceRate(Long resourceId);

	Integer ForumRate(Long resourceId, Long userId);

}

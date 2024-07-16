package com.hkmci.csdkms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hkmci.csdkms.entity.ResourceRating;
import com.hkmci.csdkms.repository.ResourceRatingRepository;

@Service
public class ResourceRatingServiceImp implements ResourceRatingService {
	
	@Autowired
	private ResourceRatingRepository resourceRatingRepository;
	
	
	@Override
	public Integer ResourceRate(Long resourceId , Long userId) {
		Integer rate = resourceRatingRepository.ResourceRating(resourceId, userId);
		if ( rate == null ) {
			rate = 0;
		}
		return rate;
	}
	
	@Override 
	public float AverageResourceRate(Long resourceId) {

		Object[] data = (Object[]) resourceRatingRepository.CountRating(resourceId);
		System.out.println(" Resource Rating = "+data[0].toString() );
		if (data[0].toString()== "0") {
			return 0;
		} else {
			Integer userCount = Integer.valueOf(data[0].toString());
			Integer totalRating = Integer.valueOf(data[1].toString());
		
			Float averageRating = (float) totalRating/userCount;
			Float returnRating = BigDecimal.valueOf(averageRating).setScale(2,RoundingMode.HALF_UP).floatValue();
			return returnRating;
		}
	}
	@Override
	public float userRatingResource(Long userId, Long resourceId, int rating) {
//	 DecimalFormat   fnum   =   new   DecimalFormat("##0.0");  
		ResourceRating user_rating = resourceRatingRepository.RatingByUser(userId, resourceId);
		ResourceRating new_rating = new ResourceRating();
		new_rating.setCreatedAt(new Date());
		new_rating.setCreatedBy(userId);
		new_rating.setIsDeleted(0);
		new_rating.setResourceId(resourceId);
		new_rating.setRating(rating);
		
		if (user_rating == null) {
			resourceRatingRepository.saveAndFlush(new_rating);
		} else {
			user_rating.setIsDeleted(1);
			resourceRatingRepository.saveAndFlush(new_rating);
		}
		
		Object[] data = (Object[]) resourceRatingRepository.CountRating(resourceId);
		Integer userCount = Integer.valueOf(data[0].toString());
		Integer totalRating = Integer.valueOf(data[1].toString());
		
		Float averageRating = (float) totalRating/userCount;
		Float returnRating = BigDecimal.valueOf(averageRating).setScale(2,RoundingMode.HALF_UP).floatValue();
	
		return returnRating;
		
	}

}

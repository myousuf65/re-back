package com.hkmci.csdkms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.ForumRating;
import com.hkmci.csdkms.entity.ResourceRating;
import com.hkmci.csdkms.repository.ForumRatingRepository;


@Service
public class ForumRatingServiceImp implements ForumRatingService {
	@Autowired
	private ForumRatingRepository forumRatingRepository;
	
	@Override
	public Integer ForumRate(Long resourceId , Long userId) {
		Integer rate = forumRatingRepository.ForumRating(resourceId, userId);
		if ( rate == null ) {
			rate = 0;
		}
		return rate;
	}
	

	
	

	@Override 
	public float AverageResourceRate(Long resourceId) {

		Object[] data = (Object[]) forumRatingRepository.CountRating(resourceId);
		System.out.println(" Forum Rating = "+data[0].toString() );
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
		ForumRating user_rating = forumRatingRepository.RatingByUser(userId, resourceId);
		ForumRating new_rating = new ForumRating();
		new_rating.setCreatedAt(new Date());
		new_rating.setCreatedBy(userId);
		new_rating.setIsDeleted(0);
		new_rating.setPostId(resourceId);
		new_rating.setRating(rating);
		
		if (user_rating == null) {
			forumRatingRepository.saveAndFlush(new_rating);
		} else {
			user_rating.setIsDeleted(1);
			forumRatingRepository.saveAndFlush(new_rating);
		}
		
		Object[] data = (Object[]) forumRatingRepository.CountRating(resourceId);
		Integer userCount = Integer.valueOf(data[0].toString());
		Integer totalRating = Integer.valueOf(data[1].toString());
		
		Float averageRating = (float) totalRating/userCount;
		Float returnRating = BigDecimal.valueOf(averageRating).setScale(2,RoundingMode.HALF_UP).floatValue();
	
		return returnRating;
		
	}

}

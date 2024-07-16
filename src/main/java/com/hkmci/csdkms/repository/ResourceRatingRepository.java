package com.hkmci.csdkms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ResourceRating;

public interface ResourceRatingRepository extends JpaRepository<ResourceRating, Long> {
	
	@Query(value = "select rr.* from csdkms.resource_rating rr where rr.created_by = ?1 and rr.resource_id = ?2 and rr.is_deleted = 0 ", nativeQuery = true)
	ResourceRating RatingByUser(Long userId, Long resourceId);
	
	@Query(value = "select count(rr.created_by) as users, sum(rr.rating) as total_rating from csdkms.resource_rating rr "
			+ " where rr.resource_id = ?1 and rr.is_deleted = 0", nativeQuery = true)
	Object CountRating (Long resourceId);

	@Query(value = "select rr.rating from csdkms.resource_rating rr where rr.is_deleted = 0 and rr.resource_id = ?1 and rr.created_by = ?2 ", nativeQuery = true)
	Integer ResourceRating(Long resourceId ,Long userId);
}

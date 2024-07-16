package com.hkmci.csdkms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ForumRating;
import com.hkmci.csdkms.entity.ResourceRating;

public interface ForumRatingRepository extends JpaRepository<ForumRating, Long> {
	
	
	
	@Query(value = "select fr.* from csdkms.forum_rating fr where fr.created_by = ?1 and fr.post_id = ?2 and fr.is_deleted = 0 ", nativeQuery = true)
	ForumRating RatingByUser(Long userId, Long postId);
	
	@Query(value = "select count(rr.created_by) as users, sum(rr.rating) as total_rating from csdkms.forum_rating rr "
			+ " where rr.post_id = ?1 and rr.is_deleted = 0", nativeQuery = true)
	Object CountRating (Long resourceId);

	@Query(value = "select rr.rating from csdkms.forum_rating rr where rr.is_deleted = 0 and rr.post_id = ?1 and rr.created_by = ?2 ", nativeQuery = true)
	Integer ForumRating(Long resourceId ,Long userId);

	void saveAndFlush(ResourceRating new_rating);

}

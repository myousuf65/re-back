package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ForumGallery;

public interface ForumGalleryRepository extends JpaRepository<ForumGallery,Long>{
	
	
	
	
	 	@Query("Select f from ForumGallery f where f.createdBy =?1 and f.isFinished =?2 ")
		Optional<ForumGallery> SearchByUserIdAndIsFinished(Long userId, int isFinished);
	 	
	 	@Query("Select f from ForumGallery f where f.postId =?1 ")
	 	Optional<ForumGallery> SearchByPostId(Long postId);
	 	
	 	
	 	
}

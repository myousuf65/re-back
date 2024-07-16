package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.NewsCorner2Tag;

public interface NewsCorner2TagRepository extends JpaRepository<NewsCorner2Tag, Long> {
	
	@Query("select bl from NewsCorner2LikeModel bl where bl.createdBy= ?1")
	Optional<NewsCorner2Tag> findById(Long CreatedBy);
	
	
	@Query("select bt  from NewsCorner2Tag bt  where bt.postId = ?1 ")
	List<NewsCorner2Tag> getTagWithNewsCorner2Id(Long newscorner2Id);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "INSERT INTO newscorner2_post_tag (tag,post_id,is_deleted)" + 
			"VALUES (?1, ?2, 0)" , nativeQuery = true)
	public void createNewsCorner2Tag(String spetag, Long postId, Long userId);

}

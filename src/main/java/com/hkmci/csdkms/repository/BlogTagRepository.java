package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.BlogTag;

public interface BlogTagRepository extends JpaRepository<BlogTag, Long> {
	
	@Query("select bl from BlogLikeModel bl where bl.createdBy= ?1")
	Optional<BlogTag> findById(Long CreatedBy);
	
	
	@Query("select bt  from BlogTag bt  where bt.postId = ?1 ")
	List<BlogTag> getTagWithBlogId(Long blogId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "INSERT INTO blog_post_tag (tag,post_id,is_deleted)" + 
			"VALUES (?1, ?2, 0)" , nativeQuery = true)
	public void createBlogTag(String spetag, Long postId, Long userId);

}

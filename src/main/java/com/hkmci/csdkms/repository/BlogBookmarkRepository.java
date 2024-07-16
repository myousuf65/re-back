package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.BlogBookmarkModel;

public interface BlogBookmarkRepository extends JpaRepository<BlogBookmarkModel, Integer> {

	@Query("select bm from BlogBookmarkModel bm where bm.userRef= ?1")
	List<BlogBookmarkModel> findByUserId(Integer UserRef);
	
//	@Query("Delete from BlogBookmarkModel bm where bm.userRef= ?1 and bm.postId = ?2")
//	 Path delby(int UserRefs, int PostId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update BlogBookmarkModel bm set bm.isDeleted = 1"
			+ " where bm.postId = ?1"
			+ " and bm.userRef = ?2")
	void delby(Integer postId, Integer userId);
	
	Optional<BlogBookmarkModel> findByPostIdAndUserRef(Integer postId, Integer userId);

	@Query("select bm from BlogBookmarkModel bm where bm.userRef= ?1")
	List<BlogBookmarkModel> findById(int UserRef);
	
//	@Modifying
//	@Transactional
//	@Query("Delete from BlogBookmarkModel bm where bm.userRef= ?1 and bm.postId = ?2")
//	void delby(int UserRefs, int PostId);
}

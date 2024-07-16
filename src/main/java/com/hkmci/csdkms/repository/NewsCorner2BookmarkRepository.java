package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.NewsCorner2BookmarkModel;

public interface NewsCorner2BookmarkRepository extends JpaRepository<NewsCorner2BookmarkModel, Integer> {

	@Query("select bm from NewsCorner2BookmarkModel bm where bm.userRef= ?1")
	List<NewsCorner2BookmarkModel> findByUserId(Integer UserRef);
	
//	@Query("Delete from NewsCorner2BookmarkModel bm where bm.userRef= ?1 and bm.postId = ?2")
//	 Path delby(int UserRefs, int PostId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update NewsCorner2BookmarkModel bm set bm.isDeleted = 1"
			+ " where bm.postId = ?1"
			+ " and bm.userRef = ?2")
	void delby(Integer postId, Integer userId);
	
	Optional<NewsCorner2BookmarkModel> findByPostIdAndUserRef(Integer postId, Integer userId);

	@Query("select bm from NewsCorner2BookmarkModel bm where bm.userRef= ?1")
	List<NewsCorner2BookmarkModel> findById(int UserRef);
	
//	@Modifying
//	@Transactional
//	@Query("Delete from NewsCorner2BookmarkModel bm where bm.userRef= ?1 and bm.postId = ?2")
//	void delby(int UserRefs, int PostId);
}

package com.hkmci.csdkms.repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.NewsCorner2LikeModel;

public interface NewsCorner2LikeRepository extends JpaRepository<NewsCorner2LikeModel, Long> {

	
	//@Query("select bl from NewsCorner2LikeModel bl where bl.createdBy= ?1 and isDeleted = 0")
	List<NewsCorner2LikeModel> findByCreatedByAndIsDeleted(Long CreatedBy, Integer isDeleted);
	
	@Query("select bl from NewsCorner2LikeModel bl where bl.createdBy= ?1")
	Optional<NewsCorner2LikeModel> findById(Long CreatedBy);
	
	@Modifying
	@Transactional
	@Query("Delete from NewsCorner2LikeModel bl where bl.pkid= ?1 and bl.createdBy= ?2")
	Path delby(int pkId,int createdBy);
	
	Optional<NewsCorner2LikeModel> findByCreatedByAndPkidAndFuncIdAndIsDeleted(Long userId, Long PkId, Integer FuncId, Integer isDeleted);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update NewsCorner2LikeModel bl set bl.isDeleted = 1"
			+ " where bl.createdBy = ?2"
			+ " and bl.pkid = ?1"
			+ " and bl.funcId = 1")
	void unlike(Long PkId, Long userId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update NewsCorner2LikeModel bl set bl.isDeleted = 1"
			+ " where bl.createdBy = ?2"
			+ " and bl.pkid = ?1"
			+ " and bl.funcId = 2")
	void commentUnlike(Long pkId, Long userId);
}

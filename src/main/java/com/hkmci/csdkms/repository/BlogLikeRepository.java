package com.hkmci.csdkms.repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.BlogLikeModel;

public interface BlogLikeRepository extends JpaRepository<BlogLikeModel, Long> {

	
	//@Query("select bl from BlogLikeModel bl where bl.createdBy= ?1 and isDeleted = 0")
	List<BlogLikeModel> findByCreatedByAndIsDeleted(Long CreatedBy, Integer isDeleted);
	
	@Query("select bl from BlogLikeModel bl where bl.createdBy= ?1")
	Optional<BlogLikeModel> findById(Long CreatedBy);
	
	@Modifying
	@Transactional
	@Query("Delete from BlogLikeModel bl where bl.pkid= ?1 and bl.createdBy= ?2")
	Path delby(int pkId,int createdBy);
	
	Optional<BlogLikeModel> findByCreatedByAndPkidAndFuncIdAndIsDeleted(Long userId, Long PkId, Integer FuncId, Integer isDeleted);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update BlogLikeModel bl set bl.isDeleted = 1"
			+ " where bl.createdBy = ?2"
			+ " and bl.pkid = ?1"
			+ " and bl.funcId = 1")
	void unlike(Long PkId, Long userId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update BlogLikeModel bl set bl.isDeleted = 1"
			+ " where bl.createdBy = ?2"
			+ " and bl.pkid = ?1"
			+ " and bl.funcId = 2")
	void commentUnlike(Long pkId, Long userId);
}

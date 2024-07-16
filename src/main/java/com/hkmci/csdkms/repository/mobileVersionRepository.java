package com.hkmci.csdkms.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.mobileVersion;

public interface mobileVersionRepository extends JpaRepository<mobileVersion, Long> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value ="update csdkms.mobile_version set is_deleted =1 , "
			+ "deleted_by = ?1 , deleted_at = ?2 "
			+ "where app = ?3 ",nativeQuery = true)
	void deletedmobileversion( Long userId, Date nowdate, String app);
	
	
	@Query("Select mv.version from mobileVersion mv  "
			+ "where mv.app =?1 and mv.isDeleted =0 ")
	String findAppVerion(String app);
	

	
	
	
}

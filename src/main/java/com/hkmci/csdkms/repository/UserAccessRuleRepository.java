package com.hkmci.csdkms.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.UserAccessRule;

/**
 * author: Holfer
 * date: 2019/03/21
 * time: 5:54
 * version: 1.0
 * description:
 */
public interface UserAccessRuleRepository extends JpaRepository<UserAccessRule, Long> {
	

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update UserAccessRule uar set uar.isDeleted = 1"
			+ " where uar.userId = ?1")
	void deleteResourceAccessRule(Long user_id);
	
	@Query("select uar.accessRuleId from UserAccessRule uar"
			+ " where uar.userId = ?1 and uar.isDeleted = 0")
	List<Long> getByUserId(Long user_id);
	
	
	
	
	@Query("select uar from UserAccessRule uar"
			+ " where uar.userId = ?1 and uar.isDeleted = 0")
	List<UserAccessRule> getByUserId2(Long user_id);

}

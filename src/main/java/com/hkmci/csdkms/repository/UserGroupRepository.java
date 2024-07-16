package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.UserGroup;

/**
 * author: Holfer
 * date: 2019/03/21
 * time: 5:54
 * version: 1.0
 * description:
 */
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
	
	
	@Query("Select u from UserGroup u where isDeleted =0 order by order")
	List<UserGroup> getAll();
	



}

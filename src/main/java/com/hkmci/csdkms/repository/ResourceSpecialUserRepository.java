package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ResourceSpecialUser;

/**
 * author: Holfer
 * date: 2019/03/21
 * time: 5:54
 * version: 1.0
 * description:
 */
public interface ResourceSpecialUserRepository extends JpaRepository<ResourceSpecialUser, Long> {

	@Query("Select rsu from ResourceSpecialUser rsu "
			+ " where rsu.resourceId in ?1 and rsu.deletedBy = 0")
	List<ResourceSpecialUser> getByResourceIds(List<Long> resource_ids);
	
	@Query("Select rsu from ResourceSpecialUser rsu "
			+ " where rsu.resourceId in ?1 ")
	List<ResourceSpecialUser> getAllByResourceIds(List<Long> resource_ids);
//	@Query("select u from user u where deleted = 0")

	@Query("Select rsu.staffNo from ResourceSpecialUser rsu where rsu.resourceId = ?1 and rsu.deletedBy = 0")
	List<String> getUserIdByResourceIds(Long resource_ids);


}

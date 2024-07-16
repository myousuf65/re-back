package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ResourceSpeicalGroup;

public interface ResourceSpecialGroupRepository extends JpaRepository<ResourceSpeicalGroup, Long> {

	
	@Query("Select rsg from ResourceSpeicalGroup rsg where rsg.resourceId in ?1 ")
	List<ResourceSpeicalGroup> getAllByResourceIds(List<Long> resource_ids);
	
	@Query("Select rsg from ResourceSpeicalGroup rsg  "
			+ " where rsg.resourceId in ?1 and rsg.isDeleted = 0 ")
	List<ResourceSpeicalGroup> getByResourceIds(List<Long> resource_ids);
	
	
	
	
	@Query(value ="select ug.user_id from csdkms.resource_special_group rsg "
			+ " left join csdkms.special_user_group_user ug "
			+ " on rsg.special_group_id = ug.special_user_group_id "
			+ " where (ug.is_deleted =0 or ug.is_deleted is null)  and rsg.is_deleted =0 ",nativeQuery = true)
	List<String> getUserIdbyResourceId(Long resource_ids);
}

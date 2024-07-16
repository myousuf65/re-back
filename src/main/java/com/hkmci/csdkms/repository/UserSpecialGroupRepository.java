package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.SpecialUserGroupUserModel;

public interface UserSpecialGroupRepository extends JpaRepository<SpecialUserGroupUserModel, Long> {

	
	@Query(value="Select user_id from special_user_group_user suu "
			+ " where suu.special_user_group_id = ?1 and is_deleted= 0 ",nativeQuery = true)
	List<String> findStaffNo(Long groupId);
	
	
	
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value =" update special_user_group_user suu "
			+ " set suu.is_deleted = 1 , suu.deleted_by = ?3 , suu.deleted_at =?4 where"
			+ " suu.special_user_group_id  = ?1 and suu.user_id in ?2 and suu.is_deleted = 0 ", nativeQuery = true)
	void deleteSpecialUser(Long groupId, List<String> specialUser_delete, Long userId, Date deleteAt);
	
}

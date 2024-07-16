package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.PinUser;

public interface PinUserRepository extends JpaRepository<PinUser, Long> {
	
	@Query(value="select userId from PinUser where pinId = ?1 and isDeleted=0")
	List<Long> getnUserId(Long pinId);

	@Query(value="select userId from  PinUser where pinId= ?1 and isDeleted=1")
	List<Long> getDeletedUserId(Long pinId);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update csdkms.pin_user set is_deleted = 1 , modified_at =?1 , modified_by =?2 where pin_id =?3 and user_id in ?4 "
	,nativeQuery = true)
	void deleteUser(Date deletedAt, Long deletedBy, Long PinId, List<Long> userId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update csdkms.pin_user set is_deleted = 0 , deleted_at =?1 , deleted_by =?2 where pin_id =?3 and user_id = ?4 "
	,nativeQuery = true)
	void updatedeleteUser(Date deletedAt, Long deletedBy, Long PinId, Long userId);
}
package com.hkmci.csdkms.repository;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.Pin;

public interface PinRepository extends JpaRepository<Pin, Long> {

	
	@Query(value="select * from csdkms.pin where is_deleted=0", nativeQuery = true)
	List<Pin> getAllPin();
	
	@Query(value= " select * from csdkms.pin where id in (" + 
			"select pin_id from csdkms.pin_user where user_id =?1 and is_deleted =0) and is_deleted =0", nativeQuery = true)
	List<Pin> getDisplayPin(Long userId);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update csdkms.pin set is_deleted = 1 ,deleted_at =?1 , deleted_by =?2 where id =?3 "
	,nativeQuery = true)
	void deletePin(Date deletedAt, Long deletedBy, Long PinId);

}

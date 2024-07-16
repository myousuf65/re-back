package com.hkmci.csdkms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
	
	@Query(value ="select up.* from csdkms.user_profile up where up.created_by = ?1 and up.type = ?2 and up.is_deleted =0 " , nativeQuery = true)
	UserProfile findExistProfile (Long userId,Integer type );
}

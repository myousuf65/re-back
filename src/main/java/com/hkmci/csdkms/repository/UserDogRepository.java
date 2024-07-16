package com.hkmci.csdkms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.UserDog;

public interface UserDogRepository extends JpaRepository<UserDog, Long> {

	
	@Query(value="select user_dog from user_dog where created_by= ?1 ", nativeQuery = true)
	public String FindUserDog(Long userId);

	
	@Query(value="select * from user_dog where created_by = ?1" , nativeQuery = true)
	public UserDog findByUserId(Long userId);
	
}

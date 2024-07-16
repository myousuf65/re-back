package com.hkmci.csdkms.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ResourceHitRate;

public interface ReosurceHitRateRepository extends JpaRepository<ResourceHitRate, Long> {

	@Query(value ="select rhr.* from resource_hit_rate rhr where rhr.resource_id = ?1 " ,nativeQuery = true)
	ResourceHitRate findByResourceId(Long resourceId);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value ="UPDATE csdkms.resource_hit_rate set hit_rate =?1 where resource_id =?2 ", nativeQuery = true)
	void updateHitRate(Integer hitRate, Long resourceId);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query (value="INSERT INTO csdkms.resource_hit_rate ( hit_rate , resource_id ) value (?1, ?2 )", nativeQuery = true)
	public void insertHitRate(Integer hitRate, Long resourceId);
}

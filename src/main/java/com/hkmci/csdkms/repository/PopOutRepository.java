package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.PopOut;

public interface PopOutRepository extends JpaRepository<PopOut, Long> {
	
	
	@Query(value="select po.* from csdkms.pop_out po where po.is_deleted=0  ", nativeQuery =true)
	List<PopOut> getPopOut();
	
	@Query(value="select po.* from csdkms.pop_out po where po.is_deleted=0 and po.access_channel in ?1  ", nativeQuery =true)
	List<PopOut> getPopOutHome(List<String> access_channel);
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update csdkms.pop_out set is_deleted = 1  where id =?1 "
	,nativeQuery = true)
	void deletePopOut(Long PopOut);

}

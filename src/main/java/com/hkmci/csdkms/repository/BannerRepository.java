package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long>{
	
	@Query("select b from Banner b where b.level =1  and b.accessChannel in ?1  and b.isDeleted = 0 order by b.orderby")
	List<Banner> findTop(List<String> access_channel);
	
	@Query("select b from Banner b where b.level =2  and b.accessChannel in ?1  and b.isDeleted = 0 order by b.orderby")
	List<Banner> findGeneral(List<String> access_channel);
	
	
	
	@Query("select b from Banner b where b.level = ?1  and b.isDeleted = 0 order by b.orderby")
	List<Banner> findAll(String level);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Banner b set b.isDeleted = 1 , b.deletedAt = ?2 ,b.deletedBy =  ?3 "
			+ " where b.id = ?1 and b.isDeleted = 0 ")
	void deleteBanner(Long bannerId, Date deletedAt, Long deletedBy);
	
}

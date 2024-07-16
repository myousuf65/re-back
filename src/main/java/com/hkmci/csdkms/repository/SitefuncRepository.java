package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.SitefuncsModel;

public interface SitefuncRepository extends JpaRepository<SitefuncsModel, Integer> {
	
	List<SitefuncsModel> findAllById(Long Id);

	@Query("Select s from SitefuncsModel s"
			+ " Left join ACLModel a"
			+ " on s.id = a.sitefuncId"
			+ " where  a.usergroupId =?1 and s.isDeleted =0"
			+ " and ( a.flagAll =1 or a.flagAdd =1 or a.flagSv =1" 
			+ " or a.flagUpd =1 or a.flagDel =1 or a.flagDownload =1)   "
			+ " order by s.sortOrder ")
	List<SitefuncsModel> siteMenu(Long groupId );

	@Query("Select s from SitefuncsModel s"
			+ " where  s.isDeleted =0 and (s.id =10 or s.id=6)"
			+ " order by s.sortOrder ")
	List<SitefuncsModel> siteMenuAll( );
	
}

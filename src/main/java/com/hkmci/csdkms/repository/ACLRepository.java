package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.ACLModel;

public interface ACLRepository extends JpaRepository<ACLModel,Integer> {
	
    @Query(value="Select sf.* from usergroup_sitefunction as sf Left join sitefunction as s "+
    			 "on sf.sitefunc_id = s.id where s.is_deleted = 0 and sf.usergroup_id =?1 "+ 
    			 "order by s.sort_order", nativeQuery = true)
	List<ACLModel> findByusergroupId(Long usergroupId);
	
	@Query(value = "Select sitefunc_id from usergroup_sitefunction where usergroup_id =?1 and "
			+ "(flag_all =1 or(flag_add=1 or flag_del =1 or flag_sv =1 or flag_download =1 or " + 
			"flag_search =1 or flag_upd =1)) ", nativeQuery = true)
	List<Long> checkAccess(Long groupId);

	
	@Query("Select a from ACLModel a")
	ACLModel getById(int id);
	
}
 


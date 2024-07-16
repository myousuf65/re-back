package com.hkmci.csdkms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.AccessRule;


@Service
public interface AccessRuleService {
	
	List<AccessRule> findAll();
	
	Optional<AccessRule> getById(Long Id);
	
	List<AccessRule> test(Long id);
	
	List<Object[]> customeized_test(Long id);
	
	AccessRule deleteById(Long id);
	
	List<Long> specialAccessRule(Long userId);
	
	AccessRule save(AccessRule CollectionAccessRule);
	
	AccessRule update(AccessRule CollectionAccessRule);
	
	List<Long> getIdByUser(Long sectionId, Long instId, Long rankId);
	
//	List<CollectionAccessRule>  Search(Long Id, Date startdate, Date enddate);
	List<AccessRule>  Search(Long Id, Date startdate, Date enddate,Long km,Long ks,Long wg, Long page,List<Long> user_access_rule, Integer is_admin, String description);

	void deleteAR(List<Long> deleted_ids, Long deleted_by);

	List<AccessRule> getAllWithRsource(List<Long> deleted_ids);

	Integer getTotal(Long id, Date startDate, Date endDate, Long km, Long ks, Long wg, Long page,
			List<Long> accessRuleId, Integer is_admin, String description);

	List<AccessRule> getIdByUserId(Long section, Long institution, Long rank, Long user_id);

}
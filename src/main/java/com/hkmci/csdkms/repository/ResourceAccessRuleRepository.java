package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ResourceAccessRule;

public interface ResourceAccessRuleRepository extends JpaRepository<ResourceAccessRule, Long> {
	
	ResourceAccessRule findFirstByResourceOrderByIdDesc(Long resourceId);

	@Query("select rar from ResourceAccessRule rar where rar.resource = ?1 and rar.deletedBy = 0")
	List<ResourceAccessRule> findByResourceId(Long id);


	@Query("select rar from FileResource r "
			+ " inner join ResourceAccessRule rar on rar.resource = r.id "
			+ " where rar.resource in ?1 and rar.deletedBy = 0")
	List<ResourceAccessRule> getAccessRuleByResourceIds(List<Long> resourceIds);

}

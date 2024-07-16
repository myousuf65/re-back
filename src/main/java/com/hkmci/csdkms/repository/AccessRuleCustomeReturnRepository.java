package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;



@NoRepositoryBean
public interface AccessRuleCustomeReturnRepository extends JpaRepository<Object, Long> {
	
	//List<AccessRuleReturn> findByIsDeleted(int isDeleted);
	@Query(value = "select ar.id, ar.description from AccessRule ar where ar.id = :id", nativeQuery = true)
	List<Object[]> getTestDataById(@Param("id") Long id);
	
	@Query("select ar.id，ar.description from AccessRule ar where ar.id = ?1")
	Optional<List<Object[]>> getCustomFieldById(Long Id);

}
package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.AccessRule;


public interface AccessRuleRepository extends JpaRepository<AccessRule, Long> {
	
	List<AccessRule> findByIsDeleted(int isDeleted);
	
	@Query("select ar from AccessRule ar where ar.id = ?1 and ar.isDeleted = 0")
	Optional<AccessRule> getById(Long Id);
	
	AccessRule findByIdAndIsDeleted(Long id, int isDeleted);
	
	@Query("select ar from AccessRule ar"
			+ " where ( Locate( ?1 ,ar.sectionId) > 0 or ar.sectionId = 0)"
			+ " and (Locate( ?2 ,ar.instId) > 0 or (ar.instId = 0 and ar.areaId = 3))"
			+ " and ( Locate( ?3 ,ar.rankId) > 0  or ar.rankId = 0)")
	List<AccessRule> getIdByUser(Long sectionId, Integer instId, Long rankId);
	
	@Query("select ar.id from AccessRule ar"
			+ " left join ResourceAccessRule rar on rar.accessRule = ar.id"
			+ " where rar.resource = ?1 ")
	Long getIdByResourceId(Long resourceId);
	
	@Query("select ar from AccessRule ar"
			+ " where ar.id in ?1 and ar.isDeleted = 0")
	List<AccessRule> getByIds(List<Long> accessRuleId);
	
	
	@Query(value = "select ar.* from access_rule ar "
			+ " where ar.is_deleted = 0 "
			+ " and (ar.id = ?1 or ?1 = 0)"
			+ " and (ar.id in ?9 or ?10 = 1)" //and ar.id in ?1 or 0 = 0 and ar.id in ?1 or  < and (u.id in ?1 or ?11 = 1) >
			+ " and (ar.created_at > ?2 or ?2 IS NULL)"
			+ " and (ar.created_at < ?3 or ?3 IS NULL)"
//			+ " and ((ar.area_id = ?4 or ar.area_id = ?5 or ar.area_id = ?6)"
//			+ " 	or ( ?4 = -1 and ?5 = -1 and ?6 = -1))"
			+ " and (ar.area_id = ?4 or ar.area_id = ?5 or ar.area_id = ?6"
			+ " 	or  ?4 = -1 and ?5 = -1 and ?6 = -1)"
			+ " and Locate(?11,ar.desc_en) > 0"
			+ " order by ar.created_at desc"
			+ " limit ?7 , ?8",
			nativeQuery = true)
	List<AccessRule> searchFunction(Long id,Date startDate, Date endDate,Long km,Long ks,Long wg,Integer start_num, Integer end_num,
									List<Long> user_access_rule, Integer is_admin, String description);

	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update AccessRule ar set "
			+ " ar.isDeleted = 1,"
			+ " ar.deletedBy = ?2,"
			+ " ar.deletedAt = ?3"
			+ " where ar.isDeleted = 0"
			+ " and ar.id in ?1")
	void deleteAR(List<Long> deleted_ids, Long deleted_by, Date deleted_at);

	@Query("Select ar from AccessRule ar "
			+ " left join ResourceAccessRule rar on rar.accessRule = ar.id "
			+ " left join FileResource r on r.id = rar.resource "
			+ " where rar.deletedBy = 0 "
			+ " and r.deleted = 0 "
			+ " and ar.deletedBy = 0"
			+ " and ar.id in ?1")
	List<AccessRule> getAllWithRsource(List<Long> deleted_ids);

	@Query(value = "select count(id) as total from (select ar.* from access_rule ar "
			+ " where ar.is_deleted = 0 "
			+ " and (ar.id = ?1 or ?1 = 0)"
			+ " and (ar.id in ?7 or ?8 = 1)" //and ar.id in ?1 or 0 = 0 and ar.id in ?1 or  < and (u.id in ?1 or ?11 = 1) >
			+ " and (ar.created_at > ?2 or ?2 IS NULL)"
			+ " and (ar.created_at < ?3 or ?3 IS NULL)"
			+ " and ((ar.area_id = ?4 or ar.area_id = ?5 or ar.area_id = ?6)"
			+ " 	or ( ?4 = -1 and ?5 = -1 and ?6 = -1))"
			+ " and Locate(?9,ar.desc_en) > 0"
			+ " order by ar.created_at desc) as a",
			nativeQuery = true)
	Integer getTotal(Long id, Date startDate, Date endDate, Long km, Long ks, Long wg, List<Long> accessRuleId,
			Integer is_admin, String description);

	@Query("Select ar from AccessRule ar "
			+ " left join UserAccessRule uar on uar.accessRuleId = ar.id "
			+ " where uar.userId = ?1 and uar.isDeleted = 0 and ar.isDeleted = 0")
	List<AccessRule> getAddedAccessRule(Long user_id);

	@Query(value = "select ar.* from access_rule ar"
			+ " left join user_access_rule uar on uar.access_rule_id = ar.id "
			+ " where (Locate( ?1 ,ar.section_id) > 0 or ar.section_id = 0)"
			+ " and ( Locate(?2,ar.inst_id) > 0 or (ar.inst_id = 0 and ar.area_id = 3))"
			+ " and ( Locate(?3 ,ar.rank_id) or ar.rank_id = 0) "
			+ " and ar.is_deleted = 0 ",
			nativeQuery = true)
	List<AccessRule> getIdByUserId(Long section, Integer institution, Long rank, Long user_id);
	 
	@Query("select ar from AccessRule ar "
			+ " where ar.isDeleted = 0")
	List<AccessRule> getAccessRulesByResourceIds(List<Long> resourceIds);

	@Query("Select ar from AccessRule ar "
			+ " left join UserAccessRule uar on uar.accessRuleId = ar.id"
			+ " where uar.isDeleted = 0 and ar.isDeleted = 0"
			+ " and uar.userId = ?1")
	List<AccessRule> getIdBySpecial(Long user_id);

	@Query("Select ar from AccessRule ar "
			+ " inner join ResourceAccessRule rar on rar.accessRule = ar.id"
			+ " where rar.deletedBy = 0 and ar.isDeleted = 0"
			+ " and rar.resource = ?1")
	List<AccessRule> getAccessRuleByResourceId(Long resourceId);

}

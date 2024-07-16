package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.OrderBy;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.FileResource;
import com.hkmci.csdkms.model.ResourceCategoryModel;

/**
 * author: Holfer
 * date: 2019/03/21
 * time: 5:54
 * version: 1.0
 * description:
 */
public interface ResourceRepository extends JpaRepository<FileResource, Long> {
	

	@Query(value = " select r.* " + 
			   	   " from resource r  " + 
			   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
			   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
			   	   " left join resource_category rc on r.id= rc.resource_id "+
			   	   " left join  ( select count(id) as hit_rate, resource_id as rid from resource_hit_rate  ) rhr on rhr.rid= r.id "+
			   	   " left join resource_special_user rsu on r.id = rsu.resource_id  "+
			   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
			   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
			   	   " where r.deleted = 0 and ar.area_id = ?1 and ar.is_deleted = 0 " + 
			   	   " and (ar.id in ?2 or ?5 = 1 or rsu.staff_no = ?8  and rsu.deleted_by =0  or sug.user_id = ?8 and sug.is_deleted = 0 and rsg.is_deleted = 0 ) and rar.deleted_by = 0  and r.access_channel in ?6 and r.activated = 1 "+
			   	   " and ((Locate( ?7,r.title_en ) > 0 or ?7 = '')"+
				   " or (Locate( ?7,r.title_tc ) > 0 or ?7 = ''))"+
			   	   " group by r.id " +
			   	   " order by r.created_at desc " +
			   	  " , CASE  WHEN  r.modified_at is null THEN r.created_at  ELSE r.modified_at END desc"+
			   	   " limit ?3 , ?4", nativeQuery = true)
	List<FileResource> findByAreaIdAndDeleted(Long areaId, List<Long> accessRuleId, Integer limitStart, Integer limitEnd, Integer is_admin,List<String> access_channel, String keyword,String staffNo);
	
	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " left join  ( select count(id) as hit_rate, resource_id as rid from resource_hit_rate  ) rhr on rhr.rid= r.id "+
		   	   " left join resource_special_user rsu on r.id = rsu.resource_id  "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
		   	   " where r.deleted = 0 and ar.area_id = ?1 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?5 = 1 or rsu.staff_no = ?8  and rsu.deleted_by =0  or sug.user_id = ?8 and sug.is_deleted = 0 and rsg.is_deleted = 0) and rar.deleted_by = 0  and r.access_channel in ?6 and r.activated = 1 "+
		   	   " and ((Locate( ?7,r.title_en ) > 0 or ?7 = '')"+
			   " or (Locate( ?7,r.title_tc ) > 0 or ?7 = ''))"+
		   	   " group by r.id " +
		   	   " , CASE  WHEN  r.modified_at is null THEN r.created_at  ELSE r.modified_at END desc"+
		   	   " order by r.modified_at desc " +
		   	   " limit ?3 , ?4", nativeQuery = true)
	List<FileResource> findByAreaIdAndDeletedOrderByModifiedAt(Long areaId, List<Long> accessRuleId, Integer limitStart, Integer limitEnd, Integer is_admin,List<String> access_channel, String keyword, String staffNo);

	
	
	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " left join  ( select hit_rate as hit_rate, resource_id as rid from resource_hit_rate  ) rhr on rhr.rid= r.id "+
		   	   " left join resource_special_user rsu on r.id = rsu.resource_id  "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
		   	   " where r.deleted = 0 and ar.area_id = ?1 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?5 = 1 or rsu.staff_no = ?8  and rsu.deleted_by =0  or sug.user_id = ?8 and sug.is_deleted = 0 and rsg.is_deleted = 0 ) and rar.deleted_by = 0  and r.access_channel in ?6 and r.activated = 1 "+
		   	   " and ((Locate( ?7,r.title_en ) > 0 or ?7 = '')"+
			   " or (Locate( ?7,r.title_tc ) > 0 or ?7 = ''))"+
		   	   " group by r.id " +
		   	   " order by rhr.hit_rate desc" +
		   	   " , CASE  WHEN  r.modified_at is null THEN r.created_at  ELSE r.modified_at END desc"+
		   	   " limit ?3 , ?4", nativeQuery = true)
	List<FileResource> findByAreaIdAndDeletedOrderByHitRate(Long areaId, List<Long> accessRuleId, Integer limitStart, Integer limitEnd, Integer is_admin,List<String> access_channel, String keyword, String staffNo);


	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " left join  ( select hit_rate, resource_id as rid from resource_hit_rate  ) rhr on rhr.rid= r.id "+
		   	   " left join resource_special_user rsu on r.id = rsu.resource_id  "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
		   	   " where r.deleted = 0 and ar.area_id = ?1 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?5 = 1 or rsu.staff_no = ?8  and rsu.deleted_by =0  or sug.user_id = ?8 and sug.is_deleted = 0 and rsg.is_deleted = 0 ) and rar.deleted_by = 0  and r.access_channel in ?6 and r.activated = 1 "+
		   	   " and ((Locate( ?7,r.title_en ) > 0 or ?7 = '')"+
			   " or (Locate( ?7,r.title_tc ) > 0 or ?7 = ''))"+
		   	   " group by r.id " +
		   	   " order by r.title_en  REGEXP '^[^A-z]', convert(r.title_en USING gbk)  " +
		   	   " , CASE  WHEN  r.modified_at is null THEN r.created_at  ELSE r.modified_at END desc"+
		   	   " limit ?3 , ?4", nativeQuery = true)
	List<FileResource> findByAreaIdAndDeletedOrderByTitleEnASC(Long areaId, List<Long> accessRuleId, Integer limitStart, Integer limitEnd, Integer is_admin,List<String> access_channel,String keyword, String staffNo);

	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " left join  ( select count(id) as hit_rate, resource_id as rid from resource_hit_rate  ) rhr on rhr.rid= r.id"+
		   	   " left join resource_special_user rsu on r.id = rsu.resource_id  "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
		   	   " where r.deleted = 0 and ar.area_id = ?1 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?5 = 1 or rsu.staff_no = ?8  and rsu.deleted_by =0  or sug.user_id = ?8 and sug.is_deleted = 0 and rsg.is_deleted = 0  ) and rar.deleted_by = 0  and r.access_channel in ?6 and r.activated = 1 "+
		   	   " and ((Locate( ?7,r.title_en ) > 0 or ?7 = '')"+
			   " or (Locate( ?7,r.title_tc ) > 0 or ?7 = ''))"+
		   	   " group by r.id " +
		   	   " order by  r.title_en  REGEXP '^[ -z]', CONVERT(r.title_en USING GBK) ASC " +
		   	   " , CASE  WHEN  r.modified_at is null THEN r.created_at  ELSE r.modified_at END desc"+
		   	   " limit ?3 , ?4", nativeQuery = true)
	List<FileResource> findByAreaIdAndDeletedOrderByTitleEnDESC(Long areaId, List<Long> accessRuleId, Integer limitStart, Integer limitEnd, Integer is_admin,List<String> access_channel, String keyword, String staffNo);

	
	
	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+  
		   	   " left join category c on rc.category_id = c.id "+
		   	   " left join resource_special_user rsu on r.id = rsu.resource_id  "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
		   	   " where r.deleted = 0 and c.id in ?1 and c.show_info = 1 and c.is_deleted = 0 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?5 = 1 or rsu.staff_no = ?8 and rsu.deleted_by =0  or sug.user_id = ?8 and sug.is_deleted = 0 and rsg.is_deleted = 0 ) and rar.deleted_by = 0  and r.access_channel in ?6 and r.activated = 1 " +
		   	   " and ((Locate( ?7,r.title_en ) > 0 or ?7 = '')"+
			   " or (Locate( ?7,r.title_tc ) > 0 or ?7 = ''))"+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit ?3 , ?4", nativeQuery = true)
List<FileResource> searchByCategoryIdIdAndDeleted(List<Long> categoryId, List<Long> accessRuleId, Integer limitStart, Integer limitEnd, Integer is_admin,List<String> access_channel,String keyword, String staffNo);

	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+  
		   	   " left join category c on rc.category_id = c.id "+
		   	   
		   	   " where r.deleted = 0 and c.id in ?1 and c.is_deleted =0 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?3 = 1) and rar.deleted_by = 0  and r.access_channel in ?4 and r.activated = 1 "+
		   	   "and ((Locate( ?5,r.title_en ) > 0 or ?5 = '')"+
			   " or (Locate( ?5,r.title_tc ) > 0 or ?5 = ''))"+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " 
		   	   , nativeQuery = true)
List<FileResource> getTotalsearchByCategoryIdIdAndDeleted(List<Long> categoryId, List<Long> accessRuleId, Integer is_admin,List<String> access_channel,String keyword);

	
	
	
	
	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+

		   	   " left join resource_special_user rsu on r.id = rsu.resource_id  "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
		   	   " where r.deleted = 0 and ar.area_id = ?1 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?3 = 1 or rsu.staff_no = ?6 and rsu.deleted_by =0  or sug.user_id = ?6 and sug.is_deleted = 0 and rsg.is_deleted = 0 ) and rar.deleted_by = 0  and r.access_channel in ?4 and r.activated = 1 "+
		   	   " and ((Locate( ?5, r.title_en)> 0 or ?5 ='' )" +
		   	   " or (Locate (?5, r.title_tc)> 0 or ?5 = '' ))"+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " , nativeQuery = true)
List<FileResource> findTotalByAreaIdAndDeleted(Long areaId, List<Long> accessRuleId, Integer is_admin,List<String> access_channel,String keyword,String staffNo);


	
	
	
	
	@Query(value = " select * from ( " +
			   " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " where r.deleted = 0 and ar.area_id = 1 and ar.is_deleted = 0 " + 
		   	   " and rar.deleted_by = 0 and ar.id in ?1 and r.access_channel in ?2 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 " + 
		   " ) as r1 union all (" +
			   " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " where r.deleted = 0 and ar.area_id = 2 and ar.is_deleted = 0 " + 
		   	   " and rar.deleted_by = 0 and ar.id in ?1 and r.access_channel in ?2 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 " + 
		   " ) union all (" + 
			   " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " where r.deleted = 0 and ar.area_id = 3 and ar.is_deleted = 0 " + 
		   	   " and rar.deleted_by = 0 and ar.id in ?1 and r.access_channel in ?2 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 " + 
		   " ) ", nativeQuery = true)
	List<FileResource> findAllResourceForHomepage(List<Long> accessRuleId, List<String> access_channel);

	
	@Query(value =  " select r.* " + 
		   	   " from resource r  " + 
		       " left join resource_special_user rsu on r.id=rsu.resource_id "+
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id " +
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+ 
		   	   " where r.deleted = 0 and ar.area_id = 1 and ar.is_deleted = 0  and rc.deleted_by = 0 " + 
		   	   " and rar.deleted_by = 0  and ( (ar.id in ?1 and rar.deleted_by =0 )or ( rsu.staff_no = ?3  and rsu.deleted_by =0 ) or  " +
		   	   " (sug.user_id = ?3 and sug.is_deleted = 0 and rsg.is_deleted = 0  )) and r.access_channel in ?2 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 ",nativeQuery =true)
	List<FileResource> findAllResourceForHomepageKM(List<Long> accessRuleId, List<String> access_channel,String staff_no);
	
	
	
	
	@Query(value =  " select r.* " + 
		   	   " from resource r  " + 
		       " left join resource_special_user rsu on r.id=rsu.resource_id "+
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id " +
		   	   " where  r.deleted = 0 and ar.area_id = 2 and ar.is_deleted = 0   and rc.deleted_by = 0 " + 
		   	   " and rar.deleted_by = 0  and ((ar.id in ?1 and rar.deleted_by = 0  )or ( rsu.staff_no = ?3  and rsu.deleted_by =0 ) "+
		   	   " or (sug.user_id = ?3 and sug.is_deleted = 0 and rsg.is_deleted = 0  ) )and r.access_channel in ?2 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 ", nativeQuery = true)
	List<FileResource> findAllResourceForHomepageKS(List<Long> accessRuleId, List<String> access_channel, String staff_no);
	
	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_special_user rsu on r.id = rsu.resource_id "+
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
		   	   " where   r.deleted = 0 and ar.area_id = 3 and ar.is_deleted = 0  and rc.deleted_by = 0 " + 
		   	   " and rar.deleted_by = 0  and ( (ar.id in ?1  and rar.deleted_by = 0 )or ( rsu.staff_no = ?3  and rsu.deleted_by =0 ) or (sug.user_id = ?3 and sug.is_deleted = 0 and rsg.is_deleted = 0  ) ) and r.access_channel in ?2 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 ", nativeQuery = true)
	List<FileResource> findAllResourceForHomepageWG(List<Long>accessRuleId, List<String> access_channel,String staff_no);
	
	
//	
//	
//	@Query(value = " select r.* " + 
//		   	   " from resource r  " + 
//		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
//		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
//		   	   " left join resource_category rc on r.id= rc.resource_id "+
//		   	   " where r.deleted = 0 and ar.area_id = 1 and ar.is_deleted = 0 " + 
//		   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
//		   	   " group by r.id " +
//		   	   " order by r.created_at desc " +
//		   	   " limit 0 , 4 " , nativeQuery = true)
//	List<FileResource> findAllResourceForAdminHomepageKM(List<String> access_channel);
//	
//	@Query(value = " select r.* " + 
//		   	   " from resource r  " + 
//		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
//		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
//		   	   " left join resource_category rc on r.id= rc.resource_id "+
//		   	   " where r.deleted = 0 and ar.area_id = 2 and ar.is_deleted = 0 " + 
//		   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
//		   	   " group by r.id " +
//		   	   " order by r.created_at desc " +
//		   	   " limit 0 , 4 " , nativeQuery = true)
//	List<FileResource> findAllResourceForAdminHomepageKS(List<String> access_channel);
//	
//	@Query(value =  " select r.* " + 
//		   	   " from resource r  " + 
//		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
//		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
//		   	   " left join resource_category rc on r.id= rc.resource_id "+
//		   	   " where r.deleted = 0 and ar.area_id = 3 and ar.is_deleted = 0 " + 
//		   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
//		   	   " group by r.id " +
//		   	   " order by r.created_at desc " +
//		   	   " limit 0 , 4 " , nativeQuery = true)
//	List<FileResource> findAllResourceForAdminHomepageWG(List<String> access_channel);
//	
	

	
	
	
	
	
	
	
	
	
	
	
	@Query(value = " select * from ( " +
					   " select r.* " + 
				   	   " from resource r  " + 
				   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
				   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
				   	   " left join resource_category rc on r.id= rc.resource_id "+
				   	   " where r.deleted = 0 and ar.area_id = 1 and ar.is_deleted = 0 " + 
				   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
				   	   " group by r.id " +
				   	   " order by r.created_at desc " +
				   	   " limit 0 , 4 " + 
				   " ) as r1 union all (" +
					   " select r.* " + 
				   	   " from resource r  " + 
				   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
				   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
				   	   " left join resource_category rc on r.id= rc.resource_id "+
				   	   " where r.deleted = 0 and ar.area_id = 2 and ar.is_deleted = 0 " + 
				   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
				   	   " group by r.id " +
				   	   " order by r.created_at desc " +
				   	   " limit 0 , 4 " + 
				   " ) union all (" + 
					   " select r.* " + 
				   	   " from resource r  " + 
				   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
				   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
				   	   " left join resource_category rc on r.id= rc.resource_id "+
				   	   " where r.deleted = 0 and ar.area_id = 3 and ar.is_deleted = 0 " + 
				   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
				   	   " group by r.id " +
				   	   " order by r.created_at desc " +
				   	   " limit 0 , 4 " + 
				   " ) ", nativeQuery = true)
	List<FileResource> findAllResourceForAdminHomepage(List<String> access_channel);
	
	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " where r.deleted = 0 and ar.area_id = 1 and ar.is_deleted = 0  and rc.deleted_by = 0 " + 
		   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 " , nativeQuery = true)
	List<FileResource> findAllResourceForAdminHomepageKM(List<String> access_channel);
	
	@Query(value = " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id  "+
		   	   " where r.deleted = 0 and ar.area_id = 2 and ar.is_deleted = 0  and rc.deleted_by = 0 " + 
		   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 " , nativeQuery = true)
	List<FileResource> findAllResourceForAdminHomepageKS(List<String> access_channel);
	
	@Query(value =  " select r.* " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " left join resource_category rc on r.id= rc.resource_id "+
		   	   " where r.deleted = 0 and ar.area_id = 3 and ar.is_deleted = 0   and rc.deleted_by = 0 " + 
		   	   " and rar.deleted_by = 0 and r.access_channel in ?1 and r.activated = 1 "+
		   	   " group by r.id " +
		   	   " order by r.created_at desc " +
		   	   " limit 0 , 4 " , nativeQuery = true)
	List<FileResource> findAllResourceForAdminHomepageWG(List<String> access_channel);
	
	

	@Query(value = " select count(id) as total from ( select r.id " + 
		   	   " from resource r  " + 
		   	   " left join resource_access_rule rar on r.id = rar.resource_id " + 
		   	   " left join access_rule ar on ar.id = rar.access_rule_id " + 
		   	   " where r.deleted = 0 and ar.area_id = ?1 and ar.is_deleted = 0 " + 
		   	   " and (ar.id in ?2 or ?3 = 1) and rar.deleted_by = 0 and (r.access_channel = 1 or r.access_channel  = 0) " +
		   	   " group by r.id " +
		   	   " order by r.created_at desc ) as a", nativeQuery = true)
	Long findTotalByAreaIdAndDeleted(Long areaId, List<Long> accessRuleId, Integer is_admin);
	
	
	@Query(value = "select r.title_en from resource r  "
			+ " where r.id = ?1 ", nativeQuery = true)
	String getResourceNameById(Long resourceId);
	
	
	@Query(value = "select r.access_channel from resource r  "
			+ " where r.id = ?1 ", nativeQuery = true)
	Integer getResourceAccessChannelById(Long resourceId);
	
	
	@Query(value ="select a.* from resource_category a ", nativeQuery = true)
	List<ResourceCategoryModel> findtest();
	
	
	@Query(value = "select r.* from resource r "
			+ " left join resource_access_rule rar on r.id = rar.resource_id"
			+ " left join resource_special_user rsu on r.id = rsu.resource_id"
			+ " left join access_rule ar on ar.id = rar.access_rule_id "
			+ " left join resource_special_group rsg on r.id = rsg.resource_id "
		   	+ " left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "
			+ " where r.deleted = 0 "
			+ " and ar.is_deleted = 0 and r.activated = 1 "
			+ " and r.id in ?1 and r.access_channel in ?6  "
			+ " and (rar.deleted_by = 0 or ?5 = 1 "
			+ " or rsu.staff_no = ?4 and rsu.deleted_by = 0 and rsu.resource_id in ?1 or sug.user_id = ?4 and sug.is_deleted = 0 and rsg.is_deleted = 0 and rsg.resource_id in ?1 )"
			+ " group by r.id"
			+ " order by r.title_en "
			+ " limit ?2 , ?3 ", nativeQuery = true)

	List<FileResource> findByCategoryIdAndDeleted(List<Long> resource_ids, Integer limitStart, Integer limitEnd, String user_id, Integer is_admin, List<String> access_channel);
	
	
	
	@Query(value = "Select count(id) as total from  (select r.id from resource r "
			+ " left join resource_access_rule rar on r.id = rar.resource_id"
			+ " left join resource_special_user rsu on r.id = rsu.resource_id"
			+ " left join access_rule ar on ar.id = rar.access_rule_id"
			+ " left join resource_special_group rsg on r.id = rsg.resource_id "
		   	+" left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "
			+ " where r.deleted = 0 "
			+ " and ar.is_deleted = 0 and r.activated = 1 "
			+ " and r.id in ?1 and r.access_channel in ?4  "
			+ " and (rar.deleted_by = 0 or ?3= 1 "
			+ " or rsu.staff_no = ?2 and rsu.deleted_by = 0 and rsu.resource_id in ?1  or sug.user_id = ?2 and sug.is_deleted = 0 and rsg.is_deleted = 0 )"
			+ " group by r.id"
			+ " order by r.created_at desc) as a"
			+ " ", nativeQuery = true)
	Integer findTotalByCatIdAndDeleted(List<Long> resource_ids, String user_id, Integer is_admin, List<String> access_channel);
	
	
	
	
	
	
	FileResource findByNfilenameAndDeleted(String nfilename, int deleted);
	
	//@Query("select r from Resource r where r.originalname like ?1")
	List<FileResource> findByOfilenameContaining(String ofilename);
	
	@Query("select r from FileResource r "
			+ " left join ResourceAccessRule rar on r.id = rar.resource"
			+ " left join AccessRule ar on ar.id = rar.accessRule"
			+ " where r.deleted = 0 and ar.id = ?1 and rar.deletedBy = 0 and r.accessChannel in ?2 order by r.createdAt desc")
	List<FileResource> findByAccessRuleID(Long accessRuleID, List<String> access_channel);
	
	@Query(value = "select r.* from resource r "
			+ " left join resource_access_rule rar on r.id = rar.resource_id"
			+ " left join access_rule ar on ar.id = rar.access_rule_id"
			+ " where r.deleted = 0 and r.activated = 1 and ar.is_deleted = 0 "
			+ " and ar.id = ?1 and rar.deleted_by = 0 and (r.access_channel = 1 or r.access_channel  =0)  "
			+ " and r.file_extension = ?3"
			+ " order by r.created_at desc limit ?2", nativeQuery = true)
	List<FileResource> getLimitByAccessRuleId(Long accessRuleID, Integer num, String type);
	
	List<FileResource> findByOfilenameAndDeleted(String ofilename, int deleted);
	
	
	List<FileResource> findByDeleted(int deleted);
	
	List<FileResource> findByCreatedAtContaining(Date createdAt);
	
	@Query("select r from FileResource r where r.id = ?1 and r.deleted = 0   and r.accessChannel in ?2 group by r.id ")
	FileResource findByIdAndDeleted(Long id, List<String> Access);
//	
//	@Query("select r from FileResource r "
//			+ " left join ResourceAccessRule rar on r.id = rar.resource "
//			+ " left join AccessRule ar on ar.id = rar.accessRule "
//			+ " left join ResourceSpecialUser rsu on rsu.resourceId = r.id "
//			+ " left join ResourceCategoryModel rc on rc.resourceId = r.id "
//			+ " left join CategoryModel c on rc.categoryId = rc.id "
//			+ " where r.deleted = 0 and (ar.id in ?1 or ?4 = 1) "
//			+ " and ar.isDeleted = 0 "
//			+ " and r.id = ?2 and c.showInfo = 1 and c.isDeleted = 0 "
//			+ " and rar.deletedBy = 0 and r.accessChannel in ?5 and rc.deletedBy = 0 "
//			+ " and (rsu.staffNo = ?3 and rsu.deletedBy = 0 and r.id = ?2 and r.accessChannel in ?5 and rc.deletedBy = 0)"
//			+ " order by r.createdAt desc")
	
	@Query(value =" select * from resource r "
			+ " left join resource_access_rule rar  on r.id= rar.resource_id "
			+ " left join access_rule ar on ar.id = rar.access_rule_id "
			+ " left join resource_special_user rsu on r.id = rsu.resource_id "
			+ " left join resource_category rc on r.id = rc.resource_id "
			+ " left join category c on rc.category_id = c.id "
			+ " left join resource_special_group rsg on r.id = rsg.resource_id "
			+ " left join special_user_group_user sug on rsg.special_group_id = sug.special_user_group_id  "
			+ " where r.activated = 1 and r.deleted = 0 and rc.deleted_by = 0  and r.access_channel in ?5 "
			+ " and ( ?4=1 or (rar.access_rule_id in ?1 and rar.deleted_by =0) or (rsu.staff_no = ?3 and rsu.deleted_by = 0)      "
			+ " or ( sug.user_id = ?3 and sug.is_deleted = 0 and rsg.is_deleted = 0 ) ) "
			+ " and r.id = ?2 group by r.id ", nativeQuery = true)
	List<FileResource> findByAccessRuleIdAndResourceId(List<Long> accessRuleId, Long resourceId, String staffNo, Integer is_admin, List<String> access_channel);
	
	@Query("select r from FileResource r "
			+ " left join ResourceAccessRule rar on r.id = rar.resource"
			+ " left join AccessRule ar on ar.id = rar.accessRule"
			+ " where r.deleted = 0 and ar.id in ?1"
			+ " and ar.isDeleted = 0"
			+ " and r.id in ?2 and (r.accessChannel = 1 or r.accessChannel  = 0) "
			+ " and rar.deletedBy = 0 order by r.createdAt desc")
	List<FileResource> findByAccessRuleIdAndResources(List<Long> accessRuleId, List<Long> resourceIds);
	
	@Query(value = "select r.* from resource r "
			+ " left join resource_access_rule rar on r.id = rar.resource_id"
			+ " left join access_rule ar on ar.id = rar.access_rule_id"
			+ " left join resource_category rc on rc.resource_id = r.id"
			+ " left join resource_special_user rsu on rsu.resource_id = r.id"
			+ " left join category c on rc.category_id = c.id "
			+ " where r.deleted = 0 " //and ar.id in ?1 or 0 = 0
			+ " and (r.id = ?1 or ?1 = 0) " 
			+ " and (rar.access_rule_id in ?13 or ?11 = 1 or ?11 =0 ) " 
			+ " and ((rc.category_id in ?14 and rc.deleted_by =0 and c.is_deleted = 0) or ?15 = 0) " 
			+ " and ((Locate( ?2,r.title_en ) > 0 or ?2 = '')"
			+ " 	or (Locate( ?2,r.title_en ) > 0 or ?2 = ''))"
			+ " and (r.created_at > ?3 or ?3 IS NULL)"
			+ " and (r.created_at < ?4 or ?4 IS NULL)"
			+ " and (r.latest_news = ?5 or ?5 = -1)"
			+ " and ((ar.area_id = ?6 or ar.area_id = ?7 or ar.area_id = ?8)"
			+ " 	or (?6 = -1 and ?7 = -1 and ?8 = -1))"
			+ " and r.access_channel in ?16  "
			+ " and (rar.deleted_by = 0 or ?11 = 1  or ?11 =0 or (rsu.staff_no = ?12 and rsu.deleted_by = 0 and r.id = ?1))"
			+ " group by r.id order by r.id desc"
			+ " limit ?9 , ?10",
			nativeQuery = true)
	List<FileResource> findByUserIdAndSearch(Long resourceId, String title, Date startdate, Date enddate,
											 Integer ln, Long km, Long ks, Long wg, Integer start_num, Integer end_num, 
											 Integer is_admin, Long user_id, List<Long> accessRuleId, List<Long> subcategory_list,Integer is_for_sub,
											 List<String> access_channel);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update FileResource r set r.deleted = 1, r.deletedBy = ?2 , r.deletedAt = ?3"
			+ " where r.deleted = 0 "
			+ " and r.id in ?1")
	public void deleteByIds(List<Long> resource_ids,Long deletedBy,Date deletedAt);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "INSERT INTO resource_special_user (resource_id,staff_no,created_at,created_by,deleted_by)" + 
			"VALUES (?1, ?2, ?3, ?4, 0)" , nativeQuery = true)
	public void createSpecialUser(Long resource_id, String specialUserId, Date created_at, Long created_by);
	
	@Transactional
	@Modifying(clearAutomatically  = true)
	@Query(value = "INSERT INTO resource_special_group (resource_id ,special_group_id, created_at,created_by, is_deleted) "
			+ "VALUES (?1,?2,?3,?4,0) ", nativeQuery = true)
	public void createSpecialGroup (Long resoruce_id , Integer specialGroupId, Date creted_at, Long created_by);
	
	
	
	
	
	@Query("select rar.accessRule from FileResource r"
			+ " left join ResourceAccessRule rar on r.id = rar.resource"
			+ " where r.id = ?1 and rar.deletedBy = 0 ")
	public List<Long> getAccessRuleByResourceId(Long resourceId);
	
	@Query("select rar.accessRule from ResourceAccessRule rar where rar.resource =?1 group by rar.accessRule ")
	public List<Long> getAccessRuleByUpdateResource(Long resoureceId);
	
	@Query(value ="select rc from ResourceCategoryModel rc where rc.resourceId = ?1 and rc.deletedBy =0 ")
	public List<ResourceCategoryModel> getResourceCategoryByResourceId(Long resourceId);
	
	
	
	@Query("select r from FileResource r"
			+ " where r.id in ?1 and r.deleted = 0 and (r.accessChannel = 1 or r.accessChannel  =0) ")
	public List<FileResource> findByIds(List<Long> resource_ids);
	
	//Native Query Return String(Object)
	@Query(value = "select rsu.staff_no from resource r"
			+ " left join resource_special_user rsu on r.id = rsu.resource_id"
			+ " where r.id = ?1 and rsu.deleted_by = 0 and (r.access_channel = 1 or r.access_channel  =0)", nativeQuery = true)
	public List<String> getSpecialUserByResourceId(Long resourceId);
	
	
	@Query(value ="select rsg.special_group_id from resource r "
			+ " left join resource_special_group rsg on r.id = rsg.resource_id "
			+ " where r.id = ?1 and rsg.is_deleted = 0 ", nativeQuery = true)
	public List<Integer> getSpecialGroupByResourceId(Long resoruceId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update ResourceAccessRule rar set rar.deletedBy = ?3, rar.deletedAt = ?4"
			+ " where rar.resource = ?2"
			+ " and rar.accessRule in ?1")
	void deleteResourceAccessRule(List<Long> resourceAccessRuleList_delete, Long resourceId, Long deleteBy, Date deletedAt);	
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update resource_special_user rsu set rsu.deleted_by = ?3, rsu.deleted_at = ?4"
			+ " where rsu.resource_id = ?2"
			+ " and rsu.staff_no in ?1 and rsu.deleted_by = 0", nativeQuery = true)
	void deleteResourceSpecialUser(List<String> resourceSpecialUser_delete, Long resourceId, Long deleteBy, Date deletedAt);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update resource_special_group rsg set rsg.deleted_by =?3, rsg.deleted_at = ?4 , rsg.is_deleted =1 "
			+ " where rsg.resource_id =?2 "
			+ " and rsg.special_group_id =?1 and rsg.is_deleted = 0 ", nativeQuery =true )
	void deletedResourceSpecialGroup (List<Integer> resourceSpecialGroup_delete, Long resourceId, Long deletedBy, Date deletedAt);
	
	
	
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update resource r set r.modified_by = ?1, r.modified_at = ?7, r.access_channel in ?3,"
			+ " r.latest_news = ?4, r.latest_news_expiry = ?5, r.activated = ?6"
			+ " where r.id in ?2"
			+ " and r.deleted = 0", nativeQuery = true)
	void updateMultipleResource(Long userId, List<Long> resource_ids, String accessChannel, Integer latestNews,
			Date latestNewsExpiry, Integer activated, Date now_date);

//	@Query(value =  " select resource.* from resource " + 
//					" where resource.id in ( " + 
//					"	SELECT r.id FROM resource r  " + 
//					"	left join resource_access_rule rar on rar.resource_id = r.id " + 
//					"	WHERE r.latest_news = 1 and r.latest_news_expiry > CURRENT_DATE  " + 
//					"	and r.deleted = 0 and rar.access_rule_id in ?1 and rar.access_rule_id not in ?2 and rar.deleted_by = 0 " + 
//					"	group by r.id order by r.created_at desc  " + 
//					" ) " + 
//					" and resource.id not in ( " + 
//					"	SELECT r.id FROM resource r  " + 
//					"	left join resource_access_rule rar on rar.resource_id = r.id " + 
//					"	WHERE r.latest_news = 1 and r.latest_news_expiry > CURRENT_DATE  " + 
//					"	and r.deleted = 0 and rar.access_rule_id in ?2 and rar.access_rule_id not in ?1 and rar.deleted_by = 0 " + 
//					"	group by r.id order by r.created_at desc  " + 
//					" ) " + 
//					" limit 0,10", nativeQuery = true)
//	@Query(value =  " SELECT r.* FROM resource r  " + 
//					" left join resource_access_rule rar on rar.resource_id = r.id " + 
//					" left join resource_special_user rsu on rsu.resource_id = r.id" + 
//					" left join resource_category rc on rc.resource_id = r.id " +
//					" WHERE r.latest_news = 1 and r.latest_news_expiry > CURRENT_DATE  " + 
//					" and r.deleted = 0 and r.activated = 1 and rc.deleted_by = 0 " + 
//					" and (rar.access_rule_id in ?1 or rsu.staff_no = ?3 ) " + 
//					" and rar.deleted_by = 0 and r.access_channel in ?2 and rsu.deleted_by = 0 " + 
//					" group by r.id order by r.created_at desc  " + 
//					" limit 0,10", nativeQuery = true)
//	List<FileResource> findByLatestNewsAndLatestNewsExpiryAndDeleted(List<Long> accessRuleId,List<String> access_channel,String staff_no);
//	

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update ResourceAccessRule rar set rar.deletedBy = ?2, rar.deletedAt = ?3"
			+ " where rar.resource in ?1 and rar.deletedBy = 0")
	void deleteResourceAccessRuleByIds(List<Long> resourceIds, Long userId, Date now_date);

	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update resource_special_user rsu set rsu.deleted_by = ?2, rsu.deleted_at = ?3"
			+ " where rsu.resource_id in ?1 and rsu.deleted_by = 0", nativeQuery = true)
	void deleteResourceSpecialUsers(List<Long> resource_ids, Long userId, Date date);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query (value = "update resource_special_group rsg set rsg.is_deleted = 1 , rsg.deleted_by = ?2 , rsg.deleted_at = ?3 "
			+ "where rsg.resource_id in ?1 and rsg.deleted_by = 0 " , nativeQuery =true)
	void deleteResourceSpecialGroups(List<Long> resoruce_ids , Long deletedBy, Date deletedAt);
	
	
	
	
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update resource_category rc "
			+" set rc.deleted_by = ?3, deleted_at = ?4 "
			+" where rc.category_id in ?1 and rc.resource_id = ?2", nativeQuery = true)
	void deleteResourceCategory(List<Long> resourceIds, Long resourceId, Long userId, Date now_date);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update resource_category rc "
			+" set rc.deleted_by = ?2, deleted_at = ?3 "
			+" where rc.resource_id in ?1 and rc.deleted_by = 0", nativeQuery = true)
	void deleteResourceCategoryByIDs(List<Long> resourceIds, Long userId, Date now_date);
	

	@Query(value = "select max(id) from resource ", nativeQuery = true)
	Long getMaxId();
	
	
	@Query(value = "select count(*) as total from (select count(r.id) from resource r "
			+ " left join resource_access_rule rar on r.id = rar.resource_id"
			+ " left join access_rule ar on ar.id = rar.access_rule_id"
			+ " left join resource_category rc on rc.resource_id = r.id"
			+ " left join resource_special_user rsu on rsu.resource_id = r.id"
			+ " where r.deleted = 0 and r.access_channel in ?14" //and ar.id in ?1 or 0 = 0
			+ " and (r.id = ?1 or ?1 = 0) " 
			+ " and (rar.access_rule_id in ?11 or ?9 = 1) " 
			+ " and ((rc.category_id in ?12 and rc.deleted_by=0) or ?13 = 0) " 
			+ " and ((Locate( ?2,r.title_en ) > 0 or ?2 = '')"
			+ " 	or (Locate( ?2,r.title_en ) > 0 or ?2 = ''))"
			+ " and (r.created_at > ?3 or ?3 IS NULL)"
			+ " and (r.created_at < ?4 or ?4 IS NULL)"
			+ " and (r.latest_news = ?5 or ?5 = -1)"
			+ " and ((ar.area_id = ?6 or ar.area_id = ?7 or ar.area_id = ?8)"
			+ " 	or (?6 = -1 and ?7 = -1 and ?8 = -1))"
			+ " and (rar.deleted_by = 0 or ?9 = 1 or (rsu.staff_no = ?10 and rsu.deleted_by = 0 and r.id = ?1))"
			
			+ " group by r.id order by r.created_at desc ) as a",
			nativeQuery = true)
	Long getTotalNum(Long resourceId, String title, Date startdate, Date enddate,
											 Integer ln, Long km, Long ks, Long wg, Integer is_admin, Long user_id,
											 List<Long> accessRuleId, List<Long> subcategory_list,Integer is_for_sub,List<String> access_channel);

	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update resource_category rc "
			+ " set rc.deleted_by = 0, rc.deleted_at = NULL "
			+" where rc.category_id in ?1 and rc.resource_id = ?2", nativeQuery = true)
	void updateResourceCategory(List<Long> resourceCategoryId, Long resourceId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update resource_special_user rsu "
			+ " set rsu.deleted_by = 0, rsu.deleted_at = NULL "
			+" where rsu.staff_no in ?1 and rsu.resource_id = ?2", nativeQuery = true)
	void updateResourceSpecialUser(List<String> resourceSpecialUserId, Long resourceId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value =" Update resource_special_group rsg "
			+ " set rsg.is_deleted = 0 , rsg.deleted_at = NULL "
			+ " where rsg.user_id in ?1 and rsg.reosuce_id = ?2 ", nativeQuery = true)
	void updateResourceSpecialGroup(List<Integer> resourceSpecialGroupId, Long resourceId);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "Update resource_access_rule rar "
			+ " set rar.deleted_by = 0, rar.deleted_at = NULL "
			+" where rar.access_rule_id in ?1 and rar.resource_id = ?2", nativeQuery = true)
	void updateResourceAccessRule(List<Long> resourceAccessRuleId, Long resourceId);


	@Query(value ="select rc from ResourceCategoryModel rc where rc.resourceId in ?1 and rc.deletedBy = 0")
	List<ResourceCategoryModel> getResourceCategoryByResourceIds(List<Long> resource_ids);


	@Query(value = "select rsu.staff_no from resource r"
			+ " left join resource_special_user rsu on r.id = rsu.resource_id"
			+ " where r.id in ?1 and rsu.deleted_by = 0 and (r.access_channel = 1 or r.access_channel  =0) ", nativeQuery = true)
	List<String> getSpecialUserByResourceIds(List<Long> resource_ids);

//	@Query(value = "select resource.* from resource "
//				+ " where resource.id in ("
//				+ " 	select r.id from resource r "
//				+ " 	left join resource_access_rule rar on rar.resource_id = r.id"
//				+ " 	left join resource_category rc on rc.resource_id = r.id"
//				+ " 	where rar.deleted_by = 0 "
//				+ " 	and (rar.access_rule_id in ?1 or ?5 = 1) "
//				+ " 	and (rc.category_id in ?3 or ?4 = 0)"
//				+ " 	and rc.deleted_by = 0"
//				+ "		group by r.id" 
//				+ " ) "
//				+ " and resource.id not in ("
//				+ " 	select r.id from resource r "
//				+ " 	left join resource_access_rule rar on rar.resource_id = r.id"
//				+ " 	left join resource_category rc on rc.resource_id = r.id"
//				+ " 	where rar.deleted_by = 0 "
//				+ " 	and (rar.access_rule_id in ?2 )  "
//				+ " 	and (rc.category_id in ?3 or ?4 = 0)"
//				+ " 	and rc.deleted_by = 0"
//				+ "		group by r.id" 
//				+ " ) ", nativeQuery = true)
	@Query(value =  " select r.* from resource r " + 
					" left join resource_access_rule rar on rar.resource_id = r.id " +
					" left join resource_category rc on rc.resource_id = r.id "+
					 "left join resource_special_user rsu on rsu.resource_id = r.id " + 
		   	   		" left join resource_special_group rsg on r.id = rsg.resource_id "+
		   	   		" left join special_user_group_user sug on sug.special_user_group_id = rsg. special_group_id "+
					" where  " +
			 		" (rar.access_rule_id in ?1 and rar.deleted_by = 0 or ?4 = 1 or rsu.staff_no =?6 and rsu.deleted_by =0 or sug.user_id = ?6 and sug.is_deleted = 0 and rsg.is_deleted = 0 ) " +
			 		" and (rc.category_id in ?2 or ?3 = 0) " + 
			 		" and rc.deleted_by = 0 and r.access_channel in ?5 and r.activated = 1 " + 
			 		" group by r.id order by r.title_en " , nativeQuery = true)
	List<FileResource> getResourceIdsFromAccessRule(List<Long> accessRuleId, List<Long> subcategory_list, Integer is_for_sub, Integer is_admin, 
			             List<String> access_channel, String user_id);

	@Query(value = " select r.* from resource r " + 
				   " left join resource_access_rule rar on rar.resource_id = r.id " +  
				   " where rar.deleted_by = 0 and rsu." + 
				   " and (rar.access_rule_id in ?1 or ?2 =1 ) " + 
				   " and r.access_channel in ?3 and r.activated = 1 " + 
				   " and r.deleted = 0 and r.latest_new =1 and ?4 < r.latest_new_expiry " +
				   " limit 0,3 group by r.id ", nativeQuery = true)
	List<FileResource> getResourceLatest(List<Long> accessRuleId, Integer is_admin, List<String> access_channel, Date nowDate);
	
	
	
//	
//	@Query(value = "select r.* from resource r "
//			+ " left join resource_access_rule rar on r.id = rar.resource_id"
//			+ " left join resource_special_user rsu on r.id = rsu.resource_id"
//			+ " left join access_rule ar on ar.id = rar.access_rule_id"
//			+ " where r.deleted = 0 "
//			+ " and ar.is_deleted = 0 and r.activated = 1 "
//			+ " and r.id in ?1 and r.access_channel in ?2  "
//			+ " and (rar.deleted_by = 0 or ?3 = 1)"
//			+ " or ((rsu.staff_no = ?4 and rsu.deleted_by = 0 and rsu.resource_id in ?1))"
//			+ " group by r.id"
//			+ " order by r.created_at desc"
//			+ " limit ?5 , ?6 ", nativeQuery = true)
//	List<FileResource> getResourceLatestDetail (List<Long> resourceId, List<String> access_channel,
//			Integer is_admin, String staffNo, Integer start, Integer end);
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Query(value = " select r.video_link from resource r "
			+ " where r.id =10339" , nativeQuery = true)
	Object getVideo();

	@Query(value = " Select r.* from resource r"
				 + " inner join resource_access_rule rar on rar.resource_id = r.id "
				 + " inner join access_rule ar on ar.id = rar.access_rule_id "
				 + " where ((ar.area_id = ?1 or ar.area_id = ?2 or ar.area_id = ?3) "
				 + " 	  or (?1 = -1 and ?2 = -1 and ?3 = -1)) "
				 + " group by r.id ", nativeQuery = true)
	List<FileResource> getAreaForReport(Long km, Long ks, Long wg);

	@Query(value = " Select r.* from resource r"
				 + " inner join resource_category rc on rc.resource_id = r.id "
				 + " where rc.category_id in ?1  or r.id in ?2  group by r.id"
				 + " order by r.id ", nativeQuery = true)
	List<FileResource> getCategoryForReport(List<Long> subcategory_list, List<Long> resource_list);

	@Query(value = " Select rc.* from resource r"
			 + " inner join resource_category rc on rc.resource_id = r.id "
			 + " where rc.category_id in ?1 "
			 + " order by r.id", nativeQuery = true)
	List<ResourceCategoryModel> getResourceIdForReport(List<Long> subcategory_list);

	@Query(value ="select r.* from resource r where r.id = ?1 and r.deleted =0  and r.access_channel in ?2 ", nativeQuery = true)
	Optional<FileResource> findById(Long resource_id, List<String> access_channel);

	@Query("select r from FileResource r where r.id > ?1 and r.id < ?2")
	List<FileResource> findByRange(Long startNum, Long endNum);

	
	
	
//	
//	@Query(value =  " SELECT r.* FROM resource r  " + 
//			" left join resource_access_rule rar on rar.resource_id = r.id "+ 
//			" left join resource_category rc on rc.resource_id = r.id " + 
//			" WHERE r.latest_news = 1 and r.latest_news_expiry > CURRENT_DATE  " + 
//			" and r.deleted = 0 and r.activated = 1 and rc.deleted_by = 0 " + 
//			" and rar.deleted_by = 0 and r.access_channel in ?1 " + 
//			" group by r.id order by r.created_at desc  " + 
//			" limit 0,10", nativeQuery = true)
	
	
//	@Query(value = " select r.* from resource r " + 
//			   " left join resource_special_user rsu on rsu.resource_id = r.id "+
//			   " left join resource_access_rule rar on rar.resource_id = r.id " + 
//			   " where rar.deleted_by = 0 " + 
//			   " and r.access_channel in ?1 and r.activated = 1 " + 
//			   " and r.deleted = 0 and r.latest_news =1 and   r.latest_news_expiry > CURRENT_DATE " +
//			   " group by r.id order bt r.created_at desc "
//			   + "limit 0,4", nativeQuery = true)
	@Query(value =  " SELECT r.* FROM resource r  " 
			+ " left join resource_access_rule rar on r.id = rar.resource_id"
			+ " left join resource_special_user rsu on r.id = rsu.resource_id"
			+ " left join access_rule ar on ar.id = rar.access_rule_id"
			+ " where r.deleted = 0 "
			+ " and ar.is_deleted = 0 and r.activated = 1 "
			+ "  and r.access_channel in ?1 and r.latest_news_expiry > CURRENT_DATE  "
			+ " group by r.id"
			+ " order by r.created_at desc"
			+ " limit 0 , 5 ", nativeQuery = true)
	List<FileResource> findByLatestNewsAndLatestNewsExpiryAndDeletedForAdmin(List<String> access_channel);
	
	
//	@Query(value =  " SELECT r.* FROM resource r  " + 
//			" left join resource_access_rule rar on rar.resource_id = r.id " + 
//			" left join resource_special_user rsu on rsu.resource_id = r.id" + 
//			" left join resource_category rc on rc.resource_id = r.id " +
//			" WHERE r.latest_news = 1 and r.latest_news_expiry > CURRENT_DATE  " + 
//			" and r.deleted = 0 and r.activated = 1 and rc.deleted_by = 0 " + 
//			" and (rar.access_rule_id in ?1 or rsu.staff_no = ?3 ) " + 
//			" and rar.deleted_by = 0 and r.access_channel in ?2 and rsu.deleted_by = 0 " + 
//			" group by r.id order by r.created_at desc  " + 
//			" limit 0,10", nativeQuery = true)
	
	
	
//	@Query(value =  " SELECT r.* FROM resource r  " 
//	+ " left join resource_access_rule rar on r.id = rar.resource_id"
//	+ " left join resource_special_user rsu on r.id = rsu.resource_id"
//	+ " left join access_rule ar on ar.id = rar.access_rule_id"
//	+ " where r.deleted = 0 "
//	+ " and ar.is_deleted = 0 and r.activated = 1 "
//	+ "  and r.access_channel in ?2 and r.latest_news_expiry > CURRENT_DATE "
//	+ " and ( rar.access_rule_id in ?1 and rar.deleted_by = 0  "
//	+ " or (rsu.staff_no = ?e and rsu.deleted_by = 0))"
//	+ " group by r.id"
//	+ " order by r.created_at desc"
//	, nativeQuery = true)
	

	
	@Query(value =  " SELECT r.* FROM resource r  " 
	+ " left join resource_access_rule rar on r.id = rar.resource_id"
	+ " left join resource_special_user rsu on r.id = rsu.resource_id"
	+ " left join access_rule ar on ar.id = rar.access_rule_id "
	+ " left join resource_special_group rsg on r.id = rsg.resource_id "
	+ " left join special_user_group_user sug on rsg.special_group_id = sug.special_user_group_id " 
	+ " where r.deleted = 0 "
	+ " and ar.is_deleted = 0 and r.activated = 1 "
	+ "  and r.access_channel in ?3 and r.latest_news_expiry > CURRENT_DATE "
	+ " and ( (rar.access_rule_id in ?1 and rar.deleted_by = 0 ) or ?2 = 1 "
	+ " or (rsu.staff_no = ?4 and rsu.deleted_by = 0) or ( sug.user_id =?4 and sug.is_deleted =0 and rsg.is_deleted=0 ) )"
	+ " group by r.id"
	+ " order by r.created_at desc "
	+ " limit 0,4" 
	, nativeQuery = true)
List<FileResource> findByLatestNewsAndLatestNewsExpiryAndDeleted(List<Long> accessRuleId,Integer is_admin,List<String> access_channel,String staff_no);


	
	
	
	
	
	
	
	
	
	
//
//	
//	
//	@Query(value =  " SELECT r.* FROM resource r  " + 
//			" left join resource_access_rule rar on rar.resource_id = r.id " + 
//			" left join resource_special_user rsu on rsu.resource_id = r.id" + 
//			" left join resource_category rc on rc.resource_id = r.id " +
//			" WHERE r.latest_news = 1 and r.latest_news_expiry > CURRENT_DATE  " + 
//			" and r.deleted = 0 and r.activated = 1 and rc.deleted_by = 0 " + 
//			" and (rar.access_rule_id in ?1 or rsu.staff_no = ?3 ) " + 
//			" and rar.deleted_by = 0 and r.access_channel in ?2 and rsu.deleted_by = 0 " + 
//			" group by r.id order by r.created_at desc  " + 
//			" limit 0,10", nativeQuery = true)
//List<FileResource> findByLatestNewsAndLatestNewsExpiryAndDeleted(List<Long> accessRuleId,List<String> access_channel,String staff_no);
//
//

	@Query(value =  " SELECT r.* FROM resource r  " 
	+ " left join resource_access_rule rar on r.id = rar.resource_id"
	+ " left join resource_special_user rsu on r.id = rsu.resource_id"
	+ " left join access_rule ar on ar.id = rar.access_rule_id"
	+ " where r.deleted = 0 "
	+ " and ar.is_deleted = 0 and r.activated = 1 "
	+ "  and r.access_channel in ?3 and r.latest_news_expiry > CURRENT_DATE "
	+ " and (( rar.access_rule_id in ?1 and rar.deleted_by = 0 ) or ?2 = 1 "
	+ " or (rsu.staff_no = ?4 and rsu.deleted_by = 0))"
	+ " group by r.id"
	+ " order by r.created_at desc"
	, nativeQuery = true)
List<FileResource> getResourceLatest (List<Long> accessRuleId,Integer is_admin, 
		List<String> access_channel,
	Date now, String staff_no);


	
	
	
	
	


	@Query(value = "select r.* from resource r "
		+ " left join resource_access_rule rar on r.id = rar.resource_id"
		+ " left join resource_special_user rsu on r.id = rsu.resource_id"
		+ " left join access_rule ar on ar.id = rar.access_rule_id"
		+ " where r.deleted = 0 "
		+ " and ar.is_deleted = 0 and r.activated = 1 "
		+ " and r.id in ?1 and r.access_channel in ?2  "
		+ " and (rar.deleted_by = 0 or ?3 = 1)"
		+ " or ((rsu.staff_no = ?4 and rsu.deleted_by = 0 and rsu.resource_id in ?1))"
		+ " group by r.id"
		+ " order by r.created_at desc"
		+ " limit ?5 , ?6 ", nativeQuery = true)
	List<FileResource> getResourceLatestDetail (List<Long> resourceId, List<String> access_channel,
		Integer is_admin, String staffNo, Integer start, Integer end);


	
}

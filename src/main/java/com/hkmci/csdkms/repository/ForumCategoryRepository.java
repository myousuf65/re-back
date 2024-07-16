package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ForumPost;
import com.hkmci.csdkms.model.ForumCategoryModel;

public interface ForumCategoryRepository extends JpaRepository<ForumCategoryModel, Long> {

	
	@Query("select fcm from ForumCategoryModel fcm where parentForumId =?1 and accessChannel in(?2) and isDeleted =0 order by orderBy ")
	List<ForumCategoryModel> getByParentID(Long forumId, List<String> channel);
	
	@Query(value ="select * from csdkms.forum_category fc " + 
			"left join csdkms.forum_access_rule  far on fc.id = far.forum_cat_id " + 
			"left join csdkms.forum_admin fa on fc.id = fa.forum_cat_id "+
			"left join csdkms.forum_special_user fsu on fc.id = fsu.forum_cat_id " +
			"left join csdkms.forum_special_usergroup fsg on fc.id = fsg.forum_cat_id " + 
			"left join csdkms.special_user_group_user sug on fsg.special_usergroup_id = sug.special_user_group_id " +
			"where fc.access_channel in (?4) "+
			"and ( far.access_rule_id in (?2) and far.is_deleted =0 " +
			" or  fa.user_id = ?3 and fa.is_deleted = 0  or fsu.user_id =?3 and fsu.is_deleted = 0" +
			" or sug.user_id = ?3 and sug.is_deleted = 0 and fsg.is_deleted = 0 ) "+
			" and fc.is_deleted =0 and fc.show_info=1 " + 
			"and fc.parent_forum_id = ?1 " + 
			"group by fc.id order by fc.order_by  ", nativeQuery = true )
	List<ForumCategoryModel> getSubcate(Long forumId,List<Long> accessRuleIds, String staff_no, List<String> access_channel);
	
	
	@Query(value = "select fc.* from  "
			+ " forum_category fc "
			+ " left join forum_access_rule far on far.forum_cat_id = fc.id "
			+ " left join forum_admin fa on fa.forum_cat_id = fc.id "
			+ " where fc.is_deleted = 0  and fc.show_info =1 and (far.access_rule_id in ?1 and far.is_deleted = 0 or  "
			+ " fa.user_id = ?2 and fa.is_deleted = 0 ) and fc.parent_forum_id = ?3 and fc.access_channel in (?4) ", nativeQuery = true)
	List<ForumCategoryModel> findSubCatByAccessRuleAndAdmin(List<Long> accessruleId, String userId, Long parentId, List<String> channel);
	
	
	@Query(value ="select fc.* from forum_category fc "+
			" left join forum_special_user fsu on fsu.forum_cat_id = fc.id "+
			"left join csdkms.forum_special_usergroup fsg on fc.id = fsg.forum_cat_id " + 
			"left join csdkms.special_user_group_user sug on fsg.special_usergroup_id = sug.special_user_group_id " +
			" where fc.is_deleted = 0 and fc.show_info = 1 and (fsu.user_id = ?1 and fsu.is_deleted = 0  or sug.user_id = ?1 and sug.is_deleted = 0 and fsg.is_deleted = 0) and fc.access_channel in (?3) and fc.parent_forum_id = ?2" ,nativeQuery = true )
	List<ForumCategoryModel> findSubCatBySpecialUser(String userId, Long parentId, List<String> channel);
	
	@Query(value = "select f.id, f.name_en, f.name_tc,fp.fid,fp.fname_en,fp.fname_tc " + 
			"from forum_category f " + 
			"left join ( " + 
			"  select id as fid, name_en as fname_en, name_tc as fname_tc from forum_category " + 
			"  )  fp on f.parent_forum_id = fp.fid " + 
			"  where  f.id = ?1 and f.access_channel in (?2) " 
			 , nativeQuery = true)
	List<Object[]> getFamily(Long forumId, List<String> channel);
	
	@Query("select fc from ForumCategoryModel fc  where isDeleted =0")
	List<ForumCategoryModel> findByIsDeleted();
	
	
	@Query(value = "select count(fc.id) from forum_category fc "
			+ " left join forum_admin fa on fc.id = fa.forum_cat_id "
			+ " left join forum_access_rule far on fc.id = far.forum_cat_id "
			+ " where fc.id = ?2 and fc.is_deleted =  0 and (fa.user_id =?1 and fa.is_deleted =0   or " 
			+ " far.access_rule_id in ?3 and far.rule_right = 1 and far.is_deleted =0) " , nativeQuery = true)
	Integer canCreateByAccessRuleAndAdmin(String staffNo, Long categoryId,List<Long> accessRule);

	
	@Query(value ="select count(fc.id) from forum_category fc "
			+ " left join forum_special_user fsu on fsu.forum_cat_id = fc.id "
			+ " left join forum_special_usergroup fsg on fc.id = fsg.forum_cat_id "
			+ " left join special_user_group_user sug on fsg.special_usergroup_id = sug.special_user_group_id "
			+ " where fc.id = ?2 and fc.is_deleted = 0 and fc.show_info = 1 and ( fsu.user_id = ?1  and fsu.is_deleted = 0 and fsu.rule_right = 1  or "
			+ " sug.user_id = ?1 and sug.is_deleted =0 and fsg.is_deleted =0 and fsg.rule_right = 1) " ,nativeQuery = true )
	Integer canCreateBySpecialUser(String userId,Long categoryId );
	
	
	
	
	
	@Query( value = "select fc.id from forum_category fc "
			+ " left join forum_admin fa "
			+ " on fc.id = fa.forum_cat_id "
			+ " left join forum_access_rule far "
			+ " on fc.id = far.forum_cat_id "
			+ " left join forum_special_user fsu "
			+ " on fc.id = fsu.forum_cat_id "
			+ " left join forum_special_usergroup fsg "
			+ " on fc.id = fsg.forum_cat_id "
			+ " left join special_user_group_user sug "
			+ " on fsg.forum_cat_id = sug.special_user_group_id "
			+ " where fc.id = ?1 and fc.is_deleted =  0 "
			+ " and ( "
			+ " fa.user_id =?2 and fa.is_deleted =0   or "
			+ " far.access_rule_id in ?3 and far.rule_right = 1 and far.is_deleted =0 or "
			+ " fsu.user_id = ?2 and fsu.rule_right = 1 and fsu.is_deleted = 0 or "
			+ " fsg.is_deleted = 0 and fsg.rule_right = 1 and sug.user_id =?2 and sug.is_deleted = 0  "
			+ ") group by fc.id " , nativeQuery = true)
	Integer canCreate(Long categoryId, String staffNo, List<Long> accessRule);
	
	

	
	
	 
//	@Query(value =" select fc.*, fa.user_id as admin ,"
//			+ " (select access_rule_id from forum_access_rule where fc.id = forum_cat_id  and is_deleted =0  group by access_rule_id ) as  access_rule_id , "
//			+ " (select rule_right from forum_access_rule where fc.id = forum_cat_id and is_deleted = 0 group by rule_right ) as access_rule_right , "
//			+ " (select user_id from forum_special_user where fc.id = forum_cat_id and is_deleted =0 group by user_id ) as user_id , "
//			+ " (select rule_right from forum_special_user where fc.id = forum_cat_id and is_deleted = 0 group by user_id ) as rule_right "
//			+ " from forum_category fc "
//			+ " left join forum_admin fa on fc.id = fa.forum_cat_id "
//			+ " where fc.id = ?1 and fc.is_deleted =0 and (fa.is_deleted =0 or fa.is_deleted is null )", nativeQuery  = true ) 

	@Query( value ="select fc.* , fa.user_id as admin, far.access_rule_id, far.rule_right as access_rule_right, fsu.user_id, fsu.rule_right " + 
			"from forum_category fc " + 
			"left join forum_admin fa " + 
			"on fc.id = fa.forum_cat_id " + 
			"left join forum_access_rule far " + 
			"on fc.id = far.forum_cat_id " + 
			"left join forum_special_user fsu " + 
			"on fc.id = fsu.forum_cat_id  " + 
			"where fc.id = ?1 and fc.is_deleted = 0 and ( fa.is_deleted = 0 or fa.is_deleted is null ) "+
			"and  (far.is_deleted = 0 or far.is_deleted is null )and (fsu.is_deleted = 0 or fsu.is_deleted is null) " 
			, nativeQuery = true)
	List<Object []> getCategoryAdmin(Long forumId);

	@Query(value="select fc.* from forum_category fc "
			+ " join forum_access_rule far "
			+ " on fc.id = far.forum_cat_id "
			+ " where far.access_rule_id in ?1 and fc.id= ?2 "
			+ " and far.is_deleted =0 and fc.is_deleted =0 ", nativeQuery = true)
	List<ForumCategoryModel> findByAccessRule(List<Long> accessRuleId, Long categoryId);
	
	
	
	@Query(value="select fc.id from forum_category fc " + 
			"left join forum_access_rule far on far.forum_cat_id = fc.id " + 
			"left join forum_admin fa  on fa.forum_cat_id = fc.id " + 
			"where (far.access_rule_id in (?1) and  far.is_deleted = 0 )" + 
			"or (fa.user_id = ?2 and fa.is_deleted = 0 ) and fc.id= ?3 and fc.access_channel in (?4) " + 
			"group by fc.id ",nativeQuery =true)
	List<Long> findCatByAccessRule(List<Long> accessRuleIds, String staff_no, Long categoryId, List<String> channel);
	
	@Query(value ="select fc.id from forum_category fc "
			+ " left join forum_special_user fsu on fsu.forum_cat_id = fc.id "
			+"left join csdkms.forum_special_usergroup fsg on fc.id = fsg.forum_cat_id " 
			+"left join csdkms.special_user_group_user sug on fsg.special_usergroup_id = sug.special_user_group_id " 
			+ " where fc.is_deleted = 0 and fc.show_info = 1 and ((fsu.user_id = ?1 and fsu.is_deleted = 0 ) or (sug.user_id = ?1 and sug.is_deleted = 0 and fsg.is_deleted = 0 ) ) and fc.id =?2 and fc.access_channel in (?3) "
			+ "and fsu.is_deleted = 0 " ,nativeQuery = true )
	List<Long> findCatBySpecialUser(String staff_no, Long categoryId, List<String> channel);
	
	
	@Query(value="select fc.id from forum_category fc " + 
			"left join forum_access_rule far on far.forum_cat_id = fc.id  " + 
			"left join forum_admin fa on fa.forum_cat_id = fc.id "+
			"left join forum_special_user fsu on fsu.forum_cat_id = fc.id " + 
			"left join csdkms.forum_special_usergroup fsg on fc.id = fsg.forum_cat_id " + 
			"left join csdkms.special_user_group_user sug on fsg.special_usergroup_id = sug.special_user_group_id " +
			"where ((far.access_rule_id in (?1) and  far.is_deleted = 0   and far.rule_right =1 )" + 
			"or (fa.user_id = ?2 and fa.is_deleted = 0) or (fsu.user_id = ?2 and fsu.is_deleted = 0 and fsu.rule_right =1 ) or ( sug.user_id = ?3 and sug.is_deleted = 0 and fsg.is_deleted = 0 )   ) and fc.access_channel in (?4) and fc.id= ?3 " + 
			"group by fc.id ; ",nativeQuery =true)
	List<Long> findCatByAccessRuleCreate(List<Long> accessRuleIds, String staff_no, Long categoryId, List<String> channel);
	
}

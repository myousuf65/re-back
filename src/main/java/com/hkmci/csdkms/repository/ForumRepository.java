package com.hkmci.csdkms.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ForumPost;

public interface ForumRepository extends JpaRepository<ForumPost, Long> {
	
	
	
	@Query(value = "select fc.id from  "
			+ " forum_category fc "
			+ " left join forum_access_rule far on far.forum_cat_id = fc.id "
			+ " left join forum_admin fa on fa.forum_cat_id = fc.id "
			+ " where fc.is_deleted = 0  and fc.show_info =1 and fc.access_channel in (?3) and (far.access_rule_id in ?1 and far.is_deleted = 0 or  "
			+ " fa.user_id = ?2 and fa.is_deleted = 0 ) ", nativeQuery = true)
	List<Long> findCategoryByAccessRuleAndAdmin(List<Long> accessruleId, String userId,List<String> channel);
	
	
	@Query(value ="select fc.id from forum_category fc "
			+ " left join forum_special_user fsu on fsu.forum_cat_id = fc.id "
			+ " left join forum_special_usergroup fsg on fsg.forum_cat_id = fc.id "
			+ " left join special_user_group_user sug on fsg.special_usergroup_id = sug.special_user_group_id "
			+ " where fc.is_deleted = 0 and fc.show_info = 1 and (fsu.user_id = ?1 and fsu.is_deleted = 0  or fsg.is_deleted =0 and  sug.user_id = ?1 and sug.is_deleted = 0 )" ,nativeQuery = true )
	List<Long> findCategoryBySpecialUser(String userId);
	
	
	
	
	@Query(value =" select f.* , fc.name_en , fc.name_tc "
			+ " from forum_post f "
			+ " left join forum_category fc  on f.forum_id = fc.id "
			+ " where f.is_deleted =0  and f.forum_id in ?1  "
			+ " group by f.id order by f.created_at desc limit 0 ,4 ", nativeQuery = true )
	List<Object[]> findHotPostByCategoryId(List<Long> categoryId);
	
	@Query(value = "Select f.* , fc.name_en , fc.name_tc "
			+ " from forum_category fc "
			+ " left join forum_post f "
			+ " on f.forum_id =fc.id  "
			+ " left join forum_access_rule far on far.forum_cat_id = fc.id  "
			+ " left join forum_special_user fsu on fsu.forum_cat_id = fc.id  "
			+ " left join forum_admin fa on fa.forum_cat_id = fc.id "
			+ " left join forum_special_usergroup fsg on fsg.forum_cat_id = fc.id "
			+ " left join special_user_group_user sug on fsg.special_usergroup_id = sug.special_user_group_id "
			+ " where f.is_deleted =0 and fc.is_deleted = 0 and fc.show_info = 1 "
			+ " and( ?1=5 "
			+ " or far.access_rule_id in( ?2) and far.is_deleted = 0 "
			+ " or fsu.user_id =?3 and fsu.is_deleted= 0 "
			+ " or fa.user_id =?3 and fa.is_deleted = 0 "
			+ " or sug.user_id = ?3 and sug.is_deleted = 0 ) "
			+ " group by f.id order by f.created_at desc limit 0,4 ",nativeQuery = true)
	List<Object[]> findHotPost(Long usergroup , List<Long> accessruleId, String userId, List<String> channel);
	
	
	@Query(value = "Select f.* , fc.name_en , fc.name_tc "
			+ " from forum_post f "
			+ " left join forum_category fc "
			+ " on f.forum_id =fc.id  and fc.is_deleted = 0 and fc.show_info = 1 "
			+ " where f.is_deleted =0 and fc.is_deleted = 0 and fc.show_info = 1 and fc.access_channel in (?1) "
			+ " group by f.id order by f.created_at desc limit 0,4 ",nativeQuery = true)
	List<Object[]> findHotPostSuperAdmin(List<String> channel);
	
	
	
	
	
	
	
	@Query(value = "Select f.* , fc.name_en , fc.name_tc "
			+ " from forum_post f "
			+ " left join forum_category fc "
			+ " on f.forum_id =fc.id  and fc.is_deleted = 0 and fc.show_info = 1 "
			+ " where f.is_deleted =0 and fc.is_deleted = 0 and fc.show_info = 1  "
			+ " group by f.id order by f.created_at desc limit 0,4 ",nativeQuery = true)
	List<Object[]> findHotPostSuperAdmin();
	
	
	
	
	
	
	@Query(value = "Select f.*  , fc.name_en , fc.name_tc "
			+ " from forum_post f "
			+ " left join forum_category fc on f.forum_id =fc.id"
			+ " left join forum_access_rule far "
			+ " on far.forum_cat_id = fc.id  =fc.id where f.is_deleted =0 "
			+ "order by f.created_at desc limit 0,4 ",nativeQuery = true)
	List<Object[]> findHotPostByAccess(List<Long> accessRuleId);
	
//	@Query("Select f,fp.forumCatId from ForumPost f "
//			+ "inner join ForumAccessRule fp "
//			+ "on f.id = fp.forumCatId "
//			+ " and f.isDeleted = 0"
//			+ "and fp.accessRuleId in ?1 and f.id = ?2 ")	
	@Query(value = "select f.*, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments , fc.name_en as forum_en, fc.name_tc as forum_tc , lm.fullname as name  "
			+ " from forum_post f "
			+ " left join ("
			+ "		select pkid as lid, count(id) as likes from forum_like where func_id = 1 and is_deleted =0 group by pkid "
			+ " ) l on f.id = l.lid "
			+ " left join ( "
			+ "		select post_id as cid, count(id) as comments from forum_comment where is_deleted = 0 group by post_id "
			+ " ) c on f.id = c.cid "
			+ " left join user lm on lm.id = f.modified_by  "
			+ " left join user u on u.id = f.created_by "
			+ " left join  forum_category fc on f.forum_id = fc.id and fc.is_deleted =0 and fc.access_channel in (?2)  "
			+ " where f.id = ?1 and f.is_deleted = 0", nativeQuery = true)	
	List<Object[]> findByAccessRuleAndPostId( Long postId, List<String> channel);

	@Query(value = "select f.*, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments , fc.name_en as forum_en, fc.name_tc as forum_tc , lm.fullname as name "
			+ " from forum_post f "
			+ " left join ("
			+ "		select pkid as lid, count(id) as likes from forum_like where func_id = 1 and is_deleted =0 group by pkid "
			+ " ) l on f.id = l.lid "
			+ " left join ( "
			+ "		select post_id as cid, count(id) as comments from forum_comment where is_deleted = 0 group by post_id "
			+ " ) c on f.id = c.cid "
			+ " left join user lm on lm.id = f.modified_by " 
			+ " left join user u on u.id = f.created_by"
			+ " left join  forum_category fc on f.forum_id = fc.id and fc.is_deleted =0 and fc.show_info=1 and fc.access_channel in(?2)  "
			+ " where f.id = ?1 and f.is_deleted = 0", nativeQuery = true)	
	List<Object[]> findByPostId( Long postId, List<String> channel);
	
	
	@Query(value ="select myData.id, myData.post_title ,myData.forum_cat_id from ( select f.*,fp.forum_cat_id, " + 
			"row_number() over(order by f.id) as rownum " + 
			"from csdkms.forum_post f " + 
			"join csdkms.forum_access_rule fp " + 
			"on f.id=fp.forum_cat_id " + 
			"and fp.access_rule_id in ?1  and fp.forum_cat_id=?2 " + 
			") myData " + 
			"where rownum >( " + 
			"select rownum from (select f.*, " + 
			"row_number() over(order by f.id )  as rownum " + 
			"from csdkms.forum_post f " + 
			"join csdkms.forum_access_rule fp " + 
			"on f.id=fp.forum_cat_id " + 
			"and fp.access_rule_id in ?1  and fp.forum_cat_id=?2 ) myData " + 
			"where id=?3 " + 
			") " + 
			" order by rownum " + 
			"LIMIT 1 " , nativeQuery = true)
	ForumPost getNextPost(List<Long> accessRuleIdList,Long forumId, Long postId);
	
	
	@Query("Select f,fp.forumCatId from ForumPost f "
			+ "inner join ForumAccessRule fp "
			+ "on f.id = fp.forumCatId "
			+ " and f.isDeleted = 0"
			+ "and fp.accessRuleId in ?1 and f.id = ?2 ")	
	List<ForumPost> findNext(List<Long> accessRuleId, Long postId);
	

	
	
	@Query(value = "select f.*, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments ,lm.fullname as name "
			+ " from forum_post f "
			+ " left join ("
			+ "		select pkid as lid, count(id) as likes from forum_like where func_id = 1 and is_deleted =0 group by pkid "
			+ " ) l on f.id = l.lid "
			+ " left join ( "
			+ "		select post_id as cid, count(id) as comments from forum_comment where  is_deleted = 0 and is_reply2cmnt =1  group by post_id "
			+ " ) c on f.id = c.cid "
			+ " left join user u on u.id = f.created_by "
			+ " left join user lm on lm.id = f.modified_by "
			+ " where f.forum_id = ?1  and f.is_deleted = 0 "
			+ " order by f.order_by , f.created_at desc "
			+ " limit ?2 , ?3", nativeQuery = true)
	List<Object[]> getCategoryPost(Long forumId, Integer start , Integer end );
	
	
	
	@Query(value = "select f.*, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments "
			+ " from forum_post f "
			+ " left join ("
			+ "		select pkid as lid, count(id) as likes from forum_like where func_id = 1 and is_deleted =0 group by pkid "
			+ " ) l on f.id = l.lid "
			+ " left join ( "
			+ "		select post_id as cid, count(id) as comments from forum_comment where is_deleted = 0 group by post_id "
			+ " ) c on f.id = c.cid "
			+ " left join user u on u.id = f.created_by "
			+ " where f.forum_id = ?1  and f.is_deleted = 0 "
			+ " order by f.order_by , f.created_at desc "
			, nativeQuery = true)
	List<Object[]> getCategoryPostTotal(Long forumId);
	
	

	@Query(value = "select f.*, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments "
			+ " from forum_post f "
			+ " left join ("
			+ "		select pkid as lid, count(id) as likes from forum_like where func_id = 1 and is_deleted =0 group by pkid "
			+ " ) l on f.id = l.lid "
			+ " left join ( "
			+ "		select post_id as cid, count(id) as comments from forum_comment where is_deleted = 0 group by post_id "
			+ " ) c on f.id = c.cid "
			+ " left join user u on u.id = f.created_by "
			+ " where f.forum_id = ?1  and f.is_deleted = 0"
			+ " order by f.order_by , f.created_at desc", nativeQuery = true)
	List<Object[]> getGeneralCategoryPost(Long forumId);
	
	
	@Query(value= "select f.* from forum_post f where f.id = ?1 ", nativeQuery = true )
	ForumPost getPostTitle(Long postId);
	
	
	@Query (value ="select Case WHEN fp.id IS NOT NULL "
			+ " THEN fp.id ELSE 0 END "
			+ " from csdkms.forum_post fp " + 
			"left join csdkms.forum_admin fa on fp.forum_id = fa.forum_cat_id " + 
			"where fp.is_deleted = 0 and fp.id = ?1 and (fp.created_by = ?2 or (fa.user_id = ?3 and fa.is_deleted =0)) " +
			"group by fp.id " , nativeQuery = true)
	Integer canDeleteByAdmin(Long postId, Long userId ,String staffNo);
	
	
	@Query(value= "select fp.id from csdkms.forum_post fp " + 
			"left join csdkms.forum_admin fa on fp.forum_id = fa.forum_cat_id " + 
			"left join csdkms.forum_comment fc on fc.post_id = fp.id " + 
			"where fp.is_deleted = 0 and fc.id = ?1 and (fp.created_by = ?2 or (fa.user_id = ?3 and fa.is_deleted =0)) group by fp.id " , nativeQuery = true)
	Integer canDeleteCommentByAdmin (Long commentId, Long userId, String staffNo);
	
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value =" update forum_post set hit = ?1 where id =?2", nativeQuery = true)
	void updatehit(Integer hit, Long Id);
}


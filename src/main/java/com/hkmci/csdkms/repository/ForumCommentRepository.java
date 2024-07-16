package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.ForumCommentModel;

public interface ForumCommentRepository extends JpaRepository<ForumCommentModel, Long> {

	
	
	@Query(value = "select fc.id from forum_comment fc"
			+ " where fc.post_id = ?1 "
			+ " and fc.is_deleted =0 and is_reply2cmnt = 1"
			+ " order by created_at asc "
			+ " limit ?2, ?3 ", nativeQuery = true)
	List<Long> getCommentsLevelOne(Long postId, Integer pageStart, Integer pageEnd);

	
	
	@Query(value = "select count(id) from forum_comment fc "+
					"where (fc.post_id =?1 and fc.created_by = ?2 and fc.is_deleted =0 and fc.is_reply2cmnt =1) or "+ 
					"( fc.id in (select fc2.is_reply2cmnt "+
					"from csdkms.forum_comment fc2 where fc2.post_id =?1 and fc2.created_by = ?2 and fc2.is_deleted =0 and fc2.is_reply2cmnt !=1) "+
					"and fc.is_deleted =0) ", nativeQuery =true)
	Integer diduserComments(Long postId, Long userId);
	
	@Query(value = "select id from forum_comment fc "+
			"where fc.post_id =?1 and fc.created_by = ?2 and fc.is_deleted =0 and fc.is_reply2cmnt > 1 ", nativeQuery =true)
	List<Long> replyuserComments(Long postId, Long userId);
	
	
//	@Query(value=" with recursive cat_tree as (  " + 
//			"   select id, " + 
//			"          post_id, " + 
//			"          is_reply2cmnt, " + 
//			"          is_deleted " + 
//			"   from csdkms.forum_comment " + 
//			"    where id in ?1 and is_deleted = 0 " + 
//			"   union all " + 
//			"   select fc.id, " + 
//			"          fc.post_id , " + 
//			"          fc.is_reply2cmnt, " + 
//			"          fc.is_deleted " + 
//			"   from csdkms.forum_comment as fc " + 
//			"     join cat_tree as parent on fc.id = parent.is_reply2cmnt and fc.is_deleted = 0 " + 
//			" ) " + 
//			" select id " + 
//			" from cat_tree ", nativeQuery =true)
//	
	
	
	
	
	@Query (value = " with recursive cat_tree as ( "
			+ "select id , post_id, is_reply2cmnt, is_deleted "
			+ " from csdkms.forum_comment where id = ?1 and is_deleted = 0 "
			+ " union all "
			+ " select fc.id , fc.post_id, fc.is_reply2cmnt, "
			+ " fc.is_deleted from csdkms.forum_comment as fc "
			+ " join cat_tree as parent on fc.id = parent.is_reply2cmnt and fc.is_deleted = 0 ) "
			+ " select id from cat_tree option (maxrecursion 0) ",nativeQuery = true)
	List<Long> getParentCommentId(Long id);
	
	
	@Query(value = "select fc.id from forum_comment fc"
			+ " where fc.post_id = ?1 "
			+ " and fc.is_deleted =0 and is_reply2cmnt = 1"
			+ " order by created_at desc "
			, nativeQuery = true)
	List<Long> getTotalComment(Long postId);
	
	
	
	
	@Query(value="select bc.*, "
			+ " CASE WHEN isl.likes IS NOT NULL "
			+ " THEN 1 ELSE 0 "
			+ " END as liked, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes"
			+ " from forum_comment bc"
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from forum_like where func_id = 2 and is_deleted = 0 group by pkid"
			+ " ) l on bc.id = l.lid "
			+ " left join ( "
			+ "		select pkid as islid,count(id) as likes from forum_like where func_id = 2 and created_by = ?2 and is_deleted = 0 group by pkid"
			+ " ) isl on bc.id = isl.islid "
			+ " where post_id = ?1 and (is_reply2cmnt in ?3 or bc.id in ?3) and bc.is_deleted = 0 "
			+ " order by bc.created_at asc ", nativeQuery = true)	
	List<ForumCommentModel> getComment(Long postId, Long userId ,List<Long> limitCommentIds);
	
	
	@Query(value="select "
			+ " CASE WHEN isl.likes IS NOT NULL "
			+ " THEN 1 ELSE 0 "
			+ " END as liked, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes"
			+ " from forum_comment bc"
			+ " left join ( "
			+ "		select pkid as islid,count(id) as likes from forum_like where func_id = 2 and created_by = ?2 and is_deleted = 0 group by pkid"
			+ " ) isl on bc.id = isl.islid "
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from forum_like where func_id = 2 and is_deleted = 0 group by pkid"
			+ " ) l on bc.id = l.lid "
			+ " where post_id = ?1 and (is_reply2cmnt in ?3 or bc.id in ?3) and bc.is_deleted = 0 "
			+ " order by bc.created_at desc ", nativeQuery = true)
	List<Object[]> getCommentsLiked(Long postId, Long userId, List<Long> limitCommentIds);
	
	
	
	@Query (value = "select * from (select count(fc.id) as total from forum_comment fc "
			+ " where fc.post_id =?1 and fc.is_deleted = 0"
			+ " order by created_at desc ) a "
			+ " inner join ( "
			+ "		select count(fcp.id) as total_for_page from forum_comment fcp "
			+ "		where fcp.post_id = ?1 and fcp.is_deleted = 0 and fcp.is_reply2cmnt =1 "
			+ " ) f", nativeQuery = true)
	List<Object[]> getTotalComments(Long postId);
	
	
	@Query("Select fc from ForumCommentModel fc where fc.id =?1 and fc.postId =?2 and fc.isDeleted = 0 "
			+ " and fc.isReply2cmnt =1")
	Optional<ForumCommentModel> checkIsReply2cmnt(Long is_reply2cmnt, Long postId);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update ForumCommentModel fc set fc.isDeleted =1 "
			+ " where fc.id =?1 "
			+ " or fc.isReply2cmnt = ?1 ")
	void deleteAllsubs(Long firstLevelId);
	
	
}

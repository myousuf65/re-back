package com.hkmci.csdkms.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.News;


public interface NewsRepository extends JpaRepository<News, Long> {

	@Query(value = "select b.*,"
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name from newscorner2_post b"
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid"
			+ " ) l on b.id = l.lid"
			+ " left join ( "
			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id"
			+ " ) c on b.id = c.cid"
			+ " left join user u on u.id = b.original_creator"
			+ " where (b.category_id = ?1 or ?1 = 0) and b.is_deleted = 0 and b.is_public = 1 and b.id not in ?4"
			+ " ?5 ?6 limit ?2 , ?3" , nativeQuery = true)
	List<Object> searchNewsCorner2ByCategoryId(Long categoryId, Integer limitStart, Integer limitEnd, List<Long> latest_newscorner2_ids,String orderBy,String sortOrder);
	
	@Query(value = "select b.*,"
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, cu.fullname, cu.chinese_name, isl.is_liked, isb.is_bookmarked, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments"
			+ " from newscorner2_post b"
			+ " left join ( "  //left
			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid"
			+ " ) l on b.id = l.lid"
			+ " left join ( " //left
			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id"
			+ " ) c on b.id = c.cid"
			+ "  join ("
			+ " 	select count(id) as is_liked from newscorner2_like where func_id = 1 and pkid = ?1 and created_by = ?2 and is_deleted = 0"
			+ " ) isl "
			+ "  join ("
			+ " 	select count(id) as is_bookmarked from newscorner2_bookmark where post_id = ?1 and user_ref = ?2 and is_deleted = 0"
			+ " ) isb"
			+ " left join user u on u.id = b.original_creator"//left
			+ " left join user cu on cu.id = b.created_by" //left
			+ " where b.id = ?1 and b.is_deleted = 0 and (b.is_public = 1 or (b.original_creator = ?2 or b.created_by = ?2))  and b.publish_at < CURRENT_TIMESTAMP " , nativeQuery = true)
	Object getDetailsById(Long blogId, Long userId);
	
	@Query(value = "select b.*,"
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments"
			+ " from newscorner2_post b"
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid"
			+ " ) l on b.id = l.lid"
			+ " left join ( "
			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id"
			+ " ) c on b.id = c.cid"
			+ " left join user u on u.id = b.original_creator"
			+ " where (b.original_creator = ?1 or created_by = ?1) and b.is_deleted = 0"
			+ " order by b.created_at desc" , nativeQuery = true)
	List<Object> getMyPosts(Long userId);
	
	@Query(value = "select b.*,"
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name from newscorner2_post b"
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid"
			+ " ) l on b.id = l.lid"
			+ " left join ( "
			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id"
			+ " ) c on b.id = c.cid"
			+ " left join newscorner2_bookmark bb on bb.post_id = b.id"
			+ " left join user u on u.id = b.original_creator"
			+ " where bb.user_ref = ?1 and bb.is_deleted = 0"
			+ " order by b.created_at desc" , nativeQuery = true)
	List<Object> getMyBookmarks(Long userId);
	
	@Query(value = "select b.*, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes, u.fullname, u.chinese_name, "
			+ " CASE WHEN c.comments IS NOT NULL "
			+ " THEN c.comments ELSE 0 "
			+ " END as comments, "
			+ " bg.id as gallery_id, "
			+ " bg.gallery_name "
			+ " from newscorner2_post b "
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid "
			+ " ) l on b.id = l.lid "
			+ " left join ( "
			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id "
			+ " ) c on b.id = c.cid"
			+ " left join user u on u.id = b.original_creator "
			+ " left join newscorner2_gallery bg on bg.post_id = b.id "
			+ " where b.is_deleted = 0 and b.is_public = 1  and publish_at < CURRENT_TIMESTAMP "
			+ " order by publish_at desc, id desc limit 0 , 6" , nativeQuery = true)
	List<Object[]> searchNewsCorner2ByLatest();
	
	@Query(value = "select b.* from news b"
			+ " where b.is_deleted = 0 and b.is_public = 1  and publish_at < CURRENT_TIMESTAMP "
			+ " order by publish_at desc limit 0 , 10" , nativeQuery = true)
	List<News> getHomePage();
	
	Optional<NewsCorner2> findByIdAndIsDeleted(Long blogId,Integer isDeleted);
	
	
	
	
	@Query(value = "select b.* from newscorner2_post b " +
					"where b.category_id = (select bp.category_id from newscorner2_post bp where bp.id =?1 )" + 
					"and b.id != ?1 and is_public =1 and is_deleted =0 order by created_at desc limit 0,4 ", nativeQuery = true)
	List<NewsCorner2> getRelated(Long blogId);
	
	
	
	@Query(value = "select b.* from newscorner2_post b"
			+ " where b.id = ?1" , nativeQuery = true)//b.is_deleted = 0 and b.is_public = 1 and
	Optional<NewsCorner2> checkNewsCorner2Status(Long blogId);
	
	
	@Query(value = "select bpt.tag from newscorner2_post_tag bpt"
			+ " where bpt.is_deleted = 0 and bpt.post_id = ?1 "
			+ " order by bpt.id desc" , nativeQuery = true)
	List<String> getTagsById(Long postId);
	
	@Query("select b from NewsCorner2 b "
			+ " left join NewsCorner2CommentModel bc on bc.PostId = b.id where bc.id = ?1 and bc.IsDeleted = 0")
	Optional<NewsCorner2> getByCommentId(Long commentId);

	@Query("select b from NewsCorner2 b where b.id in ?1")
	List<NewsCorner2> getNewsCorner2ByIds(List<Long> newscorner2_id);
	
	
}

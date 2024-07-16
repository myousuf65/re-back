package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.BlogCommentModel;

public interface BlogCommentRepository extends JpaRepository<BlogCommentModel, Long> {
	
	@Query("select bc from BlogCommentModel bc where bc.PostId =?1 and bc.IsDeleted =0")
	List<BlogCommentModel> findByPostId(Long PostId);

	@Query(value="select bc.*, "
			+ " CASE WHEN isl.likes IS NOT NULL "
			+ " THEN 1 ELSE 0 "
			+ " END as liked, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes"
			+ " from blog_comment bc"
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from blog_like where func_id = 2 and is_deleted = 0 group by pkid"
			+ " ) l on bc.id = l.lid "
			+ " left join ( "
			+ "		select pkid as islid,count(id) as likes from blog_like where func_id = 2 and created_by = ?2 and is_deleted = 0 group by pkid"
			+ " ) isl on bc.id = isl.islid "
			+ " where post_id = ?1 and (is_reply2cmnt in ?3 or bc.id in ?3) and bc.is_deleted = 0 "
			+ " order by bc.created_at desc ", nativeQuery = true)
	List<BlogCommentModel> getComments(Long postId, Long userId,List<Long> limitCommentIds);
	
	@Query(value="select "
			+ " CASE WHEN isl.likes IS NOT NULL "
			+ " THEN 1 ELSE 0 "
			+ " END as liked, "
			+ " CASE WHEN l.likes IS NOT NULL "
			+ " THEN l.likes ELSE 0 "
			+ " END as likes"
			+ " from blog_comment bc"
			+ " left join ( "
			+ "		select pkid as islid,count(id) as likes from blog_like where func_id = 2 and created_by = ?2 and is_deleted = 0 group by pkid"
			+ " ) isl on bc.id = isl.islid "
			+ " left join ( "
			+ "		select pkid as lid,count(id) as likes from blog_like where func_id = 2 and is_deleted = 0 group by pkid"
			+ " ) l on bc.id = l.lid "
			+ " where post_id = ?1 and (is_reply2cmnt in ?3 or bc.id in ?3) and bc.is_deleted = 0 "
			+ " order by bc.created_at desc ", nativeQuery = true)
	List<Object[]> getCommentsLiked(Long postId, Long userId,List<Long> limitCommentIds);

	@Query(value="select bc.id from blog_comment bc "
			+ " where bc.post_id = ?1 "
			+ " and bc.is_deleted = 0 and is_reply2cmnt = 1"
			+ " order by created_at desc "
			+ " limit ?2 , ?3",nativeQuery = true)
	List<Long> getCommentsLevelOne(Long postId, Integer pageStart, Integer pageEnd);
	
	@Query(value="select * from (select count(bc.id) as total from blog_comment bc "
			+ " where bc.post_id = ?1 and bc.is_deleted = 0"
			+ " order by created_at desc) a"
			+ " inner join ("
			+ " 	select count(bcp.id) as total_for_page from blog_comment bcp"
			+ "		where bcp.post_id = ?1 and bcp.is_deleted = 0 and bcp.is_reply2cmnt = 1"
			+ " ) b",nativeQuery = true)
	List<Object[]> getTotalComments(Long postId);
	
	@Query("Select bc from BlogCommentModel bc where bc.id = ?1 and bc.PostId = ?2 and bc.IsDeleted = 0 and bc.IsRely2cmnt = 1")
	Optional<BlogCommentModel> checkIsReply2cmnt(Long is_reply2cmnt, Long postId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update BlogCommentModel bc set bc.IsDeleted = 1"
			+ " where bc.Id = ?1 "
			+ " or bc.IsRely2cmnt = ?1")
	void deleteAllsubs(Long firstLevelId);

}

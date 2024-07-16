package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ForumAdmin;

public interface ForumAdminRepository extends JpaRepository<ForumAdmin, Long> {


	
	@Query(value="select fa.user_id from forum_admin fa "
			+ " where fa.forum_cat_id =?1 and fa.is_deleted =0  group by fa.user_id",nativeQuery = true )
	public List<String> getAdminByCategoryId(Long categoryId);
	
	
	
	@Query(value ="select fa.user_id " + 
			"from forum_admin fa " + 
			"left join forum_post fp " + 
			"on fa.forum_cat_id = fp.forum_id " + 
			"left join forum_category fc " + 
			"on fc.id = fp.forum_id " + 
			"where fa.is_deleted = 0 and fp.is_deleted = 0 and fc.is_deleted =0 and fp.id = ?1 group by fa.user_id", nativeQuery = true)
	public List<Object> getPostCreaterAndAdmin(Long postId);
	
	
	
	@Query(value ="select fa.user_id " + 
			"from forum_admin fa " + 
			"left join forum_category fc " + 
			"on fa.forum_cat_id = fc.id  " + 
			"where fa.is_deleted = 0 and fc.is_deleted =0 and fc.id = ?1 group by fa.user_id", nativeQuery = true)
	public List<Object> getCategoryCreaterAndAdmin(Long CategoryId);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update forum_admin fa set fa.is_deleted = 1 , fa.deleted_by =?3 ,"
			+ " fa.deleted_at =?4 "
			+ " where fa.forum_cat_id = ?2 and fa.user_id in ?1 and fa.is_deleted =0 ",nativeQuery = true)
	void deleteAdmin(List<String> adminUpdate_delete, Long categoryId, Long deletedBy, Date deletedAt);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="INSERT INTO forum_admin (forum_cat_id,user_id, created_by,created_at,is_deleted) "
			+ " VALUES (?1 , ?2 , ?3 ,?4 ,0) ",nativeQuery = true)
	public void createAdmin(Long categoryId, String userId, Long created_by,Date created_at);
	
}

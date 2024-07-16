package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.ForumSpecialUserModel;

public interface ForumSpecialUserRepository extends JpaRepository<ForumSpecialUserModel, Long> {
	
	@Query(value = "select fsu.user_id from forum_special_user fsu "
			+ " where forum_cat_id =?1 and rule_right = ?2  and is_deleted =0", nativeQuery = true)
	public List<String> getSpecialUserRepository(Long categoryId, Integer rule);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="INSERT INTO forum_special_user (forum_cat_id , user_id, rule_right, created_at, created_by,is_deleted) "
			+ " VALUES (?1 ,?2 ,?3 ,?4, ?5, 0)",nativeQuery = true)
	public void createSpecialUser(Long categoryId, String userId, Integer rule, Date created_at, Long created_by);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update forum_special_user fsu set  fsu.is_deleted = 1 , fsu.deleted_by =?4, fsu.deleted_at =?5 "
			+ " where fsu.forum_cat_id =?2 and fsu.user_id in (?1) and fsu.rule_right = ?3 and fsu.is_deleted =0", nativeQuery = true)
	void deleteSpecialUser(List<String> user_delete, Long categoryId,Integer rule,Long deletedBy, Date deletedAt);
	
}

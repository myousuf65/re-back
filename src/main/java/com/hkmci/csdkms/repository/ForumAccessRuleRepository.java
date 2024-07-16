package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ForumAccessRule;

public interface ForumAccessRuleRepository extends JpaRepository<ForumAccessRule, Long> {

	@Query(value="select far.access_rule_id from forum_access_rule far "
			+ " where far.forum_cat_id = ?1  and far.rule_right = ?2 and far.is_deleted=0 ",nativeQuery = true)
	public List<Long> getAccessRule(Long categoryId , Integer rule);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update forum_access_rule far set far.is_deleted =1 , far.deleted_by =?4 , far.deleted_at =?5 "
			+ " where far.forum_cat_id=?2 and far.access_rule_id in ?1 and far.rule_right =?3 and far.is_deleted =0 ", nativeQuery = true)
	void deleteAccessRule(List<Long> accessRuleId, Long categoryId,Integer rule, Long deletedBy, Date deletedAt);
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update forum_access_rule far set far.is_deleted =1 , far.deleted_by =?3 , far.deleted_at =?4 "
			+ " where far.forum_cat_id = ?1  and far.rule_right =?2 and far.is_deleted =0 ", nativeQuery = true)
	void deleteAccessRule333( Long categoryId,Integer rule, Long deletedBy, Date deletedAt);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="INSERT INTO forum_access_rule (access_rule_id, forum_cat_id,rule_right,created_by, created_at,is_deleted) "
			+ " VALUES (?1 ,?2 ,?3, ?4 ,?5 ,0) ",nativeQuery = true)
	public void createAccessRule(Long accessRule, Long categoryId,Integer rule ,Long createdBy, Date createdAt);
 	
}

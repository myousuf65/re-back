package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.ForumSpecialUserGroupModel;
import com.hkmci.csdkms.model.ForumSpecialUserModel;

public interface ForumSpecialGroupRepository extends JpaRepository<ForumSpecialUserGroupModel, Long> {

	
	@Query(value = "select fsg.special_usergroup_id from forum_special_usergroup fsg "
			+ " where forum_cat_id = ?1 and rule_right = ?2 and is_deleted = 0 ", nativeQuery = true)
	public List<Integer> getSpecicalUserGroup(Long categoryId,Integer rule);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value =" INSERT INTO forum_special_usergroup (forum_cat_id , special_usergroup_id, rule_right, created_at, created_by , is_deleted ) "
				 + " VALUES (?1 , ?2 , ?3 , ?4 , ?5 , 0)  ", nativeQuery = true)
	public void createSpecialGroup (Long categoryId, Integer groupId, Integer rule ,Date created_at , Long created_by);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value ="update forum_special_usergroup fsg set fsg.is_deleted = 1, fsg.deleted_by = ?4, fsg.deleted_at = ?5 "
				+ " where fsg.forum_cat_id = ?2 and fsg.special_usergroup_id in (?1) and fsg.rule_right = ?3 and fsg.is_deleted = 0  " ,nativeQuery = true)
	void deleteSpecialGroup (List<Integer> special_group, Long categoryId, Integer rule ,Long deletedBy, Date deletedAt);





}

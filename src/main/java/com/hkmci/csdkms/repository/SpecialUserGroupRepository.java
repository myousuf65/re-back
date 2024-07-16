package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.SpecialUserGroupModel;

public interface SpecialUserGroupRepository extends JpaRepository<SpecialUserGroupModel, Long> {

	
	@Query(value = "select * from special_usergroup su "
			+ " left join special_user_group_user suu on su.id = suu.special_user_group_id "
			+ " where (Locate (?1, su.group_name) > 0 or ?1 ='') and ( su.id =?5 or ?5 = 0 )and "
			+ " (suu.user_id  = ?2 or ?2 = 0 ) and su.is_deleted = 0 "
			+ " group by su.id "
			+ " limit ?3,?4  ", nativeQuery = true)
	List<SpecialUserGroupModel> search(String groupName, String staffNo, Integer start ,Integer end, Long groupId);

	
	@Query(value =" select * from special_usergroup su where is_deleted = 0", nativeQuery = true )
	List<SpecialUserGroupModel> getAll();
	
	
	@Query(value =" select * from special_usergroup su left join user u  "
			+ " on u.id = su.created_by "
			+ " where u.institution_id = ?1 and u.section_id = ?2  and su.is_deleted = 0 ", nativeQuery = true )
	List<SpecialUserGroupModel> getSameInstAndSession(Long instId, Long sectionId);
	//List<SpecialUserGroupModel> findAllAndIsDelete(int i);
	
	@Query(value= "select su.id from special_usergroup su "
			+ "left join forum_special_usergroup fsu on su.id = fsu.special_usergroup_id "
			+ "where su.is_deleted = 0 and fsu.is_deleted = 0  and fsu.forum_cat_id = ?1 and fsu.rule_right =?2 ", nativeQuery = true)
	List<Integer> getSpecialGroupByForumId(Long forumId, Integer rule );
	
	
	
	@Query(value ="select su.* from special_usergroup su "
			+ " left join resource_special_group rsu on rsu.special_group_id = su.id "
			+ " where rsu.resource_id = ?1 and rsu.is_deleted = 0 and su.is_deleted =0  ", nativeQuery = true)
	List<SpecialUserGroupModel> getByResourceIds(Long resrouceId);



}

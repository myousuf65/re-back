package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserAccessRule;

/**
 * author: Holfer
 * date: 2019/03/21
 * time: 5:54
 * version: 1.0
 * description:
 */
public interface UserRepository extends JpaRepository<User, Long> {
//	@Query("select u from user u where deleted = 0")
	List<User> findByIsDeleted(int isDeleted);
	
	Optional<User> findByIdAndIsDeleted(Long id, int isDeleted);
	
	@Query(value = "select u.* from user u where "
			+ " u.approved =1 and u.is_deleted = 0 and u.institution_id in ?1 "
			+ " and ( section_r is null or section_r <> ?2 or section_r <> ?3 )",nativeQuery = true)
	List<User> totalUser(List<Long> instId,String section1, String section2);
	
	@Query("Select u.score from User u where u.id = ?1 and u.isDeleted =?2")
	Integer findScoreByIdAndIsDeleted(Long id, int i);
	
	
	@Query("Select u.id from User u where u.isDeleted =?1 ")
	List<Long> findIdByIsDeleted(int isDeleted);
	
	
	@Query("Select u from User u where u.isDeleted = ?1 ")
	List<User> findAllByIsDeleted(Integer isDeleted);
	
	
	@Query("Select u from User u where u.staffNo in ?1 and  u.isDeleted =0 ")
	List<User> getByUserId(List<String> userId);
	
	@Query("Select u.todayScore from User u where u.id = ?1 and u.isDeleted =?2  ")
	Integer  findTodayScoreByIdAndIsDeleted(Long id , int i);
	
	@Query("Select u from User u "
			+ " left join BlogCommentModel bc on bc.CreatedBy = u.id"
			+ " where bc.IsDeleted = 0 "
			+ " and bc.id = ?2 "
			+ " and ( u.id = ?1 or u.id in ?3)")
	Optional<User> checkDeletePermissionById(Long userId, Long commentId, List<Long> adminIds);
	
	List<User> findByFullnameAndIsDeleted(String name, int IsDeleted);
	
	@Query(value = "select u.* from user u where "
			+ " u.staff_no = ?1  and u.is_deleted = ?2 and (section_r not like ?3 or section_r is null)  "
			,nativeQuery = true)
	User findByStaffNoAndIsDeleted(String staffNo, Integer isDeleted, String INTERDICT);

	Optional<User> findByUsernameAndIsDeleted(String username, Integer isDeleted);

	
	
	@Query(value= "select u.* from user u "
			+ " where u.is_deleted = 0 and u.email IS NOT NULL "
			+ " and (Locate(?1,u.fullname) > 0 or ?1 = '') "
			+ " order by u.created_at desc "
			,nativeQuery = true)
	List<User> findByUserNameAndSearch(String fullname);
	
	
	@Query(value = "select u.* from user u "
			+ " left join usergroup ug on u.usergroup = ug.id"
			+ " left join institutions inst on inst.id = u.institution_id"
			+ " left join sections sect on sect.id = u.section_id"
			+ " left join ranks on ranks.id = u.rank_id"
			+ " where u.is_deleted = 0 and (u.id in ?1 or ?11 = 1 or ?12 =1 )" //and ar.id in ?1 or 0 = 0 and ar.id in ?1 or  < and (u.id in ?1 or ?11 = 1) >
			+ " and (Locate(?2,u.staff_no) > 0 or ?2 = '')" // u.staff_no like concat('%', ?2 ,'%')
			+ " and (Locate(?3,u.fullname) > 0 or ?3 = '')"
			+ " and (ug.id = ?4 or ?4 = 0)"
			+ " and (Locate(?5,inst.inst_name) > 0 or ?5 = '')" //inst.inst_name like concat('%', ?5 ,'%')
			+ " and (Locate(?6,ranks.rank_name) > 0 or ?6 = '')" //ranks.rank_name like concat('%', ?6 ,'%')
			+ " and (Locate(?7,sect.section_name) > 0 or ?7 = '')" //sect.section_name like concat('%', ?7 ,'%')
			+ " and (u.blog_create = ?8 or ?8 = '-1')"
			+ " and u.delflag is null "
			+ " order by u.created_at desc"
			+ " limit ?9 , ?10",
			nativeQuery = true)
	List<User> findByUserIdAndSearch(List<Long> accessRuleId, String staffNo, String fullname, Long groupName,
			String institution, String rank, String section, Long isBlogger, Integer start_num, Integer end_num,
			Integer is_admin, Integer is_special);
	
	@Query(value = "select u.* from user u "
			+ " left join usergroup ug on u.usergroup = ug.id"
			+ " left join institutions inst on inst.id = u.institution_id"
			+ " left join sections sect on sect.id = u.section_id"
			+ " left join ranks on ranks.id = u.rank_id"
			+ " where u.is_deleted = 1 and (u.id in ?1 or ?11 = 1 or ?12 =1 )" //and ar.id in ?1 or 0 = 0 and ar.id in ?1 or  < and (u.id in ?1 or ?11 = 1) >
			+ " and (Locate(?2,u.staff_no) > 0 or ?2 = '')" // u.staff_no like concat('%', ?2 ,'%')
			+ " and (Locate(?3,u.fullname) > 0 or ?3 = '')"
			+ " and (ug.id = ?4 or ?4 = 0)"
			+ " and (Locate(?5,inst.inst_name) > 0 or '' = '')" //inst.inst_name like concat('%', ?5 ,'%')
			+ " and (Locate(?6,ranks.rank_name) > 0 or ?6 = '')" //ranks.rank_name like concat('%', ?6 ,'%')
			+ " and (Locate(?7,sect.section_name) > 0 or ?7 = '')" //sect.section_name like concat('%', ?7 ,'%')
			+ " and (u.blog_create = ?8 or ?8 = '-1')"
			+ "	and u.delflag is not null "
			+ " order by u.created_at desc"
			+ " limit ?9 , ?10",
			nativeQuery = true)
	List<User> findByDeletedUserIdAndSearch(List<Long> accessRuleId, String staffNo, String fullname, Long groupName,
			String institution, String rank, String section, Long isBlogger, Integer start_num, Integer end_num,
			Integer is_admin, Integer is_special);
	
	@Query(value = "select u.* from user u "
			+ " left join usergroup ug on u.usergroup = ug.id"
			+ " left join institutions inst on inst.id = u.institution_id"
			+ " left join sections sect on sect.id = u.section_id"
			+ " left join ranks on ranks.id = u.rank_id"
			+ " where  (u.id in ?1 or ?11 = 1 or ?12 =1 )" //and ar.id in ?1 or 0 = 0 and ar.id in ?1 or  < and (u.id in ?1 or ?11 = 1) >
			+ " and (Locate(?2,u.staff_no) > 0 or ?2 = '')" // u.staff_no like concat('%', ?2 ,'%')
			+ " and (Locate(?3,u.fullname) > 0 or ?3 = '')"
			+ " and (ug.id = ?4 or ?4 = 0)"
			+ " and (Locate(?5,inst.inst_name) > 0 or '' = '')" //inst.inst_name like concat('%', ?5 ,'%')
			+ " and (Locate(?6,ranks.rank_name) > 0 or ?6 = '')" //ranks.rank_name like concat('%', ?6 ,'%')
			+ " and (Locate(?7,sect.section_name) > 0 or ?7 = '')" //sect.section_name like concat('%', ?7 ,'%')
			+ " and (u.blog_create = ?8 or ?8 = '-1')"
			+ " "
			+ " order by u.created_at desc"
			+ " limit ?9 , ?10",
			nativeQuery = true)
	List<User> findByUserNameAndStaffAndSearch(List<Long> accessRuleId, String staffNo, String fullname, Long groupName,
			String institution, String rank, String section, Long isBlogger, Integer start_num, Integer end_num,
			Integer is_admin, Integer is_special);
	
	@Query(value="select u.* from user u"
			+ " left join usergroup ug on u.usergroup = ug.id"
			+ " left join institutions inst on inst.id = u.institution_id"
			+ " left join sections sect on sect.id = u.section_id"
			+ " left join ranks on ranks.id = u.rank_id"
			+ " where ug.id = :groupNameId or :groupNameId = 0"
			,
			nativeQuery = true)
	List<User> findByGroupNameId(Long groupNameId);
	
	
	@Query(value = " select * from ( "
				 + " 	select user_id from admin_list_temp where is_deleted = 0 "
				 + " ) as temp_list union all"
				 + " ("
				 + " 	select id from user where usergroup in (4,5,6) and is_deleted = 0 "
				 + " ) ", nativeQuery = true)
	List<Integer> getAdminList();

	@Query(value = "select count(id) as total_num from (select u.id from user u "
			+ " left join usergroup ug on u.usergroup = ug.id"
			+ " left join institutions inst on inst.id = u.institution_id"
			+ " left join sections sect on sect.id = u.section_id"
			+ " left join ranks on ranks.id = u.rank_id"
			+ " where u.is_deleted = 0 and (u.id in ?1 or ?9 = 1)" //and ar.id in ?1 or 0 = 0 and ar.id in ?1 or  < and (u.id in ?1 or ?11 = 1) >
			+ " and (Locate(?2,u.staff_no) > 0 or ?2 = '')" // u.staff_no like concat('%', ?2 ,'%')
			+ " and (Locate(?3,u.fullname) > 0 or ?3 = '')"
			+ " and (ug.id = ?4 or ?4 = 0)"
			+ " and (Locate(?5,inst.inst_name) > 0 or ?5 = '')" //inst.inst_name like concat('%', ?5 ,'%')
			+ " and (Locate(?6,ranks.rank_name) > 0 or ?6 = '')" //ranks.rank_name like concat('%', ?6 ,'%')
			+ " and (Locate(?7,sect.section_name) > 0 or ?7 = '')" //sect.section_name like concat('%', ?7 ,'%')
			+ " and (u.blog_create = ?8 or ?8 = '-1')"
			+ " order by u.created_at desc) as a",
			nativeQuery = true)
	Integer getTotal(List<Long> accessRuleId, String staffNo, String fullname, Long groupNameId, String institution,
			String rank, String section, Long isBlogger, Integer is_admin);
	
	
	@Query(value = "select count(id) as total_num from (select u.id from user u "
			+ " left join usergroup ug on u.usergroup = ug.id"
			+ " left join institutions inst on inst.id = u.institution_id"
			+ " left join sections sect on sect.id = u.section_id"
			+ " left join ranks on ranks.id = u.rank_id"
			+ " where u.is_deleted = 0 and (u.id in ?1 or ?9 = 1)" //and ar.id in ?1 or 0 = 0 and ar.id in ?1 or  < and (u.id in ?1 or ?11 = 1) >
			+ " and (Locate(?2,u.staff_no) > 0 or ?2 = '')" // u.staff_no like concat('%', ?2 ,'%')
			+ " and (Locate(?3,u.fullname) > 0 or ?3 = '')"
			+ " and (ug.id = ?4 or ?4 = 0)"
			+ " and (Locate(?5,inst.inst_name) > 0 or ''= '')" //inst.inst_name like concat('%', ?5 ,'%')
			+ " and (Locate(?6,ranks.rank_name) > 0 or ?6 = '')" //ranks.rank_name like concat('%', ?6 ,'%')
			+ " and (Locate(?7,sect.section_name) > 0 or ?7 = '')" //sect.section_name like concat('%', ?7 ,'%')
			+ " and (u.blog_create = ?8 or ?8 = '-1') "
			+ " and u.delflag is not null "
			+ " order by u.created_at desc) as a",
			nativeQuery = true)
	Integer getDeleteTotal(List<Long> accessRuleId, String staffNo, String fullname, Long groupNameId, String institution,
			String rank, String section, Long isBlogger, Integer is_admin);
	
	
	
	@Query(value="select count(id) as total_num from "
			+ " (select u.id from user u where u.is_deleted = 0 and (Locate(?1,u.fullname) > 0 or ?1 = '') order by u.created_at desc ) as a ", nativeQuery =true)
	Integer getTotalByName(String staffNo);
	
	@Query(value ="select u.usergroup From user u where u.id =?1", nativeQuery = true)
	Long getUsergroup(Long id);

	@Query(value ="select u.usergroup , u.institutionId from User u where u.id =?1 ")
	List<Object []> getUserGroupAndInst(Long id);
	
	
	
	
	@Query("Select uar from UserAccessRule uar where uar.userId = ?1 ")
	List<UserAccessRule> findByUserId(Long userId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update UserAccessRule uar set "
			+ " uar.isDeleted = 0 "
			+ " where uar.isDeleted = 1 "
			+ " and uar.userId = ?2 "
			+ " and uar.accessRuleId in ?1")
	void updateAllAccessRule(List<Long> toUpdate, Long userId);
	
	
	@Query(value="Select u.* FROM user u where u.fullname like %?1%  limit ?2 ,?3 ", nativeQuery = true)
	List<User> findByStaffName(String asText, Integer limitStart, Integer limitEnd);
	
//	@Query(value = "select u.* from csdkms.user u where "
//			+ " u.username = ?1  and u.is_deleted = ?2 and (u.section_r not like ?3 or u.section_r is null) "
//			,nativeQuery = true)
	@Query(value = "select u.* from csdkms_prod.user u where  u.username = \"ben\" "
	,nativeQuery = true)
	User findByUsernameAndIsDeleted(String username, Integer i, String INTERDICT);
	
	
	
	@Query(value = "select h.user_id from csdkms_log.hrms h where h.staff_no = ?1 ", nativeQuery = true)
	List<Long> test2db(String staff_no);

	@Query(value = "Select u.* From User u where u.delflag is null and is_deleted = 0  ",nativeQuery = true)
	List<User> getAllExist();
	
	
	@Query(value ="Select u From User u  ")
	List<User> getAll();
	
	
//	@Query(value =" select ss_rankabbr, fullname ,ss_title,ss_inst,ss_sect, ss_duty, ss_actMarks, ss_actdate, ss_actcease, ss_actnature\n" + 
//			" from csdkms.user where ss_disciplined = ?1 order by ss_inst", nativeQuery = true)
//	List<Object []> getSeniorOfficeList(Integer disciplined);

	
	@Query(value ="select ss_division,  ss_rankabbr as ss_rankabbr, ss_title, fullname, ss_inst, ss_sect, ss_duty, " + 
			"ss_actMarks, ss_actdate , ss_actcease from csdkms.user " + 
			"where ss_disciplined =?1 and ss_inst is not null and is_deleted =0 " + 
			"order by ss_division, case when ss_seq is null then 1 else 0 end asc,ss_seq asc,ss_inst,ss_duty,fullname ",nativeQuery = true)
	List<Object []> getSeniorOfficeList(Integer disciplined);
	
	
	@Query(value ="select ss_division,  ss_rankabbr as ss_rankabbr, ss_title, fullname, ss_inst, ss_sect, ss_duty, " + 
			"ss_actMarks, ss_actdate , ss_actcease from csdkms.user " + 
			"where ss_disciplined =?1 and ss_inst is not null and is_deleted =0 " + 
			"order by ss_division,ss_seq,ss_inst,ss_duty,fullname ",nativeQuery = true)
	List<Object []> getSeniorOfficeListCliv(Integer disciplined);

	@Query(value ="Select * from user where id = :user_id" ,nativeQuery = true)
	List<User> findListById(Long user_id);
	
}

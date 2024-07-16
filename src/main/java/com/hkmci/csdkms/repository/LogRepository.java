package com.hkmci.csdkms.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

	@Query("Select l from Log l "
			+ " where l.logtype = 1 and l.tableId =1 "
			+ " and l.createdAt >= ?2"
			+ " and l.createdAt <= ?3"
			+ " and (l.urankId = ?1 or ?1 = 0)"
			+ " order by l.createdAt desc")
	List<Log> getByRank(Long rankId, Date startDate, Date endDate);
	
	@Query("Select l from Log l "
			//+ " where l.logtype = 2"
			+ " where l.createdAt >= ?2"
			+ " and l.createdAt <= ?3"
			+ " and (l.urankId = ?1 or ?1 = 0)"
			+ " and (l.uinstId in ?4 or ?5=0 )"
			+ " order by l.createdAt desc")
	List<Log> getViewPageByRank(Long rankId, Date startDate, Date endDate, List<Long> instIdList,Integer instId);

	@Query("Select l from Log l "
			//+ " where l.logtype = 2"
			+"where l.createdAt >= ?2"
			//+ " and l.createdAt >= ?2"
			+ " and l.createdAt <= ?3"
			+ " and l.staffNo = ?1 "
			+ " order by l.createdAt desc")
	List<Log> getRankDetailByUser(String staffNo, Date startDate, Date endDate);

	@Query("Select l from Log l "
			+ " where l.logtype = 1 "
			+ " and l.createdAt >= ?2 "
			+ " and l.createdAt <= ?3 "
			+ " and l.staffNo = ?1 "
			+ " and l.channel in ?4 "
			+ " order by l.createdAt desc")
	List<Log> getRankLoginByUser(String staffNo, Date startDate, Date endDate ,Integer channel);

	@Query("Select l from Log l "
			+ " where l.logtype = 1 and l.tableId =1 "
			+ " and l.createdAt >= ?2"
			+ " and l.createdAt <= ?3"
			+ " and (l.uinstId in ?1 or ?4 = 0) "
			+ " order by l.createdAt desc")
	List<Log> getByInst(List<Long> instId, Date startDate, Date endDate,Integer all);
	
	

	@Query("Select l from Log l "
			//+ " where l.logtype = 2"
			+ " where l.createdAt >= ?2"
			+ " and l.createdAt <= ?3"
			+ " and (l.uinstId in ?1 or ?4 = 0)"
			+ " order by l.createdAt desc")
	List<Log> getViewPageByInst(List<Long> instId, Date startDate, Date endDate,Integer all );

	@Query("Select l from Log l "
			//+ " where l.logtype = 2"
			+ " where l.createdAt >= ?2"
			+ " and l.createdAt <= ?3"
			+ " and (l.uinstId in ?1 or ?4 =0) "
			+ " order by l.createdAt desc")
	List<Log> getInstDetail(List<Long> target_instId, Date startDate, Date endDate, Integer all );

	@Query("Select l from Log l "
			+ " where l.logtype = 1 and l.tableId =1  "
			+ " and l.createdAt >= ?2 "
			+ " and l.createdAt <= ?3 "
			+ " and (l.uinstId in ?1 or ?6 = 0)  "
			+ " and l.channel in ?4  and l.result = ?5 "
			+ " order by l.createdAt desc")
	List<Log> getInstLoginByUser(List<Long> target_instId, Date startDate, Date endDate, List<Integer> report_channel, String success,Integer All);

	@Query(value = " Select l from Log l "
				 + " where staffNo = ?1 "
				 + " and l.createdAt >= ?2 "
				 + " and l.createdAt <= ?3 "
				 + " order by l.createdAt desc")
	List<Log> getByStaffNo(String staffNo, Date startDate, Date endDate);

	@Query(value = 
			" select log.* from "
		  + " ("
		  + "	select * from log l "
		  + " 	where l.created_at >= ?2 "
		  + " 	and l.created_at <= ?3 "
		  + " ) as log,"
		  + " ("
		  + "	select * from user u where Locate( ?1, u.fullname ) > 0"
		  + " ) as user"
		  + " where log.staff_no = user.staff_no", nativeQuery = true)
	List<Log> getByFullname(String fullname, Date startDate, Date endDate);

	@Query(value = " Select l from Log l "
			 + " where staffNo = ?1 "
			 + " and l.createdAt >= ?2 "
			 + " and l.createdAt <= ?3 "
			 + " and l.channel in ?4"
			 + " and l.logtype = 1"
			 + " and l.result = ?5"
			 + " order by l.createdAt desc")
	List<Log> getLoginByStaffNo(String staffNo, Date startDate, Date endDate, List<Integer> report_channel, String Success);

	@Query(value = " Select l from Log l "
			 + " where staffNo = ?1 "
			 + " and l.createdAt >= ?2 "
			 + " and l.createdAt <= ?3 "
			 + " and l.logtype = 2"
			 + " order by l.createdAt desc")
	List<Log> getViewByStaffNo(String staffNo, Date startDate, Date endDate);

	@Query(value = " Select l from Log l "
			 + " where staffNo = ?1 "
			 + " and l.createdAt >= ?2 "
			 + " and l.createdAt <= ?3 "
			 + " and l.logtype = 2 "
			 + " and l.tableId = 2 "
			 + " order by l.createdAt desc")
	List<Log> getResourceByStaffNo(String staffNo, Date startDate, Date endDate);

	@Query(value = " Select l from Log l "
			 + " where l.createdAt >= ?1 "
			 + " and l.createdAt <= ?2 "
			 + " and l.tableId = 2 "
			 + " order by l.createdAt desc")
	List<Log> getResourceByDate(Date startDate, Date endDate);

	@Query(value = " Select l from Log l "
			 + " where l.createdAt >= ?2 "
			 + " and l.createdAt <= ?3 "
			 + " and l.logtype = ?4 "
			 + " and l.tableId = 2 "
			 + " and l.pkid = ?1"
			 + " order by l.createdAt desc")
	List<Log> getResourceLogByDate(Long resourcceId, Date startDate, Date endDate, Long logtype);

	@Query(value = " Select l from Log l "
			 + " where ((l.categoryId = ?1 and ?1 <> '0') or (?1 = '0' and l.categoryId <> '0'))"
			 + " and l.createdAt >= ?2 "
			 + " and l.createdAt <= ?3 "
			 + " and l.tableId > 2 "
			 + " order by l.createdAt desc")
	List<Log> getBlogByCategory(String categoryId, Date startDate, Date endDate);

	@Query(value = " Select l from Log l "
			 + " where l.pkid = ?1 "
			 + " and l.createdAt >= ?2 "
			 + " and l.createdAt <= ?3 "
			 + " and l.logtype = ?4 "
			 + " and l.tableId > 2 "
			 + " order by l.createdAt desc")
	List<Log> getBlogDetailById(Long blogId, Date startDate, Date endDate, Long logtype);
	
	
	@Query(value = " Select count(l.id) from Log l where l.uinstId=?1 and l.urankId = ?2 and l.createdAt >= ?3 and l.createdAt <= ?4 group by l.createdBy ")
	List<Integer> getRankLoginStaff(Long instId, Long rankId, Date start , Date end);
	
	@Query("Select count(l.id) from Log l where l.createdBy = ?1 and l.tableId =?2 and l.logtype =?3 and l.createdAt >= ?4 ")
	List<Long> getLoginTime(Long userId ,Long tableId, Long logtype , Date startDate);
	
	@Query("Select count(l.id), l.pkid from Log l where l.tableId =2 and l.logtype =2 and l.createdAt >= ?1 " + 
			" and l.createdAt <=?2 group by l.pkid")
	List<Object []> getResourceHitRate(Date start , Date end);
	
	@Query(value = "Select l.remark from Log l where l.table_id = 17 and created_by = ?3 and logtype_id =?2 and l.remark =?1 limit 1 ",nativeQuery = true)
	String findMobileAppVersion(String remark, Integer pkid, Long createdBy);
	
}

package com.hkmci.csdkms.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ScoreLog;

public interface ScoreLogRepository extends JpaRepository<ScoreLog, Long> {

	@Query("Select sl.score as score from ScoreLog sl where sl.userId = ?1 and sl.createdAt >=?2")
	List<Integer> getUserScore(Long userId, Date startDate);
	
	@Query("Select sl.score as score from ScoreLog sl where sl.userId = ?1 and sl.createdAt >=?2 and ?2< sl.createdAt  ")
	List<Integer> getUserTodayScore (Long userId, Date startDate);
	
	

	@Query(value="Select count(sl.id) as score from score_log sl  inner join "
			+ "log l on sl.user_id = ?1 and l.id = sl.log_id and l.table_id =1 and l.logtype_id =1  and sl.created_at >= ?2 ", nativeQuery=true)
	Integer getUserLoginTimes(Long userId, Date startDate);


	
//	@Query(value = "Select sum(sl.score),l.uinst_id from score_log sl "
//			+ "inner join log l "
//			+ "on sl.log_id = l.id and (l.uinst_id = ?1 or ?1 = 0) "
//			+ "and l.created_at >= ?2 "
//			+ "and l.created_at <= ?3 " , nativeQuery = true)
	
	
	@Query("Select sl from ScoreLog sl where (uinstId = ?1 or ?1 = 0) and "
			+ " created_at >= ?2 and created_at <= ?3  ")
	List<ScoreLog> getScoreReport(Long instId, Date startDate , Date endDate);
	
	
	
	
	@Query(value = "SELECT  sl.id,sl.created_at, sl.score, sl.uinst_id, sl.user_id, u.institution_id " + 
			"FROM score_log sl " + 
			"left join user u " + 
			"on sl.user_id = u.id " +
			"where (u.institution_id = ?1 or ?1 =0) " + 
			"and sl.created_at>?2 and sl.created_at< ?3 group by sl.id ", nativeQuery=true)
	List<Object[]> getScoreReport1(Long instId, Date startDate, Date endDate);
	
	@Query("Select sl from ScoreLog sl where userId = ?1 and "
			+ " created_at >= ?2 and created_at <= ?3 ")
	ScoreLog getScoreDetail(Long staff ,Date startDate , Date endDate);

	
	
	
	@Query(value = "SELECT  sl.id,sl.created_at, sl.score, sl.uinst_id, sl.user_id, u.institution_id , "
			+ " u.section_id, u.rank_id, u.staff_no " + 
			"FROM score_log sl " + 
			"left join user u " + 
			"on sl.user_id = u.id " +
			"where u.id = ?1 " + 
			"and sl.created_at>=?2 and sl.created_at< ?3 ", nativeQuery=true)
	List<Object[]> getScoreDetail1(Long staff, Date startDate, Date endDate);
	
	@Query(value = "SELECT  sl.id,sl.created_at, sl.score, sl.uinst_id, sl.user_id, u.institution_id , "
			+ " u.section_id, u.rank_id, u.staff_no " + 
			"FROM score_log sl " + 
			"left join user u " + 
			"on sl.user_id = u.id " +
			"where u.staff_no = ?1 " + 
			"and sl.created_at>?2 and sl.created_at<= ?3 ", nativeQuery=true)
	List<Object[]> getScoreDetail2(String staff, Date startDate, Date endDate);
	
	
	@Query ("select sl from ScoreLog sl where userId = ?1 and "
			+ "created_at >= ?2 and created_at <= ?3 order by created_at desc")
	List<ScoreLog> getUserMenuScore(Long userId ,Date startDate , Date endDate);
	
	
	@Query(value = "select DATE_FORMAT(created_at,'%Y%m%d') days,staff_no ,sum(score) " + 
			"from csdkms.score_log where user_id = ?1 and created_at > ?2 and created_at < ?3  group by days", nativeQuery=true)
	List<Object []> getUserMenuScore2(Long userId, Date startDate, Date endDate);
	
	
	@Query(value = "select DATE_FORMAT(created_at,'%Y%m%d') days,staff_no ,sum(score) " + 
			"from csdkms.score_log where user_id = ?1 and created_at > ?2 and created_at < ?3  ", nativeQuery=true)
	List<Object []> getUserMenuScore2ByPeriod(Long userId, Date startDate, Date endDate);
	
	
	
	
}

package com.hkmci.csdkms.report;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;


public interface ReportService {

	public List<Log> findAll();
	
	public Log newLog(Log log);
	
	public Optional<Log> findById(Long id);

	public List<Log> getByRank(Long rankId, List<Long> instId, Date startDate, Date endDate);

	public List<Log> getViewPageByRank(Long rankId, List<Long> instIdList, Date startDate, Date endDate, Long instId);

	public List<Log> getRankDetailByUser(String target_staff_no, Date startDate, Date endDate);

	public List<Log> getRankLoginByUser(String target_staff_no, Date startDate, Date endDate, Integer channel);

	public List<Log> getByInst(List<Long> instId, Date startDate, Date endDate);

	public List<Log> getViewPageByInst(List<Long> instId, Date startDate, Date endDate);

	public List<Log> getInstDetail(List<Long> target_instId, Date startDate, Date endDate);

	public List<Log> getInstLoginByUser(List<Long> target_instId, Date startDate, Date endDate, List<Integer> report_channel);

	public List<Log> getByStaffInfo(String staffNo, String fullname, Date startDate, Date endDate);

	public List<Log> getLoginByStaffNo(String staffNo, Date startDate, Date endDate, List<Integer> report_channel);

	public List<Log> getViewByStaffNo(String staffNo, Date startDate, Date endDate);

	public List<Log> getResourceByStaffNo(String staffNo, Date startDate, Date endDate);

	public List<HashMap<String,Object>> getResourceForReport(Long km, Long ks, Long wg, List<Long> subcategory_list, List<Long> resource_list);

	public List<Log> getResourceByDate(Date startDate, Date endDate);

	public List<Log> getResourceLogByDate(Long resourceId, Date startDate, Date endDate, Integer action);

	public List<User> getResourceTotalUserByDate(Long resourceId,List<User> user_list_session, List<AccessRule> resource_access_rule);

	public List<Log> getBlogByCategory(Long categoryId, Date startDate, Date endDate);

	public List<Blog> getBlogByIds(List<Long> blog_id);

	public List<Log> getBlogDetailById(Long blogId, Date startDate, Date endDate, Integer report_type);

	public List<ScoreLog> getScoreReport(Long instId,Date startDate, Date endDate);

	public ScoreLog getScoreDetail(String staff, Date startDate, Date endDate);
	
	public List<Object[]> getUserMenuScore(Long user_id, Date startDate, Date endDate);

	public List<Object[]> getScoreReport1(List<Long> instId, Date startDate, Date endDate);

	public List<ScoreLog> getScoreDetailList(Collection<Integer> values, Date startDate, Date endDate);

	ScoreLog getScoreDetail(Long userId, Date startDate, Date endDate);

	List<Object[]> getUserMenuScoreByPeriod(Long userId, Date startDate, Date endDate);


	Integer getRankLoginStaff(Long instId, Long rankId, Date startDate, Date endDate);



	
}

 package com.hkmci.csdkms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.repository.LogRepository;
import com.hkmci.csdkms.repository.ScoreLogRepository;



@Service
public class LogServiceImpl implements LogService {

	
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	private LogRepository logRepository;
	
	@Autowired
	private ScoreLogRepository scoreLogRepository;
	
	
	@Override
	public List<Log> findAll() {
		return logRepository.findAll();
	}

	@Override
	public Log newLog(Log log){
		return logRepository.saveAndFlush(log);
	}
	
	@Override
	public Optional<Log> findById(Long id){
		return logRepository.findById(id);
	}

	@Override
	public Log mobiledownload(User user, Long logType, String remark, String result, Integer channel) {
		Long table_id = 17L;
		Long pkid = 0L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
		
	}
	
	
	@Override
	public Log login(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 1L;
		Long logType = 1L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}

	@Override
	public Log viewResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category) {
		Long table_id = 2L;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(getCategoryId(category));
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}

	@Override
	public Log downloadResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category) {
		Long table_id = 2L;
		Long logType = 3L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(getCategoryId(category));
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log shareResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category) {
		Long table_id = 2L;
		Long logType = 17L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(getCategoryId(category));
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override 
	public Log createResource(User user, Long pkid , String remark, String result ,Integer channel ,List<Long> category) {
		
		Long table_id = 2L;
		Long logType = 4L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		
		List<Integer> categoryId = new ArrayList<>();
		
		
		for(Integer i =0 ; i< category.size();i++) {
			Integer id = Integer.valueOf(category.get(i).toString()) ;
			categoryId.add(id);
		}
		loginLog.setCategoryId(getCategoryId(categoryId));
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override 
	public Log editResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category) {
		Long table_id = 2L;
		Long logType = 5L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(getCategoryId(category));
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override 
	public Log deleteResource(User user , Long pkid , String remark, String result, Integer channel, List<Integer> category) {
		Long table_id = 2L;
		Long logType = 6L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(getCategoryId(category));
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	
	@Override
	public Log createBlog(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 4L;
		Long logType = 4L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}

	@Override
	public Log updateBlog(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 5L;
		Long logType = 5L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}

	@Override
	public Log deleteBlog(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 6L;
		Long logType = 6L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log viewBlog(User user, Long pkid, String remark, String result, Integer channel, Integer category) {
		Long table_id = 7L;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log shareBlog(User user, Long pkid, String remark, String result, Integer channel, Integer category) {
		Long table_id = 9L;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	
	
	@Override
	public Log viewBlogList(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 8L;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log createBlogGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 9L;
		Long logType = 9L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log createElearningGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 9L;
		Long logType = 9L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log uploadBlogGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 10L;
		Long logType = 10L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log uploadElearningGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 10L;
		Long logType = 10L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log updateBlogGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 11L;
		Long logType = 11L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log updateElearningGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 11L;
		Long logType = 11L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log deleteBlogGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 12L;
		Long logType = 12L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log deleteElearningGallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 12L;
		Long logType = 12L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log likeBlog(User user, Long pkid, String remark, String result, Integer channel,Long category) {
		Long table_id = 13L;
		Long logType = 13L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log commentBlog(User user, Long pkid, String remark, String result, Integer channel,Long category) {
		Long table_id = 14L;
		Long logType = 14L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log createNewsCorner2(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 4L;
		Long logType = 4L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}

	@Override
	public Log updateNewsCorner2(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 5L;
		Long logType = 5L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}

	@Override
	public Log deleteNewsCorner2(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 6L;
		Long logType = 6L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log viewNewsCorner2(User user, Long pkid, String remark, String result, Integer channel, Integer category) {
		Long table_id = 7L;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log shareNewsCorner2(User user, Long pkid, String remark, String result, Integer channel, Integer category) {
		Long table_id = 9L;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	
	
	@Override
	public Log viewNewsCorner2List(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 8L;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log createNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 9L;
		Long logType = 9L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log uploadNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 10L;
		Long logType = 10L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log updateNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 11L;
		Long logType = 11L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log deleteNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 12L;
		Long logType = 12L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log likeNewsCorner2(User user, Long pkid, String remark, String result, Integer channel,Long category) {
		Long table_id = 13L;
		Long logType = 13L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log commentNewsCorner2(User user, Long pkid, String remark, String result, Integer channel,Long category) {
		Long table_id = 14L;
		Long logType = 14L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		loginLog.setCategoryId(category.toString());
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log deleteComment(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 15L;
		Long logType = 15L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log viewReport(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 16L;
		Long logType = 16L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		return return_log;
	}
	
	@Override
	public Log viewSeniorOfficer(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 18L ;
		Long logType = 1L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		
		return return_log;
	}
	
	
	@Override
	public Log downloadSeniorOfficer(User user, Long pkid, String remark, String result, Integer channel) {
		Long table_id = 18L ;
		Long logType = 2L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		
		return return_log;
	}
	
	
	
	@Override
	public Log viewForumPost(User user, Long pkid, String remark, String result, Integer channel,Long category) {
		Long table_id = 19L;
		Long logType = 1L;
		Log loginLog = new Log(user,pkid,remark,result,table_id,logType);
		loginLog.setChannel(channel);
		Log return_log = logRepository.saveAndFlush(loginLog);
		
		return return_log;
		
	}
	
	
	
	
	
	private String getCategoryId(List<Integer> categoryList){
		if(categoryList == null || categoryList.size() == 0) {
			return "0";
		}else {
			List<String> reutrn_list = new ArrayList<String>();
			for(Integer i = 0; i < categoryList.size(); i++) {
				reutrn_list.add(categoryList.get(i).toString());
			}
			return String.join(",",reutrn_list);
		}
	}

	@Override
	public ScoreLog saveScoreLog(ScoreLog scoreLog) {
//		System.out.println("is it null? "+ scoreLog);
		return scoreLogRepository.saveAndFlush(scoreLog);
	}
    
	@Override
	public Integer getUserTodayScore(Long userId) {
		// TODO Auto-generated method stub
		Date queryDate = new Date();
		Calendar calendar = Calendar.getInstance(); //Calendar
		calendar.setTime(new Date());//Calendar's Current Date
		calendar.add(Calendar.DAY_OF_YEAR,0);  
		queryDate = calendar.getTime();   
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate;
		
		try {
			startDate = ymdFormat.parse(ymdFormat.format(new Date(queryDate.getTime())));
		} catch (Exception e) {
			startDate = null;
		}
		//System.out.println("Start date "+startDate);
		Integer sumOfScore = 0;
		List<Integer> userTodayScore = scoreLogRepository.getUserTodayScore(userId, startDate);
		
		//List<Integer> userTodayScore = logRepository.getLoginTime(userId, startDate);
		
		if(userTodayScore==null || userTodayScore.size()<1) {
			sumOfScore = 0;
		}else {
			 for(Integer i: userTodayScore)
				 sumOfScore = sumOfScore+i;
		}
		
//		System.out.println(" userTodayScore - "+sumOfScore);
		if (userTodayScore != null) {
			return sumOfScore;
		}else {
			return 0;
		}
		
	
	}
	
	
//	@Override
//	public Integer getUserScore(Long userId) {
//		Date queryDate = new Date();
//		Calendar calendar = Calendar.getInstance(); //Calendar
//		calendar.setTime(new Date());//Calendar's Current Date
//		calendar.add(Calendar.MONTH , -6);  //6 Month ago
//		calendar.add(Calendar.DATE, +1);
//		queryDate = calendar.getTime();   // Month date
//		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Date startDate;
//		Integer sumOfScore = 0;
//		try {
//			startDate = ymdFormat.parse(ymdFormat.format(new Date(queryDate.getTime())));
////			System.out.println("Half year before - "+startDate);
//		} catch (Exception e) {
//			startDate = null;
//		}
//		List<Integer> userScore = scoreLogRepository.getUserScore(userId,startDate);
//		if(userScore==null || userScore.size()<1) {
//			sumOfScore = 0;
//		}else {
//			 for(Integer i: userScore)
//				 sumOfScore = sumOfScore+i;
//		}
//	     
//		if(userScore != null) {
//			
//			
////			System.out.println( "Get User Score = "+sumOfScore);
//			return sumOfScore;
//		}else {
//			return 0;
//		}
//	}
	
	
	
	@Override
	public Integer getUserScore(Long userId) {
		Date queryDate =common.textToDateDate("2021-05-01");
		Calendar calendar = Calendar.getInstance(); //Calendar
		calendar.setTime(new Date());//Calendar's Current Date
		calendar.add(Calendar.MONTH , 6);  //add 6 months 
		calendar.add(Calendar.DATE, +1);
		queryDate = calendar.getTime();   // Month date
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = common.textToDateDate("2021-05-01");
		Integer sumOfScore = 0;
		try {
//			startDate = ymdFormat.parse(ymdFormat.format(new Date(queryDate.getTime())));
//			System.out.println("Start Date - "+startDate);
		} catch (Exception e) {
			startDate = null;
		}
		System.out.println("Log Service Impl : line 452 Start Date - "+startDate);
		List<Integer> userScore = scoreLogRepository.getUserScore(userId,startDate);
		
		
		if(userScore==null || userScore.size()<1) {
			sumOfScore = 0;
		}else {
			 for(Integer i: userScore)
				 sumOfScore = sumOfScore+i;
		}
	     
		if(userScore != null) {
			
			
//			System.out.println( "Get User Score = "+sumOfScore);
			return sumOfScore;
		}else {
			return 0;
		}
	}
	
	
	@Override
	public Long getUserLoginTimes(Long userId) {
		Date queryDate = new Date();
		Calendar calendar = Calendar.getInstance(); //Calendar
		calendar.setTime(new Date());//Calendar's Current Date
		calendar.add(Calendar.MONTH, -6); //6 Month ago
		calendar.add(Calendar.DATE, +1);
		queryDate = calendar.getTime();   //6 Month date
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate;
		try {
			startDate = ymdFormat.parse(ymdFormat.format(new Date(queryDate.getTime())));
		} catch (Exception e) {
			startDate = null;
		}
		Integer loginTimes = scoreLogRepository.getUserLoginTimes(userId, startDate);
		if(loginTimes != null) {
			return Long.parseLong(loginTimes.toString());
		}else {
			return 0L;
		}
	}


	
}
package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;

public interface LogService {

	public List<Log> findAll();
	
	public Log newLog(Log log);
	
	public Optional<Log> findById(Long id);
	
	public Log login(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log viewResource(User user, Long pkid, String remark, String result, Integer channel,List<Integer> category);

	public Log downloadResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category);
	
	public Log viewBlog(User user, Long pkid, String remark, String result, Integer channel,Integer category);
	
	public Log likeBlog(User user, Long pkid, String remark, String result, Integer channel, Long category);

	public Log commentBlog(User user, Long pkid, String remark, String result, Integer channel, Long category);
	
	public Log createBlog(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log updateBlog(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log deleteBlog(User user, Long pkid, String remark, String result, Integer channel);

	public Log viewBlogList(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log createBlogGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log createElearningGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log uploadBlogGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log uploadElearningGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log updateBlogGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log updateElearningGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log deleteBlogGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log deleteElearningGallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log viewNewsCorner2(User user, Long pkid, String remark, String result, Integer channel,Integer category);
	
	public Log likeNewsCorner2(User user, Long pkid, String remark, String result, Integer channel, Long category);

	public Log commentNewsCorner2(User user, Long pkid, String remark, String result, Integer channel, Long category);
	
	public Log createNewsCorner2(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log updateNewsCorner2(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log deleteNewsCorner2(User user, Long pkid, String remark, String result, Integer channel);

	public Log viewNewsCorner2List(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log createNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log uploadNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log updateNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log deleteNewsCorner2Gallery(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log deleteComment(User user, Long pkid, String remark, String result, Integer channel);
	
	public Log viewReport(User user, Long pkid, String remark, String result, Integer channel);

	public Log shareResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category);
	
	
	
	//For Score
	public ScoreLog saveScoreLog(ScoreLog scoreLog);
	
	public Integer getUserTodayScore(Long userId);
	
	public Integer getUserScore(Long userId);
	
	//For Login Times
	public Long getUserLoginTimes(Long id);

	public Log createResource(User user, Long pkid, String remark, String result, Integer channel, List<Long> resourceCategoryList);

	public Log editResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category);

	public Log deleteResource(User user, Long pkid, String remark, String result, Integer channel, List<Integer> category);

	public Log mobiledownload(User user, Long logtype, String remark, String result, Integer channel);

	public Log viewSeniorOfficer(User user, Long pkid, String remark, String result, Integer channel);

	public Log downloadSeniorOfficer(User user, Long pkid, String remark, String result, Integer channel);

	public Log viewForumPost(User user, Long pkid, String remark, String result, Integer channel, Long category);

	Log shareBlog(User user, Long pkid, String remark, String result, Integer channel, Integer category);
	Log shareNewsCorner2(User user, Long pkid, String remark, String result, Integer channel, Integer category);

	
	
}

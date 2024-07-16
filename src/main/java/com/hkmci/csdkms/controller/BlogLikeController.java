package com.hkmci.csdkms.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.BlogLikeModel;
import com.hkmci.csdkms.service.BlogLikeService;
import com.hkmci.csdkms.service.BlogService;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/blog/like")
public class BlogLikeController {
	private BlogLikeService blogLikeService;
    private LogService logger;
    private Common common;
    private BlogService blogService;

	
    
	@Autowired
	@Resource
	private UserService userService;
	
    
	@Autowired
	public BlogLikeController(BlogLikeService theBlogLikeService, LogService theLogger,Common theCommon, BlogService theBlogService)
	{
		blogLikeService = theBlogLikeService;
		logger = theLogger;
		common = theCommon;
		blogService = theBlogService;
	}
	
	@RequestMapping("/all")
	public List<BlogLikeModel> findAll(){
		return blogLikeService.findAll();
		
	}
	@RequestMapping("/user/{CreatedBy}")
	public List<BlogLikeModel> BlogLike(@PathVariable Long CreatedBy, HttpSession session){
		List<BlogLikeModel> theBlogLikeModel = blogLikeService.findById(CreatedBy);
		return theBlogLikeModel;
		
	}
	
	@RequestMapping("/create")
	public ResponseEntity<JsonResult> addLikeModel(HttpServletRequest req,HttpSession session)throws Exception, Throwable{
		//System.out.println("create like ");
		
		Long createdBy = Long.parseLong(req.getParameter("CreatedBy"));
		Integer today = userService.findTodayScoreById(createdBy);
		Integer score = userService.findScoreById(createdBy);
		//System.out.println("Today Score = "+today+ " Score = "+score);

		Integer checkUser = common.checkUserSession(createdBy, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			//Integer funcId = 1;
			Long pkId = Long.parseLong(req.getParameter("pkId"));
//			Date createdAt = new Date();
//			Long randId = 1L;
//			Long instId = 1L;
//			Long sectionId = 1L;
			Optional<BlogLikeModel> likedBefore = blogLikeService.findPostLikeByUser(pkId, createdBy);
			
			//if(record.get().getTodayScore())
//			if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
				if(today<30) {
					user.setScore(score+1);
					
				} 
//			}
			new Thread(() ->{
				Storelog(session,user,pkId,channel,likedBefore);
			}).start();
			
			//System.out.println("See what user get score = "+user.getScore());
			return JsonResult.ok("OK",session);
		   
		}
	}
	
	
	@Async
	 
	public void Storelog(HttpSession session, User user,Long pkId,Integer channel,Optional<BlogLikeModel> likedBefore) {
		
		if(likedBefore.isPresent()) {
			BlogLikeModel new_bloglike = likedBefore.get();
			new_bloglike.setIsDeleted(0);
			blogLikeService.save(new_bloglike);
			//BlogLikeModel return_bloglike =blogLikeService.save(new_bloglike);
		//	return JsonResult.ok(return_bloglike,session);
		}else {
			BlogLikeModel new_bloglike = new BlogLikeModel (1,pkId,user.getId(),new Date(),1L,1L,1L,0);
			//BlogLikeModel return_bloglike =blogLikeService.save(new_bloglike);
			blogLikeService.save(new_bloglike);
			//return JsonResult.ok(return_bloglike,session);
		}
		//Update User Score -----------------------------------------------------------------------------------
		Optional<Blog> target_blog = blogService.findById(pkId);
		Log log = logger.likeBlog(user, pkId, "", "Success",channel,target_blog.get().getCategoryId());

		
		Integer todayScore = logger.getUserTodayScore(user.getId());
		Integer totalScore = logger.getUserScore(user.getId());
		
		//System.out.println("Today score = "+ todayScore);
		Integer addScore = 30-todayScore;
		//System.out.println(" How many score we can add = "+addScore);
		if(1>addScore) {
		ScoreLog scoreLog = new ScoreLog(log.getId(),user,addScore);
		totalScore += addScore;
		logger.saveScoreLog(scoreLog);
		//Integer userScore = total + addScore;
		//System.out.println(" Right Now user score 1 = "+totalScore);
//		if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
			user.setScore(totalScore);
//		}
		user.setTodayScore(todayScore + addScore); 
		session.setAttribute("user_session", user);
		} else {
			ScoreLog scoreLog = new ScoreLog(log.getId(),user,1);
			logger.saveScoreLog(scoreLog);
			//Integer userScore = total ;
//			if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
				user.setScore(totalScore+1);
//			}
			user.setTodayScore(todayScore +1); 
			//System.out.println(" Right Now user score 2 = "+totalScore);
			session.setAttribute("user_session", user);
			
		}
		


		userService.addUser(user);
		//Exist----------------------------------------------------------------------------------------------

	}
	
	
	
	
	
	

	@RequestMapping("/delete")
	public ResponseEntity<JsonResult> unLike(HttpServletRequest req,HttpSession session)throws Exception, Throwable{
		Long createdBy = Long.parseLong(req.getParameter("CreatedBy"));
		Integer checkUser = common.checkUserSession(createdBy, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			//User user = (User) session.getAttribute("user_session");
			//Integer channel = (Integer) session.getAttribute("channel");
			Long pkId = Long.parseLong(req.getParameter("pkId"));
			//Optional<Blog> target_blog = blogService.findById(pkId);
			Optional<BlogLikeModel> likedBefore = blogLikeService.findPostLikeByUser(pkId, createdBy);
			//logger.likeBlog(user, pkId, "", "Success",channel);
			if(likedBefore.isPresent()) {
				//blogLikeService.unlike(pkId, createdBy);
				return JsonResult.ok("Unlike with success",session);
			}else {
				return JsonResult.errorMsg("Can not find this like history");
			}
		}
	}
	
	@RequestMapping("/comment/create")
	public ResponseEntity<JsonResult> addCommentLikeModel(HttpServletRequest req,HttpSession session)throws Exception, Throwable{
		//System.out.println("create comment like ");
		Long createdBy = Long.parseLong(req.getParameter("CreatedBy"));
		Integer checkUser = common.checkUserSession(createdBy, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			Integer funcId = 2;
			Long pkId = Long.parseLong(req.getParameter("pkId"));
			Long blogId = Long.parseLong(req.getParameter("blogId"));
			Date createdAt = new Date();
			Long randId = 1L;
			Long instId = 1L;
			Long sectionId = 1L;
			Optional<Blog> target_blog = blogService.findById(blogId);
			Optional<BlogLikeModel> likedBefore = blogLikeService.findPostLikeByUser(pkId, createdBy);
			logger.commentBlog(user, pkId, "", "Success", channel,target_blog.get().getCategoryId());
			//Exist
			if(likedBefore.isPresent()) {
				BlogLikeModel new_bloglike = likedBefore.get();
				new_bloglike.setIsDeleted(0);
				BlogLikeModel return_bloglike =blogLikeService.save(new_bloglike);
				return JsonResult.ok(return_bloglike,session);
			}else {
				BlogLikeModel new_bloglike = new BlogLikeModel (funcId,pkId,createdBy,createdAt,randId,instId,sectionId,0);
				BlogLikeModel return_bloglike =blogLikeService.save(new_bloglike);
				return JsonResult.ok(return_bloglike,session);
			}
		}
	}
	

	@RequestMapping("/comment/delete")
	public ResponseEntity<JsonResult> commentUnLike(HttpServletRequest req,HttpSession session)throws Exception, Throwable{
		Long createdBy = Long.parseLong(req.getParameter("CreatedBy"));
		Integer checkUser = common.checkUserSession(createdBy, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			Long pkId = Long.parseLong(req.getParameter("pkId"));
			Optional<BlogLikeModel> likedBefore = blogLikeService.findCommentsLikeByUser(pkId, createdBy);
			logger.deleteComment(user, pkId, "", "Success",channel);
			if(likedBefore.isPresent()) {
				blogLikeService.commentUnlike(pkId, createdBy);
				return JsonResult.ok("Unlike with success",session);
			}else {
				return JsonResult.errorMsg("Can not find this like history");
			}
		}
	}
}
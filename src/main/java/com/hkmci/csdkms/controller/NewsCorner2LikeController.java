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
import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.NewsCorner2LikeModel;
import com.hkmci.csdkms.service.NewsCorner2LikeService;
import com.hkmci.csdkms.service.NewsCorner2Service;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/newscorner2/like")
public class NewsCorner2LikeController {
	private NewsCorner2LikeService newscorner2LikeService;
    private LogService logger;
    private Common common;
    private NewsCorner2Service newscorner2Service;

	
    
	@Autowired
	@Resource
	private UserService userService;
	
    
	@Autowired
	public NewsCorner2LikeController(NewsCorner2LikeService theNewsCorner2LikeService, LogService theLogger,Common theCommon, NewsCorner2Service theNewsCorner2Service)
	{
		newscorner2LikeService = theNewsCorner2LikeService;
		logger = theLogger;
		common = theCommon;
		newscorner2Service = theNewsCorner2Service;
	}
	
	@RequestMapping("/all")
	public List<NewsCorner2LikeModel> findAll(){
		return newscorner2LikeService.findAll();
		
	}
	@RequestMapping("/user/{CreatedBy}")
	public List<NewsCorner2LikeModel> NewsCorner2Like(@PathVariable Long CreatedBy, HttpSession session){
		List<NewsCorner2LikeModel> theNewsCorner2LikeModel = newscorner2LikeService.findById(CreatedBy);
		return theNewsCorner2LikeModel;
		
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
			Optional<NewsCorner2LikeModel> likedBefore = newscorner2LikeService.findPostLikeByUser(pkId, createdBy);
			
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
	 
	public void Storelog(HttpSession session, User user,Long pkId,Integer channel,Optional<NewsCorner2LikeModel> likedBefore) {
		
		if(likedBefore.isPresent()) {
			NewsCorner2LikeModel new_newscorner2like = likedBefore.get();
			new_newscorner2like.setIsDeleted(0);
			newscorner2LikeService.save(new_newscorner2like);
			//NewsCorner2LikeModel return_newscorner2like =newscorner2LikeService.save(new_newscorner2like);
		//	return JsonResult.ok(return_newscorner2like,session);
		}else {
			NewsCorner2LikeModel new_newscorner2like = new NewsCorner2LikeModel (1,pkId,user.getId(),new Date(),1L,1L,1L,0);
			//NewsCorner2LikeModel return_newscorner2like =newscorner2LikeService.save(new_newscorner2like);
			newscorner2LikeService.save(new_newscorner2like);
			//return JsonResult.ok(return_newscorner2like,session);
		}
		//Update User Score -----------------------------------------------------------------------------------
		Optional<NewsCorner2> target_newscorner2 = newscorner2Service.findById(pkId);
		Log log = logger.likeNewsCorner2(user, pkId, "", "Success",channel,target_newscorner2.get().getCategoryId());

		
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
			//Optional<NewsCorner2> target_newscorner2 = newscorner2Service.findById(pkId);
			Optional<NewsCorner2LikeModel> likedBefore = newscorner2LikeService.findPostLikeByUser(pkId, createdBy);
			//logger.likeNewsCorner2(user, pkId, "", "Success",channel);
			if(likedBefore.isPresent()) {
				//newscorner2LikeService.unlike(pkId, createdBy);
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
			Long newscorner2Id = Long.parseLong(req.getParameter("newscorner2Id"));
			Date createdAt = new Date();
			Long randId = 1L;
			Long instId = 1L;
			Long sectionId = 1L;
			Optional<NewsCorner2> target_newscorner2 = newscorner2Service.findById(newscorner2Id);
			Optional<NewsCorner2LikeModel> likedBefore = newscorner2LikeService.findPostLikeByUser(pkId, createdBy);
			logger.commentNewsCorner2(user, pkId, "", "Success", channel,target_newscorner2.get().getCategoryId());
			//Exist
			if(likedBefore.isPresent()) {
				NewsCorner2LikeModel new_newscorner2like = likedBefore.get();
				new_newscorner2like.setIsDeleted(0);
				NewsCorner2LikeModel return_newscorner2like =newscorner2LikeService.save(new_newscorner2like);
				return JsonResult.ok(return_newscorner2like,session);
			}else {
				NewsCorner2LikeModel new_newscorner2like = new NewsCorner2LikeModel (funcId,pkId,createdBy,createdAt,randId,instId,sectionId,0);
				NewsCorner2LikeModel return_newscorner2like =newscorner2LikeService.save(new_newscorner2like);
				return JsonResult.ok(return_newscorner2like,session);
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
			Optional<NewsCorner2LikeModel> likedBefore = newscorner2LikeService.findCommentsLikeByUser(pkId, createdBy);
			logger.deleteComment(user, pkId, "", "Success",channel);
			if(likedBefore.isPresent()) {
				newscorner2LikeService.commentUnlike(pkId, createdBy);
				return JsonResult.ok("Unlike with success",session);
			}else {
				return JsonResult.errorMsg("Can not find this like history");
			}
		}
	}
}
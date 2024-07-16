package com.hkmci.csdkms.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.model.NewsCorner2BookmarkModel;
import com.hkmci.csdkms.service.NewsCorner2BookmarkService;
import com.hkmci.csdkms.service.NewsCorner2Service;


@CrossOrigin
@RestController
@RequestMapping("/newscorner2/bookmark")
public class NewsCorner2BookmarkController {
	private NewsCorner2BookmarkService newscorner2BookmarkService;
	
	@Autowired
	@Resource
    private NewsCorner2Service newscorner2Service;
	
	@Autowired
	public NewsCorner2BookmarkController (NewsCorner2BookmarkService theNewsCorner2BookmarkService) {
		newscorner2BookmarkService = theNewsCorner2BookmarkService;
	}
	
	@RequestMapping("/all")
	public List<NewsCorner2BookmarkModel> All() {
		return newscorner2BookmarkService.findAll();
		
	}
	
	@RequestMapping("/getDetail/{UserRef}")
	public ResponseEntity<JsonResult> NewsCorner2Bookmark(@PathVariable Integer UserRef, HttpSession session) {
		List<NewsCorner2BookmarkModel> theNewsCorner2BookmarkModel = newscorner2BookmarkService.findById(UserRef);
		return JsonResult.ok(theNewsCorner2BookmarkModel,session);
		
	}
	
	@RequestMapping("/create")
	public ResponseEntity<JsonResult>  addBookmark(HttpServletRequest req, HttpSession session)throws Exception, Throwable{
		int userRef =Integer.parseInt(req.getParameter("UserRefs"));
		int postId = Integer.parseInt(req.getParameter("PostId"));
		Date createad= new Date(); 
		
		int rankId =  1;
		int instId = 1;
		int sectionId = 1;
		
		Optional<NewsCorner2> newscorner2 = newscorner2Service.checkNewsCorner2Status(Long.parseLong(String.valueOf(postId)));
		if(newscorner2.isPresent()) {
			Optional<NewsCorner2BookmarkModel> markedBefore = newscorner2BookmarkService.findByPostAndUser(postId,userRef);
			if(markedBefore.isPresent()) {
				//System.out.println(markedBefore.get().toString());
				NewsCorner2BookmarkModel new_bookmark = markedBefore.get();
				new_bookmark.setIsDeleted(0);
				return JsonResult.ok(newscorner2BookmarkService.save(new_bookmark),session);
			}else {
				NewsCorner2BookmarkModel new_bookmark = new NewsCorner2BookmarkModel(postId,userRef,createad,rankId,instId,sectionId,0);
				NewsCorner2BookmarkModel return_bookmark = newscorner2BookmarkService.save(new_bookmark);
				return JsonResult.ok(return_bookmark,session);
			}
		}else {
			return JsonResult.errorMsg("Invalid Post Information");
		}
	}
	
	
	@RequestMapping("/delete")
	public ResponseEntity<JsonResult> delBookmark(HttpServletRequest req, HttpSession session)throws Exception, Throwable{
		Integer userRef =Integer.parseInt(req.getParameter("UserRefs"));
		Integer postId = Integer.parseInt(req.getParameter("PostId"));
		
		Optional<NewsCorner2> newscorner2 = newscorner2Service.checkNewsCorner2Status(Long.parseLong(String.valueOf(postId)));
		if(newscorner2.isPresent()) {
			Optional<NewsCorner2BookmarkModel> markedBefore = newscorner2BookmarkService.findByPostAndUser(postId,userRef);
			
			if(markedBefore.isPresent()) {
				//System.out.println(markedBefore.get().getId());
//				newscorner2BookmarkService.del(userRef,postId);
//				return JsonResult.ok("");
				NewsCorner2BookmarkModel new_bookmark = markedBefore.get();
				new_bookmark.setIsDeleted(1);
				return JsonResult.ok(newscorner2BookmarkService.save(new_bookmark),session);
			}else {
				return JsonResult.errorMsg("Could not find this bookmarked record.");
			}
		}else {
			return JsonResult.errorMsg("Invalid Post Information");
		}
		
	}
	
}

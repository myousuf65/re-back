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

import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.model.BlogBookmarkModel;
import com.hkmci.csdkms.service.BlogBookmarkService;
import com.hkmci.csdkms.service.BlogService;


@CrossOrigin
@RestController
@RequestMapping("/blog/bookmark")
public class BlogBookmarkController {
	private BlogBookmarkService blogBookmarkService;
	
	@Autowired
	@Resource
    private BlogService blogService;
	
	@Autowired
	public BlogBookmarkController (BlogBookmarkService theBlogBookmarkService) {
		blogBookmarkService = theBlogBookmarkService;
	}
	
	@RequestMapping("/all")
	public List<BlogBookmarkModel> All() {
		return blogBookmarkService.findAll();
		
	}
	
	@RequestMapping("/getDetail/{UserRef}")
	public ResponseEntity<JsonResult> BlogBookmark(@PathVariable Integer UserRef, HttpSession session) {
		List<BlogBookmarkModel> theBlogBookmarkModel = blogBookmarkService.findById(UserRef);
		return JsonResult.ok(theBlogBookmarkModel,session);
		
	}
	
	@RequestMapping("/create")
	public ResponseEntity<JsonResult>  addBookmark(HttpServletRequest req, HttpSession session)throws Exception, Throwable{
		int userRef =Integer.parseInt(req.getParameter("UserRefs"));
		int postId = Integer.parseInt(req.getParameter("PostId"));
		Date createad= new Date(); 
		
		int rankId =  1;
		int instId = 1;
		int sectionId = 1;
		
		Optional<Blog> blog = blogService.checkBlogStatus(Long.parseLong(String.valueOf(postId)));
		if(blog.isPresent()) {
			Optional<BlogBookmarkModel> markedBefore = blogBookmarkService.findByPostAndUser(postId,userRef);
			if(markedBefore.isPresent()) {
				//System.out.println(markedBefore.get().toString());
				BlogBookmarkModel new_bookmark = markedBefore.get();
				new_bookmark.setIsDeleted(0);
				return JsonResult.ok(blogBookmarkService.save(new_bookmark),session);
			}else {
				BlogBookmarkModel new_bookmark = new BlogBookmarkModel(postId,userRef,createad,rankId,instId,sectionId,0);
				BlogBookmarkModel return_bookmark = blogBookmarkService.save(new_bookmark);
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
		
		Optional<Blog> blog = blogService.checkBlogStatus(Long.parseLong(String.valueOf(postId)));
		if(blog.isPresent()) {
			Optional<BlogBookmarkModel> markedBefore = blogBookmarkService.findByPostAndUser(postId,userRef);
			
			if(markedBefore.isPresent()) {
				//System.out.println(markedBefore.get().getId());
//				blogBookmarkService.del(userRef,postId);
//				return JsonResult.ok("");
				BlogBookmarkModel new_bookmark = markedBefore.get();
				new_bookmark.setIsDeleted(1);
				return JsonResult.ok(blogBookmarkService.save(new_bookmark),session);
			}else {
				return JsonResult.errorMsg("Could not find this bookmarked record.");
			}
		}else {
			return JsonResult.errorMsg("Invalid Post Information");
		}
		
	}
	
}

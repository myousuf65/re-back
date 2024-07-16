package com.hkmci.csdkms.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.EmailUtil;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.BlogAssistant;
import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.BlogTag;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.Uinbox;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.PostReturnModel;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.service.BlogGalleryDetailService;
import com.hkmci.csdkms.service.BlogGalleryService;
import com.hkmci.csdkms.service.BlogService;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.UinboxService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;
import com.hkmci.csdkms.wowza.WowzaTokenGenerator;
import com.vdurmont.emoji.EmojiParser;

@CrossOrigin
@RestController
@RequestMapping("/blog")
public class BlogController {
	@Autowired
	@Resource
	private UserRepository userRepository;
	
	@Autowired
	@Resource
    private BlogGalleryService blogGalleryService;
	
	@Autowired
	@Resource
    private BlogGalleryDetailService blogGalleryDetailService;
	
	@Autowired
	@Resource
    private LogService logger;
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource
    private BlogService blogService;
	
	@Autowired
	@Resource
    private UserService userService;
	
	@Autowired
	@Resource
    private StorageService storageService;
	
	@Autowired
	@Resource 
	private UinboxService uinboxService;
	
	@RequestMapping("/share/{userId}")
	private ResponseEntity<JsonResult> shareDocument(@PathVariable Long userId, 
//			@RequestParam("resourceId") Long resourceId, @RequestParam("userIds") List<String> sendTo, 
			@RequestBody JsonNode jsonNode,
			HttpSession session) throws Exception, Throwable{
			HashMap<String,String> user_check = common.checkUser(userId, session);
			if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
				
				Long blogId = jsonNode.get("blogId").asLong();
				List<Long> user_list = dealWithStaffNo(jsonNode);
				List<User> user_session_list = (List<User>) session.getAttribute("user_list");
				User user = (User) session.getAttribute("user_session"); 
				String blog_title = blogService.findById(blogId).get().getPostTitle();
						
						
						
				//List<Long> user_list = dealWithSentTo(sendTo, user_session_list);
				//System.out.println("user list : " + user_list);
				List<Uinbox> shareToList = new ArrayList<>();
				for(Integer i = 0 ; i < user_list.size() ; i++ ) {
					//System.out.println("Send to = "+ user_list.get(i));
					if (user_list.get(i)!= 0) {
						Uinbox new_share = new Uinbox();
						new_share.setCreatedAt(new Date());
						new_share.setSendBy(userId);
						new_share.setIsDeleted(0);
						new_share.setIsRead(0);
						new_share.setResourceId(blogId);
						new_share.setSendTo(user_list.get(i));
						new_share.setMailType(2L);
						
						uinboxService.save(new_share);
					
					}

				}
				
				List<User> sendEmaiList = user_list.stream()
						  .map((u) -> {
							  User temp_user = user_session_list
									  		  .stream()
									  		  .filter(n -> n.getId().equals(u) && u!=0 )
									  		  .collect(Collectors.toList()).get(0);
							//System.out.println("Temp_User:" + temp_user.getStaffNo() + "--" + temp_user.getEmail() + "--" + temp_user.getNotesAccount());
							
							temp_user.setEmail((user_session_list.stream()
									 .filter((n) -> n.getId().equals(temp_user.getId()))
									 .map(User::getEmail)
									 .collect(Collectors.toList())
									 .get(0)));
							//System.out.println("Temp_User222:" + temp_user.getStaffNo() + "--" + temp_user.getEmail());
							  return temp_user;
						  })
						  .collect(Collectors.toList());
				
				EmailUtil.sendEmailToShareMiniBlog(sendEmaiList, blog_title, user.getFullname(),Long.parseLong(user_check.get("access_channel")),blogId);
				Integer channel = (Integer) session.getAttribute("channel");
				Integer today = userService.findTodayScoreById(userId);
				Integer score = userService.findScoreById(userId);
//				if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
					if(today<29) {
				
						user.setScore(score+2);
					
					} else if (28<today && today<30) {
						user.setScore(score+1);
					
					}
//				}
				session.setAttribute("user_session", user);
				new Thread(()-> {
					StorelogEmail(session,user,blogId,channel, today,user.getScore());
				}).start();
				
				
				return JsonResult.ok("sent",session);
				
			}
			
	}
	
	
	
	List<Long> dealWithStaffNo(JsonNode jsonNode){
		List<Long> return_data = new ArrayList<>();
		//System.out.println("User size = " +jsonNode.get("userIds").size() );
		for(Integer i = 0 ; i < jsonNode.get("userIds").size();i++) {
			return_data.add(jsonNode.get("userIds").get(i).asLong());
		}
		
		return return_data;
	}
	
	@RequestMapping("/create/{userId}")
	public ResponseEntity<JsonResult> create(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			Optional<BlogGallery> blogGallery = blogGalleryService.findActive(userId);
			//System.out.println(userId);
			
			if(blogGallery.isPresent()) {
				Date createdAt = new Date();
				
				String dateOfTest = jsonNode.get("publishAt").asText();
				
				Date publishAt = common.textToDate(dateOfTest);
		        Blog new_blog = new Blog();
		        Integer checkBlogCreator = common.checkBlogCreator(user, session, jsonNode);

		        if(checkBlogCreator.equals(1)) {
		        	Optional<User> creator = userService.findById(jsonNode.get("originalCreator").asLong());
		        	if(creator.isPresent()) {
		        		
		        	}
		        	//System.out.println("Creator = "+ creator.get().getFullname());
		        	new_blog.setCategoryId(jsonNode.get("categoryId").asLong());
			        new_blog.setContent(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText()));
			        new_blog.setCreatedAt(createdAt);
			        new_blog.setCreatedBy(jsonNode.get("createdBy").asLong());
			        new_blog.setHit(0L);
			        new_blog.setIs_public(jsonNode.get("isPublic").asLong());
			        new_blog.setInstId(1L);
			        new_blog.setSectionId(1L);
			        new_blog.setRankId(1L);
			        new_blog.setPublishAt(publishAt);
			        new_blog.setIsDeleted(0);
			        new_blog.setPostTitle(jsonNode.get("postTitle").asText());
			        new_blog.setShowAsAlias(jsonNode.get("showAsAlias").asInt());
			        new_blog.setAlias(jsonNode.get("alias").asText());
			        new_blog.setOriginalCreator(creator.get());
			        
			        if(jsonNode.get("videoLink") == null) {
			        	new_blog.setPostVideoLink("");
			        } else {
			        	new_blog.setPostVideoLink(jsonNode.get("videoLink").asText());
			        }
			        
			        //System.out.println("video link = " + new_blog.getPostVideoLink());
			        
			        //System.out.println(EmojiParser.parseToHtmlDecimal(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText())));
			        
			        Long return_blog_id = blogService.add(new_blog).getId();
			        blogGallery.get().setPostId(return_blog_id);
			        blogGalleryService.updateBlogGallery(blogGallery.get());
			        
			        List<String> blogTags = getPostTags(jsonNode);
					for(Integer z = 0; z < blogTags.size(); z++) {
						blogService.createBlogTag(EmojiParser.removeAllEmojis(blogTags.get(z)), return_blog_id,userId);
					}
			        
			        if(submitGallery(userId) == null) {
			        	
						return JsonResult.errorMsg("No Gallery Records");
					}
					else{
						//return JsonResult.ok(submitGallery(userId),"Is Finished");
						logger.createBlog(user, 0L, "", "Success",channel);
						return JsonResult.ok(return_blog_id,"Post Create Successfully", session);
					}
		        }else {
		        	return JsonResult.errorMsg("Creator User Status Error");
		        }
			}
			else{
				return JsonResult.errorMsg("Gallery Status Error");
			}
		}
    }
	
	@RequestMapping("/update/{blogId}")
	public ResponseEntity<JsonResult> update(@RequestBody JsonNode jsonNode,@PathVariable Long blogId, HttpSession session) {
		Optional<Blog> blog = blogService.findById(blogId);
		//System.out.println(userId);
		
		if(blog.isPresent()) {
			Integer checkBlogUser = common.checkBlogUser(jsonNode, session, blog.get(),"update");
			if(checkBlogUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				
				Date publishAt = common.textToDate(jsonNode.get("publishAt").asText());
			
				Date modifiedAt = new Date();
		        
		        Blog new_blog = blog.get();
		        //new_blog.setCategoryId(jsonNode.get("CategoryId").asLong());
		        new_blog.setContent(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText()));
		        //new_blog.setCreatedBy(jsonNode.get("createdBy").asLong());
		        new_blog.setHit(blog.get().getHit() );//+ 1
		        new_blog.setIs_public(jsonNode.get("isPublic").asLong());
		        new_blog.setModifiedy(jsonNode.get("modifiedBy").asLong());
		        new_blog.setAlias(jsonNode.get("alias").asText());
		        new_blog.setModifiedAt(modifiedAt);
		        new_blog.setPublishAt(publishAt);
		        //new_blog.setIsDeleted(0);
		        new_blog.setPostTitle(jsonNode.get("postTitle").asText());
		        new_blog.setShowAsAlias(jsonNode.get("showAsAlias").asInt());
		        //new_blog.setOriginalCreator(jsonNode.get("originalCreator").asLong());
		        
		        if(jsonNode.get("videoLink") == null) {
		        	new_blog.setPostVideoLink("");
		        } else {
		        	new_blog.setPostVideoLink(jsonNode.get("videoLink").asText());
		        }
		        
		        
		        blogService.update(new_blog);
		        
		       // dealWithTags(jsonNode,blog.get().getId());
		        
		        dealWithTags(jsonNode,blogId);
				logger.updateBlog(user, blogId, "", "Success",channel);
				return JsonResult.ok("Post Update Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	private void dealWithTags(JsonNode jsonNode, Long blogId) {
		// TODO Auto-generated method stub
		List<String> blogTags = getPostTags(jsonNode);
		
		List<BlogTag> tags_in_db = blogService.getBlogTags(blogId);
		
		List<BlogTag> tags_valid_in_DB = tags_in_db.stream()
									  .filter((n) -> n.getIsDeleted().equals(0))
									  .collect(Collectors.toList());
		List<String> tags_valid_id_in_DB = tags_in_db.stream()
										 .filter((n) -> n.getIsDeleted().equals(0))
										 .map(BlogTag::getTag)
										 .collect(Collectors.toList());
		List<BlogTag> tags_deleted_in_DB = tags_in_db.stream()
									    .filter((n) -> n.getIsDeleted().equals(1))
									    .collect(Collectors.toList());
		
		//List<BlogTag> tags = new ArrayList<>();
		
		List<BlogTag> tags_to_be_added = blogTags.stream()
										 .filter((n) -> !tags_valid_id_in_DB .contains(n))
										 .map(
												 (n) -> {
													 BlogTag temp = new BlogTag(blogId,EmojiParser.removeAllEmojis(n),0);
													 return temp;
												 }
										 ).collect(Collectors.toList());
		//System.out.println("Tags To Be Added size: " + tags_to_be_added.size());
		blogService.saveAllTags(tags_to_be_added);
		
		List<BlogTag> tags_to_be_updated = tags_deleted_in_DB.stream()
				 .filter((n) -> blogTags.contains(n.getTag()))
				 .map(
						 (n) -> {
							 BlogTag temp = new BlogTag(n.getId(),blogId,EmojiParser.removeAllEmojis(n.getTag()),0);
							 return temp;
						 }
				 ).collect(Collectors.toList());
		blogService.updateAllTags(tags_to_be_updated);
		//System.out.println("Tags To Be Updated size: " + tags_to_be_updated.size());
		
		List<BlogTag> tags_to_be_deleted = tags_valid_in_DB.stream()
				 .filter((n) -> !blogTags.contains(n.getTag()))
				 .map(
						 (n) -> {
							 BlogTag temp = new BlogTag(n.getId(),blogId,EmojiParser.removeAllEmojis(n.getTag()),1);
							 return temp;
						 }
				 ).collect(Collectors.toList());
		//tags.addAll(tags_to_be_deleted);
		
		blogService.deleteAllTags(tags_to_be_deleted);
		//System.out.println("Tags To Be Deleted size: " + tags_to_be_deleted.size());
		
//		for(Integer i = 0; i < blogTags.size(); i++) {
//			blogService.createBlogTag(EmojiParser.removeAllEmojis(blogTags.get(i)), blogId,null);
//		}
		
	}

	@RequestMapping("/delete/{blogId}")
	public ResponseEntity<JsonResult> delete(@RequestBody JsonNode jsonNode,@PathVariable Long blogId,HttpSession session) {
		Optional<Blog> blog = blogService.findById(blogId);
		//System.out.println(userId);
		
		if(blog.isPresent()) {
			Integer checkBlogUser = common.checkBlogUser(jsonNode, session, blog.get(),"delete");
			if(checkBlogUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				Date deletedAt = new Date();		        
		        Blog new_blog = blog.get();
		        new_blog.setDeletedBy(jsonNode.get("user").asLong());
		        new_blog.setDeletedAt(deletedAt);
		        new_blog.setIsDeleted(1);
		        
		        blogService.update(new_blog);
		        logger.deleteBlog(user, blogId, "", "Success",channel);
				return JsonResult.ok("Post Delete Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	
	@RequestMapping("/related/{userId}")
	public ResponseEntity<JsonResult> getRelated(@PathVariable Long userId, @RequestParam(value="post") Long postId, HttpSession session){
		List<Blog> return_data = blogService.getRelate(postId);
		List<Object> test= new ArrayList<>();
		
		 for(Integer i =0 ; i< return_data.size(); i++) {
			HashMap<String, String> retrun_data = new HashMap<String, String>();
			retrun_data.put("id", return_data.get(i).getId().toString());
			retrun_data.put("title", return_data.get(i).getPostTitle());
			retrun_data.put("publishAt", return_data.get(i).getCreatedAt().toString());
			test.add(retrun_data);
		 }
		
		
		return JsonResult.ok(test,session);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDetail/{blogId}")
	public ResponseEntity<JsonResult> getDetail(@PathVariable Long blogId, @RequestParam(value="user") Long userId, HttpSession session ) {
		Optional<Blog> blog = blogService.findById(blogId);
		Integer today = userService.findTodayScoreById(userId);
		Integer score = userService.findScoreById(userId);
	
		System.out.println("Today Score = "+today+ " Score = "+score);

		//Integer added =0;
		if(blog.isPresent()) {
			Optional<BlogGallery> blogGallery = blogGalleryService.findByPostId(blog.get().getId());
			if(blogGallery.isPresent()) {
			
				Integer checkUser = common.checkUserSession(userId, session);
				
				if(checkUser.equals(0)) {
					return JsonResult.errorMsg("Request User Status Error");
				}else {
			
					User user = (User) session.getAttribute("user_session");
					System.out.println("Blog Controller line 435 = user score : "+user.getScore());
					List<User> user_list_session = (List<User>) session.getAttribute("user_list");
					Integer channel = (Integer) session.getAttribute("channel");
					List<BlogGalleryDetail> fileList = blogGalleryService.findByGalleryId(blogGallery.get());
					//PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),userId,user_list_session);
					
					//PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),blog.get().getOriginalCreator().getId(),user_list_session);
					PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),userId,user_list_session,blog.get().getOriginalCreator().getId());
					if(return_data == null) {
						return JsonResult.list("Post Not Found (get blog not found )", null,0,session);
					}else{
//						String content = return_data.getBlog().getContent();
//						return_data.getBlog().setContent(content.replace("\"", "\\\""));
						
						System.out.println("Blog Controller - line 449 :User Today Score = "+ today +" Total Score = "+score);
//						if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
							if(today<29) {
						
								user.setScore(score+2);
							
							} else if (28<today && today<30) {
								user.setScore(score+1);
							
							}
//						}
							
						//rewrite video link
						Blog _blog = return_data.getBlog();
						if(_blog.getPostVideoLink() != null && !_blog.getPostVideoLink().equals("")) {
							String video = _blog.getPostVideoLink();		
							String[] name = video.split("/KMS/");
							
							if(video.startsWith("https://ams.csd.gov.hk/KMS/")) {
								String param1 ="https://ams.csd.gov.hk/api/vodapi.php?";
								String param2 =	"file="+name[1]+"&folder=/KMS/&key=test1";
								String param = param1 +param2;
								String videoLink =getUrlContents(param);
								_blog.setPostVideoLink(videoLink);
							} else {
								_blog.setPostVideoLink("");
							}
						}
						return_data.setBlog(_blog);
						
							
						session.setAttribute("user_session", user);
						new Thread(()-> {
							Storelog2(session,user,blogId,channel,return_data, today,user.getScore());
						}).start();

						if(fileList.size() == 0) {
							return JsonResult.fileList("Post Found Successfully", return_data,blogGallery.get().getId(),session);
						}else {
							return JsonResult.fileList("Post Found Successfully", return_data,fileList,session);
						}
					}
					
					
				}
			}else {
				return JsonResult.errorMsg("Post Gallery Not Found");
			}
			
			
			
			
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDetailWowza/{blogId}")
	public ResponseEntity<JsonResult> getDetailWowza(@PathVariable Long blogId, @RequestParam(value="user") Long userId, HttpSession session ) {
		Optional<Blog> blog = blogService.findById(blogId);
		Integer today = userService.findTodayScoreById(userId);
		Integer score = userService.findScoreById(userId);
	
		System.out.println("Today Score = "+today+ " Score = "+score);

		//Integer added =0;
		if(blog.isPresent()) {
			Optional<BlogGallery> blogGallery = blogGalleryService.findByPostId(blog.get().getId());
			if(blogGallery.isPresent()) {
			
				Integer checkUser = common.checkUserSession(userId, session);
				
				if(checkUser.equals(0)) {
					return JsonResult.errorMsg("Request User Status Error");
				}else {
			
					User user = (User) session.getAttribute("user_session");
					System.out.println("Blog Controller line 435 = user score : "+user.getScore());
					List<User> user_list_session = (List<User>) session.getAttribute("user_list");
					Integer channel = (Integer) session.getAttribute("channel");
					List<BlogGalleryDetail> fileList = blogGalleryService.findByGalleryId(blogGallery.get());
					//PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),userId,user_list_session);
					
					//PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),blog.get().getOriginalCreator().getId(),user_list_session);
					PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),userId,user_list_session,blog.get().getOriginalCreator().getId());
					if(return_data == null) {
						return JsonResult.list("Post Not Found (get blog not found )", null,0,session);
					}else{
//						String content = return_data.getBlog().getContent();
//						return_data.getBlog().setContent(content.replace("\"", "\\\""));
						
						System.out.println("Blog Controller - line 449 :User Today Score = "+ today +" Total Score = "+score);
//						if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
							if(today<29) {
						
								user.setScore(score+2);
							
							} else if (28<today && today<30) {
								user.setScore(score+1);
							
							}
//						}
							
						//rewrite video link
						Blog _blog = return_data.getBlog();
						if(_blog.getPostVideoLink() != null && !_blog.getPostVideoLink().equals("")) {
							String video = _blog.getPostVideoLink();		
							String serverUrl = "http://192.168.1.32/wowza/";
					        String sharedSecret = "12345";
					        String hashPrefix = "wowzatoken";
					        String videoPath;
					        String clientIP = null;
							
							if(video.startsWith("https://ams.csd.gov.hk/KMS/")) {
								String[] name = video.split("/KMS/");
//								String param1 ="https://ams.csd.gov.hk/api/vodapi.php?";
//								String param2 =	"file="+name[1]+"&folder=/KMS/&key=test1";
//								String param = param1 +param2;
//								String videoLink =getUrlContents(param);
						        videoPath = name[1];
							} else {
						        videoPath = video;
							}
							clientIP = null;
							String videoLink = WowzaTokenGenerator.gen(videoPath, serverUrl, sharedSecret, hashPrefix, clientIP);
							_blog.setPostVideoLink(videoLink);
						}
						return_data.setBlog(_blog);
						
							
						session.setAttribute("user_session", user);
						new Thread(()-> {
							Storelog2(session,user,blogId,channel,return_data, today,user.getScore());
						}).start();

						if(fileList.size() == 0) {
							return JsonResult.fileList("Post Found Successfully", return_data,blogGallery.get().getId(),session);
						}else {
							return JsonResult.fileList("Post Found Successfully", return_data,fileList,session);
						}
					}
					
					
				}
			}else {
				return JsonResult.errorMsg("Post Gallery Not Found");
			}
			
			
			
			
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDetail2/{blogId}")
	public ResponseEntity<JsonResult> getDetail2(@PathVariable Long blogId, @RequestParam(value="user") Long userId, HttpSession session ) {
		Optional<Blog> blog = blogService.findById(blogId);
		Integer today = userService.findTodayScoreById(userId);
		Integer score = userService.findScoreById(userId);
	
		System.out.println("Today Score = "+today+ " Score = "+score);

		//Integer added =0;
		if(blog.isPresent()) {
			Optional<BlogGallery> blogGallery = blogGalleryService.findByPostId(blog.get().getId());
			if(blogGallery.isPresent()) {
			
				Integer checkUser = common.checkUserSession(userId, session);
				
				if(checkUser.equals(0)) {
					return JsonResult.errorMsg("Request User Status Error");
				}else {
			
					User user = (User) session.getAttribute("user_session");
					System.out.println("Blog Controller line 435 = user score : "+user.getScore());
					List<User> user_list_session = (List<User>) session.getAttribute("user_list");
					Integer channel = (Integer) session.getAttribute("channel");
					List<BlogGalleryDetail> fileList = blogGalleryService.findByGalleryId(blogGallery.get());
					//PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),userId,user_list_session);
					
					//PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),blog.get().getOriginalCreator().getId(),user_list_session);
					PostReturnModel return_data = blogService.getBlogById(blog.get().getId(),userId,user_list_session,blog.get().getOriginalCreator().getId());
					if(return_data == null) {
						return JsonResult.list("Post Not Found (get blog not found )", null,0,session);
					}else{
//						String content = return_data.getBlog().getContent();
//						return_data.getBlog().setContent(content.replace("\"", "\\\""));
						
						System.out.println("Blog Controller - line 449 :User Today Score = "+ today +" Total Score = "+score);
//						if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
							if(today<29) {
						
								user.setScore(score+2);
							
							} else if (28<today && today<30) {
								user.setScore(score+1);
							
							}
//						}
							
						
							
						session.setAttribute("user_session", user);
						new Thread(()-> {
							Storelog2(session,user,blogId,channel,return_data, today,user.getScore());
						}).start();

						if(fileList.size() == 0) {
							return JsonResult.fileList("Post Found Successfully", return_data,blogGallery.get().getId(),session);
						}else {
							return JsonResult.fileList("Post Found Successfully", return_data,fileList,session);
						}
					}
					
					
				}
			}else {
				return JsonResult.errorMsg("Post Gallery Not Found");
			}
			
			
			
			
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	
	public void StorelogEmail(HttpSession session, User user,Long blogId,Integer channel,Integer today,Integer total  ) {
		Log log =logger.shareBlog(user, blogId, "", "Success",channel,0);
//		
		
		
		Integer todayScore = logger.getUserTodayScore(user.getId());
		Integer totalScore = logger.getUserScore(user.getId());
		
		//System.out.println("Original Total Score = "+totalScore+" Today Score = "+todayScore);
		
	
		
		//Integer total =user.getScore()-added;
		
		//Integer todayScore = user.getScore();
		//System.out.println("Today score = "+ todayScore);
		Integer addScore = 30-todayScore;
		//System.out.println(" How many score we can add = "+addScore);
		if(2>addScore) {
		ScoreLog scoreLog = new ScoreLog(log.getId(),user,addScore);
		totalScore += addScore;
		logger.saveScoreLog(scoreLog);
		//Integer userScore = total + addScore;
		//System.out.println(" Right Now user score 1 = "+user+ " today score = "+ user.getTodayScore() );
//		if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
			user.setScore(totalScore);
//		}
		user.setTodayScore(todayScore + addScore); 
		session.setAttribute("user_session", user);
		} else {
			ScoreLog scoreLog = new ScoreLog(log.getId(),user,2);
			logger.saveScoreLog(scoreLog);
			//Integer userScore = total ;
//			if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
				user.setScore(totalScore+2);
//			}
			user.setTodayScore(todayScore +2); 
			//System.out.println(" Right Now user score 2 = "+user+ " today score = "+ user.getTodayScore() );
			session.setAttribute("user_session", user);
			
		}
		


		userService.addUser(user);
	}
	
	
	
	
	
	
	
	
	
	public void Storelog2(HttpSession session, User user,Long blogId,Integer channel,PostReturnModel return_data ,Integer today,Integer total  ) {
		blogService.addHit(blogId,user.getId());
		Log log =logger.viewBlog(user, blogId, "", "Success",channel,Integer.parseInt(return_data.getBlog().getCategoryId().toString()));
////		
//		Integer todayScore = logger.getUserTodayScore(user.getId());
//		Integer totalScore = logger.getUserScore(user.getId());
		
		//System.out.println("Original Total Score = "+totalScore+" Today Score = "+todayScore);
		
	
		
		//Integer total =user.getScore()-added;
		
		//Integer todayScore = user.getScore();
		//System.out.println("Today score = "+ todayScore);
		Integer addScore = 30-today;
		//System.out.println(" How many score we can add = "+addScore);
		if(2>addScore) {
		ScoreLog scoreLog = new ScoreLog(log.getId(),user,addScore);
//		total += addScore;
		logger.saveScoreLog(scoreLog);
		//Integer userScore = total + addScore;
		//System.out.println(" Right Now user score 1 = "+user+ " today score = "+ user.getTodayScore() );
		user.setScore(total);
		user.setTodayScore(today + addScore); 
		session.setAttribute("user_session", user);
		} else {
			ScoreLog scoreLog = new ScoreLog(log.getId(),user,2);
			logger.saveScoreLog(scoreLog);
			//Integer userScore = total ;
			
//			if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
//				user.setScore(total+2);
//			}
			user.setTodayScore(today +2); 
			//System.out.println(" Right Now user score 2 = "+user+ " today score = "+ user.getTodayScore() );
			session.setAttribute("user_session", user);
			
		}
		

		System.out.println("Blog Controller ------- line 585 : Score "+ user.getScore());
		userService.addUser(user);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/search")
	public ResponseEntity<JsonResult> search(
				@RequestParam(value="CateId") Long categoryId, 
				@RequestParam(value="user") Long user_id,
				@RequestParam(value="selSorter", defaultValue="") String selSorter,
				@RequestParam(value="page", defaultValue="1") Integer page,
				HttpSession session) {
		Integer checkUser = common.checkUserSession(user_id, session);
		//System.out.println("Enter search API ");
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {

			if(selSorter.split(":").length < 2) {
				return JsonResult.errorMsg("Sorting Condition Error");
			}else {
				String sortBy = selSorter.split(":")[0];
				String sortOrder = selSorter.split(":")[1];
				sortBy = sortBy == "publishDate" ? "publishAt" : sortBy;
				//List<User> user_list_session = (List<User>) session.getAttribute("user_list");
				List<User> user_list_session = (List<User>) userRepository.findListById(user_id);
				
				List<BlogGalleryDetail> blog_thumb = blogGalleryDetailService.getAllThumb();
				List<PostReturnModel> latest_blogs = blogService.searchBlogByLatest(user_list_session,blog_thumb);
				
				//System.out.println("After get latest blogs ");
				
				List<Long> latest_blog_ids = new ArrayList<Long>();
				for(Integer i = 0; i < latest_blogs.size(); i++) {
					latest_blog_ids.add(latest_blogs.get(i).getBlog().getId());
				}
//				//System.out.println("Sort by: " + sortBy + " " + sortOrder);
				List<PostReturnModel> return_data = blogService.searchBlogByCategoryId(categoryId, sortBy, sortOrder, page,latest_blog_ids,user_list_session,blog_thumb);
				Integer return_total = blogService.getTotalSearchBlogByCategoryId(categoryId, sortBy, sortOrder, page,latest_blog_ids,user_list_session,blog_thumb);
				//System.out.println("Get Total Post Size: " + return_total);
				if(return_data == null) {
					return JsonResult.list("Post List Empty", return_data,0,session);
				}else{
					return JsonResult.list("Post List Searching", return_data.stream().map(
							(b) -> {
//								String content = b.getBlog().getContent();
//								b.getBlog().setContent(content.replace("\"", "\\\""));
								return b;
							}
					).collect(Collectors.toList()),return_total,session);
				}
			}
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getMyPosts/{userId}")
	public ResponseEntity<JsonResult> get_my_posts(@PathVariable Long userId,HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			List<PostReturnModel> return_data = blogService.getMyPosts(userId,user_list_session);
			if(return_data == null) {
				return JsonResult.list("Post List Empty", return_data,0,session);
			}else{
				return JsonResult.list("Post List Searching", return_data,return_data.size(),session);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getMyBookmarks/{userId}")
	public ResponseEntity<JsonResult> get_my_bookmarks(@PathVariable Long userId, HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<PostReturnModel> return_data = blogService.getMyBookmarks(userId,user_list_session);
			if(return_data == null) {
				return JsonResult.list("Post List Empty", return_data,0,session);
			}else{
				return JsonResult.list("Post List Searching", return_data,return_data.size(),session);
			}
		}
	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getLatest/{userId}/{isLoged}")
	public ResponseEntity<JsonResult> getLatest(@PathVariable Long userId,@PathVariable Integer isLoged, HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<BlogGalleryDetail> blog_thumb = blogGalleryDetailService.getAllThumb();
			Integer channel = (Integer) session.getAttribute("channel");
			List<PostReturnModel> return_data = blogService.searchBlogByLatest(user_list_session,blog_thumb);
			if(isLoged.equals(1)) {logger.viewBlogList(user, 0L, "", "Success",channel);}
			if(return_data == null) {
				return JsonResult.list("Post List Empty", return_data,0,session);
			}else{
				return JsonResult.list("Post List Searching", return_data,return_data.size(),session);
			}
		}
	}

	@RequestMapping("/gallery/check/{userId}")
    public ResponseEntity<JsonResult> checkGallery(@PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			Optional<BlogGallery> blogGallery = blogGalleryService.findActive(userId);
			//System.out.println("Start check temp folder   -"+userId);
			if(blogGallery.isPresent()) {
//				System.out.println("Bolg Gallery Present!! ");
				List<BlogGalleryDetail> fileList = blogGalleryService.findByGalleryId(blogGallery.get());
//				System.out.println("file List "+fileList);
				return JsonResult.fileList("Gallery Existed", blogGallery.get(), fileList,session);
			}
			else{
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				Md5Encode md5 = new Md5Encode();
				Date now = new Date();
		        String gallery_name = md5.getMD5("Holfer" + now.toString());
		        
		        BlogGallery new_blogGallery = new BlogGallery();
		        new_blogGallery.setGalleryName(gallery_name);
		        new_blogGallery.setCreatedBy(userId);
		        new_blogGallery.setCreatedAt(now);
		        new_blogGallery.setIsFinished(0);
		        new_blogGallery.setUserId(userId);
		        
		        BlogGallery return_blogGallery = blogGalleryService.newBlogGallery(new_blogGallery);
				logger.createBlogGallery(user, return_blogGallery.getId(), "", "Success",channel);
				//return JsonResult.ok(return_blogGallery,"Gallery Created");
				return JsonResult.fileList("Gallery Created", return_blogGallery, null,session);
			}
		}
    }
	

	@RequestMapping("/gallery/upload/{userId}")
    public ResponseEntity<JsonResult> uploadGallery(@PathVariable Long userId,
    		@RequestParam("file") MultipartFile file,
    		HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			Optional<BlogGallery> blogGallery = blogGalleryService.findActive(userId);
			//System.out.println("gallery upload --- "+userId);
			
			String folder = "D:\\csdkms\\backend\\";
			String folderFinal = folder + userId + "\\"+blogGallery.get().getGalleryName();
			//System.out.println("folder Final path = "+ folderFinal);
			
			 File tmpDir = new File(folderFinal);
			    boolean exists = tmpDir.exists();
			//System.out.println("Is the folder exists = "+ exists);
			
			
			
			
			if(blogGallery.isPresent()) {
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				//System.out.println("Present!!!");
				BlogGalleryDetail new_detail = blogGalleryService.storeToUserFolder(file, blogGallery.get().getId());
				//System.out.println("New Detail --- : "+ new_detail);
				logger.uploadBlogGallery(user, blogGallery.get().getId(), "", "Success", channel);
				List<BlogGalleryDetail> fileList = blogGalleryService.findByGalleryId(blogGallery.get());
				//System.out.println("File List -- "+ fileList);
		        return JsonResult.fileList("Gallery Upload Successfully",new_detail,fileList,session);
			}else{
				//return JsonResult.errorMap(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
				//System.out.println("Not present!!!");
				return JsonResult.errorMsg("Invalid Request");
			}
		}
    }
	
	@RequestMapping("/gallery/update/{galleryId}/{userId}")
    public ResponseEntity<JsonResult> updateGallery(@PathVariable Long galleryId,
    		@PathVariable Long userId,@RequestParam("file") MultipartFile file,
    		HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			Optional<BlogGallery> blogGallery = blogGalleryService.findById(galleryId);
			
			Path userFilePath = storageService.getUserFolderLocation();
			
			//System.out.println("Blog Gallery = "+ blogGallery.get().getId());
			Optional<Blog> blog = blogService.findById(blogGallery.get().getPostId());
			
			String userGalleryFolder = userFilePath.toString() + "/" + blogGallery.get().getUserId() + "/" + blogGallery.get().getGalleryName();
			//System.out.println("Update Gallery Id = "+ userGalleryFolder);
			
			
			 File tmpDir = new File(userGalleryFolder);
			    boolean exists = tmpDir.exists();
			//System.out.println("Is the folder exists = "+ exists);
			
			
//			System.out.println("Blog Gallery = "+blogGallery.isPresent() +" Blog = "+ blog.isPresent() +" Blog galler user Id  = "+ blogGallery.get().getUserId());
//			System.out.println("User Id = "+userId +" Blog  Created By "+blog.get().getCreatedBy()+ " Blog original Create "+blog.get().getOriginalCreator().getId());
//			if((blogGallery.isPresent() && blog.isPresent()) && 
//					(blogGallery.get().getUserId() == userId || 
//					blog.get().getCreatedBy() == userId || 
//					blog.get().getOriginalCreator().getId() == userId)) {
			
			
			
			if( blogGallery.isPresent() && blog.isPresent()
				&& ( blogGallery.get().getUserId().equals(userId) 
					||blog.get().getOriginalCreator().getId().equals(userId) ||blog.get().getCreatedBy().equals(userId) )){
				
				if(exists == false) {
					//System.out.println("No gallery folder in backend, but have record on database ");
					Path userGalleryFolderPath = Paths.get(userGalleryFolder);
					
					try {
			            Files.createDirectories(userGalleryFolderPath);
			        }
			        catch (IOException e) {
			            throw new StorageException("Could not create user folder", e);
			        }
				}
				
				
	
				BlogGalleryDetail new_detail = blogGalleryService.storeToUserFolder(file, galleryId);
				
				List<BlogGalleryDetail> fileList = blogGalleryService.findByGalleryId(blogGallery.get());
				logger.updateBlogGallery(user, galleryId, "", "Success",channel);
		        return JsonResult.fileList("Gallery Upload Successfully",new_detail,fileList,session);
				
				//return JsonResult.ok(return_blogGallery,"Gallery Created");
			}else{
				//return JsonResult.errorMap(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
				return JsonResult.errorMsg("Invalid Request");
			}
		}
	}
	
	@RequestMapping("/gallery/finish/{userId}")
    public ResponseEntity<JsonResult> createGallery(@PathVariable Long userId, HttpSession session) {
		//System.out.println(userId);
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			if(submitGallery(userId) == null) {
				return JsonResult.errorMsg("No Records");
			}
			else{
				return JsonResult.ok(submitGallery(userId),"Is Finished",session);
			}
		}
    }
	
	@RequestMapping("/gallery/delete/{detailId}")
    public ResponseEntity<JsonResult> removeGallery(@PathVariable Long detailId,
    		@RequestParam(value="user") Long userId, HttpSession session) {
		//System.out.println(userId);
		User user = (User) session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		Integer checkUser = common.checkUserSession(userId, session);
		Integer is_admin = common.checkAdmin(user, session);
		
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			BlogGalleryDetail return_data = deleteGalleryDetail(detailId,user,is_admin, channel);
			if(return_data == null) {
				return JsonResult.errorMsg("No Records");
			}
			else{
				return JsonResult.ok(return_data,"Gallery Item " + detailId + "Is Deleted",session);
			}
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/assistant/getBlogger/{user_id}")
    public ResponseEntity<JsonResult> getAssistant(@PathVariable Long user_id,
    		@RequestParam(value="staffNo") String staffNo, HttpSession session) {
		//System.out.println(userId);
		Integer checkUser = common.checkUserSession(user_id, session);
		
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			//BlogGalleryDetail return_data = getAssistant(staffNo,user,is_admin, channel);
			User user = userService.findByStaffNo(staffNo);
			//TODO check user
			if(user == null) {
				return JsonResult.ok(null,"No Staff No",session);
			}
			List<Long> assistant_ids = common.checkAssistantId(session, user);
			System.out.println("Assistant_ids: " + assistant_ids);
			List<User> user_session_list = (List<User>) session.getAttribute("user_list");
			//System.out.println("Total user in session = "+user_session_list.size());
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			
			User return_user = new User();
			return_user.setStaffNo(user.getStaffNo());
			return_user.setFullname(user.getFullname());
			return_user.setChineseName(user.getChineseName());
			return_user.setInstitutionId(user.getInstitutionId());
			return_user.setRankId(user.getRankId());
			return_user.setSectionId(user.getSectionId());
			return_user.setInstitution(institution_session.stream()
				 .filter((n) -> n.getId().equals(return_user.getInstitutionId()))
				 .map(InstitutionsModel::getInstName)
				 .collect(Collectors.toList())
				 .get(0));
			return_user.setRank(rank_session.stream()
				 .filter((n) -> n.getId().equals(return_user.getRankId()))
				 .map(RanksModel::getRankName)
				 .collect(Collectors.toList())
				 .get(0));
			return_user.setSection((section_session.stream()
				 .filter((n) -> n.getId().equals(return_user.getSectionId()))
				 .map(SectionModel::getSectionName)
				 .collect(Collectors.toList())
				 .get(0)));
			if(assistant_ids == null || assistant_ids.size() == 0 || assistant_ids.get(0).equals(0L)) {
				return_user.setAssistant(null);
				return JsonResult.ok(return_user,"No Records",session);
			}else{
				List<User> assistant_data = user_session_list.stream()
						 .filter((n) -> assistant_ids.contains(n.getId()))
						 .map((u) -> {
							 u.setInstitution(institution_session.stream()
									 .filter((n) -> n.getId().equals(u.getInstitutionId()))
									 .map(InstitutionsModel::getInstName)
									 .collect(Collectors.toList())
									 .get(0));
							 u.setRank(rank_session.stream()
									 .filter((n) -> n.getId().equals(u.getRankId()))
									 .map(RanksModel::getRankName)
									 .collect(Collectors.toList())
									 .get(0));
							 u.setSection(section_session.stream()
									 .filter((n) -> n.getId().equals(u.getSectionId()))
									 .map(SectionModel::getSectionName)
									 .collect(Collectors.toList())
									 .get(0));
							 return u;
						 })
						 .collect(Collectors.toList());
				return_user.setAssistant(assistant_data);
				//System.out.println("return_data: " + return_data);
				return JsonResult.ok(return_user,"Assist List of " + staffNo, session);
			}
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/assistant/getOriginal/{user_id}")
    public ResponseEntity<JsonResult> getOriginal(@PathVariable Long user_id,HttpSession session) {
		//System.out.println(userId);
		Integer checkUser = common.checkUserSession(user_id, session);
		
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			//User user = userService.findByStaffNo(staffNo);
			List<Long> original_ids = common.checkOriginalId(session, user_id);
			//System.out.println("Assistant_ids: " + assistant_ids);
			if(original_ids == null || original_ids.size() == 0 || original_ids.get(0).equals(0L)) {
				return JsonResult.ok(null, session);
			}else{
				List<User> user_session_list = (List<User>) session.getAttribute("user_list");
				List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
				List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
				List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
				List<User> original_data = user_session_list.stream()
										 .filter((n) -> original_ids.contains(n.getId()))
										 .map((u) -> {
											 u.setInstitution(institution_session.stream()
													 .filter((n) -> n.getId().equals(u.getInstitutionId()))
													 .map(InstitutionsModel::getInstName)
													 .collect(Collectors.toList())
													 .get(0));
											 u.setRank(rank_session.stream()
													 .filter((n) -> n.getId().equals(u.getRankId()))
													 .map(RanksModel::getRankName)
													 .collect(Collectors.toList())
													 .get(0));
											 u.setSection(section_session.stream()
													 .filter((n) -> n.getId().equals(u.getSectionId()))
													 .map(SectionModel::getSectionName)
													 .collect(Collectors.toList())
													 .get(0));
											 return u;
										 })
										 .collect(Collectors.toList());
				//System.out.println("return_data: " + return_data);
				return JsonResult.ok(original_data,"Original List of User ID " + user_id, session);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/assistant/create/{user_id}")
    public ResponseEntity<JsonResult> creteAssistant(@PathVariable Long user_id,
    		@RequestBody JsonNode jsonNode, HttpSession session) {
		//System.out.println(userId);
		Integer checkUser = common.checkUserSession(user_id, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
		    String bloggerStaffNo = jsonNode.get("bloggerStaffNo").asText();
			String assistantStaffNo = jsonNode.get("assistantStaffNo").asText();
			List<User> user_list = (List<User>) session.getAttribute("user_list");
			Long bloggerId = user_list.stream().filter((n) -> n.getStaffNo().equals(bloggerStaffNo)).map(User::getId).collect(Collectors.toList()).get(0);
			User assistant = user_list.stream().filter((n) -> n.getStaffNo().equals(assistantStaffNo)).collect(Collectors.toList()).get(0);
			
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			assistant.setInstitution(institution_session.stream()
					 .filter((n) -> n.getId().equals(assistant.getInstitutionId()))
					 .map(InstitutionsModel::getInstName)
					 .collect(Collectors.toList())
					 .get(0));
			assistant.setRank(rank_session.stream()
					 .filter((n) -> n.getId().equals(assistant.getRankId()))
					 .map(RanksModel::getRankName)
					 .collect(Collectors.toList())
					 .get(0));
			assistant.setSection((section_session.stream()
					 .filter((n) -> n.getId().equals(assistant.getSectionId()))
					 .map(SectionModel::getSectionName)
					 .collect(Collectors.toList())
					 .get(0)));
			
			BlogAssistant new_assistant = blogService.createAssistant(bloggerId,assistant.getId(),user_id);
			if(new_assistant == null) {
				return JsonResult.errorMsg("Assistant is already assigned to " + bloggerStaffNo);
			}else {
				return JsonResult.ok(assistant,"Create assistant for " + bloggerStaffNo + " successfully.",session);
			}
		}
	}
	
//	@SuppressWarnings("unchecked")
	@RequestMapping("/assistant/delete/{user_id}")
    public ResponseEntity<JsonResult> deleteAssistant(@PathVariable Long user_id,
    		@RequestBody JsonNode jsonNode, HttpSession session) {
		//System.out.println("Bolg deleted = "+user_id);
		Integer checkUser = common.checkUserSession(user_id, session);
		//System.out.println("------------------------------------");
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			String bloggerStaffNo = jsonNode.get("bloggerStaffNo").asText();
			String assistantStaffNo = jsonNode.get("assistantStaffNo").asText();
			List<User> user_list = (List<User>) session.getAttribute("user_list");
		
			Long bloggerId = user_list.stream().filter((n) -> n.getStaffNo().equals(bloggerStaffNo)).map(User::getId).collect(Collectors.toList()).get(0);
			Long assistant = user_list.stream().filter((n) -> n.getStaffNo().equals(assistantStaffNo)).map(User::getId).collect(Collectors.toList()).get(0);
			blogService.deleteAssistant(bloggerId, assistant, user_id);
			return JsonResult.ok(session);
		}
	}


	private BlogGallery submitGallery(Long userId) {
		Optional<BlogGallery> blogGallery = blogGalleryService.findActive(userId);
		//System.out.println(userId);
		
		if(blogGallery.isPresent()) {
			blogGallery.get().setModifiedAt(new Date());
			blogGallery.get().setModifiedBy(userId);
			blogGallery.get().setFinishedAt(new Date());
			blogGallery.get().setIsFinished(1);
			
			BlogGallery return_blogGallery = blogGalleryService.updateBlogGallery(blogGallery.get());
			
			return return_blogGallery;
		}
		else{
			return null;
		}
	}
	
	private BlogGalleryDetail deleteGalleryDetail(Long detailId, User user, Integer is_admin, Integer channel) {
		Optional<BlogGalleryDetail> blogGalleryDetail = blogGalleryService.findDetailById(detailId);
		//System.out.println(userId);
		
		if(blogGalleryDetail.isPresent()) {
			Optional<BlogGallery> blogGallery = blogGalleryService.findById(blogGalleryDetail.get().getGalleryId());
			Optional<Blog> blog = blogService.findById(blogGallery.get().getPostId());
			if(blogGalleryDetail.get().getCreatedBy().equals(user.getId()) 
					|| is_admin.equals(1)
					|| (blogGallery.isPresent() && blog.isPresent() && blog.get().getOriginalCreator().getId().equals(user.getId()))
			) {
				blogGalleryDetail.get().setModifiedAt(new Date());
				blogGalleryDetail.get().setModifiedBy(user.getId());
				blogGalleryDetail.get().setDeletedAt(new Date());
				blogGalleryDetail.get().setDeletedBy(user.getId());
				blogGalleryDetail.get().setDeleted(1);
				
				BlogGalleryDetail return_blogGallery = blogGalleryService.saveDetail(blogGalleryDetail.get());
				logger.deleteBlogGallery(user, detailId, "", "Success",channel);
				return return_blogGallery;
			}else {
				return null;
			}
		}
		else{
			return null;
		}
	}
	
	private List<String> getPostTags(JsonNode jsonNode) {
		//System.out.println(jsonNode.get("tagList"));
		List<String> return_data = new ArrayList<String>();
		for(Integer i = 0; i < jsonNode.get("tagList").size(); i++) {
			return_data.add(jsonNode.get("tagList").get(i).asText());
		}
		return return_data;
	}
	
	
	private static String getUrlContents(String theUrl) {
	    StringBuilder content = new StringBuilder();

	    // many of these calls can throw exceptions, so i've just
	    // wrapped them all in one try/catch statement.
	    try
	    {
	      // create a url object
	      URL url = new URL(theUrl);
	      //System.out.println("URL - "+theUrl);

	      URLConnection urlConnection = url.openConnection();

	     
	      //System.out.println("URL Connection + "+ urlConnection);
	      //System.out.println("URL get Input Stream + "+ urlConnection.getInputStream());
	      //System.out.println("New Input Stream Reader = "+new InputStreamReader(urlConnection.getInputStream()));
	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	    
	      //System.out.println("BufferedReader "+ bufferedReader);
	      String line;

	      // read from the urlconnection via the bufferedreader
	      while ((line = bufferedReader.readLine()) != null)
	      {
	        content.append(line + "\n");
	      }
	      bufferedReader.close();
	    }
	    catch(Exception e)
	    {
	    	//System.out.println("Exception== "+e);
	      e.printStackTrace();
	    }
	    //System.out.println("Content - "+ content);
	    return content.toString();
	  }
	
}

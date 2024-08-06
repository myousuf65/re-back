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
import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.NewsCorner2Assistant;
import com.hkmci.csdkms.entity.NewsCorner2Gallery;
import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;
import com.hkmci.csdkms.entity.NewsCorner2Tag;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.Uinbox;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.PostReturnModel;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.service.NewsCorner2GalleryDetailService;
import com.hkmci.csdkms.service.NewsCorner2GalleryService;
import com.hkmci.csdkms.service.NewsCorner2Service;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.UinboxService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;
import com.vdurmont.emoji.EmojiParser;

@CrossOrigin
@RestController
@RequestMapping("/newscorner2")
public class NewsCorner2Controller {
	
	@Autowired
	@Resource
    private NewsCorner2GalleryService newscorner2GalleryService;
	
	@Autowired
	@Resource
    private NewsCorner2GalleryDetailService newscorner2GalleryDetailService;
	
	@Autowired
	@Resource
    private LogService logger;
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource
    private NewsCorner2Service newscorner2Service;
	
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
				
				Long newscorner2Id = jsonNode.get("newscorner2Id").asLong();
				List<Long> user_list = dealWithStaffNo(jsonNode);
				List<User> user_session_list = (List<User>) session.getAttribute("user_list");
				User user = (User) session.getAttribute("user_session"); 
				String newscorner2_title = newscorner2Service.findById(newscorner2Id).get().getPostTitle();
						
						
						
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
						new_share.setResourceId(newscorner2Id);
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
				
				EmailUtil.sendEmailToShareMiniBlog(sendEmaiList, newscorner2_title, user.getFullname(),Long.parseLong(user_check.get("access_channel")),newscorner2Id);
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
					StorelogEmail(session,user,newscorner2Id,channel, today,user.getScore());
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
			Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findActive(userId);
			//System.out.println("201:"+newscorner2Gallery.get());
			
			if(newscorner2Gallery.isPresent()) {
				Date createdAt = new Date();
				
				String dateOfTest = jsonNode.get("publishAt").asText();
				
				Date publishAt = common.textToDate(dateOfTest);
		        NewsCorner2 new_newscorner2 = new NewsCorner2();
		        Integer checkNewsCorner2Creator = common.checkNewsCorner2Creator(user, session, jsonNode);

		        if(checkNewsCorner2Creator.equals(1)) {
		        	Optional<User> creator = userService.findById(jsonNode.get("originalCreator").asLong());
		        	if(creator.isPresent()) {
		        		
		        	}
		        	//System.out.println("Creator = "+ creator.get().getFullname());
		        	new_newscorner2.setCategoryId(jsonNode.get("categoryId").asLong());
			        new_newscorner2.setContent(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText()));
			        new_newscorner2.setCreatedAt(createdAt);
			        new_newscorner2.setCreatedBy(jsonNode.get("createdBy").asLong());
			        new_newscorner2.setHit(0L);
			        new_newscorner2.setIs_public(jsonNode.get("isPublic").asLong());
			        new_newscorner2.setInstId(1L);
			        new_newscorner2.setSectionId(1L);
			        new_newscorner2.setRankId(1L);
			        new_newscorner2.setPublishAt(publishAt);
			        new_newscorner2.setIsDeleted(0);
			        new_newscorner2.setPostTitle(jsonNode.get("postTitle").asText());
			        new_newscorner2.setShowAsAlias(jsonNode.get("showAsAlias").asInt());
			        new_newscorner2.setAlias(jsonNode.get("alias").asText());
			        new_newscorner2.setOriginalCreator(creator.get());
			        
			        if(jsonNode.get("videoLink") == null) {
			        	new_newscorner2.setPostVideoLink("");
			        } else {
			        	new_newscorner2.setPostVideoLink(jsonNode.get("videoLink").asText());
			        }
			        
			        //System.out.println("video link = " + new_newscorner2.getPostVideoLink());
			        
			        //System.out.println(EmojiParser.parseToHtmlDecimal(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText())));
			        
			        Long return_newscorner2_id = newscorner2Service.add(new_newscorner2).getId();
			        newscorner2Gallery.get().setPostId(return_newscorner2_id);
			        newscorner2GalleryService.updateNewsCorner2Gallery(newscorner2Gallery.get());
			        
			        List<String> newscorner2Tags = getPostTags(jsonNode);
					for(Integer z = 0; z < newscorner2Tags.size(); z++) {
						newscorner2Service.createNewsCorner2Tag(EmojiParser.removeAllEmojis(newscorner2Tags.get(z)), return_newscorner2_id,userId);
					}
			        
			        if(submitGallery(userId) == null) {
			        	
						return JsonResult.errorMsg("No Gallery Records");
					}
					else{
						//return JsonResult.ok(submitGallery(userId),"Is Finished");
						logger.createNewsCorner2(user, 0L, "", "Success",channel);
						return JsonResult.ok(return_newscorner2_id,"Post Create Successfully", session);
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
	
	@RequestMapping("/update/{newscorner2Id}")
	public ResponseEntity<JsonResult> update(@RequestBody JsonNode jsonNode,@PathVariable Long newscorner2Id, HttpSession session) {
		Optional<NewsCorner2> newscorner2 = newscorner2Service.findById(newscorner2Id);
		//System.out.println(userId);
		
		if(newscorner2.isPresent()) {
			Integer checkNewsCorner2User = common.checkNewsCorner2User(jsonNode, session, newscorner2.get(),"update");
			if(checkNewsCorner2User.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				
				Date publishAt = common.textToDate(jsonNode.get("publishAt").asText());
			
				Date modifiedAt = new Date();
		        
		        NewsCorner2 new_newscorner2 = newscorner2.get();
		        //new_newscorner2.setCategoryId(jsonNode.get("CategoryId").asLong());
		        new_newscorner2.setContent(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText()));
		        //new_newscorner2.setCreatedBy(jsonNode.get("createdBy").asLong());
		        new_newscorner2.setHit(newscorner2.get().getHit() );//+ 1
		        new_newscorner2.setIs_public(jsonNode.get("isPublic").asLong());
		        new_newscorner2.setModifiedy(jsonNode.get("modifiedBy").asLong());
		        new_newscorner2.setAlias(jsonNode.get("alias").asText());
		        new_newscorner2.setModifiedAt(modifiedAt);
		        new_newscorner2.setPublishAt(publishAt);
		        //new_newscorner2.setIsDeleted(0);
		        new_newscorner2.setPostTitle(jsonNode.get("postTitle").asText());
		        new_newscorner2.setShowAsAlias(jsonNode.get("showAsAlias").asInt());
		        //new_newscorner2.setOriginalCreator(jsonNode.get("originalCreator").asLong());
		        
		        if(jsonNode.get("videoLink") == null) {
		        	new_newscorner2.setPostVideoLink("");
		        } else {
		        	new_newscorner2.setPostVideoLink(jsonNode.get("videoLink").asText());
		        }
		        
		        
		        newscorner2Service.update(new_newscorner2);
		        
		       // dealWithTags(jsonNode,newscorner2.get().getId());
		        
		        dealWithTags(jsonNode,newscorner2Id);
				logger.updateNewsCorner2(user, newscorner2Id, "", "Success",channel);
				return JsonResult.ok("Post Update Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	private void dealWithTags(JsonNode jsonNode, Long newscorner2Id) {
		// TODO Auto-generated method stub
		List<String> newscorner2Tags = getPostTags(jsonNode);
		
		List<NewsCorner2Tag> tags_in_db = newscorner2Service.getNewsCorner2Tags(newscorner2Id);
		
		List<NewsCorner2Tag> tags_valid_in_DB = tags_in_db.stream()
									  .filter((n) -> n.getIsDeleted().equals(0))
									  .collect(Collectors.toList());
		List<String> tags_valid_id_in_DB = tags_in_db.stream()
										 .filter((n) -> n.getIsDeleted().equals(0))
										 .map(NewsCorner2Tag::getTag)
										 .collect(Collectors.toList());
		List<NewsCorner2Tag> tags_deleted_in_DB = tags_in_db.stream()
									    .filter((n) -> n.getIsDeleted().equals(1))
									    .collect(Collectors.toList());
		
		//List<NewsCorner2Tag> tags = new ArrayList<>();
		
		List<NewsCorner2Tag> tags_to_be_added = newscorner2Tags.stream()
										 .filter((n) -> !tags_valid_id_in_DB .contains(n))
										 .map(
												 (n) -> {
													 NewsCorner2Tag temp = new NewsCorner2Tag(newscorner2Id,EmojiParser.removeAllEmojis(n),0);
													 return temp;
												 }
										 ).collect(Collectors.toList());
		//System.out.println("Tags To Be Added size: " + tags_to_be_added.size());
		newscorner2Service.saveAllTags(tags_to_be_added);
		
		List<NewsCorner2Tag> tags_to_be_updated = tags_deleted_in_DB.stream()
				 .filter((n) -> newscorner2Tags.contains(n.getTag()))
				 .map(
						 (n) -> {
							 NewsCorner2Tag temp = new NewsCorner2Tag(n.getId(),newscorner2Id,EmojiParser.removeAllEmojis(n.getTag()),0);
							 return temp;
						 }
				 ).collect(Collectors.toList());
		newscorner2Service.updateAllTags(tags_to_be_updated);
		//System.out.println("Tags To Be Updated size: " + tags_to_be_updated.size());
		
		List<NewsCorner2Tag> tags_to_be_deleted = tags_valid_in_DB.stream()
				 .filter((n) -> !newscorner2Tags.contains(n.getTag()))
				 .map(
						 (n) -> {
							 NewsCorner2Tag temp = new NewsCorner2Tag(n.getId(),newscorner2Id,EmojiParser.removeAllEmojis(n.getTag()),1);
							 return temp;
						 }
				 ).collect(Collectors.toList());
		//tags.addAll(tags_to_be_deleted);
		
		newscorner2Service.deleteAllTags(tags_to_be_deleted);
		//System.out.println("Tags To Be Deleted size: " + tags_to_be_deleted.size());
		
//		for(Integer i = 0; i < newscorner2Tags.size(); i++) {
//			newscorner2Service.createNewsCorner2Tag(EmojiParser.removeAllEmojis(newscorner2Tags.get(i)), newscorner2Id,null);
//		}
		
	}

	@RequestMapping("/delete/{newscorner2Id}")
	public ResponseEntity<JsonResult> delete(@RequestBody JsonNode jsonNode,@PathVariable Long newscorner2Id,HttpSession session) {
		Optional<NewsCorner2> newscorner2 = newscorner2Service.findById(newscorner2Id);
		//System.out.println(userId);
		
		if(newscorner2.isPresent()) {
			Integer checkNewsCorner2User = common.checkNewsCorner2User(jsonNode, session, newscorner2.get(),"delete");
			if(checkNewsCorner2User.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				Date deletedAt = new Date();		        
		        NewsCorner2 new_newscorner2 = newscorner2.get();
		        new_newscorner2.setDeletedBy(jsonNode.get("user").asLong());
		        new_newscorner2.setDeletedAt(deletedAt);
		        new_newscorner2.setIsDeleted(1);
		        
		        newscorner2Service.update(new_newscorner2);
		        logger.deleteNewsCorner2(user, newscorner2Id, "", "Success",channel);
				return JsonResult.ok("Post Delete Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	
	@RequestMapping("/related/{userId}")
	public ResponseEntity<JsonResult> getRelated(@PathVariable Long userId, @RequestParam(value="post") Long postId, HttpSession session){
		List<NewsCorner2> return_data = newscorner2Service.getRelate(postId);
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
	@RequestMapping("/getDetail/{newscorner2Id}")
	public ResponseEntity<JsonResult> getDetail(@PathVariable Long newscorner2Id, @RequestParam(value="user") Long userId, HttpSession session ) {
		Optional<NewsCorner2> newscorner2 = newscorner2Service.findById(newscorner2Id);
		Integer today = userService.findTodayScoreById(userId);
		Integer score = userService.findScoreById(userId);
	
		System.out.println("Today Score = "+today+ " Score = "+score);

		//Integer added =0;
		if(newscorner2.isPresent()) {
			Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findByPostId(newscorner2.get().getId());
			if(newscorner2Gallery.isPresent()) {
			
				Integer checkUser = common.checkUserSession(userId, session);
				
				if(checkUser.equals(0)) {
					return JsonResult.errorMsg("Request User Status Error");
				}else {
			
					User user = (User) session.getAttribute("user_session");
					System.out.println("NewsCorner2 Controller line 435 = user score : "+user.getScore());
					List<User> user_list_session = (List<User>) session.getAttribute("user_list");
					Integer channel = (Integer) session.getAttribute("channel");
					List<NewsCorner2GalleryDetail> fileList = newscorner2GalleryService.findByGalleryId(newscorner2Gallery.get());
					//PostReturnModel return_data = newscorner2Service.getNewsCorner2ById(newscorner2.get().getId(),userId,user_list_session);
					
					//PostReturnModel return_data = newscorner2Service.getNewsCorner2ById(newscorner2.get().getId(),newscorner2.get().getOriginalCreator().getId(),user_list_session);
					PostReturnModel return_data = newscorner2Service.getNewsCorner2ById(newscorner2.get().getId(),userId,user_list_session,newscorner2.get().getOriginalCreator().getId());
					if(return_data == null) {
						return JsonResult.list("Post Not Found (get newscorner2 not found )", null,0,session);
					}else{
//						String content = return_data.getNewsCorner2().getContent();
//						return_data.getNewsCorner2().setContent(content.replace("\"", "\\\""));
						
						System.out.println("NewsCorner2 Controller - line 449 :User Today Score = "+ today +" Total Score = "+score);
//						if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
							if(today<29) {
						
								user.setScore(score+2);
							
							} else if (28<today && today<30) {
								user.setScore(score+1);
							
							}
//						}
							
						//rewrite video link
						NewsCorner2 _newscorner2 = return_data.getNewsCorner2();
						if(_newscorner2.getPostVideoLink() != null && !_newscorner2.getPostVideoLink().equals("")) {
							String video = _newscorner2.getPostVideoLink();		
							String[] name = video.split("/KMS/");
							
							if(video.startsWith("https://ams.csd.gov.hk/KMS/")) {
								String param1 ="https://ams.csd.gov.hk/api/vodapi.php?";
								String param2 =	"file="+name[1]+"&folder=/KMS/&key=test1";
								String param = param1 +param2;
								String videoLink =getUrlContents(param);
								_newscorner2.setPostVideoLink(videoLink);
							} else {
								_newscorner2.setPostVideoLink("");
							}
						}
						return_data.setNewsCorner2(_newscorner2);
						
							
						session.setAttribute("user_session", user);
						new Thread(()-> {
							Storelog2(session,user,newscorner2Id,channel,return_data, today,user.getScore());
						}).start();

						if(fileList.size() == 0) {
							return JsonResult.fileList("Post Found Successfully", return_data,newscorner2Gallery.get().getId(),session);
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
	@RequestMapping("/getDetail2/{newscorner2Id}")
	public ResponseEntity<JsonResult> getDetail2(@PathVariable Long newscorner2Id, @RequestParam(value="user") Long userId, HttpSession session ) {
		Optional<NewsCorner2> newscorner2 = newscorner2Service.findById(newscorner2Id);
		Integer today = userService.findTodayScoreById(userId);
		Integer score = userService.findScoreById(userId);
	
		System.out.println("Today Score = "+today+ " Score = "+score);

		//Integer added =0;
		if(newscorner2.isPresent()) {
			Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findByPostId(newscorner2.get().getId());
			if(newscorner2Gallery.isPresent()) {
			
				Integer checkUser = common.checkUserSession(userId, session);
				
				if(checkUser.equals(0)) {
					return JsonResult.errorMsg("Request User Status Error");
				}else {
			
					User user = (User) session.getAttribute("user_session");
					System.out.println("NewsCorner2 Controller line 435 = user score : "+user.getScore());
					List<User> user_list_session = (List<User>) session.getAttribute("user_list");
					Integer channel = (Integer) session.getAttribute("channel");
					List<NewsCorner2GalleryDetail> fileList = newscorner2GalleryService.findByGalleryId(newscorner2Gallery.get());
					//PostReturnModel return_data = newscorner2Service.getNewsCorner2ById(newscorner2.get().getId(),userId,user_list_session);
					
					//PostReturnModel return_data = newscorner2Service.getNewsCorner2ById(newscorner2.get().getId(),newscorner2.get().getOriginalCreator().getId(),user_list_session);
					PostReturnModel return_data = newscorner2Service.getNewsCorner2ById(newscorner2.get().getId(),userId,user_list_session,newscorner2.get().getOriginalCreator().getId());
					if(return_data == null) {
						return JsonResult.list("Post Not Found (get newscorner2 not found )", null,0,session);
					}else{
//						String content = return_data.getNewsCorner2().getContent();
//						return_data.getNewsCorner2().setContent(content.replace("\"", "\\\""));
						
						System.out.println("NewsCorner2 Controller - line 449 :User Today Score = "+ today +" Total Score = "+score);
//						if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
							if(today<29) {
						
								user.setScore(score+2);
							
							} else if (28<today && today<30) {
								user.setScore(score+1);
							
							}
//						}
							
						
							
						session.setAttribute("user_session", user);
						new Thread(()-> {
							Storelog2(session,user,newscorner2Id,channel,return_data, today,user.getScore());
						}).start();

						if(fileList.size() == 0) {
							return JsonResult.fileList("Post Found Successfully", return_data,newscorner2Gallery.get().getId(),session);
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
	
	
	public void StorelogEmail(HttpSession session, User user,Long newscorner2Id,Integer channel,Integer today,Integer total  ) {
		Log log =logger.shareNewsCorner2(user, newscorner2Id, "", "Success",channel,0);
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
	
	
	
	
	
	
	
	
	
	public void Storelog2(HttpSession session, User user,Long newscorner2Id,Integer channel,PostReturnModel return_data ,Integer today,Integer total  ) {
		newscorner2Service.addHit(newscorner2Id,user.getId());
		Log log =logger.viewNewsCorner2(user, newscorner2Id, "", "Success",channel,Integer.parseInt(return_data.getNewsCorner2().getCategoryId().toString()));
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
		

		System.out.println("NewsCorner2 Controller ------- line 585 : Score "+ user.getScore());
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


		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {

			if(selSorter.split(":").length < 2) {
				return JsonResult.errorMsg("Sorting Condition Error");
			}else {
				String sortBy = selSorter.split(":")[0];
				String sortOrder = selSorter.split(":")[1];
				sortBy = sortBy == "publishDate" ? "publishAt" : sortBy;
				
//				//System.out.println("Sort by: " + sortBy + " " + sortOrder);
				List<PostReturnModel> return_data = newscorner2Service.searchNewsCorner2ByCategoryId(categoryId, sortBy, sortOrder, page);
				Integer return_total = newscorner2Service.getTotalSearchNewsCorner2ByCategoryId(categoryId, sortBy, sortOrder, page);
				//System.out.println("Get Total Post Size: " + return_total);
				if(return_data == null) {
					return JsonResult.list("Post List Empty", return_data,0,session);
				}else{
					return JsonResult.list("Post List Searching", return_data.stream().map(
							(b) -> {
								return b;
							}
					).collect(Collectors.toList()),return_total,session);
				}
			}
		}
    }
	
	/*
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
				List<User> user_list_session = (List<User>) session.getAttribute("user_list");
				
				List<NewsCorner2GalleryDetail> newscorner2_thumb = newscorner2GalleryDetailService.getAllThumb();
				List<PostReturnModel> latest_newscorner2s = newscorner2Service.searchNewsCorner2ByLatest(user_list_session,newscorner2_thumb);
				
				//System.out.println("After get latest newscorner2s ");
				
				List<Long> latest_newscorner2_ids = new ArrayList<Long>();
				for(Integer i = 0; i < latest_newscorner2s.size(); i++) {
					latest_newscorner2_ids.add(latest_newscorner2s.get(i).getNewsCorner2().getId());
				}
//				//System.out.println("Sort by: " + sortBy + " " + sortOrder);
				List<PostReturnModel> return_data = newscorner2Service.searchNewsCorner2ByCategoryId(categoryId, sortBy, sortOrder, page,latest_newscorner2_ids,user_list_session,newscorner2_thumb);
				Integer return_total = newscorner2Service.getTotalSearchNewsCorner2ByCategoryId(categoryId, sortBy, sortOrder, page,latest_newscorner2_ids,user_list_session,newscorner2_thumb);
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
    } */
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getMyPosts/{userId}")
	public ResponseEntity<JsonResult> get_my_posts(@PathVariable Long userId,HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			List<PostReturnModel> return_data = newscorner2Service.getMyPosts(userId,user_list_session);
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
			List<PostReturnModel> return_data = newscorner2Service.getMyBookmarks(userId,user_list_session);
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
			/* User user = (User) session.getAttribute("user_session");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<NewsCorner2GalleryDetail> newscorner2_thumb = newscorner2GalleryDetailService.getAllThumb();
			Integer channel = (Integer) session.getAttribute("channel");
			List<PostReturnModel> return_data = newscorner2Service.searchNewsCorner2ByLatest(user_list_session,newscorner2_thumb);
			if(isLoged.equals(1)) {logger.viewNewsCorner2List(user, 0L, "", "Success",channel);}
			if(return_data == null) {
				return JsonResult.list("Post List Empty", return_data,0,session);
			}else{
				return JsonResult.list("Post List Searching", return_data,return_data.size(),session);
			} */
			return JsonResult.list("Post List Empty", null,0,session);
		}
	}

	@RequestMapping("/getLatest/youtube/{userId}/{isLoged}")
	public ResponseEntity<JsonResult> getLatestYouTube(@PathVariable Long userId,@PathVariable Integer isLoged, HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			/* User user = (User) session.getAttribute("user_session");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<NewsCorner2GalleryDetail> newscorner2_thumb = newscorner2GalleryDetailService.getAllThumb();
			Integer channel = (Integer) session.getAttribute("channel");
			List<PostReturnModel> return_data = newscorner2Service.searchNewsCorner2ByLatest(user_list_session,newscorner2_thumb);
			if(isLoged.equals(1)) {logger.viewNewsCorner2List(user, 0L, "", "Success",channel);}
			if(return_data == null) {
				return JsonResult.list("Post List Empty", return_data,0,session);
			}else{
				return JsonResult.list("Post List Searching", return_data,return_data.size(),session);
			} */
			return JsonResult.list("Post List Empty", null,0,session);
		}
	}


	/*
	 * @SuppressWarnings("unchecked")
	@RequestMapping("/getLatest/{userId}/{isLoged}")
	public ResponseEntity<JsonResult> getLatest(@PathVariable Long userId,@PathVariable Integer isLoged, HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<NewsCorner2GalleryDetail> newscorner2_thumb = newscorner2GalleryDetailService.getAllThumb();
			Integer channel = (Integer) session.getAttribute("channel");
			List<PostReturnModel> return_data = newscorner2Service.searchNewsCorner2ByLatest(user_list_session,newscorner2_thumb);
			if(isLoged.equals(1)) {logger.viewNewsCorner2List(user, 0L, "", "Success",channel);}
			if(return_data == null) {
				return JsonResult.list("Post List Empty", return_data,0,session);
			}else{
				return JsonResult.list("Post List Searching", return_data,return_data.size(),session);
			}
		}
	}
	*/

	@RequestMapping("/gallery/check/{userId}")
    public ResponseEntity<JsonResult> checkGallery(@PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findActive(userId);
			//System.out.println("Start check temp folder   -"+userId);
			if(newscorner2Gallery.isPresent()) {
//				System.out.println("Bolg Gallery Present!! ");
				List<NewsCorner2GalleryDetail> fileList = newscorner2GalleryService.findByGalleryId(newscorner2Gallery.get());
//				System.out.println("file List "+fileList);
				return JsonResult.fileList("Gallery Existed", newscorner2Gallery.get(), fileList,session);
			}
			else{
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				Md5Encode md5 = new Md5Encode();
				Date now = new Date();
		        String gallery_name = md5.getMD5("Holfer" + now.toString());
		        
		        NewsCorner2Gallery new_newscorner2Gallery = new NewsCorner2Gallery();
		        new_newscorner2Gallery.setGalleryName(gallery_name);
		        new_newscorner2Gallery.setCreatedBy(userId);
		        new_newscorner2Gallery.setCreatedAt(now);
		        new_newscorner2Gallery.setIsFinished(0);
		        new_newscorner2Gallery.setUserId(userId);
		        
		        NewsCorner2Gallery return_newscorner2Gallery = newscorner2GalleryService.newNewsCorner2Gallery(new_newscorner2Gallery);
				logger.createNewsCorner2Gallery(user, return_newscorner2Gallery.getId(), "", "Success",channel);
				//return JsonResult.ok(return_newscorner2Gallery,"Gallery Created");
				return JsonResult.fileList("Gallery Created", return_newscorner2Gallery, null,session);
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
			Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findActive(userId);
			//System.out.println("gallery upload --- "+userId);
			
			String folder = "D:\\csdkms\\backend\\";
			String folderFinal = folder + userId + "\\"+newscorner2Gallery.get().getGalleryName();
			//System.out.println("folder Final path = "+ folderFinal);
			
			 File tmpDir = new File(folderFinal);
			    boolean exists = tmpDir.exists();
			//System.out.println("Is the folder exists = "+ exists);
			
			
			
			
			if(newscorner2Gallery.isPresent()) {
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				//System.out.println("Present!!!");
				NewsCorner2GalleryDetail new_detail = newscorner2GalleryService.storeToUserFolder(file, newscorner2Gallery.get().getId());
				//System.out.println("New Detail --- : "+ new_detail);
				logger.uploadNewsCorner2Gallery(user, newscorner2Gallery.get().getId(), "", "Success", channel);
				List<NewsCorner2GalleryDetail> fileList = newscorner2GalleryService.findByGalleryId(newscorner2Gallery.get());
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
			Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findById(galleryId);
			
			Path userFilePath = storageService.getUserFolderLocation();
			
			//System.out.println("NewsCorner2 Gallery = "+ newscorner2Gallery.get().getId());
			Optional<NewsCorner2> newscorner2 = newscorner2Service.findById(newscorner2Gallery.get().getPostId());
			
			String userGalleryFolder = userFilePath.toString() + "/" + newscorner2Gallery.get().getUserId() + "/" + newscorner2Gallery.get().getGalleryName();
			//System.out.println("Update Gallery Id = "+ userGalleryFolder);
			
			
			 File tmpDir = new File(userGalleryFolder);
			    boolean exists = tmpDir.exists();
			//System.out.println("Is the folder exists = "+ exists);
			
			
//			System.out.println("NewsCorner2 Gallery = "+newscorner2Gallery.isPresent() +" NewsCorner2 = "+ newscorner2.isPresent() +" NewsCorner2 galler user Id  = "+ newscorner2Gallery.get().getUserId());
//			System.out.println("User Id = "+userId +" NewsCorner2  Created By "+newscorner2.get().getCreatedBy()+ " NewsCorner2 original Create "+newscorner2.get().getOriginalCreator().getId());
//			if((newscorner2Gallery.isPresent() && newscorner2.isPresent()) && 
//					(newscorner2Gallery.get().getUserId() == userId || 
//					newscorner2.get().getCreatedBy() == userId || 
//					newscorner2.get().getOriginalCreator().getId() == userId)) {
			
			
			
			if( newscorner2Gallery.isPresent() && newscorner2.isPresent()
				&& ( newscorner2Gallery.get().getUserId().equals(userId) 
					||newscorner2.get().getOriginalCreator().getId().equals(userId) ||newscorner2.get().getCreatedBy().equals(userId) )){
				
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
				
				
	
				NewsCorner2GalleryDetail new_detail = newscorner2GalleryService.storeToUserFolder(file, galleryId);
				
				List<NewsCorner2GalleryDetail> fileList = newscorner2GalleryService.findByGalleryId(newscorner2Gallery.get());
				logger.updateNewsCorner2Gallery(user, galleryId, "", "Success",channel);
		        return JsonResult.fileList("Gallery Upload Successfully",new_detail,fileList,session);
				
				//return JsonResult.ok(return_newscorner2Gallery,"Gallery Created");
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
			NewsCorner2GalleryDetail return_data = deleteGalleryDetail(detailId,user,is_admin, channel);
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
			//NewsCorner2GalleryDetail return_data = getAssistant(staffNo,user,is_admin, channel);
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
		    String newscorner2gerStaffNo = jsonNode.get("newscorner2gerStaffNo").asText();
			String assistantStaffNo = jsonNode.get("assistantStaffNo").asText();
			List<User> user_list = (List<User>) session.getAttribute("user_list");
			Long newscorner2gerId = user_list.stream().filter((n) -> n.getStaffNo().equals(newscorner2gerStaffNo)).map(User::getId).collect(Collectors.toList()).get(0);
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
			
			NewsCorner2Assistant new_assistant = newscorner2Service.createAssistant(newscorner2gerId,assistant.getId(),user_id);
			if(new_assistant == null) {
				return JsonResult.errorMsg("Assistant is already assigned to " + newscorner2gerStaffNo);
			}else {
				return JsonResult.ok(assistant,"Create assistant for " + newscorner2gerStaffNo + " successfully.",session);
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
			String newscorner2gerStaffNo = jsonNode.get("newscorner2gerStaffNo").asText();
			String assistantStaffNo = jsonNode.get("assistantStaffNo").asText();
			List<User> user_list = (List<User>) session.getAttribute("user_list");
		
			Long newscorner2gerId = user_list.stream().filter((n) -> n.getStaffNo().equals(newscorner2gerStaffNo)).map(User::getId).collect(Collectors.toList()).get(0);
			Long assistant = user_list.stream().filter((n) -> n.getStaffNo().equals(assistantStaffNo)).map(User::getId).collect(Collectors.toList()).get(0);
			newscorner2Service.deleteAssistant(newscorner2gerId, assistant, user_id);
			return JsonResult.ok(session);
		}
	}


	private NewsCorner2Gallery submitGallery(Long userId) {
		Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findActive(userId);
		//System.out.println(userId);
		
		if(newscorner2Gallery.isPresent()) {
			newscorner2Gallery.get().setModifiedAt(new Date());
			newscorner2Gallery.get().setModifiedBy(userId);
			newscorner2Gallery.get().setFinishedAt(new Date());
			newscorner2Gallery.get().setIsFinished(1);
			
			NewsCorner2Gallery return_newscorner2Gallery = newscorner2GalleryService.updateNewsCorner2Gallery(newscorner2Gallery.get());
			
			return return_newscorner2Gallery;
		}
		else{
			return null;
		}
	}
	
	private NewsCorner2GalleryDetail deleteGalleryDetail(Long detailId, User user, Integer is_admin, Integer channel) {
		Optional<NewsCorner2GalleryDetail> newscorner2GalleryDetail = newscorner2GalleryService.findDetailById(detailId);
		//System.out.println(userId);
		
		if(newscorner2GalleryDetail.isPresent()) {
			Optional<NewsCorner2Gallery> newscorner2Gallery = newscorner2GalleryService.findById(newscorner2GalleryDetail.get().getGalleryId());
			Optional<NewsCorner2> newscorner2 = newscorner2Service.findById(newscorner2Gallery.get().getPostId());
			if(newscorner2GalleryDetail.get().getCreatedBy().equals(user.getId()) 
					|| is_admin.equals(1)
					|| (newscorner2Gallery.isPresent() && newscorner2.isPresent() && newscorner2.get().getOriginalCreator().getId().equals(user.getId()))
			) {
				newscorner2GalleryDetail.get().setModifiedAt(new Date());
				newscorner2GalleryDetail.get().setModifiedBy(user.getId());
				newscorner2GalleryDetail.get().setDeletedAt(new Date());
				newscorner2GalleryDetail.get().setDeletedBy(user.getId());
				newscorner2GalleryDetail.get().setDeleted(1);
				
				NewsCorner2GalleryDetail return_newscorner2Gallery = newscorner2GalleryService.saveDetail(newscorner2GalleryDetail.get());
				logger.deleteNewsCorner2Gallery(user, detailId, "", "Success",channel);
				return return_newscorner2Gallery;
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

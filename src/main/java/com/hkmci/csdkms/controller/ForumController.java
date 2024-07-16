package com.hkmci.csdkms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.EmailUtil;
import com.hkmci.csdkms.entity.ForumAdmin;
import com.hkmci.csdkms.entity.ForumGallery;
import com.hkmci.csdkms.entity.ForumGalleryDetail;
import com.hkmci.csdkms.entity.ForumPost;
import com.hkmci.csdkms.entity.Uinbox;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.BlogCommentModel;
import com.hkmci.csdkms.model.CategoryModel;
import com.hkmci.csdkms.model.ForumAdminCateReturnModel;
import com.hkmci.csdkms.model.ForumCategoryModel;
import com.hkmci.csdkms.model.ForumCommentModel;
import com.hkmci.csdkms.model.ForumLikeModel;
import com.hkmci.csdkms.model.ForumPostDetailReturnModel;
import com.hkmci.csdkms.model.ForumPostReturnModel;
import com.hkmci.csdkms.repository.BlogAssistantRepository;
import com.hkmci.csdkms.repository.ForumGalleryRepository;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.service.AccessRuleService;

import com.hkmci.csdkms.service.ForumPostService;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.ResourceRatingService;
import com.hkmci.csdkms.service.UinboxService;
import com.hkmci.csdkms.storage.StorageService;
import com.lowagie.text.DocumentException;

@CrossOrigin
@RestController
@RequestMapping("/forum")
public class ForumController {
	
	
	private static final Boolean False = null;

	@Autowired
	@Resource
	private ForumPostService forumPostService;
	
	@Autowired
	@Resource
	private Common common;
	
	
	@Autowired
	@Resource
	private AccessRuleService accessRuleService;
	
	@Autowired
	@Resource
	private UserRepository userRepository;
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	@Autowired
	@Resource
	private LogService logService;
	
	@Autowired
	@Resource 
	private UinboxService uinboxService;


	@Autowired
	@Resource
	private ResourceRatingService resourceRatingService;
	
	
	
	
	private  ForumGalleryRepository forumGalleryRepository;

	
	private  BlogAssistantRepository blogAssistantRepository;
	
	@RequestMapping("/post/remove/photo/{userId}")
	public ResponseEntity<JsonResult> delete(@PathVariable Long userId , HttpSession session){
		HashMap<String, String> user_check= common.checkUser(userId,session);
		if (user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			//System.out.println("User access rule = "+accessRuleIdList);
			
			
			
			
			return JsonResult.ok(null, session);
			
			
		}
		
	}
	
	
	
	@RequestMapping("/homepage/hotTopic/{userId}")
	public ResponseEntity<JsonResult> hotTopic(@PathVariable Long userId , HttpSession session){
		HashMap<String, String> user_check= common.checkUser(userId,session);
		if (user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			Object user_session = session.getAttribute("user_session");
			String access_channel = session.getAttribute("channel").toString();
			//System.out.println("Access Channel = "+ access_channel);	
					
			User user = (User) user_session;
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			//System.out.println("User access rule = "+accessRuleIdList);

			
			//Integer check_result= forumPostService.checkAccessRule(accessRuleIdList, categoryId);
			
			//System.out.println("Start - ");
			List<ForumPost> hotPost = forumPostService.getHotTopic(user.getUsergroup(),accessRuleIdList,user.getStaffNo(),access_channel);

			return JsonResult.ok(hotPost, session);
			
			
		}
		
	}
	
	
//
//	@RequestMapping("/rating/{userId}")
//	public ResponseEntity<JsonResult> RatingResource(@PathVariable Long userId ,@RequestBody JsonNode jsonNode,
//			HttpSession session) throws IOException, DocumentException{
//		HashMap<String, String> user_check = common.checkUser(userId, session);
//		if (user_check.get("msg") != "") {
//			return JsonResult.errorMsg(user_check.get("msg").toString());
//		} else {
//			
//			Long resource_id = jsonNode.get("resourceId").asLong();
//			Integer rating = jsonNode.get("rating").asInt();
//			Object user_session = session.getAttribute("user_session");
//			User user = (User) user_session;
//			
//			Float return_rating = resourceRatingService.userRatingResource(userId, resource_id, rating);
//			
////			Float return_rating = Float.valueOf((float) 5.0);
//					
//			return JsonResult.ok(return_rating,session);
//		}
//	}
//	
//	
	
	
	@RequestMapping("/homepage/category/{userId}")
	public ResponseEntity<JsonResult> homeCate(@PathVariable Long userId, HttpSession session){
		HashMap<String, String> user_check= common.checkUser(userId,session);
		if (user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			//System.out.println("Forum controller ");
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
		
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			
			String access_channel = session.getAttribute("channel").toString();
			//System.out.println("Access Channe = "+ access_channel);
			 List<String> channel = new ArrayList<>() ;
			  //System.out.println("Access Channel = "+ access_channel);
		        if(String.valueOf(access_channel).equals("1")) {
		        	channel.add("0");
		        	channel.add("1");
		        	channel.add("2");
		        }else {
		        	channel.add("2");
		        }
			//
		//	//System.out.println(" User can access Category = "+canAccessCategory);
			if(user.getUsergroup()==5) {
			
			List<ForumCategoryModel> cat = forumPostService.getAllCategory(0L);
			//System.out.println("cat size = "+ cat.size());
			//System.out.println("After get= ");
			List<ForumCategoryModel> cat2 = cat.stream().filter(c->c.getParentForumId()==0 && c.getIsDeleted() == false
					&&  channel.contains(c.getAccessChannel()))
				.collect(Collectors.toList());

			//System.out.println("Main Category = "+ cat2.size());
			for (Integer i =0; i<cat2.size();i++) {
				List<ForumCategoryModel> cat3 = getAdminChildren(cat2.get(i).getId(),cat,channel);
				cat2.get(i).setsubCate(cat3);
			}
			return JsonResult.ok(cat2, session);
			
		}else {

			List<ForumCategoryModel> cat = forumPostService.getAllCategory(0L);
			
			List<ForumCategoryModel> cat2 = cat.stream().filter(c->c.getParentForumId()==0 && c.getIsDeleted() == false
					&&  channel.contains(c.getAccessChannel()))
				.collect(Collectors.toList());

			//System.out.println("Main Category = "+ cat2.size());
			for (Integer i =0; i<cat2.size();i++) {
				List<ForumCategoryModel> cat3 = getChildren(cat2.get(i).getId(),accessRuleIdList,user.getStaffNo(),channel);
				cat2.get(i).setsubCate(cat3);
			}
			return JsonResult.ok(cat2, session);
		
			}
		}
	}
	
	public List<ForumCategoryModel> getChildren(Long parentId, List<Long> accessRule, String staff_no, List<String> channel){ 
		List<ForumCategoryModel> return_data = new ArrayList<ForumCategoryModel>();
		
			
			
//		List<ForumCategoryModel> entity_list = all_category.stream().filter( c -> c.getParentForumId().equals(parentId) 
//				 && c.getLevel() ==2 && c.getIsDeleted()==false && c.getShowInfo() ==1  
//				 && categoryIds.contains(c.getId())==true )
//				.sorted(Comparator.comparing(ForumCategoryModel::getOrderBy))
//				.collect(Collectors.toList());
//	
		List<ForumCategoryModel> entity_list = forumPostService.getHomeSubCategory(parentId, accessRule, staff_no,channel);
			
		
		//System.out.println("Parent Id = "+ parentId +" Entity size "+ entity_list.size());
		return entity_list;  
	}

	public List<ForumCategoryModel> getAdminChildren(Long parentId,List<ForumCategoryModel> all_category, List<String> channel){ 
		//System.out.println("Get Admin Children = "+channel);
		List<ForumCategoryModel> entity_list = all_category.stream().filter( c -> c.getParentForumId().equals(parentId) 
				 && c.getLevel() ==2 && c.getIsDeleted()==false &&  channel.contains(c.getAccessChannel()))
				.sorted(Comparator.comparing(ForumCategoryModel::getOrderBy))
				.collect(Collectors.toList());
		
	
		List<ForumCategoryModel> return_data = new ArrayList<ForumCategoryModel>();
		
		if(entity_list.size() != 0){
			entity_list.sort(Comparator.comparing(ForumCategoryModel::getOrderBy));
			
			for(Integer i = 0; i < entity_list.size(); i++) {
				List<ForumCategoryModel> children = getAdminChildren(entity_list.get(i).getId(),all_category,channel);
				entity_list.get(i).setsubCate(children);
				
			}

			return entity_list;
		}else{
			
			return return_data;  
		}  
	}
	
	
	@RequestMapping("/post/details/{postId}/{userId}")
	public ResponseEntity<JsonResult> postDetail(@PathVariable Long postId, @PathVariable Long userId, HttpSession session){
		HashMap<String, String> user_check= common.checkUser(userId,session);
		if (user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			//System.out.println("access rule list ="+accessRuleIdList);
			

			String access_channel = session.getAttribute("channel").toString();
			//System.out.println("Access Channe = "+ access_channel);
			 List<String> channel = new ArrayList<>() ;
			  //System.out.println("Access Channel = "+ access_channel);
		        if(String.valueOf(access_channel).equals("1")) {
		        	channel.add("0");
		        	channel.add("1");
		        	channel.add("2");
		        }else {
		        	channel.add("2");
		        }
			ForumPostDetailReturnModel post = forumPostService.getPost(accessRuleIdList, postId, userId,user.getUsergroup(), channel, user.getStaffNo());	
			
		
			logService.viewForumPost(user, postId, "", "Success", 1, post.getPost().getCategoryId());
			
			//System.out.println("--- " + post.size());
			
			//List<ForumPost> relatedPost= new ArrayList<>();
		//	List<ForumPost> relatedPost= forumPostService.getRelativePost(accessRuleIdList, post.get(0).getForumId(), postId);
		//	System.out.println("related Post = " + relatedPost.size());
			//System.out.println("related Post = " + relatedPost.get(0).getPostTitle());
			return JsonResult.ok(post,session);
			
			
			
			
		}
	
		
	}
	
	
	

	@SuppressWarnings("unchecked")
	private List<Long> getAccessRuleIds(HashMap<String, String> user_check,HttpSession session) {
		//Object user_session = session.getAttribute("user_session");
		Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		
		Long section = Long.parseLong(user_check.get("user_section").toString());
		Long institution = Long.parseLong(user_check.get("user_institution").toString());
		Long rank = Long.parseLong(user_check.get("user_rank").toString());
		//Long accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
		
        if (user_access_rule_session == null) {
        	
        	
        	List<Long> accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
            //System.out.println("No access_rule session，set access_rule = " + accessRuleId);
            session.setAttribute("user_access_rule_session", accessRuleId);
            return accessRuleId;
        } else {
            //System.out.println("User access rule session exist(getAccessRuleIds) :" + user_access_rule_session);
            
            return (List<Long>) user_access_rule_session;
        }
	}
	
	
	
	@RequestMapping("/category/family/{userId}/{catId}")
	public ResponseEntity<JsonResult> familyPath(@PathVariable Long userId,@PathVariable Long catId, HttpSession session){
		

		String access_channel = session.getAttribute("channel").toString();
		//System.out.println("Access Channe = "+ access_channel);
		 List<String> channel = new ArrayList<>() ;
		  //System.out.println("Access Channel = "+ access_channel);
	        if(String.valueOf(access_channel).equals("1")) {
	        	channel.add("0");
	        	channel.add("1");
	        	channel.add("2");
	        }else {
	        	channel.add("2");
	        }
		return JsonResult.ok(forumPostService.getFamilyPath(catId, channel),session);
	}



	@RequestMapping("/post/new/{userId}")
	public ResponseEntity<JsonResult> createPost(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session){
		HashMap<String, String> user_check= common.checkUser(userId,session);
		if (user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			//System.out.println("User access rule = "+accessRuleIdList);
			Optional<ForumGallery> forumGallery = forumPostService.checkExistGallery(userId);

			String access_channel = session.getAttribute("channel").toString();
			//System.out.println("Access Channe = "+ access_channel);
			 List<String> channel = new ArrayList<>() ;
			  //System.out.println("Access Channel = "+ access_channel);
		        if(String.valueOf(access_channel).equals("1")) {
		        	channel.add("0");
		        	channel.add("1");
		        	channel.add("2");
		        }else {
		        	channel.add("2");
		        }
			
			
			
//			List<Long> check_result= forumPostService.checkAccessRuleCreate(accessRuleIdList,user.getStaffNo() ,jsonNode.get("category").asLong(),channel);
//			
//			System.out.println("Can User create = "+check_result+" category id = "+jsonNode.get("category").asLong());
//			
			
			ForumPost new_post = new ForumPost();
			ForumPost return_data = new ForumPost();
				new_post.setCanComment(jsonNode.get("allowComment").asInt());
				new_post.setPostTitle(jsonNode.get("title").asText());
				new_post.setContent(jsonNode.get("content").asText());
				new_post.setForumId(jsonNode.get("category").asLong());
				new_post.setCreatedAt(new Date());
				new_post.setCreatedBy(userId);
				new_post.setHit(0);
				new_post.setIsDeleted(0);
				new_post.setOrderBy(jsonNode.get("order").asInt());
				new_post.setShowAsAlias(jsonNode.get("isAlias").asInt());
				new_post.setAllowComment(jsonNode.get("allowComment").asInt());
				if(jsonNode.get("hiddenField").asText() == "null") {
					//System.out.println("hidden Filed null ");
				
				} else {
					new_post.setHiddenField(jsonNode.get("hiddenField").asText());
				}
				
				if(jsonNode.get("isAlias").asInt() == 1) {
					new_post.setAlias(jsonNode.get("alias").asText());
				}
				return_data=forumPostService.add(new_post);
				if(forumGallery.isPresent()) {
				ForumGallery SaveGallery = forumGallery.get();
				SaveGallery.setIsFinished(1);
				SaveGallery.setFinishedAt(new Date());
				SaveGallery.setPostId(return_data.getId());
				
				forumPostService.saveGallery(SaveGallery);
				}
				
				
				//-----------Start Email-------------------------
				String showedName = jsonNode.get("isAlias").asText().equals("0") ? user.getFullname() : jsonNode.get("alias").asText();
				
				List<Object> sendEmailToFourmAdmin = forumPostService.getAdminByCategory(jsonNode.get("category").asLong());	
				 
							
				List<String> tryLong = new ArrayList<>();
				
				
				for(Integer i =0 ; i<sendEmailToFourmAdmin.size();i++ ) {
					tryLong.add(sendEmailToFourmAdmin.get(i).toString());
				}
				
				
				
				
				//System.out.println("How many Fourm Admin = "+ sendEmailToFourmAdmin.size());			
				if( sendEmailToFourmAdmin.size() != 0) {
				List<User> sendToList = userRepository.getByUserId(tryLong);
				
				//System.out.println("Use List = "+sendToList.size());
				
				EmailUtil.sendEmailCreateForumPost(sendToList,jsonNode.get("title").asText(), showedName);
				}
				
				
				
				
				
				
				
				
				
				
			return JsonResult.ok(return_data,session);
			
			
		}
	}
	
	@RequestMapping("/post/updateget/{userId}/{postId}")
	public ResponseEntity<JsonResult> updateget(@PathVariable Long userId, @PathVariable Long postId , HttpSession session){

		String access_channel = session.getAttribute("channel").toString();
		
		
			Optional<ForumPost> update_post =  forumPostService.find(postId);
			User user = (User) session.getAttribute("user_session");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			
			ForumGallery gallery = new ForumGallery() ;
			Optional<ForumGallery> forumGallery =  forumPostService.searchByPostId(postId);
			//System.out.println("Is present + "+ forumGallery.isPresent() +"Is empty? "+ forumGallery.isEmpty()+ "    "  );
			
			//System.out.println("User group = "+ user.getUsergroup());
		
			if( update_post.isPresent()) {
				Integer canDeleteAdmin = forumPostService.canDeletePost(postId, userId, user.getStaffNo());
				
				if(update_post.get().getCreatedBy() == userId || user.getUsergroup() == 5 || canDeleteAdmin >0) {
				
				ForumPostReturnModel return_data = new ForumPostReturnModel();
				return_data.setAlias(update_post.get().getAlias());
				return_data.setIsAlias(update_post.get().getShowAsAlias());
				return_data.setAllowComment(update_post.get().getAllowComment());
				return_data.setCategoryId(update_post.get().getForumId());
				return_data.setTitle(update_post.get().getPostTitle());
				return_data.setContent(update_post.get().getContent());
				if(update_post.get().getHiddenField() =="null") {
					
				} else {
					return_data.setHiddenField(update_post.get().getHiddenField());
				}
				return_data.setCanEdit(1);
				if(gallery!=null) {
				return_data.setGalleryId(gallery.getId());
				}
				User creater = userRepository.findById(userId).get();

				return_data.setCreatedBy(user_list_session.stream().filter(
						(u) -> u.getId().equals(update_post.get().getCreatedBy())
						).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
				
				
				//System.out.println("Created By( post/updateget)  = "+return_data.getCreatedBy());
				return_data.setOrder(update_post.get().getOrderBy());
				
				
					return JsonResult.ok(return_data,session);
				} else {
					return JsonResult.errorMsg("Not allow update this post. ");
				}
		
			} else {
				return JsonResult.errorMsg("Post does not exist.");
			}
	}

	
	@RequestMapping("/post/update/{userId}")
	public ResponseEntity<JsonResult> update(@RequestBody JsonNode jsonNode, @PathVariable Long userId , HttpSession session){
			
		
		
		//System.out.println("Post Id = "+ jsonNode.get("id").asLong()); 
		Optional<ForumPost> update_post =  forumPostService.find(jsonNode.get("id").asLong());
			
			
			
			//System.out.println("Update post = "+ update_post.get().getId());
			User user = (User) session.getAttribute("user_session");
		
			//System.out.println("User group = "+ user.getUsergroup());
		
			if( update_post.isPresent()) {
				

				Integer canDeleteAdmin = forumPostService.canDeletePost(update_post.get().getId(), userId, user.getStaffNo());
				
				if(update_post.get().getCreatedBy() == userId || user.getUsergroup() == 5 || canDeleteAdmin > 0) {
				
					update_post.get().setModifiedAt(new Date());
					update_post.get().setModifiedBy(userId);
					update_post.get().setPostTitle(jsonNode.get("title").asText());
					update_post.get().setContent(jsonNode.get("content").asText());
					update_post.get().setForumId(jsonNode.get("category").asLong());
					update_post.get().setCanComment(jsonNode.get("allowComment").asInt());
					update_post.get().setAllowComment(jsonNode.get("allowComment").asInt());
					update_post.get().setOrderBy(jsonNode.get("order").asInt());
					update_post.get().setShowAsAlias(jsonNode.get("isAlias").asInt());
					
					if (jsonNode.get("hiddenField").asText() == "null") {
						update_post.get().setHiddenField(null);
					}else {
					update_post.get().setHiddenField(jsonNode.get("hiddenField").asText());
					}
					if(jsonNode.get("isAlias").asBoolean()==true) {
						update_post.get().setAlias(jsonNode.get("alias").asText());
					}	
				
					return JsonResult.ok(forumPostService.update(update_post.get()),session);
				} else {
					return JsonResult.errorMsg("Not allow update this post. ");
				}
		
			} else {
				return JsonResult.errorMsg("Post does not exist.");
			}
	}
	
	@RequestMapping("/post/delete/{postId}/{userId}")
	public ResponseEntity<JsonResult> delete(@RequestBody JsonNode jsonNode,@PathVariable Long postId, @PathVariable Long userId, HttpSession session){
		Optional<ForumPost> delete_post =  forumPostService.find(postId);
		
		User user = (User) session.getAttribute("user_session");
		//System.out.println("User staff no = "+ user.getStaffNo());
		if( delete_post.isPresent()) {
			//System.out.println("Post created By = "+ delete_post.get().getCreatedBy());
			Integer canDeleteAdmin = forumPostService.canDeletePost(postId, userId, user.getStaffNo());
			
			
			if(delete_post.get().getCreatedBy() == userId || user.getUsergroup() == 5 || canDeleteAdmin>0) {
				ForumPost post = delete_post.get();
				post.setIsDeleted(1);
				post.setDeletedAt(new Date());
				post.setDeletedBy(userId);
				return JsonResult.ok(forumPostService.update(post),session);
			} else {
				return JsonResult.errorMsg("No right to delete post. ");
			}
			
		}else {
			return JsonResult.errorMsg("Post does not exist. ");
		}
		
		
	}
	
	@RequestMapping("/comment/new/{userId}")
	public ResponseEntity<JsonResult> createComment(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session){
		
		User user = (User) session.getAttribute("user_session");
		
			
			ForumCommentModel comment = new ForumCommentModel();
			comment.setIsDeleted(0);
			comment.setCreatedAt(new Date());
			comment.setCreatedBy(userId);
			comment.setContent(jsonNode.get("content").asText());
			comment.setIsReply2cmnt(jsonNode.get("isReply2Cmnt").asLong());
			comment.setPostId(jsonNode.get("postId").asLong());
			comment.setShowAsAlias(jsonNode.get("isAlias").asInt());
			if(jsonNode.get("isAlias").asBoolean()) {
				comment.setAlias(jsonNode.get("alias").asText());
			} 
			ForumCommentModel retrun_data = forumPostService.addComment(comment);
			List<User> user_session_list = (List<User>) session.getAttribute("user_list");
			String showedName = jsonNode.get("isAlias").asText().equals("0") ? user.getFullname() : jsonNode.get("alias").asText();
			//System.out.println("User List size = "+ user_session_list.size());
			//System.out.println("Showed Name = "+ showedName);
			
			
			
			
			
			//-----------Start Email-------------------------
			ForumPost post_title = forumPostService.getPostTitle(jsonNode.get("postId").asLong());
			List<Object> sendEmailToFourmAdmin = forumPostService.getAdminByPost(jsonNode.get("postId").asLong());
			List<String> tryLong = new ArrayList<>();
			tryLong.add(post_title.getCreatedBy().toString());
			for(Integer i =0 ; i<sendEmailToFourmAdmin.size();i++ ) {
				tryLong.add(sendEmailToFourmAdmin.get(i).toString());
			}
			
			
			
			
			//System.out.println("How many Fourm Admin = "+ sendEmailToFourmAdmin.size());			
			if( sendEmailToFourmAdmin.size() != 0) {
			List<User> sendToList = userRepository.getByUserId(tryLong);
			
			//System.out.println("Use List = "+sendToList.size());
			
			EmailUtil.sendEmailForumComment(sendToList, post_title.getPostTitle(), showedName, jsonNode.get("content").asText());
			}
			
			
			return JsonResult.ok(retrun_data,session);
			
		
		
	}
	
	@RequestMapping("/comment/delete/{commentId}/{userId}")
	public ResponseEntity<JsonResult> deleteComment(@PathVariable Long commentId, @PathVariable Long userId,HttpSession session){
		
		Optional<ForumCommentModel> comment= forumPostService.findComment(commentId);
		User user = (User) session.getAttribute("user_session");
		//System.out.println("User staff no = "+user.getStaffNo());
		//Integer canDeleteAdmin = forumPostService.canDeletePost(postId, userId, user.getStaffNo());
		Integer canDeleteAdmin=0;
				canDeleteAdmin= forumPostService.canDeleteComment(commentId, userId, user.getStaffNo());
			if (canDeleteAdmin == null) {
				canDeleteAdmin =0;
			}
				//System.out.println("get created by " + comment.get().getCreatedBy()+ " User Id " + userId);
				//System.out.println("canDeleteAdmin "+ canDeleteAdmin);
		
				ForumCommentModel delete_comment = new ForumCommentModel();
				delete_comment = comment.get();
				delete_comment.setIsDeleted(1);
				delete_comment.setDeletedAt(new Date());
				delete_comment.setDeletedBy(userId);
			
				return JsonResult.ok(forumPostService.updateComment(delete_comment),session);
			
	}
	
	
	
	@RequestMapping("/comment/getcomment/{postId}")
	public ResponseEntity<JsonResult> getForumComment(@PathVariable Long postId,
			@RequestParam(name="page") Integer page , @RequestParam(name="user") Long userId, HttpSession session){
		List<User> user_list_session = (List<User>) session.getAttribute("user_list");
		Integer limitStart = (page - 1) * 20;
		//Integer limitEnd = page * 5;
		Integer limitEnd =  20;
		//System.out.println("start " + limitStart + " end " + limitEnd);
		List<Long> limitCommentIds = forumPostService.getPageComments(postId, limitStart, limitEnd);
		//System.out.println("Limit Comment size = " + limitCommentIds.size());
		Map<String,Object> return_data = forumPostService.getComments(postId, page, userId, limitCommentIds,user_list_session);
		if(return_data == null) {

						return JsonResult.twoList("Here is Comments data ",null,0,0,session);
		}else {
		@SuppressWarnings("unchecked")
		List<BlogCommentModel> return_list = (List<BlogCommentModel>) return_data.get("list");
		//System.out.println("how many return = "+ return_list.size());
		return JsonResult.twoList("Here is Comments data ",return_list,Integer.parseInt(return_data.get("total").toString()),Integer.parseInt(return_data.get("total_for_page").toString()),session);
		}
		
	}

	
	@RequestMapping("/category/post/{userId}")
	public ResponseEntity<JsonResult> getCategoryPost(@PathVariable Long userId,
			@RequestParam (name="categoryId") Long categoryId ,@RequestParam (name="page") Integer page ,HttpSession session){
		
//			System.out.println("User Id : " + userId+ " Post Id : " + categoryId + " Page : " + page);

			String access_channel = session.getAttribute("channel").toString();
			//System.out.println("Access Channe = "+ access_channel);
			 List<String> channel = new ArrayList<>() ;
			  //System.out.println("Access Channel = "+ access_channel);
		        if(String.valueOf(access_channel).equals("1")) {
		        	channel.add("0");
		        	channel.add("1");
		        	channel.add("2");
		        }else {
		        	channel.add("2");
		        }
		        
			HashMap<String,String> user_check = common.checkUser(userId, session);
			if( user_check.get("msg") !="") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
				Object user_session = session.getAttribute("user_session");
				User user = (User) user_session;
				
				//System.out.println("See the channel = "+ channel);
//				Integer is_admin = common.checkAdmin(user, session);
//				String staff_no = user.getStaffNo();
				List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
//				System.out.println("access rule id list = "+accessRuleIdList);
				List<Long> check_result= forumPostService.checkAccessRule(accessRuleIdList,user.getStaffNo() ,categoryId,channel);
//				System.out.println("Retrun Data (check access rule ) "+check_result.size());
				if(user.getUsergroup()==5 ||check_result.size()>0 ) {
					List<ForumPostReturnModel> return_data = forumPostService.getCategoryPost(categoryId, page);
					Integer total =  forumPostService.getCateogryTotal(categoryId);
//					System.out.println("Total Post = "+total);
					return JsonResult.listTotal("",return_data,total,session);
					
				}else {
					return JsonResult.errorMsg("You have no right get in");
					
				}
			}
		
	}
	
	
	
	@RequestMapping("/category/{userId}/{categoryId}")
	public ResponseEntity<JsonResult> getSubCategory(@PathVariable Long userId, @PathVariable Long categoryId, HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg") !="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			Object user_session =session.getAttribute("user_session");
			User user =(User) user_session;
			Integer access_channel = (Integer) session.getAttribute("channel");
			 List<String> channel = new ArrayList<>() ;
//			  System.out.println("Access Channel = "+ access_channel);
		        if(String.valueOf(access_channel).equals("1")) {
		        	channel.add("0");
		        	channel.add("1");
		        	channel.add("2");
		        }else {
		        	channel.add("2");
		        }
//			System.out.println("See the channel "+ channel);
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			List<ForumCategoryModel> entity_list = new ArrayList<>();
			//Integer check_result= forumPostService.checkAccessRule(accessRuleIdList, categoryId);
			if(user.getUsergroup()==5L) {
				entity_list = forumPostService.getSubCategory(categoryId,channel);
			}else {
			entity_list = forumPostService.getHomeSubCategory(categoryId, accessRuleIdList, user.getStaffNo(),channel);
			}	
			return JsonResult.ok(entity_list,session);
			
			
		}
	}
	
	
	@RequestMapping ("/admin/category/newadmin/{userId}")
	public ResponseEntity<JsonResult> createNewAdmin(@PathVariable Long userId,@RequestBody JsonNode jsonNode,HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			Integer channel = (Integer) session.getAttribute("user_session");
//			System.out.println("User group Id - "+user.getUsergroup());
			
			if(user.getUsergroup() ==5) {
				
				
//					System.out.println(""+jsonNode.get("admin").get(i).asText());
					
				
				return JsonResult.ok(forumPostService.createAdmin(jsonNode.get("admin").asText(), jsonNode.get("categoryId").asLong(),userId),session);
			} else {
				return JsonResult.errorMsg("You have no right to create Admin  ");
			}
			
			
		}
		
//		
	}
	
	
	@RequestMapping("/admin/category/create/{userId}")
	public ResponseEntity<JsonResult> createForumCate(@RequestBody JsonNode jsonNode,@PathVariable Long userId, HttpSession session){
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if (user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
			
		}else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			if(user.getUsergroup() == 5) {

				String imgUrl = jsonNode.get("nameEn").asText();
//				System.out.println("IMG URL Controller " + imgUrl);
				forumPostService.createCategory( jsonNode, userId, user);
				
				//post.setAlias(jsonNode.get("content").asText());
				return JsonResult.ok(null,session);
			} else {
				return JsonResult.errorMsg("Don't have right to create forum category");
			}
		}
	}
	
	@RequestMapping("/admin/category/update/{userId}")
	public ResponseEntity<JsonResult> updateCateInfo(@PathVariable Long userId,@RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if (user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
			
		}else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			if(user.getUsergroup() == 5) {
				Long categoryId= jsonNode.get("id").asLong();
				//List<ForumAdminCateReturnModel> return_data = forumPostService.getAdminCategory(categoryId);
				
				forumPostService.updateCategory(jsonNode,  userId);
				
				
				return JsonResult.ok(null,session);
			} else {
				return JsonResult.errorMsg("Don't have right to create forum category");
			}
		}
	}
	
	
	
	@RequestMapping("/admin/category/get/{userId}/{categoryId}")
	public ResponseEntity<JsonResult> getAdminCategory(@PathVariable Long userId, @PathVariable Long categoryId, HttpSession session){
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!=""){
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			Object user_session = session.getAttribute("user_session");
			User user =(User) user_session;
			if(user.getUsergroup()==5) {
				List<ForumAdminCateReturnModel> return_data = forumPostService.getAdminCategory(categoryId);
				return JsonResult.ok(return_data,session);
				
			} else {
				return JsonResult.errorMsg("Don't have right to get in");
			}
		}
		
		
	}
	
	@RequestMapping("/admin/categoryall/{userId}")
	public ResponseEntity<JsonResult> getAdminCatAll(@PathVariable Long userId, HttpSession session){
		
		
		HashMap<String,String> user_check = common.checkUser(userId, session);
		Integer access_channel = (Integer) session.getAttribute("channel");
		 List<String> channel = new ArrayList<>() ;
//		  System.out.println("Access Channel = "+ access_channel);
	       
	        	channel.add("0");
	        	channel.add("1");
	        	channel.add("2");
	       
//		System.out.println("See the channel "+ channel);
		
		
		if(user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
			
		} else {
			
			Object user_session = session.getAttribute("user_session");
			
			
			User user = (User) user_session;
			if (user.getUsergroup()==5) {
				List<ForumCategoryModel> cat = forumPostService.getAllCategory(0L);
//				System.out.println("Admin Category All =");
				List<ForumCategoryModel> cat2 = cat.stream().filter(c->c.getParentForumId()==0 && c.getIsDeleted() == false
						)
						.collect(Collectors.toList());
//				System.out.println("Main Category = "+ cat2.size());
				for (Integer i =0; i<cat2.size();i++) {
					List<ForumCategoryModel> cat3 = getAdminChildren(cat2.get(i).getId(),cat,channel);
					cat2.get(i).setsubCate(cat3);
				}
				return JsonResult.ok(cat2, session);
				
				
			} else {
				return JsonResult.errorMsg("Don't have right to get here ");
			}
					
		}
		
	}
	
	@RequestMapping("/admin/category/delete/{categoryId}/{userId}")
	public ResponseEntity<JsonResult> deletedCate(@PathVariable Long categoryId, @PathVariable Long userId,
			HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
			
		} else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			if(user.getUsergroup() == 5) {
				
				forumPostService.deleteCategory1(categoryId, userId);
				return JsonResult.ok(session);
			}else {
				return JsonResult.errorMsg("Don't have right to delete");
			}
		}
		
	}
	
	
	
	
//	@RequestMapping("/category/info/{userId}/{categoryId}")
//	public ResponseEntity<JsonResult> getCateInfo(@PathVariable Long userId, @PathVariable Long categoryId,
//			HttpSession session){
//		HashMap<String, String> user_check = common.checkUser(userId,session);
//		
//		System.out.println("User Id = "+ userId);
//		System.out.println("Category Id = "+categoryId);
//		if(user_check.get("msg")!="") {
//			return JsonResult.errorMsg(user_check.get("msg").toString());
//			
//		} else {
//			Object user_session = session.getAttribute("user_session");
//			User user = (User) user_session;
//			//Integer channel = (Integer) session.getAttribute("user_session");
//			
//			ForumCategoryModel cat = forumPostService.getCategory(categoryId);
//			
//			ForumCategoryModel return_data = new ForumCategoryModel();
//			return_data.setNameEn(cat.getNameEn());
//			return_data.setId(cat.getId());
//			return_data.setNameTc(cat.getNameTc());
//			return_data.setImgUrl(cat.getImgUrl());
//			return_data.setTabStyle(cat.getTabStyle());
//			
//			return JsonResult.ok(return_data,session);
//		}
//	}
//	@RequestMapping("/post/new/photo/{userId}")
//	public  HashMap<String, String> getNewPhoto(@RequestParam("upload") MultipartFile file,
//            RedirectAttributes redirectAttributes,HttpSession session){
//	       storageService.store(file);
////	        redirectAttributes.addFlashAttribute("message",
////	                "You successfully uploaded " + file.getOriginalFilename() + "!");
//	        //JsonResult back_data = new JsonResult( file.getOriginalFilename());
//	        
//	        //final String nfilename;
//	        final String nfileType;
//	        final String nfilename;
//	        final String originalname;
//	        final String filesize;
////	        String file_name = System.currentTimeMillis()+file.getOriginalFilename(); 
//	        
//	        String file_name = StringUtils.cleanPath(file.getOriginalFilename());
//	        System.out.println("File Name = "+file_name);
//	        
//	        String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
//	        
//	        Md5Encode md5 = new Md5Encode();
//	        //nfilename = md5.getMD5(file_name)+fileType;
//	        nfilename = md5.getMD5(file_name)+fileType;
//	        
//	        nfileType = fileType;
//	        originalname = file_name;
//	        filesize = String.valueOf(file.getSize());
//	        
//	        //Original Checking
//	        
//	        HashMap<String, String > return_file_data = new HashMap<String, String>(){
//	        	/**
//				 * {
//			    "uploaded": 1,
//			    "fileName": "foo(2).jpg",
//			    "url": "/files/foo(2).jpg",
//			    "error": {
//			        "message": "A file with the same name already exists. The uploaded file was renamed to \"foo(2).jpg\"."
//			    }
//			}
//				 */
//				private static final long serialVersionUID = -575551168712619111L;
//
//				{
////					put("nfilename",nfilename); 
////					put("ofilename",originalname); 
////					put("originalname",originalname); 
////					put("filetype",nfileType); 
////					put("filesize",filesize); 
//					
//					put("uploaded","1");
//					put("fileName",originalname);
//					put("url","resources/"+nfilename);
//					
//					
//				}
//			};
//			
//			//Copy to another folder.
//			System.out.println("New file name = "+return_file_data.get("nfilename"));
//			
//	        return return_file_data;
//		
//		
//	}
	@RequestMapping("/category/info/{userId}/{categoryId}")
	public ResponseEntity<JsonResult> getCateInfo(@PathVariable Long userId, @PathVariable Long categoryId,
			HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		
		String access_channel = session.getAttribute("channel").toString();
//		System.out.println("Access Channe = "+ access_channel);
		 List<String> channel = new ArrayList<>() ;
//		  System.out.println("Access Channel = "+ access_channel);
	        if(String.valueOf(access_channel).equals("1")) {
	        	channel.add("0");
	        	channel.add("1");
	        	channel.add("2");
	        }else {
	        	channel.add("2");
	        }
		
		
//		System.out.println("User Id = "+ userId);
//		System.out.println("Category Id = "+categoryId);
		if(user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
			
		} else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			//Integer channel = (Integer) session.getAttribute("user_session");
			
			ForumCategoryModel cat = forumPostService.getCategory(categoryId);
			
			ForumCategoryModel return_data = new ForumCategoryModel();
			return_data.setNameEn(cat.getNameEn());
			return_data.setId(cat.getId());
			return_data.setNameTc(cat.getNameTc());
			return_data.setImgUrl(cat.getImgUrl());
			return_data.setTabStyle(cat.getTabStyle());
			
			return JsonResult.ok(return_data,session);
		}
	}
	
	
	
	
	@RequestMapping("/post/new/photo/{userId}/{galleryId}")
	public  HashMap<String, String> getNewPhoto(@PathVariable Long userId,@PathVariable String galleryId, @RequestParam("upload") MultipartFile file,
           RedirectAttributes redirectAttributes,HttpSession session){
//		   System.out.println("Start new photo ");
	       Integer checkUser = common.checkUserSession(userId, session);
	       String splitStr = null;
	       Integer j =0;
	       if( galleryId.indexOf("&")>0) {
	    	   j=  galleryId.indexOf("&");
	       }
	       
//	       System.out.println("Gallery Id ="+ j);
	       
	       
	       if(j != null) {
	    	   splitStr = galleryId.substring(0, j);
	       } else {
	    	   splitStr = galleryId;
	       }
	       
//	       System.out.println("Split String = "+ splitStr);
	     
	       
	       Optional<ForumGallery> forumGallery = forumPostService.checkExistGallery(userId);
	       ForumGallery newForumGallery = new ForumGallery();
           
	       Long gallery = Long.parseLong(splitStr.toString());
	       if (gallery != null) {
//	    	   System.out.println("Gallery id = "+ gallery);
	       }
	       if (gallery > 0 ) { 
	    	   Optional<ForumGallery> update_post =  forumGalleryRepository.findById(gallery);
	    	   newForumGallery	= update_post.get();
	       }else if(forumGallery.isPresent()) {
	    	   newForumGallery = forumGallery.get();
//	    	   System.out.println("Forum Gallery Present ");
// -- if gallery present 	    	   
	       } 
	       else {
//	    	   System.out.println("Forum Gallery NOT Present !!!!!");
	    	   User user = (User) session.getAttribute("user_session");
	    	   Integer channel =(Integer) session.getAttribute("channel");
	    	   Md5Encode md5 = new Md5Encode();
	    	   Date now= new Date();
	    	   String gallery_name = md5.getMD5("Holfer"+ now.toString());
	    	   
	    	   ForumGallery new_forumGallery =new ForumGallery();
	    	   new_forumGallery.setCreatedAt(now);
	    	   new_forumGallery.setCreatedBy(userId);
	    	   new_forumGallery.setGalleryName(gallery_name);
	    	   new_forumGallery.setIsFinished(0);
//	    	   newForumGallery.equals(new_forumGallery);
	    	   newForumGallery = forumPostService.newForumGallery(new_forumGallery);
	       }
//		System.out.println("Gallery Name = "+ newForumGallery.getGalleryName());

		ForumGalleryDetail new_detail = forumPostService.storeToUserFolder(file, newForumGallery.getId());
		
		List<ForumGalleryDetail> fileList = forumPostService.findByGalleryId(newForumGallery);
//		System.out.println("File List -- "+ fileList);
//		System.out.println("fileList Size = "+ fileList.size() );
//		System.out.println("Label = "+ fileList.get(fileList.size()-1).getLabel());
//		System.out.println("Ofilename = "+fileList.get(fileList.size()-1).getOfilename() );
//		
        HashMap<String, String > return_file_data = new HashMap<String, String>();
		
			return_file_data.put("uploaded","1");
			return_file_data.put("fileName",fileList.get(fileList.size()-1).getOfilename());
			return_file_data.put("url", fileList.get(fileList.size()-1).getOfilename());
       
		
	        return return_file_data;
		
		
	}
	
	List<Long> dealWithStaffNo(JsonNode jsonNode){
		List<Long> return_data = new ArrayList<>();
		//System.out.println("User size = " +jsonNode.get("userIds").size() );
		for(Integer i = 0 ; i < jsonNode.get("userIds").size();i++) {
			return_data.add(jsonNode.get("userIds").get(i).asLong());
		}
		
		return return_data;
	}
	
	@RequestMapping("/share/{userId}")
	private ResponseEntity<JsonResult> shareForum(@PathVariable Long userId, 
//			@RequestParam("resourceId") Long resourceId, @RequestParam("userIds") List<String> sendTo, 
			@RequestBody JsonNode jsonNode,
			HttpSession session) throws Exception, Throwable{
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			Long cocktailId = jsonNode.get("cocktailId").asLong();
			List<Long> user_list = dealWithStaffNo(jsonNode);
			List<User> user_session_list = (List<User>) session.getAttribute("user_list");
	        User user = (User) session.getAttribute("user_session"); 
			
			ForumPost  cocktail = forumPostService.getPostTitle(cocktailId);
			String cocktailTitle = cocktail.getPostTitle();
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
                    new_share.setResourceId(cocktailId);
                    new_share.setSendTo(user_list.get(i));
                    new_share.setMailType(3L);
                    
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
            EmailUtil.sendEmailToShareCocktail(sendEmaiList, cocktailTitle, user.getFullname(),Long.parseLong(user_check.get("access_channel")),cocktailId);
            
            return JsonResult.ok("sent",session);
			
		}
		
	}
	
	
}

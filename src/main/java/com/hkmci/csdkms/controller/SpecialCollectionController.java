package com.hkmci.csdkms.controller;

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

import com.aspose.pdf.internal.imaging.internal.bouncycastle.util.Arrays.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.SpecialCollection;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.PostReturnModel;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.BlogService;
import com.hkmci.csdkms.service.SpecialCollectionService;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.UserService;
import com.vdurmont.emoji.EmojiParser;

@CrossOrigin
@RestController
@RequestMapping("/specialCollection")
public class SpecialCollectionController {
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource
    private LogService logger;
	
	@Autowired
	@Resource
    private SpecialCollectionService specialCollectionService;
	
	@Autowired
	@Resource
    private BlogService blogService;
	
	@Autowired
	@Resource
    private UserService userService;
	
	@Autowired
	@Resource
	private AccessRuleService accessRuleService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/search")
	public ResponseEntity<JsonResult> search(
				@RequestParam(value="CateId") Long categoryId, 
				@RequestParam(value="user") Long user_id,
				@RequestParam(value="selSorter", defaultValue="") String selSorter,
				@RequestParam(value="page", defaultValue="1") Integer page,
				HttpSession session) {
		Integer checkUser = common.checkUserSession(user_id, session);
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		//System.out.println("Enter search API ");
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {

			if(selSorter.split(":").length < 2) {
				return JsonResult.errorMsg("Sorting Condition Error");
			}else {
				List<Long> accessRuleId = getAccessRuleIds(user_check,session);
				if(accessRuleId.size() == 0) {
					return JsonResult.errorMsg("No valid resources");
				}else {
					String sortBy = selSorter.split(":")[0];
					//String sortOrder = selSorter.split(":")[1];
					sortBy = sortBy == "publishDate" ? "publishAt" : sortBy;
					
					List<PostReturnModel> return_data = specialCollectionService.searchSpecialCollectionByCategoryId(page,accessRuleId,Long.parseLong(user_check.get("access_channel")));
					Integer return_total = specialCollectionService.getTotalSearchSpecialCollectionByCategoryId(accessRuleId,Long.parseLong(user_check.get("access_channel")));
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
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getMyPosts/{userId}")
	public ResponseEntity<JsonResult> get_my_posts(@PathVariable Long userId,HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			List<PostReturnModel> return_data = specialCollectionService.getMyPosts(userId);
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
			HashMap<String, String> user_check = common.checkUser(userId,session);
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			List<PostReturnModel> return_data = specialCollectionService.searchSpecialCollectionByLatest(accessRuleId,Long.parseLong(user_check.get("access_channel")));
			if(return_data == null) {
				return JsonResult.list("Post List Empty", return_data,0,session);
			}else{
				return JsonResult.list("Post List Searching", return_data,return_data.size(),session);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDetail/{specialCollectionId}")
	public ResponseEntity<JsonResult> getDetail(@PathVariable Long specialCollectionId, @RequestParam(value="user") Long userId, HttpSession session ) {
		HashMap<String, String> user_check = common.checkUser(userId,session);
		SpecialCollection data = specialCollectionService.getSpecialCollectionById(specialCollectionId,userId);	
		PostReturnModel return_data = new PostReturnModel();
		return_data.setSpecialCollection(data);
		return JsonResult.fileList("Post Found Successfully", return_data,null,session);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDetail2/{specialCollectionId}")
	public ResponseEntity<JsonResult> getDetail2(@PathVariable Long specialCollectionId, @RequestParam(value="user") Long userId, HttpSession session ) {
		HashMap<String, String> user_check = common.checkUser(userId,session);
		SpecialCollection data = specialCollectionService.getSpecialCollectionById(specialCollectionId,userId);	
		PostReturnModel return_data = new PostReturnModel();
		return_data.setSpecialCollection(data);
		return JsonResult.fileList("Post Found Successfully", return_data,null,session);
    }
	
	@RequestMapping("/create/{userId}")
	public ResponseEntity<JsonResult> create(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			//System.out.println(userId);
			
			Date createdAt = new Date();
			
			String dateOfTest = jsonNode.get("publishAt").asText();
			
			Date publishAt = common.textToDate(dateOfTest);
	        SpecialCollection new_specialCollection = new SpecialCollection();
	        Integer checkBlogCreator = common.checkBlogCreator(user, session, jsonNode);

	        if(checkBlogCreator.equals(1)) {
	        	Optional<User> creator = userService.findById(jsonNode.get("originalCreator").asLong());
	        	if(creator.isPresent()) {
	        		
	        	}
	        	
	        	JsonNode array = jsonNode.get("accessRuleId");
	        	String setAccessRuleId="";
		        if (array.isArray()) {
		        	List<JsonNode> nodeList = new ArrayList<>();
		        	array.elements().forEachRemaining(nodeList::add);
		        	setAccessRuleId = nodeList.stream()
		                    .map(JsonNode::asText)
		                    .collect(Collectors.joining(","));
		        }
		        
		        String link = jsonNode.get("link").asText();
		        if (!(link.startsWith("https://") || link.startsWith("http://"))) {
		            link = "http://"+link;
		        }
		        
	        	//System.out.println("Creator = "+ creator.get().getFullname());
		        new_specialCollection.setCreatedAt(createdAt);
		        new_specialCollection.setCreatedBy(jsonNode.get("createdBy").asLong());
		        new_specialCollection.setIs_public(jsonNode.get("isPublic").asLong());
		        new_specialCollection.setPublishAt(publishAt);
		        new_specialCollection.setIsDeleted(0);
		        new_specialCollection.setPostTitle(jsonNode.get("postTitle").asText());
		        new_specialCollection.setPostTitleZh(jsonNode.get("postTitleZh").asText());
		        new_specialCollection.setShowAsAlias(jsonNode.get("showAsAlias").asInt());
		        new_specialCollection.setAlias(jsonNode.get("alias").asText());
		        new_specialCollection.setOriginalCreator(creator.get());
		        new_specialCollection.setLink(link);
		        new_specialCollection.setAccessRuleId(setAccessRuleId);
		        new_specialCollection.setAccessChannel(jsonNode.get("accessChannel").asText());
		        System.out.println("setLink:"+jsonNode.get("link").asText());
		        
		        System.out.println("setAccessRuleId:"+setAccessRuleId);
		        System.out.println("array[0]:"+array.get(0));
		        //System.out.println("video link = " + new_specialCollection.getPostVideoLink());
		        
		        //System.out.println(EmojiParser.parseToHtmlDecimal(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText())));
		        
		        Long return_blog_id = specialCollectionService.add(new_specialCollection).getId();
		        logger.createBlog(user, 0L, "", "Success",channel);
				return JsonResult.ok(return_blog_id,"Post Create Successfully", session);
	        }else {
	        	return JsonResult.errorMsg("Creator User Status Error");
	        }
		}
    }
	
	@RequestMapping("/update/{specialCollectionId}")
	public ResponseEntity<JsonResult> update(@RequestBody JsonNode jsonNode,@PathVariable Long specialCollectionId, HttpSession session) {
		Optional<SpecialCollection> specialCollection = specialCollectionService.findById(specialCollectionId);
		//System.out.println(userId);
		if(specialCollection.isPresent()) {
			Integer checkSpecialCollectionUser = common.checkSpecialCollectionUser(jsonNode, session, specialCollection.get(),"update");
			if(checkSpecialCollectionUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
		        SpecialCollection new_specialCollection = specialCollection.get();
		        
		        String dateOfTest = jsonNode.get("publishAt").asText();
				Date publishAt = common.textToDate(dateOfTest);
				
		        JsonNode array = jsonNode.get("accessRuleId");
	        	String setAccessRuleId="";
		        if (array.isArray()) {
		        	List<JsonNode> nodeList = new ArrayList<>();
		        	array.elements().forEachRemaining(nodeList::add);
		        	setAccessRuleId = nodeList.stream()
		                    .map(JsonNode::asText)
		                    .collect(Collectors.joining(","));
		        }
		        
		        String link = jsonNode.get("link").asText();
		        if (!(link.startsWith("https://") || link.startsWith("http://"))) {
		            link = "http://"+link;
		        }
		        
		        new_specialCollection.setIs_public(jsonNode.get("isPublic").asLong());

		        new_specialCollection.setIsDeleted(0);
		        new_specialCollection.setPostTitle(jsonNode.get("postTitle").asText());
		        new_specialCollection.setPostTitleZh(jsonNode.get("postTitleZh").asText());
		        new_specialCollection.setShowAsAlias(jsonNode.get("showAsAlias").asInt());
		        new_specialCollection.setAlias(jsonNode.get("alias").asText());
		        new_specialCollection.setPublishAt(publishAt);
		        new_specialCollection.setLink(link);
		        new_specialCollection.setAccessRuleId(setAccessRuleId);
		        new_specialCollection.setAccessChannel(jsonNode.get("accessChannel").asText());
		        
		        specialCollectionService.update(new_specialCollection);
		       // dealWithTags(jsonNode,blog.get().getId());
		        //dealWithTags(jsonNode,specialCollectionId);
				//logger.updateBlog(user, blogId, "", "Success",channel);
				return JsonResult.ok("Post Update Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@RequestMapping("/delete/{specialCollectionId}")
	public ResponseEntity<JsonResult> delete(@RequestBody JsonNode jsonNode,@PathVariable Long specialCollectionId,HttpSession session) {
		Optional<SpecialCollection> specialCollection = specialCollectionService.findById(specialCollectionId);
		//System.out.println(userId);
		
		if(specialCollection.isPresent()) {
			Integer checkSpecialCollectionUser = common.checkSpecialCollectionUser(jsonNode, session, specialCollection.get(),"update");
			if(checkSpecialCollectionUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				Date deletedAt = new Date();		        
		        SpecialCollection new_specialCollection = specialCollection.get();
		        new_specialCollection.setDeletedBy(jsonNode.get("user").asLong());
		        new_specialCollection.setDeletedAt(deletedAt);
		        new_specialCollection.setIsDeleted(1);
		        
		        specialCollectionService.update(new_specialCollection);
		        //logger.deleteSpecialCollection(user, blogId, "", "Success",channel);
				return JsonResult.ok("Post Delete Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
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
}

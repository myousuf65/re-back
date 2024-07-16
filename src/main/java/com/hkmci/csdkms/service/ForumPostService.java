package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.ForumAdmin;
import com.hkmci.csdkms.entity.ForumGallery;
import com.hkmci.csdkms.entity.ForumGalleryDetail;
import com.hkmci.csdkms.entity.ForumPost;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.ForumAdminCateReturnModel;
import com.hkmci.csdkms.model.ForumCategoryModel;
import com.hkmci.csdkms.model.ForumCommentModel;
import com.hkmci.csdkms.model.ForumPostDetailReturnModel;
import com.hkmci.csdkms.model.ForumPostReturnModel;

public interface ForumPostService {
	
	public ForumPost add(ForumPost post);
	
	public ForumPost update(ForumPost post);
	
	public ForumPostDetailReturnModel getPost(List<Long> accessRuleIdList, Long postId, Long userId ,Long userGroup, List<String> channel, String staffNo);
	
	public List<ForumPost> getRelativePost(List<Long> accessRuleIdList, Long forumId,Long postId);
	
	public List<ForumPostReturnModel> getCategoryPost(Long forumId, Integer page);
	
	public Integer getCateogryTotal(Long forumId);
	
	
	public ForumCommentModel addComment(ForumCommentModel comment);
	
	public ForumCommentModel updateComment(ForumCommentModel comment);
	
	public void deleteCategory1 (Long categoryId, Long userId);
	public Optional<ForumPost> find(Long postId);
	
	public Optional<ForumCommentModel> findComment(Long commentId);
	
	public List<Long> getPageComments(Long postId, Integer pageStart, Integer pageEnd);
	
	public List<Long> getTotalComments(Long postId);
	
	public List<ForumCategoryModel> getSubCategory(Long categoryId, List<String> channel);
	public List<ForumCategoryModel> getFamilyPath(Long categoryId, List<String> channel);
	
	public List<ForumCategoryModel> getHomeSubCategory (Long categoryId, List<Long> accessRuleId , String staff_no, List<String> access_channel);
	
	public List<ForumCategoryModel> getAllCategory(Long isDeleted);
	
	public ForumCategoryModel getCategory(Long id);
	
	public ForumCategoryModel deleteCategory(Long id, Long userId);
	
	public void createCategory(JsonNode jsonNode,Long userId,User user);
	
	public void updateCategory(JsonNode jsonNode, Long userId);
	
	public List<ForumAdminCateReturnModel> getAdminCategory(Long categoryId);
	
	
	public List<ForumPost> getHotTopic(Long long1 , List<Long> accessruleId,String userId, String access_channel);
	
	public Map<String, Object> getComments(Long postId, Integer page ,Long userId,List<Long> limitCommentIds,List<User> user_list_session);
	
	List<Long> checkAccessRule (List<Long> accessRuleId, String staffNo,Long categoryId, List<String> channel);
	
	List<Long> checkAccessRuleCreate (List<Long> accessRuleId, String staffNo,Long categoryId,List<String> channel);

	Optional<ForumGallery> checkExistGallery(Long userId);
	
	Optional<ForumGallery> searchByPostId(Long postId);
	
	public ForumGallery newForumGallery(ForumGallery new_forumGallery);

	public List<ForumGalleryDetail> findByGalleryId(ForumGallery gallery);
	
	public ForumGalleryDetail storeToUserFolder(MultipartFile file, Long galleryId);
	
	public List<Object> findForumAdmin(Long postId);
	
	public ForumAdmin createAdmin(String staffNo, Long categoryId, Long userId);
	
	public List<Object> getAdminByPost (Long postId);
	
	
	public List<Object> getAdminByCategory (Long categoryId);
	
	ForumPost getPostTitle(Long postId);

	Integer canDeletePost(Long postId, Long userId, String staffNo);

	Integer canDeleteComment(Long commentId, Long userId, String staffNo);

	ForumGallery saveGallery(ForumGallery SaveGallery);

	
}

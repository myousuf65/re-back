package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.BlogAssistant;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.BlogTag;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.PostReturnModel;

public interface BlogService {

	public List<Blog> findAll();
	
	public Optional<Blog> findById(Long blogId);
	
	public Optional<Blog> checkBlogStatus(Long blogId);
	
	public Blog add(Blog blog);
	
	public List<BlogTag> getBlogTags(Long blogId);
	
	public Blog update(Blog blog);
	
	public void deleteAssistant(Long bloggerId, Long assistantId, Long userId);
	
	public BlogAssistant createAssistant(Long bloggerId, Long assistantId, Long userId);
	
	public List<PostReturnModel> searchBlogByCategoryId(Long categoryId, String sortBy, String sortOrder, Integer page, List<Long> latest_blog_ids, List<User> user_list_session, List<BlogGalleryDetail> blog_thumb);

	public List<PostReturnModel> searchBlogByLatest(List<User> user_list_session, List<BlogGalleryDetail> blog_thumb);
	
	public void createBlogTag(String tag, Long postId, Long userId);
	
	public PostReturnModel getBlogById(Long blogId, Long userId,List<User> user_list_session,Long user);
	
	public List<PostReturnModel> getMyPosts(Long userId, List<User> user_list_session);
	
	public List<PostReturnModel> getMyBookmarks(Long userId, List<User> user_list_session);
	
	public void addHit(Long blogId, Long userId);

	public void saveAllTags(List<BlogTag> tags);

	public Integer getTotalSearchBlogByCategoryId(Long categoryId, String sortBy, String sortOrder, Integer page,
			List<Long> latest_blog_ids, List<User> user_list_session, List<BlogGalleryDetail> blog_thumb);

	public void updateAllTags(List<BlogTag> tags_to_be_updated);

	public void deleteAllTags(List<BlogTag> tags_to_be_updated);

	List<Blog> getRelate(Long blogId);
}

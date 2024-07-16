package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.NewsCorner2Assistant;
import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;
import com.hkmci.csdkms.entity.NewsCorner2Tag;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.PostReturnModel;

public interface NewsCorner2Service {

	public List<NewsCorner2> findAll();
	
	public Optional<NewsCorner2> findById(Long newscorner2Id);
	
	public Optional<NewsCorner2> checkNewsCorner2Status(Long newscorner2Id);
	
	public NewsCorner2 add(NewsCorner2 newscorner2);
	
	public List<NewsCorner2Tag> getNewsCorner2Tags(Long newscorner2Id);
	
	public NewsCorner2 update(NewsCorner2 newscorner2);
	
	public void deleteAssistant(Long newscorner2gerId, Long assistantId, Long userId);
	
	public NewsCorner2Assistant createAssistant(Long newscorner2gerId, Long assistantId, Long userId);
	
	public List<PostReturnModel> searchNewsCorner2ByCategoryId(Long categoryId, String sortBy, String sortOrder, Integer page);

	public List<PostReturnModel> searchNewsCorner2ByLatest(List<User> user_list_session, List<NewsCorner2GalleryDetail> newscorner2_thumb);
	
	public void createNewsCorner2Tag(String tag, Long postId, Long userId);
	
	public PostReturnModel getNewsCorner2ById(Long newscorner2Id, Long userId,List<User> user_list_session,Long user);
	
	public List<PostReturnModel> getMyPosts(Long userId, List<User> user_list_session);
	
	public List<PostReturnModel> getMyBookmarks(Long userId, List<User> user_list_session);
	
	public void addHit(Long newscorner2Id, Long userId);

	public void saveAllTags(List<NewsCorner2Tag> tags);

	public Integer getTotalSearchNewsCorner2ByCategoryId(Long categoryId, String sortBy, String sortOrder, Integer page);

	/*public Integer getTotalSearchNewsCorner2ByCategoryId(Long categoryId, String sortBy, String sortOrder, Integer page,
			List<Long> latest_newscorner2_ids, List<User> user_list_session, List<NewsCorner2GalleryDetail> newscorner2_thumb);
	*/
	public void updateAllTags(List<NewsCorner2Tag> tags_to_be_updated);

	public void deleteAllTags(List<NewsCorner2Tag> tags_to_be_updated);

	List<NewsCorner2> getRelate(Long newscorner2Id);
}

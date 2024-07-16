package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hkmci.csdkms.model.NewsCorner2CommentModel;

public interface NewsCorner2CommentService {
	
	public List<NewsCorner2CommentModel> findAll();
	
	public Optional<NewsCorner2CommentModel> findById(Long commentId);
	
	public List<NewsCorner2CommentModel> findByPost(Long PostId);
	
	public NewsCorner2CommentModel save (NewsCorner2CommentModel TheModel);

	public Map<String,Object> getComments(Long postId, Integer page, Long userId,List<Long> limitCommentIds);
	
	public List<Long> getPageComments(Long postId, Integer pageStart, Integer pageEnd);

	public void delete(NewsCorner2CommentModel newscorner2CommentModel, Long userId);

	public boolean checkIsReply2cmnt(Long is_reply2cmnt, Long postId);
	

}

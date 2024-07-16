package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hkmci.csdkms.model.BlogCommentModel;

public interface BlogCommentService {
	
	public List<BlogCommentModel> findAll();
	
	public Optional<BlogCommentModel> findById(Long commentId);
	
	public List<BlogCommentModel> findByPost(Long PostId);
	
	public BlogCommentModel save (BlogCommentModel TheModel);

	public Map<String,Object> getComments(Long postId, Integer page, Long userId,List<Long> limitCommentIds);
	
	public List<Long> getPageComments(Long postId, Integer pageStart, Integer pageEnd);

	public void delete(BlogCommentModel blogCommentModel, Long userId);

	public boolean checkIsReply2cmnt(Long is_reply2cmnt, Long postId);
	

}

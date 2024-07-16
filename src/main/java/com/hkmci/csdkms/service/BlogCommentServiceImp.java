package com.hkmci.csdkms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.model.BlogCommentModel;
import com.hkmci.csdkms.repository.BlogCommentRepository;
import com.hkmci.csdkms.repository.BlogRepository;
import com.vdurmont.emoji.EmojiParser;


@Service
public class BlogCommentServiceImp implements BlogCommentService {

	private BlogCommentRepository blogCommentRepository;
	
	private BlogRepository blogRepository;
	
	
	@Autowired
	public BlogCommentServiceImp(BlogCommentRepository theBlogCommentRepository, BlogRepository theBlogRepository) {
		blogCommentRepository = theBlogCommentRepository;
		blogRepository = theBlogRepository;
	}
	
	@Override
	public List<BlogCommentModel> findAll() {
		// TODO Auto-generated method stub
//		Map<String,List<BlogCommentModel>> Comment = new HashMap<>();
		List<BlogCommentModel> com = blogCommentRepository.findAll();
		List<BlogCommentModel> com2 = com.stream().filter(c->c.getIsRely2cmnt()==0).collect(Collectors.toList());
		//HashMap<String,Object> com2_object =new HashMap<>();
		//for(int i=0;i<com2.size();i++) {
			//HashMap<String,Object> com3_object = new HashMap<>();
			//int j =i;
			//List<BlogCommentModel> com3 = com.stream().filter (c -> c.getIsRely2cmnt()==com2.get(j).getId()).collect(Collectors.toList());
		//com2_object.put(String.valueOf(j), com3);
		
		//}
		
		return com2;
	}

	
	@Override
	public List<BlogCommentModel> findByPost(Long PostId) {
// TODO Auto-generated method stub
//				Map<String,List<BlogCommentModel>> Comment = new HashMap<>();
				List<BlogCommentModel> com = blogCommentRepository.findByPostId(PostId);
				List<BlogCommentModel> com2 = com.stream().filter(c->c.getIsRely2cmnt()==0).collect(Collectors.toList());
				//HashMap<String,Object> com2_object =new HashMap<>();
				//for(int i=0;i<com2.size();i++) {
					//HashMap<String,Object> com3_object = new HashMap<>();
					//int j =i;
					//List<BlogCommentModel> com3 = com.stream().filter (c -> c.getIsRely2cmnt()==com2.get(j).getId()).collect(Collectors.toList());
				//com2_object.put(String.valueOf(j), com3);
				//}		
				return com2;
	}

	@Override
	public BlogCommentModel save(BlogCommentModel TheModel) {
		// TODO Auto-generated method stub
		return blogCommentRepository.saveAndFlush(TheModel);
	}

	@Override
	public Map<String,Object> getComments (Long postId, Integer page, Long userId, List<Long> limitCommentIds) {
		// TODO Auto-generated method stub
		//System.out.println(limitCommentIds);
		if(limitCommentIds.size() == 0) {
			limitCommentIds.add(0L);
		}
		
		List<BlogCommentModel> query_data = blogCommentRepository.getComments(postId,userId,limitCommentIds);
		List<BlogCommentModel> query_return_data = query_data.stream().map(temp -> {
														temp.setContent(EmojiParser.parseToUnicode(temp.getContent()));
											            return temp;
											        }).collect(Collectors.toList());
		List<BlogCommentModel> return_data_for_stream = query_return_data;
		
		List<Object[]> query_data_liked = blogCommentRepository.getCommentsLiked(postId,userId,limitCommentIds);
		
		List<Object[]> query_total = blogCommentRepository.getTotalComments(postId);
		
        for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data_liked.get(i);
        	query_data.get(i).setLiked(Integer.parseInt(String.valueOf(data[0])));
        	query_data.get(i).setLikes(Long.parseLong(String.valueOf(data[1])));
            //System.out.println(data[0]);
        }
		
        HashMap<String, Object> for_return = new HashMap<String, Object>();
        
        
		List<BlogCommentModel> return_first_level_data = query_return_data.stream()
				.filter( 
					data -> data.getIsRely2cmnt().equals(1L)
				)
				.map(
					(bcm) -> {
						List<BlogCommentModel> subComments = return_data_for_stream.stream()
								.filter( 
										data -> data.getIsRely2cmnt().equals(bcm.getId())
								)
								.collect(Collectors.toList());
						bcm.setSubComments(subComments);
						return bcm;
					}
				)
				.collect(Collectors.toList());
		
		for_return.put("list", return_first_level_data);
		for_return.put("total", query_total.get(0)[0]);
		for_return.put("total_for_page", query_total.get(0)[1]);
		return for_return;
	}

	@Override
	public List<Long> getPageComments(Long postId, Integer pageStart, Integer pageEnd) {
		// TODO Auto-generated method stub
		return blogCommentRepository.getCommentsLevelOne(postId,pageStart,pageEnd);
	}

	@Override
	public Optional<BlogCommentModel> findById(Long commentId) {
		// TODO Auto-generated method stub
		return blogCommentRepository.findById(commentId);
	}

	@Override
	public void delete(BlogCommentModel blogCommentModel, Long userId) {
		// TODO Auto-generated method stub
		if(blogCommentModel.getIsRely2cmnt().equals(1L)) {
			blogCommentModel.setIsDeleted(1);
			blogCommentModel.setDeletedAt(new Date());
			blogCommentModel.setDeletedBy(userId);
			blogCommentRepository.deleteAllsubs(blogCommentModel.getId());
		}else {
			blogCommentModel.setIsDeleted(1);
			blogCommentModel.setDeletedAt(new Date());
			blogCommentModel.setDeletedBy(userId);
			blogCommentRepository.saveAndFlush(blogCommentModel);
		}
		
		return;
	}

	@Override
	public boolean checkIsReply2cmnt(Long is_reply2cmnt, Long postId) {
		// TODO Auto-generated method stub
		
		if(is_reply2cmnt == 1L) {
			Optional<Blog> return_data = blogRepository.checkBlogStatus(postId);
			if(return_data.isPresent()) {
				return true;
			}else {
				return false;
			}
		}else {
			Optional<BlogCommentModel> return_data = blogCommentRepository.checkIsReply2cmnt(is_reply2cmnt,postId);
			if(return_data.isPresent()) {
				return true;
			}else {
				return false;
			}
		}
	}

}

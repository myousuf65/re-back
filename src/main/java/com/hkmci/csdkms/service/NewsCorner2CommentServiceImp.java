package com.hkmci.csdkms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.model.NewsCorner2CommentModel;
import com.hkmci.csdkms.repository.NewsCorner2CommentRepository;
import com.hkmci.csdkms.repository.NewsCorner2Repository;
import com.vdurmont.emoji.EmojiParser;


@Service
public class NewsCorner2CommentServiceImp implements NewsCorner2CommentService {

	private NewsCorner2CommentRepository newscorner2CommentRepository;
	
	private NewsCorner2Repository newscorner2Repository;
	
	
	@Autowired
	public NewsCorner2CommentServiceImp(NewsCorner2CommentRepository theNewsCorner2CommentRepository, NewsCorner2Repository theNewsCorner2Repository) {
		newscorner2CommentRepository = theNewsCorner2CommentRepository;
		newscorner2Repository = theNewsCorner2Repository;
	}
	
	@Override
	public List<NewsCorner2CommentModel> findAll() {
		// TODO Auto-generated method stub
//		Map<String,List<NewsCorner2CommentModel>> Comment = new HashMap<>();
		List<NewsCorner2CommentModel> com = newscorner2CommentRepository.findAll();
		List<NewsCorner2CommentModel> com2 = com.stream().filter(c->c.getIsRely2cmnt()==0).collect(Collectors.toList());
		//HashMap<String,Object> com2_object =new HashMap<>();
		//for(int i=0;i<com2.size();i++) {
			//HashMap<String,Object> com3_object = new HashMap<>();
			//int j =i;
			//List<NewsCorner2CommentModel> com3 = com.stream().filter (c -> c.getIsRely2cmnt()==com2.get(j).getId()).collect(Collectors.toList());
		//com2_object.put(String.valueOf(j), com3);
		
		//}
		
		return com2;
	}

	
	@Override
	public List<NewsCorner2CommentModel> findByPost(Long PostId) {
// TODO Auto-generated method stub
//				Map<String,List<NewsCorner2CommentModel>> Comment = new HashMap<>();
				List<NewsCorner2CommentModel> com = newscorner2CommentRepository.findByPostId(PostId);
				List<NewsCorner2CommentModel> com2 = com.stream().filter(c->c.getIsRely2cmnt()==0).collect(Collectors.toList());
				//HashMap<String,Object> com2_object =new HashMap<>();
				//for(int i=0;i<com2.size();i++) {
					//HashMap<String,Object> com3_object = new HashMap<>();
					//int j =i;
					//List<NewsCorner2CommentModel> com3 = com.stream().filter (c -> c.getIsRely2cmnt()==com2.get(j).getId()).collect(Collectors.toList());
				//com2_object.put(String.valueOf(j), com3);
				//}		
				return com2;
	}

	@Override
	public NewsCorner2CommentModel save(NewsCorner2CommentModel TheModel) {
		// TODO Auto-generated method stub
		return newscorner2CommentRepository.saveAndFlush(TheModel);
	}

	@Override
	public Map<String,Object> getComments (Long postId, Integer page, Long userId, List<Long> limitCommentIds) {
		// TODO Auto-generated method stub
		//System.out.println(limitCommentIds);
		if(limitCommentIds.size() == 0) {
			limitCommentIds.add(0L);
		}
		
		List<NewsCorner2CommentModel> query_data = newscorner2CommentRepository.getComments(postId,userId,limitCommentIds);
		List<NewsCorner2CommentModel> query_return_data = query_data.stream().map(temp -> {
														temp.setContent(EmojiParser.parseToUnicode(temp.getContent()));
											            return temp;
											        }).collect(Collectors.toList());
		List<NewsCorner2CommentModel> return_data_for_stream = query_return_data;
		
		List<Object[]> query_data_liked = newscorner2CommentRepository.getCommentsLiked(postId,userId,limitCommentIds);
		
		List<Object[]> query_total = newscorner2CommentRepository.getTotalComments(postId);
		
        for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data_liked.get(i);
        	query_data.get(i).setLiked(Integer.parseInt(String.valueOf(data[0])));
        	query_data.get(i).setLikes(Long.parseLong(String.valueOf(data[1])));
            //System.out.println(data[0]);
        }
		
        HashMap<String, Object> for_return = new HashMap<String, Object>();
        
        
		List<NewsCorner2CommentModel> return_first_level_data = query_return_data.stream()
				.filter( 
					data -> data.getIsRely2cmnt().equals(1L)
				)
				.map(
					(bcm) -> {
						List<NewsCorner2CommentModel> subComments = return_data_for_stream.stream()
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
		return newscorner2CommentRepository.getCommentsLevelOne(postId,pageStart,pageEnd);
	}

	@Override
	public Optional<NewsCorner2CommentModel> findById(Long commentId) {
		// TODO Auto-generated method stub
		return newscorner2CommentRepository.findById(commentId);
	}

	@Override
	public void delete(NewsCorner2CommentModel newscorner2CommentModel, Long userId) {
		// TODO Auto-generated method stub
		if(newscorner2CommentModel.getIsRely2cmnt().equals(1L)) {
			newscorner2CommentModel.setIsDeleted(1);
			newscorner2CommentModel.setDeletedAt(new Date());
			newscorner2CommentModel.setDeletedBy(userId);
			newscorner2CommentRepository.deleteAllsubs(newscorner2CommentModel.getId());
		}else {
			newscorner2CommentModel.setIsDeleted(1);
			newscorner2CommentModel.setDeletedAt(new Date());
			newscorner2CommentModel.setDeletedBy(userId);
			newscorner2CommentRepository.saveAndFlush(newscorner2CommentModel);
		}
		
		return;
	}

	@Override
	public boolean checkIsReply2cmnt(Long is_reply2cmnt, Long postId) {
		// TODO Auto-generated method stub
		
		if(is_reply2cmnt == 1L) {
			Optional<NewsCorner2> return_data = newscorner2Repository.checkNewsCorner2Status(postId);
			if(return_data.isPresent()) {
				return true;
			}else {
				return false;
			}
		}else {
			Optional<NewsCorner2CommentModel> return_data = newscorner2CommentRepository.checkIsReply2cmnt(is_reply2cmnt,postId);
			if(return_data.isPresent()) {
				return true;
			}else {
				return false;
			}
		}
	}

}

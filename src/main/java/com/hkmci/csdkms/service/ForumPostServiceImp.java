package com.hkmci.csdkms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.controller.Md5Encode;
import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.ForumAccessRule;
import com.hkmci.csdkms.entity.ForumAdmin;
import com.hkmci.csdkms.entity.ForumGallery;
import com.hkmci.csdkms.entity.ForumGalleryDetail;
import com.hkmci.csdkms.entity.ForumPost;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.BlogCommentModel;
import com.hkmci.csdkms.model.ForumAdminCateReturnModel;
import com.hkmci.csdkms.model.ForumCategoryModel;
import com.hkmci.csdkms.model.ForumCommentModel;
import com.hkmci.csdkms.model.ForumPostDetailReturnModel;
import com.hkmci.csdkms.model.ForumPostReturnModel;
import com.hkmci.csdkms.model.ForumSpecialUserGroupModel;
import com.hkmci.csdkms.model.ForumSpecialUserModel;
import com.hkmci.csdkms.model.SpecialUserGroupModel;
import com.hkmci.csdkms.repository.ForumAccessRuleRepository;
import com.hkmci.csdkms.repository.ForumAdminRepository;
import com.hkmci.csdkms.repository.ForumCategoryRepository;
import com.hkmci.csdkms.repository.ForumCommentRepository;
import com.hkmci.csdkms.repository.ForumGalleryDetailRepository;
import com.hkmci.csdkms.repository.ForumGalleryRepository;
import com.hkmci.csdkms.repository.ForumRepository;
import com.hkmci.csdkms.repository.ForumSpecialGroupRepository;
import com.hkmci.csdkms.repository.ForumSpecialUserRepository;
import com.hkmci.csdkms.repository.SpecialUserGroupRepository;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;
import com.vdurmont.emoji.EmojiParser;


@Service
public class ForumPostServiceImp implements ForumPostService {

	
	@Autowired
	@Resource
	private ForumRepository forumRepository;
	
	@Autowired
	@Resource
	private ForumCommentRepository forumCommentRepository;
	
	@Autowired
	@Resource
	private ForumCategoryRepository forumCategoryRepository;
	
	@Autowired 
	@Resource 
	private ForumAdminRepository forumAdminRepository;
	
	@Autowired
	@Resource 
	private ForumSpecialUserRepository forumSpecialUserRepository;
	
	
	@Autowired
	@Resource
	private ForumAccessRuleRepository forumAccessRuleRepository;
	
	@Autowired
	@Resource
	private ForumGalleryDetailRepository forumGalleryDetailRepository;
	
	
	
	@Autowired
	@Resource
	private ForumSpecialGroupRepository forumSpecialGroupRepository;
	
	
	@Autowired
	@Resource
	private SpecialUserGroupRepository specialUserGroupRepository;    
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	
	
	
	@Autowired
	@Resource
	private ForumGalleryRepository forumGalleryRepository;
	

	@Override
	public ForumPost add(ForumPost post) {
		// TODO Auto-generated method stub
		return forumRepository.saveAndFlush(post);
	}



	@Override
	public ForumPost update(ForumPost post) {
		// TODO Auto-generated method stub
		return forumRepository.saveAndFlush(post);
	}
	
	

	@Override
	public Optional<ForumPost> find(Long postId) {
		// TODO Auto-generated method stub
		return forumRepository.findById(postId);
	}



	@Override
	public ForumCommentModel addComment(ForumCommentModel comment) {
		// TODO Auto-generated method stub
		return forumCommentRepository.saveAndFlush(comment);
	}

	

	@Override
	public Optional<ForumCommentModel> findComment(Long commentId) {
		// TODO Auto-generated method stub
		return forumCommentRepository.findById(commentId);
	}



	@Override
	public ForumCommentModel updateComment(ForumCommentModel comment) {
		// TODO Auto-generated method stub
		return forumCommentRepository.saveAndFlush(comment);
	}


	
	
	
	
	@Override
	public ForumPostDetailReturnModel getPost(List<Long> accessRuleIdList, Long postId, Long userId, Long userGroup, List<String> channel,String staffNo) {
		// TODO Auto-generated method stub
//		System.out.println("accessRuleIdList" + accessRuleIdList + "Post Id" + postId + "user ID " + userId + " Access channel "+ channel);
		ForumPostDetailReturnModel post  = new ForumPostDetailReturnModel();
		
		List<Object[]> query_data = new ArrayList<>();
		//ForumPostReturnModel temp =  new ForumPostReturnModel();
		if (userGroup==5) {
//			System.out.println("Super Admin ");
			query_data= forumRepository.findByPostId(postId,channel);
			
		
			
		}else {
			System.out.println("Forum Post Services Imp , line 180 : user id = "+ userId +" channel = "+ channel);
			query_data = forumRepository.findByAccessRuleAndPostId( postId, channel);
		}
		
		
		
		for (Integer i = 0 ; i< query_data.size() ; i++) {
			ForumPostReturnModel  temp = new ForumPostReturnModel();
			ForumCategoryModel Cattemp = new ForumCategoryModel();
			Object[] data = (Object[]) query_data.get(i);
			data[6] = data[6]== null? "":data[6];
			data[11] = data[11] == null? "":data[11];
			Integer is_comment = forumCommentRepository.diduserComments(postId, userId);
			//List<Long> replyId = forumCommentRepository.replyuserComments(postId, userId);
			
			
			
			
				temp.setId(Long.valueOf(data[0].toString()));
				temp.setTitle(data[1].toString());
				temp.setCreatedAt((Date) data[10]);
				temp.setAlias(data[6].toString());
				
				temp.setIsAlias(Integer.valueOf(data[7].toString()));
				
				temp.setHit(Integer.valueOf( data[5].toString())+1);
				temp.setContent(data[2].toString());
				
				temp.setLastUpdatedAt((Date) data[12]);
				if(data[12]!=null) {
				temp.setModifiedAt((Date) data[12]); 
				}
				temp.setModifiedBy(data[11].toString());
				temp.setOrder(Integer.valueOf(data[4].toString()));
				temp.setComments(Integer.valueOf(data[21].toString()));
				temp.setLikes(Integer.valueOf(data[18].toString()));
				temp.setCreatedBy(data[19].toString());
				
				if(data[17] == null) {
					temp.setHiddenField(null);
				} else if (is_comment > 0 ) {
					temp.setHiddenField(data[17].toString());
				} else {
					temp.setHiddenField("0");
				}
				
				
				
				
				
				
				if(data[24]==null) {
					data[24]="";
				}
				temp.setLastUpdateBy(data[24].toString());
				temp.setAllowComment(Integer.valueOf(data[16].toString()));
				
				Cattemp.setId(Long.valueOf(data[8].toString()));
				Cattemp.setNameEn(data[22].toString());
				Cattemp.setNameTc(data[23].toString());
				temp.setCategory(Cattemp);
				
				Integer canEdit =forumRepository.canDeleteByAdmin(postId, userId, staffNo);
				if (canEdit== null) {
					canEdit=0;
				}
				
//				System.out.println("User Id "+ userId + " Created by = "+data[9].toString() +" user group = "+ userGroup +" can edit = " + canEdit  );
				if(userId == Long.valueOf(data[9].toString()) ||  userGroup==5 ||canEdit>0 ) {
				temp.setCanEdit(1);
				} else {
					temp.setCanEdit(0);
				}
				post.setPost(temp);
//				System.out.println("hit = "+ temp.getHit());
				
		}
	
		forumRepository.updatehit(post.getPost().getHit(), postId);
		
		
	
		
		return post;
	}



	@Override
	public List<ForumPost> getRelativePost(List<Long> accessRuleIdList, Long forumId,Long postId) {
		// TODO Auto-generated method stub
//		System.out.println("accessRuleIdList" + accessRuleIdList + "Post Id" + postId + "forum ID " + forumId);
		List<ForumPost> post =new ArrayList<>();
		post.add(forumRepository.getNextPost(accessRuleIdList, forumId,postId));
//		System.out.println("Post size = " + post.size());
		return post ;
	}



	@Override
	public List<Long> getPageComments(Long postId, Integer pageStart, Integer pageEnd) {
		// TODO Auto-generated method stub
		
		return forumCommentRepository.getCommentsLevelOne(postId, pageStart, pageEnd);
	}




	@Override
	public List<Long> getTotalComments(Long postId) {
		// TODO Auto-generated method stub
		return forumCommentRepository.getTotalComment(postId);
	}


	@Override
	public Map<String, Object> getComments(Long postId, Integer page, Long userId, List<Long> limitCommentIds,List<User> user_list_session) {
		// TODO Auto-generated method stub
//		System.out.println("limit Comment Ids = " + limitCommentIds);
		if(limitCommentIds.size() ==0) {
			limitCommentIds.add(0L);
		}
		List<ForumCommentModel> query_data = forumCommentRepository.getComment(postId, userId, limitCommentIds);
		
		
		

		for(Integer i =0 ; i< query_data.size();i++) {
			//System.out.println("Query Data size = "+ query_data.size()+"User list session = "+ user_list_session.size());
			Integer j =i;
			User temp = user_list_session.stream()
					 .filter(n -> n.getId().equals(query_data.get(j).getCreatedBy()))
					 .collect(Collectors.toList())
					 .get(0);
//			System.out.println("Temp user  = "+ temp.getId());
			query_data.get(i).setUser(temp);
		
		}
		List<ForumCommentModel> query_return_data = query_data.stream().map(temp -> {
			temp.setContent(EmojiParser.parseToUnicode(temp.getContent()));
            return temp;
        }).collect(Collectors.toList());

		
		
		List<ForumCommentModel> return_data_for_stream = query_return_data;
		
		List<Object[]> query_data_liked =  forumCommentRepository.getCommentsLiked(postId, userId, limitCommentIds);
		List<Object[]> query_total = forumCommentRepository.getTotalComments(postId);
	    for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data_liked.get(i);
        	query_data.get(i).setLiked(Integer.parseInt(String.valueOf(data[0])));
        	query_data.get(i).setLikes(Long.parseLong(String.valueOf(data[1])));
            //System.out.println(data[0]);
        }
	    HashMap<String, Object> for_return = new HashMap<String, Object>();
        

		List<ForumCommentModel> return_first_level_data = query_return_data.stream()
				.filter( 
					data -> data.getIsReply2cmnt().equals(1L)
				)
				.map(
					(bcm) -> {
						List<ForumCommentModel> subComments = return_data_for_stream.stream()
								.filter( 
										data -> data.getIsReply2cmnt().equals(bcm.getId())
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
	public List<Long> checkAccessRule(List<Long> accessRuleId, String staffNo, Long categoryId, List<String> channel) {
		// TODO Auto-generated method stub
//		System.out.println("access rule = "+ accessRuleId+" staff no = "+ staffNo +" category Id = "+categoryId);
		List<Long> return_list= new ArrayList<>();
		List<Long> return_data = new ArrayList<>();
		return_list.addAll(forumCategoryRepository.findCatByAccessRule(accessRuleId, staffNo,categoryId, channel));
		return_list.addAll(forumCategoryRepository.findCatBySpecialUser(staffNo, categoryId, channel));
		
		return_data = return_list.stream()
			    .distinct()
			    .collect(Collectors.toList());
//		System.out.println("checkAccessRule for access category see post = "+ return_data);

		return return_data;
		
		
	}

	

	@Override
	public List<Long> checkAccessRuleCreate(List<Long> accessRuleId, String staffNo, Long categoryId,List<String> channel) {
		// TODO Auto-generated method stub
	
		List<Long> return_list= forumCategoryRepository.findCatByAccessRuleCreate(accessRuleId, staffNo,categoryId, channel);
//		System.out.println("After check access rule create = ");
		return return_list;
		
		
	}

	@Override
	public Integer getCateogryTotal(Long forumId) {
		List<Object[]> query_data = forumRepository.getCategoryPostTotal(forumId);
		return query_data.size();
	}







	@Override
	public List<ForumPostReturnModel> getCategoryPost(Long forumId,Integer page) {
		// TODO Auto-generated method stub
		
		Integer startIndex = (page- 1) * 20;
		List<Object[]> query_data = forumRepository.getCategoryPost(forumId,startIndex,20 );
//		System.out.println(" forum Post size = "+ query_data.size());
		
		List<ForumPostReturnModel> return_data = new ArrayList<>();
		for (Integer i = 0 ; i< query_data.size() ; i++) {
			ForumPostReturnModel  temp = new ForumPostReturnModel();
			Object[] data = (Object[]) query_data.get(i);
//			System.out.println("Id " + data[0]+ " Title " + data[1]);
//			System.out.println("Order" + data[4]+" Hit "+ data[5]);
//			System.out.println("Alias "+ data[6]+ " Show as "+ data[7]);
//			System.out.println("Last Update "+ data[12]+ " like "+ data[16]);
//			System.out.println("Full name "+ data[17]+" Chinese name"+ data[18]);
//			System.out.println("Comment "+data[19]+ "");
		
			data[6] = data[6]== null? "":data[6];
			
			
			temp.setId(Long.valueOf(data[0].toString()));
			temp.setTitle(data[1].toString());
			temp.setCreatedAt((Date) data[10]);
			temp.setAlias(data[6].toString());
			temp.setIsAlias(Integer.valueOf(data[7].toString()));
			temp.setHit(Integer.valueOf( data[5].toString()));
			temp.setLastUpdatedAt((Date) data[12]);
			temp.setOrder(Integer.valueOf(data[4].toString()));
			temp.setComments(Integer.valueOf(data[21].toString()));
			temp.setLikes(Integer.valueOf(data[16].toString()));
			temp.setCreatedBy(data[19].toString());
			if (data[11]!=null) {
			temp.setLastUpdateBy(data[22].toString());
			}
			
			return_data.add(temp);
			
		}
		
		return return_data;
	}



	@Override
	public List<ForumCategoryModel> getSubCategory(Long categoryId,List<String> channel) {
		// TODO Auto-generated method stub
		List<ForumCategoryModel> return_list =forumCategoryRepository.getByParentID(categoryId, channel);
		
		List<ForumCategoryModel> return_data = new ArrayList<>();
		for(Integer i =0 ; i< return_list.size() ;i++) {
			ForumCategoryModel temp = return_list.get(i);
			temp.setCanCreate(1);
			return_data.add(temp);
		}
		
		

		return return_data;
	}

	@Override
	public List<ForumCategoryModel> getHomeSubCategory(Long categoryId, List<Long> accessRuleId, String staff_no, List<String> access_channel) {
//		System.out.println("Category Id = "+ categoryId + " accessRuleId "+ accessRuleId + " staff_no "+ staff_no);
		
		//List<ForumCategoryModel> return_data =forumCategoryRepository.getSubcate(categoryId, accessRuleId, staff_no, access_channel);
		  List<ForumCategoryModel> data_back = new ArrayList<>();
		   data_back.addAll(forumCategoryRepository.findSubCatByAccessRuleAndAdmin(accessRuleId, staff_no,categoryId, access_channel));
		   data_back.addAll(forumCategoryRepository.findSubCatBySpecialUser(staff_no,categoryId, access_channel));
		  
		   List<ForumCategoryModel> return_data = new ArrayList<>();
		   return_data = data_back.stream()
				    .distinct()
				    .sorted(Comparator.comparing(ForumCategoryModel::getOrderBy))
				    .collect(Collectors.toList());
		
		
			for(Integer j =0 ; j<return_data.size() ;j++) {

				Integer canEdit = 0;
				Integer access = 0 ;
				Integer special = 0;
				//forumCategoryRepository.canCreate(return_data.get(j).getId(), staff_no, accessRuleId);
				access = forumCategoryRepository.canCreateByAccessRuleAndAdmin(staff_no, categoryId,accessRuleId );
//				System.out.println("Can Edit By Special USER and GROUP Before " +access );
				special = forumCategoryRepository.canCreateBySpecialUser(staff_no, return_data.get(j).getId());
//				System.out.println("Can Edit By Special USER and GROUP After " +special + " -- Staff No :  "+ staff_no + " Category Id == "+  return_data.get(j).getId());
				
				canEdit = access+special;
				
				if (canEdit == 0) {
//					System.out.println("Category Id = "+ return_data.get(j).getId()+ " Can Edit = "+ canEdit);
					return_data.get(j).setCanCreate(0);	
				} else {
//					System.out.println("Category Id = "+ return_data.get(j).getId()+ " Can Edit = "+ canEdit);
					return_data.get(j).setCanCreate(1);	
				}
			}
			
		
//		System.out.println(" Parent id = "+ categoryId+ "Return size = "+return_data.size());
		return return_data;
	}


	@Override
	public List<ForumCategoryModel> getFamilyPath(Long categoryId, List<String> channel) {
	   List<Object[]> query_data = forumCategoryRepository.getFamily(categoryId, channel);
	   List<ForumCategoryModel> return_data = new ArrayList<>();
	   ForumCategoryModel parent = new ForumCategoryModel();
	   ForumCategoryModel child = new ForumCategoryModel();

//	    System.out.println("retur"+query_data.size());
		for (Integer i = 0 ; i< query_data.size() ; i++) {
			
			Object[] data = (Object[]) query_data.get(i);
			parent.setId(Long.valueOf(data[3].toString()));
			parent.setNameEn(data[4].toString());
			parent.setNameTc(data[5].toString());
			child.setId(Long.valueOf(data[0].toString()));
			child.setNameEn(data[1].toString());
			child.setNameTc(data[2].toString());
			return_data.add(parent);
			return_data.add(child);
		}
	   return return_data;
	}
	@Override
	public List<ForumCategoryModel> getAllCategory(Long isDeleted) {
		// TODO Auto-generated method stub
		return forumCategoryRepository.findByIsDeleted();
	}

	
	
	@Override
	public List<ForumAdminCateReturnModel> getAdminCategory(Long categoryId) {
		// TODO Auto-generated method stub
		
		List<ForumAdminCateReturnModel> return_data = new ArrayList<>();
		
		List<Object []> query_data = forumCategoryRepository.getCategoryAdmin(categoryId);
//		System.out.println(" See size ="+query_data.size());
		ForumAdminCateReturnModel temp = new ForumAdminCateReturnModel();
		List<Long> admin = new ArrayList<>();
		List<Long> accessRule = new ArrayList<>();
		List<Long> accessRule_wirte = new ArrayList<>();
		List<Long> specialuser = new ArrayList<>();
		List<Long> specialuser_wirte = new ArrayList<>();
		
	
		
		
		for(Integer i = 0 ; i< query_data.size() ; i++) {
			Object[] data = (Object[]) query_data.get(i);
			data[9] = data[9]== null? "":data[9];
			temp.setId(Long.valueOf(data[0].toString()));
			temp.setNameEN(data[1].toString());
			temp.setNameTc(data[2].toString());
			temp.setTabStyle(data[9].toString());
			temp.setImgUrl(data[3].toString());
			temp.setParentForumId(Long.valueOf(data[5].toString()));
		
			temp.setAccessChannel(Integer.valueOf(data[6].toString()));
			if(data[8]==null) {
				temp.setShowInfo(0);
			}else {
			temp.setShowInfo(Integer.valueOf(data[8].toString()) );
			};
			
			if(data[7] ==null) {
				temp.setOrderBy(999);
			}else {
			temp.setOrderBy(Integer.valueOf(data[7].toString()));
			};
			
			
			
			
			if (data[17]!=null ) {
			admin.add(Long.valueOf(data[17].toString()));
			};
//			if(data[16] !=null) {
//				if(Integer.valueOf(data[16].toString()) == 0) {
//					accessRule.add(Long.valueOf(data[15].toString()));
//				} else {
//					accessRule_wirte.add(Long.valueOf(data[15].toString()));
//				};
//			};
			
			if(data[18]!=null && data[19]!=null ) {
				if(Integer.valueOf(data[19].toString())== 0) {
				accessRule.add(Long.valueOf(data[18].toString())); 
				} else if (Integer.valueOf(data[19].toString())== 1) {
					accessRule_wirte.add(Long.valueOf(data[18].toString()));
				}
			};
			
			if(data[20]!=null && data[21]!= null ) {
				if(Integer.valueOf(data[21].toString())==0) {
				specialuser.add(Long.valueOf(data[20].toString()));
				} else if(Integer.valueOf(data[21].toString())==1) {
					specialuser_wirte.add(Long.valueOf(data[20].toString()));
				}
			};
			
			
			
//			if(data[18] != null) {
//				if(Integer.valueOf(data[18].toString()) == 0) {
//					specialuser.add(Long.valueOf(data[17].toString()));
//				} else {
//					specialuser_wirte.add(Long.valueOf(data[17].toString()));
//				};
//			};
			
		}
		
//		System.out.println("detail size = "+query_data.size() );
		HashSet h = new HashSet(admin);
		admin.clear();
		admin.addAll(h);
//		System.out.println("Admin list = "+ admin);
		HashSet a = new HashSet(accessRule);
		accessRule.clear();
		accessRule.addAll(a);
//		System.out.println("accessRule = "+ accessRule);
		HashSet b = new HashSet(accessRule_wirte);
		accessRule_wirte.clear();
		accessRule_wirte.addAll(b);
//		System.out.println("accessRule_wirte = "+ accessRule);
		HashSet c = new HashSet(specialuser);
		specialuser.clear();
		specialuser.addAll(c);
//		System.out.println("specialuser = "+ accessRule);
		HashSet d = new HashSet(specialuser_wirte);
		specialuser_wirte.clear();
		specialuser_wirte.addAll(d);
//		System.out.println("specialuser_wirte = "+ accessRule);
	     
//		System.out.println("Category Id = "+ categoryId);
		List<Integer>  specialGroup_access = specialUserGroupRepository.getSpecialGroupByForumId(categoryId,0);
		List<Integer>  specialGroup_writer = specialUserGroupRepository.getSpecialGroupByForumId(categoryId,1);
//		System.out.println("specialGroup_access " + specialGroup_access.size());
//		System.out.println("specialGroup_writer "+ specialGroup_writer.size());
		
		temp.setAccessRules_access(accessRule);
		temp.setAccessRules_writer(accessRule_wirte);
		temp.setAdmin(admin);
		temp.setSpecialUser_access(specialuser);
		temp.setSpecialUser_writer(specialuser_wirte);
		temp.setSpecialGroup_access(specialGroup_access);
		temp.setSpecialGroup_writer(specialGroup_writer);
		return_data.add(temp);
		
		return return_data;
	}






	@Override
	public List<ForumPost> getHotTopic(Long usergroup , List<Long> accessruleId, String userId, String access_channel) {
		// TODO Auto-generated method stub
//		System.out.println(" user group = "+ usergroup + " access rule = "+accessruleId + " access channel = "+ access_channel);
		
		 List<String> channel = new ArrayList<>() ;
//	        System.out.println("Access Channel = "+ access_channel);
	        if(String.valueOf(access_channel).equals("1")) {
	        	channel.add("0");
	        	channel.add("1");
	        	channel.add("2");
	        }else {
	        	channel.add("2");
	        }
	        
//	        System.out.println("Channel -"+ channel); 
		
		
		List<Object[]> query_data = new ArrayList<>();
		if (usergroup==5) {
			query_data = forumRepository.findHotPostSuperAdmin(channel);
		} else {
			List<Long> categoryId = new ArrayList<>();
			categoryId.addAll(forumRepository.findCategoryByAccessRuleAndAdmin(accessruleId, userId, channel));
//			System.out.println("Category Id Before Special User = "+ categoryId);
			categoryId.addAll(forumRepository.findCategoryBySpecialUser(userId));
//			System.out.println("Category Id After Special User - "+categoryId);
//			System.out.println("category Id " +categoryId );
			List<Long> result = new ArrayList<>();
			result = categoryId.stream()
				    .distinct()
				    .collect(Collectors.toList());
//			System.out.println("Result = " +result );
			if(result.size() == 0) {
				result.add(0L);
			}
			query_data = forumRepository.findHotPostByCategoryId(result);
		}
		List<ForumPost> return_data =new ArrayList<>();
//		System.out.println("how many hot topic "+ query_data.size());
		
		for (Integer i = 0 ; i< query_data.size() ; i++) {
		
		ForumCategoryModel subcat = new ForumCategoryModel();
		ForumPost temp= new ForumPost();
		Object[] data = (Object[]) query_data.get(i);
		subcat.setId(Long.valueOf(data[8].toString()));
		subcat.setNameEn(data[18].toString());
		subcat.setNameTc(data[19].toString());
		temp.setSubCate(subcat);
		temp.setId(Long.valueOf(data[0].toString()));
		temp.setPostTitle(data[1].toString());
		
		temp.setCreatedAt((Date) data[10]);
		
		
		return_data.add(temp);
		
		
		}
		return return_data;
	}



	@Override
	public ForumCategoryModel getCategory(Long id) {
		// TODO Auto-generated method stub
		

		Optional<ForumCategoryModel> cat =forumCategoryRepository.findById(id);
		ForumCategoryModel return_data = new ForumCategoryModel();
			return_data.setId(cat.get().getId());
			return_data.setNameEn(cat.get().getNameEn());
			return_data.setNameTc(cat.get().getNameTc());
			return_data.setImgUrl(cat.get().getImgUrl());
			return_data.setTabStyle(cat.get().getTabStyle());
				return return_data;
	}
	
	@Override 
	public void deleteCategory1(Long id, Long userId) {
		Optional<ForumCategoryModel> cat =forumCategoryRepository.findById(id);
		ForumCategoryModel return_data = cat.get();
		return_data= cat.get();
		return_data.setIsDeleted(true);
		return_data.setDeletedAt(new Date());
		return_data.setDeletedBy(userId);
		
		 forumCategoryRepository.saveAndFlush(return_data);
	}
	
	
	@Override 
	public void createCategory(JsonNode jsonNode,Long userId,User user) {
		ForumCategoryModel category = new ForumCategoryModel();
		List<ForumAdmin> admin = new ArrayList<>();
		List<ForumAccessRule> accessRule = new ArrayList<>();
		List<ForumSpecialUserModel> specialUser = new ArrayList<>();
		List<ForumSpecialUserGroupModel> specialGroup = new ArrayList<>();
		
		category.setNameEn(jsonNode.get("nameEn").asText());
		category.setNameTc(jsonNode.get("nameTc").asText());
		if(jsonNode.get("tabStyle")!=null) {
		category.setTabStyle(jsonNode.get("tabStyle").asText());
		}
		category.setLevel(jsonNode.get("level").asInt());
		
		category.setParentForumId(Long.valueOf(jsonNode.get("parentForumId").toString()));
		
		category.setAccessChannel(jsonNode.get("accessChannel").toString());
		if (jsonNode.get("showInfo")!=null) {
		category.setShowInfo(jsonNode.get("showInfo").asInt());
		}else {
		category.setShowInfo(0);
		};
		
		
		
		if(jsonNode.get("orderBy") != null) {
		category.setOrderBy(jsonNode.get("orderBy").asInt());
		}else {
		category.setOrderBy(999);
		};
		
		category.setCreatedAt(new Date());
		category.setCreatedBy(userId);
		String imgUrl = jsonNode.get("nameEn").asText();
//		System.out.println(" IMG URL = "+ imgUrl);
		if(jsonNode.get("imgUrl")!=null) {
		category.setImgUrl(jsonNode.get("imgUrl").asText());
		}
		category.setIsDeleted(false);
		
		ForumCategoryModel add_category = forumCategoryRepository.saveAndFlush(category);
		
		
		if (jsonNode.get("admin") != null) {
		for(Integer i =0; i<jsonNode.get("admin").size();i++ ) {
			ForumAdmin temp = new ForumAdmin();
			temp.setCreatedAt(new Date());
			temp.setCreatedBy(userId);
			temp.setForumCatId(add_category.getId());
//			System.out.println("Admin = "+ jsonNode.get("admin").get(i));
			temp.setUserId(jsonNode.get("admin").get(i).asText());
			temp.setIsDeleted(0);
			
		 admin.add(temp);			
		}
		
		forumAdminRepository.saveAll(admin);
		}
//		System.out.println("how many admin save = "+admin.size());
		
		
		if(jsonNode.get("accessRules_access")!= null || jsonNode.get("accessRules_writer") !=null) {
//		System.out.println("access rule read = "+ jsonNode.get("accessRules_access").size());
		for(Integer i =0 ; i <jsonNode.get("accessRules_access").size();i++ ) {
			ForumAccessRule temp = new ForumAccessRule();
			temp.setCreatedAt(new Date());
			temp.setCreatedBy(userId);
			temp.setForumCatId(add_category.getId());
			temp.setRuleRight(0);
			temp.setAccessRuleId(jsonNode.get("accessRules_access").get(i).asLong());
			temp.setIsDeleted(false);
			accessRule.add(temp);
		}
		if(jsonNode.get("accessRules_writer") !=null) {
			for(Integer i=0; i <jsonNode.get("accessRules_writer").size();i++) {
				ForumAccessRule temp = new ForumAccessRule();
				temp.setCreatedAt(new Date());
				temp.setCreatedBy(userId);
				temp.setForumCatId(add_category.getId());
				temp.setRuleRight(1);
				temp.setAccessRuleId(jsonNode.get("accessRules_writer").get(i).asLong());
				temp.setIsDeleted(false);
				accessRule.add(temp);
			}
		}	
		forumAccessRuleRepository.saveAll(accessRule);
		}
		
		
		if (jsonNode.get("specialUser_access") != null || jsonNode.get("specialUser_writer")!= null ) {
		for(Integer i = 0; i< jsonNode.get("specialUser_access").size();i++) {
			ForumSpecialUserModel temp = new ForumSpecialUserModel();
			temp.setCreatedAt(new Date());
			temp.setCreatedBy(userId);
			temp.setForumCatId(add_category.getId());
			temp.setIsDeleted(0);
			temp.setRuleRight(0);
			temp.setUserId(jsonNode.get("specialUser_access").get(i).asText());
			
			specialUser.add(temp);
		}
		
		if(jsonNode.get("specialUser_writer")!= null ) {
			for(Integer i =0 ; i< jsonNode.get("specialUser_writer").size();i++) {
				ForumSpecialUserModel temp = new ForumSpecialUserModel();
				temp.setCreatedAt(new Date());
				temp.setCreatedBy(userId);
				temp.setForumCatId(add_category.getId());
				temp.setIsDeleted(0);
				temp.setRuleRight(1);
				temp.setUserId(jsonNode.get("specialUser_writer").get(i).asText());
				
				specialUser.add(temp);
			}
		}
		forumSpecialUserRepository.saveAll(specialUser);
		}
		
		
		
		
		
		if (jsonNode.get("specialUserList_access") != null || jsonNode.get("specialUserList_writer")!= null ) {
			for(Integer i = 0; i< jsonNode.get("specialUserList_access").size();i++) {
			//	ForumSpecialUserModel temp = new ForumSpecialUserModel();
				ForumSpecialUserGroupModel temp = new ForumSpecialUserGroupModel();
				temp.setCreatedAt(new Date());
				temp.setCreatedBy(userId);
				temp.setForumCatId(add_category.getId());
				temp.setIsDeleted(0);
				temp.setRuleRight(0);
				//temp.setUserId(jsonNode.get("specialUserList_access").get(i).asLong());
				temp.setSpecialUsergroupId(jsonNode.get("specialUserList_access").get(i).asLong());
				specialGroup.add(temp);
				
			}
			
			if(jsonNode.get("specialUserList_writer")!= null ) {
				for(Integer i =0 ; i< jsonNode.get("specialUser_writer").size();i++) {
					ForumSpecialUserGroupModel temp = new ForumSpecialUserGroupModel();
					temp.setCreatedAt(new Date());
					temp.setCreatedBy(userId);
					temp.setForumCatId(add_category.getId());
					temp.setIsDeleted(0);
					temp.setRuleRight(1);
					temp.setSpecialUsergroupId(jsonNode.get("specialUserList_access").get(i).asLong());
					specialGroup.add(temp);
				}
			}
			forumSpecialGroupRepository.saveAll(specialGroup);
			}
	}



	@Override
	public void updateCategory(JsonNode jsonNode, Long userId) {
		// TODO Auto-generated method stub
		
		Optional<ForumCategoryModel> return_data1 = forumCategoryRepository.findById(jsonNode.get("id").asLong());
		List<ForumAdmin> admin = new ArrayList<>();
		List<ForumAccessRule> accessRule = new ArrayList<>();
		List<ForumSpecialUserModel> specialUser = new ArrayList<>();
		
		ForumCategoryModel category = return_data1.get();
		
		category.setNameEn(jsonNode.get("nameEn").asText());
		category.setNameTc(jsonNode.get("nameTc").asText());
		if( jsonNode.get("tabStyle") !=null) {
		category.setTabStyle(jsonNode.get("tabStyle").asText());
		}
		category.setImgUrl(jsonNode.get("imgUrl").asText());
		category.setLevel(jsonNode.get("level").asInt());
		category.setParentForumId(Long.valueOf(jsonNode.get("parentForumId").asText()));
		category.setAccessChannel(jsonNode.get("accessChannel").asText());
		if(jsonNode.get("showInfo") !=null) {
		category.setShowInfo(jsonNode.get("showInfo").asInt());
		}
		if(jsonNode.get("orderBy") !=null) {
		category.setOrderBy(jsonNode.get("orderBy").asInt());
		}
		category.setModifiedAt(new Date());
		category.setModifiedBy(userId);
		category.setIsDeleted(false);
		
		ForumCategoryModel add_category = forumCategoryRepository.saveAndFlush(category);
		
		if(jsonNode.get("admin")!=null) {
		dealWithAdmin(jsonNode,userId);
		}
		if(jsonNode.get("accessRules_access")!=null) {
		dealWithAccessRule(jsonNode, userId, 0);
		}
		if(jsonNode.get("accessRules_writer")!=null) {
			dealWithAccessRule(jsonNode, userId, 1);
		}
//		
		if(jsonNode.get("specialUser_access")!=null) {
			dealWithSpecicalUser(jsonNode,userId,0);
		}
		if(jsonNode.get("specialUser_writer")!=null) {
			dealWithSpecicalUser(jsonNode,userId,1);
		}
		
		if(jsonNode.get("specialUserList_access")!=null) {
			dealWithSpecicalUserList(jsonNode,userId,0);
		}
		if(jsonNode.get("specialUserList_writer")!=null) {
			dealWithSpecicalUserList(jsonNode,userId,1);
		}
		
		
		
		
		
		
		
		
		
		if (jsonNode.get("admin") != null) {
		for(Integer i =0; i<jsonNode.get("admin").size();i++ ) {
			ForumAdmin temp = new ForumAdmin();
			temp.setCreatedAt(new Date());
			temp.setCreatedBy(userId);
			temp.setForumCatId(add_category.getId());
//			System.out.println("Admin = "+ jsonNode.get("admin").get(i));
			temp.setUserId(jsonNode.get("admin").get(i).asText());
			temp.setIsDeleted(0);
			
		 admin.add(temp);			
		}
		
		forumAdminRepository.saveAll(admin);
		}
//		System.out.println("how many admin save = "+admin.size());
		
		
		if(jsonNode.get("accessRules_access")!= null || jsonNode.get("accessRules_writer") !=null) {
			forumAccessRuleRepository.deleteAccessRule333(jsonNode.get("id").asLong(), 0, userId, new Date());
			forumAccessRuleRepository.deleteAccessRule333(jsonNode.get("id").asLong(), 1, userId, new Date());
//		System.out.println("access rule read = "+ jsonNode.get("accessRules_access").size());
		for(Integer i =0 ; i <jsonNode.get("accessRules_access").size();i++ ) {
			ForumAccessRule temp = new ForumAccessRule();
			temp.setCreatedAt(new Date());
			temp.setCreatedBy(userId);
			temp.setForumCatId(add_category.getId());
			temp.setRuleRight(0);
			temp.setAccessRuleId(jsonNode.get("accessRules_access").get(i).asLong());
			temp.setIsDeleted(false);
			accessRule.add(temp);
		}
		for(Integer i=0; i <jsonNode.get("accessRules_writer").size();i++) {
			ForumAccessRule temp = new ForumAccessRule();
			temp.setCreatedAt(new Date());
			temp.setCreatedBy(userId);
			temp.setForumCatId(add_category.getId());
			temp.setRuleRight(1);
			temp.setAccessRuleId(jsonNode.get("accessRules_writer").get(i).asLong());
			temp.setIsDeleted(false);
			accessRule.add(temp);
		}
//		
		forumAccessRuleRepository.saveAll(accessRule);
		}
		
		
//		if (jsonNode.get("specialUser_access") != null || jsonNode.get("specialUser_writer")!= null ) {
//		for(Integer i = 0; i< jsonNode.get("specialUser_access").size();i++) {
//			ForumSpecialUserModel temp = new ForumSpecialUserModel();
//			temp.setCreatedAt(new Date());
//			temp.setCreatedBy(userId);
//			temp.setForumCatId(add_category.getId());
//			temp.setIsDeleted(0);
//			temp.setRuleRight(0);
//			temp.setUserId(jsonNode.get("specialUser_access").get(i).asLong());
//			
//			specialUser.add(temp);
//		}
//		
//		
//		for(Integer i =0 ; i< jsonNode.get("specialUser_writer").size();i++) {
//			ForumSpecialUserModel temp = new ForumSpecialUserModel();
//			temp.setCreatedAt(new Date());
//			temp.setCreatedBy(userId);
//			temp.setForumCatId(add_category.getId());
//			temp.setIsDeleted(0);
//			temp.setRuleRight(1);
//			temp.setUserId(jsonNode.get("specialUser_writer").get(i).asLong());
//			
//			specialUser.add(temp);
//		}
//		forumSpecialUserRepository.saveAll(specialUser);
//		}
	}

	
	private void dealWithAdmin(JsonNode jsonNode, Long userId) {
		List<String> adminUpdate = getForumAdmin(jsonNode);
		
		
//		System.out.println("Update Admin =" );
		
		List<String> adminDb_org = forumAdminRepository.getAdminByCategoryId(jsonNode.get("id").asLong());
//		System.out.println(adminDb_org);
	
		List<String> adminDb = adminDb_org.stream().map(
				(n) -> {
					
						return n.toString();
					
				}
				).collect(Collectors.toList());
//		System.out.println("After ");
	
		List<String> adminUpdate_add = adminUpdate.stream().filter(
				(n)-> adminDb.indexOf(n)==-1L).collect(Collectors.toList()
				);
		List<String> adminUpdate_delete = adminDb.stream().filter(
						(n)-> adminUpdate.indexOf(n) == -1L).collect(Collectors.toList()					
				);
		
		
		for(Integer j =0 ; j<adminUpdate_add.size();j++) {
			forumAdminRepository.createAdmin(jsonNode.get("id").asLong(), adminUpdate_add.get(j), userId, new Date());
		}
		
		if(adminUpdate_delete.size() == 0) {
			adminUpdate_delete.add("0");
		}
		
		forumAdminRepository.deleteAdmin(adminUpdate_delete, jsonNode.get("id").asLong(), userId, new Date());
	}
	
	private List<String> getForumAdmin(JsonNode jsonNode){
		List<String> return_data = new ArrayList<>();
		Integer admin_size = jsonNode.get("admin") == null ? 0 : jsonNode.get("admin").size();
		for(Integer i = 0; i<admin_size ;i++) {
			return_data.add(jsonNode.get("admin").get(i).asText());
		}
		return return_data;
	}
	
	
	private void dealWithSpecicalUser(JsonNode jsonNode, Long userId, Integer rule) {
		List<String> specialUserUpdate = getSpecialUser(jsonNode, rule);
		
		List<String> specialUserDb2 = forumSpecialUserRepository.getSpecialUserRepository(jsonNode.get("id").asLong(), rule);
		
		List<String> specialUserDb = specialUserDb2.stream().map(
				(n) -> {
					if(n == null) {
						return "0";
					}else {
						return n;
					}
				}
				).collect(Collectors.toList());
		
		List<String> specialUser_add = specialUserUpdate.stream().filter(
					(n) -> specialUserDb.indexOf(n) == -1).collect(Collectors.toList()
				);
		List<String> specialUser_delete = specialUserDb.stream().filter(
				 (n) -> specialUserUpdate.indexOf(n) == -1).collect(Collectors.toList()
							
				);
		for(Integer j =0 ;j <specialUser_add.size() ;j++) {
			forumSpecialUserRepository.createSpecialUser(jsonNode.get("id").asLong(), specialUser_add.get(j), rule, new Date(), userId);
		}
		if(specialUser_delete.size()==0 ) {
			specialUser_delete.add("0");
		}
		forumSpecialUserRepository.deleteSpecialUser(specialUser_delete, jsonNode.get("id").asLong(), rule, userId, new Date());
		
	}
	
	
	private void dealWithSpecicalUserList(JsonNode jsonNode, Long userId, Integer rule) {
		
		List<Integer> specialUserListUpdate = getSpecialUserList(jsonNode, rule);
		
		//List<Long> specialUserDb2 = forumSpecialUserRepository.getSpecialUserRepository(jsonNode.get("id").asLong(), rule);
		
		
		
		List<Integer> specialUserDb2 =forumSpecialGroupRepository.getSpecicalUserGroup(jsonNode.get("id").asLong(), rule);
		
		
		List<Integer> specialUserDb = specialUserDb2.stream().map(
				(n) -> {
					if(n == null) {
						return 0;
					}else {
						return n;
					}
				}
				).collect(Collectors.toList());
		
		List<Integer> specialUser_add = specialUserListUpdate.stream().filter(
					(n) -> specialUserDb.indexOf(n) == -1).collect(Collectors.toList()
				);
		List<Integer> specialUser_delete = specialUserDb.stream().filter(
				 (n) -> specialUserListUpdate.indexOf(n) == -1).collect(Collectors.toList()
							
				);
		for(Integer j =0 ;j <specialUser_add.size() ;j++) {
//			System.out.println("Special Group id = "+jsonNode.get("id").asLong()+" - "+specialUser_add.get(j) + " - "+rule );
			
			
			forumSpecialGroupRepository.createSpecialGroup( jsonNode.get("id").asLong(), specialUser_add.get(j), rule,  new Date(), userId);
		}
		if(specialUser_delete.size()==0 ) {
			specialUser_delete.add(0);
		}

		forumSpecialGroupRepository.deleteSpecialGroup(specialUser_delete, jsonNode.get("id").asLong(), rule, userId, new Date());
	}
	
	
	
	
	
	
	
	
	
	private List<String> getSpecialUser(JsonNode jsonNode, Integer rule){
		List<String> return_data = new ArrayList<>();
		if( rule ==0) {
		Integer user_size = jsonNode.get("specialUser_access") == null ? 0:jsonNode.get("specialUser_access").size();
			for (Integer i = 0 ; i<user_size;i++) {
			return_data.add(jsonNode.get("specialUser_access").get(i).asText());
			}
		} else if (rule ==1){
		Integer user_size = jsonNode.get("specialUser_writer") == null ? 0:jsonNode.get("specialUser_writer").size();
			for (Integer i = 0 ; i<user_size;i++) {
			return_data.add(jsonNode.get("specialUser_writer").get(i).asText());
			}
		}
		return return_data;
		
	}
	
	private List<Integer> getSpecialUserList(JsonNode jsonNode, Integer rule){
		List<Integer> return_data = new ArrayList<>();
		if( rule ==0) {
		Integer user_size = jsonNode.get("specialUserList_access") == null ? 0:jsonNode.get("specialUserList_access").size();
//			System.out.println("specialUserList_access = "+user_size);
			for (Integer i = 0 ; i<user_size;i++) {
//				System.out.println("specialUserList_access - " + jsonNode.get("specialUserList_access").get(i).asInt());
			return_data.add(jsonNode.get("specialUserList_access").get(i).asInt());
			}
		} else if (rule ==1){
		Integer user_size = jsonNode.get("specialUserList_writer") == null ? 0:jsonNode.get("specialUserList_writer").size();
			
//				System.out.println("specialUserList_writer = "+ user_size);
				for (Integer i = 0 ; i<user_size;i++) {
//					System.out.println("specialUserList_writer - " + jsonNode.get("specialUserList_writer").get(i).asInt());
					return_data.add(jsonNode.get("specialUserList_writer").get(i).asInt());
				}
			
		}
		return return_data;
		
	}
	
	private void dealWithAccessRule(JsonNode jsonNode, Long userId, Integer rule) {
		
//		System.out.println("Access Rule ----- ");
		List<Long> accessRule_update = getAccessRule(jsonNode, rule);
		List<Long> accessRule_Db = forumAccessRuleRepository.getAccessRule(jsonNode.get("id").asLong(), rule);
		
		
		List<Long> accessRule_add = accessRule_update.
										stream().filter(
													(n)->accessRule_Db.indexOf(n)==-1).collect(Collectors.toList()
										);
		
//		System.out.println("Forum Post Service Imp : Line 1244:  access rule in DB = "+accessRule_Db);
//		System.out.println("Forum Post Service Imp : Line 1245:  access rule update = " + accessRule_update);
		List<Long> accessRule_delete = accessRule_update.
										stream().filter(
												(n)->accessRule_Db.indexOf(n)==-1).collect(Collectors.toList()
										);
		
		
		
		
		for(Integer j = 0; j< accessRule_add.size();j++) {
//			System.out.println("Access Rule ----- ");
			forumAccessRuleRepository.createAccessRule(accessRule_add.get(j), jsonNode.get("id").asLong(), rule, userId, new Date());
		}
		
		if(accessRule_delete.size()==0) {
			accessRule_delete.add(0L);
		}
		forumAccessRuleRepository.deleteAccessRule(accessRule_delete, jsonNode.get("id").asLong(), rule, userId, new Date());						
	}
	
	
	
	
	
	
	
	
	private List<Long> getAccessRule(JsonNode jsonNode, Integer rule){
		List<Long> return_data = new ArrayList<Long>();
		if( rule == 0) {
			Integer rule_size = jsonNode.get("accessRules_access") == null? 0: jsonNode.get("accessRules_access").size();
				for(Integer i = 0 ;i<rule_size;i++) {
					return_data.add(jsonNode.get("accessRules_access").get(i).asLong());
				}
		} else if (rule ==1){
			Integer rule_size = jsonNode.get("accessRules_writer") == null? 0: jsonNode.get("accessRules_writer").size();
				for(Integer i = 0 ;i<rule_size;i++) {
					return_data.add(jsonNode.get("accessRules_writer").get(i).asLong());
				}
		}
		return return_data;
	}

	@Override
	public ForumCategoryModel deleteCategory(Long id, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Optional<ForumGallery> checkExistGallery(Long userId) {
		// TODO Auto-generated method stub
	
//		System.out.println("User Id = "+userId);
		Optional<ForumGallery> forumGallery = forumGalleryRepository.SearchByUserIdAndIsFinished(userId, 0);
//		System.out.println("Is present + "+ forumGallery.isPresent() +"Is empty? "+ forumGallery.isEmpty()+ "    "  );
		
		
		return forumGallery;
	}



	@Override
	public ForumGallery newForumGallery(ForumGallery forumGallery) {
		// TODO Auto-generated method stub
		//Path userFilePath = storageService.getUserFolderLocation();
		Path userFilePath = storageService.getCocktailLocation();
	
		
		String userGalleryFolder = userFilePath.toString() +"/"+ forumGallery.getUserId()+"/"+forumGallery.getGalleryName();
		
		
//		System.out.println("Create User Gallery Folder = "+userGalleryFolder);
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		
		
		try {
			Files.createDirectories(userGalleryFolderPath);
		}
		catch (IOException e) {
			throw new StorageException("Could not create user folder",e);
		}
		
		return forumGalleryRepository.saveAndFlush(forumGallery);
	}



	@Override
	public List<ForumGalleryDetail> findByGalleryId(ForumGallery gallery) {
		// TODO Auto-generated method stub
		
		List<ForumGalleryDetail> fileList = forumGalleryDetailRepository.findByGalleryIdAndIsDeleted(gallery.getId(), 0);
		List<ForumGalleryDetail> return_data = fileList.stream()
				  .filter((n) ->{ 
					  String fileAddress = "resources/" + gallery.getUserId() + "/" + gallery.getGalleryName();
					  return !n.getOfilename().contains(fileAddress);
				  })
				  .map((n) -> {
					  ForumGalleryDetail new_temp = new ForumGalleryDetail();
						String fileAddress = "resources/" + gallery.getUserId() + "/" + gallery.getGalleryName();
						String ofilename = n.getNfilename();
						new_temp.setId(n.getId());
						new_temp.setGalleryId(gallery.getId());
						new_temp.setOfilename(fileAddress + "/" + ofilename); 
						return new_temp;
				  })
				  .collect(Collectors.toList());
return return_data;
	}



	
	@Override
	public ForumGalleryDetail storeToUserFolder(MultipartFile file, Long galleryId) {
		
		Optional<ForumGallery> forumGallery = forumGalleryRepository.findById(galleryId);

		//Path userFilePath = storageService.getUserFolderLocation();
		
		Path userFilePath = storageService.getCocktailLocation();
	
		String userGalleryFolder = userFilePath.toString() + "/" + forumGallery.get().getUserId() + "/" + forumGallery.get().getGalleryName();
//		System.out.println("Gallert Folder = "+ userGalleryFolder);
		
		//blogGallery.get().getGalleryName(); // no this gallery File
		
		String fileAddress = "resources/" + forumGallery.get().getUserId() + "/" + forumGallery.get().getGalleryName();
		
		Path userGalleryFolderPath = Paths.get(userGalleryFolder);
		
		
		storageService.storeToLocation(file, userGalleryFolderPath);
	
		
		//File Info to Database
		//String file_name = System.currentTimeMillis()+file.getOriginalFilename();
		String file_name = StringUtils.cleanPath(file.getOriginalFilename());
        String fileType=file_name.substring(file_name.lastIndexOf("."),file_name.length());
    	Md5Encode md5 = new Md5Encode();
        String filename = md5.getMD5(file_name)+fileType;
        String ofilename = md5.getMD5(file.getOriginalFilename())+fileType;
        
        
        ForumGalleryDetail new_record = new ForumGalleryDetail();
        new_record.setCreatedAt(new Date());
        new_record.setCreatedBy(forumGallery.get().getCreatedBy());
        new_record.setGalleryId(forumGallery.get().getId());
        new_record.setLabel(fileAddress);
        new_record.setNfilename(filename);
        new_record.setOfilename(ofilename);
        new_record.setOrdering(0);
        
        
        return forumGalleryDetailRepository.saveAndFlush(new_record);
        //return forumGalleryDetailRepository.saveAndFlush(new_record);
	}



	@Override
	public List<Object> findForumAdmin(Long postId) {
		// TODO Auto-generated method stub
		
		
		List<Object> retrun_data = forumAdminRepository.getPostCreaterAndAdmin(postId) ;
		
		
		
		
		return retrun_data;
	}



	@Override
	public ForumAdmin createAdmin(String staffNo, Long categoryId, Long userId) {
		// TODO Auto-generated method stub
		
		ForumAdmin saveAdmin = new ForumAdmin();
		saveAdmin.setCreatedAt(new Date());
		saveAdmin.setCreatedBy(userId);
		saveAdmin.setUserId(staffNo);
		saveAdmin.setIsDeleted(0);
		
		
		
		
		return forumAdminRepository.saveAndFlush(saveAdmin); 
	}

	



	@Override
	public List<Object> getAdminByPost (Long postId){
		return forumAdminRepository.getPostCreaterAndAdmin(postId);
	}

	
	
	@Override 
	public List<Object> getAdminByCategory(Long categoryId){
		return forumAdminRepository.getCategoryCreaterAndAdmin(categoryId);
	}

	@Override
	public ForumPost getPostTitle(Long postId) {
		// TODO Auto-generated method stub
		return forumRepository.getPostTitle(postId);
	}



	@Override
	public Optional<ForumGallery> searchByPostId(Long postId) {
		// TODO Auto-generated method stub
		return forumGalleryRepository.SearchByPostId(postId);
	}
	

	@Override
	public Integer canDeletePost(Long postId, Long userId, String staffNo) {
		return forumRepository.canDeleteByAdmin(postId, userId, staffNo);
	}
	
	@Override 
	public Integer canDeleteComment(Long commentId, Long userId, String staffNo) {
		return forumRepository.canDeleteCommentByAdmin(commentId, userId, staffNo);
	}
	
	@Override 
	public ForumGallery saveGallery(ForumGallery SaveGallery) {
		return forumGalleryRepository.saveAndFlush(SaveGallery);
	}
	
}

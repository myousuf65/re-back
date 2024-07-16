package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.NewsCorner2Assistant;
import com.hkmci.csdkms.entity.NewsCorner2GalleryDetail;
import com.hkmci.csdkms.entity.NewsCorner2Tag;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.PostReturnModel;
import com.hkmci.csdkms.repository.NewsCorner2AssistantRepository;
import com.hkmci.csdkms.repository.NewsCorner2GalleryDetailRepository;
import com.hkmci.csdkms.repository.NewsCorner2Repository;
import com.hkmci.csdkms.repository.NewsCorner2TagRepository;
import com.hkmci.csdkms.storage.StorageService;




@Service
public class NewsCorner2ServiceImpl implements NewsCorner2Service {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Resource
	private NewsCorner2Repository newscorner2Repository;
	
	@Autowired
	@Resource
	private NewsCorner2AssistantRepository newscorner2AssistantRepository;
	
	@Autowired
	@Resource
	private NewsCorner2TagRepository newscorner2TagRepository;
	
	@Autowired
	@Resource
	private UserService userService;
	
	
	@Autowired
	@Resource
	private NewsCorner2GalleryDetailRepository newscorner2GalleryDetailRepository;
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	
	@Override
	public List<NewsCorner2> findAll() {
		return newscorner2Repository.findAll();
	}


	@Override
	public NewsCorner2 add(NewsCorner2 newscorner2) {
		return newscorner2Repository.saveAndFlush(newscorner2);
	}


	@Override
	public NewsCorner2 update(NewsCorner2 newscorner2) {

		return newscorner2Repository.saveAndFlush(newscorner2);
	}

	@Override
	public List<PostReturnModel> searchNewsCorner2ByCategoryId(Long categoryId, String sortBy, String sortOrder, 
			Integer page) {
		String orderBy = sortBy + " " + sortOrder;
		Integer limitStart = ((page - 1) * 20);
		Integer limitEnd = 20;
		List<NewsCorner2> query_data = newscorner2Repository.searchNewsCorner2ByCategoryId(categoryId,limitStart,limitEnd,orderBy);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
		for (int i = 0; i < query_data.size(); i++) {
			PostReturnModel tmp = new PostReturnModel();
			tmp.setNewsCorner2(query_data.get(i));
			return_data.add(tmp);
		}
		return return_data;
	}
	
	/*
	@Override
	public List<PostReturnModel> searchNewsCorner2ByCategoryId(Long categoryId, String sortBy, String sortOrder, 
			Integer page , List<Long> latest_newscorner2_ids, List<User> user_list_session,List<NewsCorner2GalleryDetail> newscorner2_thumb) {
		
		//EntityUtils converor = new EntityUtils();
		
		String orderBy = sortBy + " " + sortOrder;
		Integer limitStart = ((page - 1) * 20);
		Integer limitEnd = 20;
		List<Object[]> query_data = getPostReturnModel(categoryId,page, latest_newscorner2_ids,limitStart,limitEnd,orderBy);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
//		System.out.println("Return Data Size: " + query_data.size());
        for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data.get(i);
            //System.out.println(data[0]);
        	 User creator = user_list_session.stream()
					 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
					 .collect(Collectors.toList())
					 .get(0);
            data[17] = creator;
            NewsCorner2 new_newscorner2 = new NewsCorner2(data);
//            data[23] = data[23] == null ? "" : data[23];
//            //System.out.println(new_newscorner2.getId());
//            PostReturnModel temp_obj = new PostReturnModel(new_newscorner2, (Long.valueOf(data[21].toString())),
//    				(Long.valueOf(data[24].toString())),data[22].toString(), data[23].toString(), (Long.valueOf(data[25].toString())) );
////            System.out.println("Gallery ID: " + Long.valueOf(data[25].toString()));
//            //System.out.println(newscorner2_thumb.stream().map(NewsCorner2GalleryDetail::getGalleryId).collect(Collectors.toList()));
//            List<String> thumbnail_list = newscorner2_thumb.stream().filter(
//				    				(n) -> n != null && n.getGalleryId().equals(Long.valueOf(data[25].toString()))
//				    		).map(
//				    				(n) -> {
//				    					String fileAddress = "resources/" + n.getCreatedBy() + "/" + data[26].toString();
//										String ofilename = n.getNfilename();
//										//n.setOfilename(fileAddress + "/" + ofilename); 
//										return fileAddress + "/" + ofilename;
//				    				}
//				    		).collect(Collectors.toList());
            
            data[24] = data[24] == null ? "" : data[24];
            //System.out.println(new_newscorner2.getId());
            PostReturnModel temp_obj = new PostReturnModel(new_newscorner2, (Long.valueOf(data[22].toString())),
    				(Long.valueOf(data[25].toString())),data[23].toString(), data[24].toString(), (Long.valueOf(data[26].toString())) );
//            System.out.println("Gallery ID: " + Long.valueOf(data[25].toString()));
            //System.out.println(newscorner2_thumb.stream().map(NewsCorner2GalleryDetail::getGalleryId).collect(Collectors.toList()));
            List<String> thumbnail_list = newscorner2_thumb.stream().filter(
				    				(n) -> n != null && n.getGalleryId().equals(Long.valueOf(data[26].toString()))
				    		).map(
				    				(n) -> {
				    					String fileAddress = "resources/" + n.getCreatedBy() + "/" + data[27].toString();
										String ofilename = n.getNfilename();
										//n.setOfilename(fileAddress + "/" + ofilename); 
										return fileAddress + "/" + ofilename;
				    				}
				    		).collect(Collectors.toList());
            
            
            String thumb = thumbnail_list == null || thumbnail_list.size() == 0 ? "" : thumbnail_list.get(0);
            temp_obj.getNewsCorner2().setThumbnail(
            		thumb
            );
            return_data.add( temp_obj );
            //System.out.println(relationTopic.toString());
        }
		return return_data;
	}
	*/
	
	@Override
	public Integer getTotalSearchNewsCorner2ByCategoryId(Long categoryId, String sortBy, String sortOrder, Integer page) {
		
		//EntityUtils converor = new EntityUtils();
		
		String orderBy = sortBy + " " + sortOrder;
		//Integer query_data = getPostTotal(categoryId,page, latest_newscorner2_ids,orderBy);
		Integer query_data = getNewsCorner2Total(categoryId,page,orderBy);
		return query_data;
	}
	/*
	public List<NewsCorner2> getNewsCorner2(Long categoryId, Integer page , Integer limitStart, Integer limitEnd,  String orderBy){
 
		
		String sql = "select b.* "
    			+ " from newscorner2_post b"
    			+ " left join ( "
    			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid"
    			+ " ) l on b.id = l.lid"
    			+ " left join ( "
    			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id"
    			+ " ) c on b.id = c.cid"
    			+ " left join user u on u.id = b.original_creator "
    			+ " left join newscorner2_gallery bg on bg.post_id = b.id "
    			+ " where (b.category_id = " + categoryId + " or " + categoryId + " = 0) "
				+ " and b.is_deleted = 0  and b.publish_at < CURRENT_TIMESTAMP and b.is_public = 1 and b.id "
    			+ " order by " + orderBy + ", publish_at desc"
    			+ " limit " + limitStart + " , " + limitEnd;
    	@SuppressWarnings("unchecked")
		TypedQuery<NewsCorner2> query = (TypedQuery<NewsCorner2>) entityManager.createNativeQuery(sql);
    	//query.setParameter(1,id);
    	List<NewsCorner2> result = query.getResultList();
    	
    	return result;
    }*/
	
	
	public Integer getNewsCorner2Total(Long categoryId, Integer page , String orderBy){
		
		String sql = "select b.id as total"
    			+ " from newscorner2_post b"
    			+ " left join ( "
    			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid"
    			+ " ) l on b.id = l.lid"
    			+ " left join ( "
    			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id"
    			+ " ) c on b.id = c.cid"
    			+ " left join user u on u.id = b.original_creator "
    			+ " left join newscorner2_gallery bg on bg.post_id = b.id "
    			+ " where (b.category_id = " + categoryId + " or " + categoryId + " = 0) "
				+ " and b.is_deleted = 0 and b.is_public = 1";
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
    	//query.setParameter(1,id);
		List<Object[]> result = query.getResultList(); 
//		System.out.println("Result size: " + result.size());
    	//Long return_result = Long.parseLong(result.toString());
    	
    	return result.size();
    }
	
public Integer getPostTotal(Long categoryId, Integer page , List<Long> latest_newscorner2_ids, String orderBy){
    	
		String query_array = "(0";
		for(Integer i = 0; i < latest_newscorner2_ids.size(); i++) {
			query_array += "," + latest_newscorner2_ids.get(i);
		}
		query_array += ")";
		
		String sql = "select b.id as total"
    			+ " from newscorner2_post b"
    			+ " left join ( "
    			+ "		select pkid as lid,count(id) as likes from newscorner2_like where func_id = 1 and is_deleted = 0 group by pkid"
    			+ " ) l on b.id = l.lid"
    			+ " left join ( "
    			+ "		select post_id as cid,count(id) as comments from newscorner2_comment where is_deleted = 0 group by post_id"
    			+ " ) c on b.id = c.cid"
    			+ " left join user u on u.id = b.original_creator "
    			+ " left join newscorner2_gallery bg on bg.post_id = b.id "
    			+ " where (b.category_id = " + categoryId + " or " + categoryId + " = 0) "
				+ " and b.is_deleted = 0 and b.is_public = 1 and b.id not in " + query_array;
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
    	//query.setParameter(1,id);
		List<Object[]> result = query.getResultList(); 
//		System.out.println("Result size: " + result.size());
    	//Long return_result = Long.parseLong(result.toString());
    	
    	return result.size();
    }
	
	@Override
	public List<PostReturnModel> searchNewsCorner2ByLatest(List<User> user_list_session,List<NewsCorner2GalleryDetail> newscorner2_thumb) {
		
		List<Object[]> query_data = newscorner2Repository.searchNewsCorner2ByLatest();
		System.out.println("Run searchNewsCorner2ByLatest"+query_data.get(0)[17].toString());
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
        for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data.get(i);
    		System.out.println("Run searchNewsCorner2ByLatest:query_data:"+i+","+data[17].toString());
    		System.out.println("Run user_list_session size"+user_list_session.size());
            //System.out.println(data[0]);
            User creator = user_list_session.stream()
            						 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
            						 .collect(Collectors.toList())
            						 .get(0);
            data[17] =  creator;
            NewsCorner2 new_newscorner2 = new NewsCorner2(data);
 //           data[23] = data[23] == null ? "" : data[23];
            data[24] = data[24] == null ? "" : data[24];
            
//            System.out.println("NewsCorner2 ID: " + new_newscorner2.getId());
//            System.out.println("likes: " + (Long.valueOf(data[21].toString())));
//            System.out.println("comments: " + (Long.valueOf(data[24].toString())));
//            System.out.println("fullname: " + data[22].toString());
//            System.out.println("chinese name: " + data[23].toString());

//            PostReturnModel temp_obj = new PostReturnModel(new_newscorner2, (Long.valueOf(data[21].toString())),
//    				(Long.valueOf(data[24].toString())),data[22].toString(), data[23].toString(), (Long.valueOf(data[25].toString())) );
//            List<String> thumbnail_list = newscorner2_thumb.stream().filter(
//    				(n) -> n.getGalleryId().equals(temp_obj.getGalleryId())
//    		).map(
//    				(n) -> {
//    					String fileAddress = "resources/" + n.getCreatedBy() + "/" + data[26].toString();
//						String ofilename = n.getNfilename();
//						//n.setOfilename(fileAddress + "/" + ofilename); 
//						return fileAddress + "/" + ofilename;
//    				}
//    		).collect(Collectors.toList());

            
            PostReturnModel temp_obj = new PostReturnModel(new_newscorner2, (Long.valueOf(data[22].toString())),
     				(Long.valueOf(data[25].toString())),data[23].toString(), data[24].toString(), (Long.valueOf(data[26].toString())) );
             List<String> thumbnail_list = newscorner2_thumb.stream().filter(
     				(n) -> n.getGalleryId().equals(temp_obj.getGalleryId())
     		).map(
     				(n) -> {
     					String fileAddress = "resources/" + n.getCreatedBy() + "/" + data[27].toString();
 						String ofilename = n.getNfilename();
 						//n.setOfilename(fileAddress + "/" + ofilename); 
 						return fileAddress + "/" + ofilename;
     				}
     		).collect(Collectors.toList());
            
            
            String thumbnail = thumbnail_list == null || thumbnail_list.size() == 0 ? "" : thumbnail_list.get(0);
            temp_obj.getNewsCorner2().setThumbnail(
            		thumbnail
            );
            return_data.add( temp_obj );
        }
		return return_data;
	}


	@Override
	public Optional<NewsCorner2> findById(Long newscorner2Id) {
		return newscorner2Repository.findByIdAndIsDeleted(newscorner2Id,0);
	}
	
	
	
	@Override
	public List<NewsCorner2> getRelate(Long newscorner2Id){
		return newscorner2Repository.getRelated(newscorner2Id);
	}

	@Override
	public void createNewsCorner2Tag(String tag, Long postId, Long userId) {
		NewsCorner2Tag newscorner2Tag = new NewsCorner2Tag(postId,tag,0);
		newscorner2TagRepository.saveAndFlush(newscorner2Tag);
		return;
	}


	@Override
	public PostReturnModel getNewsCorner2ById(Long newscorner2Id, Long userId, List<User> user_session_list,Long user) {
		List<String> post_tags = newscorner2Repository.getTagsById(newscorner2Id);
		Object[] data = (Object[])newscorner2Repository.getDetailsById(newscorner2Id,userId);
		if(data == null) {
			return null;
		}
//		System.out.println("NewsCorner2 Id = "+ newscorner2Id + " User ID ="+userId+" data[17] = " +data[17].toString());
    
		User creator = user_session_list.stream()
				 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
				 .collect(Collectors.toList())
				 .get(0);
        data[17] = creator;
        NewsCorner2 new_newscorner2 = new NewsCorner2(data);
//        System.out.println(">>>>>>>>26>>>>>>>>>");
//        System.out.println(Long.valueOf(data[26].toString()));
//        System.out.println(">>>>>>>>22>>>>>>>>>");
//        System.out.println(data[22].toString());
//        System.out.println(">>>>>>>>23>>>>>>>>>");
//        System.out.println(data[23].toString());

//        data[23] = data[23] == null ? "" : data[23];
//        data[22] = data[22] == null ? "" : data[22];
//        PostReturnModel return_data = new PostReturnModel(new_newscorner2, (Long.valueOf(data[21].toString())),
//        		(Long.valueOf(data[26].toString())),data[22].toString(), data[23].toString(), 
//        		 (Integer.parseInt(data[24].toString())),Integer.parseInt(data[25].toString()), post_tags) ;

        data[24] = data[24] == null ? "" : data[24];
        data[23] = data[23] == null ? "" : data[23];
        PostReturnModel return_data = new PostReturnModel(new_newscorner2, (Long.valueOf(data[22].toString())),
        		(Long.valueOf(data[27].toString())),data[23].toString(), data[24].toString(), 
        		 (Integer.parseInt(data[25].toString())),Integer.parseInt(data[26].toString()), post_tags) ;
        
        
//        System.out.println("Like = "+ (Long.valueOf(data[21].toString()))+" Comment = "+data[26].toString());
//        System.out.println("fullname = "+ data[22].toString() +" Chinese  = "+ data[23].toString());
//        System.out.println("is Like  = "+ (Long.valueOf(data[24].toString())) +" is bookmark  = "+ Integer.parseInt(data[25].toString()));
//        
      //  System.out.println(data[1].toString()+ "  "+data[2].toString()+ "  "+data[3].toString()+ "  "+ data[4].toString()+ "  "+ data[5].toString()+ "  "+ data[6].toString()+ "  "+ data[7].toString()+ "  "+ data[8].toString()+ "  "+ data[9].toString()+ "  "+ data[10].toString()+ "  ");
         
      //  System.out.println(data[11].toString()+ "  "+data[12].toString()+ "  "+data[13].toString()+ "  "+ data[14].toString()+ "  "+ data[15].toString()+ "  "+ data[16].toString()+ "  "+ data[17].toString()+ "  "+ data[18].toString()+ "  "+ data[19].toString()+ "  "+ data[20].toString()+ "  ");
         
//        System.out.println(data[21].toString()+ "  "+data[22].toString()+ "  "+data[23].toString()+ "  "+ data[24].toString()+ "  "+ data[25].toString()+ "  "+ data[26].toString()+ "  ");
        
        
        
        
        
        //System.out.println(relationTopic.toString());
		
		return return_data;
	}

	@Override
	public List<PostReturnModel> getMyPosts(Long userId,List<User> user_session_list) {
		List<Object> query_data = newscorner2Repository.getMyPosts(userId);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
        for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data.get(i);
        	
//            System.out.println(">>>>>>>>22>>>>>>>>>");
//            System.out.println(data[22].toString());
//            System.out.println(">>>>>>>>23>>>>>>>>>");
//        	data[22] = data[22] == null ? data[22] : data[22];
//            data[23] = data[23] == null ? data[22] : data[23];
           	data[23] = data[23] == null ? data[23] : data[23];
            data[24] = data[24] == null ? data[24] : data[24];
//            System.out.println(data[23].toString());
        	
        	User creator = user_session_list.stream()
					 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
					 .collect(Collectors.toList())
					 .get(0);
            data[17] = creator;
            NewsCorner2 new_newscorner2 = new NewsCorner2(data);
//            data[23] = data[23] == null ? "" : data[23];
//            return_data.add( new PostReturnModel(new_newscorner2, (Long.valueOf(data[21].toString())),(Long.valueOf(data[24].toString())),data[22].toString(), data[23].toString() ) );
            data[24] = data[24] == null ? "" : data[24];
            return_data.add( new PostReturnModel(new_newscorner2, (Long.valueOf(data[22].toString())),(Long.valueOf(data[25].toString())),data[23].toString(), data[24].toString() ) );

        
        }
		return return_data;
	}


	@Override
	public void addHit(Long newscorner2Id, Long userId) {
		Optional<NewsCorner2> targetNewsCorner2 = newscorner2Repository.findByIdAndIsDeleted(newscorner2Id,0);
		if(targetNewsCorner2.isPresent()) {
			targetNewsCorner2.get().setHit( targetNewsCorner2.get().getHit() + 1 );
			newscorner2Repository.saveAndFlush(targetNewsCorner2.get());
			return;
		}else {
			return;
		}
	}


	@Override
	public List<PostReturnModel> getMyBookmarks(Long userId, List<User> user_session_list) {
		List<Object> query_data = newscorner2Repository.getMyBookmarks(userId);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
		for (int i = 0; i < query_data.size(); i++) {
			Object[] data = (Object[])query_data.get(i);
			User creator = user_session_list.stream()
					 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
					 .collect(Collectors.toList())
					 .get(0);
            data[17] = (User) creator;
            NewsCorner2 new_newscorner2 = new NewsCorner2(data);
//            data[23] = data[23] == null ? "" : data[23];
//		    return_data.add( new PostReturnModel(new_newscorner2, (Long.valueOf(data[21].toString())),(Long.valueOf(data[21].toString())),data[22].toString(), data[23].toString() ) );
            data[24] = data[24] == null ? "" : data[24];
		    return_data.add( new PostReturnModel(new_newscorner2, (Long.valueOf(data[22].toString())),(Long.valueOf(data[22].toString())),data[23].toString(), data[24].toString() ) );

		
		}
		return return_data;
	}


	@Override
	public Optional<NewsCorner2> checkNewsCorner2Status(Long newscorner2Id) {
		
		return newscorner2Repository.checkNewsCorner2Status(newscorner2Id);
	}


	@Override
	public List<NewsCorner2Tag> getNewsCorner2Tags(Long newscorner2Id) {
		return newscorner2TagRepository.getTagWithNewsCorner2Id(newscorner2Id);
	}


	@Override
	public void saveAllTags(List<NewsCorner2Tag> tags) {
		
		newscorner2TagRepository.saveAll(tags);
		return;
		
	}


	@Override
	public void deleteAssistant(Long newscorner2gerId, Long assistantId, Long userId) {
		newscorner2AssistantRepository.deleteAssistant(newscorner2gerId, assistantId, userId);
		return;
		
	}


	@Override
	public NewsCorner2Assistant createAssistant(Long newscorner2gerId, Long assistantId, Long userId) {

		List<NewsCorner2Assistant> targetCheck = newscorner2AssistantRepository.findByUserId(newscorner2gerId).stream().filter((n) -> n.getAssistantId().equals(assistantId)).collect(Collectors.toList());
		if( targetCheck == null || targetCheck.size() == 0 ) {
			NewsCorner2Assistant new_assistant = new NewsCorner2Assistant();
			new_assistant.setAssistantId(assistantId);
			new_assistant.setIsDeleted(0);
			new_assistant.setUserId(newscorner2gerId);
			new_assistant.setCreatedBy(userId);
			new_assistant.setCreatedAt(new Date());
			return newscorner2AssistantRepository.saveAndFlush(new_assistant);
		}else {
			NewsCorner2Assistant target = targetCheck.get(0);
			if(target.getIsDeleted().equals(0)) {
				return null;
			}else {
				target.setIsDeleted(0);
				return newscorner2AssistantRepository.saveAndFlush(target);
			}
		}
	}


	@Override
	public void updateAllTags(List<NewsCorner2Tag> tags_to_be_updated) {
		newscorner2TagRepository.saveAll(tags_to_be_updated);
		
	}
	
	@Override
	public void deleteAllTags(List<NewsCorner2Tag> tags_to_be_updated) {
		newscorner2TagRepository.saveAll(tags_to_be_updated);
		
	}

	
	
}
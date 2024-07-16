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

import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.BlogAssistant;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.BlogTag;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.PostReturnModel;
import com.hkmci.csdkms.repository.BlogAssistantRepository;
import com.hkmci.csdkms.repository.BlogGalleryDetailRepository;
import com.hkmci.csdkms.repository.BlogRepository;
import com.hkmci.csdkms.repository.BlogTagRepository;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.storage.StorageService;




@Service
public class BlogServiceImpl implements BlogService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Resource
	private UserRepository userRepository;
	
	@Autowired
	@Resource
	private BlogRepository blogRepository;
	
	@Autowired
	@Resource
	private BlogAssistantRepository blogAssistantRepository;
	
	@Autowired
	@Resource
	private BlogTagRepository blogTagRepository;
	
	@Autowired
	@Resource
	private UserService userService;
	
	
	@Autowired
	@Resource
	private BlogGalleryDetailRepository blogGalleryDetailRepository;
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	
	@Override
	public List<Blog> findAll() {
		return blogRepository.findAll();
	}


	@Override
	public Blog add(Blog blog) {
		return blogRepository.saveAndFlush(blog);
	}


	@Override
	public Blog update(Blog blog) {

		return blogRepository.saveAndFlush(blog);
	}


	@Override
	public List<PostReturnModel> searchBlogByCategoryId(Long categoryId, String sortBy, String sortOrder, 
			Integer page , List<Long> latest_blog_ids, List<User> user_list_session,List<BlogGalleryDetail> blog_thumb) {
		
		//EntityUtils converor = new EntityUtils();
		
		String orderBy = sortBy + " " + sortOrder;
		Integer limitStart = ((page - 1) * 20);
		Integer limitEnd = 20;
		List<Object[]> query_data = getPostReturnModel(categoryId,page, latest_blog_ids,limitStart,limitEnd,orderBy);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
//		System.out.println("Return Data Size: " + query_data.size());
        for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data.get(i);
            //System.out.println(data[0]);
//        	 User creator = user_list_session.stream()
//					 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
//					 .collect(Collectors.toList())
//					 .get(0);
        	
        	Optional<User> creator = userRepository.findById(Long.parseLong(data[17].toString()));
        	if(creator.isPresent()) {
        		data[17] = creator.get();
        	} else {
        		
        	}
            Blog new_blog = new Blog(data);
//            data[23] = data[23] == null ? "" : data[23];
//            //System.out.println(new_blog.getId());
//            PostReturnModel temp_obj = new PostReturnModel(new_blog, (Long.valueOf(data[21].toString())),
//    				(Long.valueOf(data[24].toString())),data[22].toString(), data[23].toString(), (Long.valueOf(data[25].toString())) );
////            System.out.println("Gallery ID: " + Long.valueOf(data[25].toString()));
//            //System.out.println(blog_thumb.stream().map(BlogGalleryDetail::getGalleryId).collect(Collectors.toList()));
//            List<String> thumbnail_list = blog_thumb.stream().filter(
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
            //System.out.println(new_blog.getId());
            PostReturnModel temp_obj = new PostReturnModel(new_blog, (Long.valueOf(data[22].toString())),
    				(Long.valueOf(data[25].toString())),data[23].toString(), data[24].toString(), (Long.valueOf(data[26].toString())) );
//            System.out.println("Gallery ID: " + Long.valueOf(data[25].toString()));
            //System.out.println(blog_thumb.stream().map(BlogGalleryDetail::getGalleryId).collect(Collectors.toList()));
            List<String> thumbnail_list = blog_thumb.stream().filter(
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
            temp_obj.getBlog().setThumbnail(
            		thumb
            );
            return_data.add( temp_obj );
            //System.out.println(relationTopic.toString());
        }
		return return_data;
	}
	
	@Override
	public Integer getTotalSearchBlogByCategoryId(Long categoryId, String sortBy, String sortOrder, Integer page,
			List<Long> latest_blog_ids, List<User> user_list_session, List<BlogGalleryDetail> blog_thumb) {
		
		//EntityUtils converor = new EntityUtils();
		
		String orderBy = sortBy + " " + sortOrder;
		Integer query_data = getPostTotal(categoryId,page, latest_blog_ids,orderBy);
        
		return query_data;
	}
	
	public List<Object[]> getPostReturnModel(Long categoryId, Integer page , List<Long> latest_blog_ids, Integer limitStart, Integer limitEnd,  String orderBy){
    	
		String query_array = "(0";
		for(Integer i = 0; i < latest_blog_ids.size(); i++) {
			query_array += "," + latest_blog_ids.get(i);
		}
		query_array += ")";
		
		String sql = "select b.*,"
    			+ " CASE WHEN l.likes IS NOT NULL "
    			+ " THEN l.likes ELSE 0 "
    			+ " END as likes, u.fullname, u.chinese_name,"
    			+ " CASE WHEN c.comments IS NOT NULL "
    			+ " THEN c.comments ELSE 0 "
    			+ " END as comments, "
    			+ " bg.id as gallery_id, "
    			+ " bg.gallery_name "
    			+ " from blog_post b"
    			+ " left join ( "
    			+ "		select pkid as lid,count(id) as likes from blog_like where func_id = 1 and is_deleted = 0 group by pkid"
    			+ " ) l on b.id = l.lid"
    			+ " left join ( "
    			+ "		select post_id as cid,count(id) as comments from blog_comment where is_deleted = 0 group by post_id"
    			+ " ) c on b.id = c.cid"
    			+ " left join user u on u.id = b.original_creator "
    			+ " left join blog_gallery bg on bg.post_id = b.id "
    			+ " where (b.category_id = " + categoryId + " or " + categoryId + " = 0) "
				+ " and b.is_deleted = 0  and b.publish_at < CURRENT_TIMESTAMP and b.is_public = 1 and b.id not in " + query_array
    			+ " order by " + orderBy + ", publish_at desc"
    			+ " limit " + limitStart + " , " + limitEnd;
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
    	//query.setParameter(1,id);
    	List<Object[]> result = query.getResultList();
    	
    	return result;
    }
	
public Integer getPostTotal(Long categoryId, Integer page , List<Long> latest_blog_ids, String orderBy){
    	
		String query_array = "(0";
		for(Integer i = 0; i < latest_blog_ids.size(); i++) {
			query_array += "," + latest_blog_ids.get(i);
		}
		query_array += ")";
		
		String sql = "select b.id as total"
    			+ " from blog_post b"
    			+ " left join ( "
    			+ "		select pkid as lid,count(id) as likes from blog_like where func_id = 1 and is_deleted = 0 group by pkid"
    			+ " ) l on b.id = l.lid"
    			+ " left join ( "
    			+ "		select post_id as cid,count(id) as comments from blog_comment where is_deleted = 0 group by post_id"
    			+ " ) c on b.id = c.cid"
    			+ " left join user u on u.id = b.original_creator "
    			+ " left join blog_gallery bg on bg.post_id = b.id "
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
	public List<PostReturnModel> searchBlogByLatest(List<User> user_list_session,List<BlogGalleryDetail> blog_thumb) {
		
		List<Object[]> query_data = blogRepository.searchBlogByLatest();
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
        for (int i = 0; i < query_data.size(); i++) {
        	Object[] data = (Object[])query_data.get(i);
            //System.out.println(data[0]);
//            User creator = user_list_session.stream()
//            						 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
//            						 .collect(Collectors.toList())
//            						 .get(0);
        	Optional<User> creator = userRepository.findById(Long.parseLong(data[17].toString()));
            if(creator.isPresent()) {
            	data[17] =  creator.get();
            }
            Blog new_blog = new Blog(data);
 //           data[23] = data[23] == null ? "" : data[23];
            data[24] = data[24] == null ? "" : data[24];
            
//            System.out.println("Blog ID: " + new_blog.getId());
//            System.out.println("likes: " + (Long.valueOf(data[21].toString())));
//            System.out.println("comments: " + (Long.valueOf(data[24].toString())));
//            System.out.println("fullname: " + data[22].toString());
//            System.out.println("chinese name: " + data[23].toString());

//            PostReturnModel temp_obj = new PostReturnModel(new_blog, (Long.valueOf(data[21].toString())),
//    				(Long.valueOf(data[24].toString())),data[22].toString(), data[23].toString(), (Long.valueOf(data[25].toString())) );
//            List<String> thumbnail_list = blog_thumb.stream().filter(
//    				(n) -> n.getGalleryId().equals(temp_obj.getGalleryId())
//    		).map(
//    				(n) -> {
//    					String fileAddress = "resources/" + n.getCreatedBy() + "/" + data[26].toString();
//						String ofilename = n.getNfilename();
//						//n.setOfilename(fileAddress + "/" + ofilename); 
//						return fileAddress + "/" + ofilename;
//    				}
//    		).collect(Collectors.toList());

            
            PostReturnModel temp_obj = new PostReturnModel(new_blog, (Long.valueOf(data[22].toString())),
     				(Long.valueOf(data[25].toString())),data[23].toString(), data[24].toString(), (Long.valueOf(data[26].toString())) );
             List<String> thumbnail_list = blog_thumb.stream().filter(
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
            temp_obj.getBlog().setThumbnail(
            		thumbnail
            );
            return_data.add( temp_obj );
        }
		return return_data;
	}


	@Override
	public Optional<Blog> findById(Long blogId) {
		return blogRepository.findByIdAndIsDeleted(blogId,0);
	}
	
	
	
	@Override
	public List<Blog> getRelate(Long blogId){
		return blogRepository.getRelated(blogId);
	}

	@Override
	public void createBlogTag(String tag, Long postId, Long userId) {
		BlogTag blogTag = new BlogTag(postId,tag,0);
		blogTagRepository.saveAndFlush(blogTag);
		return;
	}


	@Override
	public PostReturnModel getBlogById(Long blogId, Long userId, List<User> user_session_list,Long user) {
		List<String> post_tags = blogRepository.getTagsById(blogId);
		Object[] data_wild = (Object[])blogRepository.getDetailsById(blogId,userId);
		Object[] data = (Object[])blogRepository.getDetailsByIdMyself(blogId,userId);
		if(data_wild==null && Long.parseLong(data[17].toString())!=userId) {
			return null;
		}
		if(data == null) {
			return null;
		}
//		System.out.println("Blog Id = "+ blogId + " User ID ="+userId+" data[17] = " +data[17].toString());
    
//		User creator = user_session_list.stream()
//				 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
//				 .collect(Collectors.toList())
//				 .get(0);
//        data[17] = creator;
		Optional<User> creator = userRepository.findById(Long.parseLong(data[17].toString()));
    	if(creator.isPresent()) {
    		data[17] = creator.get();
    	} else {
    		
    	}
        Blog new_blog = new Blog(data);
//        System.out.println(">>>>>>>>26>>>>>>>>>");
//        System.out.println(Long.valueOf(data[26].toString()));
//        System.out.println(">>>>>>>>22>>>>>>>>>");
//        System.out.println(data[22].toString());
//        System.out.println(">>>>>>>>23>>>>>>>>>");
//        System.out.println(data[23].toString());

//        data[23] = data[23] == null ? "" : data[23];
//        data[22] = data[22] == null ? "" : data[22];
//        PostReturnModel return_data = new PostReturnModel(new_blog, (Long.valueOf(data[21].toString())),
//        		(Long.valueOf(data[26].toString())),data[22].toString(), data[23].toString(), 
//        		 (Integer.parseInt(data[24].toString())),Integer.parseInt(data[25].toString()), post_tags) ;

        data[24] = data[24] == null ? "" : data[24];
        data[23] = data[23] == null ? "" : data[23];
        PostReturnModel return_data = new PostReturnModel(new_blog, (Long.valueOf(data[22].toString())),
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
		List<Object> query_data = blogRepository.getMyPosts(userId);
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
        	
//        	User creator = user_session_list.stream()
//					 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
//					 .collect(Collectors.toList())
//					 .get(0);
//            data[17] = creator;
            
            Optional<User> creator = userRepository.findById(Long.parseLong(data[17].toString()));
        	if(creator.isPresent()) {
        		data[17] = creator.get();
        	} else {
        		
        	}
            Blog new_blog = new Blog(data);
//            data[23] = data[23] == null ? "" : data[23];
//            return_data.add( new PostReturnModel(new_blog, (Long.valueOf(data[21].toString())),(Long.valueOf(data[24].toString())),data[22].toString(), data[23].toString() ) );
            data[24] = data[24] == null ? "" : data[24];
            return_data.add( new PostReturnModel(new_blog, (Long.valueOf(data[22].toString())),(Long.valueOf(data[25].toString())),data[23].toString(), data[24].toString() ) );

        
        }
		return return_data;
	}


	@Override
	public void addHit(Long blogId, Long userId) {
		Optional<Blog> targetBlog = blogRepository.findByIdAndIsDeleted(blogId,0);
		if(targetBlog.isPresent()) {
			targetBlog.get().setHit( targetBlog.get().getHit() + 1 );
			blogRepository.saveAndFlush(targetBlog.get());
			return;
		}else {
			return;
		}
	}


	@Override
	public List<PostReturnModel> getMyBookmarks(Long userId, List<User> user_session_list) {
		List<Object> query_data = blogRepository.getMyBookmarks(userId);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
		for (int i = 0; i < query_data.size(); i++) {
			Object[] data = (Object[])query_data.get(i);
//			User creator = user_session_list.stream()
//					 .filter(n -> n.getId().equals(Long.parseLong(data[17].toString())))
//					 .collect(Collectors.toList())
//					 .get(0);
//            data[17] = (User) creator;
			Optional<User> creator = userRepository.findById(Long.parseLong(data[17].toString()));
        	if(creator.isPresent()) {
        		data[17] = creator.get();
        	} else {
        		
        	}
            Blog new_blog = new Blog(data);
//            data[23] = data[23] == null ? "" : data[23];
//		    return_data.add( new PostReturnModel(new_blog, (Long.valueOf(data[21].toString())),(Long.valueOf(data[21].toString())),data[22].toString(), data[23].toString() ) );
            data[24] = data[24] == null ? "" : data[24];
		    return_data.add( new PostReturnModel(new_blog, (Long.valueOf(data[22].toString())),(Long.valueOf(data[22].toString())),data[23].toString(), data[24].toString() ) );

		
		}
		return return_data;
	}


	@Override
	public Optional<Blog> checkBlogStatus(Long blogId) {
		
		return blogRepository.checkBlogStatus(blogId);
	}


	@Override
	public List<BlogTag> getBlogTags(Long blogId) {
		return blogTagRepository.getTagWithBlogId(blogId);
	}


	@Override
	public void saveAllTags(List<BlogTag> tags) {
		
		blogTagRepository.saveAll(tags);
		return;
		
	}


	@Override
	public void deleteAssistant(Long bloggerId, Long assistantId, Long userId) {
		blogAssistantRepository.deleteAssistant(bloggerId, assistantId, userId);
		return;
		
	}


	@Override
	public BlogAssistant createAssistant(Long bloggerId, Long assistantId, Long userId) {

		List<BlogAssistant> targetCheck = blogAssistantRepository.findByUserId(bloggerId).stream().filter((n) -> n.getAssistantId().equals(assistantId)).collect(Collectors.toList());
		if( targetCheck == null || targetCheck.size() == 0 ) {
			BlogAssistant new_assistant = new BlogAssistant();
			new_assistant.setAssistantId(assistantId);
			new_assistant.setIsDeleted(0);
			new_assistant.setUserId(bloggerId);
			new_assistant.setCreatedBy(userId);
			new_assistant.setCreatedAt(new Date());
			return blogAssistantRepository.saveAndFlush(new_assistant);
		}else {
			BlogAssistant target = targetCheck.get(0);
			if(target.getIsDeleted().equals(0)) {
				return null;
			}else {
				target.setIsDeleted(0);
				return blogAssistantRepository.saveAndFlush(target);
			}
		}
	}


	@Override
	public void updateAllTags(List<BlogTag> tags_to_be_updated) {
		blogTagRepository.saveAll(tags_to_be_updated);
		
	}
	
	@Override
	public void deleteAllTags(List<BlogTag> tags_to_be_updated) {
		blogTagRepository.saveAll(tags_to_be_updated);
		
	}

	
	
}
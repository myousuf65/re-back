package com.hkmci.csdkms.service;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.hkmci.csdkms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.SpecialCollection;
import com.hkmci.csdkms.entity.FileResource;
import com.hkmci.csdkms.entity.News;
import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.ResourceAccessRule;
import com.hkmci.csdkms.entity.ResourceSpecialUser;
import com.hkmci.csdkms.entity.ResourceSpeicalGroup;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.ResourceCategoryModel;
import com.hkmci.csdkms.model.SpecialUserGroupModel;

@Service
public class ResourceServiceImpl implements ResourceService{
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
    private ResourceRepository resourceRepository;
	
	@Autowired
    private NewsCorner2Repository newsCorner2Repository;


	@Autowired
	private YouTubeRepository youTubeRepository;

	@Autowired
	private FacebookRepository facebookRepository;
	
	@Autowired
    private SpecialCollectionRepository specialCollectionRepository;
	
	@Autowired
	@Resource
	private UserService userService;
	
	@Autowired
    private ResourceAccessRuleRepository resourceAccessRuleRepository;
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private NewsRepository newsRepository;
    
	@Autowired
    private ResourceCategoryRespository resourceCategory;
	
	@Autowired
	@Resource
	private ResourceSpecialUserRepository resourceSpecialUserRepository;
	
	@Autowired
	@Resource
	private AccessRuleRepository accessRuleRepository;
	
	
	@Autowired
	@Resource
	private ResourceSpecialGroupRepository resourceSpecialGroupRepository;
	
	@Autowired
	@Resource
	private SpecialUserGroupRepository specialUserGroupRepository;
	
	
	@Autowired
	private ResourceRatingService resourceRatingService;
	
	
	@Autowired
	private ResourceHitRateService resourceHitRateService;
	
    public List<FileResource> findAll(){
        List<FileResource> ResourceList = new ArrayList<FileResource>();
        ResourceList = resourceRepository.findByDeleted(0);
        return  ResourceList;
    }
    
    public List<FileResource> findByAccessRule(Long id,Long access_channel){
    	
        List<FileResource> ResourceList = new ArrayList<FileResource>();
        List<String> channel = new ArrayList<>() ;
//        System.out.println("Access Channel = "+ access_channel);
        if(String.valueOf(access_channel).equals("1")) {
        	channel.add("0");
        	channel.add("1");
        	channel.add("2");
        }else {
        	channel.add("2");
        }
        
//        System.out.println("Channel -"+ channel); 
        ResourceList = resourceRepository.findByAccessRuleID(id,channel);
        return  ResourceList;
    }
    
    public List<FileResource> findByAccessRuleAndLimit(Long accessRuleId, Integer num, String type){
    	Integer number = num + 1;
    	List<FileResource> return_data = resourceRepository.getLimitByAccessRuleId(accessRuleId,number,type);
    	return return_data;
    }
    
    
    public HashMap<String, List<?>> findHomePageBlog(List<Long> accessRuleId, Integer is_admin, Long access_channel){
    	 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
         
    	final List<Blog> miniBlogList;
    	
    	//latestResourceList = resourceRepository.findByLatestNewsAndLatestNewsExpiryAndDeleted(accessRuleId,is_admin, channel);
    	
    	miniBlogList = blogRepository.getHomePage().stream().map(
    				(blog) -> {
    					blog.setContent(null);
    					return blog;
    				}
    			).collect(Collectors.toList());
        
        
        HashMap<String, List<?>> return_data = new HashMap<String, List<?>>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("miniblog", miniBlogList);
			}
		};
        return  return_data;
    }
    
    
    public HashMap<String, List<?>> findHomePageNewsCorner(List<Long> accessRuleId, Integer is_admin, Long access_channel){
   	 List<String> channel = new ArrayList<>() ;
//        System.out.println("Access Channel = "+ access_channel);
        if(String.valueOf(access_channel).equals("1")) {
        	channel.add("0");
        	channel.add("1");
        	channel.add("2");
        }else {
        	channel.add("2");
        }
        
   	final List<News> newsCornerList;
   	
   	//latestResourceList = resourceRepository.findByLatestNewsAndLatestNewsExpiryAndDeleted(accessRuleId,is_admin, channel);
   	
   	newsCornerList = newsRepository.getHomePage().stream().map(
   				(News) -> {
   					News.setContent(null);
   					return News;
   				}
   			).collect(Collectors.toList());
       
       
       HashMap<String, List<?>> return_data = new HashMap<String, List<?>>(){
       	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("newscorner2", newsCornerList);
			}
		};
       return  return_data;
   }


    public HashMap<String, List<?>> findHomePageNewsCorner2(List<Long> accessRuleId, Integer is_admin, Long access_channel){
   	 List<String> channel = new ArrayList<>() ;
//        System.out.println("Access Channel = "+ access_channel);
        if(String.valueOf(access_channel).equals("1")) {
        	channel.add("0");
        	channel.add("1");
        	channel.add("2");
        }else {
        	channel.add("2");
        }
    System.out.println("NewsCorner2AccessChannel:"+String.valueOf(access_channel));    
   	final List<NewsCorner2> newsCorner2List;
   	
   	//latestResourceList = resourceRepository.findByLatestNewsAndLatestNewsExpiryAndDeleted(accessRuleId,is_admin, channel);
   	
   	newsCorner2List = newsCorner2Repository.getHomePage().stream().map(
   				(newscorner2) -> {
   					newscorner2.setContent(null);
   					return newscorner2;
   				}
   			).collect(Collectors.toList());
       
       
       HashMap<String, List<?>> return_data = new HashMap<String, List<?>>(){
       	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("newsCorner2", newsCorner2List);
			}

		};
       return  return_data;
   }

	public List<NewsCorner2> findHomePageYoutube(List<Long> accessRuleId, Integer is_admin, Long access_channel){
		List<String> channel = new ArrayList<>() ;
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
		}else {
			channel.add("2");
		}
		final List<NewsCorner2> homepageYoutubeList = youTubeRepository.getHomePage();

		return homepageYoutubeList;
	}

	public List<NewsCorner2> findHomePageFacebook(List<Long> accessRuleId, Integer is_admin, Long access_channel){
		List<String> channel = new ArrayList<>() ;
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
		}else {
			channel.add("2");
		}
		final List<NewsCorner2> homepageYoutubeList = facebookRepository.getHomePage();

		return homepageYoutubeList;
	}
    
    public HashMap<String, List<?>> findHomePageSpecialCollection(List<Long> accessRuleId, Integer is_admin, Long access_channel){
    	List<String> channel = new ArrayList<>();
//      System.out.println("Access Channel = "+ access_channel+ "Access Rule Id =  "+ accessRuleId);
	    if(String.valueOf(access_channel).equals("1")) {
	      	channel.add("0");
	      	channel.add("1");
	      	channel.add("2");
	    }else {
	      	channel.add("2");
	      	channel.add("4");
	    } 
	    System.out.println("SpecialCollectionAccessChannel:"+String.valueOf(access_channel));
      	final List<SpecialCollection> specialCollectionList;		
		
		specialCollectionList = specialCollectionHomePage(accessRuleId,channel);
          
          
          HashMap<String, List<?>> return_data = new HashMap<String, List<?>>(){
          	/**
   			 * 
   			 */
   			private static final long serialVersionUID = 1L;

   			{
   				put("specialCollection", specialCollectionList);
   			}
   		};
          return  return_data;
      }
    
    public List<SpecialCollection> specialCollectionHomePage(List<Long> accessRuleId,List<String> channels){
    	String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
    	String sql = "select b.*, u.fullname"
    	        + " from special_collection_post b "
    	        + " Left Join user u ON u.id=b.original_creator"
    	        + " where b.is_deleted = 0 and b.is_public = 1 and access_channel IN (:channels)"
    	        + " and ("+accessRuleIdString
    			+ ") order by b.publish_at DESC limit 0,3";
		System.out.println("SpecialCollectionSql:"+sql);
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
    	query.setParameter("channels",channels);
    	for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		List<Object[]> result = query.getResultList(); 

		List<SpecialCollection> specialCollections = new ArrayList<>();
		for (Object[] row : result) {
			User user = new User();
			user.setFullname((String) row[18]);
		    SpecialCollection specialCollection = new SpecialCollection();
		    specialCollection.setId(((Integer) row[0]).longValue());
		    specialCollection.setPostTitle((String) row[1]);
		    specialCollection.setPostTitleZh((String) row[2]);
		    specialCollection.setCreatedAt((Date) row[3]);
		    specialCollection.setCreatedBy(((Integer) row[4]).longValue());
		    specialCollection.setPublishAt((Date) row[10]);
		    specialCollection.setIs_public(Byte.valueOf((byte) row[11]).longValue());
		    specialCollection.setOriginalCreator(user);
		    specialCollection.setShowAsAlias(Byte.valueOf((byte) row[13]).intValue());
		    specialCollection.setAlias((String) row[14]);
		    specialCollection.setLink((String) row[15]);
		    specialCollection.setAccessRuleId((String) row[16]);
		    specialCollection.setAccessChannel((String) row[17]);
		    specialCollections.add(specialCollection);
		}
    	
    	return specialCollections;
    }
    
    public HashMap<String, List<?>> findHomePageResource(List<Long> accessRuleId, Integer is_admin,Long access_channel, String staff_no){
    	 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel+ "Access Rule Id =  "+ accessRuleId);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
         
         
//         System.out.println("Channel - "+channel);
    	 List<FileResource> ResourceList1 = new ArrayList<>();
    	 List<FileResource> ResourceList2 = new ArrayList<>();
    	 List<FileResource> ResourceList3 = new ArrayList<>();
    	
    	List<FileResource> total_resource1 = new ArrayList<>(); 
    	List<FileResource> total_resource2 = new ArrayList<>(); 
    	List<FileResource> total_resource3 = new ArrayList<>(); 
    	if(is_admin.equals(0)) {
    		//total_resource = resourceRepository.findAllResourceForHomepage(accessRuleId, channel);
    		total_resource1 = resourceRepository.findAllResourceForHomepageKM(accessRuleId, channel, staff_no);
    		total_resource2 = resourceRepository.findAllResourceForHomepageKS(accessRuleId, channel,staff_no);
    		total_resource3 = resourceRepository.findAllResourceForHomepageWG(accessRuleId, channel,staff_no);
   
    	}else {
    		//total_resource = resourceRepository.findAllResourceForAdminHomepage(channel);
    		
    		total_resource2 = resourceRepository.findAllResourceForAdminHomepageKS(channel);
    		total_resource3 = resourceRepository.findAllResourceForAdminHomepageWG(channel);
    		total_resource1 = resourceRepository.findAllResourceForAdminHomepageKM(channel);
    	}
//    	for ( Integer i = 0; i < 12; i++) {
//    		if(i < 4) {
//    			ResourceList1.add(total_resource.get(i));
//    		}
//    		if(3 < i && i < 8) {
//    			ResourceList2.add(total_resource.get(i));
//    		}
//    		if(7 < i && i < 12) {
//    			ResourceList3.add(total_resource.get(i));
//    		}
//    	}
//    	
//        
    	
    	if(total_resource1!=null) {
    		for ( Integer i = 0; i < total_resource1.size(); i++) {
    			ResourceList1.add(total_resource1.get(i));
    		}
    	} 
    	if(total_resource2!=null) {
    		for ( Integer i = 0; i < total_resource2.size(); i++) {
    			
    			ResourceList2.add(total_resource2.get(i));
    			
    		}
    	}
    	if(total_resource3!=null) {
    		for ( Integer i = 0; i < total_resource3.size(); i++) {
   
    			ResourceList3.add(total_resource3.get(i));
    		}
    	}
    	
    	
    	
    	
    	
    	
        HashMap<String, List<?>> return_data = new HashMap<String, List<?>>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("kmarket", ResourceList1); 
				put("ksquare", ResourceList2); 
				put("wisdomgallery", ResourceList3);
			}
		};
        return  return_data;
    }
    
    
    public List<FileResource> getByFileName(String filename, Long access_channel){
        List<FileResource> ResourceList = new ArrayList<FileResource>();
        ResourceList = resourceRepository.findByOfilenameContaining(filename);
        //ResourceList = resourceRepository.findByOriginalnameAndDeleted(filename,0);
        return  ResourceList;
    }
    
    public List<FileResource> getByDate(Date creationdate){
        List<FileResource> ResourceList = new ArrayList<FileResource>();
        ResourceList = resourceRepository.findByCreatedAtContaining(creationdate);
        return  ResourceList;
    }
    
    public FileResource findByRef(Long id,Long access_channel,Long userId) {
      	 List<String> channel = new ArrayList<>() ;
//        System.out.println("Access Channel = "+ access_channel);
        if(String.valueOf(access_channel).equals("1")) {
        	channel.add("0");
        	channel.add("1");
        	channel.add("2");
        }else {
        	channel.add("2");
        }
//            System.out.println("find resource id = "+ id + " access channel = "+channel);
       	FileResource return_data = resourceRepository.findByIdAndDeleted(id,channel);
      
//       	System.out.println("File reosurce = "+ return_data);
//       	System.out.println("---------    Find By REF  --------- " + id);
//       	
//       	System.out.println("Reutrn data as watermark = "+ return_data.getAsWatermark() + "  As Word = "+ return_data.getAsWord() +" wfilename = "+  return_data.getWfilename());
       	if(return_data.getAsWord() == 1L) {
       		return_data.setWfilename(return_data.getWfilename());
       	} else {
       		return_data.setWfilename(null);
       	}
       	List<ResourceAccessRule> access_rules = resourceAccessRuleRepository.findByResourceId(id);
       	Integer hitRate = resourceHitRateService.CountResourceHitRate(id);
//       	System.out.println("Access rule = "+ access_rules);
       	
       		return_data.setResourceAccessRuleList(access_rules);
       		
       		return_data.setAverageRating(resourceRatingService.AverageResourceRate(id));
       		return_data.setUserRating(resourceRatingService.ResourceRate(id, userId));
       		return_data.setHitRate(hitRate+1);
       		
       		
       		
           return return_data;
       }

    public FileResource addResource(FileResource Resource) {
//    	System.out.println("In the save action "+ Resource);
    	//Resource.setFilepath("/resources/");
    	Resource.setDeleted(0);
//    	System.out.println(Resource.getTitleEn());
        return resourceRepository.saveAndFlush(Resource);
    }

 
    public FileResource updateResourceByRef(FileResource Resource) {
    	//Resource.setFilepath("/resources/");
    	Resource.setDeleted(0);
       return resourceRepository.saveAndFlush(Resource);
    }

    @Override
    public FileResource deleteResourceByRef(Long id, Long access_channel) {
    	 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
    	FileResource new_resource = new FileResource();
    	FileResource resource = resourceRepository.findByIdAndDeleted(id, channel);
    	new_resource = resource;
    	new_resource.setDeleted(1);
        return resourceRepository.save(new_resource);
    }

	@Override
	public FileResource findByNfilename(String nfilename) {
        return  resourceRepository.findByNfilenameAndDeleted(nfilename,0);
	}
	
	@Override
	public Integer checkAccessRule(List<Long> accessRuleId,Long resourceId, String staffNo, Integer is_admin, Long access_channel) {
		 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
//		System.out.println("*********accessRuleId "+accessRuleId+" resourceId = "+resourceId+ " staffNo "+staffNo+" is admin "+ is_admin  );
		List<FileResource> check_result = resourceRepository.findByAccessRuleIdAndResourceId(accessRuleId, resourceId, staffNo,is_admin, channel);
//		System.out.println("*********Check result "+check_result);
		if(check_result == null || check_result.size() == 0) {
			return 0;
		}else {
			if(check_result.get(0).getId().equals(resourceId)) {
				return 1;
			}else {
				return 0;
			}
		}
		
	}
	
	@Override
	public Long getAccessRuleByResourceIdAndUser(Long userId,Long resourceId,Long access_rule) {
		ResourceAccessRule resourceAccessRule = resourceAccessRuleRepository.findFirstByResourceOrderByIdDesc(resourceId);
		//Long accessRuleId = accessRuleRepository.getIdByResourceId(resourceId);
		return resourceAccessRule.getAccessRule();
		
	}

	@Override
	public List<FileResource> findByUserIdAndSearch(List<Long> accessRuleId, Long resourceId, String title,
													Date startdate,Date enddate,Integer ln, Long km, Long ks, Long wg, Integer page, 
													Integer is_admin,Long user_id,
													List<Long> subcategory_list, Long access_channel) {
		 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
        
//		// Check Admin Role
//		List<Long> admin_ids = new ArrayListresourceAccessRuleRepository<Long>();
//		admin_ids.add(1L);
		
//		System.out.println("Search Params: accessRuleId: " + accessRuleId + ",\n resourceId: " + resourceId 
//							+ ",\n titleEn: " + title + ",\n startdate: " + startdate + ",\n enddate: " + enddate
//							+ ",\n ln: " + ln + ",\n km: " + km + ",\n ks: " + ks + ",\n wg: " + wg);
		Integer start_num = (page - 1) * 50;
		Integer end_num = 50;
		Integer is_for_sub = 0;
		if(subcategory_list.contains(0L)) {
			is_for_sub = 0;
		}else {
			is_for_sub = 1;
		}
		
		//System.out.println("subcategory_list: " + subcategory_list.stream().collect(Collectors.toList()));
//		System.out.println("subcategory_list size : " + subcategory_list.size());
//		
//		System.out.println("Start Date =  "+ startdate +"End date = "+ enddate);
//		System.out.println("KM =" + km +" KS = " + ks + " WG = " + wg );
		return resourceRepository.findByUserIdAndSearch(resourceId, title, startdate, enddate, ln, km, ks, wg, start_num, end_num, is_admin,user_id,accessRuleId,subcategory_list,is_for_sub, channel);
	}

	@Override
	public void createResourceAccessRule(Long resourceAccessRuleId, Long resourceId) {
		ResourceAccessRule resourceAccessRule = new ResourceAccessRule();
		resourceAccessRule.setAccessRule(resourceAccessRuleId);
		resourceAccessRule.setResource(resourceId);
		resourceAccessRule.setDeletedBy(0L);
		resourceAccessRuleRepository.saveAndFlush(resourceAccessRule);
		return ;
	}
 
	
	
	@Override
	public void createResourceCategory(Long resourceCategoryId, Long resourceId) {
		
//		System.out.println("Resource Category ID "+ resourceCategoryId + " resourceId  "+ resourceId);
		
		ResourceCategoryModel resourceCategoryModel = new ResourceCategoryModel();
		resourceCategoryModel.setCategoryId(resourceCategoryId);
		resourceCategoryModel.setResourceId(resourceId);
		resourceCategoryModel.setDeletedBy(0L);
		resourceCategory.saveAndFlush(resourceCategoryModel);
		return;

	}

	
	@Override
	public void createRCByBath(List<Long> resource_category_ids, Long resourceId) {
		List<ResourceCategoryModel> toBeAdd = resource_category_ids.stream().map(
				(rc_id) -> {
					ResourceCategoryModel new_rc = new ResourceCategoryModel();
					new_rc.setCategoryId(rc_id);
					new_rc.setResourceId(resourceId);
					new_rc.setDeletedBy(0L);
					return new_rc;
				}
			).collect(Collectors.toList());
		resourceCategory.saveAll(toBeAdd);
		return;
	}
	
	@Override
	public void createRARByBath(List<Long> resource_access_rule_ids, Long resourceId) {
		List<ResourceAccessRule> toBeAdd = resource_access_rule_ids.stream().map(
				(rar_id) -> {
					ResourceAccessRule new_rar = new ResourceAccessRule();
					new_rar.setAccessRule(rar_id);
					new_rar.setResource(resourceId);
					new_rar.setDeletedBy(0L);
					return new_rar;
				}
			).collect(Collectors.toList());
		resourceAccessRuleRepository.saveAll(toBeAdd);
		return;
	}
	
	@Override
	public List<FileResource> getByFilePath(String filepath) {
		
		return null;
	}

	
	
	@Override
	public List<FileResource> getBySearchName(List<Long> categoryId, List<Long> accessRuleId, Integer page, Integer is_admin, Long access_channel,String keyword,String staffNo){
		List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
		} else {
			channel.add("2");
		}
		
		Integer startIndex = (page -1) * 20;
		List<FileResource> return_data= resourceRepository.searchByCategoryIdIdAndDeleted(categoryId, accessRuleId, startIndex, 20, is_admin, channel,keyword,staffNo);
		for(Integer i =0 ;i<return_data.size();i++) {
//		System.out.println("Before filter name  = "+ return_data.get(i).getId());
		}
		//List<FileResource> return_final = return_data.stream().filter(n -> n.getTitleEn().contains(keyword)|| n.getTitleTc().contains(keyword)).collect(Collectors.toList());
//		System.out.println("After filter name = "+ return_data.size());
		return return_data;
	}
	
	@Override
	public Integer getTotalBySearchName(List<Long> categoryId, List<Long> accessRuleId, Integer page, Integer is_admin, Long access_channel,String keyword){
		List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
		} else {
			channel.add("2");
		}
		
	
		List<FileResource> return_data= resourceRepository.getTotalsearchByCategoryIdIdAndDeleted(categoryId, accessRuleId, is_admin, channel,keyword);
//		System.out.println("Before filter name  = "+ return_data.size());
	//	List<FileResource> return_final = return_data.stream().filter(n -> n.getTitleEn().contains(keyword)|| n.getTitleTc().contains(keyword)).collect(Collectors.toList());
//		System.out.println("After filter name = "+ return_data.size());
		return return_data.size();
	}
	
	
	
	
	
	
	
	
	
	@Override
	public List<FileResource> getByAreaId(Long areaId, List<Long> accessRuleId,Integer page, Integer is_admin,Long access_channel,String sortBy, String sortOrder, String keyword,String staffNo ) {
		 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
         List<FileResource> return_data = new ArrayList<>();
		Integer startIndex = (page- 1) * 20;
		String orderBy = sortBy + " "+sortOrder;
		if (sortBy.equals("hit_rate")) {
			return_data = resourceRepository.findByAreaIdAndDeletedOrderByHitRate(areaId, accessRuleId, startIndex, 20, is_admin,channel, keyword, staffNo);
			
		} else if (sortBy.equals("modified_at")) {
			return_data = resourceRepository.findByAreaIdAndDeletedOrderByModifiedAt(areaId, accessRuleId, startIndex, 20, is_admin,channel,keyword, staffNo);
			
		} else if (sortBy.equals("title_en")&& sortOrder.equals("asc")) {
			return_data = resourceRepository.findByAreaIdAndDeletedOrderByTitleEnASC(areaId, accessRuleId, startIndex, 20, is_admin,channel,keyword, staffNo);
			
		} else if (sortBy.equals("title_en")&& sortOrder.equals("desc")) {
			return_data = resourceRepository.findByAreaIdAndDeletedOrderByTitleEnDESC(areaId, accessRuleId, startIndex, 20, is_admin,channel,keyword, staffNo);
			
		}else {
			return_data = resourceRepository.findByAreaIdAndDeleted(areaId, accessRuleId, startIndex, 20, is_admin,channel,keyword, staffNo);
			
		}
		
		
//		System.out.println(" Order by = "+ orderBy);
		// return_data = resourceRepository.findByAreaIdAndDeleted(areaId, accessRuleId, startIndex, 20, is_admin,channel,orderBy);
//		System.out.println("How many return data  = "+ return_data);
		return return_data;
	}
	
	@Override
	public Integer getTotalByAreaId(Long areaId, List<Long> accessRuleId,Integer page, Integer is_admin,Long access_channel,String keyword, String staffNo) {
		 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
        
	
		List<FileResource> return_data = resourceRepository.findTotalByAreaIdAndDeleted(areaId, accessRuleId,  is_admin,channel, keyword, staffNo);
//		System.out.println("How many return data  = "+ return_data.size());
		return return_data.size();
	}
	

	
	
	@Override
	public Integer getCatByAreaId(Long categoryId, List<Long> accessRuleId, Integer page, String user_id, Integer is_admin, Long access_channel) {
		 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
		
		Integer startIndex = (page- 1) * 10;
		String staff_no = user_id.toString();
		List<Long> sub_list = new ArrayList<Long>();
		sub_list.add(categoryId);
		Integer is_for_sub = 1;
		List<Long> resource_ids = resourceRepository.getResourceIdsFromAccessRule(accessRuleId,sub_list,is_for_sub,is_admin,channel,user_id.toString()).stream().map((r)->{return r.getId();}).collect(Collectors.toList());
//		System.out.println(" Resource size = "+resource_ids.size());
		if(resource_ids == null || resource_ids.size() == 0) {
			resource_ids.add(0L);
		}
//		System.out.println(resource_ids);
		Integer return_data = resourceRepository.findTotalByCatIdAndDeleted(resource_ids, staff_no, is_admin, channel);
//		System.out.println(" Return data size aftet find category = "+return_data);
		return return_data;
	}

	
	@Override
	public List<FileResource> getLatest(List<Long> accessRuleId, Integer page, Long user_id, Integer is_admin,
			Long access_channel) {

		
		List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
			
		} else {
			channel.add("2");
		}
			
		
		
		Integer startIndex = (page-1)*20;
		String staff_no = user_id.toString();
		
		List<Long> resource_ids = resourceRepository.getResourceLatest(accessRuleId, is_admin, channel, new Date()).stream().map((r)->{return r.getId();}).collect(Collectors.toList());
//		System.out.println("Resource Id = "+resource_ids);
		
		if(resource_ids == null || resource_ids.size() ==0) {
			resource_ids.add(0L);
			
		}
		
		List<FileResource> return_data = resourceRepository.getResourceLatestDetail(resource_ids, channel, is_admin, staff_no, startIndex, 20);
				
		return return_data;
	}
	
	@Override
	public Integer getLatestTotal(List<Long> accessRuleId, Integer page, Long user_id, Integer is_admin,
			Long access_channel) {
		// TODO Auto-generated method stub
		
		
		
		List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
			
		} else {
			channel.add("2");
		}
			
		
		
		Integer startIndex = (page-1)*20;
		String staff_no = user_id.toString();
		
		List<Long> resource_ids = resourceRepository.getResourceLatest(accessRuleId, is_admin, channel, new Date()).stream().map((r)->{return r.getId();}).collect(Collectors.toList());
//		System.out.println("Resource Id = "+resource_ids);
		
		if(resource_ids == null || resource_ids.size() ==0) {
			resource_ids.add(0L);
			
		}
		
		
		return resource_ids.size();
	}

	
	
	@Override
	public List<FileResource> getByCategoryId(Long categoryId, List<Long> accessRuleId, Integer page, Long user_id, Integer is_admin,String staff_no, Long access_channel){
		 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
		
		Integer startIndex = (page- 1) * 20;
		
//		System.out.println("staff_no =" +staff_no);
		List<Long> sub_list = new ArrayList<Long>();
		sub_list.add(categoryId);
		Integer is_for_sub = 1;
		List<Long> resource_ids = resourceRepository.getResourceIdsFromAccessRule(accessRuleId,sub_list,is_for_sub,is_admin,channel, staff_no).stream().map((r)->{return r.getId();}).collect(Collectors.toList());
//		System.out.println(" Resource size = "+resource_ids.size());
		if(resource_ids == null || resource_ids.size() == 0) {
			resource_ids.add(0L);
		}
//		System.out.println(resource_ids);
		List<FileResource> return_data = resourceRepository.findByCategoryIdAndDeleted(resource_ids, startIndex,  20, staff_no, is_admin, channel);
//		System.out.println(" Return data size aftet find category = "+return_data);
		return return_data;
	}
	
	@Override
	public List<FileResource> findByIds(List<Long> resource_ids) {
		List<FileResource> return_data = resourceRepository.findByIds(resource_ids);
		
		return return_data;
	}

	@Override
	public Integer checkAccessRuleByList(List<Long> accessRuleIdList, List<Long> resource_ids) {
		//System.out.println(resource_ids);
		List<FileResource> check_result = resourceRepository.findByAccessRuleIdAndResources(accessRuleIdList, resource_ids);
		//System.out.println(check_result);
		if(check_result == null) {
			return 0;
		}else {
			return 1;
		}
	}

	@Override
	public void deleteByIds(List<Long> resource_ids,Long userId) {
		
		resourceRepository.deleteByIds(resource_ids,userId, new Date());
		return;
	}

	@Override
	public void createResourceSpecialUser(String specialUserId, Long resourceId, Long userId) {
		
		resourceRepository.createSpecialUser(resourceId,specialUserId, new Date(), userId);
	}
	
	@Override
	public void createResoruceSpecialGroup(Integer specialGroupId, Long resourceId, Long userId) {
//		System.out.println("specialGroupId = "+ specialGroupId + " resource Id = "+ resourceId+ "user Id = "+ userId);
		resourceRepository.createSpecialGroup(resourceId, specialGroupId, new Date(), userId);
	}

	@Override
	public List<Long> getAccessRuleByResourceId(Long resourceId) {
		
		List<Long> return_data = resourceRepository.getAccessRuleByResourceId(resourceId);
		return return_data;
	}
	
	
	

	@Override
	public List<ResourceCategoryModel> getResourceCategoryByResourceId(Long resourceId) {
		
		
		List<ResourceCategoryModel> return_data = resourceRepository.getResourceCategoryByResourceId(resourceId);
		//System.out.println(">>>>>>>>>>>>>getResourceCategoryByResourceId: " + return_data.stream().collect(Collectors.toList()));
		return return_data;
	}

	
	
	
	
	@Override
	public List<String> getSpecialUserByResourceId(Long resourceId) {
		
		List<String> return_data = resourceRepository.getSpecialUserByResourceId(resourceId);
		return return_data;
	}
	
	
	@Override
	public List<Integer> getSpecialGroupByResourceId(Long resourceId){
		List<Integer> return_data = resourceRepository.getSpecialGroupByResourceId(resourceId);
//		System.out.println("Return Data = " +  return_data.size());
		return return_data;
	}

	@Override
	public void deleteResourceAccessRule(List<Long> resourceAccessRuleList_delete,Long resourceId, Long deletedBy, Date deletedAt) {
		
		resourceRepository.deleteResourceAccessRule(resourceAccessRuleList_delete ,resourceId, deletedBy, deletedAt);
		return;
	}
	
	
	@Override
	public void deleteResourceResourceCategory(List<Long> resourceCategory_delete, Long resourceId, Long deletedBy,
			Date deletedAt) {
		
		
		if(resourceCategory_delete.contains(0L)) {
			return;
		}else {
			resourceRepository.deleteResourceCategory(resourceCategory_delete,resourceId, deletedBy, deletedAt );
			return;
		}
		
	}
	@Override 
	public void deleteResoufceSpecialGroup(List<Integer> resourceSpecialGroup_delete, Long resourceId, Long deletedBy, Date deletedAt) {
		resourceRepository.deletedResourceSpecialGroup(resourceSpecialGroup_delete, resourceId, deletedBy, deletedAt);
	}
	
	
	@Override
	public void deleteResourceSpecialUser(List<String> resourceSpecialUser_delete,Long resourceId, Long deletedBy, Date deletedAt) {
		
		resourceRepository.deleteResourceSpecialUser(resourceSpecialUser_delete ,resourceId, deletedBy, deletedAt);
		return;
	}

	@Override
	public void updateMultipleResource(Long userId, List<Long> resource_ids, String accessChannel, Integer latestNews,
			Date latestNewsExpiry, Integer activated) {
		List<FileResource> toBeUpdated = resourceRepository.findByIds(resource_ids);
		toBeUpdated.stream().map(
					(r) -> {
						r.setAccessChannel(accessChannel);
						r.setActivated(activated);
						r.setLatestNews(latestNews);
						r.setLatestNewsExpiry(latestNewsExpiry);
						r.setModifiedAt(new Date());
						r.setModifiedBy(userId);
						return r;
					}
				).collect(Collectors.toList());
		resourceRepository.saveAll(toBeUpdated);
		//resourceRepository.updateMultipleResource(userId,resource_ids,accessChannel,latestNews,latestNewsExpiry,activated, new Date());
		return;
	}

	@Override
	public List<ResourceAccessRule> getAccessRuleByResourceIds(List<Long> resourceIds) {
		
		return resourceAccessRuleRepository.getAccessRuleByResourceIds(resourceIds);
	}
	
	@Override
	public List<AccessRule> getAccessRulesByResourceIds(List<Long> resourceIds) {
		
		
		return accessRuleRepository.getAccessRulesByResourceIds(resourceIds);
	}

	@Override
	public void deleteResourceAccessRuleByIds(List<Long> toBeDeleted, Long userId) {
		if(toBeDeleted.size() == 0) {
			toBeDeleted.add(0L);
		}
		resourceRepository.deleteResourceAccessRuleByIds(toBeDeleted, userId, new Date());
		return;
	}

	@Override
	public void saveAllRAR(List<ResourceAccessRule> batchList) {
		resourceAccessRuleRepository.saveAll(batchList);
		return;
		
	}
	
	
	@Override
	public void deletedSpecialGroupByIds(List<Long> resource_ids , Long userId) {
		resourceRepository.deleteResourceSpecialGroups(resource_ids, userId, new Date());
		return ;
	}

	@Override
	public void deleteSpecialUserByIds(List<Long> resource_ids, Long userId) {
		
		resourceRepository.deleteResourceSpecialUsers(resource_ids ,userId, new Date());
		return;
	}

	@Override
	public void deleteResourceCategoryByIds(List<Long> resource_ids, Long userId) {
		
		resourceRepository.deleteResourceCategoryByIDs(resource_ids, userId, new Date()); 
		return;
		
	}
		public List<ResourceCategoryModel> findCategory() {
			
			return(resourceRepository.findtest());
			
		}
	

	@Override
	public Long getMaxId() {
		Long maxId = resourceRepository.getMaxId();
		return maxId;
	}

	@Override
	public Long getTotal(List<Long> accessRuleId, Long resourceId, String title,Date startdate,Date enddate,
			 Integer ln, Long km, Long ks, Long wg, Integer page, Integer is_admin,Long user_id, List<Long> subcategory_list,Long access_channel) {
		 List<String> channel = new ArrayList<>() ;
//         System.out.println("Access Channel = "+ access_channel);
         if(String.valueOf(access_channel).equals("1")) {
         	channel.add("0");
         	channel.add("1");
         	channel.add("2");
         }else {
         	channel.add("2");
         }
		
		Integer is_for_sub = 0;
		if(subcategory_list.contains(0L)) {
			is_for_sub = 0;
		}else {
			is_for_sub = 1;
		}
		
		
		
		Long totalNum = resourceRepository.getTotalNum(resourceId, title, startdate, enddate, ln, km, ks, wg, is_admin,user_id,accessRuleId,subcategory_list,is_for_sub,channel);
		return totalNum;
	}

	@Override
	public void updateResourceCategory(List<Long> resourceCategoryId, Long resourceId) {
		
		resourceRepository.updateResourceCategory(resourceCategoryId,resourceId);
		return;
	}
	
	@Override
	public void updateResourceAccessRule(List<Long> resourceAccessRuleId, Long resourceId) {
		
		resourceRepository.updateResourceAccessRule(resourceAccessRuleId,resourceId);
		return;
	}

	@Override
	public List<ResourceCategoryModel> getResourceCategoryByResourceIds(List<Long> resource_ids) {
		List<ResourceCategoryModel> return_data = resourceRepository.getResourceCategoryByResourceIds(resource_ids);
	
		return return_data;
	}

	@Override
	public List<String> getSpecialUserByResourceIds(List<Long> resource_ids) {
		
		List<String> return_data = resourceRepository.getSpecialUserByResourceIds(resource_ids);
		return return_data;
	}
	
	@Override
	public List<ResourceSpeicalGroup> getAllSpecialGroups(List<Long> resoruce_ids){
		List<ResourceSpeicalGroup> return_data = resourceSpecialGroupRepository.getAllByResourceIds(resoruce_ids);
		return return_data;
	}
	
	
	
	@Override
	public List<ResourceSpecialUser> getAllSpecialUsers(List<Long> resource_ids) {
		
		List<ResourceSpecialUser> return_data = resourceSpecialUserRepository.getAllByResourceIds(resource_ids);
		return return_data;
	}

	
	@Override 
	public String getResourceNameById(Long resourceId) {
		
		
		String return_data = resourceRepository.getResourceNameById(resourceId);
		return return_data;
	}
	
	@Override 
	public Integer getResourceAccessChannelById(Long resourceId) {
		
		
		Integer return_data = resourceRepository.getResourceAccessChannelById(resourceId);
		return return_data;
	}
	
	
	@Override 
	public List<SpecialUserGroupModel> getResourceSpecialGroup(Long resource_ids){
	//	List<SpecialUserGroupModel> return_data = resourceSpecialGroupRepository.getByResourceIds(resource_ids);
		List<SpecialUserGroupModel> return_data = specialUserGroupRepository.getByResourceIds(resource_ids);
		return return_data;
	}
	@Override
	public List<ResourceSpecialUser> getResourceSpecialUser(List<Long> resource_ids) {
		List<ResourceSpecialUser> return_data = resourceSpecialUserRepository.getByResourceIds(resource_ids);
		return return_data;
	}

	@Override
	public void saveAllThumbs(List<FileResource> toUpdate) {
		resourceRepository.saveAll(toUpdate);
	}
	
	@Override
	public void saveSingleThumbs(FileResource toUpdate) {
		resourceRepository.saveAndFlush(toUpdate);
		
	}

	@Override
	public void saveAll(List<FileResource> r_to_update) {
		resourceRepository.saveAll(r_to_update);
		return;
	}
	
	
	@Override
	public void createRSGByBath(List<Integer> resourceSpecialGroup_to_add, Long resource_id,Long userId ) {
		List<ResourceSpeicalGroup> toBeAdd = resourceSpecialGroup_to_add.stream().map(
				(rsg_id) -> {
					ResourceSpeicalGroup new_rsg = new ResourceSpeicalGroup();
					new_rsg.setResourceId(resource_id);
					new_rsg.setCreatedAt(new Date());
					new_rsg.setSpecialGroupId(Long.valueOf(rsg_id.toString()));
					new_rsg.setIsDeleted(0);
					return new_rsg;
				}
			).collect(Collectors.toList());
		resourceSpecialGroupRepository.saveAll(toBeAdd);
		return ;
	}
	
	
	@Override
	public void createRSUByBath(List<String> resourceSpecialUser_to_add, Long resource_id) {
		
		List<ResourceSpecialUser> toBeAdd = resourceSpecialUser_to_add.stream().map(
				(rsu_id) -> {
					ResourceSpecialUser new_rsu = new ResourceSpecialUser();
					new_rsu.setStaffNo(rsu_id.toString());
					new_rsu.setResourceId(resource_id);
					new_rsu.setDeletedBy(0L);
					return new_rsu;
				}
			).collect(Collectors.toList());
		resourceSpecialUserRepository.saveAll(toBeAdd);
		return;
		
	}
	
	
	@Override
	public void updateSpecialGroup(List<Integer> resourceSpecialGroupId, Long resourceId) {
		resourceRepository.updateResourceSpecialGroup(resourceSpecialGroupId, resourceId);
		return;
	}

	@Override
	public void updateSpecialUser(List<String> resourceSpecialUserId, Long resourceId) {
		
		resourceRepository.updateResourceSpecialUser(resourceSpecialUserId,resourceId);
		return;
	}

	public Long getTotalByAreaId(Long area_id, List<Long> accessRuleId, Integer page, Integer is_admin) {
		Long total = resourceRepository.findTotalByAreaIdAndDeleted(area_id, accessRuleId, is_admin);
		return total;
	}



	
	
	@Override
	public Object getVideo() {
		// TODO Auto-generated method stub
		return resourceRepository.getVideo();
	}

	@Override
	public List<Integer> getCategoryString(Long resourceId) {
		List<Integer> rengturn_data = resourceCategory.findByResourceIdAndDeletedBy(resourceId, 0L).stream()
												   .map((n) -> {return Integer.parseInt(n.getCategoryId().toString());})
												   .collect(Collectors.toList());
		
		return rengturn_data;
	}

	@Override
	public Optional<FileResource> findById(Long resource_id, Long access_channel) {
		// TODO Auto-generated method stub
		 List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
	     	channel.add("0");
	     	channel.add("1");
	     	channel.add("2");
	     }else {
	     	channel.add("2");
	     }
//		System.out.println("Resource Id = "+ resource_id);
		return resourceRepository.findById(resource_id,channel);
	}

	@Override
	public List<FileResource> findByIdRange(Integer startNum, Integer endNum) {
		// TODO Auto-generated method stub
		return resourceRepository.findByRange(Long.parseLong(startNum.toString()), Long.parseLong(endNum.toString()));
	}
	
	
	
    public HashMap<String, List<?>> findHomePageLatest(List<Long> accessRuleId, Integer is_admin, Long access_channel, String staff_no){
      	 List<String> channel = new ArrayList<>() ;
//           System.out.println("Access Channel = "+ access_channel);
           if(String.valueOf(access_channel).equals("1")) {
           	channel.add("0");
           	channel.add("1");
           	channel.add("2");
           }else {
           	channel.add("2");
           }
           
      	final List<FileResource> latestResourceList;
      	if(is_admin.equals(1)) {
      		latestResourceList = resourceRepository.findByLatestNewsAndLatestNewsExpiryAndDeletedForAdmin(channel);
      	}else {
      		latestResourceList = resourceRepository.findByLatestNewsAndLatestNewsExpiryAndDeleted(accessRuleId, 0,channel,staff_no);
      	}
      	
      	
      	
          
          
          HashMap<String, List<?>> return_data = new HashMap<String, List<?>>(){
          	/**
   			 * 
   			 */
   			private static final long serialVersionUID = 1L;

   			{
   				put("latest", latestResourceList);
   			}
   		};
          return  return_data;
      }
	
	
	@Override
	public List<FileResource> getLatest(List<Long> accessRuleId, Integer page, String user_id, Integer is_admin,
			Long access_channel) {

		
		List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
			
		} else {
			channel.add("2");
		}
			
		
		
		Integer startIndex = (page-1)*20;
		String staff_no = user_id;
		
		List<Long> resource_ids = resourceRepository.getResourceLatest(accessRuleId, is_admin, channel, new Date(), staff_no).stream().map((r)->{return r.getId();}).collect(Collectors.toList());
//		System.out.println("Resource Id = "+resource_ids);
		
		if(resource_ids == null || resource_ids.size() ==0) {
			resource_ids.add(0L);
			
		}
		
		List<FileResource> return_data = resourceRepository.getResourceLatestDetail(resource_ids, channel, is_admin, staff_no, startIndex, 20);
				
		return return_data;
	}
	
	@Override
	public Integer getLatestTotal(List<Long> accessRuleId, Integer page, String user_id, Integer is_admin,
			Long access_channel) {
		// TODO Auto-generated method stub
		
		
		
		List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
			channel.add("0");
			channel.add("1");
			channel.add("2");
			
		} else {
			channel.add("2");
		}
			
		
		
		Integer startIndex = (page-1)*20;
		String staff_no = user_id.toString();
		
		List<Long> resource_ids = resourceRepository.getResourceLatest(accessRuleId, is_admin, channel, new Date(),staff_no).stream().map((r)->{return r.getId();}).collect(Collectors.toList());
//		System.out.println("Resource Id = "+resource_ids);
//		
//		System.out.println("Resource id = "+ resource_ids.size());
		
		return resource_ids.size();
	}





	

	
}

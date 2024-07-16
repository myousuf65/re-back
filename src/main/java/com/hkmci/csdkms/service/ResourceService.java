package com.hkmci.csdkms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.FileResource;
import com.hkmci.csdkms.entity.ResourceAccessRule;
import com.hkmci.csdkms.entity.ResourceSpecialUser;
import com.hkmci.csdkms.entity.ResourceSpeicalGroup;
import com.hkmci.csdkms.model.ResourceCategoryModel;
import com.hkmci.csdkms.model.SpecialUserGroupModel;;

@Service
public interface ResourceService {
	Object getVideo();

	List<FileResource> findAll();
	
	 Optional<FileResource> findById(Long resource_id, Long access_channel);
	
	List<FileResource> findByIds(List<Long> resource_ids);
	
	HashMap<String,List<?>> findHomePageBlog(List<Long> accessRuleId, Integer is_admin, Long access_channel);
	
	HashMap<String,List<?>> findHomePageLatest(List<Long> accessRuleId, Integer is_admin, Long access_channel,String staff_no);
	
	HashMap<String,List<?>> findHomePageResource(List<Long> accessRuleId, Integer is_admin, Long access_channel, String staff_no);
	
	List<FileResource> getByFilePath(String filepath);
	
	List<FileResource> getByAreaId(Long areaId, List<Long> accessRuleId, Integer page, Integer is_admin, Long access_channel,String sortBy, String sortOrder, String keyword,String staffNo);
	
	HashMap<String,List<?>> findHomePageNewsCorner(List<Long> accessRuleId, Integer is_admin, Long access_channel);
	
	HashMap<String,List<?>> findHomePageNewsCorner2(List<Long> accessRuleId, Integer is_admin, Long access_channel);

	HashMap<String,List<?>> findHomePageSpecialCollection(List<Long> accessRuleId, Integer is_admin, Long access_channel);
	
	List<FileResource> getByCategoryId(Long categoryId, List<Long> accessRuleId, Integer page, Long user_id, Integer is_admin, String staff_no ,Long access_channel);
	
	List<FileResource> getLatest(List<Long> accessRuleId, Integer page, Long user_id, Integer is_admin, Long access_channel);
	
	
	Integer getLatestTotal(List<Long> accessRuleId, Integer page, Long user_id, Integer is_admin, Long access_channel);
	
	
	List<FileResource> getByFileName(String filename, Long access_channel);
	
	List<FileResource> getByDate(Date createdAt);
	
	//List<FileResource> getByCreationDate(String creationdate);

	FileResource findByNfilename(String nfilename);
	
	FileResource findByRef(Long ref,Long access_channel,Long userId);
	
	List<FileResource> findByAccessRule(Long id,Long access_channel );
	
	List<Long> getAccessRuleByResourceId(Long resourceId);
	
	List<ResourceCategoryModel> getResourceCategoryByResourceId(Long resourceId);
	
	List<ResourceAccessRule> getAccessRuleByResourceIds(List<Long> resourceIds);
	
	List<AccessRule> getAccessRulesByResourceIds(List<Long> resourceIds);
	
	List<String> getSpecialUserByResourceId(Long resourceId);

	List<Integer> getSpecialGroupByResourceId(Long resourceId);
	
	List<FileResource> findByAccessRuleAndLimit(Long accessRuleId, Integer num, String string);
	
	List<FileResource> findByUserIdAndSearch(List<Long> accessRuleId, Long resourceId, String title,Date startdate,Date enddate,
											 Integer ln, Long km, Long ks, Long wg, Integer page, Integer is_admin,Long user_id,
											 List<Long> subcategory_list,Long access_channel);

	FileResource addResource(FileResource resource);

	FileResource updateResourceByRef(FileResource resource);

	FileResource deleteResourceByRef(Long ref,Long access_channel);
	
	Integer checkAccessRule(List<Long> accessRuleId,Long resourceId,String staffNo, Integer is_admin,Long access_channel);
	
	Long getAccessRuleByResourceIdAndUser(Long userId,Long resourceId,Long access_rule);
	
	void createResourceAccessRule(Long resourceAccessRuleId, Long resourceId);
	
	void createResourceCategory(Long resourceCategoryId, Long resourceId);
	
	
	void createResourceSpecialUser(String staffNo, Long resourceId, Long userId);
	
	void createResoruceSpecialGroup(Integer specialGroupId, Long resourceId, Long userId);

	Integer checkAccessRuleByList(List<Long> accessRuleIdList, List<Long> resource_ids);

	void deleteByIds(List<Long> resource_ids,Long userId);
	
	void deleteResourceAccessRule(List<Long> resourceAccessRuleList_delete, Long resourceId, Long deletedBy, Date deleteAt);
	
	void deleteResourceResourceCategory(List<Long> resourceCategory_delete, Long resourceId, Long deletedBy, Date deleteAt);
	
	void deleteResourceSpecialUser(List<String> resourceSpecialUser_delete, Long resourceId, Long deletedBy, Date deleteAt);
	
	void updateMultipleResource(Long userId, List<Long> resource_ids, String accessChannel, Integer latestNews, Date latestNewsExpiry, Integer activated);

	void deleteResourceAccessRuleByIds(List<Long> toBeDeleted, Long userId);
	
	void deleteResourceCategoryByIds(List<Long> resource_ids, Long userId);

	void saveAllRAR(List<ResourceAccessRule> batchList);

	void deleteSpecialUserByIds(List<Long> resource_ids, Long userId);
	
	Long getMaxId();
	
	Long getTotal(List<Long> accessRuleId, Long resourceId, String title,Date startdate,Date enddate,
			 Integer ln, Long km, Long ks, Long wg, Integer page, Integer is_admin, Long user_id, List<Long> subcategory_list,Long access_channel);

	List<ResourceCategoryModel> findCategory();

	List<ResourceCategoryModel> getResourceCategoryByResourceIds(List<Long> resource_ids);

	List<String> getSpecialUserByResourceIds(List<Long> resource_ids);

	List<ResourceSpecialUser> getResourceSpecialUser(List<Long> resource_ids);
	
	List<ResourceSpecialUser> getAllSpecialUsers(List<Long> resource_ids);

	void saveAllThumbs(List<FileResource> toUpdate);
	
	void saveSingleThumbs(FileResource toUpdate);

	void createRCByBath(List<Long> resource_category_ids, Long resourceId);

	void saveAll(List<FileResource> r_to_update);

	void updateResourceAccessRule(List<Long> resourceAccessRule_to_update, Long resourceId);
	
	void updateResourceCategory(List<Long> resourceCategoryId, Long resourceId);
	
	void updateSpecialUser(List<String> special_user_to_update, Long resourceId);

	void createRARByBath(List<Long> resourceAccessRule_to_add, Long user_id);

	void createRSUByBath(List<String> special_user_to_add, Long resourceId);

	Long getTotalByAreaId(Long area_id, List<Long> accessRuleId, Integer page, Integer is_admin);

	List<Integer> getCategoryString(Long resourceId);

	List<FileResource> findByIdRange(Integer startNum, Integer endNum);

	Integer getCatByAreaId(Long categoryId, List<Long> accessRuleId, Integer page, String user_id, Integer is_admin, Long access_channel);

	List<FileResource> getLatest(List<Long> accessRuleId, Integer page, String user_id, Integer is_admin, Long access_channel);
	
	Integer getLatestTotal(List<Long> accessRuleId, Integer page, String user_id, Integer is_admin, Long access_channel);

	Integer getTotalByAreaId(Long areaId, List<Long> accessRuleId, Integer page, Integer is_admin,
			Long access_channel,String keyword, String staffNo);


	List<FileResource> getBySearchName(List<Long> categoryId, List<Long> accessRuleId, Integer page, Integer is_admin,
			Long access_channel, String keyword,String staffNo);

	Integer getTotalBySearchName(List<Long> categoryId, List<Long> accessRuleId, Integer page,
			Integer is_admin, Long access_channel, String keyword);

	void deleteResoufceSpecialGroup(List<Integer> resourceSpecialGroup_delete, Long resourceId, Long deletedBy,
			Date deletedAt);

	void deletedSpecialGroupByIds(List<Long> resource_ids, Long userId);

	List<ResourceSpeicalGroup> getAllSpecialGroups(List<Long> resoruce_ids);


	void createRSGByBath(List<Integer> resourceSpecialGroup_to_add, Long resource_id, Long userId);

	void updateSpecialGroup(List<Integer> resourceSpecialGroupId, Long resourceId);

	List<SpecialUserGroupModel> getResourceSpecialGroup(Long resource_ids);

	String getResourceNameById(Long resourceId);

	Integer getResourceAccessChannelById(Long resourceId);




	
	
}


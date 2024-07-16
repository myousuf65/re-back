package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.ResourceCategoryModel;

public interface ResourceCategoryRespository extends JpaRepository<ResourceCategoryModel, Integer> {

	

	@Query(value ="select rc.* from resource_category as rc Join resource as r"
			+ " on rc.resource_id = r.id"
			+ " where rc.category_id =?1 and r.deleted=0 and rc.deleted_by =0 ",nativeQuery = true)
	List<ResourceCategoryModel> findexistID(Long id);

	List<ResourceCategoryModel> findByResourceIdAndDeletedBy(Long resourceId, Long deletedBy);
	
	@Query("Select rc from ResourceCategoryModel rc where rc.categoryId in ?1")
	List<ResourceCategoryModel> findByCategoryList(List<Long> categoryList);
	
	@Query("Select rc from ResourceCategoryModel rc where rc.resourceId in ?1")
	List<ResourceCategoryModel> findByResourceList(List<Long> resourceList);
	

}

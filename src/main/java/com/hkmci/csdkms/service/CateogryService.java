package com.hkmci.csdkms.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.hkmci.csdkms.model.CatAllModel;
import com.hkmci.csdkms.model.CategoryModel;
import com.hkmci.csdkms.model.ResourceCategoryModel;

public interface CateogryService {
	
	
	
	public List<CategoryModel> findAll(HttpSession session);
	
	public List<CategoryModel> findAdminAll(HttpSession session);
	
	public void save(CatAllModel new_categorytheModel);
	
	public CategoryModel  searchParentId(Long parentId);
	
	public CatAllModel findById(Long id);
	
	public List<ResourceCategoryModel> hasResource(Long Id);
	
	public CatAllModel getById(Integer categoryId);

	public List<CategoryModel> findById2(Long id);
	
	public List<CategoryModel> findByName(String Cat );

	public void deleteCat(Long id);
	
	public List<Long> findSubCatId(Long categoryId);

	List<Long> findChildId(Long Id);
}


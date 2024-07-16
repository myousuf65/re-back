package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.CatAllModel;
import com.hkmci.csdkms.model.CategoryModel;
import com.hkmci.csdkms.model.ResourceCategoryModel;
import com.hkmci.csdkms.repository.CatAllRepository;
import com.hkmci.csdkms.repository.CategoryRepository;
import com.hkmci.csdkms.repository.ResourceCategoryRespository;

//import jdk.internal.org.jline.utils.Log;
@Service
public class CategoryServiceImp implements CateogryService {
  
	private CategoryRepository categoryRepository;
	private CatAllRepository catAllRepository;
	private ResourceCategoryRespository resourceCategoryRespository;
	@Autowired
	public CategoryServiceImp (CategoryRepository theCategoryRepository, CatAllRepository theCatAllRepository, ResourceCategoryRespository theResourceCategoryRespository) {
		categoryRepository = theCategoryRepository;
		catAllRepository = theCatAllRepository;
		resourceCategoryRespository = theResourceCategoryRespository;
	}

	public List<CategoryModel> findAll(HttpSession session) {
		List<CategoryModel> cat =  categoryRepository.findByIsDeleted(0);
		//System.out.println("See the category we get from database"+cat.size());
		
		List<CategoryModel> cat2 = cat.stream().filter( c -> c.getParentCatId()==0 && c.getshowInfo()==true && c.getIsDeleted()==false)
		
				.collect(Collectors.toList());
		//System.out.println(" Going to get Child size ="+ cat2.size());

		for(int i =0 ; i<cat2.size();i++) {
			Integer j = i;
		
			List<CategoryModel> cat3 = getChildren(cat2.get(j).getId(),cat);
			
			cat2.get(j).setChildren(cat3);
		}
		
		Object catSession = session.getAttribute("admin_category");
		if(catSession == null) {
			List<CategoryModel> admin_cat = cat.stream().filter( c -> c.getParentCatId()==0)
					.sorted(Comparator.comparing(CategoryModel::getNameEn)).collect(Collectors.toList());

			for(int i =0 ; i< admin_cat.size();i++) {
				Integer j = i;
			
				List<CategoryModel> admin_cat3 = getChildren(admin_cat.get(j).getId(),cat);
				
				admin_cat.get(j).setChildren(admin_cat3);
			}
			session.setAttribute("admin_category", admin_cat);
		}
		//System.out.println("See the update cat 2 "+cat2.size());
		return cat2;
		
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryModel> findAdminAll(HttpSession session) {
		// TODO Auto-generated method stub
		Object catSession = session.getAttribute("admin_category");
		//if(catSession == null) {
			List<CategoryModel> cat =  categoryRepository.findByIsDeleted(0);
			//System.out.println("See the category we get from database"+cat.size());
			
			List<CategoryModel> cat2 = cat.stream().filter( c -> c.getParentCatId()==0).collect(Collectors.toList());

			for(int i =0 ; i<cat2.size();i++) {
				Integer j = i;
			
				List<CategoryModel> cat3 = getAdminChildren(cat2.get(j).getId(),cat);
				
				cat2.get(j).setChildren(cat3);
			}
			session.setAttribute("admin_category", cat2);
			return cat2;
//		}else {
//			return (List<CategoryModel>) catSession;
//		}
		
		
	}
	
	
	

	public List<CategoryModel> getChildren(Long parentId,List<CategoryModel> all_category){ 
		//System.out.println("In get child");
		List<CategoryModel> entity_list = all_category.stream().filter( c -> c.getParentCatId().equals(parentId) 
				&& c.getshowInfo()==true  && c.getIsDeleted()==false)
				.sorted(Comparator.comparing(CategoryModel::getNameEn))
				.collect(Collectors.toList());
		
	
		List<CategoryModel> return_data = new ArrayList<CategoryModel>();
		
		if(entity_list.size() != 0){
			entity_list.sort(Comparator.comparing(CategoryModel::getNameEn));
			
			for(Integer i = 0; i < entity_list.size(); i++) {
				List<CategoryModel> children = getChildren(entity_list.get(i).getId(),all_category);
				entity_list.get(i).setChildren(children);
				
			}

			return entity_list;
		}else{
			
			return return_data;  
		}  
	}

	
	

	public List<CategoryModel> getAdminChildren(Long parentId,List<CategoryModel> all_category){ 
//		List<CategoryModel> entity_list = all_category.stream()
//										.filter( 
//											(n) -> n.getParentCatId() == parentId
//										).collect(Collectors.toList());
		List<CategoryModel> entity_list = all_category.stream().filter( c -> c.getParentCatId().equals(parentId) ).collect(Collectors.toList());
		//System.out.println("Node ID is: " + parentId);

		List<CategoryModel> return_data = new ArrayList<CategoryModel>();
		if(entity_list.size() != 0){
			for(Integer i = 0; i < entity_list.size(); i++) {
				List<CategoryModel> children = getAdminChildren(entity_list.get(i).getId(),all_category);
				entity_list.get(i).setChildren(children);
				//return_data.add(entity_list.get(i));
			}
			return entity_list;
		}else{
			return return_data;  
		}  
	}
	
	
	
	
	

	@Override
	public void save(CatAllModel new_categorytheModel) {
		// TODO Auto-generated method stub
		//catAllRepository.save(new_categorytheModel);
		catAllRepository.saveAndFlush(new_categorytheModel);
	}

	@Override
	public CategoryModel searchParentId(Long parentId) {
		// TODO Auto-generated method stub
//		List<CategoryModel> cat_in_db = categoryRepository.findAll();
//		System.out.println("Parent Id "+ parentId);
		List<CategoryModel> cat_list = categoryRepository.searchParentIdWithChildren(parentId);
//		System.out.println("Category List = "+cat_list.size());
		CategoryModel cat = cat_list.stream().filter(c -> c.getId().equals(parentId) && c.getshowInfo()==true && c.getIsDeleted()==false)
				.collect(Collectors.toList()).get(0);

		List<CategoryModel> children = cat_list.stream().filter(c -> !c.getId().equals(parentId))
				.sorted(Comparator.comparing(CategoryModel::getNameEn))
				.collect(Collectors.toList());
		
		
//		System.out.println("Children ==  "+cat.getId());
		if(children !=null) {
		cat.setChildren(children);
		}
		return cat;
	}

	@Override
	public CatAllModel getById(Integer categoryId) {
		// TODO Auto-generated method stub
		Optional<CatAllModel> return_data = catAllRepository.findById(categoryId);
		if(return_data.isPresent()) {
			return return_data.get();
		}else {
			return null;
		}
	}

	@Override
	public CatAllModel findById(Long Id) {
		// TODO Auto-generated method stub
		
		return catAllRepository.getById(Id);
	}

	@Override 
	public List<Long> findChildId(Long Id){
		
		return catAllRepository.findChildren(Id);
	}
	
	
	
	@Override
	public List<ResourceCategoryModel> hasResource(Long Id) {
		// TODO Auto-generated method stub
		return resourceCategoryRespository.findexistID(Id);
	}

	@Override
	public List<CategoryModel> findById2(Long id) {
		// TODO Auto-generated method stub
//		System.out.println(" id in before find "+id);
		return categoryRepository.seachById(id);
	}

	@Override
	public void deleteCat(Long id) {
		// TODO Auto-generated method stub
		categoryRepository.deleteCategory(id);
				
	}


	
	
	
	
	@Override
	public List<CategoryModel> findByName(String Cat) {
		// TODO Auto-generated method stub
		
		List<CategoryModel> all_category = categoryRepository.findAll();
		
		List<CategoryModel> cat = categoryRepository.getName(Cat);
		
		List<Long> path_arr_id = new ArrayList<Long>();
		
		for(Integer i = 0; i < cat.size(); i++) {
			String path_string = getParentName(cat.get(i).getId(),all_category);
			//System.out.println("Path string is: " + path_string);
			String[] path_arr = path_string.split("->");
			//System.out.println("Path Arr: " + path_arr.length);
			path_arr_id.add(cat.get(i).getId());
			for(Integer j = 0; j < path_arr.length; j++) {
				//System.out.println(path_arr[j]);
				path_arr_id.add(Long.parseLong(path_arr[j]));
			}
		}
		
//		System.out.println("See the category we get from database: " + path_arr_id.stream().distinct().collect(Collectors.toList()));

		 
//		for(int i =0 ; i<cat2.size();i++) {
//			Integer j = i;
//			
//			List<CategoryModel> cat3 = getChildren(cat2.get(j).getId(),cat);
//			
//			cat2.get(j).setChildren(cat3);
//			
//		}
		List<CategoryModel> search_category_for_loop = all_category.stream().filter(
					(c) -> path_arr_id.contains(c.getId())
				).collect(Collectors.toList());
		

		List<CategoryModel> search_category = all_category.stream().filter(
				(c) -> path_arr_id.contains(c.getId())
			).collect(Collectors.toList());
		return search_category.stream().filter(
					(c) -> c.getParentCatId().equals(0L)
				).map(
					(c) -> {
						List<CategoryModel> cat_sub = getAdminChildren(c.getId(),search_category_for_loop);
						c.setChildren(cat_sub);
						return c;
					}
				).collect(Collectors.toList());
		
	}
	
	public String getParentName(Long parentId,List<CategoryModel> all_category){ 
		List<CategoryModel> entity_list = all_category.stream()
										.filter( 
											(n) -> n.getId().equals(parentId)
										).collect(Collectors.toList());
		//System.out.println("Node ID is: " + parentId);
		String back_string = "";
		if(entity_list.size() != 0){
			CategoryModel entity = entity_list.get(0);
			if(entity.getParentCatId() != 0) {
				String configName = entity.getId()+"->";

				String returnConfigName = getParentName(Long.parseLong(String.valueOf(entity.getParentCatId())),all_category);
				back_string = returnConfigName+configName;
//				return returnConfigName+configName;
				return back_string;
			}else {
				String configName = entity.getId() + "->";
				return back_string+configName;
			}
		}else{
			return "";  
		}  
	}

	
	public List<Long> findSubCatId(Long categoryId){
		return categoryRepository.getSubCatId(categoryId);
		}


}

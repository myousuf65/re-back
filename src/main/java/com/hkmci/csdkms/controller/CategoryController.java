package com.hkmci.csdkms.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.model.CatAllModel;
import com.hkmci.csdkms.model.CategoryModel;
import com.hkmci.csdkms.model.ResourceCategoryModel;
import com.hkmci.csdkms.repository.CatAllRepository;
import com.hkmci.csdkms.service.CateogryService;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	@Resource
	private CateogryService categoryService;
	
	@Autowired
	@Resource
	private CatAllRepository catAllRepository;
	
	@RequestMapping("/all")
	public ResponseEntity<JsonResult> findAll(HttpSession session){
		 List<CategoryModel> return_data = categoryService.findAll(session);
		 ////System.out.println("Not data problem = " + return_data);
		 
		 return JsonResult.ok(return_data,session);
	}
	
	
	@RequestMapping("/adminall")
	public ResponseEntity<JsonResult> findAdminAll(HttpSession session){
		 List<CategoryModel> return_data = categoryService.findAdminAll(session);
		 //System.out.println(" Return Data = "+return_data.size());
		 return JsonResult.ok(return_data,session);
	}
	
	@RequestMapping("/add")
	public ResponseEntity<JsonResult> addCategory(@RequestBody JsonNode jsonNode,HttpSession session){
		CatAllModel new_category = new CatAllModel();
		new_category.setIsDeleted(false);
		new_category.setCreated(new Date());
		new_category.setCreatedBy(jsonNode.get("createdBy").asLong());
		new_category.setNameTc(jsonNode.get("nameTc").asText());
		//System.out.println("nameTc:  "+jsonNode.get("nameTc").asText());
		new_category.setNameEn(jsonNode.get("nameEn").asText());
		//System.out.println("nameEn:  "+jsonNode.get("nameEn").asText());
		new_category.setShowInfo(jsonNode.get("showInfo").asBoolean());
		//System.out.println("showInfo:  "+jsonNode.get("showInfo").asText());
		new_category.setParentCatId(jsonNode.get("parent").asLong());
		if(jsonNode.get("parent").asLong() ==1 ) {
			new_category.setLevel(1L);
		} else {
			new_category.setLevel(0L);
		}
//		categoryService.save(new_category);
		//System.out.println("Category = "+ new_category.getNameEn());
		//session.setAttribute("admin_category", value);
	   return JsonResult.ok(catAllRepository.saveAndFlush(new_category),session);
		
	
	}
	
	@RequestMapping("/get")
	public ResponseEntity<JsonResult> findAll(
			@RequestParam(value="id",required=false, defaultValue ="") Long Cat,HttpSession session){
		//System.out.println("Id we get from user "+Cat);
		
	 	
		CatAllModel return_type = categoryService.findById(Cat);
		List<Long> children_id = categoryService.findChildId(Cat);	
		return JsonResult.ok(return_type,children_id,session);
	}
	
	
	@RequestMapping("/update")
	public ResponseEntity<JsonResult> editCategory(@RequestBody JsonNode jsonNode,HttpSession session){
		
		//System.out.println("----------- In update ---------");
		CatAllModel new_category = categoryService.findById(jsonNode.get("id").asLong());
		//System.out.println("Id:  "+jsonNode.get("id").asInt());
 
		new_category.setId(jsonNode.get("id").asLong());
		new_category.setNameTc(jsonNode.get("nameTc").asText());
		new_category.setNameEn(jsonNode.get("nameEn").asText());
		new_category.setShowInfo(jsonNode.get("showInfo").asBoolean());
		if(new_category.getParentCatId() ==0 || new_category.getId() ==jsonNode.get("parent").asLong() ) {
			
		} else {
		new_category.setParentCatId(jsonNode.get("parent").asLong());
		}
		new_category.setModifiedAt(new Date());
		new_category.setIsDeleted(false);
		//System.out.println("----------- In update2 ---------");
		new_category.setModifiedBy(jsonNode.get("modifiedBy").asLong());
		 //System.out.println(" Name En "+ jsonNode.get("id").asInt() + " Name :"+jsonNode.get("nameEn").asText()+ " Name Tc: "+ jsonNode.get("nameTc").asText()+" Parent : " + jsonNode.get("parent").asLong());
		categoryService.save(new_category);
		//System.out.println(" SHOW ?  "+jsonNode.get("showInfo").asBoolean() );
		return JsonResult.ok("OK",session);
	}
// for testing 2
	
	@RequestMapping("/find")
	public ResponseEntity<JsonResult> findDetail(
			@RequestParam(value="name",required=false, defaultValue ="") String Cat,HttpSession session){
		//System.out.println(" Name "+ Cat);
		
		List<CategoryModel> return_type = categoryService.findByName(Cat);
		return JsonResult.ok(return_type,session);
	}
	
	@RequestMapping("/delete")
	public ResponseEntity<JsonResult> deleteCat(
			@RequestParam(value="categoryId", required=true, defaultValue = "") String Cat,
			@RequestParam(value="userId", required=true, defaultValue="") Long userId,HttpSession session
			){
		 	Long Id = Long.parseLong(Cat);
		 	//System.out.println(" User Id --"+userId);
		 	List<CategoryModel> return_type = categoryService.findById2(Id);
		 	//System.out.println("return -- "+return_type);
		 	
		 	List<ResourceCategoryModel> exist = categoryService.hasResource(Id);
		 	//System.out.println("exist "+exist);
		
		 	if(exist.size()==0 && return_type.size()==0 ) {
		 		//System.out.println("Delete -- "+ Id);
		 		CatAllModel new_category = categoryService.findById(Id);
		 		new_category.setDeleted_at(new Date());
		 		new_category.setDeletedBy(userId);
		 		new_category.setShowInfo(false);
		 		new_category.setIsDeleted(true);
		 		categoryService.save(new_category);
		 		//categoryService.deleteCat(Id);
		 		return JsonResult.ok("Deleted!!!",session);
		 				
		 		}
		 	else {
		 		if(return_type.size()==0) {
		 		return JsonResult.ok("Still have resource", session);
		 		} else if (exist.size()==0) {
		 			return JsonResult.ok("Still have sub category", session);
		 		}else 
		 			return JsonResult.ok("Have both resource and sub category", session);
		 		}
			}
	
	
	
	
	
	@RequestMapping("/search")
	public ResponseEntity<JsonResult> findSearch(
		@RequestParam(value="categoryId",required=false, defaultValue ="0") String Cat,
		@RequestParam(value="userId",required=false, defaultValue="0") String UserId,HttpSession session ){
		
		Long parentId = Long.parseLong(Cat);
		List<CatAllModel> all_category = catAllRepository.findAllByOrderNameEn();
		
		//System.out.println("List size is: " + all_category.size());
		String path_string = getParentName(parentId,all_category);
		
		//System.out.println("Path string is: " + path_string);
		String[] path_arr = path_string.split("->");
		//System.out.println("Path Arr: " + path_arr.length);
		
		List<Integer> path_arr_id = new ArrayList<Integer>();
		for(int i = 0; i < path_arr.length; i++) {
			//System.out.println(path_arr[i]);
			path_arr_id.add(Integer.parseInt(path_arr[i]));
		}
		
//		List<CatAllModel> return_data = all_category.stream()
//										.filter( 
//												(n) -> path_arr_id.contains(Integer.parseInt(n.getId().toString()))
//											)
//										.sorted(Comparator.comparing(CatAllModel::getParentCatId))
//										.collect(Collectors.toList());

//// Correct Category Order 		
		List<CatAllModel> return_data = path_arr_id.stream()
				.map(
					(cat) -> {
						CatAllModel temp = all_category.stream()
								.filter( 
							//		(n) -> cat.equals(Integer.parseInt(n.getId().toString())) && n.getshowInfo() == true
							(n) -> cat.equals(Integer.parseInt(n.getId().toString())) && n.getshowInfo() == true && n.getIsDeleted() == false
								)
								.collect(Collectors.toList()).get(0);
						return temp;
					}
					)
				//).sorted(Comparator.comparing(CatAllModel::getParentCatId))
				.collect(Collectors.toList());
		
		
//		
//		List<CatAllModel> return_data = path_arr_id.stream()
//				.map(
//					(cat) -> {
//						CatAllModel temp = all_category.stream()
//								.filter( 
//									(n) -> cat.equals(n.getId())
//								)
//								.collect(Collectors.toList()).get(0);
//						return temp;
//					}
//				)
//				.collect(Collectors.toList());

		return JsonResult.path("Data with Path Array",categoryService.searchParentId(parentId),return_data,true,session);
		
		
	}
	

	public String getParentName(Long parentId,List<CatAllModel> all_category){ 
		List<CatAllModel> entity_list = all_category.stream()
										.filter( 
											(n) -> n.getId().equals(parentId)
										)
										.collect(Collectors.toList());
		//System.out.println("Node ID is: " + parentId);
		String back_string = "";
		if(entity_list.size() != 0){
			CatAllModel entity = entity_list.get(0);
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
	
}

package com.hkmci.csdkms.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserAccessRule;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/access_rule")
public class AccessRuleController {
	
	@Autowired
	//private User user;
	@Resource
    private UserService userService;
	
	@Autowired
	@Resource 
	private AccessRuleService accessRuleService;
	
	@Autowired
	@Resource 
	private Common common;
	
	
	@RequestMapping("/all")
    private ResponseEntity<JsonResult> getList(HttpSession session) {
	
		
        return JsonResult.ok(accessRuleService.findAll(),session);
    }
	
	@RequestMapping("/findByID")
    private ResponseEntity<JsonResult> findByID(@RequestParam(value="id",required=false,defaultValue="") Long id,HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(accessRuleService.getById(id),session);
    }
	
	@RequestMapping("/test")
	private ResponseEntity<JsonResult> testById(@RequestParam(value="id",required=false,defaultValue="") Long id, HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(accessRuleService.test(id),session);
    }
	
	@RequestMapping("/customeized_test")
	private ResponseEntity<JsonResult> customeized_testById(@RequestParam(value="id",required=false,defaultValue="") Long id, HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(accessRuleService.customeized_test(id),session);
     
    }

	private String getInstId(JsonNode jsonNode){
		List<String> reutrn_inst = new ArrayList<String>();
		//System.out.println("Inst Size: " + jsonNode.get("inst").size());
		if(jsonNode.get("inst").size() > 1 && jsonNode.get("areaId").asInt() != 2) {
			return null;
		}
		for(Integer i = 0; i < jsonNode.get("inst").size(); i++) {
			reutrn_inst.add(jsonNode.get("inst").get(i).asText());
		}
		if(reutrn_inst.size() == 0 || reutrn_inst == null) {
//			reutrn_inst.add(jsonNode.get("inst").asText());
			reutrn_inst.add("0");
		}
		return String.join(",",reutrn_inst);
	}
	
	private String getRankId(JsonNode jsonNode){
		List<String> reutrn_rank = new ArrayList<String>();
		for(Integer i =0; i< jsonNode.get("rank").size();i++) {
			reutrn_rank.add(jsonNode.get("rank").get(i).asText());
		}
		
		return String.join(",",reutrn_rank);
	}
	
	private String getSectionId(JsonNode jsonNode){
		List<String> reutrn_section = new ArrayList<String>();
		for(Integer i =0; i< jsonNode.get("section").size();i++) {
			reutrn_section.add(jsonNode.get("section").get(i).asText());
		}
		
		return String.join(",",reutrn_section);
	}
   
	
	
	@RequestMapping("/delete")
	private ResponseEntity<JsonResult> deleteByRuleIds(@RequestBody JsonNode jsonNode, HttpSession session) throws Exception, Throwable{
		//System.out.println(" from front end  - "+jsonNode );

		List<Long> deleted_ids = getIds(jsonNode.get("id"));
		Long deleted_by = jsonNode.get("deletedBy").asLong();
		//CollectionAccessRule collectionAccessRule = new CollectionAccessRule();
		
		List<Long> access_rule_with_resource_ids = accessRuleService.getAllWithRsource(deleted_ids)
				.stream()
				.map(
						(car) -> {
							return car.getId();
						}
				).distinct().collect(Collectors.toList());
		//System.out.println("Access rules assigned to resource: " + access_rule_with_resource_ids);
		
//		List<Long> check_list = deleted_ids.stream().filter(
//					(l) -> access_rule_with_resource_ids.contains(l)
//				).collect(Collectors.toList());
		
		if(access_rule_with_resource_ids != null && access_rule_with_resource_ids.size() != 0) {
			return JsonResult.listTotal("Can't delete, due to these access rules assigned to resources",access_rule_with_resource_ids,access_rule_with_resource_ids.size(),session);
		}else {
			accessRuleService.deleteAR(deleted_ids,deleted_by);
			//System.out.println("delete successfully");
			return JsonResult.ok("delete successfully",session);
		}
		
	}
	
	@RequestMapping("/update")
	

	private ResponseEntity<JsonResult> updateAccessRule(@RequestBody JsonNode jsonNode, HttpSession session) throws Exception, Throwable{
		//System.out.println(" from front end  - "+jsonNode );
	
		Optional<AccessRule> getOldEntity = accessRuleService.getById(jsonNode.get("id").asLong());
		if(getOldEntity.isPresent()) {
			AccessRule collectionAccessRule = getOldEntity.get();
			
			collectionAccessRule.setAreaId(jsonNode.get("areaId").asLong());
			if(getInstId(jsonNode) == null) {
				return JsonResult.errorMsg("Invalid AccessRule Request");	
			}
			collectionAccessRule.setInstId(getInstId(jsonNode));
			collectionAccessRule.setDescription(jsonNode.get("description").asText());

			collectionAccessRule.setModifiedAt(new Date());
			collectionAccessRule.setRankId(getRankId(jsonNode));

			collectionAccessRule.setSectionId(getSectionId(jsonNode));
			collectionAccessRule.setModifiedBy(jsonNode.get("createdBy").asLong());
			//System.out.println("section Id "+getSectionId(jsonNode));
			collectionAccessRule.setId(jsonNode.get("id").asLong());
			
			accessRuleService.save(collectionAccessRule);
			//System.out.println("edit successfully");
			return JsonResult.ok(collectionAccessRule,session);
		}else {
			return JsonResult.errorMsg("Invalid AccessRule Info");		
		}
		
	}
	@RequestMapping("/add")
	private ResponseEntity<JsonResult> addAccessRule(@RequestBody JsonNode jsonNode, HttpSession session){
		
		//System.out.println(" Json Node - "+jsonNode);
		AccessRule collectionAccessRule = new AccessRule();
		collectionAccessRule.setAreaId(jsonNode.get("areaId").asLong());
		collectionAccessRule.setCreatedBy(jsonNode.get("createdBy").asLong());
		collectionAccessRule.setDescription(jsonNode.get("description").asText());
		collectionAccessRule.setCreatedAt(new Date());
		String instId = getInstId(jsonNode);
		if(instId == null) {
			return JsonResult.errorMsg("Invalid AccessRule Request");	
		}
//		if (jsonNode.get("areaId").asLong()==2) {
//			collectionAccessRule.setInstId(instId);
//			System.out.println("Inst Id:2 " + instId );
//		}else {
			collectionAccessRule.setInstId(getInstId(jsonNode));
			//System.out.println("Inst Id:2 " + instId );
//		}
		
		collectionAccessRule.setRankId(getRankId(jsonNode));
		//System.out.println("rank Id: "+getRankId(jsonNode) );
		
		
		collectionAccessRule.setSectionId(getSectionId(jsonNode));
		//System.out.println("section Id: "+getSectionId(jsonNode));
		
		collectionAccessRule.setDeletedBy(0L);
		
		AccessRule return_Resource = accessRuleService.save(collectionAccessRule);
		//System.out.println("Save successfully");
		return JsonResult.ok(return_Resource,session);
		
	}
	

	
	
	
	@RequestMapping("/search/{user_id}")
	private ResponseEntity<JsonResult> findsearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@RequestParam(value="areaId",required=false,defaultValue="0") Long areaId,
			 	@RequestParam(value="startdate",required=false,defaultValue="0") Long startdate_str,
			 	@RequestParam(value="enddate",required=false, defaultValue="0") Long enddate_str,
			 	@RequestParam(value="description",required=false, defaultValue="") String description,
			 	@RequestParam(value="km",required=false,defaultValue="-1") Long km,
			 	@RequestParam(value="ks",required=false,defaultValue="-1") Long ks,
			 	@RequestParam(value="wg",required=false,defaultValue="-1") Long wg,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
//			Long section_id = user.getSectionId().getId();
//			Long institution_id = user.getInstitutionId().getId();
//			Long rank_id = user.getRankId().getId();
			//Long accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
			//List<Long> accessRuleId = accessRuleService.getIdByUser(section_id, institution_id, rank_id);
			@SuppressWarnings("unchecked")
			List<Long> accessRuleId = (List<Long>) session.getAttribute("user_access_rule_session");
			if(accessRuleId == null) {
				return JsonResult.errorMsg("No valid user list");
			}else {
				//TODO Check Admin Role
				Integer is_admin = common.checkAdmin(user,session);
				//TODO Need to clearfy how to use access rule here
				//accessRuleId.clear();
				//System.out.println("accessRuleId from user Info" + accessRuleId);
				//accessRuleId.add(0L);
				//Now, the logic is, user_access_rule + collection_access_rule / admin
				List<UserAccessRule> user_access_rule = userService.getByUserId(user_id);
				if(user_access_rule == null) {
					accessRuleId.add(0L);
				}else {
					for(Integer i = 0; i < user_access_rule.size(); i++) {
						accessRuleId.add(user_access_rule.get(i).getId());
					}
				}
				
				SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
				Timestamp startdate = startdate_str != 0 ? new Timestamp(startdate_str) : null;
				Date startDate;
				try {
					startDate = ymdFormat.parse(ymdFormat.format(new Date(startdate.getTime())));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					startDate = null;
					//e.printStackTrace();
				}
				Timestamp enddate = enddate_str != 0 ? new Timestamp(enddate_str) : null;
				Date endDate;
				try {
					endDate = ymdFormat.parse(ymdFormat.format(new Date(enddate.getTime())));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					endDate = null;
					//e.printStackTrace();
				}
				km = km == -1L ? -1L : 1L;
				ks = ks == -1L ? -1L : 2L;
				wg = wg == -1L ? -1L : 3L;
		
				
				
				Date start =null;
				if (startdate != null) {
					Date end = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(startdate);
					c.add(Calendar.DATE, 0);
					end =c.getTime();
					try {
						start = ymdFormat.parse(ymdFormat.format(new Date(end.getTime())));
					} catch (Exception e) {
						start = null;
					}
				}
				
				Date finish =null;
				
				if (endDate != null) {
					Date end = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(enddate);
					c.add(Calendar.DATE, 1);
					end =c.getTime();
				
				
				
					try {
						finish = ymdFormat.parse(ymdFormat.format(new Date(end.getTime())));
					} catch (Exception e) {
						finish = null;
					}
				}
				
				
				
				
				List<AccessRule> result = accessRuleService.Search(Id,start,finish,km,ks,wg,page,accessRuleId,is_admin,description); 
				Integer result_length = accessRuleService.getTotal(Id,start,finish,km,ks,wg,page,accessRuleId,is_admin,description); 
//				System.out.println("here is the result  "+result.get(0));
				
				
				
				return JsonResult.listTotal("Access Rule List.",result,result_length,session);
			}
		}
	}
	
	@RequestMapping("/delete/{id}")
	private ResponseEntity<JsonResult> deleteById(@PathVariable Long id , HttpSession session){
			
			//System.out.println(" Id = " + id);
			
			return JsonResult.ok(accessRuleService.deleteById(id),session);
			
		}
	
	private List<Long> getIds(JsonNode jsonNode){
		List<Long> return_data = new ArrayList<Long>();
		for(Integer i =0; i < jsonNode.size(); i++) {
			return_data.add(jsonNode.get(i).asLong());
		}
		return return_data;
	}

}

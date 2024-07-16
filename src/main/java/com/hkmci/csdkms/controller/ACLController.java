package com.hkmci.csdkms.controller;

import java.util.Optional;

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
import com.hkmci.csdkms.model.ACLModel;
import com.hkmci.csdkms.service.ACLService;
import com.hkmci.csdkms.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/acl")
public class ACLController {
	
	@Autowired
	@Resource
	private ACLService aclService;
	
	@Autowired
	@Resource 
	private UserService userService;
	
	
	@RequestMapping("/all")
	public ResponseEntity<JsonResult> findByGroupId(@RequestParam (value="userGroupId", required=false, defaultValue="") Long groupId, HttpSession session){
		return JsonResult.ok(aclService.getByUser(groupId),session); 
	}
	
	@RequestMapping("/update")
	public ResponseEntity<JsonResult> addACL(@RequestBody JsonNode jsonNode, HttpSession session){
		
		ACLModel SaveList = new ACLModel();
		//System.out.println(" user group id "+ jsonNode);
		//System.out.println("id -  "+ jsonNode.get("id").asLong());
	//	System.out.println("Reuturn from database = "+ aclService.findById(jsonNode.get("id").asLong()));
		Optional<ACLModel> new_acl = aclService.findById(jsonNode.get("id").asLong());
		//System.out.println("New ACL "+ new_acl);
		SaveList.setId(jsonNode.get("id").asInt());
		SaveList.setFlagAdd(new_acl.get().getFlagAdd());
		SaveList.setFlagDel(new_acl.get().getFlagDel());
		SaveList.setFlagDownload(new_acl.get().getFlagDownload());
		SaveList.setFlagSearch(new_acl.get().getFlagSearch());
		SaveList.setFlagSv(new_acl.get().getFlagSv());
		SaveList.setFlagUpd(new_acl.get().getFlagUpd());
		SaveList.setFlagAll(new_acl.get().getFlagAll());
		SaveList.setUsergroupId(new_acl.get().getUsergroupId());
		SaveList.setSiteId(new_acl.get().getsiteId());
		
		if(jsonNode.get("column").asText().equals("flagAll")) {
			SaveList.setFlagAll(jsonNode.get("value").asBoolean());
			if(	Boolean.TRUE.equals(jsonNode.get("value").asBoolean())) {
				SaveList.setFlagAdd(false);
				SaveList.setFlagDel(false);
				SaveList.setFlagDownload(false);
				SaveList.setFlagSearch(false);
				SaveList.setFlagSv(false);
				SaveList.setFlagUpd(false);
			} 
		} else if (jsonNode.get("column").asText().equals("flagAdd")) {
			SaveList.setFlagAdd(jsonNode.get("value").asBoolean());
		} else if (jsonNode.get("column").asText().equals("flagDel")) {
			SaveList.setFlagDel(jsonNode.get("value").asBoolean());
		}else if (jsonNode.get("column").asText().equals("flagDownload")) {
			SaveList.setFlagDownload(jsonNode.get("value").asBoolean());
		}else if (jsonNode.get("column").asText().equals("flagSearch")) {
			SaveList.setFlagSearch(jsonNode.get("value").asBoolean());
		}else if (jsonNode.get("column").asText().equals("flagSv")) {
			SaveList.setFlagSv(jsonNode.get("value").asBoolean());
		}else if (jsonNode.get("column").asText().equals("flagUpd")) {
			SaveList.setFlagUpd(jsonNode.get("value").asBoolean());
		}
		
		//System.out.println("Save List - "+SaveList);
		
		
		
		return JsonResult.ok(aclService.save(SaveList),session);
		
	}
	
	
	@RequestMapping("/request")
	public ResponseEntity<JsonResult> findByID(@RequestParam(value="GroupId",required=false,defaultValue="") Long GroupId, HttpSession session) {
 
	//	System.out.print("user group id "+ aclService.accessFunction(GroupId));
		
        
        return JsonResult.ok(aclService.accessFunction(GroupId),session);
   }
	
	@RequestMapping("/sitebar")
	public ResponseEntity<JsonResult> findSiteBar(@RequestParam(value="id",required=false,defaultValue="") Long id, HttpSession session) {
        //System.out.println("We get id - "+ id);
		Long GroupId = userService.getUserGroup(id);
		System.out.print("user group id "+ GroupId);
		//List<SitefuncsModel> functions = aclService.siteBar(GroupId);
        return JsonResult.ok(aclService.siteBar(GroupId,Long.valueOf(id)),session);
   }
	
	
	
}

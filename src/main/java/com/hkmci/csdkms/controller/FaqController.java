package com.hkmci.csdkms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.FaqReturn;
import com.hkmci.csdkms.service.FAQService;

@CrossOrigin
@RestController
@RequestMapping("/faq")
public class FaqController {
	@Autowired
	@Resource
	private FAQService fAQService;
	
	@Autowired
	@Resource
	private Common common;
	
	
	
	@RequestMapping("/{userId}")
	public ResponseEntity<JsonResult> fagMainPage(@PathVariable Long userId, HttpSession session) throws IOException {
		List<FaqReturn> return_data = fAQService.RetrunFaq();
		
		return JsonResult.ok(fAQService.adminParentFaq(),session);
	}
	
	
	
	@RequestMapping("/admin/get/{userId}/{faqId}")
	public ResponseEntity<JsonResult> adminFaqDetail(@PathVariable Long userId, @PathVariable Long faqId, HttpSession session) throws IOException{
		
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!= "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			if(user.getUsergroup() ==5) {
				
				
				
				return JsonResult.ok(fAQService.getFaq(faqId),session);
			} else {
				return JsonResult.errorMsg("You don't have right to enter.");
			}
			
			
		}
		
	}
	@RequestMapping("/admin/category/update/{userId}")
	public ResponseEntity<JsonResult> adminFaqDelete(@PathVariable Long userId,  @RequestBody JsonNode jsonNode, HttpSession session) throws IOException{
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!= "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			if(user.getUsergroup() ==5) {
				FaqReturn save_record = new FaqReturn();
				save_record.setId(jsonNode.get("id").asLong());
				if (jsonNode.get("AnswerEn").isNull()) {
					save_record.setAnswerEn(null);
				}else {
					save_record.setAnswerEn(jsonNode.get("AnswerEn").asText());
				};
				
				if (jsonNode.get("AnswerTc").isNull()) {
					save_record.setAnswerTc(null);
				}else {
					save_record.setAnswerTc(jsonNode.get("AnswerTc").asText());
				};
				
				
				if (jsonNode.get("QuestionEn").isNull()) {
					save_record.setQuestionEn("null");
				}else {
					save_record.setQuestionEn(jsonNode.get("QuestionEn").asText());
				};
				if (jsonNode.get("QuestionTc").isNull()) {
					save_record.setQuestionTc(null);
				}else {
					save_record.setQuestionTc(jsonNode.get("QuestionTc").asText());
				};
				
				if (jsonNode.get("titleEn").isNull()) {
					save_record.setTitleEn(null);
				}else {
					save_record.setTitleEn(jsonNode.get("titleEn").asText());
				};
				
				
				if (jsonNode.get("titleTc").isNull()) {
					save_record.setTitleTc(null);
				}else {
					save_record.setTitleTc(jsonNode.get("titleTc").asText());
				};
				
				fAQService.adminUpdateFaq(save_record, userId);
				return JsonResult.ok("ok",session);
			} else {
				return JsonResult.errorMsg("You don't have right to enter.");
			}
			
			
		}
		
	}
	
	@RequestMapping("/admin/category/delete/{faqId}/{userId}")
	public ResponseEntity<JsonResult> adminFaqDelete(@PathVariable Long faqId, @PathVariable Long userId, HttpSession session) throws IOException{
		
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!= "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			if(user.getUsergroup() ==5) {
				
				fAQService.deleteFaq(faqId,userId);
				
				return JsonResult.ok("ok",session);
			} else {
				return JsonResult.errorMsg("You don't have right to enter.");
			}
			
			
		}
	}
	
	
	@RequestMapping("/admin/categoryall/{userId}")
	public ResponseEntity<JsonResult> adminFaq(@PathVariable Long userId, HttpSession session) throws IOException{
		HashMap<String, String> user_check= common.checkUser(userId,session);
		if (user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			Object user_session = session.getAttribute("user_session");
			
			
			User user = (User) user_session;
			if (user.getUsergroup()==5) {
				
			
			
			
			return JsonResult.ok(fAQService.adminParentFaq(), session);
			} else {
				return JsonResult.errorMsg("You didn't have right to access rule");
			}
 		}
	}
		
	
	
	@RequestMapping("/create/{userId}")
	public ResponseEntity<JsonResult> createFAQ(@PathVariable Long userId, @RequestBody JsonNode jsonNode, HttpSession session) throws IOException{
		FaqReturn save_record = new FaqReturn();
		if(Integer.valueOf(jsonNode.get("parentForumId").toString()) == 0) {
			
			save_record.setTitleEn(jsonNode.get("titleEn").asText());
			save_record.setTitleTc(jsonNode.get("titleTc").asText());
			save_record.setLevel(0);
		} else {
		save_record.setAnswerEn(jsonNode.get("AnswerEn").asText());
		save_record.setAnswerTc(jsonNode.get("AnswerTc").asText());
		save_record.setQuestionEn(jsonNode.get("QuestionEn").asText());
		save_record.setQuestionTc(jsonNode.get("QuestionTc").asText());
		save_record.setId(Long.valueOf(jsonNode.get("parentForumId").toString()));
		save_record.setLevel(1);
		}
		return JsonResult.ok(fAQService.CreateFaq(save_record, userId),session);
		
	}
	

}

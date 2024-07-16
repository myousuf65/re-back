package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.Faq;
import com.hkmci.csdkms.model.FaqReturn;
import com.hkmci.csdkms.repository.FAQRepository;

@Service
public class FAQServiceImpl implements FAQService {
	
	
	@Autowired
	@Resource
	private FAQRepository fAQRepository;
	
	
	
	
	
	
	
	
	
	@Override
	public FaqReturn adminUpdateFaq(FaqReturn update_data, Long userId) {
		if(update_data.getQuestionEn().equals("null")) {
		   Faq return_title = 	fAQRepository.findFaq(update_data.getId());
		   return_title.setChinese(update_data.getTitleTc());
		   return_title.setEnglish(update_data.getTitleEn());
		   return_title.setModifiedAt(new Date());
		   return_title.setModifiedBy(userId);
		   fAQRepository.saveAndFlush(return_title);
		} else { 
			Faq return_question = 	fAQRepository.findFaq(update_data.getId());
			Faq return_answer = 	fAQRepository.findFaqAnswer(update_data.getId());
			return_question.setChinese(update_data.getQuestionTc());
			return_question.setEnglish(update_data.getQuestionEn());
			return_question.setModifiedAt(new Date());
			return_question.setModifiedBy(userId);
			fAQRepository.saveAndFlush(return_question);
			return_answer.setChinese(update_data.getAnswerTc());
			return_answer.setEnglish(update_data.getAnswerEn());
			return_answer.setModifiedAt(new Date());
			return_answer.setModifiedBy(userId);
			fAQRepository.saveAndFlush(return_answer);
			
			
		}
		return update_data;
		
	}
	
	
	
	
	@Override
	public List<FaqReturn> adminParentFaq(){
		List<Faq> return_data = fAQRepository.FaqParent();
		
		List<Faq> parent_list = return_data.stream().filter( n -> n.getTitleLevel() == 0 && n.getIsDeleted() ==0).collect(Collectors.toList());
		List<FaqReturn> return_list = new ArrayList<>();
		for (Integer i = 0 ; i< parent_list.size() ; i++) {
			FaqReturn faq_Return = new FaqReturn();
			faq_Return.setId(parent_list.get(i).getId());
			faq_Return.setTitleEn(parent_list.get(i).getEnglish());
			faq_Return.setTitleTc(parent_list.get(i).getChinese());
			List<FaqReturn> child_list = adminGetChild(parent_list.get(i).getId(), return_data);
			faq_Return.setFaqQuestion(child_list);
			faq_Return.setLevel(0);
			return_list.add(faq_Return);
		}
		return return_list;
	}
	
	Integer x=0;
	
	@Override
	public List<FaqReturn> adminGetChild(Long parentId, List<Faq> all_data){
		
		List<Faq> child_List = all_data.stream().filter(n-> n.getTitleLevel() ==1 && n.getIsDeleted() == 0 && n.getParentId()== parentId && n.getQa()== 0).collect(Collectors.toList());
		List<FaqReturn> return_list = new ArrayList<>();
		for(Integer i = 0; i<child_List.size();i++) {
			x=i;
			FaqReturn faq_Return = new FaqReturn();
				faq_Return.setQuestionEn(child_List.get(i).getEnglish());
				faq_Return.setQuestionTc(child_List.get(i).getChinese());  
				faq_Return.setId(child_List.get(i).getId());
				List<Faq> answer_List =all_data.stream().filter(n-> n.getTitleLevel() ==1 && n.getIsDeleted() == 0 && n.getParentId()== child_List.get(x).getId() && n.getQa()==1).collect(Collectors.toList());
				faq_Return.setAnswerEn(answer_List.get(0).getEnglish());
				faq_Return.setAnswerTc(answer_List.get(0).getChinese());
					
				return_list.add(faq_Return);
			
		}
		
		return return_list;
	}
	
	
	
	@Override
	public void deleteFaq(Long faqId,Long userId) {
		List<Object[]> detail = fAQRepository.findFaqList(faqId);
		List <Long> return_id = new ArrayList<>();
		
		for(Integer i = 0 ; i < detail.size();i++) {
			Object[] data = (Object[]) detail.get(i);
			return_id.add(Long.valueOf(data[0].toString()));
		}
		System.out.println("faq service impl , line 79 : return Id = "+return_id);
		fAQRepository.deleteFaq(new Date(), userId, return_id);
		
		
	}
	
	
	@Override 
	public List<FaqReturn> getFaq(Long faqId) {
		
		List<Object[]> detail = fAQRepository.findFaqList(faqId);
		List<FaqReturn> return_list = new ArrayList<>();
		FaqReturn record = new FaqReturn();
		System.out.println("FAQ Service Impl, line 79 = detail.size "+ detail.size());
		for (Integer i =0 ; i< detail.size(); i++) {
		
			Object[] data = (Object[]) detail.get(i);
			if( Long.valueOf(data[0].toString()).equals(faqId)  && Integer.valueOf(data[6].toString()) ==0) {
				record = new FaqReturn();
				record.setId(faqId);
				record.setTitleEn(data[2].toString());
				record.setTitleTc(data[1].toString());
				record.setLevel(0);
//				return_list.add(record);
				break;
			} else if ( Integer.valueOf(data[6].toString()) ==1) {
				if(Integer.valueOf(data[3].toString()) == 0) {
					record.setQuestionEn(data[2].toString());
					record.setQuestionTc(data[1].toString());
				} else if(Integer.valueOf(data[3].toString()) == 1) {
					record.setAnswerEn(data[2].toString());
					record.setAnswerTc(data[1].toString());
				}
				record.setId(faqId);
				record.setLevel(1);
				
			}
		}
		return_list.add(record);
		return return_list;
		
	}
	
	
	
	@Override
	public String CreateFaq(FaqReturn createFaq,Long userId)
	{
		
		if (createFaq.getLevel() == 0) {
			Faq createtitle = new Faq();
			createtitle.setCreatedAt(new Date());
			createtitle.setCreatedBy(userId);
			createtitle.setTitleLevel(0);
			createtitle.setChinese(createFaq.getTitleTc());
			createtitle.setEnglish(createFaq.getTitleEn());
			createtitle.setIsDeleted(0);
			fAQRepository.saveAndFlush(createtitle);
		} else {
		
		
		Faq createQuestion = new Faq();
		Faq createAnswer = new Faq();
		
		createQuestion.setChinese(createFaq.getQuestionTc());
		createQuestion.setEnglish(createFaq.getQuestionEn());
		createQuestion.setCreatedAt(new Date());
		createQuestion.setIsDeleted(0);
		createQuestion.setParentId(createFaq.getId());
		createQuestion.setQa(0);
		createQuestion.setTitleLevel(1);
		createQuestion =fAQRepository.saveAndFlush(createQuestion);
		createAnswer.setChinese(createFaq.getAnswerTc());
		createAnswer.setEnglish(createFaq.getAnswerEn());
		createAnswer.setCreatedAt(new Date());
		createAnswer.setIsDeleted(0);
		createAnswer.setParentId(createQuestion.getId());
		createAnswer.setQa(1);
		createAnswer.setTitleLevel(1);
		fAQRepository.saveAndFlush(createAnswer);
		}
		return "ok";
	}
	@Override
	public List<FaqReturn> RetrunFaq(){
	
	List<Object[]> detail  = fAQRepository.FaqReturn();
	List<FaqReturn> return_list = new ArrayList<>();
	
		for(Integer i =0 ; i<detail.size() ; i++) {
			//Integer j = i +1 ;
			Object[] data = (Object[]) detail.get(i);
			FaqReturn return_data = new FaqReturn();
			return_data.setId(Long.valueOf(data[4].toString()));
			return_data.setQuestionTc(data[2].toString());
			return_data.setQuestionEn(data[3].toString());
			return_data.setAnswerTc(data[0].toString());
			return_data.setAnswerEn(data[1].toString());
			return_list.add(return_data);
		}
	return return_list;
	}
}

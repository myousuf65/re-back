package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hkmci.csdkms.controller.JsonResult;
import com.hkmci.csdkms.entity.Banner;
import com.hkmci.csdkms.entity.Pin;
import com.hkmci.csdkms.entity.PopOut;
import com.hkmci.csdkms.model.BannerModel;
import com.hkmci.csdkms.model.PopOutModel;
import com.hkmci.csdkms.repository.PopOutRepository;

@Service
public class PopOutServiceImpl implements PopOutService {
	
	@Autowired
	@Resource
	private PopOutRepository popOutRepository;
	
//	@Autowired
//	public PopOutServiceImpl(PopOutRepository thePopOutRepository) {
//		popOutRepository = thePopOutRepository;
//	}
//	
//	

	@Override
	public List<PopOutModel> getAllPopOut(){
		
		List<PopOut> return_data = popOutRepository.getPopOut();
		List<PopOutModel> return_list = new ArrayList<>();
		for(Integer i=0; i<return_data.size(); i++) {
			PopOutModel temp_data = new PopOutModel();
			
			
			if(return_data.get(i).getAccessRule() ==null || return_data.get(i).getAccessRule().equals("")) {
			
			temp_data.setAccessRule(return_data.get(i).getAccessRule());
			temp_data.setHypryLink(return_data.get(i).getHypryLink());
			temp_data.setId(return_data.get(i).getId());
			temp_data.setAccessChannel(return_data.get(i).getAccessChannel());
			temp_data.setIsDeleted(0);
			String new_path = return_data.get(i).getImageUrl();
			new_path = new_path.replace("pop_out", "resources");
			temp_data.setImageUrl(new_path);
			return_list.add(temp_data);
			} else {
		
		    List<Long> popOut_access_rule = Arrays.asList(return_data.get(i).getAccessRule().split(","))
							.stream()
							.map((l) -> {return Long.parseLong(l);})
							.collect(Collectors.toList());
			
			temp_data.setAccessRuleId(popOut_access_rule);}
			temp_data.setAccessRule(return_data.get(i).getAccessRule());
			temp_data.setHypryLink(return_data.get(i).getHypryLink());
			temp_data.setId(return_data.get(i).getId());
			temp_data.setAccessChannel(return_data.get(i).getAccessChannel());
			temp_data.setIsDeleted(0);
			String new_path = return_data.get(i).getImageUrl();
			new_path = new_path.replace("pop_out", "resources");
			temp_data.setImageUrl(new_path);
			return_list.add(temp_data);
		}
		return return_list;
	}

	
	
	@Override
	public PopOut getPopOut1(){
		List<PopOut> return_data = popOutRepository.getPopOut();
		
	//System.out.println("Banner List Size = "+ banner_list.size());
	
		

		
		PopOut return_list = new PopOut();
		Integer popOutSize = return_data.size();
		System.out.println("Pop Out service impl, line 115, size - "+popOutSize);
		if (popOutSize==0) {
			return return_list;
		} else {
		Random ran = new Random();
		Integer x = ran.nextInt(popOutSize);
		
		System.out.println("Pop Out ServiceImpl ,line 42 : random number = "+ x);
		return_list = return_data.get(x);
		String new_path = return_list.getImageUrl();
		new_path = new_path.replace("pop_out", "resources");
		return_list.setImageUrl(new_path);
		return return_list;
		}
	}
	
	
	@Override
	public PopOut getPopOut(List<Long> accessRuleId, Integer access_channel){
		
		
		
		System.out.println("PopOutServiceImpl, line 119 : access_channel "+ access_channel);
		 List<String> channel = new ArrayList<>() ;
		 if(String.valueOf(access_channel).equals("1")) {
	        	channel.add("0");
	        	channel.add("1");
	        	channel.add("2");
	        }else {
	        	channel.add("2"); 
	        	channel.add("4"); 
	        }
		List<PopOut> return_data = popOutRepository.getPopOutHome(channel);
		
	//System.out.println("Banner List Size = "+ banner_list.size());
		List<PopOut> return_PopOut = return_data.stream()
					  .filter(
							  (n)->{
								  System.out.println("Pop Out Service Impl, line 83 : "+n.getAccessRule() );
								  if(n.getAccessRule().equals("")) {
									  return false;

								  } else {
								  List<Long> popOut_access_rule = Arrays.asList(n.getAccessRule().split(","))
										  							.stream()
										  							.map((l) -> {return Long.parseLong(l);})
										  							.collect(Collectors.toList());
								  List<Long> return_result = accessRuleId.stream()
										  					 .filter(ar -> popOut_access_rule.contains(ar))
										  					 .collect(Collectors.toList());
								  if(return_result != null && return_result.size() > 0) {
										  return true;
									  }else {
										  return false;
									  }
								  }
							  }
					  )
					  .map(
							  (n) -> {
								  PopOut return_temp = new PopOut();
								  return_temp.setId(n.getId());
								  return_temp.setImageUrl(n.getImageUrl());
								  return_temp.setHypryLink(n.getHypryLink());
								 
									  return_temp.setAccessRule(n.getAccessRule());
								 
								  return return_temp;
							  }
					  )
					  .collect(Collectors.toList());
		
		
		

		
		PopOut return_list = new PopOut();
		Integer popOutSize = return_PopOut.size();
		System.out.println("Pop Out service impl, line 115, size - "+popOutSize);
		if (popOutSize==0) {
			return return_list;
		} else {
		Random ran = new Random();
		Integer x = ran.nextInt(popOutSize);
		
		System.out.println("Pop Out ServiceImpl ,line 42 : random number = "+ x);
		return_list = return_PopOut.get(x);
		String new_path = return_list.getImageUrl();
		new_path = new_path.replace("pop_out", "resources");
		return_list.setImageUrl(new_path);
		return return_list;
		}
	}
	
	
	
	@Override 
	public PopOut savePopOut(PopOut new_PopOut,HttpSession session) {
		System.out.println("Pop Out Service Impl, line 74:"+new_PopOut);
		return popOutRepository.saveAndFlush(new_PopOut);
	}
	
	@Override
	public Optional<PopOut> findById(Long Id) {
		// TODO Auto-generated method stub
		return popOutRepository.findById(Id);
	}
	
	
	@Override
	public void deletePopOut(Long popOutId, Long userId) {
		popOutRepository.deletePopOut( popOutId);
		
	}

}

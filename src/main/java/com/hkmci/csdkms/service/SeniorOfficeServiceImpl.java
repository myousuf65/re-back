package com.hkmci.csdkms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.repository.UserRepository;

@Service
public class SeniorOfficeServiceImpl implements SeniorOfficeService {
	
	
	@Autowired 
	private UserRepository userRepository;
	
	
	
	public List<Object[]> getSeniorOfficerList(Integer i){
		
		
		List<Object[]> return_list = userRepository.getSeniorOfficeList(i);
		
//		System.out.println("Return List = "+ return_list.size());
		
		
		
		
		
		return return_list;	
		
	}
}

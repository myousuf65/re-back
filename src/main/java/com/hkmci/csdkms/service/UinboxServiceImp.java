package com.hkmci.csdkms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.Uinbox;
import com.hkmci.csdkms.repository.UinboxRepository;

@Service
public class UinboxServiceImp implements UinboxService {

	@Autowired
	private UinboxRepository uinboxRepository;
	
	
	
	@Override
	public Uinbox save(Uinbox new_record) {
		// TODO Auto-generated method stub
		return uinboxRepository.saveAndFlush(new_record);
		
	}

	@Override 
	public List<Uinbox> saveAll(List<Uinbox> save_all){
		
		
		return uinboxRepository.saveAll(save_all);
		
		
	}


	@Override
	public Integer numberMail(Long userId) {
		// TODO Auto-generated method stub
		return uinboxRepository.countmail(userId);
	}
	
	
	@Override 
	public Uinbox getInboxInfo(Long id ) {
		return uinboxRepository.getInbox(id);
	}


	@Override
	public List<Object []> getMail(Long userId) {
		// TODO Auto-generated method stub
		return uinboxRepository.getMail(userId);
	}

}

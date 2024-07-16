package com.hkmci.csdkms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.SitefuncsModel;
import com.hkmci.csdkms.repository.SitefuncRepository;


@Service
public class SitefuncServiceImp implements SitefuncService {
	
	@Autowired
	private SitefuncRepository sitefuncRepository;
	
	@Override
	public List<SitefuncsModel> getById(Long Id) {
		// TODO Auto-generated method stub
		return sitefuncRepository.findAllById(Id);
	}

}

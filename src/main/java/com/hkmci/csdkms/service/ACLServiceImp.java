package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.ACLModel;
import com.hkmci.csdkms.model.SitefuncsModel;
import com.hkmci.csdkms.repository.ACLRepository;
import com.hkmci.csdkms.repository.SitefuncRepository;


@Service
public class ACLServiceImp implements ACLService {
	@Autowired
	private ACLRepository aclRepository;
	
	
	@Autowired
	private SitefuncRepository sitefuncRepository;

	@Override
	public List<ACLModel> getByUser(Long usergroupId) {
		// TODO Auto-generated method stub
		return aclRepository.findByusergroupId(usergroupId);
	}

	
	@Override
	public Optional<ACLModel> findById(Long Id){
		int id =Id.intValue();
//		System.out.println("Id is "+id);
		return aclRepository.findById(id);
	}
	
	
	
	@Override
	public ACLModel save(ACLModel new_acl) {
		// TODO Auto-generated method stub
		return aclRepository.save(new_acl);
		
	}

//
//	@Override
//	public List<Long> accessFunction(Long groupId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
	@Override
	public List<Long> accessFunction(Long groupId) {

		return (aclRepository.checkAccess(groupId));
	}

	
	
	@Override
	public List<SitefuncsModel> siteBar(Long groupId ,Long userId){
		//System.out.println(" From  data - "+ sitefuncRepository.siteBar(userId).stream().map(SitefuncsModel::getId).collect(Collectors.toList()));
		
		List<SitefuncsModel> return_data = new ArrayList<>();
		if( 397 ==userId  || 4715 ==userId || 562 ==userId || 6865== userId ) {
			return_data =sitefuncRepository.siteMenuAll();
		}else {
		
		 return_data =sitefuncRepository.siteMenu(groupId);
		
		}
		
		
//		System.out.println("Return from SQL - "+return_data);
		return return_data;
	}

}

package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.ACLModel;
import com.hkmci.csdkms.model.SitefuncsModel;

@Service
public interface ACLService {
	
	List<ACLModel> getByUser(Long usergroupId);
	ACLModel save(ACLModel new_acl);
	Optional<ACLModel> findById(Long Id);
	List<Long> accessFunction(Long groupId);
	List<SitefuncsModel> siteBar(Long groupId,Long userId);
}

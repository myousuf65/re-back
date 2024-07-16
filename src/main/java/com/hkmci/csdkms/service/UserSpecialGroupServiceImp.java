package com.hkmci.csdkms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.SpecialUserGroupModel;
import com.hkmci.csdkms.model.SpecialUserGroupUserModel;
import com.hkmci.csdkms.repository.UserSpecialGroupRepository;
@Service
public class UserSpecialGroupServiceImp implements UserSpecialGroupService {

	@Autowired
	private UserSpecialGroupRepository userSpecialGroupRepository;
	
	
	@Override
	public List<SpecialUserGroupUserModel> save(List<SpecialUserGroupUserModel> specialUser) {
		// TODO Auto-generated method stub
		return userSpecialGroupRepository.saveAll(specialUser);
	}
	
	@Override
	public void deleteSpeicalUser (List<String> speicalUser_delete, Long groupId,Long deleteBy, Date deletedAt) {
		userSpecialGroupRepository.deleteSpecialUser(groupId, speicalUser_delete, deleteBy, deletedAt);
	}
	
	
	@Override 
	public List<String> getStaffNo(Long groupId){
		
		return userSpecialGroupRepository.findStaffNo(groupId);
	}
}

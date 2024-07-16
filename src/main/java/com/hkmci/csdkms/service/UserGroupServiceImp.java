package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.UserGroup;
import com.hkmci.csdkms.repository.UserGroupRepository;

@Service
public class UserGroupServiceImp implements UserGroupService {
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@Override
	public List<UserGroup> findAll() {
		// TODO Auto-generated method stub
		return userGroupRepository.getAll();
	}

	@Override
	public Optional<UserGroup> findById(Long userGroup) {
		// TODO Auto-generated method stub
		return userGroupRepository.findById(userGroup);
	}

	
}

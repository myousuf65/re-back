package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.UserGroup;

public interface UserGroupService {

		List<UserGroup> findAll();
		
		Optional<UserGroup> findById( Long userGroup);
}

package com.hkmci.csdkms.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.SpecialUserGroupUserModel;


@Service
public interface UserSpecialGroupService {


	List<SpecialUserGroupUserModel> save(List<SpecialUserGroupUserModel> specialUser);

	List<String> getStaffNo(Long groupId);

	void deleteSpeicalUser(List<String> speicalUser_delete, Long groupId, Long deleteBy, Date deletedAt);
			
}

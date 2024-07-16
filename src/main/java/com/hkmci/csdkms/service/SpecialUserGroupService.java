package com.hkmci.csdkms.service;

import java.util.List;

import com.hkmci.csdkms.entity.SpecialGroupUser;
import com.hkmci.csdkms.model.SpecialUserGroupModel;

public interface SpecialUserGroupService {
	
	public SpecialUserGroupModel save(SpecialUserGroupModel specailUserGroupModel);
	public SpecialUserGroupModel findById(Long groupId);
	
	
	
	public List<SpecialUserGroupModel> getSameInstAndSession(Long instId, Long sectionId);
	public List<SpecialUserGroupModel> getAll();
	public List<SpecialGroupUser> search(Long userId, Integer page, String groupName, String staffNo,Long groupId);
}

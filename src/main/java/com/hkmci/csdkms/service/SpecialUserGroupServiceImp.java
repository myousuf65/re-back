package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.SpecialGroupUser;
import com.hkmci.csdkms.model.SpecialUserGroupModel;
import com.hkmci.csdkms.repository.SpecialUserGroupRepository;
import com.hkmci.csdkms.repository.UserSpecialGroupRepository;

@Service
public class SpecialUserGroupServiceImp implements SpecialUserGroupService {
	@Autowired
	private SpecialUserGroupRepository specialUserGroupRepository;
	
	@Autowired
	private UserSpecialGroupRepository userSpecialGroupRepository;
	
	
	public SpecialUserGroupModel save(SpecialUserGroupModel specailUserGroupModel) {
		return specialUserGroupRepository.saveAndFlush(specailUserGroupModel);
	}
	
	
	public SpecialUserGroupModel findById(Long groupId) {
		SpecialUserGroupModel return_data = new SpecialUserGroupModel();
//		System.out.println("Group Id  = "+groupId);
				return_data = specialUserGroupRepository.findById(groupId).get();
		return return_data;
	}


	@Override
	public List<SpecialGroupUser> search(Long userId, Integer page, String groupName, String staffNo,Long groupId) {
		// TODO Auto-generated method stub
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 50;
		Integer end_num = 50;
		
		List<SpecialGroupUser> return_data = new ArrayList<>();
		
		
//		System.out.println("Group Name = "+groupName+"  " +staffNo+"  "+ start_num+"  "+ end_num);
		
		List<SpecialUserGroupModel> data_list = specialUserGroupRepository.search(groupName, staffNo, start_num, end_num,groupId);
//		System.out.println("Data List = "+ data_list.size());
		for(Integer i = 0 ; i < data_list.size() ; i ++) {
			SpecialGroupUser temp_group = new SpecialGroupUser();
			temp_group.setCreatedAt(data_list.get(i).getCreatedAt());
			temp_group.setModifiedAt(data_list.get(i).getModifiedAt());
			temp_group.setGroupName(data_list.get(i).getGroupName());
			temp_group.setId(data_list.get(i).getId());
			
			temp_group.setUserList(userSpecialGroupRepository.findStaffNo(data_list.get(i).getId()));
			return_data.add(temp_group);
		}
		
//		System.out.println("Return Data Size = "+ return_data.size());
		return return_data;
	}


	@Override
	public List<SpecialUserGroupModel> getAll() {
		// TODO Auto-generated method stub
		return specialUserGroupRepository.getAll();
	}


	@Override
	public List<SpecialUserGroupModel> getSameInstAndSession(Long instId, Long sectionId) {
		// TODO Auto-generated method stub
		return specialUserGroupRepository.getSameInstAndSession(instId, sectionId);
	}
	
}

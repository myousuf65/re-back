package com.hkmci.csdkms.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserProfile;
import com.hkmci.csdkms.repository.UserProfileRepository;
import com.hkmci.csdkms.repository.UserRepository;

@Service
public class UserProfileServiceImpl implements UserProfileService {
		
	@Autowired 
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	@Override
	public UserProfile checkExist(Long id, Integer type) {
		UserProfile exist_user = userProfileRepository.findExistProfile(id, type);
		return exist_user; 
	}
	
	
	@Override
	public User ApproveIcon(UserProfile exist_user) {
		
		
		
//		userProfileRepository.saveAndFlush(exist_user);
		
		
		
		
//		Long user_id = exist_user.getCreatedBy();
		
		Optional<User> user = userRepository.findById(exist_user.getId());
//		if (exist_user.getType() == 1) {
//			user.get().setProfilePhoto(exist_user.getPicture());
//		} else {
//			user.get().setProfilePhoto(exist_user.getPicture());
//		}
		return userRepository.saveAndFlush(user.get());
		
		
		
	}
	
	
	
	
	
	
	@Override
	public void userCreateIcon(Long userId, String filePath, Integer type,String old_path) {
//		System.out.println("User Profile Service Impl : userId = "+ userId + " filePath = "+ filePath + " type = "+ type );
//		UserProfile exist_user = null;
//		exist_user=	userProfileRepository.findExistProfile(userId, type);
//		UserProfile new_user = new UserProfile();
//		new_user.setApproved(0);
//		new_user.setCreatedAt(new Date());
//		new_user.setCreatedBy(userId);
//		new_user.setPicture(filePath);
//		new_user.setIsDeleted(0);
//		new_user.setType(type);
//		
//		System.out.println("User Profile Service Impl = "+ old_path.equals(filePath));
//		if(exist_user  != null &&  old_path.equals(filePath)) {
//		
//		} else if (exist_user  != null &&  !old_path.equals(filePath) ) {
//			exist_user.setIsDeleted(1);
//			userProfileRepository.saveAndFlush(exist_user);
//			userProfileRepository.saveAndFlush(new_user);
//		} else {
//		 userProfileRepository.saveAndFlush(new_user);
//		}
	}
}

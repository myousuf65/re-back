package com.hkmci.csdkms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.UserDog;
import com.hkmci.csdkms.repository.UserDogRepository;
@Service
public class UserDogServiceImpl implements UserDogService {

	@Autowired
	private UserDogRepository userDogRepositroy;
	
	@Override
	public String getUserDog (Long userID) {
		
		return userDogRepositroy.FindUserDog(userID);
	}
	@Override
	public UserDog getDog(Long userId) {
		return userDogRepositroy.findByUserId(userId);
	}
	
	@Override
	public UserDog saveLevelDog(UserDog dog) {
		return userDogRepositroy.saveAndFlush(dog);
	}
}

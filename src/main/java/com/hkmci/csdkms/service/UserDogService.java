package com.hkmci.csdkms.service;

import com.hkmci.csdkms.entity.UserDog;

public interface UserDogService {

//	String getUserDog(Long UserId);

	String getUserDog(Long userId);

	UserDog saveLevelDog(UserDog dog);

	UserDog getDog(Long userId);
}

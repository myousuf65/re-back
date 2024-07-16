package com.hkmci.csdkms.service;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserProfile;

public interface UserProfileService {

	void userCreateIcon(Long userId, String filePath, Integer type, String old_path);

	UserProfile checkExist(Long id, Integer type);

	User ApproveIcon(UserProfile exist_user);

}

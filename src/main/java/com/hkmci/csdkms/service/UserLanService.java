package com.hkmci.csdkms.service;

import java.util.List;

import com.hkmci.csdkms.model.UserLanModel;

public interface UserLanService {
	public List<UserLanModel> findAll();
	public UserLanModel findByUserId(int UserId);
	public void save (UserLanModel TheModel);
	
}

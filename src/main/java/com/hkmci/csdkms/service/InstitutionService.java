package com.hkmci.csdkms.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.InstitutionsModel;

public interface InstitutionService {
	
	List<InstitutionsModel> getAll();

	List<InstitutionsModel> getAllByUser(User user, HttpSession session);

}

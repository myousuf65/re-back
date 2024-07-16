package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.UserLanModel;
import com.hkmci.csdkms.repository.UserLanRepository;


@Service
public class UserLanServiceImp implements UserLanService {

	private UserLanRepository userLanRepository;
	
	@Autowired
	public UserLanServiceImp(UserLanRepository theUserLanRepository) {
		userLanRepository = theUserLanRepository;
	}
	
	
	
	@Override
	public List<UserLanModel> findAll() {
		// TODO Auto-generated method stub
		return userLanRepository.findAll();
	}

	public UserLanModel findByUserId(int UserId) {
		Optional<UserLanModel> result= userLanRepository.findById(UserId);
		UserLanModel theUserLanModel = null;
		if(result.isPresent()) {
			theUserLanModel = result.get();
		}else {
			throw new RuntimeException("Did not find user id :"+UserId);
		}
		
		return theUserLanModel;
}

	public void save(UserLanModel TheModel) {
		// TODO Auto-generated method stub
		userLanRepository.save(TheModel);
	}

}

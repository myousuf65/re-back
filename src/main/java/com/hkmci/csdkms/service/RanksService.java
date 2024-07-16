package com.hkmci.csdkms.service;

import java.util.List;

import com.hkmci.csdkms.model.RanksModel;

public interface RanksService {

	List<RanksModel> getAll();
	
	RanksModel getById(Long id);

}

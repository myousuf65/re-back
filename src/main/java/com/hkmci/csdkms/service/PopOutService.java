package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.hkmci.csdkms.entity.PopOut;
import com.hkmci.csdkms.model.PopOutModel;

public interface PopOutService {

	PopOut getPopOut(List<Long> accessRuleId,Integer channel);

	PopOut savePopOut(PopOut new_PopOut,HttpSession session);

	void deletePopOut(Long popOutId, Long userId);

	Optional<PopOut> findById(Long Id);

	List<PopOutModel> getAllPopOut();

	PopOut getPopOut1();

	
	
	
}

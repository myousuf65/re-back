package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.hkmci.csdkms.entity.Pin;
import com.hkmci.csdkms.model.PinReturnModel;

public interface PinService {

	PinReturnModel save(PinReturnModel thePin,HttpSession session );

	List<PinReturnModel> show(HttpSession session);

	List<Pin> displayPin(Long userId);

	Optional<Pin> findById(Long Id);

	Pin savePin(Pin TheModel);

	String updateStaffNo(Long pinId, String staffNo,Long userId, HttpSession session);

	void deletepin(Long pinId, Long userId);

}

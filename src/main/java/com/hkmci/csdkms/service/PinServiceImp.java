package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.Banner;
import com.hkmci.csdkms.entity.Pin;
import com.hkmci.csdkms.entity.PinUser;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.PinReturnModel;
import com.hkmci.csdkms.repository.PinRepository;
import com.hkmci.csdkms.repository.PinUserRepository;

@Service
public class PinServiceImp implements PinService {
	
	private PinRepository pinRepository;
	
	private PinUserRepository pinUserRepository;
	
	@Autowired
	public PinServiceImp(PinRepository thePinRepository,PinUserRepository thePinUserRepository) {
		pinRepository = thePinRepository;
		pinUserRepository = thePinUserRepository;
	}
	
	
	
	Integer x =0;
	
	@Override
	public List<PinReturnModel> show(HttpSession session) {
		List<User> user_list_session = (List<User>) session.getAttribute("user_list");
		
		List<Pin> return_pin = pinRepository.getAllPin();
		List<PinReturnModel> return_returnModel = new ArrayList<>();
		
		for(Integer i = 0 ; i<return_pin.size() ; i++) {
			PinReturnModel temp_pin = new PinReturnModel();
			temp_pin.setId(return_pin.get(i).getId());
			temp_pin.setCreatedAt(return_pin.get(i).getCreatedAt());
			temp_pin.setCreatedBy(return_pin.get(i).getCreatedBy());
			temp_pin.setImageUrl(return_pin.get(i).getImageUrl());
			temp_pin.setIsDeleted(return_pin.get(i).getIsDeleted());
			temp_pin.setDescription(return_pin.get(i).getDescription());
			temp_pin.setName(return_pin.get(i).getName());
			temp_pin.setNameTc(return_pin.get(i).getNameTc());
			
			
			if (pinUserRepository.getnUserId(temp_pin.getId()).size() ==0) {
				temp_pin.setStaffNo("");
			} else {
			List<Long> user_id = pinUserRepository.getnUserId(temp_pin.getId());
			List<String> temp_staff_no = new ArrayList<>();
			for( x =0 ;x<user_id.size(); x++) {
				String staff_no = user_list_session.stream().filter(
						(u) -> u.getId().equals(user_id.get(x))
						).map((u) -> {return u.getStaffNo();}).collect(Collectors.toList()).get(0);
				temp_staff_no.add(staff_no);
			}
			
			String staff =temp_staff_no.stream()
				      .map(n -> String.valueOf(n))
				      .collect(Collectors.joining(",", "", ""));
			temp_pin.setStaffNo(staff);
			}
			return_returnModel.add(temp_pin);
		}
		
		return return_returnModel;
		
	}
	
	@Override
	public List<Pin> displayPin(Long userId){
		List<Pin> return_pin = pinRepository.getDisplayPin(userId);
		return return_pin;
		
	}
	
	@Override
	public Optional<Pin> findById(Long Id) {
		// TODO Auto-generated method stub
		return pinRepository.findById(Id);
	}

	@Override
	public Pin savePin(Pin TheModel) {
		// TODO Auto-generated method stub
		return pinRepository.save(TheModel);
		
	}
	
	
	List<String> staff_no = new ArrayList<>();
	
	@Override 
	public String updateStaffNo(Long pinId,String staffNo,Long userId,HttpSession session) {
			
		
		staff_no = Arrays.asList(staffNo.split(","));
		List<User> user_list_session = (List<User>) session.getAttribute("user_list");
		
		List<Long> exist_user_id = pinUserRepository.getnUserId(pinId);
		List<String> exist_staff_no = new ArrayList<>();
		for(Integer i=0; i < exist_user_id.size(); i++) {
			Long temp_userId= exist_user_id.get(i);
			String sn = user_list_session.stream().filter(
					(u) -> u.getId().equals(temp_userId)
				).map((u) -> {return u.getStaffNo();}).collect(Collectors.toList()).get(0);
			exist_staff_no.add(sn);
		}
		
		List<String> new_user = staff_no.stream()
			    .filter(aObject -> {
			        return ! exist_staff_no.contains(aObject);
			      })
			    .collect(Collectors.toList());
		
		List<String> delete_user = exist_staff_no.stream()
			    .filter(aObject -> {
			        return ! staff_no.contains(aObject);
			      })
			    .collect(Collectors.toList());
		
		
		List<Long> db_deleted_user_id = pinUserRepository.getDeletedUserId(pinId);
		List<Long> delete__user_id = new ArrayList<>();
		
		for(Integer x = 0 ; x<new_user.size();x++) {
			String add_staff = new_user.get(x);
			Long user_id = user_list_session.stream().filter(
					(u) -> u.getStaffNo().equals(add_staff)
				).map((u) -> {return u.getId();}).collect(Collectors.toList()).get(0);
			if(db_deleted_user_id.contains(user_id)) {
				pinUserRepository.updatedeleteUser(new Date(), userId, pinId, user_id);
			} else {		
			
				PinUser pin_user = new PinUser();
				pin_user.setCreatedAt(new Date());
				pin_user.setCreatedBy(userId);
				pin_user.setIsDeleted(0);
				pin_user.setPinId(pinId);
			
				pin_user.setUserId(user_id);
				pinUserRepository.saveAndFlush(pin_user);
			}
		}
		if(delete_user.size() ==0)
		{
			
		} else {
		for(Integer y =0 ; y<delete_user.size();y++) {
			String delete_staff = delete_user.get(y);
			Long user_id = user_list_session.stream().filter(
					(u) -> u.getStaffNo().equals(delete_staff)
				).map((u) -> {return u.getId();}).collect(Collectors.toList()).get(0);
			delete__user_id.add(user_id);
		}
		Date deleted_date = new Date();
		pinUserRepository.deleteUser(deleted_date,userId,pinId,delete__user_id);
		System.out.println("Pin Service Imp, line 125 : new add staff = "+new_user);
		System.out.println("Pin Service Imp, line 126 : delete staff = "+ delete_user);
		}
		return null;
	}
	
	@Override
	public PinReturnModel save(PinReturnModel thePin,HttpSession session ) {
		
		List<User> user_list_session = (List<User>) session.getAttribute("user_list");
		
		Pin save_pin = new Pin();
		
		save_pin.setCreatedAt(new Date());
		save_pin.setCreatedBy(thePin.getCreatedBy());
		save_pin.setDescription(thePin.getDescription());
		save_pin.setImageUrl(thePin.getImageUrl());
		save_pin.setIsDeleted(0);
		save_pin.setName(thePin.getName());
		save_pin.setNameTc(thePin.getNameTc());
	
		
		Pin return_pin = pinRepository.saveAndFlush(save_pin);
		thePin.setId(return_pin.getId());
		
		List<String> staff_no = new ArrayList<>();
		
		staff_no = Arrays.asList(thePin.getStaffNo().split(","));
		
//		List<Long> staff_user_id = new ArrayList<>();
		
		for(Integer i = 0 ; i<staff_no.size() ;i++) {
			
			PinUser pin_user = new PinUser();
			String target_staffNo = staff_no.get(i);
			pin_user.setCreatedAt(new Date());
			pin_user.setCreatedBy(thePin.getCreatedBy());
			pin_user.setIsDeleted(0);
			pin_user.setPinId(return_pin.getId());
			
			System.out.println("PinServiceImp , line 112 : targetStaff = "+ target_staffNo);
			pin_user.setUserId(user_list_session.stream().filter(
					(u) -> u.getStaffNo().equals(target_staffNo)
				).map((u) -> {return u.getId();}).collect(Collectors.toList()).get(0));
			System.out.println("PinServiceImp , line 119: pin user  = "+ pin_user.getCreatedBy() + " is delete = "+ pin_user.getIsDeleted() +" pin id = "+ pin_user.getPinId());
			
			pinUserRepository.saveAndFlush(pin_user);
		}
		
		
		return thePin;
	}
	@Override 
	public void deletepin(Long pinId,Long userId) {
		
		
		pinRepository.deletePin(new Date(), userId, pinId);
		
	}

}


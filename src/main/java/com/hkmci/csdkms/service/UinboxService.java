package com.hkmci.csdkms.service;

import java.util.List;

import com.hkmci.csdkms.entity.Uinbox;

public interface UinboxService {

	Uinbox save(Uinbox new_record);
	Integer numberMail(Long userId );
	List<Object[]> getMail(Long userId);
	Uinbox getInboxInfo(Long id);
	List<Uinbox> saveAll(List<Uinbox> save_all);
	
	
	
	
}

package com.hkmci.csdkms.service;

import java.util.Date;

import org.jboss.as.controller.ModelVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.mobileVersion;
import com.hkmci.csdkms.repository.LogRepository;
import com.hkmci.csdkms.repository.mobileVersionRepository;

@Service
public class MobileVersionServiceImpl implements MobileVersionService {
	
	
	@Autowired
	private mobileVersionRepository mobileVersionRepository;
	
	@Autowired
	private LogRepository logRepository;
	
	@Override
	public mobileVersion createNewVersion(User user, String version, String app) {
		System.out.println("mobile version service impl : App:  "+ app);
		mobileVersionRepository.deletedmobileversion(user.getId(), new Date(), app);
		
		mobileVersion newMobileVersion = new mobileVersion();
		newMobileVersion.setApp(app);
		newMobileVersion.setCreatedAt(new Date());
		newMobileVersion.setCreatedBy(user.getId());
		newMobileVersion.setIsDeleted(0L);
		newMobileVersion.setVersion(version);
		return mobileVersionRepository.saveAndFlush(newMobileVersion);
		
	}
	
	
	
	
	@Override
	public Integer checkMobileVersion(Long user, String app) {
		System.out.println("Mobile Version Service Impl line 44, app version = "+app);
		app = app.toLowerCase();
		String app_version ="";
		app_version= mobileVersionRepository.findAppVerion(app);
		System.out.println("Mobile Version Service Impl , line 48 : existing app version = "+app_version + " App = " +app);
		Integer pkid =0;
		if (app.equals("ios")) {
			pkid =2;
		}else {
			pkid =1;
		}
		
		
		
		String needUpdate="";
		System.out.println("Mobile Version Service Impl , line 59 " + app_version + " pkid = "+ pkid + " user - " + user);
		needUpdate = logRepository.findMobileAppVersion(app_version, pkid, user);
		
		System.out.println("Mobile Version Service Impl , line 60 = "+ needUpdate);
		Integer check = 0; 
		if (app.equals("")) {
			System.out.println("Mobile Version Service Impl , line 50: app = null");
			check = 1;
		} else {
			if(needUpdate==null) {
				System.out.println("Mobile Version Service Impl, line 67: User need to update mobile version .");
				check = 1;
				}
				else {
					System.out.println("Mobile Version Service Impl, line 71: User has the latest mobile version .");
				
			}
			
		}
		return check;
	}
	
	
	@Override
	public String findAppVersion(String app) {
		
		return mobileVersionRepository.findAppVerion(app);
	}
	

}

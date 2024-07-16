package com.hkmci.csdkms.service;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.mobileVersion;

public interface MobileVersionService {

	mobileVersion createNewVersion(User user, String version, String app);

	String findAppVersion(String app);

	Integer checkMobileVersion(Long user, String app);

}

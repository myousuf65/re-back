package com.hkmci.csdkms.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserAccessRule;
import com.hkmci.csdkms.entity.UserGroup;

@Service
public interface UserService {
	List<User> findAll();
	
	List<User> allInSession();
	
	List<User> getUserList();

    List<User> findByName(String name);
	
	Optional<User> findById(Long id);
	
	User findByUsername(String username);

	Integer findScoreById(Long id);
	
	Integer findTodayScoreById(Long id);
	
	User findByStaffNo(String staffNo);
	
	User addUser(User user);

	User updateUserById(User user);

	User deleteUserById(Long id);
	
	Optional<User> checkDeletePermissionById(Long userId, Long commentId);

	List<User> findByUserIdAndSearch(List<Long> accessRuleId, String staffNo, String fullname, String groupName, String institution, String rank, String section, Long isBlogger, Integer page, Integer is_admin,Integer is_special);

	User storeToUserFolder(MultipartFile file, User user, String type);

	List<UserGroup> getGroup();

	List<Long> getAdminList();

	List<UserAccessRule> getByUserId(Long user_id);

	void saveAllAccessRule(List<UserAccessRule> toSave);

	void deleteResourceAccessRule(Long userId);

	Integer getTotal(List<Long> accessRuleId, String staffNo, String fullname, String groupName, String institution,
			String rank, String section, Long isBlogger, Integer page, Integer is_admin);

	Long getUserGroup(Long id);

	List<Object []> getUserGroupAndInst(Long id);
	
	List<UserAccessRule> getAll(Long userId);

	void updateAllAccessRule(List<Long> toUpdate, Long userId);

	List<User> findByStaffName(String asText, Integer limitStart, Integer limitEnd);

	List<User> getSpecial(Long user_id);
	

	User getCurrentUser();

	User changeLang(User user);

	List<User> findByUserNameAndSearch(String fullname, Integer page);

	Integer getTotalByName(String fullName);

	List<User> allExistInSession();

	List<Object> getSeniorOfficerList(Integer disciplined);
	

}


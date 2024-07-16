package com.hkmci.csdkms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hkmci.csdkms.controller.Md5Encode;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.CustomUserDetails;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserAccessRule;
import com.hkmci.csdkms.entity.UserGroup;
import com.hkmci.csdkms.repository.BlogRepository;
import com.hkmci.csdkms.repository.UserAccessRuleRepository;
import com.hkmci.csdkms.repository.UserGroupRepository;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;


@Service
public class UserServiceImpl implements UserService{
	@Autowired 
	HttpServletRequest request;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private BlogRepository blogRepository;
	
	@Autowired
    private UserGroupRepository userGroupRepository;
	
	@Autowired
	@Resource
	private UserAccessRuleRepository userAccessRuleRepository;
	
	@Autowired
	@Resource
	private StorageService storageService;
	
	@PersistenceContext
	private EntityManager entityManager;

    /**
     * Get User List
     * @return
     */
    public List<User> getUserList(){
        List<User> userList = new ArrayList<User>();
        userList = userRepository.findByIsDeleted(0);
        return  userList;
    }
    
    /**
     * Find All User
     * @return
     */
    public List<User> findAll(){
        List<User> userList = new ArrayList<User>();
        userList = userRepository.findByIsDeleted(0);
        return  userList;
    }
    
    
    
    
    @Override
    public List<User> allExistInSession(){
    	List<User> result = userRepository.getAllExist().stream()
				.map((n) -> {
					User new_user = new User(n.getId(),n.getFullname(),
							n.getStaffNo(),n.getUsername(),n.getEmail(),n.getNotesAccount(),n.getInstitutionId(),
							n.getSectionId(),n.getRankId(), n.getProfilePhoto(), n.getAliasPhoto(),n.getAliasPhotoIsPending(),n.getProfilePhotoIsPending(),n.getIsDeleted());
					return new_user;
				}).collect(Collectors.toList());
    	System.out.println("Test Rank size "+ result.size());
    	System.out.println("Test Rank: " + result.get(8).getRankId());
    	return result;
    }
    
    
    
    @Override
    public List<User> allInSession(){
		// TODO Auto-generated method stub
//		String sql ="select new User(id,fullname,staffNo,institutionId,sectionId,rankId) from User u,";
//		TypedQuery<User> query =entityManager.createQuery(sql, User.class);
//		List<User> result = query.getResultList();
    	List<User> result = userRepository.getAll().stream()
    						.map((n) -> {
    							User new_user = new User(n.getId(),n.getFullname(),
    									n.getStaffNo(),n.getUsername(),n.getEmail(),n.getNotesAccount(),n.getInstitutionId(),
    									n.getSectionId(),n.getRankId(), n.getProfilePhoto(), n.getAliasPhoto(),n.getAliasPhotoIsPending(),n.getProfilePhotoIsPending(),n.getIsDeleted());
    							return new_user;
    						}).collect(Collectors.toList());
//    	System.out.println("Test Rank size "+ result.size());
//		System.out.println("Test Rank: " + result.get(8).getRankId());
		return result;
	}
    
    /**
     * Find By ID
     * @param user_id ID
     * @return
     */
    public Optional<User> findById(Long id) {
        return userRepository.findByIdAndIsDeleted(id,0);
    }
    
    
    /**
     * Find user score 
     * @param user_id Id
     * @return
     */
    public Integer findScoreById(Long id) {
    	
    	return userRepository.findScoreByIdAndIsDeleted(id,0);
    }
    
    /**
     * Find user today score 
     * @param user_id Id
     * @return
     */
    public Integer findTodayScoreById(Long id) {
    	return userRepository.findTodayScoreByIdAndIsDeleted(id, 0);
    }
    
    
    
    

    /**
     * Find By Username
     * @param username
     * @return
     */
    public User findByUsername(String username) 
    {
    	
    	String INTERDICT ="INTERDICT";
    	System.out.println("User Service Impl line 162, username = "+ username + " INTERDICT = " + INTERDICT );
        return userRepository.findByUsernameAndIsDeleted(username, 0,INTERDICT);
    }
    
    
    /**
     * Find by Staff No (employee Number)
     * @param staff no 
     * @return
     */
    public User findByStaffNo(String staffNo) {
//    	HttpSession session = request.getSession();
//    	User user = (User) session.getAttribute("user_session");
//    	if(user.getStaffNo().equals(staffNo)) {
//    		return user;
//    	}else {
    	String INTERDICT ="INTERDICT";
    		User new_login = userRepository.findByStaffNoAndIsDeleted(staffNo,0,INTERDICT);
    		//session.setAttribute("user_session", new_login);
    		return new_login;
//    	}
    }
    
    /**
     * Add User
     * @param User user
     * @return 
     * @return
     */
    public User addUser(User User) {
    	User.setIsDeleted(0);
        return userRepository.saveAndFlush(User);
    }


	
    
    
    
    /**
     * Update User
     * @param User user
     * @return
     */
    public User updateUserById(User User) {
    	User.setIsDeleted(0);
       return userRepository.saveAndFlush(User);
    }

    /**
     * Delete User
     * @param id Id
     */
    @Override
    public User deleteUserById(Long id) {
    	Optional<User> user = userRepository.findByIdAndIsDeleted(id, 0);
    	User new_user = new User();
    	new_user = user.get();
    	new_user.setIsDeleted(1);
        return userRepository.save(new_user);
    }

	@Override
	public List<User> findByName(String name) {
		List<User> userList = new ArrayList<User>();
        userList = userRepository.findByFullnameAndIsDeleted(name,0);
        return  userList;
    }

	@Override
	public Optional<User> checkDeletePermissionById(Long userId, Long commentId) {
		// TODO Auto-generated method stub
		List<Long> adminIds = new ArrayList<Long>();
		//Check admin ids
		adminIds.add(1L);
		//Check post creator
		Optional<Blog> return_blog = blogRepository.getByCommentId(commentId);
		if(return_blog.isPresent()) {
			adminIds.add(return_blog.get().getCreatedBy());
			adminIds.add(return_blog.get().getOriginalCreator().getId());
		}
		return userRepository.checkDeletePermissionById(userId, commentId, adminIds);
	}

	@Override
	public List<User> findByUserIdAndSearch(List<Long> accessRuleId, String staffNo, String fullname, String groupName,
			String institution, String rank, String section, Long isBlogger, Integer page, Integer is_admin,Integer is_special) {
		// TODO Auto-generated method stub
//		System.out.println("Search Params: accessRuleId: " + accessRuleId + ",\n staffNo: " + staffNo 
//				+ ",\n fullname: " + fullname + ",\n groupName: " + groupName + ",\n institution: " + institution
//				+ ",\n rank: " + rank + ",\n section: " + section + ",\n isBlogger: " + isBlogger );
		Integer start_num = (page - 1) * 20;
		Integer end_num =  20;
		Long groupNameId = Long.parseLong(groupName);
		institution=institution.toUpperCase();
//		if (groupNameId==7) {
//			return userRepository.findByGroupNameId(groupNameId);
//		}
		if (staffNo.length() >3   || fullname.length() >3) {
			//return userRepository.findByDeletedUserIdAndSearch(accessRuleId, staffNo, fullname, groupNameId, institution, rank, section, isBlogger, start_num, end_num, is_admin, is_special);
			return userRepository.findByUserNameAndStaffAndSearch(accessRuleId, staffNo, fullname, groupNameId, institution, rank, section, isBlogger, start_num, end_num, is_admin, is_special);
		}
		else if (institution.contains("RESIGN")) {
//			System.out.println("User service Imp : Line 258 : institution contains resign");
			return userRepository.findByDeletedUserIdAndSearch(accessRuleId, staffNo, fullname, groupNameId, institution, rank, section, isBlogger, start_num, end_num, is_admin, is_special);
			
		} else {
			return userRepository.findByUserIdAndSearch(accessRuleId, staffNo, fullname, groupNameId, institution, rank, section, isBlogger, start_num, end_num, is_admin, is_special);
		}
	}
	@Override
	public List<User> findByUserNameAndSearch( String fullname,Integer page) {
		
		Integer start_num = (page - 1) * 20;
		Integer end_num =  20;
//		System.out.println("Full name = "+ fullname + " start number = "+ start_num + " end num " + end_num);
		return userRepository.findByUserNameAndSearch( fullname);
	}
	
	
	
	
	
	
	@Override
	public User storeToUserFolder(MultipartFile file, User user, String type) {
			
			Path userFilePath = storageService.getUserFolderLocation();
			String userFolderName = "resources/" + storageService.getUserFolderName();
			String userFolder = userFilePath.toString() + "/" + user.getId() + "/";
			Path userFolderPath = Paths.get(userFolder);
			try {
	            Files.createDirectories(userFolderPath);
	        }
	        catch (IOException e) {
	            throw new StorageException("Could not create user folder", e);
	        }
			storageService.storeToLocation(file, userFolderPath);
			
			//File Info to Database
			String file_name_original =StringUtils.cleanPath(file.getOriginalFilename());
	        String fileType=file_name_original.substring(file_name_original.lastIndexOf("."),file_name_original.length());
	    	Md5Encode md5 = new Md5Encode();
	        String filename = md5.getMD5(file_name_original)+fileType;
	        //String ofilename = md5.getMD5(file.getOriginalFilename())+fileType;
	        if(type.equals("alias")) {
//	        	user.setAliasPhoto(userFolderName + "/" + user.getId() + "/" + filename );
	        	user.setAliasPhotoIsPending(userFolderName + "/" + user.getId() + "/" + filename);
	        	user.setAliasPhotoPendingDate(new Date());
	        }else {
//	        	user.setProfilePhoto(userFolderName + "/" + user.getId() + "/" + filename); 
	        	user.setProfilePhotoIsPending(userFolderName + "/" + user.getId() + "/" + filename);
	        	user.setProfilePhotoPendingDate(new Date());
	        }
	        System.out.println("User Service Imp = 314 : "+ user.getAliasPhotoIsPending() + " Profile  " + user.getProfilePhotoIsPending() );
	        return userRepository.saveAndFlush(user);
		}
	
	@Override
	public User changeLang(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public List<UserGroup> getGroup() {
		List<UserGroup> return_data = userGroupRepository.getAll();
		return return_data;
	}

	@Override
	public List<Long> getAdminList() {
		List<Integer> admin_list = userRepository.getAdminList();
		return admin_list.stream().map((n) -> Long.parseLong(n.toString())).distinct().collect(Collectors.toList());
	}

	@Override
	public List<UserAccessRule> getByUserId(Long user_id) {
		List<UserAccessRule> return_data = userAccessRuleRepository.getByUserId2(user_id);
		if(return_data == null) {
			return null;
		}else {
			if(return_data.size() == 0) {
				return null;
			}else {
				return return_data;
			}
		}
	}

	@Override
	public void saveAllAccessRule(List<UserAccessRule> toSave) {
		userAccessRuleRepository.saveAll(toSave);
		return;
		
	}

	@Override
	public void deleteResourceAccessRule(Long userId) {
		userAccessRuleRepository.deleteResourceAccessRule(userId);
		return;
		
	}
	
	
	@Override 
	public Integer getTotalByName(String fullName) {
		Integer back_date = userRepository.getTotalByName(fullName);
		return back_date;
	}
	
	@Override
	public Integer getTotal(List<Long> accessRuleId, String staffNo, String fullname, String groupName,
			String institution, String rank, String section, Long isBlogger, Integer page, Integer is_admin) {
		Long groupNameId = Long.parseLong(groupName);
		institution = institution.toUpperCase();
		Integer back_date =0;
		if(institution.contains("RESIGN")) {
			 back_date = userRepository.getDeleteTotal(accessRuleId, staffNo, fullname, groupNameId, institution, rank, section, isBlogger, is_admin);
				
		} else {
		 back_date = userRepository.getTotal(accessRuleId, staffNo, fullname, groupNameId, institution, rank, section, isBlogger, is_admin);
		}
		 return back_date;
	}

	@Override
	public Long getUserGroup(Long id) {
		// TODO Auto-generated method stub
		
//		System.out.println("User ServiceImpl user grop - "+ userRepository.getUsergroup(id));
		return (userRepository.getUsergroup(id));
	}

	
	@Override
	public List<Object> getSeniorOfficerList(Integer disciplined){
		List<Object []> return_list = userRepository.getSeniorOfficeList(disciplined);
		List<Object > return_data= new ArrayList<>();
		
		for(Integer i =0 ;i <return_list.size();i++) {
			Object [] data = (Object [])return_list.get(i); 
			HashMap<String, String> list = new HashMap<String, String>();
			String INST =null;
			String DIV = null;
			String RANK = null;
			String NAME = null;
			String SECT = null;
			String DUTY = null;
			String REMARK = null;
			
			INST= data[4].toString();
			if(data[0]==null) {
				DIV="";
			} else {
				DIV = data[0].toString();
			}
			if(data[7].toString().length() >3) {
				String number = data[7].toString().substring(data[7].toString().lastIndexOf("%") +1 );
//				System.out.println("User Service Impl : line 408 : Number = "+ number);
				if(number.length() >1) {
					RANK = number;
				 } else {
					 RANK = data[1].toString();
				 }
				
			} else 
			{
				RANK = data[1].toString();
			}
			
			
			
			if(data[2].toString().length()>2) {
				NAME = " "+data[2].toString() + " "+ data[3].toString();
			} else {
				NAME = " "+data[3].toString();
			}
			SECT = data[5].toString();
			if(data[7].toString().length()>3) {
				String replace_duty ="";
				replace_duty = data[7].toString().substring(replace_duty.indexOf("%") + 10);
//				System.out.println("User Service Impl : line 433 : replace duty = "+ replace_duty);
				DUTY = replace_duty;
			} else {
				DUTY = data[6].toString();
			}
			
			if(data[8].toString().length()>1 && data[9].toString().length() >1) {
				REMARK =data[7].toString()+" FROM "+ data[8].toString() + " TO "+ data[9].toString();
			} else if (data[8] .toString().length()>1 && data[9] .toString().length()==1) {
				REMARK =data[7].toString()+" FROM "+ data[8].toString() ;
			} else {
				REMARK =data[7].toString();
			}
			
			
			list.put("INST",INST );
			list.put("DIV", DIV);
			list.put("RANK",RANK);
			list.put("NAME",NAME );
			list.put("SECT",SECT );
			list.put("DUTY",DUTY );
			list.put("REMARK",REMARK );
//			System.out.println("User Service Impl : line 428 : "+ list);
			return_data.add(list);
			
		}
		
		
		return return_data;
	}
	
	@Override
	public List<Object []> getUserGroupAndInst(Long id){
		
		List<Object []> return_data = userRepository.getUserGroupAndInst(id);
		return return_data;
	}
	
	
	@Override
	public List<UserAccessRule> getAll(Long userId) {
		List<UserAccessRule> return_data = userRepository.findByUserId(userId);
		return return_data;
	}

	@Override
	public void updateAllAccessRule(List<Long> toUpdate, Long userId) {
		userRepository.updateAllAccessRule(toUpdate, userId);
		return;
	}

	@Override
	public List<User> findByStaffName(String asText, Integer limitStart, Integer limitEnd) {
		// TODO Auto-generated method stub
		return (userRepository.findByStaffName(asText,limitStart, limitEnd));
	}

	@Override
	public List<User> getSpecial(Long user_id) {
		// TODO Auto-generated method stub
		//userRepository.
		return null;
	}
	
    public User getCurrentUser() {
    	
        CustomUserDetails customUserDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new User(customUserDetails);
    }
	

	
}

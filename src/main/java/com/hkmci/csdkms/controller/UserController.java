package com.hkmci.csdkms.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.EmailUtil;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.entity.UserAccessRule;
import com.hkmci.csdkms.entity.UserDog;
import com.hkmci.csdkms.entity.UserGroup;
import com.hkmci.csdkms.entity.UserProfile;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.report.ReportService;
import com.hkmci.csdkms.security.JwtTokenProvider;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.UserDogService;
import com.hkmci.csdkms.service.UserGroupService;
import com.hkmci.csdkms.service.UserProfileService;
import com.hkmci.csdkms.service.UserService;

import ch.qos.logback.classic.Logger;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

	
	@Value("${app.isDebug}")
	private Integer isDebug;
	
	@Autowired
	//private User user;
	@Resource
    private UserService userService;
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource
	private AccessRuleService accessRuleService;
	
	@Autowired
	@Resource
	private UserGroupService userGroupService;
	
	@Autowired
	@Resource
	 private ReportService reportService;
	
	@Autowired
	@Resource
	private UserProfileService userProfileService;
	
	@Autowired
	@Resource
	private UserDogService userDogService;
	
	
	
	@RequestMapping("/all")
    public ResponseEntity<JsonResult> getList(HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(userService.findAll(),session);
    }
	
	@RequestMapping("/getGroup")
    public ResponseEntity<JsonResult> getGroup(HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(userService.getGroup(),session);
    }
	
	
	
	@RequestMapping("/changeLang/{userId}/{lang}")
	public ResponseEntity<JsonResult> changeLanguage(@PathVariable Long userId , @PathVariable String lang ,HttpSession session){
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			User user = (User) session.getAttribute("user_session");
			System.out.println("User current lang - "+ user.getLang());
			user.setLang(lang);
			System.out.println("User changed lang - "+ user.getLang());
			return JsonResult.ok(userService.changeLang(user),session);
		}
		
	}
	@RequestMapping("/findByID")
    public ResponseEntity<JsonResult> findByID(@RequestParam(value="id",required=false,defaultValue="") Long id,HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(userService.findById(id),session);
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getDetails")
    public ResponseEntity<JsonResult> getDetails(@RequestParam(value="userId",required=true,defaultValue = "0") Long userId,HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User temp_user = userService.findById(userId).get();
			//User temp_user = (User) session.getAttribute("user_session");
			//List<Long> session_access_rule = (List<Long>) session.getAttribute("user_access_rule_session");
			List<UserAccessRule> temp_data = userService.getByUserId(userId);
			List<Long> user_access_rule = new ArrayList<>();
			if(temp_data == null || temp_data.size() == 0) {
				user_access_rule.add(0L);
			}else{
				user_access_rule = userService.getByUserId(temp_user.getId())
										  .stream().map(UserAccessRule::getAccessRuleId).collect(Collectors.toList());
			}
			
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			 
			temp_user.setUsergroup(temp_user.getUsergroup());

			
			
			String path ="";
//			
			System.out.println("User Controller : 203 =AliasPhoto is pending is not null "+ temp_user.getAliasPhotoIsPending());
			
			if(temp_user.getAllowUpload() ==1) {
				System.out.println("User Controller : 206 =AliasPhoto is pending is not null "+ temp_user.getAliasPhotoIsPending());
				
				temp_user.setAliasPhoto(temp_user.getAliasPhoto());
				temp_user.setProfilePhoto(temp_user.getProfilePhoto());
			} else {
				if( temp_user.getAliasPhotoIsPending() != null ) {
					System.out.println("AliasPhoto is pending is not null "+ temp_user.getAliasPhotoIsPending());
					path = temp_user.getAliasPhotoIsPending().toString();
					
					path =path.replace("user_profile", "resources");	
					
					System.out.println("User Controller: 215 = Path :"+ path);
					temp_user.setAliasPhoto(path);
				} else  {
					path = temp_user.getAliasPhoto().toString();
					path = path.replace("user_profile", "resources");
					temp_user.setAliasPhoto( temp_user.getAliasPhoto());
		    	}
				System.out.println("User Controller : line 187 - "+ temp_user.getProfilePhotoIsPending() );
				if( temp_user.getProfilePhotoIsPending() != null )  {
					path = temp_user.getProfilePhotoIsPending().toString();
					path = path.replace("user_profile", "resources");
					System.out.println("User Controller , line 191 = "+ path);
					temp_user.setProfilePhoto(path);
				} 
			}
			
			temp_user.setInstitution(institution_session.stream()
									 .filter((n) -> n.getId().equals(temp_user.getInstitutionId()))
									 .map(InstitutionsModel::getInstName)
									 .collect(Collectors.toList())
									 .get(0));
			temp_user.setSection(section_session.stream()
								 .filter((n) -> n.getId().equals(temp_user.getSectionId()))
								 .map(SectionModel::getSectionName)
								 .collect(Collectors.toList())
								 .get(0));
			temp_user.setRank(rank_session.stream()
							  .filter((n) -> n.getId().equals(temp_user.getRankId()))
							  .map(RanksModel::getRankName)
							  .collect(Collectors.toList())
							  .get(0));
			Integer is_blogger = temp_user.getBlogCreate().equals("1") ? 1 : 0;
			temp_user.setIsBlogger(is_blogger); 
			temp_user.setLoginTries(temp_user.getLoginFailAttempt());
			temp_user.setAccessRuleList(user_access_rule);
			
			
			if(temp_user.getProfilePhotoPendingDate() != null) {
				
				Calendar rightNow2 = Calendar.getInstance();
				rightNow2.setTime(temp_user.getProfilePhotoPendingDate());
				rightNow2.add(Calendar.DAY_OF_YEAR, 90);//日子加1日
				Date startDate = rightNow2.getTime();
				
				Date today = new Date();
				if(temp_user.getScore() > 2700 && today.after(startDate) ) {
					System.out.println("User Controller : line 230 = "+ today.after(startDate) );
					
					temp_user.setAllowUpload(1);
				} else { 
					System.out.println("User Controller : line 234 = "+ today.after(startDate));
					
					temp_user.setAllowUpload(0);
				}

			} else {
				if((temp_user.getScore() > 2700 || temp_user.getUsergroup() == 5 ) ) {
					
					temp_user.setAllowUpload(1);
					
				} else { 
					temp_user.setAllowUpload(0);

				}
			}
			return JsonResult.ok(temp_user,session);
		}
    }

//	@RequestMapping("/findbystaffno")
//    public ResponseEntity<JsonResult> findByID(HttpServletRequest request,HttpSession session) {
//		
//		User user = new User();
//		if(isDebug != null && isDebug.equals(1) && request.getHeader("employeeNumber") == null) {
//			session.setAttribute("channel",1);
//			common.checkApiSession("12240",  session,  1);
//		}else {
//			user = userService.findByStaffNo(request.getHeader("employeeNumber"));
//		}
//		User user_from_session = (User) session.getAttribute("user_session");
//		System.out.println("User From Session Login last try = " + user_from_session.getLoginLastTry() );
//		
//		//HttpSession session = request.getSession();
//		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
//		HashMap<String, String> user_check = common.checkUser(user.getId(),session);
//			if(user_check.get("msg") != "") {
//				return JsonResult.errorMsg(user_check.get("msg").toString());
//			}else{
//				//  --- remote error null pointer 
//				Integer is_blogger = user_from_session.getBlogCreate().equals("N") ? 0 : 1;
//				user_from_session.setIsBlogger(is_blogger);
//				System.out.println("Last Oogin time  "+user_from_session.getLoginLastTry());
//				//common.saveUserLastLoginTime(user_from_session, session);
//				return JsonResult.ok(user_from_session,session);
//			}
//		
//    }
	
	
	@RequestMapping("/getUserScore/{user_id}")
	public ResponseEntity<JsonResult> getUserMenuScore(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		
		
		User user_from_session = (User) session.getAttribute("user_session");
		Date start = common.textToDateDate(jsonNode.get("startDate").asText());
		Date end = new Date();
		Date KReward1Strat =common.textToDateDate("2020-03-01");
		Date KReward1End = common.textToDateDate("2020-8-31");
		
		Date KReward2Strat =common.textToDateDate("2021-05-01");
		Date KReward2End =common.textToDateDate("2021-10-31");
		
		
		
		Calendar rightNow2 = Calendar.getInstance();
		rightNow2.setTime(start);
		rightNow2.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
		Date startDate = rightNow2.getTime();
		
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(end);
		rightNow.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
		Date endDate = rightNow.getTime();
		System.out.println("User id = "+ user_id +" Start Date = " + startDate + " End Date = " + endDate);
		
		
		Calendar rightNow3 = Calendar.getInstance();
		rightNow3.setTime(KReward1Strat);
		rightNow3.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
		Date KReward1Strat2 = rightNow3.getTime();
		
		Calendar rightNow4 = Calendar.getInstance();
		rightNow4.setTime(KReward1End);
		rightNow4.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
		Date KReward1End2 = rightNow4.getTime();
		System.out.println("User id = "+ user_id +" Start Date = " + startDate + " End Date = " + endDate);
		
		Calendar rightNow5 = Calendar.getInstance();
		rightNow5.setTime(KReward2Strat);
		rightNow5.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
		Date KReward2Strat2 = rightNow5.getTime();
		
		Calendar rightNow6 = Calendar.getInstance();
		rightNow6.setTime(KReward2End);
		rightNow6.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
		Date KReward2End2 = rightNow6.getTime();
		System.out.println("User id = "+ user_id +" Start Date = " + startDate + " End Date = " + endDate);
		
		
		
		
	        ZoneId zoneId = ZoneId.of("Asia/Hong_Kong");
		System.out.println("-------------------");
		//score_log = reportService.getUserMenuScore(user_id, sumDate);
		List<Object []> score_log = reportService.getUserMenuScore(user_id, startDate, endDate);
		System.out.println("=====================================");
	

		
		List<Object> return_data =new ArrayList<>();
		for(Integer i = 0 ; i < score_log.size() ; i++) {
			HashMap<String,Object> score_info = new HashMap<>();
			Object[] data = (Object []) score_log.get(i);
			score_info.put("createdAt", data[0]);
			score_info.put("score", data[2]);
			return_data.add(score_info);
		}
	
		System.out.println("Return data  Size = " + score_log.size());
		List<Object> return_data2 =new ArrayList<>();
		List<Object []> score_log2= reportService.getUserMenuScoreByPeriod(user_id, KReward1Strat2, KReward1End2);
		HashMap<String,Object> score_info2 = new HashMap<>();
		Object[] data2 = (Object []) score_log2.get(0);
		score_info2.put("K-Reward1", data2[2]);
//		return_data2.add(score_info2);
		score_log2= reportService.getUserMenuScoreByPeriod(user_id, KReward2Strat, KReward2End);
		score_info2.put("login",user_from_session.getLoginTries() );
		
		data2 = (Object []) score_log2.get(0);
		score_info2.put("K-Reward2", data2[2]);
		return_data2.add(score_info2);
//		
		return JsonResult.ok(return_data,return_data2,session);
	}
	
	
	
	
	
	
//	
//	@RequestMapping("/getUserScore/{user_id}")
//	public ResponseEntity<JsonResult> getUserMenuScore(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
//		
//		
//
//		Date start = common.textToDateDate(jsonNode.get("startDate").asText());
//		Date end = common.textToDateDate(jsonNode.get("endDate").asText());
//		
//		
//		Calendar rightNow2 = Calendar.getInstance();
//		rightNow2.setTime(start);
//		rightNow2.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
//		Date startDate = rightNow2.getTime();
//		
//		Calendar rightNow = Calendar.getInstance();
//		rightNow.setTime(end);
//		rightNow.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
//		Date endDate = rightNow.getTime();
//		System.out.println("User id = "+ user_id +" Start Date = " + startDate + " End Date = " + endDate);
//		
//		
//	        ZoneId zoneId = ZoneId.of("Asia/Hong_Kong");
//		System.out.println("-------------------");
//		//score_log = reportService.getUserMenuScore(user_id, sumDate);
//		List<ScoreLog> score_log = reportService.getUserMenuScore(user_id, startDate, endDate);
//		System.out.println("=====================================");
//		Map<Object, Integer> score_each = score_log.stream()
//				
////				.collect(Collectors.groupingBy(e ->
////			    // easier way to truncate the date
////			    Date.from(e.getCreatedAt().toInstant().truncatedTo(ChronoUnit.DAYS)), Collectors.summingInt(ScoreLog::getScore)));
////		
//				.collect(Collectors.groupingBy(e ->
////			    // easier way to truncate the date
//			    Date.from(e.getCreatedAt().toInstant()
//			    	      .truncatedTo(ChronoUnit.DAYS)), Collectors.summingInt(ScoreLog::getScore)));
////		ZonedDateTime.now(UTC).
//		
//		System.out.println("-------------------------");
//		//List<ScoreLog> return_data =new ArrayList<>(); List<Object> rList = new ArrayList<>();
//		List<Object> return_data =new ArrayList<>();
//		score_each.forEach((k,v)->{
//			HashMap<String,Object> score_info = new HashMap<>();
//			score_info.put("createdAt", k);
//			score_info.put("score", v);
//			return_data.add(score_info);
//;		});
//		
//		System.out.println("Return data  Size = " + score_log.size());
//		
//		return JsonResult.ok(return_data,session);
//	}
//	

	
	
	@RequestMapping("/findbystaffno")
    public ResponseEntity<JsonResult> findByID(HttpServletRequest request,HttpSession session) {
		System.out.println("get into find by staff no , employee number ");
		
		User user = new User();
		
		if(isDebug != null && isDebug.equals(1) && request.getHeader("employeeNumber") == null) {
			System.out.println("In debug mode ");
			session.setAttribute("channel",1);
			common.checkApiSession("12240",  session,  1);
		}else {
			System.out.println("Not in debug mode ");
			if (request.getHeader("employeeNumber") != null) {
				System.out.println("Employee Number is not null, Employee number =  " +request.getHeader("employeeNumber") );
				user = userService.findByStaffNo(request.getHeader("employeeNumber"));
				session.setAttribute("user_session", user);
				
			}else if (request.getHeader("authenticatedUser") != null) {
				System.out.println("Authenticated User is not null ");
				user = userService.findByUsername(request.getHeader("authenticatedUser"));
				System.out.println("Authenticated User is not null , user staff no = "+ user.getStaffNo());
			}
		}
		
		
//		System.out.println("Session is null = "+ session.getAttribute("user_session"));
		User user_from_session = (User) session.getAttribute("user_session");
		System.out.println("User From Session "+user_from_session.getId());
		System.out.println("User From Session Login last try = " + user_from_session.getLoginLastTry() );
		
		//HttpSession session = request.getSession();
		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		HashMap<String, String> user_check = common.checkUser(user.getId(),session);
		
		Object channel_session = session.getAttribute("channel");
		Integer channel = channel_session == null ? 1 : (Integer) channel_session;
		System.out.println("Check User Channel = "+ channel);
			if(user_check.get("msg") != "") {
				System.out.println("MSG is not null ");
				return JsonResult.errorMsg(user_check.get("msg").toString());
			}else{
				//  --- remote error null pointer 
				System.out.println("MSG is null");
				Integer is_blogger = user_from_session.getBlogCreate().equals("N")  ||  user_from_session.getBlogCreate().equals("0") ? 0 : 1;
				user_from_session.setIsBlogger(is_blogger);
				
				System.out.println("Last Oogin time  "+user_from_session.getLoginLastTry());
				//common.saveUserLastLoginTime(user_from_session, session);
				
//				if(new Date ().before(common.textToDateDate("2021-05-01")) ) {
//					user_from_session.setScore(0);
//				}
				return JsonResult.ok(user_from_session,channel.toString(),session);
			}
		
		
    }
	
	@RequestMapping("/LoginLastTime/{staffNo}")
	 public ResponseEntity<JsonResult> LoginLastTime(@PathVariable String staffNo,HttpSession session) {
//		User return_user = userService.findByStaffNo(staffNo);
//		return_user.setLoginLastTry(new Date());
//		userService.addUser(return_user);
//		System.out.println("User Login Last Try =  "+return_user.getLoginLastTry());
		return JsonResult.ok(null,session);
		
	}
	
	
	@RequestMapping("/getUserByStaffNo/{user_id}")
    public ResponseEntity<JsonResult> getUserByStaffNo(@PathVariable Long user_id,HttpSession session,
    		@RequestParam(value="staffNo",required=false,defaultValue="0") String staffNo) {
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User return_user = userService.findByStaffNo(staffNo);
			if(return_user == null) {
				return JsonResult.ok(null,session);
			}else {
				HashMap<String,Object> return_object = new HashMap<>();
				return_object.put("id", return_user.getId());
				return_object.put("staffNo", return_user.getStaffNo());
				return_object.put("fullname", return_user.getFullname());
				//return_object.put("chinese_name", return_user.getChineseName());
				return JsonResult.ok(return_object,session);
			}
		}
    }
	
	@RequestMapping("/delete")
    public ResponseEntity<JsonResult> deleteUser(@RequestParam(value="id",required=false,defaultValue="") Long id,HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(userService.deleteUserById(id),session);
    }
	
	@RequestMapping("/findByName")
    public ResponseEntity<JsonResult> findByName(@RequestParam(value="name",required=false,defaultValue="") String name,HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(userService.findByName(name),session);
    }
	
	@RequestMapping("/special/get/{user_id}")
    public ResponseEntity<JsonResult> getSpecialUser(@PathVariable Long user_id,HttpSession session) {
         //model.addAttribute("user", userRepository.findAll());
         
         return JsonResult.ok(userService.getSpecial(user_id),session);
    }
	

	
	
	@RequestMapping("/approve/{userId}")
	public ResponseEntity<JsonResult> approveProfilePic(@PathVariable Long userId, @RequestBody JsonNode jsonNode, HttpSession session ) 
	throws Exception, Throwable {
		Integer type = jsonNode.get("type").asInt();
		Integer approved = jsonNode.get("approve").asInt();
		Long update_user = jsonNode.get("user").asLong();
		System.out.println("User Controller , line 553 = update user id = "+update_user);
//		UserProfile exist_user = userProfileService.checkExist(userId, type);
//		if (exist_user.equals(null)) {
//			return JsonResult.errorMsg("No Icon need to approve");
//		} else {
//			exist_user.setApproved(approved);
//			exist_user.setApprovedBy(userId);
//			exist_user.setApprovedAt(new Date());
//			userProfileService.ApproveIcon(exist_user);
//			
//			return JsonResult.ok("",session);
//		}
		
		Optional<User> findOldUser = userService.findById(update_user);
		if(findOldUser.isPresent()) {
			User return_user = findOldUser.get();
			if(type ==1 && approved== 0) {
//				return_user.setProfilePhoto(return_user.getProfilePhotoIsPending());
				return_user.setProfilePhotoPendingDate(null);
				userService.updateUserById(return_user);
				String title ="KMS Admin has been rejected your icon ";
				String content = "KMS Admin has been rejected your icon, please upload your profile icon again.";
				String staffEmail = return_user.getEmail();
				EmailUtil.sendEmailNoticAdmin(return_user.getFullname(), content, title, staffEmail);
				return JsonResult.ok(session);
				
			} else if (type ==1 && approved== 1){
				return_user.setProfilePhoto(return_user.getProfilePhotoIsPending());
//				return_user.setProfilePhotoIsPending(null);
//				return_user.setProfilePhotoPendingDate(null);
				userService.updateUserById(return_user);
				String title ="KMS Admin has been approved your icon ";
				String content = "KMS Admin has been approved your profile icon.";
				String staffEmail = return_user.getEmail();
				EmailUtil.sendEmailNoticAdmin(return_user.getFullname(), content, title, staffEmail);
				return JsonResult.ok(session);
				
			} else if (type ==2 && approved== 0) {
				return_user.setAliasPhotoPendingDate(null);
				userService.updateUserById(return_user);
				String title ="KMS Admin has been rejected your icon ";
				String content = "KMS Admin has been rejected your icon, please upload your alias icon again.";
				String staffEmail = return_user.getEmail();
				EmailUtil.sendEmailNoticAdmin(return_user.getFullname(), content, title, staffEmail);
				return JsonResult.ok(session);
			} else if (type ==2 && approved ==1 ) {
				return_user.setAliasPhoto(return_user.getAliasPhotoIsPending());
//				return_user.setAliasPhotoIsPending(null);
//				return_user.setAliasPhotoPendingDate(null);
				String title ="KMS Admin has been approved your icon ";
				String content = "KMS Admin has been approved your profile icon.";
				String staffEmail = return_user.getEmail();
				EmailUtil.sendEmailNoticAdmin(return_user.getFullname(), content, title, staffEmail);
				return JsonResult.ok(session);
			} else {
				
				System.out.println("User Controller : line 595");
				return JsonResult.ok(session);
				
			}
			
		} else { 
			return JsonResult.errorMsg("Don't have this user");
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/update")
	public ResponseEntity<JsonResult> updateUser(
			@RequestBody JsonNode jsonNode,HttpSession session) throws Exception, Throwable{
		Long id = jsonNode.get("id") == null ? 0L : jsonNode.get("id").asLong();
		System.out.println("User Controller : line 625 id = "+ id);
		Optional<User> findOldUser = userService.findById(id);
		System.out.println("User Controller : line 627  findOldUser.isPresent() = "+ findOldUser.isPresent());
		if(findOldUser.isPresent()) {
			User return_user = findOldUser.get();
			
			//Check User Group 
			Long userGroupId = jsonNode.get("userGroup") == null ? findOldUser.get().getUsergroup() : jsonNode.get("userGroup").asLong();
			Optional<UserGroup>UserGroupInfo = userGroupService.findById(userGroupId);
			if(UserGroupInfo.isPresent()) {
				JsonNode alias_node = jsonNode.get("alias");
				String alias = "";
				if(alias_node != null) {
					alias = alias_node.asText();
				}
				return_user.setAlias(alias);
//			System.out.println("aliasPhoto length = "+jsonNode.get("aliasPhoto").asText().length() + "Profile Photo length = "+ jsonNode.get("profilePhoto").asText().length()  );
				
		
				
				if(return_user.getScore() > 2700 ||return_user.getUsergroup() == 5 ) {
					if (jsonNode.get("aliasPhoto").asText().length() >5) {
//						 if (jsonNode.get("aliasPhoto").asText().contains(return_user.getAliasPhotoIsPending().substring(return_user.getAliasPhotoIsPending().lastIndexOf("/") + 1))) {
							if (!jsonNode.get("aliasPhoto").asText().contains(".")) {
							 System.out.println("User Controller : line 599 " + return_user.getAliasPhotoIsPending().substring(return_user.getAliasPhotoIsPending().lastIndexOf("/") + 1));
						 } else {
							 return_user.setAliasPhotoIsPending(jsonNode.get("aliasPhoto").asText().replace("resources/", ""));	
							 System.out.println("User Controller : line 602 :Get Alias Photo Name: " + jsonNode.get("aliasPhoto").asText().replace("resources/", ""));				
						 }
					} else {
						 return_user.setAliasPhoto(jsonNode.get("aliasPhoto").asText());
//						 return_user.setAliasPhotoIsPending(null);
//						 return_user.setAliasPhotoPendingDate(null);
						 System.out.println("User Controller : line 606 : "+jsonNode.get("aliasPhoto").asText() );
					}
					
					
					if (jsonNode.get("profilePhoto").asText().length() >5) {
//						if (jsonNode.get("profilePhoto").asText().contains(return_user.getProfilePhotoIsPending().substring(return_user.getProfilePhotoIsPending().lastIndexOf("/") + 1))) {
						if (!jsonNode.get("profilePhoto").asText().contains(".")) {
//							System.out.println("User Controller : line 612 " + return_user.getProfilePhotoIsPending().substring(return_user.getProfilePhotoIsPending().lastIndexOf("/") + 1));
						}	else {
							return_user.setProfilePhotoIsPending(jsonNode.get("profilePhoto").asText().replace("resources/", "")); 
							System.out.println("User Controller : line 615 "+ jsonNode.get("profilePhoto").asText().replace("resources/", ""));
							String content =return_user.getFullname() +" (" +return_user.getStaffNo() +") has changed his/her icon, please go to Users Management to approve it." ;
							String title = return_user.getFullname() +" (" +return_user.getStaffNo() +") has changed his/her icon";
							String email="yu_sw@csd.gov.hk";
							EmailUtil.sendEmailNoticAdmin(return_user.getFullname() +" (" +return_user.getStaffNo() +") ", content,title,email);
							
						}
					} else {
						return_user.setProfilePhoto(jsonNode.get("profilePhoto").asText());
						System.out.println("User Controller : line 619 "+ jsonNode.get("profilePhoto").asText());
//						 return_user.setProfilePhotoIsPending(null);
//						 return_user.setProfilePhotoPendingDate(null);
					}
					
					
				}else {
			    	System.out.println("User Controller : line 550 = "+jsonNode.get("aliasPhoto").asText() );
			    	 return_user.setAliasPhoto(jsonNode.get("aliasPhoto").asText());
			    	 return_user.setAliasPhotoIsPending(jsonNode.get("aliasPhoto").asText());
			    	 return_user.setProfilePhoto(jsonNode.get("profilePhoto").asText());
			    	 return_user.setProfilePhotoIsPending(jsonNode.get("profilePhoto").asText());
//			    	 return_user.setAliasPhotoIsPending(jsonNode.get("aliasPhoto").asText());	
//			    	 System.out.println("Get Alias Photo Name: " + jsonNode.get("aliasPhoto").asText());
//					 return_user.setProfilePhotoIsPending(jsonNode.get("profilePhoto").asText());
			     }
				
				//return_user.setUsergroup(userGroupId);
//				String profilePhoto = null;
//				profilePhoto=	jsonNode.get("profilePhoto").asText();
//				profilePhoto = profilePhoto.replace("user_profile/", "");
//				String aliasPhoto =  null;
//				aliasPhoto =	jsonNode.get("aliasPhoto").asText();
//				aliasPhoto = aliasPhoto.replace("user_profile/", "");
//				
				
//				if(return_user.getScore()> 2500) {
//					if( aliasPhoto.length() >10) {
//						userProfileService.userCreateIcon(id,  aliasPhoto, 2, return_user.getAliasPhoto());
//					}
//					if (profilePhoto != null) {	
//						userProfileService.userCreateIcon(id, profilePhoto, 1,return_user.getProfilePhoto());
//					}
//				}  
				
					JsonNode is_profilo = jsonNode.get("is_profilo");
					if(is_profilo == null) {
					JsonNode accessRuleList = jsonNode.get("accessRuleList");
					List<Long> access_rule_list = new ArrayList<Long>();
					for(Integer i = 0; i < accessRuleList.size(); i++) {
						access_rule_list.add(accessRuleList.get(i).asLong());
					}
				
					dealWithMultipleAccessRule(id, access_rule_list);
					return_user.setLoginTries(jsonNode.get("loginTries").asLong());
					return_user.setLoginFailAttempt(jsonNode.get("loginTries").asLong());
					return_user.setUsergroup(UserGroupInfo.get().getId());
					//return_user.setEmail(jsonNode.get("email").asText());
					return_user.setLang(jsonNode.get("language").asText());
					return_user.setIsBlogger(jsonNode.get("isBlogger").asInt());
					return_user.setBlogCreate(jsonNode.get("isBlogger").asText());
				}
				userService.updateUserById(return_user);
				
				return JsonResult.ok(jsonNode,session);
			}else {
				return JsonResult.errorMsg("Invalid User Group Info");
			}
		}else {
			return JsonResult.errorMsg("Invalid Request");
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/searchByName/{userId}")
	public ResponseEntity<JsonResult> searchName ( @RequestParam(value="fullname",defaultValue="") String fullname,
												@RequestParam(value="page", defaultValue="1") Integer page,
												@PathVariable Long userId,HttpSession session){
		
		List<User> return_data = new ArrayList<User>();
		List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
		List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
		List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
		return_data.addAll(userService.findByUserNameAndSearch(fullname, page));
		

	
		
		return_data.stream().map(temp_user -> {
			temp_user.setUsergroup(temp_user.getUsergroup());
			
			temp_user.setInstitution(institution_session.stream()
					 .filter((n) -> n.getId().equals(temp_user.getInstitutionId()))
					 .map(InstitutionsModel::getInstName)
					 .collect(Collectors.toList())
					 .get(0));
			temp_user.setSection(section_session.stream()
								 .filter((n) -> n.getId().equals(temp_user.getSectionId()))
								 .map(SectionModel::getSectionName)
								 .collect(Collectors.toList())
								 .get(0));
			temp_user.setRank(rank_session.stream()
							  .filter((n) -> n.getId().equals(temp_user.getRankId()))
							  .map(RanksModel::getRankName)
							  .collect(Collectors.toList())
							  .get(0));
			temp_user.setUsername(null);
			temp_user.setSubstantiveRank(null);
			temp_user.setActiveRank(null);
			temp_user.setDutiesT(null);
			temp_user.setDutiesX(null);
			temp_user.setInstT(null);
			temp_user.setInstX(null);
			temp_user.setSectionT(null);
			temp_user.setSectionX(null);
			temp_user.setNotesAccount(null);
			temp_user.setLang(null);
			temp_user.setEmail(null);
			temp_user.setAccessRuleList(null);
			Integer is_blogger = temp_user != null && temp_user.getBlogCreate() != null && temp_user.getBlogCreate().equals("1") ? 1 : 0;
			temp_user.setIsBlogger(is_blogger);
            return temp_user;
        }).collect(Collectors.toList());
		
	
		Integer getTotalNum = userService.getTotalByName(fullname);

		
		if(return_data.size() == 0) {
			return JsonResult.listTotal("User not Found",null,0,session);
		}else{
			return JsonResult.listTotal("User List Found total in: ",return_data ,getTotalNum,session);
		}
	}
				
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/search/{user_id}")
	public ResponseEntity<JsonResult> search(@RequestParam(value="staffNo", defaultValue="" ) String staffNo, 
											 @RequestParam(value="fullname",defaultValue="") String fullname,
											 @RequestParam(value="userGroup", defaultValue="0") Integer userGroup,
											 @RequestParam(value="institution", defaultValue="") String institution,
											 @RequestParam(value="rank", defaultValue="") String rank,
											 @RequestParam(value="section", defaultValue="") String section,
											 @RequestParam(value="isBlogger", defaultValue="-1") Long isBlogger,
											 @RequestParam(value="page", defaultValue="1") Integer page,
											 @PathVariable Long user_id,
											 HttpSession session
											 ){
		
		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
//			
			List<Long> accessRuleId = (List<Long>) session.getAttribute("user_access_rule_session");
			if(accessRuleId == null) {
				return JsonResult.errorMsg("No valid user list");
			}else {
				//TODO Check Admin Role
				Integer is_admin = common.checkAdmin(user,session);
				Integer is_special = 0;
				if(379 == user_id|| 4715 ==user_id || 562 ==user_id || 6865 ==user_id ) {
					is_special=1;
				}
				
				
				//TODO Need to clearfy how to use access rule here
				accessRuleId.clear();
				accessRuleId.add(0L);
				institution= institution.toUpperCase();
				List<User> return_data = new ArrayList<User>();
				List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
				List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
				List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
				
				return_data.addAll(userService.findByUserIdAndSearch(accessRuleId,staffNo,fullname,String.valueOf(userGroup),institution,rank,section,isBlogger,page,is_admin,is_special));
				
				institution = institution.toUpperCase();
				if (institution.contains("RESIGN")) {
					
					return_data.stream().map(temp_user -> {
						temp_user.setUsergroup(temp_user.getUsergroup());
						
						temp_user.setInstitution("RESIGN");
						
						temp_user.setSection("RESIGN");
						temp_user.setRank(rank_session.stream()
								  .filter((n) -> n.getId().equals(temp_user.getRankId()))
								  .map(RanksModel::getRankName)
								  .collect(Collectors.toList())
								  .get(0));
						
						temp_user.setUsername(null);
						temp_user.setSubstantiveRank(null);
						temp_user.setActiveRank(null);
						temp_user.setDutiesT(null);
						temp_user.setDutiesX(null);
						temp_user.setInstT(null);
						temp_user.setInstX(null);
						temp_user.setSectionT(null);
						temp_user.setSectionX(null);
						temp_user.setNotesAccount(null);
						temp_user.setLang(null);
						temp_user.setEmail(null);
						String sec = temp_user.getSectionR();
						System.out.println("User Section R  = " + sec);
						temp_user.setSectionR(sec);
						temp_user.setAccessRuleList(null);
						Integer is_blogger = temp_user != null && temp_user.getBlogCreate() != null && temp_user.getBlogCreate().equals("1") ? 1 : 0;
						temp_user.setIsBlogger(is_blogger);
			            return temp_user;
			        }).collect(Collectors.toList());
					
				} else {
				
				return_data.stream().map(temp_user -> {
					temp_user.setUsergroup(temp_user.getUsergroup());
					
					
					if(temp_user.getInstitutionId() == 99999L || temp_user.getIsDeleted() ==1 ) {
						temp_user.setInstitution("RESIGN");
						
						temp_user.setSection("RESIGN");
					} else {
					temp_user.setInstitution(institution_session.stream()
							 .filter((n) -> n.getId().equals(temp_user.getInstitutionId()))
							 .map(InstitutionsModel::getInstName)
							 .collect(Collectors.toList())
							 .get(0));
					
					temp_user.setSection(section_session.stream()
										 .filter((n) -> n.getId().equals(temp_user.getSectionId()))
										 .map(SectionModel::getSectionName)
										 .collect(Collectors.toList())
										 .get(0));
					}
					temp_user.setRank(rank_session.stream()
									  .filter((n) -> n.getId().equals(temp_user.getRankId()))
									  .map(RanksModel::getRankName)
									  .collect(Collectors.toList())
									  .get(0));
					
					temp_user.setLoginTries(temp_user.getLoginFailAttempt());
					System.out.println("User Login Tries = " + temp_user.getLoginTries() +  " User Login Fail = "+temp_user.getLoginFailAttempt() );
					temp_user.setUsername(null);
					temp_user.setSubstantiveRank(null);
					temp_user.setActiveRank(null);
					temp_user.setDutiesT(null);
					temp_user.setDutiesX(null);
					temp_user.setInstT(null);
					temp_user.setInstX(null);
					temp_user.setSectionT(null);
					temp_user.setSectionX(null);
					temp_user.setNotesAccount(null);
					temp_user.setLang(null);
					temp_user.setEmail(null);
					String sec = temp_user.getSectionR();
					System.out.println("User Section R  = " + sec);
					temp_user.setSectionR(sec);
					temp_user.setAccessRuleList(null);
					Integer is_blogger = temp_user != null && temp_user.getBlogCreate() != null && temp_user.getBlogCreate().equals("1") ? 1 : 0;
					temp_user.setIsBlogger(is_blogger);
		            return temp_user;
		        }).collect(Collectors.toList());
			}
				Integer getTotalNum = userService.getTotal(accessRuleId,staffNo,fullname,String.valueOf(userGroup),institution,rank,section,isBlogger,page,is_admin);
				
//				for(int i = 0; i < return_data.size(); i++) {
//					
//					System.out.println(return_data.get(i));
//				}
				
				if(return_data.size() == 0) {
					return JsonResult.listTotal("User not Found",null,0,session);
				}else{
					return JsonResult.listTotal("User List Found total in: ",return_data ,getTotalNum,session);
				}
				//return JsonResult.ok(accessRuleId);
			}
		}

	}
	
	
	@RequestMapping("/upload/{userId}")
    public ResponseEntity<JsonResult> uploadGallery(@PathVariable Long userId,@RequestParam("file") MultipartFile file,
    		HttpSession session,
    												@RequestParam(value="type", defaultValue="profile") String type) {
		Optional<User> user = userService.findById(userId);
		//System.out.println(userId);
		
		if(user.isPresent()) {
			String store_to_type = type.equals("profile") ? "profile" : "alias";
			User new_detail = userService.storeToUserFolder(file, user.get(),store_to_type);
			
			if(type.equals("profile")) {
				return JsonResult.ok(new_detail.getProfilePhotoIsPending(),"Profile Photo Upload Successfully",session);
			}else {
				return JsonResult.ok(new_detail.getAliasPhotoIsPending(),"Alias Photo Upload Successfully",session);
			}
			
			//return JsonResult.ok(return_blogGallery,"Gallery Created");
		}else{
			//return JsonResult.errorMap(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
			return JsonResult.errorMsg("Invalid Request");
		}
    }
	
	
	private void dealWithMultipleAccessRule(Long userId, List<Long> access_rule_list) {
		//TODO Clear Old Data (Old Functions, no checking of existed)
		userService.deleteResourceAccessRule(userId);
		List<Long> user_access_rule_in_DB = userService.getAll(userId).stream().map(
					(uar) -> {return uar.getAccessRuleId();}
				).collect(Collectors.toList());
		List<UserAccessRule> toSave = access_rule_list.stream().filter(
						(n) -> !user_access_rule_in_DB.contains(n)
				).map(
						(uar) -> {
							UserAccessRule new_user_access_rule = new UserAccessRule();
							new_user_access_rule.setAccessRuleId(uar);
							new_user_access_rule.setIsDeleted(0);
							new_user_access_rule.setUserId(userId);
							return new_user_access_rule;
						}
				).collect(Collectors.toList());
		if(toSave != null && toSave.size() != 0) {
			userService.saveAllAccessRule(toSave);
		}
		List<Long> toUpdate = access_rule_list.stream().filter(
						(n) -> user_access_rule_in_DB.contains(n)
				).collect(Collectors.toList());
		if(toUpdate != null && toUpdate.size() != 0) {
			userService.updateAllAccessRule(toUpdate,userId);
		}
		
		return ;
	}
	

	@RequestMapping("/get/{userId}")
	public ResponseEntity<JsonResult> getUserGroup(@PathVariable Long userId,HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		//System.out.println("user Id "+ session.getAttribute("user_id"));
		//System.out.println("Session "+user_check.get("user_id"));
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			//User user = (User) session.getAttribute("user_session");
			List<Object []> return_list = userService.getUserGroupAndInst(Long.parseLong(user_check.get("user_id")));
			HashMap<String,Object> for_return = new HashMap<String,Object>();
			//for (Integer i=0; i<return_list.size();i++) {
			Object[] data =(Object [])return_list.get(0) ;
				for_return.put("groupId",data[0]);
				for_return.put("instId",data[1]);
				
			//}
		
		return JsonResult.ok(for_return,session);
		}

	}
	
	@RequestMapping("/dogs/update/{userId}")
	public ResponseEntity<JsonResult> updateUserDog(@PathVariable Long userId, @RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String,String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			UserDog userDog = userDogService.getDog(userId);
			String updateDog = jsonNode.get("userdog").asText();
			userDog.setUserDog(updateDog);
//			userDogService.saveLevelDog(userDog);
			return JsonResult.ok(userDogService.saveLevelDog(userDog),session);
		}
	}
	
	
	
	
	
	
	@RequestMapping("/dogs/{userId}")
	public ResponseEntity<JsonResult> getUserDog(@PathVariable Long userId, HttpSession session){
		HashMap<String, String> user_check = common.checkUser(userId, session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else { 
			User user = (User) session.getAttribute("user_session"); 
			UserDog userDog = new UserDog();
			userDog = userDogService.getDog(userId);
			if (user.getScore()>499) {
				String dog = "";
				dog=userDogService.getUserDog(userId);
				System.out.println("");
				if(dog=="" || dog==null) {
					 Random rand = new Random();
					  int upperbound = 5;
					  int int_random = //rand.nextInt(upperbound); 
					  
					  (int)(Math.random()*4);  
					  System.out.println("User Controller Line 1070 :int_random = "+int_random);
					  switch (int_random) {
					  	case 1:
					  		userDog.setCreatedAt(new Date());
					  		userDog.setCreatedBy(userId);
					  		userDog.setUserDog("A");
					  		userDog.setDogLevel1("A");
						  break;
					  	case 2:
					  		 userDog.setCreatedAt(new Date());
							 userDog.setCreatedBy(userId);
							 userDog.setUserDog("B");
							 userDog.setDogLevel1("B");
							break;
					  	case 3: 
					  		 userDog.setCreatedAt(new Date());
							 userDog.setCreatedBy(userId);
							 userDog.setUserDog("C");
							 userDog.setDogLevel1("C");
							break;
					  	case 0: 
					  		 userDog.setCreatedAt(new Date());
							 userDog.setCreatedBy(userId);
							 userDog.setUserDog("D");
							 userDog.setDogLevel1("D");
							break;
					  }
					userDogService.saveLevelDog(userDog);
					
					HashMap<String, String> return_data = new HashMap<>();
					return_data.put("userdog", userDog.getUserDog());
					HashMap<String, List<String>> return_data2 = new HashMap<>();
					List<String> AbleDog =new ArrayList<>();
					AbleDog.add(userDog.getUserDog());
					return_data2.put("abledog", AbleDog);
					return JsonResult.ok(return_data,return_data2,session);
				} else {
					  if(user.getScore()> 1000) {
						 if (userDog.getDogLevel2() =="" ||userDog.getDogLevel2()==null) {
							 Random rand = new Random();
							  int upperbound = 3;
							  int int_random =  (int)(Math.random()*3);  
							  switch (int_random) {
							  case 0:
							  		userDog.setCreatedAt(new Date());
							  		userDog.setCreatedBy(userId);
							  		if(userDog.getDogLevel1() =="A") {
							  			userDog.setDogLevel2("D");
							  		} else {
							  			userDog.setDogLevel2("A");
							  		}
								  break;
							  	case 1:
							  		 userDog.setCreatedAt(new Date());
									 userDog.setCreatedBy(userId);
									 if(userDog.getDogLevel1() =="B") {
										 userDog.setDogLevel2("D");
									 } else {
										 userDog.setDogLevel2("B");
									 }
									break;
							  	case 2: 
							  		 userDog.setCreatedAt(new Date());
									 userDog.setCreatedBy(userId);
									 if(userDog.getDogLevel1() =="C") {
										 userDog.setDogLevel2("D");
									 } else {
										 userDog.setDogLevel2("C"); 
									 }
									break;
							  
							  }
							 
						 }
						  
					  } 
					  if(user.getScore()> 1800) {
							 if (userDog.getDogLevel3() =="" ||userDog.getDogLevel3()==null) {
								 Random rand = new Random();
								  int upperbound = 2;
								  int int_random =  (int)(Math.random()*2);  
								  switch (int_random) {
								  case 0:
								  		userDog.setCreatedAt(new Date());
								  		userDog.setCreatedBy(userId);
								  		if(userDog.getDogLevel2() =="A"||userDog.getDogLevel2() =="C") {
								  			userDog.setDogLevel3("D");
								  		} else {
								  			if(userDog.getDogLevel2() =="A") {
								  				userDog.setDogLevel3("C");
								  			} else {
								  				userDog.setDogLevel3("A");
								  			}
								  		}
									  break;
								  	case 1:
								  		 userDog.setCreatedAt(new Date());
										 userDog.setCreatedBy(userId);
										 if(userDog.getDogLevel2() =="B" || userDog.getDogLevel2() =="D" ) {
											 userDog.setDogLevel3("C");
										 } else {
											 if(userDog.getDogLevel2() =="B" ) {
												 userDog.setDogLevel3("D");
											 } else {
												 userDog.setDogLevel3("B");
											 }
										 }
										break;
								  }
								 
							 }
							  
						  }
					  if(user.getScore()>2700) {
						  if(userDog.getDogLevel4() =="" || userDog.getDogLevel4() == null) {
							  List<String> exist_dog = new ArrayList<>();
							  exist_dog.add(userDog.getDogLevel1());
							  exist_dog.add(userDog.getDogLevel2());
							  exist_dog.add(userDog.getDogLevel3());
							  List<String> rest_dog = new ArrayList<>();
							  rest_dog.add("A");
							  rest_dog.add("B");
							  rest_dog.add("C");
							  rest_dog.add("D");
							  
							  
							  List<String> difference2 = rest_dog.stream()
									    .filter(aObject -> ! exist_dog.contains(aObject))
									    .collect(Collectors.toList());
							  System.out.println("User Controller, line 1194 : dog left = "+ difference2);
							  userDog.setCreatedAt(new Date());
						  		userDog.setCreatedBy(userId);
						  		 userDog.setDogLevel4(difference2.get(0));
						  }
					  }
					  
					  userDogService.saveLevelDog(userDog);
					  
					  
					  
					  List<String> AbleDog =new ArrayList<>();
					  HashMap<String, List<String>> return_data2 = new HashMap<>();
					  if(user.getScore()> 1000) {
						  AbleDog.add(userDog.getDogLevel2()) ;
					  }
					  if(user.getScore()> 1800) {
						  AbleDog.add(userDog.getDogLevel3()) ;
					  }
					  if(user.getScore()> 500) {
						  AbleDog.add(userDog.getDogLevel1()) ;
					  }
					  if(user.getScore()>2700) {
						  AbleDog.add(userDog.getDogLevel4());
					  }
					
					  
					HashMap<String, String> return_data = new HashMap<>();
					return_data.put("dog", dog);
					return_data2.put("abledog", AbleDog);
					return JsonResult.ok(return_data,return_data2,session);
					
				}
				
				
			} else {
				return JsonResult.ok(session);
			}
			
		}
		
	}
	
	
	
	
}

package com.hkmci.csdkms.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.ElearningCategory;
import com.hkmci.csdkms.entity.ElearningCourse;
import com.hkmci.csdkms.entity.ElearningQuestion;
import com.hkmci.csdkms.entity.ElearningQuiz;
import com.hkmci.csdkms.entity.SpecialCollection;
import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.PageNameModel;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.repository.BlogAssistantRepository;
import com.hkmci.csdkms.security.JwtTokenProvider;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.InstitutionService;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.PageNameService;
import com.hkmci.csdkms.service.RanksService;
import com.hkmci.csdkms.service.SectionService;
import com.hkmci.csdkms.service.UserService;
//
//import lotus.domino.Database;
//import lotus.domino.Document;
//import lotus.domino.NotesException;
//import lotus.domino.NotesFactory;
//import lotus.domino.NotesThread;
//import lotus.domino.Session;

@Component
public class Common {

	private final UserService userService;
	
	private String DBPATH_SAMPLEDATA_NSF = "D:\\csdkms\\Data\\Lotus\\MB_System_Administrator.nsf";
	
	private final LogMessageProperties logMessage;

	private final AccessRuleService accessRuleService;
	
	private final BlogAssistantRepository blogAssistantRepository;
	
    private final LogService logger;
    
    @Autowired
    private RanksService ranksService;
    
    @Autowired
    private InstitutionService instService;
    
    @Autowired
    private SectionService sectService;
    
    @Autowired
    private PageNameService pageNameService;

    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    public Common(UserService theUserService,LogMessageProperties theLogMessage, LogService theLogger,
    		AccessRuleService theAccessRuleService, BlogAssistantRepository theBlogAssistantRepository) {
        this.userService = theUserService;
        this.logMessage = theLogMessage;
        this.accessRuleService = theAccessRuleService;
        this.blogAssistantRepository = theBlogAssistantRepository;
        this.logger = theLogger;
    }
    
    public String specialCollectionAccessRuleSql(Integer accessRuleIdSize) {
    	String sql = "";
    	for (int i = 0; i < accessRuleIdSize; i++) {
    	    if (i > 0) {
    	        sql += " OR ";
    	    }
    	    sql += " FIND_IN_SET(:accessRuleId" + i + ", b.access_rule_id) ";
    	}

    	
    	return sql;
    }

	public Date textToDate(String dateOfTest) {
		Date returnDate;
		//System.out.println(dateOfTest.length());
		if(dateOfTest.length() > 19) {
			dateOfTest = dateOfTest.substring(0, 18);
		} 
		//System.out.println(dateOfTest);
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			returnDate = ymdFormat.parse(dateOfTest);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
			returnDate = null;
			//e.printStackTrace();
		}
		return returnDate;
	}
	
	
	public Date textToDateDate(String dateOfTest) {
		//System.out.println("Date of Test = "+dateOfTest);
		Date returnDate;
		//System.out.println(dateOfTest.length());
		if(dateOfTest.length() > 10) {
			dateOfTest = dateOfTest.substring(0, 10);
			//System.out.println("Date of Test 2 "+ dateOfTest);
		} 
		//System.out.println(dateOfTest);
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			returnDate = ymdFormat.parse(dateOfTest);
			//System.out.println("Retrun Date  "+returnDate);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
			returnDate = null;
			//e.printStackTrace();
		}
		return returnDate;
	}
	
	
	
	
	
	public Date blogDateTextToDate(String dateOfTest) {
		Date returnDate;
		String new_dateOfTest = "";
		//System.out.println(dateOfTest.length());
		if(dateOfTest.length() > 10) {
			String[] dateOfTest_array = dateOfTest.substring(10).split("/");
			String yearString = dateOfTest_array[2];
			String monthString = dateOfTest_array[1];
			String dayString = dateOfTest_array[0];
			new_dateOfTest = yearString + "-" + monthString + "-" + dayString;
		}
		//System.out.println(dateOfTest);
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			returnDate = ymdFormat.parse(new_dateOfTest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			returnDate = null;
			//e.printStackTrace();
		}
		return returnDate;
	}
	
	

	
	
	public void  Storelog( HttpSession session,User user, Integer channel) {
		//Integer userScore = logger.getUserScore(user.getId());
		System.out.println("User Score( In Store Log) - "+user.getScore());
		List<User> user_data = userService.allInSession();
		List<User> exist_user = userService.allExistInSession();
//		System.out.println("Common , line 162 : user_data size = "+ user_data );
		session.setAttribute("user_list", user_data);
		session.setAttribute("exist_user", exist_user);
//      new add score rule not more than 20
//		System.out.println("Store Log login--");
		Log log = logger.login(user, 0L, "", "Success",channel);

//      
		Integer todayScore = logger.getUserTodayScore(user.getId());
//		System.out.println("User Today Score ( In Store Log) - "+todayScore);
		
		Date queryDate = new Date();
		Calendar calendar = Calendar.getInstance(); //Calendar
		calendar.setTime(new Date());//Calendar's Current Date
		calendar.add(Calendar.DAY_OF_YEAR,0);  
		queryDate = calendar.getTime();   
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date yesterday;
		try {
			yesterday = ymdFormat.parse(ymdFormat.format(new Date(queryDate.getTime())));
		} catch (Exception e) {
			yesterday = null;
		}
		if(user.getLoginLastTry()== null) {
			todayScore=0;
		}else {
		if(!user.getLoginLastTry().after(yesterday)) {
//			System.out.println("Yesterday"+ user.getLoginLastTry());
			todayScore=0;
		}
		}
		
		Integer addScore = 30 - todayScore ;
		System.out.println("Common Line 194 : Channel = "+channel +"  Add score = " + addScore);
		if(addScore<1) {
		
		ScoreLog scoreLog = new ScoreLog(log.getId(),user,0);
		logger.saveScoreLog(scoreLog);
		user.setTodayScore(todayScore + 0);
		session.setAttribute("user_session", user);
		} else {
			if (channel ==4 && addScore >2) {
				
					ScoreLog scoreLog = new ScoreLog(log.getId(),user,2);
					logger.saveScoreLog(scoreLog);
			
					user.setTodayScore(todayScore + 2 );
					session.setAttribute("user_session", user);
			} else {
				
				ScoreLog scoreLog = new ScoreLog(log.getId(),user,1);
				logger.saveScoreLog(scoreLog);
			
				user.setTodayScore(todayScore + 1 );
				session.setAttribute("user_session", user);
			}
			
		}
		
		user.setLoginLastTry(new Date());
		 userService.addUser(user);
//		System.out.println("User last login time = "+ user.getLoginLastTry());
	}
	
	public void storeQuestionSession( String question,HttpSession session) {
		
		session.setAttribute("question", question);
	}
	

	
	public void checkApiSession(String staffNo, HttpSession session, Integer is_remote) {

		Object user_session = session.getAttribute("user_session");
		Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		Object channel_session = session.getAttribute("channel");
		
		Integer channel = channel_session == null ? is_remote : (Integer) channel_session;
		System.out.println("Common, line 240 ; channel " + channel);
		Date queryDate = new Date();
		Calendar calendar = Calendar.getInstance(); //Calendar
		calendar.setTime(new Date());//Calendar's Current Date
		calendar.add(Calendar.DAY_OF_YEAR,0);  
		queryDate = calendar.getTime();   
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date yesterday;
		try {
			yesterday = ymdFormat.parse(ymdFormat.format(new Date(queryDate.getTime())));
		} catch (Exception e) {
			yesterday = null;
		}
        if (user_session == null) {
        	System.out.println("Common Line 251 : User session is null , staff_no = "+staffNo);
        	User user = userService.findByStaffNo(staffNo);
        	Integer TotalScore = logger.getUserScore(user.getId());
        	List<Long> accessRuleId = new ArrayList<>();
            if (user_access_rule_session == null) {
            	Long section = user.getSectionId();
    			Long institution = user.getInstitutionId();
    			Long rank = user.getRankId();
    			Long user_group = user.getUsergroup();
    			accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
    			
                if(accessRuleId == null || accessRuleId.size() == 0 ) {
    				logger.login(user, 0L, logMessage.No_Access_Right(), "Failure", channel);
    				return ;
    			}else{
    				if(user.getLoginLastTry()== null ) {
    					
    						user.setScore(TotalScore+1);
    					
    			    } else {
    			    if(user.getTodayScore()<30 || !user.getLoginLastTry().after(yesterday) ) {
    			    	
    			    		user.setScore(TotalScore+1);
    			    	
    					}  else {
    						user.setScore(TotalScore);
    					}
    			    }
    				Long userLoginTimes = logger.getUserLoginTimes(user.getId());
                    session.setAttribute("user_access_rule_session", accessRuleId);
                    checkRankSession(session);
                    session.setAttribute("channel", channel);
                    user.setLoginTries(userLoginTimes);
                    session.setAttribute("user_session", user);
                    user.setLoginLastTry(user.getLoginLastTry());
                    userService.addUser(user);
    				new Thread(() ->{
    					Storelog(session,user,channel);
    				}).start();
    				return ;
    				
    			
    			}
            }else {
            
            	return ;
            }
        }else {
        	User user_session_info = (User) user_session;
        	if(!user_session_info.getStaffNo().equals(staffNo) && !user_session_info.getStaffNo().equals("99998") ) {
				logger.login(user_session_info, 0L, logMessage.Try_To_Access_Others()+staffNo.toString(), "Failure" ,channel);
			}
            return ;
        }
	}
	
	
	
	
	
	
	public void checkApiSession22(String staffNo, HttpSession session, Integer is_remote) {
		//System.out.println("Check API Session, Staff No =  "+ staffNo);
		Object user_session = session.getAttribute("user_session");
		Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		Object channel_session = session.getAttribute("channel");
		
		Integer channel = channel_session == null ? is_remote : (Integer) channel_session;
	   //	Integer channel = is_remote;
		////System.out.println("user session = "+ user_session);
	
        if (user_session == null) {
        	User user = userService.findByStaffNo(staffNo);
        
        	// System.out.println("User table last login time 1 = "+ user.getLoginLastTry());
        	
        	List<Long> accessRuleId = new ArrayList<>();
            if (user_access_rule_session == null) {
            	//System.out.println(" User info: "+ user.getSectionId());
    			Long section = user.getSectionId();
    			//System.out.println(" User Inst: "+ user.getInstitutionId());
    			Long institution = user.getInstitutionId();
    			//System.out.println("User Rank: "+ user.getRankId());
    			Long rank = user.getRankId();
    			Long user_group = user.getUsergroup();
    			//System.out.println("Check API Session User group = "+user_group);
    			//Long accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
    			accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
    			
    			session.setAttribute("user_session", user);

    			//System.out.println("-----  can get access Rule Service get By User ------"+ accessRuleId);
            }
        }
	}
	
	
	

	
	public HashMap<String, String> checkUser(Long user_id, HttpSession session) {
		
		//Get Session  
		//HashMap<String, Object> session_data = getSessions(request);
		Object user_session = session.getAttribute("user_session");
		
		Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		Object channel_session = session.getAttribute("channel");
		Integer channel = channel_session == null ? 1 : (Integer) channel_session;
		System.out.println("Check User Channel = "+ channel);
		//TODO Test only
		//checkRankSession(session);
		
		HashMap<String, String> return_data = new HashMap<>();
        if (user_session == null) {
        	Optional<User> user = userService.findById(user_id);
        	//System.out.println("USER SESSION is null");
        	if(user.isPresent()) {
                List<Long> accessRuleId = new ArrayList<>();
                if (user_access_rule_session == null) {
                	//System.out.println(" User info: "+ user.get().getSectionId());
        			Long section = user.get().getSectionId();
        			//System.out.println(" User Inst: "+ user.get().getInstitutionId());
        			Long institution = user.get().getInstitutionId();
        			//System.out.println("User Rank: "+ user.get().getRankId());
        			Long rank = user.get().getRankId();
        			Long user_group= user.get().getUsergroup();
        			String staff_no = user.get().getStaffNo();
        			//Long accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
        			accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
        			
                    if(accessRuleId == null || accessRuleId.size() == 0) {
                    	return_data.put("user_id", "");
        				return_data.put("user_section", "");
        				return_data.put("user_institution", "");
        				return_data.put("user_rank", "");
        				return_data.put("access_channel","");
        				return_data.put("msg", "No access right for system");
        				//System.out.println("No access right for system");
        				logger.login(user.get(), 0L, logMessage.No_Access_Right(), "Failure",channel);
        				return return_data;
        			}else{
        				return_data.put("staff_no", String.valueOf(staff_no));
        				return_data.put("user_id", String.valueOf(user_id));
        				return_data.put("user_section", String.valueOf(section));
        				return_data.put("user_institution", String.valueOf(institution));
        				return_data.put("user_rank", String.valueOf(rank));
        				return_data.put("access_channel",String.valueOf(channel));
        				return_data.put("user_group", String.valueOf(user_group));
        				return_data.put("msg", "");
        				//System.out.println("-- Can get into to system --");
        				
        				//System.out.println("No user session，set user=" + user.get());
                        session.setAttribute("user_session", user.get());
        				//System.out.println("No access_rule session，set access_rule=" + accessRuleId);
                        session.setAttribute("user_access_rule_session", accessRuleId);
                        
                        logger.login(user.get(), 0L, "", "Success",channel);
        				return return_data;
        			}
        			//System.out.println("-----  can get access Rule Service get By User ------"+ accessRuleId);
                }else {
                	//System.out.println("Find access_rule session" + user_access_rule_session);
                	User user_session_info = user.get();
                	//System.out.println("Staff No IN Common = "+ user_session_info.getStaffNo());
                	return_data.put("staff_no",String.valueOf(user_session_info.getStaffNo()));
                	return_data.put("user_id",String.valueOf(user_session_info.getId()));
                	return_data.put("user_section", String.valueOf(user_session_info.getSectionId()));
        			return_data.put("user_institution", String.valueOf(user_session_info.getInstitutionId()));
        			return_data.put("user_rank", String.valueOf(user_session_info.getRankId()));
    				return_data.put("access_channel",String.valueOf(channel));
    				return_data.put("user_group", String.valueOf(user_session_info.getUsergroup()));
        			return_data.put("msg", "");
        			//System.out.println("-- Can get into to system");
                	return return_data;
//		                	accessRuleId = user_access_rule_session.;
                }
    		}else{
    			return_data.put("user", null);
    			return_data.put("msg", "Invalid User Request");
    			//System.out.println("--  Invalid User Request -- ");
    			return return_data;
    		}
        }else  {
        	//System.out.println("User session is not null ");
        	User user_session_info = (User) user_session;
        	//System.out.println("User Session exist : ID = "+ user_session_info.getId());
            //System.out.println("Find user session: " + user_session_info.getId());
        	return_data.put("user_id",String.valueOf(user_session_info.getId()));
        	return_data.put("staff_no", user_session_info.getStaffNo());
            return_data.put("user_section", String.valueOf(user_session_info.getSectionId()));
			return_data.put("user_institution", String.valueOf(user_session_info.getInstitutionId()));
			return_data.put("user_rank", String.valueOf(user_session_info.getRankId()));
			return_data.put("access_channel",String.valueOf(channel));
			return_data.put("user_group", String.valueOf(user_session_info.getUsergroup()));
			return_data.put("msg", "");
			//System.out.println("Find access_rule session" + user_access_rule_session);
			System.out.println("Staff Number = "+ return_data.get("staff_no"));
			//System.out.println("-- Can get into to system");
//			if(!user_session_info.getId().equals(user_id)||  !(return_data.get("staff_no")=="99998")) {
//				logger.login(user_session_info, 0L, logMessage.Try_To_Access_Others() + "user id = "+ user_id, "Failure", channel);
//			}
			//System.out.println("After loggin");
            return return_data;
        }
	}
	
	public String checkEnviroment() {
		String springProfilesActive="";
		try {
            // 加載XML文件
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("csdkms.xml");

            // 獲取根元素
            Element root = document.getDocumentElement();

            // 獲取arguments元素
            NodeList argumentsList = root.getElementsByTagName("arguments");
            Element argumentsElement = (Element) argumentsList.item(0);

            // 獲取-Dspring.profiles.active的值
            springProfilesActive = argumentsElement.getTextContent().split("-Dspring.profiles.active=")[1].split(" ")[0];
            System.out.println("-Dspring.profiles.active: " + springProfilesActive);
            return springProfilesActive;
            
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
	}
	
	public Integer checkAdmin(User user, HttpSession session) {
		// TODO Admin Specification
//		HttpSession session = request.getSession();
		if(checkEnviroment().contentEquals("developer")) {
			return 1;
		}
		Object user_session = session.getAttribute("user_session");
		Integer is_admin = (Integer) session.getAttribute("is_admin");
		if(user_session == null) {
			return 0;
		}else {
			if(is_admin == null) {
				List<Long> admin_list = userService.getAdminList();
				if(admin_list.contains(user.getId()) || user.getUsergroup() ==6 || user.getUsergroup()==5|| user.getUsergroup()==4 ) {
					session.setAttribute("is_admin", 1);
					//System.out.println("Set Admin Session: " + 1);
					return 1;
				}else{
					session.setAttribute("is_admin", 0);
					//System.out.println("Set Admin Session: " + 0);
					return 0;
				}
			}else {
				//System.out.println("Is Admin Session: " + is_admin);
				return is_admin;
			}
		}
	}

	
	public Integer checkBlogCreator(User user_from_session, HttpSession session, JsonNode jsonNode) {
		// TODO Blog Creator Specification
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_from_session == null) {
			//No valid user in session
			return 0;
		}else {
			Long created_by = jsonNode.get("createdBy").asLong();
			Long originalCreator = jsonNode.get("originalCreator").asLong();
			
			//Check assistant_ids session
			List<Long> assistant_ids = checkAssistantId(session, user_from_session);
			
			//Check User valid
			if( !user_from_session.getId().equals(created_by) ) {
				//System.out.println("User try to create blog through other account.");
				logger.createBlog(user_from_session, 0L, logMessage.Create_Blog_With_Others(), "Failure",channel);
				return 0;
			}else {
				if( assistant_ids.contains(originalCreator) || originalCreator.equals(user_from_session.getId())) {
					//System.out.println("Can create blog.");
					
					return 1;
				}else {
					//System.out.println("Invalid assistant usage.");
					logger.createBlog(user_from_session, 0L, logMessage.Try_To_Use_Invalid_Assistant(), "Failure",channel);
					//TODO Need to concate with DB
					return 1;
				}
			}
		}
	}
	
	public Integer checkNewsCorner2Creator(User user_from_session, HttpSession session, JsonNode jsonNode) {
		// TODO NewsCorner2 Creator Specification
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_from_session == null) {
			//No valid user in session
			return 0;
		}else {
			Long created_by = jsonNode.get("createdBy").asLong();
			Long originalCreator = jsonNode.get("originalCreator").asLong();
			
			//Check assistant_ids session
			List<Long> assistant_ids = checkAssistantId(session, user_from_session);
			
			//Check User valid
			if( !user_from_session.getId().equals(created_by) ) {
				//System.out.println("User try to create NewsCorner2 through other account.");
				logger.createNewsCorner2(user_from_session, 0L, logMessage.Create_Blog_With_Others(), "Failure",channel);
				return 0;
			}else {
				if( assistant_ids.contains(originalCreator) || originalCreator.equals(user_from_session.getId())) {
					//System.out.println("Can create NewsCorner2.");
					
					return 1;
				}else {
					//System.out.println("Invalid assistant usage.");
					logger.createNewsCorner2(user_from_session, 0L, logMessage.Try_To_Use_Invalid_Assistant(), "Failure",channel);
					//TODO Need to concate with DB
					return 1;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Long> checkAssistantId(HttpSession session, User user) {
		List<Long> temp_assistant_id = new ArrayList<>();
		Object assistant_ids = session.getAttribute("assistant_id");
		User session_user = (User) session.getAttribute("user_session");
		if(session_user.getStaffNo().equals(user.getStaffNo())) {
			if(assistant_ids == null) {
				//No valid assistant_ids in session
				List<Long> assistant_id_in_db = blogAssistantRepository.findAssistantIdByUserIdAndIsDeleted(user.getId(), 0);
				//System.out.println("assistant_id_in_db: " + assistant_id_in_db.size());
				if(assistant_id_in_db == null || assistant_id_in_db.size() == 0 ) {
					temp_assistant_id.add(0L);
					session.setAttribute("assistant_id", temp_assistant_id);
					return temp_assistant_id;
				}else {
					temp_assistant_id.addAll(assistant_id_in_db);
					session.setAttribute("assistant_id", temp_assistant_id);
					return temp_assistant_id;
				}
			}else {
				return (List<Long>) assistant_ids;
			}
		}else {
			//Searching staffNo
			List<Long> assistant_id_in_db = blogAssistantRepository.findAssistantIdByUserIdAndIsDeleted(user.getId(), 0);
			//System.out.println("assistant_id_in_db: " + assistant_id_in_db);
			if(assistant_id_in_db == null || assistant_id_in_db.size() == 0 ) {
				temp_assistant_id.add(0L);
				return temp_assistant_id;
			}else {
				temp_assistant_id.addAll(assistant_id_in_db);
				return temp_assistant_id;
			}
		}
		
	}
	
	public Integer checkUserSession(Long userId, HttpSession session) {
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			if(user.getId().equals(userId)) {
				return 1;
			}else {
				//logger.viewBlog(user, 0L, logMessage.Try_To_Access_Others(), "Failure",channel,null);
				logger.login(user, 0L, logMessage.Try_To_Access_Others()+"User id ="+ userId, "Failure", channel);
				return 0;
			}
		}
	}
	
public Integer checkSpecialCollectionUser(JsonNode jsonNode, HttpSession session, SpecialCollection specialCollection, String type) {
		
		JsonNode operator = jsonNode.get("modifiedBy");
		if(operator == null) {
			operator = jsonNode.get("user");
		}
		Long request_user = operator.asLong();
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			Integer is_admin = checkAdmin(user, session);
			if(user.getId().equals(request_user)) {
				if(specialCollection.getCreatedBy().equals(request_user) || specialCollection.getOriginalCreator().equals(user) || is_admin.equals(1)) {
					return 1;
				}else {
					if(type == "update") {
						logger.updateBlog(user, specialCollection.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}else {
						logger.deleteBlog(user, specialCollection.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}
					return 0;
				}
			}else {
				if(type == "update") {
					logger.updateBlog(user, specialCollection.getId(), logMessage.Update_Blog_With_Others(), "Failure",channel);
				}else {
					logger.deleteBlog(user, specialCollection.getId(), logMessage.Update_Blog_With_Others(), "Failure", channel);
				}
				return 0;
			}
		}
	}

	public Integer checkElearningQuizUser(JsonNode jsonNode, HttpSession session, ElearningQuiz elearningQuiz, String type) {
		
		JsonNode operator = jsonNode.get("modifiedBy");
		if(operator == null) {
			operator = jsonNode.get("user");
		}
		Long request_user = operator.asLong();
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			Integer is_admin = checkAdmin(user, session);
			if(user.getId().equals(request_user)) {
				if(elearningQuiz.getCreatedBy().equals(request_user) || is_admin.equals(1)) {
					return 1;
				}else {
					if(type == "update") {
						logger.updateBlog(user, elearningQuiz.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}else {
						logger.deleteBlog(user, elearningQuiz.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}
					return 0;
				}
			}else {
				if(type == "update") {
					logger.updateBlog(user, elearningQuiz.getId(), logMessage.Update_Blog_With_Others(), "Failure",channel);
				}else {
					logger.deleteBlog(user, elearningQuiz.getId(), logMessage.Update_Blog_With_Others(), "Failure", channel);
				}
				return 0;
			}
		}
	}
	
public Integer checkElearningCategoryUser(JsonNode jsonNode, HttpSession session, ElearningCategory elearningCategory, String type) {
		
		JsonNode operator = jsonNode.get("modifiedBy");
		if(operator == null) {
			operator = jsonNode.get("user");
		}
		Long request_user = operator.asLong();
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			Integer is_admin = checkAdmin(user, session);
			if(user.getId().equals(request_user)) {
				if(elearningCategory.getCreatedBy().equals(request_user) || is_admin.equals(1)) {
					return 1;
				}else {
					if(type == "update") {
						logger.updateBlog(user, elearningCategory.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}else {
						logger.deleteBlog(user, elearningCategory.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}
					return 0;
				}
			}else {
				if(type == "update") {
					logger.updateBlog(user, elearningCategory.getId(), logMessage.Update_Blog_With_Others(), "Failure",channel);
				}else {
					logger.deleteBlog(user, elearningCategory.getId(), logMessage.Update_Blog_With_Others(), "Failure", channel);
				}
				return 0;
			}
		}
	}

	public Integer checkElearningQuestionUser(JsonNode jsonNode, HttpSession session, ElearningQuestion elearningQuestion, String type) {
		
		JsonNode operator = jsonNode.get("modifiedBy");
		if(operator == null) {
			operator = jsonNode.get("user");
		}
		Long request_user = operator.asLong();
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			Integer is_admin = checkAdmin(user, session);
			if(user.getId().equals(request_user)) {
				if(elearningQuestion.getCreatedBy().equals(request_user) || is_admin.equals(1)) {
					return 1;
				}else {
					if(type == "update") {
						logger.updateBlog(user, elearningQuestion.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}else {
						logger.deleteBlog(user, elearningQuestion.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}
					return 0;
				}
			}else {
				if(type == "update") {
					logger.updateBlog(user, elearningQuestion.getId(), logMessage.Update_Blog_With_Others(), "Failure",channel);
				}else {
					logger.deleteBlog(user, elearningQuestion.getId(), logMessage.Update_Blog_With_Others(), "Failure", channel);
				}
				return 0;
			}
		}
	}
	
	public Integer checkElearningCourseUser(JsonNode jsonNode, HttpSession session, ElearningCourse elearningCourse, String type) {
		JsonNode operator = jsonNode.get("modifiedBy");
		if(operator == null) {
			operator = jsonNode.get("user");
		}
		Long request_user = operator.asLong();
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			Integer is_admin = checkAdmin(user, session);
			if(user.getId().equals(request_user)) {
				if(elearningCourse.getCreatedBy().equals(request_user) || is_admin.equals(1)) {
					return 1;
				}else {
					if(type == "update") {
						logger.updateBlog(user, elearningCourse.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}else {
						logger.deleteBlog(user, elearningCourse.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}
					return 0;
				}
			}else {
				if(type == "update") {
					logger.updateBlog(user, elearningCourse.getId(), logMessage.Update_Blog_With_Others(), "Failure",channel);
				}else {
					logger.deleteBlog(user, elearningCourse.getId(), logMessage.Update_Blog_With_Others(), "Failure", channel);
				}
				return 0;
			}
		}
	}

	public Integer checkBlogUser(JsonNode jsonNode, HttpSession session, Blog blog, String type) {
		
		JsonNode operator = jsonNode.get("modifiedBy");
		if(operator == null) {
			operator = jsonNode.get("user");
		}
		Long request_user = operator.asLong();
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			Integer is_admin = checkAdmin(user, session);
			if(user.getId().equals(request_user)) {
				if(blog.getCreatedBy().equals(request_user) || blog.getOriginalCreator().equals(user) || is_admin.equals(1)) {
					return 1;
				}else {
					if(type == "update") {
						logger.updateBlog(user, blog.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}else {
						logger.deleteBlog(user, blog.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}
					return 0;
				}
			}else {
				if(type == "update") {
					logger.updateBlog(user, blog.getId(), logMessage.Update_Blog_With_Others(), "Failure",channel);
				}else {
					logger.deleteBlog(user, blog.getId(), logMessage.Update_Blog_With_Others(), "Failure", channel);
				}
				return 0;
			}
		}
	}
	
public Integer checkNewsCorner2User(JsonNode jsonNode, HttpSession session, NewsCorner2 blog, String type) {
		
		JsonNode operator = jsonNode.get("modifiedBy");
		if(operator == null) {
			operator = jsonNode.get("user");
		}
		Long request_user = operator.asLong();
		Object user_session = session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		if(user_session == null ) {
			return 0;
		}else {
			User user = (User) user_session;
			Integer is_admin = checkAdmin(user, session);
			if(user.getId().equals(request_user)) {
				if(blog.getCreatedBy().equals(request_user) || blog.getOriginalCreator().equals(user) || is_admin.equals(1)) {
					return 1;
				}else {
					if(type == "update") {
						logger.updateBlog(user, blog.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}else {
						logger.deleteBlog(user, blog.getId(), logMessage.Try_To_Update_Blog_Without_Permission(), "Failure",channel);
					}
					return 0;
				}
			}else {
				if(type == "update") {
					logger.updateBlog(user, blog.getId(), logMessage.Update_Blog_With_Others(), "Failure",channel);
				}else {
					logger.deleteBlog(user, blog.getId(), logMessage.Update_Blog_With_Others(), "Failure", channel);
				}
				return 0;
			}
		}
	}

	public HashMap<String, String> checkReportUser(Long user_id, HttpSession session) {
		//Get Session  
		//HashMap<String, Object> session_data = getSessions(request);
		Object user_session = session.getAttribute("user_session");
		Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		Object channel_session = session.getAttribute("channel");
		//System.out.println("Channel from session: " + channel_session);
		Integer channel = channel_session == null ? 2 : (Integer) channel_session;
		//System.out.println("Channel Check Report Session - "+ channel);
		session.setAttribute("channel", channel);
		Optional<User> user = userService.findById(user_id);
		HashMap<String, String> return_data = new HashMap<>();
        if (user_session == null) {
        	//System.out.println("User ID from Request: " + user_id);
        
        	if(user.isPresent()) {
        		//System.out.println("User ID: " + user.get().getId());
                List<Long> accessRuleId = new ArrayList<>();
                if (user_access_rule_session == null) {
                	//System.out.println(" User info: "+ user.get().getSectionId());
        			Long section = user.get().getSectionId();
        			//System.out.println(" User Inst: "+ user.get().getInstitutionId());
        			Long institution = user.get().getInstitutionId();
        			//System.out.println("User Rank: "+ user.get().getRankId());
        			Long rank = user.get().getRankId();
        			Long id=user.get().getId();
        			Long user_group =user.get().getUsergroup();
        			//System.out.println("User group "+ user_group);
        			//Long accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
        			accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
        			
                    if(accessRuleId == null || accessRuleId.size() == 0) {
        				return_data.put("user_section", "");
        				return_data.put("user_institution", "");
        				return_data.put("user_rank", "");
        				return_data.put("access_channel","");
        				return_data.put("msg", "No access right for system");
        				//System.out.println("No access right for system");
        				logger.login(user.get(), 0L, logMessage.No_Access_Right(), "Failure",channel);
        				return return_data;
        			}else{
        				return_data.put("user_section", String.valueOf(section));
        				return_data.put("user_institution", String.valueOf(institution));
        				return_data.put("user_rank", String.valueOf(rank));
        				return_data.put("access_channel", String.valueOf(channel));
        				return_data.put("user_id", String.valueOf(id));
        				return_data.put("user_group", String.valueOf(user_group));
        				return_data.put("msg", "");
        				//System.out.println("-- Can get into to system(Check Report) --");
        				
        				//System.out.println("No user session，set user=" + user.get());
                        session.setAttribute("user_session", user.get());
        				//System.out.println("No access_rule session，set access_rule=" + accessRuleId);
                        session.setAttribute("user_access_rule_session", accessRuleId);
                        logger.login(user.get(), 0L, "", "Success",channel);
        				return return_data;
        			}
        			////System.out.println("-----  can get access Rule Service get By User ------"+ accessRuleId);
                }else {
                	//System.out.println("Find access_rule session" + user_access_rule_session);
                	User user_session_info = user.get();
                	return_data.put("user_section", String.valueOf(user_session_info.getSectionId()));
        			return_data.put("user_institution", String.valueOf(user_session_info.getInstitutionId()));
        			return_data.put("user_rank", String.valueOf(user_session_info.getRankId()));
        			return_data.put("user_group",String.valueOf(user_session_info.getUsergroup()));
        			return_data.put("msg", "");
        			//System.out.println("-- Can get into to system");
                	return return_data;
//				                	accessRuleId = user_access_rule_session.;
                }
    		}else{
    			return_data.put("user", null);
    			return_data.put("msg", "Invalid User Request");
    			//System.out.println("--  Invalid User Request -- ");
    			return return_data;
    		}
        }else {
        	User user_session_info = (User) user_session;
           
            return_data.put("user_section", String.valueOf(user_session_info.getSectionId()));
			return_data.put("user_institution", String.valueOf(user_session_info.getInstitutionId()));
			return_data.put("user_rank", String.valueOf(user_session_info.getRankId()));
			return_data.put("user_id", String.valueOf(user_session_info.getId()));
			return_data.put("user_group", String.valueOf(user_session_info.getUsergroup()));
			return_data.put("msg", "");
			////System.out.println("Find access_rule session" + user_access_rule_session);
			//System.out.println("-- Can get into to system (check api ) "+ return_data.get("user_group"));
			if(!user_session_info.getId().equals(user_id)) {
				logger.login(user_session_info, 0L, logMessage.Try_To_Access_Others()+"user id = "+ user_id, "Failure", channel);
			}
            return return_data;
        }
	}

	public void checkRankSession(HttpSession session) {
		Object rank_session = session.getAttribute("rank");
		if(rank_session == null) {
			List<RanksModel> rank_data = ranksService.getAll();
			List<InstitutionsModel> inst_data = instService.getAll();
			List<SectionModel> sect_data = sectService.getAll();
			//List<User> user_data = userService.allInSession();
			List<PageNameModel> page_name_data = pageNameService.getAll();
			session.setAttribute("rank", rank_data);
			session.setAttribute("section", sect_data);
			session.setAttribute("institution", inst_data);
			//session.setAttribute("user_list", user_data);
			session.setAttribute("page_name", page_name_data);
		}
		
	}
	
//	public void sendNotice(List<User> sendToList,String postTitle, String fullname, String content) {
//		try
//        {
//			String notesAccount = sendToList.get(0).getNotesAccount();
//            NotesThread.sinitThread();
//            Session s = NotesFactory.createSession((String)null, (String)null, "13149421");
//            String p = s.getPlatform();
//			Database db = s.getDatabase(s.getServerName(), DBPATH_SAMPLEDATA_NSF, true);
//			//System.out.println( "Connected as: " + dominoSession.getUserName() );
//		   
//			Document memo = db.createDocument();
//			memo.appendItemValue( "Form", "CSDKMS" );
//			memo.appendItemValue( "Importance", "1" );
////			memo.appendItemValue( "CopyTo", "holfer.zhang@hkmci.com" );
//			
//			for(Integer i = 0; i < sendToList.size(); i++) {
//				if( i > 0 ) {
//					memo.appendItemValue( "CopyTo", sendToList.get(i).getNotesAccount() );
//				}
//			}
//			
//			memo.appendItemValue( "Subject", "Comment From KMS Mini Blogs: " + fullname);
//			memo.appendItemValue( "Body", fullname + " leaves a comment for blog: " + postTitle + "\n" + content );
//			memo.send( false, notesAccount );
//		   
//			//Recycle
//			db.recycle();
//			s.recycle();
//            System.out.println("Platform = " + p);
//        }
//	 	catch ( NotesException e ){
//			
//			System.out.println( "NotesException Error - " + e.id + " " + e.text );
//			//e.printStackTrace( System.out );
//		}catch ( Exception e ){
//			System.out.println( "Error - " + e.toString() );
//			//e.printStackTrace( System.out );
//		}
//        finally
//        {
//            NotesThread.stermThread();
//        }
//	}

	@SuppressWarnings("unchecked")
	public List<Long> checkOriginalId(HttpSession session, Long user_id) {
		List<Long> temp_original_id = new ArrayList<>();
		Object original_ids = session.getAttribute("original_id");
		if(original_ids == null) {
			//No valid assistant_ids in session
			List<Long> original_id_in_db = blogAssistantRepository.findOriginalIdByUserIdAndIsDeleted(user_id, 0);
			System.out.println("original_id_in_db: " + original_id_in_db);
			if(original_id_in_db == null || original_id_in_db.size() == 0 ) {
				temp_original_id.add(0L);
				session.setAttribute("original_id", temp_original_id);
				return temp_original_id;
			}else {
				temp_original_id.addAll(original_id_in_db);
				session.setAttribute("original_id", temp_original_id);
				return temp_original_id;
			}
		}else {
			return (List<Long>) original_ids;
		}
	}
	
	
	public Integer validateToken(String jwt) {
		return tokenProvider.validateToken(jwt);
	}
	


	public String getStaffNoByToken(String jwt) {
		String username = tokenProvider.getUsernameFromJWT(jwt);
        User user = userService.findByUsername(username);
		return user.getStaffNo();
	}

	

	

	
}

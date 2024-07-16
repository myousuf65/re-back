package com.hkmci.csdkms.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.BlogGallery;
import com.hkmci.csdkms.entity.BlogGalleryDetail;
import com.hkmci.csdkms.entity.ElearningCategory;
import com.hkmci.csdkms.entity.ElearningCourse;
import com.hkmci.csdkms.entity.ElearningCourseQuiz;
import com.hkmci.csdkms.entity.ElearningGallery;
import com.hkmci.csdkms.entity.ElearningGalleryDetail;
import com.hkmci.csdkms.entity.ElearningQuestion;
import com.hkmci.csdkms.entity.ElearningQuiz;
import com.hkmci.csdkms.entity.ElearningQuizQuestion;
import com.hkmci.csdkms.entity.ElearningReport;
import com.hkmci.csdkms.entity.ElearningReportQuizRecord;
import com.hkmci.csdkms.entity.Institutions;
import com.hkmci.csdkms.entity.Ranks;
import com.hkmci.csdkms.entity.Sections;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.ElearningCourseReport;
import com.hkmci.csdkms.model.ElearningFullReport;
import com.hkmci.csdkms.model.ElearningQuizAndQuestion;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.model.ManageQuestionRequest;
import com.hkmci.csdkms.repository.ElearningCategoryRepository;
import com.hkmci.csdkms.repository.ElearningCourseQuizRepository;
import com.hkmci.csdkms.repository.ElearningCourseRepository;
import com.hkmci.csdkms.repository.ElearningQuestionRepository;
import com.hkmci.csdkms.repository.ElearningQuizQuestionRepository;
import com.hkmci.csdkms.repository.ElearningQuizRepository;
import com.hkmci.csdkms.repository.ElearningReportQuizRecordRepository;
import com.hkmci.csdkms.repository.ElearningReportRepository;
import com.hkmci.csdkms.repository.InstitutionsRepository;
import com.hkmci.csdkms.repository.RanksRepository;
import com.hkmci.csdkms.repository.SectionRepository;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.ElearningGalleryService;
import com.hkmci.csdkms.service.ElearningService;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.storage.StorageException;
import com.hkmci.csdkms.storage.StorageService;

@CrossOrigin
@RestController
@RequestMapping("/elearning")
public class ElearningController {
	@Autowired
	@Resource 
	private ElearningService elearningService;
	
	@Autowired
	@Resource
	private InstitutionsRepository institutionsRepository;
	
	@Autowired
	@Resource
	private SectionRepository sectionRepository;
	
	@Autowired
	@Resource
	private RanksRepository ranksRepository;
	
	@Autowired
	@Resource
	private ElearningCategoryRepository elearningCategoryRepository;
	
	@Autowired
	@Resource
	private ElearningCourseRepository elearningCourseRepository;
	
	@Autowired
	@Resource
	private ElearningCourseQuizRepository elearningCourseQuizRepository;
	
	@Autowired
	@Resource
    private StorageService storageService;
	
	@Autowired
	@Resource
	private ElearningGalleryService elearningGalleryService;
	
	@Autowired
	@Resource
	private ElearningQuizRepository elearningQuizRepository;
	
	@Autowired
	@Resource
	private ElearningQuizQuestionRepository elearningQuizQuestionRepository;
	
	@Autowired
	@Resource
	private ElearningQuestionRepository elearningQuestionRepository;
	
	@Autowired
	@Resource
	private ElearningReportRepository elearningReportRepository;
	
	@Autowired
	@Resource
	private ElearningReportQuizRecordRepository elearningReportQuizRecordRepository;
	
	@Autowired
	@Resource
	private UserRepository  userRepository;
	
	@Autowired
	@Resource
    private LogService logger;
	
	@Autowired
	@Resource 
	private Common common;
	
	@Autowired
	@Resource
	private AccessRuleService accessRuleService;
	
	@Autowired
	@Resource
    private UserService userService;
	
	@RequestMapping("/search_quiz/{user_id}")
	private ResponseEntity<JsonResult> quizSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@RequestParam(value="catId",required=false,defaultValue="0") Long catId,
			 	@RequestParam(value="search",required=false,defaultValue="") String search,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			//List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			//List<ElearningQuiz> result = elearningService.search(Id,page,accessRuleId,Long.parseLong(user_check.get("access_channel")));
			//Integer result_length = elearningService.searchTotal(Id,accessRuleId,Long.parseLong(user_check.get("access_channel")));
			List<ElearningQuiz> result = elearningService.searchQuiz(Id,page,search,catId);
			Integer result_length = elearningService.searchQuizTotal(Id,search,catId);
			
			return JsonResult.listTotal("Elearning Quiz List.",result,result_length,session);
		}
	}
	
	@RequestMapping("/searchAccess/{user_id}")
	private ResponseEntity<JsonResult> quizAccessSearch_bk(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@RequestParam(value="catId",required=false,defaultValue="0") Long catId,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<String> channel = new ArrayList<>();
			if(String.valueOf(user_check.get("access_channel")).equals("1")) {
		      	channel.add("0");
		      	channel.add("1");
		      	channel.add("2");
		    }else {
		    	channel.add("2");
		      	channel.add("4");
		    }
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			List<ElearningQuiz> result = elearningService.searchAccessQuizClient(Id,page,accessRuleId,channel);
			for (ElearningQuiz quiz : result) {
			    Long quizId = quiz.getId();
			    List<ElearningReportQuizRecord> records = elearningReportQuizRecordRepository.reportQuizRecordSearch(user_id, quizId);
			    if(records.size()<quiz.getRepeatTime() || quiz.getRepeatTime()==0) {
			    	quiz.setRunJoin(1);
			    } else {
			    	quiz.setRunJoin(0);
			    }
			    if(records.size()>0) {
			    	quiz.setRunResult(1);
			    } else {
			    	quiz.setRunResult(0);
			    }
			}
			Integer result_length = elearningService.searchTotalClient(Id,accessRuleId,channel);
			return JsonResult.listTotal("Elearning Quiz List.",result,result_length,session);
		}
	}
	
	@RequestMapping("/searchQuizAccess/{user_id}")
	private ResponseEntity<JsonResult> quizAccessSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long courseId,
			 	@RequestParam(value="catId",required=false,defaultValue="0") Long catId,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			String staffNo = userRepository.findById(user_id).get().getStaffNo();
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			ElearningCourse course = elearningService.findByIdAndStaffList(courseId, staffNo,accessRuleId);
			//Optional<ElearningCourse> course = elearningCourseRepository.findByIdAndStaffList(courseId, staffNo);
			Integer start_num = (Integer.parseInt(page.toString()) - 1) * 10;
			Integer end_num = 10;
			
			List<ElearningCourseQuiz> courseQuizs = elearningCourseQuizRepository.findByCourseIdPage(courseId,start_num,end_num);
			List<ElearningCourseQuiz> tmp = elearningCourseQuizRepository.findByCourseId(courseId);
			Integer total = tmp.size();
			List<ElearningQuiz> quizs = new ArrayList<>();
			for(ElearningCourseQuiz courseQuiz : courseQuizs) {
				Optional<ElearningQuiz> quizOptional = elearningQuizRepository.findByIdIsNotDelete(courseQuiz.getQuizId());
				if(quizOptional.isPresent()) {
					ElearningQuiz quiz = quizOptional.get();
					List<ElearningReportQuizRecord> records = elearningReportQuizRecordRepository.reportQuizRecordSearch(user_id, quiz.getId());
					if(records.size()<quiz.getRepeatTime() || quiz.getRepeatTime()==0) {
				    	quiz.setRunJoin(1);
				    } else {
				    	quiz.setRunJoin(0);
				    }
				    if(records.size()>0) {
				    	quiz.setRunResult(1);
				    } else {
				    	quiz.setRunResult(0);
				    }
				    quizs.add(quiz);
				}
			}
			//Integer result_length = elearningService.searchTotalClient(Id,accessRuleId,channel);
			return JsonResult.listTotal("Elearning Assessment List.",quizs,total,session);
		}
	}
	
	@RequestMapping("/searchCourseAccess/{user_id}")
	private ResponseEntity<JsonResult> courseAccessSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long courseId,
			 	@RequestParam(value="catId",required=false,defaultValue="0") Long catId,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<String> channel = new ArrayList<>();
			if(String.valueOf(user_check.get("access_channel")).equals("1")) {
		      	channel.add("0");
		      	channel.add("1");
		      	channel.add("2");
		    }else {
		    	channel.add("2");
		      	channel.add("4");
		    }
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			Optional<User> user = userRepository.findById(user_id);
			String staffNo = user.get().getStaffNo();
			List<ElearningCourse> courses = elearningService.searchAccessCourseClient(staffNo,page,channel,accessRuleId);
			Integer courses_length = elearningService.searchAccessCourseClientTotal(staffNo,channel,accessRuleId);
			return JsonResult.listTotal("Elearning Course List.",courses,courses_length,session);
		}
	}
	
	@RequestMapping("/getQuizQuestion/{user_id}")
	private ResponseEntity<JsonResult> quizSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@RequestParam(value="courseId",required=false,defaultValue="0" ) Long courseId,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			List<String> channel = new ArrayList<>();
			Long accessChannel = Long.parseLong(user_check.get("access_channel"));
			if(String.valueOf(accessChannel).equals("1")) {
		      	channel.add("0");
		      	channel.add("1");
		      	channel.add("2");
		    }else {
		    	channel.add("2");
		      	channel.add("4");
		    }
			String staffNo = userRepository.findById(user_id).get().getStaffNo();
			ElearningCourse course = elearningService.findByIdAndStaffList(courseId, staffNo,accessRuleId);
			//Optional<ElearningCourse> course = elearningCourseRepository.findByIdAndStaffList(courseId, staffNo);
			
			ElearningQuiz quiz = new ElearningQuiz();
			quiz = elearningService.searchAccessQuiz(Id,accessRuleId,channel);
			List<ElearningQuizQuestion> quizQuestions = elearningQuizQuestionRepository.findByQuizId(quiz.getId());
			List<ElearningQuestion> questions = new ArrayList<>();
			List<ElearningGalleryDetail> fileList = new ArrayList<>();
			for (ElearningQuizQuestion quizQuestion : quizQuestions) {
			    Optional<ElearningQuestion> questionOptional  = elearningQuestionRepository.findById(quizQuestion.getQuestionId());
			    if (questionOptional.isPresent()) {
			        ElearningQuestion question = questionOptional.get();
			        
			        
			        String video = question.getPostVideoLink();		
					String[] name = video.split("/KMS/");
					String param1 ="https://ams.csd.gov.hk/api/vodapi.php?";
					String param2 =	"file="+name[1]+"&folder=/KMS/&key=test1";
					//System.out.println(" Should use this "+param2);
					String param = param1 +param2;
					String videoLink =getUrlContents(param);
					
					question.setCorrectAnswer(0);
					question.setPostVideoLink(videoLink);
					
			        questions.add(question);
			        Optional<ElearningGallery> elearningGallery = elearningGalleryService.findByPostId(question.getId());
			        if(elearningGallery.isPresent()) {
			        	fileList = elearningGalleryService.findByGalleryId(elearningGallery.get());
			        }
			    }
			}
			ElearningQuizAndQuestion quizQuestion = new ElearningQuizAndQuestion();
			quizQuestion.setQuestions(questions);
			quizQuestion.setQuiz(quiz);
			//ElearningQuizAndQuestion result = elearningService.searchAccess2(Id,accessRuleId,);
			
			
			if(fileList.size() == 0) {
				return JsonResult.fileList("Post Found Successfully", quizQuestion,null,session);
			}else {
				return JsonResult.fileList("Post Found Successfully", quizQuestion,fileList,session);
			}
			
			//return JsonResult.listTotal("Elearning Quiz List.",quizQuestion,1,session);
		}
	}
	
	private static String getUrlContents(String theUrl) {
	    StringBuilder content = new StringBuilder();

	    // many of these calls can throw exceptions, so i've just
	    // wrapped them all in one try/catch statement.
	    try
	    {
	      // create a url object
	      URL url = new URL(theUrl);
	      //System.out.println("URL - "+theUrl);

	      URLConnection urlConnection = url.openConnection();

	     
	      //System.out.println("URL Connection + "+ urlConnection);
	      //System.out.println("URL get Input Stream + "+ urlConnection.getInputStream());
	      //System.out.println("New Input Stream Reader = "+new InputStreamReader(urlConnection.getInputStream()));
	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	    
	      //System.out.println("BufferedReader "+ bufferedReader);
	      String line;

	      // read from the urlconnection via the bufferedreader
	      while ((line = bufferedReader.readLine()) != null)
	      {
	        content.append(line + "\n");
	      }
	      bufferedReader.close();
	    }
	    catch(Exception e)
	    {
	    	//System.out.println("Exception== "+e);
	      e.printStackTrace();
	    }
	    //System.out.println("Content - "+ content);
	    return content.toString();
	  }
	
	@RequestMapping("/copyCourse/{user_id}")
	private ResponseEntity<JsonResult> categorySearch(
			 
		 	@RequestParam(value="courseId",required=false,defaultValue="0" ) Long courseId,
		 	@PathVariable Long user_id,
		 	HttpSession session
		 ){
		Optional<ElearningCourse> courseOpt = elearningCourseRepository.findById(courseId);
		Date createdAt = new Date();
		if(courseOpt.isPresent()) {
			ElearningCourse course = courseOpt.get();
			ElearningCourse newCourse = new ElearningCourse();
			newCourse.setCourseName(course.getCourseName()+"(copy)");
			newCourse.setStaffNoList(course.getStaffNoList());
			newCourse.setAccessChannel(course.getAccessChannel());
			newCourse.setAccessRuleId(course.getAccessRuleId());
			newCourse.setStartDate(course.getStartDate());
			newCourse.setEndDate(course.getEndDate());
			newCourse.setIsPublish(0);
			newCourse.setCreatedBy(user_id);
			newCourse.setCreatedAt(createdAt);
			newCourse.setIsDeleted(0);
			Long new_course_id = elearningCourseRepository.save(newCourse).getId();
			List<ElearningCourseQuiz> courseQuizs = elearningCourseQuizRepository.findByCourseId(courseId);
			List<Long> QuizIds = new ArrayList<>();
			for(ElearningCourseQuiz courseQuiz:courseQuizs) {
				Optional<ElearningQuiz> quizOpt = elearningQuizRepository.findById(courseQuiz.getQuizId());
				if(quizOpt.isPresent()) {
					ElearningQuiz quiz = quizOpt.get();
					ElearningQuiz new_quiz = new ElearningQuiz();
					new_quiz.setCatId(quiz.getCatId());
					new_quiz.setTitle(quiz.getTitle()+"-copy");
					new_quiz.setAccessChannel(quiz.getAccessChannel());
					new_quiz.setAccessRuleId(quiz.getAccessRuleId());
					new_quiz.setLimitTime(quiz.getLimitTime());
					new_quiz.setPassMark(quiz.getPassMark());
					new_quiz.setRepeatTime(quiz.getRepeatTime());
					new_quiz.setResult(quiz.getResult());
					new_quiz.setIsPublic(quiz.getIsPublic());
					new_quiz.setPublishAt(quiz.getPublishAt());
					new_quiz.setCreatedBy(user_id);
					new_quiz.setCreatedAt(createdAt);
					new_quiz.setIsDeleted(quiz.getIsDeleted());
					Long new_quiz_id = elearningQuizRepository.save(new_quiz).getId();
					QuizIds.add(new_quiz_id);
					
					ElearningCourseQuiz new_course_quiz = new ElearningCourseQuiz();
					new_course_quiz.setCourseId(new_course_id);
					new_course_quiz.setQuizId(new_quiz_id);
					new_course_quiz.setOrderBy(courseQuiz.getOrderBy());
					new_course_quiz.setCreatedBy(user_id);
					elearningCourseQuizRepository.save(new_course_quiz);
					
					List<ElearningQuizQuestion> QuizQuestions = elearningQuizQuestionRepository.findByQuizId(courseQuiz.getQuizId());
					for(ElearningQuizQuestion QuizQuestion:QuizQuestions) {
						Optional<ElearningQuestion> questionOpt = elearningQuestionRepository.findById(QuizQuestion.getQuestionId());
						if(questionOpt.isPresent()) {
							ElearningQuestion question = questionOpt.get();
							ElearningQuestion new_question = new ElearningQuestion();
							new_question.setCatId(question.getCatId());
							new_question.setQuestionTitle(question.getQuestionTitle()+"-copy");
							new_question.setAnswer(question.getAnswer());
							new_question.setCorrectAnswer(question.getCorrectAnswer());
							new_question.setIsDeleted(question.getIsDeleted());
							new_question.setCreatedAt(createdAt);
							new_question.setCreatedBy(user_id);
							new_question.setPostVideoLink(question.getPostVideoLink());
							new_question.setRandomSetting(question.getRandomSetting());
							Long new_question_id = elearningQuestionRepository.save(new_question).getId();
							
							ElearningQuizQuestion new_quiz_question = new ElearningQuizQuestion();
							new_quiz_question.setQuizId(new_quiz_id);
							new_quiz_question.setQuestionId(new_question_id);
							new_quiz_question.setOrderBy(QuizQuestion.getOrderBy());
							new_quiz_question.setCreatedBy(user_id);
							
							elearningQuizQuestionRepository.save(new_quiz_question);
						}
					}
				}
			}
		}

		return JsonResult.fileList("Post Found Successfully", null,null,session);
	}
	
	@RequestMapping("/searchCategory/{user_id}")
	private ResponseEntity<JsonResult> categorySearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@RequestParam(value="catId",required=false,defaultValue="0") Long catId,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@RequestParam(value="search",required=false,defaultValue="") String search,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningCategory> result = elearningService.categorySearch(Id,page,search);
			Integer result_length = elearningService.categorySearchTotal(Id,page,search);
			return JsonResult.listTotal("Elearning Category List.",result,result_length,session);
		}
	}
	
	@RequestMapping("/searchAllCategory/{user_id}")
	private ResponseEntity<JsonResult> categoryAllSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningCategory> result = elearningService.categoryAllSearch(Id);
			Integer result_length = elearningService.categoryAllSearchTotal(Id);
			return JsonResult.listTotal("Elearning Category List.",result,result_length,session);
		}
	}
	
	@RequestMapping("/searchAllCourseCat/{user_id}")
	private ResponseEntity<JsonResult> courseCatAllSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningCourse> result = elearningCourseRepository.findByIsDeleted(0);
			
			return JsonResult.listTotal("Elearning Category List.",result,result.size(),session);
		}
	}
	
	@RequestMapping("/searchQuestion/{user_id}")
	private ResponseEntity<JsonResult> questionSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@RequestParam(value="catId",required=false,defaultValue="0") Long catId,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@RequestParam(value="search",required=false,defaultValue="") String search,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningQuestion> result = elearningService.questionSearch(Id,catId,page,search);
			Integer result_length = elearningService.questionSearchTotal(Id,catId,search);
			return JsonResult.listTotal("Elearning Question List.",result,result_length,session);
		}
	}
	
	@RequestMapping("/searchCourse/{user_id}")
	private ResponseEntity<JsonResult> courseSearch(
			 
			 	@RequestParam(value="id",required=false,defaultValue="0" ) Long Id,
			 	@RequestParam(value="page",required=false,defaultValue="1") Long page,
			 	@RequestParam(value="search",required=false,defaultValue="") String search,
			 	@PathVariable Long user_id,
			 	HttpSession session
			 ){
		
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningCourse> result = elearningService.courseSearch(Id,page,search);
			Integer result_length = elearningService.courseSearchTotal(Id,search);
			return JsonResult.listTotal("Elearning Question List.",result,result_length,session);
		}
	}
	
	@RequestMapping("/getManageQuestions/{userId}")
	public ResponseEntity<JsonResult> getQuestions(@PathVariable Long userId, @RequestParam(value="quizId",required=true,defaultValue="0" ) Long quizId, HttpSession session) {
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningQuizQuestion> quizQuestions = elearningQuizQuestionRepository.findByQuizId(quizId);
			List<ElearningQuestion> questions = new ArrayList<>();
			for (ElearningQuizQuestion quizQuestion : quizQuestions) {
		        Long questionId = quizQuestion.getQuestionId();
		        Optional<ElearningQuestion> questionOptional = elearningQuestionRepository.findById(questionId);

		        if (questionOptional.isPresent()) {
		            ElearningQuestion question = questionOptional.get();
		            questions.add(question);
		        }
		    }
			return JsonResult.listTotal("Elearning Category List.",questions,questions.size(),session);
		}
	}
	
	@RequestMapping("/getManageQuiz/{userId}")
	public ResponseEntity<JsonResult> getQuizs(@PathVariable Long userId, @RequestParam(value="courseId",required=true,defaultValue="0" ) Long courseId, HttpSession session) {
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningCourseQuiz> courseQuizs = elearningCourseQuizRepository.findByCourseId(courseId);
			List<ElearningQuiz> quizs = new ArrayList<>();
			for (ElearningCourseQuiz courseQuiz : courseQuizs) {
		        Long quizId = courseQuiz.getQuizId();
		        Optional<ElearningQuiz> quizOptional = elearningQuizRepository.findByIdIsNotDelete(quizId);

		        if (quizOptional.isPresent()) {
		            ElearningQuiz quiz = quizOptional.get();
		            quizs.add(quiz);
		        }
		    }
			return JsonResult.listTotal("Elearning course quizs Category List.",quizs,quizs.size(),session);
		}
	}
	
	@RequestMapping("/getReportList/{userId}")
	private ResponseEntity<JsonResult> getReportList(@PathVariable Long userId, @RequestParam(value="quizId",required=true,defaultValue="0" ) Long quizId, @RequestParam(value="courseId",required=true,defaultValue="0" ) Long courseId,
		 	HttpSession session
		 ){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<ElearningReportQuizRecord> records = new ArrayList<>();
			if(courseId!=0) {
				records = elearningReportQuizRecordRepository.findByQuizCourseWithUser(quizId,courseId);
			} else {
				records = elearningReportQuizRecordRepository.findByQuizWithUser(quizId);
			}
			return JsonResult.listTotal("Elearning Category List.",records,records.size(),session);
		}
	}
	
	@RequestMapping("/getReport/{userId}")
	private ResponseEntity<JsonResult> getReport(@PathVariable Long userId, @RequestParam(value="clientId",required=true,defaultValue="0" ) Long clientId, @RequestParam(value="quizId",required=true,defaultValue="0" ) Long quizId,
		 	HttpSession session
		 ){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			ElearningFullReport full_report = new ElearningFullReport();
			full_report.setReportQuizRecord(elearningReportQuizRecordRepository.reportQuizRecordSearch(clientId,quizId));
			full_report.setReport(elearningReportRepository.reportSearch(clientId,quizId,full_report.getReportQuizRecord().get(0).getQuizConductCode()));
			Integer score=0;
			Integer total_score=full_report.getReport().size();
			Optional<ElearningQuiz> quiz = elearningQuizRepository.findById(quizId);
			if(quiz.isPresent() && quiz.get().getResult()>0) {
				for (ElearningReport report : full_report.getReport()) {
				    Optional<ElearningQuestion> questionOptional  = elearningQuestionRepository.findById(report.getQuestionId());
				    if (questionOptional.isPresent()) {
				        ElearningQuestion question = questionOptional.get();
				        report.setCorrectAnswer(question.getCorrectAnswer());
				    }
				}
				return JsonResult.listTotal("Elearning Category List.",full_report,1,session);
			} else {
				return JsonResult.listTotal("Elearning Category List.",null,0,session);
			}
		}
	}
	
	@RequestMapping("/getReportDetail/{userId}")
	private ResponseEntity<JsonResult> getReportDetail(@PathVariable Long userId, @RequestParam(value="quizConductCode",required=true,defaultValue="0" ) String quizConductCode,
		 	HttpSession session
		 ){
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			ElearningFullReport full_report = new ElearningFullReport();
			full_report.setReportQuizRecord(elearningReportQuizRecordRepository.findByQuizConductCode(quizConductCode));
			full_report.setReport(elearningReportRepository.findByQuizConductCode(quizConductCode));
			List<ElearningGalleryDetail> fileList = new ArrayList<>();
			for (ElearningReport report : full_report.getReport()) {
				String video = report.getPostVideoLink();		
				String[] name = video.split("/KMS/");
				String param1 ="https://ams.csd.gov.hk/api/vodapi.php?";
				String param2 =	"file="+name[1]+"&folder=/KMS/&key=test1";
				//System.out.println(" Should use this "+param2);
				String param = param1 +param2;
				String videoLink =getUrlContents(param);
				
				report.setPostVideoLink(videoLink);
				
			    Optional<ElearningQuestion> questionOptional  = elearningQuestionRepository.findById(report.getQuestionId());
			    if (questionOptional.isPresent()) {
			        Optional<ElearningGallery> elearningGallery = elearningGalleryService.findByPostId(report.getQuestionId());
			        if(elearningGallery.isPresent()) {
			        	fileList = elearningGalleryService.findByGalleryId(elearningGallery.get());
			        }
			    }
			}
			if(fileList.size() == 0) {
				return JsonResult.fileList("Post Found Successfully", full_report,null,session);
			}else {
				return JsonResult.fileList("Post Found Successfully", full_report,fileList,session);
			}
		}
	}
	
	
	@RequestMapping("/submit_answers/{userId}")
	private ResponseEntity<JsonResult> submitAnswer(@RequestBody JsonNode jsonNode, @PathVariable Long userId, @RequestParam(value="quizId",required=true,defaultValue="0" ) Long quizId, @RequestParam(value="courseId",required=true,defaultValue="0" ) Long courseId,
		 	HttpSession session
		 ){
		List<ElearningQuestion> questions = new ObjectMapper().convertValue(jsonNode.get("questions"), new TypeReference<List<ElearningQuestion>>(){});
		ElearningQuiz quiz = new ObjectMapper().convertValue(jsonNode.get("quiz"), new TypeReference<ElearningQuiz>(){});
		//List<ElearningQuestion> questions = request.getAddedQuestionList();
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Date createdAt = new Date();
			Instant now = Instant.now();
	        long timestamp = now.toEpochMilli();
	        String quizConductCode = userId + "_" + timestamp;
	        int num = 0;
		    Long longNum = (long) num;
		    Optional<User> user = userRepository.findById(userId);
			ElearningReportQuizRecord quizRecord = new ElearningReportQuizRecord();
			quizRecord.setCourseId(courseId);
			quizRecord.setQuizId(quizId);
			quizRecord.setQuizConductCode(quizConductCode);
	        quizRecord.setCreatedAt(createdAt);
		    quizRecord.setCreatedBy(userId);
		    quizRecord.setIsDeleted(longNum);
		    quizRecord.setTitle(quiz.getTitle());
		    quizRecord.setLimitTime(quiz.getLimitTime());
		    quizRecord.setTimeUse(jsonNode.get("timeUse").asInt());
		    quizRecord.setPassMark(quiz.getPassMark());
		    quizRecord.setRepeatTime(quiz.getRepeatTime());
		    quizRecord.setResult(quiz.getResult());
		    quizRecord.setAccessRuleId(quiz.getAccessRuleId());
		    quizRecord.setAccessChannel(quiz.getAccessChannel());
		    quizRecord.setFullname(user.get().getFullname());
		    quizRecord.setStaffNo(user.get().getStaffNo());

			int score = 0;
			for(Integer i=0;i<questions.size();i++) {
				Optional<ElearningQuestion> questionOptional  = elearningQuestionRepository.findById(questions.get(i).getId());
				ElearningReport elearningReport = new ElearningReport();
				elearningReport.setQuizId(quizId);
				elearningReport.setQuestionId(questions.get(i).getId());
				elearningReport.setQuizConductCode(quizConductCode);
				elearningReport.setQuestionTitle(questions.get(i).getQuestionTitle());
				elearningReport.setAnswer(questions.get(i).getAnswer());
				elearningReport.setUserAnswer((Integer) Integer.parseInt(jsonNode.get("userAnswers").get(i).get("answer").asText()));
				elearningReport.setCreatedBy(userId);
				elearningReport.setCreatedAt(createdAt);
				elearningReport.setPostVideoLink(questions.get(i).getPostVideoLink());
				if(questionOptional.isPresent()) {
					elearningReport.setCorrectAnswer(questionOptional.get().getCorrectAnswer());
					System.out.println("questionOptional is present");
					if (questionOptional.get().getCorrectAnswer().equals(elearningReport.getUserAnswer())) {
						score=score+1;
			        }
				} else {
					elearningReport.setCorrectAnswer(0);
				}
				elearningService.addReport(elearningReport);
			}
			int totalScore = questions.size();

			// 計算整數百分比
			int percentage = (score * 100) / totalScore;
			quizRecord.setScore(score);
			quizRecord.setTotalScore(totalScore);
			quizRecord.setScorePercentage(percentage);
			elearningService.addQuizReport(quizRecord);
			return JsonResult.ok("Post Update Successfully",session);
		}
	}
	
	@RequestMapping("/manage_question/{userId}")
	private ResponseEntity<JsonResult> manageQuestion(@RequestBody JsonNode jsonNode, @PathVariable Long userId, @RequestParam(value="quizId",required=true,defaultValue="0" ) Long quizId,
			 	HttpSession session
			 ){
		List<ElearningQuestion> questions = new ObjectMapper().convertValue(jsonNode.get("addedQuestionList"), new TypeReference<List<ElearningQuestion>>(){});
		//List<ElearningQuestion> questions = request.getAddedQuestionList();
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Date createdAt = new Date();
			elearningService.cleanQuizQuestion(quizId);
			for(Integer i=0;i<questions.size();i++) {
				ElearningQuizQuestion elearningQuizQuestion = new ElearningQuizQuestion();
				elearningQuizQuestion.setQuestionId(questions.get(i).getId());
				elearningQuizQuestion.setQuizId(quizId);
				elearningQuizQuestion.setOrderBy(i);
				elearningQuizQuestion.setCreatedBy(userId);
				elearningQuizQuestion.setCreatedAt(createdAt);
				elearningService.updateQuizQuestion(elearningQuizQuestion);
			}
			return JsonResult.ok("Post Update Successfully",session);
		}
	}
	
	@RequestMapping("/manage_quiz/{userId}")
	private ResponseEntity<JsonResult> manageQuiz(@RequestBody JsonNode jsonNode, @PathVariable Long userId, @RequestParam(value="courseId",required=true,defaultValue="0" ) Long courseId,
			 	HttpSession session
			 ){
		List<ElearningQuiz> quizs = new ObjectMapper().convertValue(jsonNode.get("addedQuizList"), new TypeReference<List<ElearningQuiz>>(){});
		//List<ElearningQuestion> questions = request.getAddedQuestionList();
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Date createdAt = new Date();
			elearningService.cleanCourseQuiz(courseId);
			for(Integer i=0;i<quizs.size();i++) {
				ElearningCourseQuiz elearningCourseQuiz = new ElearningCourseQuiz();
				elearningCourseQuiz.setQuizId(quizs.get(i).getId());
				elearningCourseQuiz.setCourseId(courseId);
				elearningCourseQuiz.setOrderBy(i);
				elearningCourseQuiz.setCreatedBy(userId);
				elearningCourseQuiz.setCreatedAt(createdAt);
				elearningCourseQuizRepository.saveAndFlush(elearningCourseQuiz);
			}
			return JsonResult.ok("Post Update Successfully",session);
		}
	}
	
	@RequestMapping("/create_quiz/{userId}")
	public ResponseEntity<JsonResult> createQuiz(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			//System.out.println(userId);
			
			Date createdAt = new Date();
			
			String dateOfTest = jsonNode.get("publishAt").asText();
			
			Date publishAt = common.textToDate(dateOfTest);
	        ElearningQuiz new_quiz = new ElearningQuiz();
	        
	        Optional<ElearningQuiz> duplicate = elearningQuizRepository.findByTitle(jsonNode.get("title").asText());
	        if(!duplicate.isPresent()) {
		        JsonNode array = jsonNode.get("accessRuleId");
		        String setAccessRuleId="";
			    if (array.isArray()) {
			    	List<JsonNode> nodeList = new ArrayList<>();
			       	array.elements().forEachRemaining(nodeList::add);
			       	setAccessRuleId = nodeList.stream()
			       				.map(JsonNode::asText)
			                    .collect(Collectors.joining(","));
			    }
			    int num = 0;
			    Long longNum = (long) num;
			    new_quiz.setCatId(jsonNode.get("catId").asLong());
		        new_quiz.setCreatedAt(createdAt);
			    new_quiz.setCreatedBy(jsonNode.get("createdBy").asLong());
			    new_quiz.setIsPublic(jsonNode.get("isPublic").asLong());
			    new_quiz.setPublishAt(publishAt);
			    new_quiz.setIsDeleted(longNum);
			    new_quiz.setTitle(jsonNode.get("title").asText());
			    new_quiz.setLimitTime(jsonNode.get("limitTime").asInt());
			    new_quiz.setPassMark(jsonNode.get("passMark").asInt());
			    new_quiz.setRepeatTime(jsonNode.get("repeat").asInt());
			    new_quiz.setResult(jsonNode.get("result").asInt());
			    new_quiz.setAccessRuleId(setAccessRuleId);
			    new_quiz.setAccessChannel(jsonNode.get("accessChannel").asText());
			    Long return_blog_id = elearningService.addQuiz(new_quiz).getId();
			    logger.createBlog(user, 0L, "", "Success",channel);
			    return JsonResult.ok(return_blog_id,"Post Create Successfully", session);
			} else {
				return JsonResult.ok("Duplicate Quiz Name", session);
			}
			
		}
    }
	
	@RequestMapping("/create_course/{userId}")
	public ResponseEntity<JsonResult> createCourse(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			//System.out.println(userId);
			
			Optional<ElearningCourse> duplicate = elearningCourseRepository.findByCourseName(jsonNode.get("courseName").asText());
			
			if(!duplicate.isPresent()) {
				Date createdAt = new Date();
				
				String dateOfTest = jsonNode.get("startDate").asText();
				Date startDate = common.textToDate(dateOfTest);
				dateOfTest = jsonNode.get("endDate").asText();
				Date endDate = common.textToDate(dateOfTest);
		        ElearningCourse new_course = new ElearningCourse();
		        
		        int num = 0;
			    Long longNum = (long) num;
			    
			    JsonNode array = jsonNode.get("accessRuleId");
		        String setAccessRuleId="";
			    if (array.isArray()) {
			    	List<JsonNode> nodeList = new ArrayList<>();
			       	array.elements().forEachRemaining(nodeList::add);
			       	setAccessRuleId = nodeList.stream()
			       				.map(JsonNode::asText)
			                    .collect(Collectors.joining(","));
			    }
			    
		        new_course.setCourseName(jsonNode.get("courseName").asText());
		        new_course.setAccessChannel(jsonNode.get("accessChannel").asText());
		        new_course.setAccessRuleId(setAccessRuleId);
		        new_course.setStaffNoList(jsonNode.get("staffNoList").asText());
		        new_course.setStartDate(startDate);
		        new_course.setEndDate(endDate);
		        new_course.setIsPublish(jsonNode.get("isPublish").asLong());
		        new_course.setIsDeleted(longNum);
		        new_course.setCreatedAt(createdAt);
		        new_course.setCreatedBy(jsonNode.get("createdBy").asLong());
			    
			    Long return_course_id = elearningCourseRepository.saveAndFlush(new_course).getId();
			    logger.createBlog(user, 0L, "", "Success",channel);
			    return JsonResult.ok(return_course_id,"Post Create Successfully", session);
			} else {
				return JsonResult.ok("Duplicate Course Name", session);
			}
	        
		}
    }
	
	@RequestMapping("/update_course/{elearningCourseId}")
	public ResponseEntity<JsonResult> updateCourse(@RequestBody JsonNode jsonNode, @PathVariable Long elearningCourseId, HttpSession session) {
		Optional<ElearningCourse> elearningCourse = elearningCourseRepository.findById(elearningCourseId);
		System.out.println(jsonNode.get("modifiedBy"));
		if(elearningCourse.isPresent()) {
			Integer checkElearningQuizUser = common.checkElearningCourseUser(jsonNode, session, elearningCourse.get(),"update");
			if(checkElearningQuizUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				Optional<ElearningCourse> duplicate = elearningCourseRepository.findDuplicateByIdCourseName(elearningCourseId,jsonNode.get("courseName").asText());
				if(!duplicate.isPresent()) {
					ElearningCourse new_elearning = elearningCourse.get();
					String dateOfTest = jsonNode.get("startDate").asText();
					Date startDate = common.textToDate(dateOfTest);
					dateOfTest = jsonNode.get("endDate").asText();
					Date endDate = common.textToDate(dateOfTest);
					Date modifiedAt = new Date();
					
					JsonNode array = jsonNode.get("accessRuleId");
			        String setAccessRuleId="";
				    if (array.isArray()) {
				    	List<JsonNode> nodeList = new ArrayList<>();
				       	array.elements().forEachRemaining(nodeList::add);
				       	setAccessRuleId = nodeList.stream()
				       				.map(JsonNode::asText)
				                    .collect(Collectors.joining(","));
				    }
					
			        //new_elearning.setCatId(jsonNode.get("catId").asLong());
			        new_elearning.setModifiedAt(modifiedAt);
			        new_elearning.setModifiedBy(jsonNode.get("createdBy").asLong());
			        new_elearning.setIsPublish(jsonNode.get("isPublish").asLong());
			        new_elearning.setStartDate(startDate);
			        new_elearning.setEndDate(endDate);
			        new_elearning.setAccessChannel(jsonNode.get("accessChannel").asText());
			        new_elearning.setAccessRuleId(setAccessRuleId);
			        new_elearning.setCourseName(jsonNode.get("courseName").asText());
			        new_elearning.setStaffNoList(jsonNode.get("staffNoList").asText());
			        elearningCourseRepository.saveAndFlush(new_elearning);
					return JsonResult.ok("Post Update Successfully",session);
				} else {
					return JsonResult.ok("Duplicate Course Name",session);
				}
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@RequestMapping("/delete_course/{elearningQuizId}")
	public ResponseEntity<JsonResult> deleteCourse(@RequestBody JsonNode jsonNode, @PathVariable Long elearningQuizId, HttpSession session) {
		Optional<ElearningCourse> elearningCourse = elearningCourseRepository.findById(elearningQuizId);
		//System.out.println(userId);
		if(elearningCourse.isPresent()) {
			Integer checkSpecialCollectionUser = common.checkElearningCourseUser(jsonNode, session, elearningCourse.get(),"update");
			if(checkSpecialCollectionUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				ElearningCourse new_elearning = elearningCourse.get();
				int num = 1;
			    Long longNum = (long) num;
		        new_elearning.setIsDeleted(longNum);
		        elearningCourseRepository.saveAndFlush(new_elearning);
				return JsonResult.ok("Post Delete Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	
	
	@RequestMapping("/create_question/{userId}")
	public ResponseEntity<JsonResult> createQuestion(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			//System.out.println(userId);
			
			Date createdAt = new Date();
			
	        ElearningQuestion new_question = new ElearningQuestion();
	        
		    int num = 0;
		    Long longNum = (long) num;
		    new_question.setCatId(jsonNode.get("catId").asLong());
		    new_question.setQuestionTitle(jsonNode.get("question").asText());
//		    new_question.setAnswer1(jsonNode.get("answer_1").asText());
//		    new_question.setAnswer2(jsonNode.get("answer_2").asText());
//		    new_question.setAnswer3(jsonNode.get("answer_3").asText());
//		    new_question.setAnswer4(jsonNode.get("answer_4").asText());
		    new_question.setAnswer(jsonNode.get("answer").asText());
		    new_question.setCorrectAnswer(jsonNode.get("correct_ans").asInt());
		    new_question.setRandomSetting(jsonNode.get("random_setting").asLong());
		    new_question.setIsDeleted(longNum);
		    new_question.setCreatedAt(createdAt);
		    new_question.setCreatedBy(jsonNode.get("createdBy").asLong());
		    new_question.setPostVideoLink(jsonNode.get("post_video_link").asText());
		    Long return_question_id = elearningService.addQuestion(new_question).getId();
		    Optional<ElearningGallery> elearningGallery = elearningGalleryService.findActive(userId);
			if(elearningGallery.isPresent()) {
				elearningGallery.get().setPostId(return_question_id);
				elearningGallery.get().setFinishedAt(new Date());
				elearningGallery.get().setIsFinished(1);
		        elearningGalleryService.updateElearningGallery(elearningGallery.get());
			}
		    logger.createBlog(user, 0L, "", "Success",channel);
		    return JsonResult.ok(return_question_id,"Post Create Successfully", session);
	        
		}
    }
	
	@RequestMapping("/update_question/{elearningQuestionId}")
	public ResponseEntity<JsonResult> updateQuestion(@RequestBody JsonNode jsonNode, @PathVariable Long elearningQuestionId, @RequestParam(value="user",required=false,defaultValue="0" ) Long userId, HttpSession session) {
		Optional<ElearningQuestion> elearningQuestion = elearningService.findQuestionById(elearningQuestionId);
		System.out.println(jsonNode.get("modifiedBy"));
		if(elearningQuestion.isPresent()) {
			Integer checkUser = common.checkUserSession(userId, session);
			if(checkUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				ElearningQuestion new_question = elearningQuestion.get();
				Date modifiedAt = new Date();
		        //new_elearning.setCatId(jsonNode.get("catId").asLong());
				new_question.setModifiedAt(modifiedAt);
		        new_question.setModifiedBy(jsonNode.get("modifiedBy").asLong());
		        new_question.setCatId(jsonNode.get("catId").asLong());
			    new_question.setQuestionTitle(jsonNode.get("question").asText());
			    new_question.setRandomSetting(jsonNode.get("random_setting").asLong());
			    //new_question.setAnswer1(jsonNode.get("answer_1").asText());
			    //new_question.setAnswer2(jsonNode.get("answer_2").asText());
			    //new_question.setAnswer3(jsonNode.get("answer_3").asText());
			    //new_question.setAnswer4(jsonNode.get("answer_4").asText());
			    new_question.setAnswer(jsonNode.get("answer").asText());
			    new_question.setCorrectAnswer(jsonNode.get("correct_ans").asInt());;
			    new_question.setPostVideoLink(jsonNode.get("post_video_link").asText());
		        elearningService.updateQuestion(new_question);
//		        Optional<ElearningGallery> elearningGallery = elearningGalleryService.findActive(userId);
//				if(elearningGallery.isPresent()) {
//					elearningGallery.get().setPostId(new_question.getId());
//					elearningGallery.get().setModifiedAt(new Date());
//					elearningGallery.get().setModifiedBy(userId);
//					elearningGallery.get().setFinishedAt(new Date());
//					elearningGallery.get().setIsFinished(1);
//			        elearningGalleryService.updateElearningGallery(elearningGallery.get());
//				}
				return JsonResult.ok("Post Update Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getQuizDetail/{elearningQuizId}")
	public ResponseEntity<JsonResult> getQuizDetail(@PathVariable Long elearningQuizId, @RequestParam(value="user") Long userId, HttpSession session ) {
		HashMap<String, String> user_check = common.checkUser(userId,session);
		Optional<ElearningQuiz> data = elearningService.findQuizById(elearningQuizId);	
		//PostReturnModel return_data = new PostReturnModel();
		//return_data.setSpecialCollection(data);
		return JsonResult.fileList("Post Found Successfully", data,null,session);
    }
	
	@RequestMapping("/update_quiz/{elearningQuizId}")
	public ResponseEntity<JsonResult> updateQuiz(@RequestBody JsonNode jsonNode, @PathVariable Long elearningQuizId, HttpSession session) {
		Optional<ElearningQuiz> elearningQuiz = elearningService.findQuizById(elearningQuizId);
		System.out.println(jsonNode.get("modifiedBy"));
		if(elearningQuiz.isPresent()) {
			Integer checkElearningQuizUser = common.checkElearningQuizUser(jsonNode, session, elearningQuiz.get(),"update");
			if(checkElearningQuizUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				Optional<ElearningQuiz> duplicate = elearningQuizRepository.findByIdTitle(elearningQuizId,jsonNode.get("title").asText());
				if(!duplicate.isPresent()) {
					ElearningQuiz new_elearning = elearningQuiz.get();
			        String dateOfTest = jsonNode.get("publishAt").asText();
					Date publishAt = common.textToDate(dateOfTest);
					Date modifiedAt = new Date();
					JsonNode array = jsonNode.get("accessRuleId");
			        String setAccessRuleId="";
				    if (array.isArray()) {
				    	List<JsonNode> nodeList = new ArrayList<>();
				       	array.elements().forEachRemaining(nodeList::add);
				       	setAccessRuleId = nodeList.stream().map(JsonNode::asText)
				                    .collect(Collectors.joining(","));
				    } else {
				    	setAccessRuleId = array.asText();
				    }
			        //new_elearning.setCatId(jsonNode.get("catId").asLong());
			        new_elearning.setModifiedAt(modifiedAt);
			        new_elearning.setModifiedBy(jsonNode.get("modifiedBy").asLong());
			        new_elearning.setIsPublic(jsonNode.get("isPublic").asLong());
			        new_elearning.setPublishAt(publishAt);
			        new_elearning.setTitle(jsonNode.get("title").asText());
			        new_elearning.setLimitTime(jsonNode.get("limitTime").asInt());
			        new_elearning.setPassMark(jsonNode.get("passMark").asInt());
			        new_elearning.setRepeatTime(jsonNode.get("repeat").asInt());
			        new_elearning.setResult(jsonNode.get("result").asInt());
			        new_elearning.setAccessRuleId(setAccessRuleId);
			        new_elearning.setAccessChannel(jsonNode.get("accessChannel").asText());
			        elearningService.updateQuiz(new_elearning);
					return JsonResult.ok("Post Update Successfully",session);
				} else {
					return JsonResult.ok("Duplicate Quiz Name",session);
				}
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@RequestMapping("/delete_quiz/{elearningQuizId}")
	public ResponseEntity<JsonResult> deleteQuiz(@RequestBody JsonNode jsonNode, @PathVariable Long elearningQuizId, HttpSession session) {
		Optional<ElearningQuiz> elearningQuiz = elearningService.findQuizById(elearningQuizId);
		//System.out.println(userId);
		if(elearningQuiz.isPresent()) {
			Integer checkSpecialCollectionUser = common.checkElearningQuizUser(jsonNode, session, elearningQuiz.get(),"update");
			if(checkSpecialCollectionUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				ElearningQuiz new_elearning = elearningQuiz.get();
				int num = 1;
			    Long longNum = (long) num;
		        new_elearning.setIsDeleted(longNum);
		        elearningService.updateQuiz(new_elearning);
				return JsonResult.ok("Post Update Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@RequestMapping("/delete_category/{elearningCategoryId}")
	public ResponseEntity<JsonResult> deleteCategory(@RequestBody JsonNode jsonNode, @PathVariable Long elearningCategoryId, HttpSession session) {
		Optional<ElearningCategory> elearningCategory = elearningCategoryRepository.findById(elearningCategoryId);
		//System.out.println(userId);
		if(elearningCategory.isPresent()) {
			Integer checkSpecialCollectionUser = common.checkElearningCategoryUser(jsonNode, session, elearningCategory.get(),"update");
			if(checkSpecialCollectionUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				ElearningCategory new_elearning = elearningCategory.get();
				int num = 1;
			    Long longNum = (long) num;
		        new_elearning.setIsDeleted(longNum);
		        elearningCategoryRepository.saveAndFlush(new_elearning);
				return JsonResult.ok("Post Update Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@RequestMapping("/delete_question/{elearningQuestionId}")
	public ResponseEntity<JsonResult> deleteQuestion(@RequestBody JsonNode jsonNode, @PathVariable Long elearningQuestionId, HttpSession session) {
		Optional<ElearningQuestion> elearningQuestion = elearningQuestionRepository.findById(elearningQuestionId);
		//System.out.println(userId);
		if(elearningQuestion.isPresent()) {
			Integer checkSpecialCollectionUser = common.checkElearningQuestionUser(jsonNode, session, elearningQuestion.get(),"update");
			if(checkSpecialCollectionUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				ElearningQuestion new_elearning = elearningQuestion.get();
				int num = 1;
			    Long longNum = (long) num;
		        new_elearning.setIsDeleted(longNum);
		        elearningQuestionRepository.saveAndFlush(new_elearning);
				return JsonResult.ok("Post Update Successfully",session);
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@RequestMapping("/create_category/{userId}")
	public ResponseEntity<JsonResult> createCategory(@RequestBody JsonNode jsonNode, @PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			//System.out.println(userId);
			Optional<ElearningCategory> duplicate = elearningCategoryRepository.findByCategoryTitle(jsonNode.get("title").asText());
			if(!duplicate.isPresent()) {
				Date createdAt = new Date();
				
		        ElearningCategory new_cat = new ElearningCategory();
		        int num = 0;
			    Long longNum = (long) num;
		        new_cat.setIsDeleted(longNum);
		        new_cat.setCreatedAt(createdAt);
		        new_cat.setCreatedBy(jsonNode.get("createdBy").asLong());
		        new_cat.setTitle(jsonNode.get("title").asText());
			    Long return_cat_id = elearningService.addCategory(new_cat).getId();
			    //logger.createBlog(user, 0L, "", "Success",channel);
			    return JsonResult.ok(return_cat_id,"Post Create Successfully", session);
			}else {
				return JsonResult.ok("Duplicate Category Name", session);
			}
		}
    }
	
	@RequestMapping("/update_category/{elearningCatId}")
	public ResponseEntity<JsonResult> updateCat(@RequestBody JsonNode jsonNode, @PathVariable Long elearningCatId, HttpSession session) {
		Optional<ElearningCategory> elearningCat = elearningCategoryRepository.findById(elearningCatId);
		System.out.println(jsonNode.get("modifiedBy"));
		if(elearningCat.isPresent()) {
			Integer checkElearningCatUser = common.checkElearningCategoryUser(jsonNode, session, elearningCat.get(),"update");
			if(checkElearningCatUser.equals(0)) {
				return JsonResult.errorMsg("Request User Status Error");
			}else {
				Optional<ElearningCategory> duplicate = elearningCategoryRepository.findByIdCategoryTitle(elearningCatId,jsonNode.get("title").asText());
				
				if(!duplicate.isPresent()) {
					ElearningCategory new_elearning = elearningCat.get();
					
			        new_elearning.setTitle(jsonNode.get("title").asText());
			        elearningCategoryRepository.saveAndFlush(new_elearning);
					return JsonResult.ok("Post Update Successfully",session);
				} else {
					return JsonResult.ok("Duplicate Category Name",session);
				}
			}
		}
		else{
			return JsonResult.errorMsg("Post Not Found");
		}
    }
	
	@RequestMapping(value = "/course_report/{elearningCourseId}")
	public ResponseEntity<JsonResult> getCourseReport(@RequestBody JsonNode jsonNode, @PathVariable Long elearningCourseId, HttpSession session) {
	    List<ElearningCourseReport> courseReportList = new ArrayList<>();
	    Optional<ElearningCourse> tmp_course = elearningCourseRepository.findById(elearningCourseId);
	    System.out.println("course_report:start...");
	    if(tmp_course.isPresent()) {
	    	ElearningCourse course = tmp_course.get();
	    	System.out.println("course_report:Course Found ID:"+course.getId());
	    	List<ElearningCourseQuiz> tmp_courseQuiz = elearningCourseQuizRepository.findByCourseId(course.getId());
	    	
	    	Map<Long, Integer> countMap = new HashMap<>();
	    	
	    	for(ElearningCourseQuiz courseQuiz : tmp_courseQuiz) {
	    		System.out.println("course_report:CourseQuiz Found ID:"+courseQuiz.getId());
	    		System.out.println("course_report:CourseId:"+courseQuiz.getCourseId()+",QuizId:"+courseQuiz.getQuizId());
    			
    			
    				
				List<ElearningReportQuizRecord> tmp_report = elearningReportQuizRecordRepository.findByCourseIdAndQuizID(courseQuiz.getCourseId(),courseQuiz.getQuizId());
				
				
				for(ElearningReportQuizRecord report : tmp_report) {
					ElearningCourseReport courseReport = new ElearningCourseReport();
	    			Optional<User> tmp_user = userRepository.findById(report.getCreatedBy());
					System.out.println("course_report:Quiz Record Found ID:"+report.getId());
	    			//Rank rank = re
					if(tmp_user.isPresent()) {
						User user = tmp_user.get();
	    				System.out.println("course_report:User Found ID:"+user.getId());
		    			String pass_str = "Fail";
		    			if(report.getScorePercentage() > report.getPassMark()) {
		    				pass_str = "Pass";
		    			}
		    			
		    			courseReport.setCourseName(course.getCourseName());
		    			courseReport.setCourseId(course.getId());
		    			courseReport.setAssessmentId(courseQuiz.getQuizId());
		    			courseReport.setAssessmentName(report.getTitle());
		    			courseReport.setStaffNo(user.getStaffNo());
		    			courseReport.setScore(report.getScorePercentage());
		    			courseReport.setPassFail(pass_str);
		    			courseReport.setFullName(user.getFullname());
		    			courseReport.setEmail(user.getEmail());
		    			
		    			Optional<InstitutionsModel> inst = institutionsRepository.findById(user.getInstitutionId());
		    			if(inst.isPresent()) {
		    				courseReport.setInstitution(inst.get().getInstName());
		    			}
		    			
		    			Optional<SectionModel> sections = sectionRepository.findById(user.getSectionId());
		    			if(inst.isPresent()) {
		    				courseReport.setSection(sections.get().getSectionName());
		    			}
		    			
		    			Optional<RanksModel> ranks = ranksRepository.findAllById(user.getRankId());
		    			if(ranks.isPresent()) {
		    				courseReport.setRank(ranks.get().getRankName());
		    			}
		    			courseReport.setSex(user.getSex());
		    			courseReport.setDuration(convertSecondsToTime(report.getTimeUse()));
		    			courseReport.setCompleteTime(report.getCreatedAt().toString());
		    			
		    			//List<ElearningReportQuizRecord> records = elearningReportQuizRecordRepository.reportQuizRecordSearch(user.getId(), courseQuiz.getCourseId(), courseQuiz.getQuizId());
		    			//courseReport.setAttemptNumber(records.size());
		    			
		    			if (countMap.containsKey(user.getId())) {
		                    int count = countMap.get(user.getId());
		                    countMap.put(user.getId(), count + 1);
		                } else {
		                    countMap.put(user.getId(), 1);
		                }
		    			
		    			courseReport.setAttemptNumber(countMap.get(user.getId()));
		    			
		    			courseReportList.add(courseReport);

	    				System.out.println("course_report:CourseReport added");
	    			}
	    		}
    		}
    	}
	    
	    // Fill the report object with data
	    return JsonResult.listTotal("Report data Extract Success",courseReportList,1,session);

	    
	  }
	
	@RequestMapping("/importQuestions")
	public ResponseEntity<JsonResult> importQuestions(@RequestBody List<JsonNode> importedQuestionList,HttpSession session) {
    	List<String> output_str =new ArrayList<>();
    	List<String> course_str =new ArrayList<>();
    	List<String> quiz_str =new ArrayList<>();
    	List<String> category_str =new ArrayList<>();
    	output_str.add("Run Inserting...");
		System.out.println("Run Inserting...function - importQuestions() at ElearningController");
    	for (JsonNode questionData : importedQuestionList) {
    		
    		String courseName = questionData.get("courseName").asText();
    		Optional<ElearningCourse> tmp_course = elearningCourseRepository.findByCourseName(courseName);
    		if(!tmp_course.isPresent()) {
    			if(!course_str.contains(courseName)) {
    				course_str.add(courseName);
    			}
    		}
    		
    		String quizName = questionData.get("quizName").asText();
    		Optional<ElearningQuiz> tmp_quiz = elearningQuizRepository.findByTitle(quizName);
    		if(!tmp_quiz.isPresent()) {
    			if(!quiz_str.contains(quizName)) {
    				quiz_str.add(quizName);
    			}
    		} else {
    		}
    	
    		String category = questionData.get("category").asText();
    		Optional<ElearningCategory> tmp_category = elearningCategoryRepository.findByCategoryTitle(category);
    		if(!tmp_category.isPresent()) {
    			if(!category_str.contains(category)) {
    				category_str.add(category);
    			}
    		}
    	}
    	
    	for (int i = 0; i < course_str.size(); i++) {
    		output_str.add("<span style='color:red'>Error</span> - Course: <span style='color:red'>" + course_str.get(i) + "</span> cannot be found. Please check it at Course Tab");
		}
    	
    	for (int i = 0; i < quiz_str.size(); i++) {
    		output_str.add("<span style='color:red'>Error</span> - Assessment: <span style='color:red'>" + quiz_str.get(i) + "</span> cannot be found. Please check it at Assessment Tab");
	  	}
    	
    	for (int i = 0; i < category_str.size(); i++) {
    		output_str.add("<span style='color:red'>Error</span> - Category: <span style='color:red'>" + category_str.get(i) + "</span> cannot be found. Please check it at category Tab");
	  	}
    	

		if(course_str.size()==0 && quiz_str.size()==0 && category_str.size()==0) {
			Date createdAt = new Date();
    		int k=0;
    		int j=0;
	    	for (JsonNode questionData : importedQuestionList) {
	    		Optional<ElearningCourse> tmp_course = elearningCourseRepository.findByCourseName(questionData.get("courseName").asText());
	    		Optional<ElearningQuiz> tmp_quiz = elearningQuizRepository.findByTitle(questionData.get("quizName").asText());
	    		Optional<ElearningCategory> tmp_category = elearningCategoryRepository.findByCategoryTitle(questionData.get("category").asText());
	    		if(tmp_course.isPresent() && tmp_quiz.isPresent() && tmp_category.isPresent()) {
	    			Long courseId = tmp_course.get().getId();
	    			Long quizId = tmp_quiz.get().getId();
	    			Long categoryId = tmp_category.get().getId();
	    			Long createdBy = questionData.get("createdBy").asLong();
	    			
	    			ElearningQuestion q = new ElearningQuestion();
		    		q.setQuestionTitle(questionData.get("questionTitle").asText());
		    		q.setAnswer(questionData.get("answer").asText());
		    		q.setCorrectAnswer(questionData.get("correctAnswer").asInt());
		    		q.setCreatedAt(createdAt);
		    		q.setCreatedBy(createdBy);
		    		q.setIsDeleted(0L);
		    		q.setCatId(categoryId);
		    		q.setPostVideoLink(questionData.get("postVideoLink").asText());
		    		q.setRandomSetting(questionData.get("randomSetting").asLong());
		    		
		    		output_str.add("<span style='color:green'>Success</span> - Course: <span style='color:red'>"+questionData.get("courseName").asText()+"</span>;Assessment: <span style='color:red'>"+questionData.get("quizName").asText()+"</span>;Category: <span style='color:red'>"+questionData.get("category").asText()+"</span> Question Added titled <span style='color:red'>'"+q.getQuestionTitle()+"'</span>.");
		    		
		    		Long questionId = elearningQuestionRepository.saveAndFlush(q).getId();
		    		
		    		ElearningQuizQuestion quizQuestion = new ElearningQuizQuestion();
		    		quizQuestion.setQuizId(quizId);
		    		quizQuestion.setQuestionId(questionId);
		    		quizQuestion.setOrderBy(k);
		    		quizQuestion.setCreatedAt(createdAt);
		    		quizQuestion.setCreatedBy(createdBy);
		    		elearningQuizQuestionRepository.saveAndFlush(quizQuestion);
		    		
		    		
		    		Optional<ElearningCourseQuiz> tmpCourseQuiz = elearningCourseQuizRepository.findByCourseIdQuizId(courseId,quizId);
		    		if(!tmpCourseQuiz.isPresent()) {
		    			
			    		ElearningCourseQuiz courseQuiz = new ElearningCourseQuiz();
			    		courseQuiz.setCourseId(courseId);
			    		courseQuiz.setQuizId(quizId);
			    		courseQuiz.setOrderBy(j);
			    		courseQuiz.setCreatedAt(createdAt);
			    		courseQuiz.setCreatedBy(createdBy);
			    		elearningCourseQuizRepository.saveAndFlush(courseQuiz);
			    		
			    		j++;
		    		}
		    		k++;
	    		} else {
	    			output_str.add("Error 3 items not isPresesnt()");
		    	}
	    		
	    	}
	    	return JsonResult.listTotal("Success",output_str,1,session);
		} else {
			return JsonResult.listTotal("Error",output_str,1,session);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> getAccessRuleIds(HashMap<String, String> user_check,HttpSession session) {
		//Object user_session = session.getAttribute("user_session");
		Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		
		Long section = Long.parseLong(user_check.get("user_section").toString());
		Long institution = Long.parseLong(user_check.get("user_institution").toString());
		Long rank = Long.parseLong(user_check.get("user_rank").toString());
		//Long accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
		
        if (user_access_rule_session == null) {
        	List<Long> accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
            //System.out.println("No access_rule session，set access_rule = " + accessRuleId);
            session.setAttribute("user_access_rule_session", accessRuleId);
            return accessRuleId;
        } else {
            //System.out.println("User access rule session exist(getAccessRuleIds) :" + user_access_rule_session);
            return (List<Long>) user_access_rule_session;
        }
	}
	
	@RequestMapping("/gallery/check/{userId}")
    public ResponseEntity<JsonResult> checkGallery(@PathVariable Long userId, HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			Optional<ElearningGallery> elearningGallery = elearningGalleryService.findActive(userId);
			//System.out.println("Start check temp folder   -"+userId);
			if(elearningGallery.isPresent()) {
//				System.out.println("Bolg Gallery Present!! ");
				List<ElearningGalleryDetail> fileList = elearningGalleryService.findByGalleryId(elearningGallery.get());
//				System.out.println("file List "+fileList);
				return JsonResult.fileList("Gallery Existed", elearningGallery.get(), fileList,session);
			}
			else{
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				Md5Encode md5 = new Md5Encode();
				Date now = new Date();
		        String gallery_name = md5.getMD5("Holfer" + now.toString());
		        
		        ElearningGallery new_elearningGallery = new ElearningGallery();
		        new_elearningGallery.setGalleryName(gallery_name);
		        new_elearningGallery.setCreatedBy(userId);
		        new_elearningGallery.setCreatedAt(now);
		        new_elearningGallery.setIsFinished(0);
		        new_elearningGallery.setUserId(userId);
		        
		        ElearningGallery return_elearningGallery = elearningGalleryService.newElearningGallery(new_elearningGallery);
				logger.createElearningGallery(user, return_elearningGallery.getId(), "", "Success",channel);
				//return JsonResult.ok(return_elearningGallery,"Gallery Created");
				return JsonResult.fileList("Gallery Created", return_elearningGallery, null,session);
			}
		}
    }
	

	@RequestMapping("/gallery/upload/{userId}")
    public ResponseEntity<JsonResult> uploadGallery(@PathVariable Long userId,
    		@RequestParam("file") MultipartFile file,
    		HttpSession session) {
		
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			Optional<ElearningGallery> elearningGallery = elearningGalleryService.findActive(userId);
			//System.out.println("gallery upload --- "+userId);
			
			String folder = "D:\\csdkms\\backend\\";
			String folderFinal = folder + userId + "\\"+elearningGallery.get().getGalleryName();
			//System.out.println("folder Final path = "+ folderFinal);
			
			 File tmpDir = new File(folderFinal);
			    boolean exists = tmpDir.exists();
			//System.out.println("Is the folder exists = "+ exists);
			
			
			
			
			if(elearningGallery.isPresent()) {
				User user = (User) session.getAttribute("user_session");
				Integer channel = (Integer) session.getAttribute("channel");
				//System.out.println("Present!!!");
				ElearningGalleryDetail new_detail = elearningGalleryService.storeToUserFolder(file, elearningGallery.get().getId());
				//System.out.println("New Detail --- : "+ new_detail);
				logger.uploadElearningGallery(user, elearningGallery.get().getId(), "", "Success", channel);
				List<ElearningGalleryDetail> fileList = elearningGalleryService.findByGalleryId(elearningGallery.get());
				//System.out.println("File List -- "+ fileList);
		        return JsonResult.fileList("Gallery Upload Successfully",new_detail,fileList,session);
			}else{
				//return JsonResult.errorMap(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
				//System.out.println("Not present!!!");
				return JsonResult.errorMsg("Invalid Request");
			}
		}
    }
	
	@RequestMapping("/gallery/update/{galleryId}/{userId}")
    public ResponseEntity<JsonResult> updateGallery(@PathVariable Long galleryId,
    		@PathVariable Long userId,@RequestParam("file") MultipartFile file,
    		HttpSession session) {
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			Optional<ElearningGallery> elearningGallery = elearningGalleryService.findById(galleryId);
			
			Path userFilePath = storageService.getUserFolderLocation();
			
			//System.out.println("Elearning Gallery = "+ elearningGallery.get().getId());
			Optional<ElearningQuestion> elearning = elearningQuestionRepository.findById(elearningGallery.get().getPostId());
			
			String userGalleryFolder = userFilePath.toString() + "/" + elearningGallery.get().getUserId() + "/" + elearningGallery.get().getGalleryName();
			//System.out.println("Update Gallery Id = "+ userGalleryFolder);
			
			
			 File tmpDir = new File(userGalleryFolder);
			    boolean exists = tmpDir.exists();
			//System.out.println("Is the folder exists = "+ exists);
			
			
//			System.out.println("Elearning Gallery = "+elearningGallery.isPresent() +" Elearning = "+ elearning.isPresent() +" Elearning galler user Id  = "+ elearningGallery.get().getUserId());
//			System.out.println("User Id = "+userId +" Elearning  Created By "+elearning.get().getCreatedBy()+ " Elearning original Create "+elearning.get().getOriginalCreator().getId());
//			if((elearningGallery.isPresent() && elearning.isPresent()) && 
//					(elearningGallery.get().getUserId() == userId || 
//					elearning.get().getCreatedBy() == userId || 
//					elearning.get().getOriginalCreator().getId() == userId)) {
			
			
			
			if( elearningGallery.isPresent() && elearning.isPresent()
				&& ( elearningGallery.get().getUserId().equals(userId) 
					 ||elearning.get().getCreatedBy().equals(userId) )){
				
				if(exists == false) {
					//System.out.println("No gallery folder in backend, but have record on database ");
					Path userGalleryFolderPath = Paths.get(userGalleryFolder);
					
					try {
			            Files.createDirectories(userGalleryFolderPath);
			        }
			        catch (IOException e) {
			            throw new StorageException("Could not create user folder", e);
			        }
				}
				
				
	
				ElearningGalleryDetail new_detail = elearningGalleryService.storeToUserFolder(file, galleryId);
				
				List<ElearningGalleryDetail> fileList = elearningGalleryService.findByGalleryId(elearningGallery.get());
				logger.updateElearningGallery(user, galleryId, "", "Success",channel);
		        return JsonResult.fileList("Gallery Upload Successfully",new_detail,fileList,session);
				
				//return JsonResult.ok(return_elearningGallery,"Gallery Created");
			}else{
				//return JsonResult.errorMap(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
				return JsonResult.errorMsg("Invalid Request");
			}
		}
	}
	
	@RequestMapping("/gallery/finish/{userId}")
    public ResponseEntity<JsonResult> createGallery(@PathVariable Long userId, HttpSession session) {
		//System.out.println(userId);
		Integer checkUser = common.checkUserSession(userId, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			if(submitGallery(userId) == null) {
				return JsonResult.errorMsg("No Records");
			}
			else{
				return JsonResult.ok(submitGallery(userId),"Is Finished",session);
			}
		}
    }
	
	@RequestMapping("/gallery/delete/{detailId}")
    public ResponseEntity<JsonResult> removeGallery(@PathVariable Long detailId,
    		@RequestParam(value="user") Long userId, HttpSession session) {
		//System.out.println(userId);
		User user = (User) session.getAttribute("user_session");
		Integer channel = (Integer) session.getAttribute("channel");
		Integer checkUser = common.checkUserSession(userId, session);
		Integer is_admin = common.checkAdmin(user, session);
		
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			ElearningGalleryDetail return_data = deleteGalleryDetail(detailId,user,is_admin, channel);
			if(return_data == null) {
				return JsonResult.errorMsg("No Records");
			}
			else{
				return JsonResult.ok(return_data,"Gallery Item " + detailId + "Is Deleted",session);
			}
		}
    }
	
	private ElearningGallery submitGallery(Long userId) {
		Optional<ElearningGallery> elearningGallery = elearningGalleryService.findActive(userId);
		//System.out.println(userId);
		
		if(elearningGallery.isPresent()) {
			elearningGallery.get().setModifiedAt(new Date());
			elearningGallery.get().setModifiedBy(userId);
			elearningGallery.get().setFinishedAt(new Date());
			elearningGallery.get().setIsFinished(1);
			
			ElearningGallery return_elearningGallery = elearningGalleryService.updateElearningGallery(elearningGallery.get());
			
			return return_elearningGallery;
		}
		else{
			return null;
		}
	}
	
	private ElearningGalleryDetail deleteGalleryDetail(Long detailId, User user, Integer is_admin, Integer channel) {
		Optional<ElearningGalleryDetail> elearningGalleryDetail = elearningGalleryService.findDetailById(detailId);
		//System.out.println(userId);
		
		if(elearningGalleryDetail.isPresent()) {
			Optional<ElearningGallery> elearningGallery = elearningGalleryService.findById(elearningGalleryDetail.get().getGalleryId());
			//Optional<ElearningQuestion> elearning = elearningQuestionRepository.findById(elearningGallery.get().getPostId());
			if(elearningGalleryDetail.get().getCreatedBy().equals(user.getId()) 
					|| is_admin.equals(1)
					|| (elearningGallery.isPresent())
			) {
				elearningGalleryDetail.get().setModifiedAt(new Date());
				elearningGalleryDetail.get().setModifiedBy(user.getId());
				elearningGalleryDetail.get().setDeletedAt(new Date());
				elearningGalleryDetail.get().setDeletedBy(user.getId());
				elearningGalleryDetail.get().setDeleted(1);
				
				ElearningGalleryDetail return_elearningGallery = elearningGalleryService.saveDetail(elearningGalleryDetail.get());
				logger.deleteElearningGallery(user, detailId, "", "Success",channel);
				return return_elearningGallery;
			}else {
				return null;
			}
		}
		else{
			return null;
		}
	}
	
	private String convertSecondsToTime(int seconds) {
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        long remainingSeconds = seconds % 60;

        return String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
    }
}

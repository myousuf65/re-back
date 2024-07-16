package com.hkmci.csdkms.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.EmailUtil;
import com.hkmci.csdkms.common.LogMessageProperties;
import com.hkmci.csdkms.common.OfficeWithOldVersion;
import com.hkmci.csdkms.common.PDF2IMG;
import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.Banner;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.Favorites;
import com.hkmci.csdkms.entity.FileResource;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ResourceAccessRule;
import com.hkmci.csdkms.entity.ResourceSpecialUser;
import com.hkmci.csdkms.entity.ResourceSpeicalGroup;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.Uinbox;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.exception.StorageException;
import com.hkmci.csdkms.model.BannerModel;
import com.hkmci.csdkms.model.CatAllModel;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.model.ResourceCategoryModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.model.SpecialUserGroupModel;
import com.hkmci.csdkms.repository.CatAllRepository;
import com.hkmci.csdkms.service.AccessRuleService;
import com.hkmci.csdkms.service.BannerService;
import com.hkmci.csdkms.service.CateogryService;
import com.hkmci.csdkms.service.FavoritesService;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.PopOutService;
import com.hkmci.csdkms.service.ResourceHitRateService;
import com.hkmci.csdkms.service.ResourceRatingService;
import com.hkmci.csdkms.service.ResourceService;
import com.hkmci.csdkms.service.UinboxService;
import com.hkmci.csdkms.service.UserService;
import com.hkmci.csdkms.storage.FileCopy;
import com.hkmci.csdkms.storage.ImgToPDF;
import com.hkmci.csdkms.storage.StorageFileNotFoundException;
import com.hkmci.csdkms.storage.StorageProperties;
import com.hkmci.csdkms.storage.StorageService;
import com.hkmci.csdkms.storage.WaterMark;
import com.hkmci.csdkms.wowza.WowzaTokenGenerator;
import com.lowagie.text.DocumentException;
//import com.spire.pdf.FileFormat;
//import com.spire.pdf.PdfDocument;
//import com.spire.pdf.PdfVersion;;

@CrossOrigin(
//		allowedHeaders = {"Content-Type","Accept","Authorization"},
//		allowCredentials = "TRUE",
//		origins = "http://react.holfer.com",
//		methods = {RequestMethod.GET,RequestMethod.OPTIONS,RequestMethod.POST,RequestMethod.PUT}
)
@RestController
@RequestMapping("/resource")
public class ResourceController {
	
	private final StorageService storageService;
	private final LogMessageProperties logMessage;
	
	//private final Path rootLocation;
    //private final Path MarketLocation;
    //private final Path AnotherLocation;

	@Autowired
	@Resource
    private ResourceService resourceService;
	

	@Autowired
	@Resource
    private BannerService bannerService;
	

    
    
	@Autowired
	@Resource
    private LogService logger;
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource
	private UserService userService;
	
	@Autowired
	@Resource
	private AccessRuleService accessRuleService;
	
	@Autowired
	@Resource
	private CatAllRepository catAllRepository;
	
	@Autowired
	@Resource 
	private UinboxService uinboxService;
	
	
	@Autowired
	@Resource
	private CateogryService categoryService;
	
	@Resource 
	private FavoritesService favoritesService;
	
	@Autowired
	@Resource
	private ResourceRatingService resourceRatingService;
	
	@Autowired
	@Resource
	private ResourceHitRateService resourceHitRateService;

	@Autowired
	@Resource
	private PopOutService popOutService;
	
	@Autowired
	@Resource
    private ReportController reportController;
	
	@Autowired
	public ResourceController(StorageService storageService,StorageProperties properties,LogMessageProperties logMessage) {
        this.storageService = storageService;
        this.logMessage = logMessage;
    }
	
	
	
	@RequestMapping("/all/{userId}/{accessRuleID}")
    public ResponseEntity<JsonResult> getAccessRuleList(@PathVariable Long userId,@PathVariable Long accessRuleID,HttpSession session) {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(userId,session);
		user_check.forEach((key,value)->{
	        ////System.out.println(String.format("Kenny Header '%s' = %s", key, value));
	    });
		System.out.println("Resource Controller - getAccessRuleList [end] : " + (System.currentTimeMillis() - begin));
		 //System.out.println("session "+ user_check.get("access_channel"));
		return JsonResult.ok(resourceService.findByAccessRule(accessRuleID,Long.parseLong(user_check.get("access_channel"))),session);
    }
	
	@RequestMapping("/by_user/{userId}")
    public ResponseEntity<JsonResult> getListByUserId(@PathVariable Long userId, HttpSession session) {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getListByUserId [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			if(accessRuleId.size() == 0) {
				System.out.println("Resource Controller - getListByUserId [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.errorMsg("No valid resources");
			}else {
				List<FileResource> return_data = new ArrayList<FileResource>();
				for(int i = 0; i < accessRuleId.size(); i++) {
					return_data.addAll(resourceService.findByAccessRule(accessRuleId.get(i),Long.parseLong(user_check.get("access_channel"))));
				}
				System.out.println("Resource Controller - getListByUserId [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.ok(return_data,session);
				//return JsonResult.ok(accessRuleId);
			}
		}
    }

//	@RequestMapping("/rating/{userId}")
//	public ResponseEntity<JsonResult> RatingResource(@PathVariable Long userId ,@RequestBody JsonNode jsonNode,
//			HttpSession session) throws IOException, DocumentException{
//		HashMap<String, String> user_check = common.checkUser(userId, session);
//		if (user_check.get("msg") != "") {
//			return JsonResult.errorMsg(user_check.get("msg").toString());
//		} else {
//			
//			Long resource_id = jsonNode.get("resourceId").asLong();
//			Integer rating = jsonNode.get("rating").asInt();
//			Object user_session = session.getAttribute("user_session");
//			User user = (User) user_session;
//			
//			Float return_rating = resourceRatingService.userRatingResource(userId, resource_id, rating);
//			
//			
//			return JsonResult.ok(return_rating,session);
//		}
//	}
//	
	
	@RequestMapping("/check/{resourceId}")
    public ResponseEntity<JsonResult> checkAccessRule(@PathVariable Long resourceId,
    		HttpSession session,
    		@RequestParam(value="user",required=true) Long user_id) throws IOException, DocumentException {
		long begin = System.currentTimeMillis();
		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			Object user_session = session.getAttribute("user_session");
			User user = (User) user_session;
			Integer channel = (Integer) session.getAttribute("channel");
			//System.out.println("See the channel = "+channel);
			Integer is_admin = common.checkAdmin(user, session);
			
			
			String staff_no = user.getStaffNo();
			
			
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			//System.out.println("accessRuleIdList 2  --  "+ accessRuleIdList);

			
			System.out.println("Check Access Rule: Access rule list = "+accessRuleIdList+"  resource Id = " +resourceId+" staff No =  "+ staff_no + " Is admin = "+is_admin+" access_channel"+Long.parseLong(user_check.get("access_channel")));
			Integer check_result = resourceService.checkAccessRule(accessRuleIdList,resourceId,staff_no,is_admin,Long.parseLong(user_check.get("access_channel")));
			//System.out.println("Check Access Rule: Access rule list = "+accessRuleIdList+"  resource Id = " +resourceId+" staff No =  "+ staff_no + " Is admin = "+is_admin);
			
			if(check_result.equals(0)) {

				logger.viewResource(user, resourceId, logMessage.No_Permission_To_Resource(), "Failure",channel,null);
				System.out.println("Resource Controller - checkAccessRule [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.errorMsg("No permission to access this resource. -- No.1");
			}else {
				FileResource return_resource = resourceService.findByRef(resourceId,Long.parseLong(user_check.get("access_channel")), user_id);
				//System.out.println("Resource Controller : line 235 : As watermark = "+return_resource.getAsWatermark() +" As word = "+return_resource.getAsWord() + " Wfilename = "+ return_resource.getWfilename());
				
				//Get Related Resources.
				Long accessRuleId = resourceService.getAccessRuleByResourceIdAndUser(user_id, resourceId,Long.parseLong(user_check.get("access_channel")));
				List<FileResource> related_list = resourceService.findByAccessRuleAndLimit(accessRuleId, 4,return_resource.getType())
												  .stream()
												  .filter((n) -> !n.getId().equals(resourceId))
												  .collect(Collectors.toList());	
				@SuppressWarnings("unchecked")
				List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
				String rankName = rank_session.stream()
						  .filter((n) -> n.getId().equals(user.getRankId()))
						  .map(RanksModel::getRankName)
						  .collect(Collectors.toList())
						  .get(0);
				//String rankName = "Lam Ho ";
				Integer today = userService.findTodayScoreById(user_id);
				Integer score = userService.findScoreById(user_id);
				List<Integer> category = resourceService.getCategoryString(resourceId);
				//System.out.println("User = "+ user.getId()+ " User Score ( user session )"+user.getScore()+" User Today Score ( user session )  = "+ user.getTodayScore());
				//System.out.println("Today Score (use Service )  = "+ score +"Today score( user Service ) = "+today);
//				if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
				if(today<29) {
					user.setScore(score+2);
					//today = today+2;
//					System.out.println("------Add 2 -------");
					} else if (today>28 && today<30) {
//						System.out.println("------Add 1----------");
						user.setScore(score+1);
						//today=today+1;
					}
//				}
				 Integer today2=today;
				
//				System.out.println("User Score (After view resource )"+user.getScore());
				session.setAttribute("user_session", user);
				new Thread(()-> {
					StorelogResource(session,user,channel,resourceId,category,today2,user.getScore());
				}).start();
				
				//New need add catPath
				List<ResourceCategoryModel> cates = resourceService.getResourceCategoryByResourceId(resourceId);
				List<CatAllModel> all_category = catAllRepository.findAll();
				List<FileResource> return_data_list = new ArrayList<>();
				return_data_list.add(return_resource);
				List<FileResource> return_data = return_data_list.stream().map((f) ->{
					List<Long> catId = cates.stream().filter((n)-> n.getResourceId().equals(f.getId())).map((n) -> {
						System.out.println("Resource Controller - checkAccessRule [end] : " + (System.currentTimeMillis() - begin));
						return Long.parseLong(n.getCategoryId().toString());
					}).collect(Collectors.toList());
					
					
					for(Integer i = 0 ; i<catId.size();i++) {
//						System.out.println("Resource Id = "+ catId.get(i));
					}
					
					
					
					List<Object> catPath = catId.stream().map((n)-> {
						String catpath = reportController.getParentName(n, all_category);
						//System.out.println(" Cat Path = "+catpath);
						System.out.println("Resource Controller - checkAccessRule [end] : " + (System.currentTimeMillis() - begin));
						return catpath.substring(0,catpath.length() - 4);
					}).collect(Collectors.toList());
					
					f.setCatpath(catPath);
					f.setCatId(catId);
					System.out.println("Resource Controller - checkAccessRule [end] : " + (System.currentTimeMillis() - begin));
					return f;
				}).collect(Collectors.toList());
				
				System.out.println("Rsource Controller : line 344 ");
				resourceHitRateService.updateResourceHitRate(return_data.get(0).getHitRate(), return_data.get(0).getId());
				System.out.println("Rsource Controller : line 346 ");

				
				
				
//				if(return_resource.getVideoLink() == null || return_resource.getVideoLink().equals("") || return_resource.getVideoLink().isEmpty()|| return_resource.getVideoLink().equals("null")) {//.equals("")
				System.out.println("Resource Controller : line 355: File type = "+ return_resource.getType());
				if(return_resource.getType().equals("PDF")|| return_resource.getType().equals("WORD")) {
					return_resource.setFilepath(waterMark(return_resource.getNfilename(),user_id,return_resource.getFilepath(),staff_no,rankName));
					//System.out.println(return_resource);
					System.out.println("Resource Controller : line 357 : as_word = "+ return_data.get(0).getAsWatermark() + " as_watermark = " + return_data.get(0).getAsWord() );
					System.out.println("Resource Controller - checkAccessRule [end] : " + (System.currentTimeMillis() - begin));
					return JsonResult.relatedList(return_data.get(0),related_list,session);
				}else {
				
					String video = return_resource.getVideoLink().toString();	
					System.out.println("Resource Controller : line 362 : ResourReturn Resource get Video Link = "+ video);
					String[] name = video.split("/KMS/");
					
					//TODO Set param1 to yaml file
					String param1 ="https://ams.csd.gov.hk/api/vodapi.php?";
					String param2 =	"file="+name[1]+"&folder=/KMS/&key=test1";
//					System.out.println(" Should use this "+param2);
					String param = param1 +param2;
					String videoLink =getUrlContents(param);
					System.out.println("Resource Controller: line 373 :Video Link "+ videoLink);
					return_resource.setVideoLink(videoLink);
					return_data.get(0).setVideoLink(videoLink);
					System.out.println("Resource Controller: line 376 :Video Link "+ return_data.get(0).getVideoLink());
					System.out.println("Resource Controller - checkAccessRule [end] : " + (System.currentTimeMillis() - begin));
					return JsonResult.relatedList(return_data.get(0),related_list,session);
				}
			}
			
		}
		
    }
	
	
	public void StorelogResource(HttpSession session, User user,Integer channel,Long resourceId  ,List<Integer> category,Integer today,Integer total ) {
		
		//System.out.println("----- In Resource Store Log -------");
		
		//System.out.println("user.getScore = "+ user.getScore() + " user.getTodayScore = "+ user.getTodayScore()+" today ="+ today + " total = "+total);
		
		
		//Update Score
		Log log = logger.viewResource(user, resourceId, "", "Success",channel,category);

		//Integer todayScore = logger.getUserTodayScore(user.getId());
		//Integer todayScore = user.getScore();
		
		//System.out.println("Today score - "+ today);
		Integer addScore = 30-today;
		//System.out.println(" Can add how many score "+addScore);
		if(2>addScore) {
		ScoreLog scoreLog = new ScoreLog(log.getId(),user,addScore);
		logger.saveScoreLog(scoreLog);
		Integer userScore = today+ addScore;
		//System.out.println("User Score "+ total + " Add Score "+ addScore);
		
		user.setTodayScore(userScore);
		//System.out.println("Right now user score 1 = "+userScore);
		session.setAttribute("user_session", user);
		} else {
			ScoreLog scoreLog = new ScoreLog(log.getId(),user,2);
			logger.saveScoreLog(scoreLog);
			Integer userScore = today + 2;
			user.setTodayScore(userScore);
			//System.out.println("Right now user score 2 = "+userScore);
			session.setAttribute("user_session", user);
			
		}
		
		userService.addUser(user);
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
//------------------------------------------------------------------------------------------------------	
	
	

	public ResponseEntity downloader(HttpServletRequest request) throws IOException {
		String file_path ="/Users/eva/my_project/CombineCSDKMS-Revamp-BackEnd/kms_resource/30716_af95f2865aa6d9a0b79a7fe386d867cd.xlsx";
		//System.out.println("File path: " + file_path);
		File file = new File(file_path);
		//System.out.println("File: " + file);
		InputStream in = new FileInputStream(file);
		 HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=utf-8");
		headers.add("Content-Disposition", "attachment; filename=30716_af95f2865aa6d9a0b79a7fe386d867cd.xlsx" );
		return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
	}
	
	
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/download/{resourceId}")
    public ResponseEntity download(HttpServletRequest request,@PathVariable Long resourceId) throws IOException {
		long begin = System.currentTimeMillis();
		HttpSession session = request.getSession();
		Object user_session = session.getAttribute("user_session");
		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		
		if(user_session == null) {
			return JsonResult.errorMsg("Invalid Request");
		}else{
			User user = (User) user_session;
			Integer channel = (Integer) session.getAttribute("channel");
			Integer is_admin = common.checkAdmin(user, session);
			String staff_no = user.getStaffNo();
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			String rankName = rank_session.stream()
					  .filter((n) -> n.getId().equals(user.getRankId()))
					  .map(RanksModel::getRankName)
					  .collect(Collectors.toList())
					  .get(0);
			@SuppressWarnings("unchecked")
			List<Long> accessRuleIdList = (List<Long>) session.getAttribute("user_access_rule_session");
			List<Integer> category = resourceService.getCategoryString(resourceId);
			System.out.println("accessRuleIdList--  "+ accessRuleIdList);
			Integer check_result = resourceService.checkAccessRule(accessRuleIdList,resourceId,staff_no,is_admin, Long.valueOf(channel));
			if(check_result == 0) {
				logger.downloadResource(user, resourceId, logMessage.No_Permission_To_Download(), "Failure",channel, category);
				return JsonResult.errorMsg("No permission to access this resource. -- No.1");
			}else {
				FileResource return_resource = resourceService.findByRef(resourceId, Long.valueOf(channel),user.getId());
				Path resourcePath = storageService.getResourceLocation();
				Path tempPath = storageService.getTempLocation();
				String file_path = null;
				try {
					waterMark(return_resource.getNfilename(),user.getId(),return_resource.getFilepath(),staff_no,rankName);
				} catch (IOException | DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					if ((return_resource.getAsWord() == 0 || return_resource.getAsWord() == null) 
						&& (return_resource.getAsWatermark() == 0 || return_resource.getAsWatermark() == null)) {
					file_path = resourcePath.toString() + "/pdf/water_mark/" + user.getId() + "/" + return_resource.getNfilename();
					} else if(return_resource.getAsWord() == 0 && return_resource.getAsWatermark() == 1 ) {
						String yourString = return_resource.getFilepath();
						yourString= yourString.substring(yourString.indexOf("kms_resource/") + 12 , yourString.length());
						System.out.println("yourString " + yourString);
						file_path = resourcePath.toString() + yourString+"/" + return_resource.getNfilename();
					} else {
						System.out.println("Download orginial File ");
						if(return_resource.getWfilename().contains(".pdf")) {
							String yourString = return_resource.getFilepath();
							yourString= yourString.substring(yourString.indexOf("kms_resource/") + 12 , yourString.length());
							System.out.println("yourString " + yourString);
							file_path = resourcePath.toString() + yourString+"/" + return_resource.getNfilename();
						} else {
							System.out.println("Root path = "+ resourcePath.toString()  +"/"+ return_resource.getWfilename());
							file_path = resourcePath.toString()  +"/" +return_resource.getId() +"_"+ return_resource.getWfilename();
						} 
					}
				System.out.println("File Path = "+ file_path);
	
				//Update Score
				Log log = logger.downloadResource(user, resourceId, "", "Success",channel, category);

				Integer todayScore = logger.getUserTodayScore(user.getId());
		 		Integer total =logger.getUserScore(user.getId());
				//System.out.println("Score Before download = "+total);
				Integer addScore = 30-todayScore;
				if(1>addScore) {
				ScoreLog scoreLog = new ScoreLog(log.getId(),user,addScore);
				logger.saveScoreLog(scoreLog);
				Integer userScore = total ;
//				if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
					user.setScore(userScore);
//				}
				session.setAttribute("user_session", user);
				} else {
					ScoreLog scoreLog = new ScoreLog(log.getId(),user,1);
					System.out.println("---------11DOWNLOAD111------------");
					logger.saveScoreLog(scoreLog);
					Integer userScore = total + 1;
//					if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
						user.setScore(userScore);
//					}
					user.setTodayScore(todayScore+1);
					session.setAttribute("user_session", user);
					
				}
				//System.out.println("Score After  download = "+user.getScore());
				
				userService.addUser(user);
				
//				return JsonResult.ok(file_path,"Can Download this resource",session);
				if(return_resource.getAsWord() == 0 ) {
			
//-----------------------WORKING DOWNLOAD PDF ----------------------------------------------
					//System.out.println("File path: " + file_path);
					File file = new File(file_path);
					//System.out.println("File: " + file);
//				     PdfDocument document = new PdfDocument();
////			        document.loadFromFile(input);
//			        document.loadFromFile(file_path);
//			        document.getFileInfo().setVersion(PdfVersion.Version_1_7);
//			        document.saveToFile(file_path, FileFormat.PDF);
//			        document.close();
					
					
					
				//	 PdfDocument document = new PdfDocument();
				//	 document.loadFromFile(file_path);
				//	System.out.println("Resource Controller "+ document.getFileInfo().getVersion());
				//	
					
				//if(document.getFileInfo().getVersion().toString().contains("1_3")) {
				//	return new ResponseEntity( HttpStatus.FORBIDDEN);
				//} else {
					InputStream in = new FileInputStream(file);
					final HttpHeaders headers = new HttpHeaders();
//					headers.add("Content-Type", "application/pdf");
					headers.add("Content-Type", "application/octet-streams");
					headers.add("Content-Disposition", "attachment; filename=" + return_resource.getOfilename() );
					System.out.println("Resource Controller - download [end] : " + (System.currentTimeMillis() - begin));
					return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
				//}
//-------------------END PDF DOWNLOAD ---------------------------------------------------------------
				}  
				
				else {
					//System.out.println("File path Not As PDF: " + file_path);
					//System.out.println("File path: " + file_path);
					File file = new File(file_path);
					//System.out.println("File: " + file);
					InputStream in = new FileInputStream(file);
					final HttpHeaders headers = new HttpHeaders();
					headers.add("Content-Type", "application/pdf");
					headers.add("Content-Disposition", "attachment; filename=" + return_resource.getOfilename() );
					System.out.println("Resource Controller - download [end] : " + (System.currentTimeMillis() - begin));
					return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
//					
				}
			}
		}
	}


//	Handle download documents'
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/download/multiple/{userId}")
    public ResponseEntity downloadMultiple(HttpServletRequest request,@PathVariable Long resourceId) throws IOException {
		long begin = System.currentTimeMillis();
		HttpSession session = request.getSession();
		Object user_session = session.getAttribute("user_session");
		//Object user_access_rule_session = session.getAttribute("user_access_rule_session");
		
		if(user_session == null) {
			return JsonResult.errorMsg("Invalid Request");
		}else{
			User user = (User) user_session;
			Integer channel = (Integer) session.getAttribute("channel");
			Integer is_admin = common.checkAdmin(user, session);
			String staff_no = user.getStaffNo();
			@SuppressWarnings("unchecked")
			List<Long> accessRuleIdList = (List<Long>) session.getAttribute("user_access_rule_session");
			List<Integer> category = resourceService.getCategoryString(resourceId);
			//System.out.println("accessRuleIdList--  "+ accessRuleIdList);
			Integer check_result = resourceService.checkAccessRule(accessRuleIdList,resourceId,staff_no,is_admin, Long.valueOf(channel));
			if(check_result == 0) {
				logger.downloadResource(user, resourceId, logMessage.No_Permission_To_Download(), "Failure",channel, category);
				return JsonResult.errorMsg("No permission to access this resource. -- No.1");
			}else {
				FileResource return_resource = resourceService.findByRef(resourceId, Long.valueOf(channel),user.getId());
				Path resourcePath = storageService.getResourceLocation();
				Path tempPath = storageService.getTempLocation();
				String file_path = null;
			
					if ((return_resource.getAsWord() == 0 || return_resource.getAsWord() == null) 
						&& (return_resource.getAsWatermark() == 0 || return_resource.getAsWatermark() == null)) {
					file_path = resourcePath.toString() + "/pdf/water_mark/" + user.getId() + "/" + return_resource.getNfilename();
					} else if(return_resource.getAsWord() == 0 && return_resource.getAsWatermark() == 1 ) {
						String yourString = return_resource.getFilepath();
						yourString= yourString.substring(yourString.indexOf("kms_resource/") + 12 , yourString.length());
						//System.out.println("yourString " + yourString);
						file_path = resourcePath.toString() + yourString+"/" + return_resource.getNfilename();
					} else {
						//System.out.println("Download orginial File ");
						if(return_resource.getWfilename().contains(".pdf")) {
							String yourString = return_resource.getFilepath();
							yourString= yourString.substring(yourString.indexOf("kms_resource/") + 12 , yourString.length());
							//System.out.println("yourString " + yourString);
							file_path = resourcePath.toString() + yourString+"/" + return_resource.getNfilename();
						} else {
							//System.out.println("Root path = "+ resourcePath.toString()  +"/"+ return_resource.getWfilename());
							file_path = resourcePath.toString()  +"/" +return_resource.getId() +"_"+ return_resource.getWfilename();
						} 
					}
				//System.out.println("File Path = "+ file_path);
	
				//Update Score
				Log log = logger.downloadResource(user, resourceId, "", "Success",channel, category);

				Integer todayScore = logger.getUserTodayScore(user.getId());
		 		Integer total =logger.getUserScore(user.getId());
				//System.out.println("Score Before download = "+total);
				Integer addScore = 30-todayScore;
				if(1>addScore) {
				ScoreLog scoreLog = new ScoreLog(log.getId(),user,addScore);
				logger.saveScoreLog(scoreLog);
				Integer userScore = total ;
//				if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
					user.setScore(userScore);
//				}
				session.setAttribute("user_session", user);
				} else {
					ScoreLog scoreLog = new ScoreLog(log.getId(),user,1);
					//System.out.println("---------11DOWNLOAD111------------");
					logger.saveScoreLog(scoreLog);
					Integer userScore = total + 1;
//					if(new Date ().after(common.textToDateDate("2021-05-01")) ) {
						user.setScore(userScore);
//					}
					user.setTodayScore(todayScore+1);
					session.setAttribute("user_session", user);
					
				}
				//System.out.println("Score After  download = "+user.getScore());
				
				userService.addUser(user);
				
//				return JsonResult.ok(file_path,"Can Download this resource",session);
				if(return_resource.getAsWord() == 0 ) {
			
//-----------------------WORKING DOWNLOAD PDF ----------------------------------------------
					//System.out.println("File path: " + file_path);
					File file = new File(file_path);
					//System.out.println("File: " + file);
//				     PdfDocument document = new PdfDocument();
////			        document.loadFromFile(input);
//			        document.loadFromFile(file_path);
//			        document.getFileInfo().setVersion(PdfVersion.Version_1_7);
//			        document.saveToFile(file_path, FileFormat.PDF);
//			        document.close();
					
					
					
				//	 PdfDocument document = new PdfDocument();
				//	 document.loadFromFile(file_path);
				//	System.out.println("Resource Controller "+ document.getFileInfo().getVersion());
				//	
					
				//if(document.getFileInfo().getVersion().toString().contains("1_3")) {
				//	return new ResponseEntity( HttpStatus.FORBIDDEN);
				//} else {
					InputStream in = new FileInputStream(file);
					final HttpHeaders headers = new HttpHeaders();
//					headers.add("Content-Type", "application/pdf");
					headers.add("Content-Type", "application/octet-streams");
					headers.add("Content-Disposition", "attachment; filename=" + return_resource.getOfilename() );
					System.out.println("Resource Controller - downloadMultiple [end] : " + (System.currentTimeMillis() - begin));
					return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
				//}
//-------------------END PDF DOWNLOAD ---------------------------------------------------------------
				}  
				
				else {
					//System.out.println("File path Not As PDF: " + file_path);
					//System.out.println("File path: " + file_path);
					File file = new File(file_path);
					//System.out.println("File: " + file);
					InputStream in = new FileInputStream(file);
					final HttpHeaders headers = new HttpHeaders();
					headers.add("Content-Type", "application/pdf");
					headers.add("Content-Disposition", "attachment; filename=" + return_resource.getOfilename() );
					System.out.println("Resource Controller - downloadMultiple [end] : " + (System.currentTimeMillis() - begin));
					return new ResponseEntity(IOUtils.toByteArray(in), headers, HttpStatus.OK);
//					
				}
			}
		}
	}
// End 	
	
	

	
	@RequestMapping("/all")
    public ResponseEntity<JsonResult> getAllList(HttpSession session) {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		long begin = System.currentTimeMillis();
		Long accessRuleID = (long) 0;
		String stringToConvert = String.valueOf(session.getAttribute("channel"));
        Long access_channel = Long.parseLong(stringToConvert);
        System.out.println("Resource Controller - getAllList [end] : " + (System.currentTimeMillis() - begin));
		return JsonResult.ok(resourceService.findByAccessRule(accessRuleID,access_channel),session);
    }
	
	
	
	@RequestMapping("/searchname/{userId}/{categoryId}/{searchname}")
	public ResponseEntity<JsonResult> searchByResourceName(@PathVariable Long userId,@PathVariable String searchName, HttpSession session){
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkReportUser(userId, session);
		if(user_check.get("msg")!= "") {
			System.out.println("Resource Controller - searchByResourceName [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			System.out.println("Resource Controller - searchByResourceName [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(session);
			
		}
	}
	
	
	@RequestMapping("/homepage/popout/{user_id}")
	public ResponseEntity<JsonResult> getPopOut(@PathVariable Long user_id, HttpSession session){
		long begin = System.currentTimeMillis();
		HashMap<String,String> user_check = common.checkUser(user_id, session);
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getPopOut [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			System.out.println("Resource Controller - getPopOut [end] : " + (System.currentTimeMillis() - begin));
		
			
			return JsonResult.ok(popOutService.getPopOut1(),session);
		}
		
	}
	
	@RequestMapping("/homepage/latest/{user_id}")
    public ResponseEntity<JsonResult> getListWithLatest(@PathVariable Long user_id,HttpSession session) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		
		
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getListWithLatest [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
		//	System.out.println("Staff_no : "+user_check.get("staff_no"));
			String staff_no = user_check.get("staff_no");
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
		    logger.login(user, 0L, "", "Success",Integer.parseInt(user_check.get("access_channel")));
			//TODO Admin status is already set in session this is old code.
			Integer is_admin = common.checkAdmin(user, session);
			//System.out.println("Access Rule Id (homepage) = "+ accessRuleId + " Is Admin = "+is_admin+ "Staff No = "+ staff_no);
			HashMap<String, List<?>> return_data = resourceService.findHomePageLatest(accessRuleId,is_admin,Long.parseLong(user_check.get("access_channel")),staff_no);
			System.out.println("Resource Controller - getListWithLatest [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_data,session);
			//return JsonResult.errorHttpMsg(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
			
		}
    }
	
	@RequestMapping("/getlatest")
	public ResponseEntity<JsonResult> getLates(@RequestParam(value="user", required = true) Long user_id, HttpSession session,
			@RequestParam(value = "page",required = false, defaultValue = "1")Integer page){
		long begin = System.currentTimeMillis();
		HashMap<String,String> user_check =common.checkUser(user_id, session);
		if(user_check.get("msg")!="") {
			System.out.println("Resource Controller - getLates [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			Integer is_admin = common.checkAdmin(user, session);
			String staff_no = user_check.get("staff_no");
			//System.out.println("Access Rule Id (Show all)  = "+ accessRuleId + " Is Admin = "+is_admin+ "Staff No = "+ staff_no);
			List<FileResource> return_resource = resourceService.getLatest(accessRuleId, page, staff_no, is_admin,Long.parseLong(user_check.get("access_channel")));
		
			List<Long> resource_ids = new ArrayList<>();
			if(return_resource == null || return_resource.size()==0) {
				resource_ids.add(0L);
			}else {
				resource_ids = return_resource.stream().map(FileResource::getId).collect(Collectors.toList());
			}
			
			
			
			
			List<ResourceCategoryModel> cates = resourceService.getResourceCategoryByResourceIds(resource_ids);
			List<CatAllModel> all_category = catAllRepository.findAll();
			List<FileResource> return_data = return_resource.stream().map((f) ->{
				List<Long> catId = cates.stream().filter((n)-> n.getResourceId().equals(f.getId())).map((n) -> {
					return Long.parseLong(n.getCategoryId().toString());
				}).collect(Collectors.toList());
				List<Object> catPath = catId.stream().map((n)-> {
					String catpath = reportController.getParentName(n, all_category);
					////System.out.println(" Cat Path = "+catpath);
					return catpath.substring(0,catpath.length() - 4);
				}).collect(Collectors.toList());
				
				f.setCatpath(catPath);
				f.setCatId(catId);
				return f;
			}).collect(Collectors.toList());
			
			
			//Integer total = resourceService.getCatByAreaId(category_id, accessRuleId, page,user_id, is_admin,Long.parseLong(user_check.get("access_channel")));
			
			Integer total = resourceService.getLatestTotal(accessRuleId, page, staff_no, is_admin,Long.parseLong(user_check.get("access_channel")));
			//System.out.println(">>>>>>>>>>Get Count = " + total);
//			return JsonResult.ok(return_data,session);
		//	return JsonResult.listTotal("Resource Found.",return_data, return_data.size(),session);
			System.out.println("Resource Controller - getLates [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.listTotal("Resource Found.",return_data,total,session);
		
		}
	}
	
	
	
	
	@RequestMapping("/homepage/blog/{user_id}")
    public ResponseEntity<JsonResult> getListWithBlog(@PathVariable Long user_id,HttpSession session) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getListWithBlog [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");//TODO Admin status is already set in session this is old code.
			Integer is_admin = common.checkAdmin(user, session);
			HashMap<String, List<?>> return_data = resourceService.findHomePageBlog(accessRuleId,is_admin,Long.parseLong(user_check.get("access_channel")));
			System.out.println("Resource Controller - getListWithBlog [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_data,session);
		}
    }
	
	
	@RequestMapping("/homepage/newscorner2/{user_id}")
    public ResponseEntity<JsonResult> getListWithNewsCorner(@PathVariable Long user_id,HttpSession session) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getListWithNewsCorner [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			
			//TODO Admin status is already set in session this is old code.
			Integer is_admin = common.checkAdmin(user, session);
			
			HashMap<String, List<?>> return_data = resourceService.findHomePageNewsCorner2(accessRuleId,is_admin,Long.parseLong(user_check.get("access_channel")));
			System.out.println("Resource Controller - getListWithNewsCorner [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_data,session);
		}
    }
	
	@RequestMapping("/homepage/specialCollection/{user_id}")
    public ResponseEntity<JsonResult> getListWithSpecialCollection(@PathVariable Long user_id,HttpSession session) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getListWithSpecialCollection [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			
			Integer is_admin = common.checkAdmin(user, session);
			
			HashMap<String, List<?>> return_data = resourceService.findHomePageSpecialCollection(accessRuleId,is_admin,Long.parseLong(user_check.get("access_channel")));
			System.out.println("Resource Controller - getListWithSpecialCollection [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_data,session);
		}
    }
	
	
	@RequestMapping("/share/{userId}")
	private ResponseEntity<JsonResult> shareDocument(@PathVariable Long userId, 
//			@RequestParam("resourceId") Long resourceId, @RequestParam("userIds") List<String> sendTo, 
			@RequestBody JsonNode jsonNode,
			HttpSession session) throws Exception, Throwable{
			
			long begin = System.currentTimeMillis();
		
			HashMap<String,String> user_check = common.checkUser(userId, session);
			if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			} else {
				
				Long resourceId = jsonNode.get("resourceId").asLong();
				List<Long> user_list = dealWithStaffNo(jsonNode);
				List<User> user_session_list = (List<User>) session.getAttribute("user_list");
				User user = (User) session.getAttribute("user_session"); 
				String resource_title = resourceService.getResourceNameById(resourceId);
				Integer resource_access_channel= resourceService.getResourceAccessChannelById(resourceId);
				//List<Long> user_list = dealWithSentTo(sendTo, user_session_list);
				//System.out.println("user list : " + user_list);
				List<Uinbox> shareToList = new ArrayList<>();
				for(Integer i = 0 ; i < user_list.size() ; i++ ) {
					//System.out.println("Send to = "+ user_list.get(i));
					if (user_list.get(i)!= 0) {
						Uinbox new_share = new Uinbox();
						new_share.setCreatedAt(new Date());
						new_share.setSendBy(userId);
						new_share.setIsDeleted(0);
						new_share.setIsRead(0);
						new_share.setResourceId(resourceId);
						new_share.setSendTo(user_list.get(i));
						new_share.setMailType(1L);
						uinboxService.save(new_share);
					
					}

				}
				
				List<User> sendEmaiList = user_list.stream()
						  .map((u) -> {
							  User temp_user = user_session_list
									  		  .stream()
									  		  .filter(n -> n.getId().equals(u) && u!=0 )
									  		  .collect(Collectors.toList()).get(0);
							//System.out.println("Temp_User:" + temp_user.getStaffNo() + "--" + temp_user.getEmail() + "--" + temp_user.getNotesAccount());
							
							temp_user.setEmail((user_session_list.stream()
									 .filter((n) -> n.getId().equals(temp_user.getId()))
									 .map(User::getEmail)
									 .collect(Collectors.toList())
									 .get(0)));
							//System.out.println("Temp_User222:" + temp_user.getStaffNo() + "--" + temp_user.getEmail());
							  return temp_user;
						  })
						  .collect(Collectors.toList());
				
				EmailUtil.sendEmailToShareResource(sendEmaiList, resource_title, user.getFullname(),resource_access_channel,resourceId);
				
			
				
				System.out.println("Resource Controller - shareDocument [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.ok("sent",session);
				
			}
			
	}
	
	List<Long> dealWithStaffNo(JsonNode jsonNode){
		List<Long> return_data = new ArrayList<>();
		//System.out.println("User size = " +jsonNode.get("userIds").size() );
		for(Integer i = 0 ; i < jsonNode.get("userIds").size();i++) {
			return_data.add(jsonNode.get("userIds").get(i).asLong());
		}
		
		return return_data;
	}
	
	
	List<Long> dealWithSentTo(List<String> sendTo, List<User> user_session_list){
		//System.out.println("sendTo size "+sendTo.size());
		List<Long> return_data = new ArrayList<>();
		for(Integer i = 0 ; i<sendTo.size() ;i++ ) {
			Integer x = i;
			List<Long> userId = new ArrayList<>();
			//System.out.println("user_session_list SIZE "+ user_session_list.size());
			//System.out.println("User Id = " + sendTo.get(x));
//			 userId = user_session_list.stream()
//						.filter((n) -> n.getStaffNo().equals(sendTo.get(x)))
//						.map((n) -> {return n.getId();})
//						.collect(Collectors.toList());
//			//System.out.println("User Id Deal With Sent to = "+ userId);
			//return_data.add( sendTo.get(x));		
		}
		return return_data;
	}
	
	@RequestMapping("/homepage/resource/{user_id}")
    public ResponseEntity<JsonResult> getHomepageResource(@PathVariable Long user_id,HttpSession session) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getHomepageResource [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			String staff_no = user_check.get("staff_no");
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			//System.out.println("Access Rule Id = "+accessRuleId);
			//List<Long> specialAccessRuleId = accessRuleService.specialAccessRule(user_id);
//			System.out.println("Special Access Rule ID - "+ specialAccessRuleId);
//			for(Integer i =0 ; i<specialAccessRuleId.size();i++) {
//				accessRuleId.add(specialAccessRuleId.get(i));
//			}
			//System.out.println("Total access rule id = "+ accessRuleId);
			User user = (User) session.getAttribute("user_session");
			Integer is_admin = common.checkAdmin(user, session);
			
			//System.out.println("Access Channel = "+user_check.get("access_channel"));
			HashMap<String, List<?>> return_data = resourceService.findHomePageResource(accessRuleId,is_admin,Long.parseLong(user_check.get("access_channel")),staff_no);
			System.out.println("Resource Controller - getHomepageResource [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_data,session);
		}
    }
	
	
	@RequestMapping("/homepage/banner/{user_id}")
    public ResponseEntity<JsonResult> getBannerList(@PathVariable Long user_id,HttpSession session) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		Integer channel = (Integer) session.getAttribute("channel");
		
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - getBannerList [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			Integer is_admin = common.checkAdmin(user, session);
			
			List<Banner> banner_list = bannerService.findGeneral(channel);
			//System.out.println("Banner List Size = "+ banner_list.size());
			List<BannerModel> return_banner = banner_list.stream()
											  .filter(
													  (n) -> {
														  List<Long> banner_access_rule = Arrays.asList(n.getAccessRuleId().split(","))
																  							.stream()
																  							.map((l) -> {return Long.parseLong(l);})
																  							.collect(Collectors.toList());
														  List<Long> return_result = accessRuleId.stream()
																  					 .filter(ar -> banner_access_rule.contains(ar))
																  					 .collect(Collectors.toList());
														  if(is_admin.equals(1)) {
															  return true;
														  }else {
															  if(return_result != null && return_result.size() > 0) {
																  return true;
															  }else {
																  return false;
															  }
														  }
													  }
											  )
											  .map(
													  (n) -> {
														  BannerModel return_temp = new BannerModel();
														  return_temp.setId(n.getOrderby());
														  return_temp.setImgUrl(n.getImgUrl());
														  return_temp.setTarget(n.getName());
														  return_temp.setTargetTc(n.getName_tc());
														  return_temp.setTargetUrl(n.getLinkTo());
														  return_temp.setOrderBy(n.getOrderby());
														  return return_temp;
													  }
											  )
											  .collect(Collectors.toList());
			//.sorted(Comparator.comparing(CatAllModel::getParentCatId))
			System.out.println("Resource Controller - getBannerList [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_banner,session);
		}
    }
	
	@RequestMapping("/findByID")
    public ResponseEntity<JsonResult> findByID(@RequestParam(value="ref",required=false,defaultValue="") Long ref,HttpSession session) {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		long begin = System.currentTimeMillis();
		String stringToConvert = String.valueOf(session.getAttribute("channel"));
        Long access_channel = Long.parseLong(stringToConvert);
		Long userId =1L;
		System.out.println("Resource Controller - findByID [end] : " + (System.currentTimeMillis() - begin));
         return JsonResult.ok(resourceService.findByRef(ref,access_channel,userId),session);
    }
	
	
	@RequestMapping("/searchByName")
	public ResponseEntity<JsonResult> searchByName(@RequestParam(value="user",required=true) Long user_id,
												   @RequestParam(value="categoryId",required= true) Long category_id,
												   @RequestParam(value="page",required= true) Integer page,
												   @RequestParam(value="searchWord",required=true) String search_word,
												   HttpSession session){
		long begin = System.currentTimeMillis();
		HashMap<String,String> user_check = common.checkUser(user_id, session);
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - searchByName [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else {
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			Integer is_admin = common.checkAdmin(user, session);
		
			
			
			List<Long> subcat =categoryService.findSubCatId(category_id);
			subcat.add(category_id);
			//System.out.println("Sub Category size = "+ subcat);
			
			List<FileResource> return_resource = resourceService.getBySearchName(subcat, accessRuleId, page, is_admin, Long.parseLong(user_check.get("access_channel")), search_word,user.getStaffNo());
			List<Long> resource_ids = return_resource.stream().map(FileResource::getId).collect(Collectors.toList());
			
			
			
			if(resource_ids.size()>0) {
				
				List<ResourceCategoryModel> cates = resourceService.getResourceCategoryByResourceIds(resource_ids);
				List<CatAllModel> all_category = catAllRepository.findAll();
				
				List<FileResource> return_data = return_resource.stream().map((f) ->{
					List<Long> catId = cates.stream().filter((n)-> n.getResourceId().equals(f.getId())).map((n) -> {
						return Long.parseLong(n.getCategoryId().toString());
					}).collect(Collectors.toList());
					List<Object> catPath = catId.stream().map((n)-> {
						String catpath = reportController.getParentName(n, all_category);
						//System.out.println(" Cat Path = "+catpath);
						return catpath.substring(0,catpath.length() - 4);
					}).collect(Collectors.toList());
					
					f.setCatpath(catPath);
					f.setCatId(catId);
					return f;
				}).collect(Collectors.toList());
				
				
//				Long total = resourceService.getTotalByAreaId(area_id, accessRuleId, page,is_admin);
//				
				
				Integer total = resourceService.getTotalBySearchName(subcat, accessRuleId, page, is_admin, Long.parseLong(user_check.get("access_channel")), search_word);
				System.out.println("Resource Controller - searchByName [end] : " + (System.currentTimeMillis() - begin));
				//System.out.println(">>>>>>>>>>Get Count" + total);
				return JsonResult.listTotal("Resource Found.",return_data, total,session);
				} else {
					System.out.println("Resource Controller - searchByName [end] : " + (System.currentTimeMillis() - begin));
					return JsonResult.listTotal("Resource Found.",0, 0,session);
				} 
		}
		
	}
	
	
	
	
	@RequestMapping("/getByAreaId")
    public ResponseEntity<JsonResult> findByPath(@RequestParam(value="user",required=true) Long user_id,
    											 @RequestParam(value="area_id",required=true) Long area_id,
    											 HttpSession session,
    											 @RequestParam(value ="searchWord", defaultValue ="") String keyword,
    											 @RequestParam(value="sort_by",defaultValue=" : ") String selSorter,
    											 @RequestParam(value="page",required=true) Integer page) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			String sortBy = null;
			String sortOrder = null;
			//System.out.println("sel sorter "+selSorter);
			
		
			if(!selSorter.isEmpty()) {
				sortBy = selSorter.split(":")[0];
				sortOrder = selSorter.split(":")[1];
				}
			
			
//			if(!selSorter.isBlank()) {
//			sortBy = selSorter.split(":")[0];
//			sortOrder = selSorter.split(":")[1];
//			}
			//System.out.println("Sort By = "+ sortBy + " Sort Order = "+ sortOrder);
			
			
			
			
			
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			String staffNo = user.getStaffNo();
			Integer is_admin = common.checkAdmin(user, session);
			List<FileResource> return_resource = resourceService.getByAreaId(area_id, accessRuleId, page,is_admin,Long.parseLong(user_check.get("access_channel")),sortBy,sortOrder,keyword,staffNo);
			List<Long> resource_ids = return_resource.stream().map(FileResource::getId).collect(Collectors.toList());
			//System.out.println("Get By Area Id - Resource Ids = "+ resource_ids);
			
			
			
			if(resource_ids.size()>0) {
			
			List<ResourceCategoryModel> cates = resourceService.getResourceCategoryByResourceIds(resource_ids);
			List<CatAllModel> all_category = catAllRepository.findAll();
			
			List<FileResource> return_data = return_resource.stream().map((f) ->{
				List<Long> catId = cates.stream().filter((n)-> n.getResourceId().equals(f.getId())).map((n) -> {
					return Long.parseLong(n.getCategoryId().toString());
				}).collect(Collectors.toList());
				List<Object> catPath = catId.stream().map((n)-> {
					String catpath = reportController.getParentName(n, all_category);
					//System.out.println(" Cat Path = "+catpath);
					return catpath.substring(0,catpath.length() - 4);
				}).collect(Collectors.toList());
				
				f.setCatpath(catPath);
				f.setCatId(catId);
				return f;
			}).collect(Collectors.toList());
			
			
//			Long total = resourceService.getTotalByAreaId(area_id, accessRuleId, page,is_admin);
//			
			
			Integer total = resourceService.getTotalByAreaId(area_id, accessRuleId, page, is_admin, Long.parseLong(user_check.get("access_channel")),keyword,staffNo);
			//System.out.println(">>>>>>>>>>Get Count" + total);
			System.out.println("Resource Controller - findByPath [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.listTotal("Resource Found.",return_data, total,session);
			} else {
				System.out.println("Resource Controller - findByPath [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.listTotal("Resource Found.",0, 0,session);
			}
		}
    }
	
	@RequestMapping("/test")
	public List<ResourceCategoryModel> testing() {
		long begin = System.currentTimeMillis();
		List<ResourceCategoryModel> return_type = resourceService.findCategory();
		System.out.println("Resource Controller - testing [end] : " + (System.currentTimeMillis() - begin));
		return (return_type);
	}
	
	
	@RequestMapping("/getlates")
	public ResponseEntity<JsonResult> getLatest(@RequestParam(value="user", required = true) Long user_id, HttpSession session,
			@RequestParam(value = "page",required = false, defaultValue = "1")Integer page){
		long begin = System.currentTimeMillis();
		HashMap<String,String> user_check =common.checkUser(user_id, session);
		if(user_check.get("msg")!="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		} else {
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			Integer is_admin = common.checkAdmin(user, session);
			
			List<FileResource> return_resource = resourceService.getLatest(accessRuleId, page, user_id, is_admin,Long.parseLong(user_check.get("access_channel")));
		
			List<Long> resource_ids = new ArrayList<>();
			if(return_resource == null || return_resource.size()==0) {
				resource_ids.add(0L);
			}else {
				resource_ids = return_resource.stream().map(FileResource::getId).collect(Collectors.toList());
			}
			
			
			List<ResourceCategoryModel> cates = resourceService.getResourceCategoryByResourceIds(resource_ids);
			List<CatAllModel> all_category = catAllRepository.findAll();
			List<FileResource> return_data = return_resource.stream().map((f) ->{
				List<Long> catId = cates.stream().filter((n)-> n.getResourceId().equals(f.getId())).map((n) -> {
					return Long.parseLong(n.getCategoryId().toString());
				}).collect(Collectors.toList());
				List<Object> catPath = catId.stream().map((n)-> {
					String catpath = reportController.getParentName(n, all_category);
					//System.out.println(" Cat Path = "+catpath);
					return catpath.substring(0,catpath.length() - 4);
				}).collect(Collectors.toList());
				
				f.setCatpath(catPath);
				f.setCatId(catId);
				return f;
			}).collect(Collectors.toList());
			
			
			//Integer total = resourceService.getCatByAreaId(category_id, accessRuleId, page,user_id, is_admin,Long.parseLong(user_check.get("access_channel")));
			
			Integer total = resourceService.getLatestTotal(accessRuleId, page, user_id, is_admin,Long.parseLong(user_check.get("access_channel")));
			//System.out.println(">>>>>>>>>>Get Count = " + total);
//			return JsonResult.ok(return_data,session);
		//	return JsonResult.listTotal("Resource Found.",return_data, return_data.size(),session);
			System.out.println("Resource Controller - getlates [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.listTotal("Resource Found.",return_data,total,session);
		
		}
	}

	
	@RequestMapping("/getByCategoryId")
	  public ResponseEntity<JsonResult> findByCate(@RequestParam(value="user",required=true) Long user_id,
				 @RequestParam(value="categoryId",required=true) Long category_id,
				 HttpSession session,
				 @RequestParam(value="page",required=false, defaultValue = "1") Integer page) {
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		//System.out.println("Staff No( by cat)  = "+ user_check.get("staff_no"));
		
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - findByCate [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			List<Long> accessRuleId = getAccessRuleIds(user_check,session);
			User user = (User) session.getAttribute("user_session");
			Integer is_admin = common.checkAdmin(user, session);
			String staff_no = user_check.get("staff_no");
//			List<Long> other_accessRuleId = accessRuleService.findAll()
//					.stream().filter(
//							(car) -> car.getDeletedBy() == null && !accessRuleId.contains(car.getId())
//					).map(
//							(car) -> {
//								return car.getId();
//							}
//					).collect(Collectors.toList());
			List<FileResource> return_resource =resourceService.getByCategoryId(category_id, accessRuleId, page,user_id, is_admin,staff_no,Long.parseLong(user_check.get("access_channel")));
			List<Long> resource_ids = new ArrayList<>();
			if(return_resource == null || return_resource.size()==0) {
				resource_ids.add(0L);
			}else {resource_ids = return_resource.stream().map(FileResource::getId).collect(Collectors.toList());}
			
			List<ResourceCategoryModel> cates = resourceService.getResourceCategoryByResourceIds(resource_ids);
			List<CatAllModel> all_category = catAllRepository.findAll();
			List<FileResource> return_data = return_resource.stream().map((f) ->{
				List<Long> catId = cates.stream().filter((n)-> n.getResourceId().equals(f.getId())).map((n) -> {
					return Long.parseLong(n.getCategoryId().toString());
				}).collect(Collectors.toList());
				List<Object> catPath = catId.stream().map((n)-> {
					String catpath = reportController.getParentName(n, all_category);
					//System.out.println(" Cat Path = "+catpath);
					return catpath.substring(0,catpath.length() - 4);
				}).collect(Collectors.toList());
				
				f.setCatpath(catPath);
				f.setCatId(catId);
				return f;
			}).sorted(Comparator.comparing(FileResource::getTitleEn))
					.collect(Collectors.toList());
			
			
			Integer total = resourceService.getCatByAreaId(category_id, accessRuleId, page,staff_no, is_admin,Long.parseLong(user_check.get("access_channel")));
			//System.out.println(">>>>>>>>>>Get Count = " + total);
//			return JsonResult.ok(return_data,session);
		//	return JsonResult.listTotal("Resource Found.",return_data, return_data.size(),session);
			System.out.println("Resource Controller - findByCate [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.listTotal("Resource Found.",return_data,total,session);
		}
	}

	
	
	
	@CrossOrigin
	@RequestMapping("/findByName")
    public ResponseEntity<JsonResult> findByName(@RequestParam(value="searchValue",required=false,defaultValue="") String filename,HttpSession session) {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		long begin = System.currentTimeMillis();
		String stringToConvert = String.valueOf(session.getAttribute("channel"));
        Long access_channel = Long.parseLong(stringToConvert);
        System.out.println("Resource Controller - findByName [end] : " + (System.currentTimeMillis() - begin));
         return JsonResult.ok(resourceService.getByFileName(filename, access_channel),session);
    }
	
	@RequestMapping("/findByDate")
    public ResponseEntity<JsonResult> findByCreationDate(@RequestParam(value="searchValue",required=false,defaultValue="") String creationdate,HttpSession session) throws Exception {
		long begin = System.currentTimeMillis();
		StringToDate tester = new StringToDate();
         //model.addAttribute("Resource", ResourceRepository.findAll());
		Date searchDate = tester.parseStringV2(creationdate);
		//Date current = new Date();
		//searchDate = current.toLocaleString();
		System.out.println("Resource Controller - findByCreationDate [end] : " + (System.currentTimeMillis() - begin));
		return JsonResult.ok(resourceService.getByDate(searchDate),session);
    }
	
	@RequestMapping("/getPath")
    public ResponseEntity<JsonResult> getPath(@RequestParam(value="filename",required=false,defaultValue="") String filename,HttpSession session) {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		long begin = System.currentTimeMillis();
		String old_file_path = storageService.getPath(filename);
		String new_file_path = old_file_path.replace("upload-dir", "kms-resource");
		FileCopy.copy(old_file_path, new_file_path, filename);
		System.out.println("Resource Controller - getPath [end] : " + (System.currentTimeMillis() - begin));
        return JsonResult.ok(old_file_path + ";" + new_file_path,session);
    }
	
	@RequestMapping("/search")
	public ResponseEntity<JsonResult> search(@RequestParam(value="id", defaultValue="0" ) Long resource_id, 
											 @RequestParam(value="user") Long user_id,
											 @RequestParam(value="category",defaultValue="0") Long category,
											 @RequestParam(value="subcategory",defaultValue="0") Integer subcategory,
											 @RequestParam(value="title", defaultValue="") String title,
											 @RequestParam(value="startdate", defaultValue="0") Long startdate_str,
											 @RequestParam(value="enddate", defaultValue="0") Long enddate_str,
											 @RequestParam(value="ln", defaultValue="-1") Integer ln,
											 @RequestParam(value="km", defaultValue="-1") Long km,
											 @RequestParam(value="ks", defaultValue="-1") Long ks,
											 @RequestParam(value="wg", defaultValue="-1") Long wg,
											 @RequestParam(value="page", defaultValue="1") Integer page,
											 HttpSession session
											 ){
//	public ResponseEntity<JsonResult> search(HttpServletRequest req){
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - search [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			//HashMap<String, Object> session_data = getSessions(request);
			User user_session = (User) session.getAttribute("user_session");
			@SuppressWarnings("unchecked")
			List<Long> accessRuleId = (List<Long>)session.getAttribute("user_access_rule_session");
			//System.out.println(">>>>>>>>>>>>User Access Info: " + 
//					user_session.getSectionId() + "," + 
//					user_session.getInstitutionId() + "," + 
//					user_session.getRankId());
			//System.out.println(">>>>>>>>>>>>New Access Rule: " + accessRuleId);
			
			if(accessRuleId == null || accessRuleId.size() == 0) {
				System.out.println("Resource Controller - search [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.errorMsg("No valid resources");
			}else {
				//TODO Check Admin Role
				Integer is_admin = common.checkAdmin(user_session,session);
				//System.out.println("Is Admin: " + is_admin);
				List<FileResource> return_data = new ArrayList<FileResource>();
				SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
				Timestamp startdate = startdate_str != 0 ? new Timestamp(startdate_str) : null;
				Date startDate;
				try {
					startDate = ymdFormat.parse(ymdFormat.format(new Date(startdate.getTime())));
				} catch (Exception e) {
					//Auto-generated catch block
					startDate = null;
					//e.printStackTrace();
				}
				
				km = km == -1L ? -1L : 1L;
				ks = ks == -1L ? -1L : 2L;
				wg = wg == -1L ? -1L : 3L;
				
				Timestamp enddate = enddate_str != 0 ? new Timestamp(enddate_str) : null;
				Date endDate;
				try {
					endDate = ymdFormat.parse(ymdFormat.format(new Date(enddate.getTime())));
				} catch (Exception e) {
					endDate = null;
					//e.printStackTrace();
				}
				List<Long> subcategory_list = new ArrayList<Long>();
				if(subcategory == 0) {
					subcategory_list.add(category);
				}else {
					List<CatAllModel> all_category = catAllRepository.findAll();
					List<Long> all_with_sub = getSubcategoryList(category,category,all_category);
					
					
					if(all_with_sub == null || all_with_sub.size() == 0) {
						all_with_sub.add(0L);
					}
					for(Integer i =0; i<all_with_sub.size() ;i++) {
						//System.out.println(""+ all_with_sub.get(i));
					}
					subcategory_list.addAll(all_with_sub);
				}
				
				
				Date start =null;
				if (startdate != null) {
					Date end = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(startdate);
					c.add(Calendar.DATE, 0);
					end =c.getTime();
					
					
					
					try {
						start = ymdFormat.parse(ymdFormat.format(new Date(end.getTime())));
					} catch (Exception e) {
						start = null;
					}
					}
				
				Date finish =null;
				
				if (endDate != null) {
				Date end = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(enddate);
				c.add(Calendar.DATE, 1);
				end =c.getTime();
				
				
				
				try {
					finish = ymdFormat.parse(ymdFormat.format(new Date(end.getTime())));
				} catch (Exception e) {
					finish = null;
				}
				}
				
				return_data.addAll(resourceService.findByUserIdAndSearch(accessRuleId,resource_id,title,start,finish,ln,km,ks,wg,page,is_admin,user_id,subcategory_list,Long.parseLong(user_check.get("access_channel"))));
				
				//System.out.println("return_data: " + return_data.size());
				Long total_count = resourceService.getTotal(accessRuleId,resource_id,title,start,finish,ln,km,ks,wg,page,is_admin,user_id,subcategory_list,Long.parseLong(user_check.get("access_channel")));
				//System.out.println(">>>>>>>>>>Get Count" + total_count);
				
//				List<FileResource> return_data = resourceService.findByUserIdAndSearch(accessRuleId,resource_id,title,startDate,endDate,ln,km,ks,wg)
//												 .stream().collect(Collectors.toList());
				List<Long> resource_ids = return_data.stream().map(FileResource::getId).collect(Collectors.toList());
				if(resource_ids.size() == 0 || resource_ids == null) {
					resource_ids.add(0L);
					return JsonResult.list("File not Found",null,0,session);
				}
				//Native Query returns a String
                List<AccessRule> access_rules = resourceService.getAccessRulesByResourceIds(resource_ids);
                
                List<ResourceAccessRule> resource_access_rules = resourceService.getAccessRuleByResourceIds(resource_ids);
                //System.out.println("Resource Ids: " + resource_ids);
                //System.out.println("Access Rule Size: " + access_rules.size());
                //System.out.println("Resource Access Rule Size: " + resource_access_rules.size());
                
                List<ResourceCategoryModel> resource_category_inDB = resourceService.getResourceCategoryByResourceIds(resource_ids);
                List<ResourceSpecialUser> resource_special_user_inDB = resourceService.getResourceSpecialUser(resource_ids);
                //List<SpecialUserGroupModel> resoruce_special_group_inDB = resourceService.getResourceSpecialGroup(resource_ids);
                
                List<Object> rList = new ArrayList<>();
				for(Integer j = 0; j < resource_ids.size(); j++) {
					Integer x = j;
					//System.out.println(resource_ids.get(j).toString());
					//List<String> resource_special_users = resourceService.getSpecialUserByResourceId(resource_ids.get(j));
					List<String> resource_special_users = resource_special_user_inDB.stream().filter(
							rsu -> rsu != null && rsu.getResourceId().toString().equals(resource_ids.get(x).toString())
						).map(
							(rc) -> {return rc.getStaffNo();}
						).collect(Collectors.toList());
					//all_special_users_by_resource.add(resource_special_users);
					List<SpecialUserGroupModel> resource_special_group =resourceService.getResourceSpecialGroup(resource_ids.get(x));
					
					
					List<Long> resource_category = resource_category_inDB.stream().filter(
								(rc) -> rc != null && rc.getDeletedBy() != null && rc.getDeletedBy().equals(0L) && 
										rc.getResourceId().toString().equals(resource_ids.get(x).toString())
							).map(
								(rc) -> {return Long.parseLong(rc.getCategoryId().toString());}
							).collect(Collectors.toList());
					//all_resource_category.add(resource_category);
					
					HashMap<String,Object> return_object = new HashMap<>();
					//List<String> rarList = new ArrayList<>();
					
					List<Long> resource_access_rule_ids = resource_access_rules.stream()
														  .filter((rc) -> rc.getResource().equals(resource_ids.get(x)))
														  .map(ResourceAccessRule::getAccessRule)
														  .collect(Collectors.toList());
					//System.out.println("resource_access_rule_ids: " + resource_access_rule_ids);
					HashMap<String, List<AccessRule>> ResourceAccessRuleList = new HashMap<>();
					ResourceAccessRuleList.put("kmarket", access_rules.stream()
											   .filter((ar) -> ar != null && ar.getAreaId() != null && ar.getAreaId().equals(1L) && resource_access_rule_ids.contains(ar.getId()))
											   .map((ar) -> {
												   ar.setInstId(null);
												   //ar.setAreaId(null);
												   return ar;
											   })
											   .collect(Collectors.toList()));
					ResourceAccessRuleList.put("ksquare", access_rules.stream()
											   .filter((ar) -> ar != null && ar.getAreaId() != null && ar.getAreaId().equals(2L) && resource_access_rule_ids.contains(ar.getId()))
											   .map((ar) -> {
												   ar.setInstId(null);
												   //ar.setAreaId(null);
												   return ar;
											   })
											   .collect(Collectors.toList()));
					ResourceAccessRuleList.put("wisdomgallery", access_rules.stream()
													 .filter((ar) -> ar != null && ar.getAreaId() != null && ar.getAreaId().equals(3L) && resource_access_rule_ids.contains(ar.getId()))
													 .map((ar) -> {
														 	ar.setInstId(null);
														 	//ar.setAreaId(null);
														 	return ar;
													   })
													 .collect(Collectors.toList()));
//					List<String> ResourceAccessRuleList = all_resource_access_rule.stream().filter(
//								rar -> rar.getResource().equals(return_data.get(x).getId())
//							).map(
//								(rar) -> {
//									return rar.getAccessRule().toString();
//								}
//							).collect(Collectors.toList());
					
					
					return_object.put("resource", return_data.get(j));
					return_object.put("accessRuleList", ResourceAccessRuleList);
					return_object.put("categoryId", resource_category);
					return_object.put("special_user", resource_special_users);
					return_object.put("special_group", resource_special_group);
				 
					rList.add(return_object);
				}

				
				if(return_data.size() == 0) {
					System.out.println("Resource Controller - search [end] : " + (System.currentTimeMillis() - begin));
					return JsonResult.list("File not Found",null,0,session);
				}else{
					System.out.println("Resource Controller - search [end] : " + (System.currentTimeMillis() - begin));
					return JsonResult.list("File Found",rList ,Integer.parseInt(total_count.toString()),session);
				}
			
			}
		}

	}
	
	@RequestMapping("/getAllRAR/{user_id}")
	public ResponseEntity<JsonResult> getAllRAR(@PathVariable Long user_id,HttpSession session){
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(user_id,session);
		if(user_check == null || user_check.get("msg") != "") {
			if(user_check == null) {
				return JsonResult.errorMsg("No access rule to access here.");
			}else {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			}
		}else{
			//AccessRule with user access rule
			List<AccessRule> accessRuleIdList = getAccessRules(user_check,user_id);
			HashMap<String, List<AccessRule>> return_data = new HashMap<>();
			return_data.put("kmarket", accessRuleIdList.stream()
									   .filter((ar) -> ar != null && ar.getAreaId() != null && ar.getAreaId().equals(1L))
									   .map((ar) -> {
										   if(ar == null) {
											   return null;
										   }else {
											   return ar;
										   }
									   })
									   .collect(Collectors.toList()));
			return_data.put("ksquare", accessRuleIdList.stream()
									   .filter((ar) -> ar != null && ar.getAreaId() != null && ar.getAreaId().equals(2L))
									   .map((ar) -> {
										   if(ar == null) {
											   return null;
										   }else {
											   return ar;
										   }
									   })
									   .collect(Collectors.toList()));
			return_data.put("wisdomgallery", accessRuleIdList.stream()
											 .filter((ar) -> ar != null && ar.getAreaId() != null && ar.getAreaId().equals(3L))
											 .map((ar) -> {
												 if(ar == null) {
													   return null;
												   }else {
													   return ar;
												   }
											   })
											 .collect(Collectors.toList()));
			System.out.println("Resource Controller - getAllRAR [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_data,session);
		}
	}
	

	private List<AccessRule> getAccessRules(HashMap<String, String> user_check, Long user_id) {
		Long section = Long.parseLong(user_check.get("user_section").toString());
		Long institution = Long.parseLong(user_check.get("user_institution").toString());
		Long rank = Long.parseLong(user_check.get("user_rank").toString());
		//Long accessRuleId = accessRuleService.getIdByUser(section, institution, rank);
		List<AccessRule> accessRules = accessRuleService.getIdByUserId(section, institution, rank,user_id)
									   .stream()
									   .map(
											   (ar) -> {
												   ar.setRankIdList(null);
												   ar.setSectionIdList(null);
												   ar.setInstIdList(null);
												   ar.setInstId(null);
												   //ar.getSectionIdList().clear();
												   //ar.getRankIdList().clear();
												   return ar;
											   }
										)
									   .collect(Collectors.toList());
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//System.out.println(accessRules.stream().map(AccessRule::getRankIdList).collect(Collectors.toList()));
		return accessRules;
	}  

	private List<Long> getSubcategoryList(Long categoryId,Long parentId,List<CatAllModel> all_category) {
			List<CatAllModel> entity_list = all_category.stream()
											.filter( 
												(n) -> n.getId().equals(categoryId)
											).collect(Collectors.toList());
			//System.out.println("Node ID is: " + categoryId);
			List<Long> back_data = new ArrayList<Long>();
			if(entity_list.size() != 0) {
				//ID EXISTED
				List<Long> its_children = all_category.stream()
										  .filter( 
												(n) -> n.getParentCatId().equals(categoryId)
										  ).map(
												(n) -> {
													return n.getId();
												}
										  ).collect(Collectors.toList());
				back_data.add(categoryId);
				for (Integer i = 0; i < its_children.size(); i++) {
					//System.out.println("Children's ID is: " + its_children.get(i));
					List<Long> childrens_children = getSubcategoryList(its_children.get(i),parentId,all_category);
					back_data.addAll(childrens_children);
				}
				return back_data;
			}else {
				//System.out.println("Category ID is not existed.");
				back_data.add(0L);
				return back_data; 
			}

	}

	@RequestMapping("/create")
	public ResponseEntity<JsonResult> multipleCreate(@RequestBody JsonNode jsonNode,@RequestParam(value="user") Long user_id,HttpSession session) throws Exception, Throwable{
		long begin = System.currentTimeMillis();
		 String os = System.getProperty("os.name");
		 HashMap<String, String> user_check = common.checkUser(user_id,session);
			if(user_check.get("msg") != "") {
				return JsonResult.errorMsg(user_check.get("msg").toString());
			}else{
				User user = (User) session.getAttribute("user_session");
				for(Integer i = 0; i <  jsonNode.size(); i++) {
			
					FileResource new_Resource = new FileResource();
			
					Date latestNewsExpiry = common.textToDate(jsonNode.get(i).get("latestNewsExpiry").asText());
		
					new_Resource.setAccessChannel( String.valueOf(getAccessChannel(jsonNode.get(i))) );
					new_Resource.setActivated(jsonNode.get(i).get("activated").asInt());
					new_Resource.setCreatedAt(new Date());
					new_Resource.setCreatedBy(user_id);
					new_Resource.setDeleted(0);
		
					new_Resource.setDescEn(jsonNode.get(i).get("descEn").asText());
					new_Resource.setDescTc(jsonNode.get(i).get("descTc").asText());
					new_Resource.setLatestNews(jsonNode.get(i).get("latestNews").asInt());
					new_Resource.setLatestNewsExpiry(latestNewsExpiry);
					new_Resource.setOfilename(jsonNode.get(i).get("originalname").asText());
					new_Resource.setTitleEn(jsonNode.get(i).get("titleEn").asText());
					new_Resource.setTitleTc(jsonNode.get(i).get("titleTc").asText());
					new_Resource.setVideoLink(jsonNode.get(i).get("videoLink").asText());
					new_Resource.setResourceTypeId(1);
					new_Resource.setAsWatermark(jsonNode.get(i).get("downloadType").asInt());
					new_Resource.setAsWord(jsonNode.get(i).get("downloadOriginal").asInt());
////					
					
					if (jsonNode.get(i).get("downloadOriginal").asInt() ==1 ) {
						new_Resource.setWfilename(jsonNode.get(i).get("nfilename").asText());
					}
					//System.out.println(jsonNode.get(i).get("filesize").asText());
					if(jsonNode.get(i).get("filesize").asText() != "null") {
						new_Resource.setType("PDF");
						new_Resource.setFilesize(jsonNode.get(i).get("filesize").asText());
				
					}else {
						new_Resource.setType("VIDEO");
					}
		
					//Store to database
					FileResource added_resource = resourceService.addResource(new_Resource);
		
					//Update to database
					//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<You will find ID here<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
					//System.out.println(added_resource.getId());
					String folder_name = "";
					Integer file_id = Integer.parseInt(String.valueOf(added_resource.getId()));
					if(file_id % 1000 == 0) {
						folder_name =  String.valueOf(((file_id / 1000)) * 1000);
					}else {
						folder_name =  String.valueOf(((file_id / 1000) +  1) * 1000);
					}
					if(added_resource.getType().equals("VIDEO")) {
						//System.out.println("Get Type is Video");
						String file_name = jsonNode.get(i).get("thumbUrl").asText();
						//System.out.println("N file name = "+ file_name);
						file_name = file_name.substring(file_name.lastIndexOf("/") + 1);
						//System.out.println("N file name 2 = "+file_name );
						FileResource added_file_resource = moveToFolder(file_name, os, folder_name, added_resource);
						resourceService.updateResourceByRef(added_file_resource);
				
					}else {
				
						//System.out.println("Get Type is PDF");
						String file_name = jsonNode.get(i).get("nfilename").asText();
						//System.out.println("N file name = "+ file_name);
						FileResource added_file_resource = moveToFolder(file_name, os, folder_name, added_resource);
						resourceService.updateResourceByRef(added_file_resource);
					}
			
			
					List<Long> resourceAccessRuleList = getResourceAccessRule(jsonNode.get(i));
					for(Integer j = 0; j < resourceAccessRuleList.size(); j++) {
						resourceService.createResourceAccessRule(resourceAccessRuleList.get(j), added_resource.getId());
					}
//   ------------------------------------------new add for Resource Category-----------------------------------------------------	
			
					List<Long> resourceCategoryList = getResourceCategory(jsonNode.get(i));
//			
					//System.out.println("Add category for New category");
					for(Integer j=0; j< resourceCategoryList.size();j++) {
						//System.out.println("Resource Category ----- j is: " + j + "\n J value: " + resourceCategoryList.get(j)+ "\n ID is: " + added_resource.getId());
						resourceService.createResourceCategory(resourceCategoryList.get(j), added_resource.getId());
					}
//			
//	-----------------------------end for add Resource Category ------------------------------------------------------------------		
			
					List<String> resourceSpecialUserList = getResourceSpecialUser(jsonNode.get(i));
					for(Integer z = 0; z < resourceSpecialUserList.size(); z++) {
						resourceService.createResourceSpecialUser(resourceSpecialUserList.get(z), added_resource.getId(),user_id);
					}
			
					List<Integer> resoruceSpecialGroupList = getResourceSpecialUserList(jsonNode.get(i));
					//System.out.println("resoruce Special Group List = "+ resoruceSpecialGroupList.size());
					for(Integer x = 0 ; x < resoruceSpecialGroupList.size() ; x++ ) {
						resourceService.createResoruceSpecialGroup(resoruceSpecialGroupList.get(x), added_resource.getId(), user_id);
					}
//					
					
					//System.out.println("**Session Channel - " +session.getAttribute("channel") );
			//Thread.sleep(1000);
					logger.createResource(user,  added_resource.getId(), "", "Success", Integer.valueOf(session.getAttribute("channel").toString()), resourceCategoryList);
				}
			}
		System.out.println("Resource Controller - multipleCreate [end] : " + (System.currentTimeMillis() - begin));
		return JsonResult.ok(jsonNode,session);
	}
	
	public Integer getAccessChannel(JsonNode jsonNode ) {
		////System.out.println("Access Channel size = "+ jsonNode.get("accessChannel").size());
		if( jsonNode.get("accessChannel") == null ||  jsonNode.get("accessChannel").size() ==1 ) {
			return 1;
		}else {
			return 2;
		}
	}
	
	private List<Long> getResourceAccessRule(JsonNode jsonNode ) {
		
		List<Long> return_data = new ArrayList<Long>();
		
		Integer k_market_size = jsonNode.get("k_market") == null ? 0 : jsonNode.get("k_market").size();
		Integer k_square_size = jsonNode.get("k_square") == null ? 0 : jsonNode.get("k_square").size();
		Integer wisdom_gallery_size = jsonNode.get("wisdom_gallery") == null ? 0 : jsonNode.get("wisdom_gallery").size();
		
		for(Integer i = 0; i < k_market_size; i++) {
			return_data.add(jsonNode.get("k_market").get(i).asLong());
		}
		for(Integer i = 0; i < k_square_size; i++) {
			return_data.add(jsonNode.get("k_square").get(i).asLong());
		}
		for(Integer i = 0; i < wisdom_gallery_size; i++) {
			return_data.add(jsonNode.get("wisdom_gallery").get(i).asLong());
		}
		
		return return_data;
	}
	
	
	private List<Long> getResourceCategory(JsonNode jsonNode){
		List<Long> return_data = new ArrayList<Long>();
		if(jsonNode.get("resCate") == null) {
			return return_data;
		}else {
			for(Integer i =0; i < jsonNode.get("resCate").size(); i++) {
				return_data.add(jsonNode.get("resCate").get(i).asLong());
			}
			return return_data;
		}
	}
	
	
	
	private List<String> getResourceSpecialUser(JsonNode jsonNode ) {
		
		List<String> return_data = new ArrayList<>();
		Integer special_user_size = jsonNode.get("special_user") == null ? 0 : jsonNode.get("special_user").size();
		for(Integer i = 0; i < special_user_size; i++) {
			return_data.add(jsonNode.get("special_user").get(i).asText());
		}
		
		return return_data;
	}
	
	
	private List<Integer> getResourceSpecialUserList(JsonNode jsonNode ) {
		
		List<Integer> return_data = new ArrayList<>();
		Integer special_user_size = jsonNode.get("special_user_list") == null ? 0 : jsonNode.get("special_user_list").size();
		
		//System.out.println("special user size = "+ special_user_size);
		for(Integer i = 0; i < special_user_size; i++) {
			
			 //System.out.println("special user = "+jsonNode.get("special_user_list").get(i).asInt());
			return_data.add(jsonNode.get("special_user_list").get(i).asInt());
		}
		
		return return_data;
	}

	@RequestMapping("/update")
	public ResponseEntity<JsonResult> updateResource(@RequestBody JsonNode jsonNode,
			@RequestParam(value="user") Long userId,HttpSession session) throws Exception, Throwable{
		//FileResource new_Resource = new FileResource();
		//Object form_data = req;
		//return JsonResult.ok(jsonNode.get("id"));
		long begin = System.currentTimeMillis();
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			//System.out.println("**Session Channel - " +session.getAttribute("channel") );
			String stringToConvert = String.valueOf(session.getAttribute("channel"));
			Long access_channel = Long.parseLong(stringToConvert);
		
			//System.out.println("Update in access channel = "+ access_channel);
		
			FileResource new_Resource = resourceService.findByRef(jsonNode.get("id").asLong(),access_channel,userId);
		
			Date latestNewsExpiry = common.textToDate(jsonNode.get("updates").get("latestNewsExpiry").asText());
			new_Resource.setAccessChannel( String.valueOf(getAccessChannel(jsonNode.get("updates"))) );
			//new_Resource.setAccessChannel( String.valueOf(getAccessChannel(jsonNode.get("updates").get("accessChannel"))) );
			new_Resource.setLatestNewsExpiry(latestNewsExpiry);
			new_Resource.setLatestNews(jsonNode.get("updates").get("latestNews").asInt());
			new_Resource.setTitleEn(jsonNode.get("updates").get("titleEn").asText());
			new_Resource.setTitleTc(jsonNode.get("updates").get("titleTc").asText());
			new_Resource.setLatestNewsExpiry(latestNewsExpiry);
			new_Resource.setLatestNews(jsonNode.get("updates").get("latestNews").asInt());
			new_Resource.setDescEn(jsonNode.get("updates").get("descEn").asText());
			new_Resource.setDescTc(jsonNode.get("updates").get("descTc").asText());
			new_Resource.setVideoLink(jsonNode.get("updates").get("videoLink").asText());
			new_Resource.setActivated(jsonNode.get("updates").get("activated").asInt());
			new_Resource.setModifiedBy(userId);
			new_Resource.setAsWatermark(jsonNode.get("updates").get("downloadType").asInt());
			new_Resource.setAsWord(jsonNode.get("updates").get("downloadOriginal").asInt());
			
			if (jsonNode.get("updates").get("downloadOriginal").asInt() == 1 ) {
				new_Resource.setWfilename(jsonNode.get("updates").get("nfilename").asText());
			}
			
			
////			
			new_Resource.setModifiedAt(new Date());
		
		
		
			//System.out.println("Old New File name = "+ jsonNode.get("updates").get("nfilename").asText() + " New new file name = " +new_Resource.getNfilename());
		
			//System.out.println("not (update nfilename = database n filename) "+ !jsonNode.get("updates").get("nfilename").asText().equals(new_Resource.getNfilename()));
		
		
			//Check if file updated
			if(jsonNode.get("updates").get("nfilename") != null && !jsonNode.get("updates").get("nfilename").asText().equals(new_Resource.getNfilename())) {
				//System.out.println("Edit in Update - move to folder");
			
				//Move to folder
				new_Resource.setNfilename(jsonNode.get("updates").get("nfilename").asText());
				String file_name = jsonNode.get("updates").get("nfilename").asText();
				//System.out.println("Reupload filename "+jsonNode.get("updates").get("nfilename").asText());
				String os = System.getProperty("os.name");
				String folder_name = "";
				Integer file_id = Integer.parseInt(String.valueOf(jsonNode.get("id").asLong()));
				if(file_id % 1000 == 0) {
					folder_name =  String.valueOf(((file_id / 1000)) * 1000);
				}else {
					folder_name =  String.valueOf(((file_id / 1000) +  1) * 1000);
				}
				new_Resource = moveToFolder(file_name, os, folder_name, new_Resource);
			} else {
				//System.out.println("NO update new filename");
				new_Resource.setNfilename(new_Resource.getNfilename());
			}
		
			//resourceService.updateResourceByRef(new_Resource);
			//new_Resource.setAccessChannel(String.valueOf(jsonNode.get("updates").get("accessChannel").size()));
		
			//Deal with access rule update
			dealWithAccessRule(jsonNode, userId);
		
		
			//Deal with access rule update
			dealWithResourceCategory(jsonNode, userId);
		
		
			//Deal with special user update
			dealWithSpecialUser(jsonNode, userId);
		
//			
			dealWithSpecialGroup(jsonNode,userId);
//			
			
			//Update resource info
			resourceService.updateResourceByRef(new_Resource);
			List<Long> return_cat= getResourceCategory(jsonNode.get("updates"));
			List<Integer> catgoryId = new ArrayList<>();
			for(Integer i =0 ; i<return_cat.size();i++) {
				Integer catId = Integer.valueOf(return_cat.get(i).toString());
				catgoryId.add(catId);
			}
			logger.editResource(user, jsonNode.get("id").asLong(), "", "success", Integer.valueOf(access_channel.toString()), catgoryId);
			System.out.println("Resource Controller - updateResource [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(jsonNode,session);
			}
	}
	
	@RequestMapping("/delete/{userId}")
	public ResponseEntity<JsonResult> deleteResource(@RequestBody JsonNode jsonNode,@PathVariable Long userId,HttpSession session) throws Exception, Throwable{
		long begin = System.currentTimeMillis();
		//Check AccessRight
		HashMap<String, String> user_check = common.checkUser(userId,session);
		if(user_check.get("msg") != "") {
			System.out.println("Resource Controller - deleteResource [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			String stringToConvert = String.valueOf(session.getAttribute("channel"));
			Long access_channel = Long.parseLong(stringToConvert);
			List<Long> accessRuleIdList = getAccessRuleIds(user_check,session);
			List<Long> resource_ids = new ArrayList<Long>();
			List<Integer> category = new ArrayList<>();
			JsonNode get_ids = jsonNode.get("id");
			if(get_ids.isArray()) {
				for(Integer i = 0; i < get_ids.size(); i++) {
					resource_ids.add(get_ids.get(i).asLong());
				}
			}else {
				System.out.println("Resource Controller - deleteResource [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.errorMsg("Resource info error");
			}
			Integer check_result = resourceService.checkAccessRuleByList(accessRuleIdList,resource_ids);
			if(check_result == 0) {
				System.out.println("Resource Controller - deleteResource [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.errorMsg("No permission to access these resource");
			}else {
				resourceService.deleteByIds(resource_ids,userId);
				for(Integer i =0 ; i<resource_ids.size();i++) {
					logger.deleteResource(user, resource_ids.get(i), "", "success",Integer.valueOf(access_channel.toString()) ,category );
				}
				System.out.println("Resource Controller - deleteResource [end] : " + (System.currentTimeMillis() - begin));
				return JsonResult.ok("Delete with success",session);
			}
			//return JsonResult.ok(return_data);
		}
	}
	
	@RequestMapping("/multiple_update")
	public ResponseEntity<JsonResult> multipleUpdate(@RequestBody JsonNode jsonNode,
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value="user") Long userId) throws Exception, Throwable{
		
		//List<FileResource> resourcesToBeUpdated = resourceService.findByIds(resource_ids);
		Integer activated = 0;
		Date latestNewsExpiry = null;
		Integer latestNews  =null;
		String accessChannel = "1";
		if(jsonNode.get("updates").get("activated")!= null) {
			activated = jsonNode.get("updates").get("activated").asInt();
		}
		if (jsonNode.get("updates").get("latestNews") !=null) {
			 latestNewsExpiry = common.textToDate(jsonNode.get("updates").get("latestNewsExpiry").asText());
			 latestNews = jsonNode.get("updates").get("latestNews").asInt();
		}
		if(getAccessChannel(jsonNode.get("updates")) != null ) {
		
			accessChannel = String.valueOf(getAccessChannel(jsonNode.get("updates")));
		}

		
		JsonNode ids_from_node = jsonNode.get("selIds");
		List<Long> resource_ids = new ArrayList<Long>();
		for(Integer i = 0; i < ids_from_node.size(); i++) {
			resource_ids.add(ids_from_node.get(i).asLong());
		}
		//System.out.println("resource_ids: " + resource_ids);
		
		//Update basic information
		//System.out.println("Check resource id -- "+ resource_ids+"  access Channel - "+accessChannel);
		resourceService.updateMultipleResource(userId,resource_ids,accessChannel,latestNews,latestNewsExpiry,activated);
		
		//Deal with resource tags (descriptions)
		dealWithMultipleTag(jsonNode, userId, resource_ids);
		
		//Deal with access rule update
		//jsonNode.get("updates").
		dealWithMultipleAccessRule(jsonNode, userId, resource_ids);
		
		dealWithMultipleResourceCategory(jsonNode, userId, resource_ids);
		
		//Deal with special user update
		dealWithMultipleSpecialUser(jsonNode, userId, resource_ids);
		
		
		dealWithMultipleSpecialGroup(jsonNode, userId, resource_ids);
//		FileResource return_Resource = resourceService.updateResourceByRef(new_Resource);
		return JsonResult.ok(jsonNode,session);
	}
	
	private void dealWithMultipleTag(JsonNode jsonNode, Long userId, List<Long> resource_ids) {
		String resource_tag_en = jsonNode.get("updates").get("descEn") == null ? "" : jsonNode.get("updates").get("descEn").asText();
		String resource_tag_tc = jsonNode.get("updates").get("descTc") == null ? "" : jsonNode.get("updates").get("descTc").asText();
		if(resource_tag_en.equals("") || resource_tag_tc.equals("")) {
			return;
		}else {
			List<FileResource> r_to_update = resourceService.findByIds(resource_ids).stream().map(
					(r) -> {
						r.setDescEn(resource_tag_en);
						r.setDescTc(resource_tag_tc);
						r.setModifiedAt(new Date());
						r.setModifiedBy(userId);
						return r;
					}
				).collect(Collectors.toList());
			resourceService.saveAll(r_to_update);
		}
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
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
	
//	private Integer getResourceTypeId(String ofilename) {
//		return 1;
//	}
	
	private void dealWithAccessRule(JsonNode jsonNode,Long userId) {
		List<Long> resourceAccessRuleList_update = getResourceAccessRule(jsonNode.get("updates"));
		
		//System.out.println("how many need to update = " +resourceAccessRuleList_update.size());
		List<Long> resourceAccessRuleList_db = resourceService.getAccessRuleByResourceId(jsonNode.get("id").asLong());
	//	System.out.println("Org resource access rule = "+resourceAccessRuleList_db);
		
		List<Long> resourceAccessRuleList_add = resourceAccessRuleList_update.
													stream().filter(
															(n) -> resourceAccessRuleList_db.indexOf(n) == -1).collect(Collectors.toList()
													);
		//System.out.println("Need to add = "+resourceAccessRuleList_add);
		List<Long> resourceAccessRuleList_delete = resourceAccessRuleList_db.
													stream().filter(
															(n) -> resourceAccessRuleList_update.indexOf(n) == -1).collect(Collectors.toList()
													);
		//System.out.println("Need to delete = "+resourceAccessRuleList_delete);
		for(Integer j = 0; j < resourceAccessRuleList_add.size(); j++) {
			resourceService.createResourceAccessRule(resourceAccessRuleList_add.get(j), jsonNode.get("id").asLong());
		}
		if(resourceAccessRuleList_delete.size() == 0) {
			resourceAccessRuleList_delete.add(0L);
		}
		resourceService.deleteResourceAccessRule(resourceAccessRuleList_delete,jsonNode.get("id").asLong(),userId, new Date());
		
		return ;
	}
	
	private void dealWithResourceCategory(JsonNode jsonNode, Long userId) {
		
		List<Long> resourceCategory_update = getResourceCategory(jsonNode.get("updates"));
		List<ResourceCategoryModel> resourceCategory_inDB = resourceService.getResourceCategoryByResourceId(jsonNode.get("id").asLong());
		List<Long> resourceCategory_db = resourceCategory_inDB.stream().map(
							(rc) -> {
								return Long.parseLong(rc.getCategoryId().toString());
							}
						).collect(Collectors.toList());
		
		List<Long> resourceCategory_deleted_inDB = resourceCategory_inDB.stream().filter(
					(rc) -> rc != null && rc.getDeletedBy() != null
				).map(
				(rc) -> {
					return Long.parseLong(rc.getCategoryId().toString());
				}
			).collect(Collectors.toList());

	
		List<Long> resourceCategory_add = resourceCategory_update.
											stream().filter(
													(n) -> resourceCategory_db.indexOf(n) == -1).collect(Collectors.toList());
		
		List<Long> resourceCate_update = resourceCategory_update.
								stream().filter(
										(n) -> resourceCategory_deleted_inDB.contains(n)
									).collect(Collectors.toList());
	  
		List<Long> resourceCategory_delete = resourceCategory_db.
											stream().filter(
													(n) -> resourceCategory_update.indexOf(n) == -1
												).collect(Collectors.toList());
		 
		for(Integer j =0 ; j < resourceCategory_add.size(); j++) {
			resourceService.createResourceCategory(resourceCategory_add.get(j), jsonNode.get("id").asLong());
		}
		
		if(resourceCate_update.size() == 0) {
			resourceCate_update.add(0L);
		}
		resourceService.updateResourceCategory(resourceCate_update, jsonNode.get("id").asLong());
		//System.out.println("see what in Resource Category Update"+ resourceCate_update);
		
		if(resourceCategory_delete.size() == 0) {
			resourceCategory_delete.add(0L);
		}
		resourceService.deleteResourceResourceCategory(resourceCategory_delete, jsonNode.get("id").asLong(), userId, new Date());
		
		//System.out.println("see what in Resource Category Delete"+ resourceCategory_delete);
		
		return;
	}
	
	private void dealWithMultipleAccessRule(JsonNode jsonNode,Long userId, List<Long> resource_ids) {
		//Filter updates and deletes
		List<Long> resourceAccessRuleList_update = getResourceAccessRule(jsonNode.get("updates"));
		//System.out.println("resourceAccessRuleList_update:" + resourceAccessRuleList_update);
		
		//ResourceAccessRule to be deleted
		if(resourceAccessRuleList_update != null && resourceAccessRuleList_update.size() != 0) {
			resourceService.deleteResourceAccessRuleByIds(resource_ids,userId);
			List<ResourceAccessRule> resourceAccessRule_deleted_inDB = resourceService.getAccessRuleByResourceIds(resource_ids).stream().collect(Collectors.toList());
		
			for(Integer i = 0; i < resource_ids.size(); i++) {
				Integer x = i;
				List<Long> resourceAccessRule_deleted_id_inDB = resourceAccessRule_deleted_inDB.stream().filter(
						(rar) -> rar.getResource().equals(resource_ids.get(x))
					).map(
						(rar) -> {
							return Long.parseLong(rar.getId().toString());
						}
					).collect(Collectors.toList());
				
				List<Long> resourceAccessRule_to_add = resourceAccessRuleList_update.stream().filter(
						(n) -> !resourceAccessRule_deleted_id_inDB.contains(n)
					).collect(Collectors.toList());
				if(resourceAccessRule_to_add != null && resourceAccessRule_to_add.size( )!= 0) {
					resourceService.createRARByBath(resourceAccessRule_to_add, resource_ids.get(i));
				}
				List<Long> resourceAccessRule_to_update = resourceAccessRuleList_update.stream().filter(
						(n) -> resourceAccessRule_deleted_id_inDB.contains(n)
					).collect(Collectors.toList());
				if(resourceAccessRule_to_update != null && resourceAccessRule_to_update.size( )!= 0) {
					resourceService.updateResourceAccessRule(resourceAccessRule_to_update, resource_ids.get(i));
				}
			}
		}else {
			return;
		}
		
	}
	
	
	
	
	private void dealWithMultipleResourceCategory(JsonNode jsonNode, Long userId, List<Long> resource_ids) {

		List<Long> resourceCategory_update = getResourceCategory(jsonNode.get("updates"));
		
		//System.out.println("Resource Category Update: " + resourceCategory_update);
		if(resourceCategory_update != null && resourceCategory_update.size() != 0) {
			resourceService.deleteResourceCategoryByIds(resource_ids, userId);
			List<ResourceCategoryModel> resourceCategory_deleted_inDB = resourceService.getResourceCategoryByResourceIds(resource_ids).stream().collect(Collectors.toList());
			
			for(Integer i = 0; i < resource_ids.size(); i++) {
				Integer x = i;
				List<Long> resourceCategory_deleted_id_inDB = resourceCategory_deleted_inDB.stream().filter(
						(rc) -> rc.getResourceId().equals(resource_ids.get(x))
					).map(
						(rc) -> {
							return Long.parseLong(rc.getId().toString());
						}
					).collect(Collectors.toList());
				
				List<Long> resourceCategory_to_add = resourceCategory_update.stream().filter(
						(n) -> !resourceCategory_deleted_id_inDB.contains(n)
					).collect(Collectors.toList());
				
				if(resourceCategory_to_add != null && resourceCategory_to_add.size( )!= 0) {
					resourceService.createRCByBath(resourceCategory_to_add, resource_ids.get(i));
				}
				
				List<Long> resourceCategory_to_update = resourceCategory_update.stream().filter(
						(n) -> resourceCategory_deleted_id_inDB.contains(n)
					).collect(Collectors.toList());
				if(resourceCategory_to_update != null && resourceCategory_to_update.size( )!= 0) {
					resourceService.updateResourceCategory(resourceCategory_to_update, resource_ids.get(i));
				}
			}
		}else {
			return;
		}
		
	}
	
	
	
	
	
	private void dealWithMultipleSpecialGroup(JsonNode jsonNode, Long userId, List<Long> resource_ids) {
		List<Integer> resourceSpecialGroup_update = getResourceSpecialUserList(jsonNode.get("updates"));
		//System.out.println("resourceSpecialGroup_update: "+resourceSpecialGroup_update );
		if(resourceSpecialGroup_update != null || resourceSpecialGroup_update.size() != 0) {
			resourceService.deletedSpecialGroupByIds(resource_ids, userId);
			List<ResourceSpeicalGroup> special_group_deleted_inDb = resourceService.getAllSpecialGroups(resource_ids).stream().collect(Collectors.toList());
			for(Integer i = 0 ; i< resource_ids.size() ; i++ ) {
				Integer x = i ;
				List<Long> special_group_deleted_id_inDB = special_group_deleted_inDb.stream().filter(
						(rc) -> rc.getResourceId().equals(resource_ids.get(x))
					).map(
						(rc) -> {
							return rc.getSpecialGroupId();
						}
					).collect(Collectors.toList());
				List<Integer> special_group_to_add = resourceSpecialGroup_update.stream().filter(
						(n) -> !special_group_deleted_id_inDB.contains(n)
					).collect(Collectors.toList());
				
				
				if(special_group_to_add != null && special_group_to_add.size( )!= 0) {
					resourceService.createRSGByBath(special_group_to_add, resource_ids.get(i),userId);
				}
				
				List<Integer> special_user_to_update = resourceSpecialGroup_update.stream().filter(
						(q) -> special_group_deleted_id_inDB.contains(q)
					).collect(Collectors.toList());
				if(special_user_to_update != null && special_user_to_update.size( )!= 0) {
					//resourceService.updateSpecialUser(special_user_to_update, resource_ids.get(i));
					resourceService.updateSpecialGroup(special_user_to_update, resource_ids.get(i));
				}
				
			}
			
			
		}else {
			return;
		}
	}

	
	
	private void dealWithMultipleSpecialUser(JsonNode jsonNode,Long userId, List<Long> resource_ids) {
		//Filter updates and deletes
		List<String> resourceSpecialUser_update = getResourceSpecialUser(jsonNode.get("updates"));
		//System.out.println("resourceSpecialUser_update:" + resourceSpecialUser_update);
		//System.out.println("Special User size = "+ resourceSpecialUser_update.size());
		if(resourceSpecialUser_update != null ||  resourceSpecialUser_update.size() != 0) {
			//Special User to be deleted
			resourceService.deleteSpecialUserByIds(resource_ids,userId);
			List<ResourceSpecialUser> special_user_deleted_inDB = resourceService.getAllSpecialUsers(resource_ids).stream().collect(Collectors.toList());
			for(Integer i = 0; i < resource_ids.size(); i++) {
				Integer x = i;
				List<String> special_user_deleted_id_inDB = special_user_deleted_inDB.stream().filter(
						(rc) -> rc.getResourceId().equals(resource_ids.get(x))
					).map(
						(rc) -> {
							return rc.getStaffNo();
						}
					).collect(Collectors.toList());
				
				List<String> special_user_to_add = resourceSpecialUser_update.stream().filter(
						(n) -> !special_user_deleted_id_inDB.contains(n)
					).collect(Collectors.toList());
				
				if(special_user_to_add != null && special_user_to_add.size( )!= 0) {
					resourceService.createRSUByBath(special_user_to_add, resource_ids.get(i));
				}
				
				List<String> special_user_to_update = resourceSpecialUser_update.stream().filter(
						(n) -> special_user_deleted_id_inDB.contains(n)
					).collect(Collectors.toList());
				if(special_user_to_update != null && special_user_to_update.size( )!= 0) {
					resourceService.updateSpecialUser(special_user_to_update, resource_ids.get(i));
				}
				
			}
		}else {
			return;
		}
	}
	
	
	private void dealWithSpecialGroup(JsonNode jsonNode, Long userId) {
		List<Integer> resourceSpecialGroup_update =   getResourceSpecialUserList(jsonNode.get("updates"));
		List<Integer> reosurceSpecialGroup_db = resourceService.getSpecialGroupByResourceId(jsonNode.get("id").asLong()).stream().map(
					(n)->{
						if(n==null) {
							return 0;
						} else {
							return n;
						}
					}
				).collect(Collectors.toList());
		
		//System.out.println("Resource Special Group Update =  "+ resourceSpecialGroup_update);
		//System.out.println("Resource Special Group DB = "+ reosurceSpecialGroup_db);
		
		List<Integer>  resourceSpecialGroup_add = resourceSpecialGroup_update.stream().filter(
													(n)->reosurceSpecialGroup_db.indexOf(n) == -1).collect(Collectors.toList());
		
		List<Integer> resourceSpecialGroup_delete = reosurceSpecialGroup_db.stream().filter(
													(n)->resourceSpecialGroup_update.indexOf(n) == -1).collect(Collectors.toList()
												);
		
		//System.out.println("Need to add to Resource Special group = "+ resourceSpecialGroup_add);
		//System.out.println("Need to be deleted at Resource Special Group = "+ resourceSpecialGroup_delete);
	
		for(Integer j = 0 ; j < resourceSpecialGroup_add.size () ; j++) {
			resourceService.createResoruceSpecialGroup(resourceSpecialGroup_add.get(j), jsonNode.get("id").asLong(), userId);
		}
		if (resourceSpecialGroup_delete.size() ==0) {
			resourceSpecialGroup_delete.add(0);
		}
		
		//resourceService.dl(resourceSpecialGroup_delete, jsonNode.get("id").asLong(), userId, new Date());
		resourceService.deleteResoufceSpecialGroup(resourceSpecialGroup_delete, jsonNode.get("id").asLong(), userId, new Date());
	}
	
	
	
	private void dealWithSpecialUser(JsonNode jsonNode,Long userId) {
		List<String> resourceSpecialUser_update = getResourceSpecialUser(jsonNode.get("updates"));
		List<String> resourceSpecialUser_db = resourceService.getSpecialUserByResourceId(jsonNode.get("id").asLong()).stream().map(
				(n) -> {
					if(n == null) {
						return "0";
					}else {
						return n;
					}
				}
				).collect(Collectors.toList());
		
		List<String> resourceSpecialUser_add = resourceSpecialUser_update.
													stream().filter(
															(n) -> resourceSpecialUser_db.indexOf(n) == -1).collect(Collectors.toList()
													);
		List<String> resourceSpecialUser_delete = resourceSpecialUser_db.
													stream().filter(
															(n) -> resourceSpecialUser_update.indexOf(n) == -1).collect(Collectors.toList()
														);
		
		for(Integer j = 0; j < resourceSpecialUser_add.size(); j++) {
			resourceService.createResourceSpecialUser(resourceSpecialUser_add.get(j), jsonNode.get("id").asLong(), userId );
		}
		if(resourceSpecialUser_delete.size() == 0) {
			resourceSpecialUser_delete.add("0");
		}
		resourceService.deleteResourceSpecialUser(resourceSpecialUser_delete,jsonNode.get("id").asLong(),userId, new Date());
		
		return ;
	}
	
	@RequestMapping("/img2pdf")
    public ResponseEntity<JsonResult> testImgToPDF(HttpSession session) throws IOException {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		
		return JsonResult.ok(imgToPDF("related-resources01.png", "png","1"),session);
    }
	
	@RequestMapping("/water_mark")
    public ResponseEntity<JsonResult> testWaterMark(HttpSession session) throws IOException, DocumentException {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		//System.out.println("Before Create water mark ");
		return JsonResult.ok(waterMark("related-resources01.pdf",1L,"kms-resource/pdf/1","1","1"),session);
    }
	
	@RequestMapping("/pdf2img")
    public ResponseEntity<JsonResult> testPDF2IMG(HttpSession session) throws IOException, DocumentException {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		Path resourcePath = storageService.getResourceLocation();
		PDF2IMG.pdf2png(resourcePath.toString(), "related-resources01.pdf", 0, "jpg", "1");
		return JsonResult.ok(session);
    }
	
	@RequestMapping("testing")
	public ResponseEntity<JsonResult> testing(HttpSession session){
		return JsonResult.timeOut("Session time out ");
	}
	
	@RequestMapping("/office2pdf")
    public ResponseEntity<JsonResult> office2pdf(String inputname, String outputname,HttpSession session) throws IOException, DocumentException {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		
		//Method one -- Use pdftron (Free version will add watermark)
//		OfficeWithPDFTron tester = new OfficeWithPDFTron();
//		//tester.doTest("simple-word_2007","docx");
//		//tester.doTest("simple-excel_2007","xlsx");
//		Long startTime=System.currentTimeMillis();   //Time start
//		tester.doTest("simple-word_2007","docx");
//		Long endTime=System.currentTimeMillis(); //Time end
//		System.out.println("OfficeWithPDFTron -- Word： "+(endTime-startTime)+"ms");
//		
//		Long startTime_excel = System.currentTimeMillis();   //Time start
//		tester.doTest("simple-excel_2007","xlsx");
//		Long endTime_excel = System.currentTimeMillis(); //Time end
//		System.out.println("OfficeWithPDFTron -- Excel： "+(endTime_excel-startTime_excel)+"ms");
//		
//		return JsonResult.ok();
		
		//Method two -- Use libreoffice (Excel out of range will be an error)
//		OfficeWithLibre tester2 = new OfficeWithLibre();
//		Path resourcePath = storageService.getResourceLocation();
//		//System.out.println(resourcePath.toString()+"/simple-test_word_file.docx");
//		Long startTime=System.currentTimeMillis();   //Time start
//		tester2.Word2Pdf(resourcePath.toString()+"/simple-word_2007.docx",resourcePath.toString()+"/test_word_file.pdf");
//		Long endTime=System.currentTimeMillis(); //Time end
//		System.out.println("OfficeWithLibre： "+(endTime-startTime)+"ms");
//		return JsonResult.ok();
		
		//Method three -- Use old tools (Only windows version with office)
		OfficeWithOldVersion converter = new OfficeWithOldVersion();
		Integer result = converter.runCMD(inputname, outputname);
		if(result == 1) {
			return JsonResult.ok(session);
		}else {
			return JsonResult.errorMsg("Converter Error");
		}
		
    }
	
    public String pdfUseCMD(String FileName, String fileType, String folderName) throws IOException, DocumentException {
    	System.out.println("office to PDF  "+FileName + " file Type : "+ fileType + " folder Name : "+ folderName);
    	String pdfFileName = FileName.replace(fileType, "pdf");
    	Path resourcePath = storageService.getResourceLocation();
		String FilePath = resourcePath.toString() + "/" + FileName;
		String pdfFilePath = resourcePath.toString() + "/pdf/" + folderName + "/" + pdfFileName;
    	
		//Method three -- Use old tools (Only windows version with office)
		OfficeWithOldVersion converter = new OfficeWithOldVersion();
		Integer result = converter.runCMD(FilePath, pdfFilePath);
		System.out.println("Result = "+ result);
		if(result == 1) {
			return pdfFileName;
		}else {
			return "";
		}
		
    }
	private String imgToPDF(String imgFileName, String fileType, String folderName) throws IOException {
		ImgToPDF convertor = new ImgToPDF();
		
		String pdfFileName = imgFileName.replace(fileType, "pdf");
		Path resourcePath = storageService.getResourceLocation();
		String imgFilePath = resourcePath.toString() + "/" + imgFileName;
		String pdfFilePath = resourcePath.toString() + "/pdf/" + folderName + "/" + pdfFileName;
		//System.out.println("Before ImgToPDF , imgFilePath = "+ imgFilePath + " pdfFilePath = "+ pdfFilePath);
		
		
		convertor.imgToPdf(imgFilePath, pdfFilePath);
		
		return  pdfFileName;
	}
	
	private String waterMark(String fileName,Long userId, String filePath, String staff_no, String rankName) throws IOException, DocumentException {
		WaterMark convertor = new WaterMark();
		
		//Copy Temp File
		String pureFileName = fileName.substring(fileName.lastIndexOf("/") + 1,fileName.length());
		Path resourcePath = storageService.getResourceLocation();
		//System.out.println("resourcePath: " + resourcePath);
		
		String folderName = filePath.replace(storageService.getResourceFolderName(), "");
		if(!folderName.substring(folderName.length() - 1).equals("/")) {
			folderName = folderName + "/";
		}
		//System.out.println("folderName: " + folderName);
		
		String original_fileName = resourcePath.toString() + folderName + fileName;
		//System.out.println("original_fileName: " + original_fileName);
		
		String new_filePath = resourcePath.toString() + "/pdf/water_mark/" + userId + "/";
		//System.out.println("new_filePath: " + new_filePath);
		
		try {
            Files.createDirectories(Paths.get(new_filePath));
        }
        catch (IOException e) {
            throw new StorageException("Could not crate water mark folder", e);
        }
		String new_fileName = new_filePath + pureFileName;
		FileCopy.copy(original_fileName, new_fileName, pureFileName);
		convertor.doWaterMark(new_fileName, original_fileName, rankName,staff_no);
		
		
		return "resources/pdf/water_mark/" + userId + "/" + pureFileName;
		
		//return "resources/pdf/" + pdfFileName;
	}
	
	private String pdf2Img(String filename,Integer indexOfStart,String type, String folderName) {
		Path resourcePath = storageService.getResourceLocation();
		return PDF2IMG.pdf2png(resourcePath.toString(), filename, indexOfStart, type, folderName);
	}
	
	private String pdf2LocalImg(String filename,Integer indexOfStart,String type, String folderName) throws IOException, Exception {
		Path resourcePath = storageService.getResourceLocation();
		return PDF2IMG.pdf2LocalPng(resourcePath.toString(), filename, indexOfStart, type, folderName);
	}
	
	@RequestMapping("/officeTest/{filename}/{filetype}")
    public ResponseEntity<JsonResult> officeTest(@PathVariable String filename, @PathVariable String filetype,HttpSession session) throws IOException, DocumentException {
         //model.addAttribute("Resource", ResourceRepository.findAll());
		
		//OfficeWithPDFTron tester = new OfficeWithPDFTron();
		
//		tester.doTest("file-sample_1MB","docx");
		//tester.doTest(filename,filetype);
//		tester.doTest("file-sample_500kB","docx");
//		tester.doTest("SampleDOCFile_2000kb","doc");
//		tester.doTest("SampleDOCFile_5000kb","doc");
//		tester.doTest("SampleXLSFile_212kb","xlsx");
//		tester.doTest("SampleXLSFile_904kb","xlsx");
//		tester.doTest("SampleXLSFile_1736kb","xlsx");
//		tester.doTest("SampleXLSFile_6800kb","xlsx");
		return JsonResult.ok(session);
	}
	
	@RequestMapping("/genThumb/{folder_name}")
	public ResponseEntity<JsonResult> genThumb(@PathVariable Integer folder_name,HttpSession session) throws Exception{
		long begin = System.currentTimeMillis();
		String folderName = folder_name.toString();
		Path resourcePath = storageService.getResourceLocation();
		Path targetPath = Paths.get( resourcePath.toString() + "/pdf/" + folderName );
		////System.out.println("targetPath: " + targetPath);
		//List<String> return_data = new ArrayList<String>();
		List<Long> resource_ids = new ArrayList<Long>();
		Integer startNum = (folder_name - 1000) + 1;
		for(Integer id = startNum; id < startNum + 1000; id++) {
			resource_ids.add(Long.parseLong(id.toString()));
		}
		List<FileResource> fileData = resourceService.findByIds(resource_ids);
		List<FileResource> toUpdate = new ArrayList<FileResource>();
		try {
            Stream<Path> files =  Files.walk(targetPath, 1)
                .filter(path -> !path.equals(targetPath))
                .map(targetPath::relativize);
            List<String> files_list = files.filter(
            			(f) -> !f.toString().substring(0,1).equals(".") && 
            					f.toString().substring(f.toString().lastIndexOf(".") + 1,f.toString().length()).toLowerCase().equals("pdf") 
            		).map(
            			(f) -> {return f.toString();}
            		).collect(Collectors.toList());
            //System.out.println("files_list: " + files_list);
            for(Integer i = 0; i < files_list.size(); i++) {
            	String thumbnail = "resources/pdf/" + folderName + "/" + pdf2LocalImg(StringUtils.cleanPath(files_list.get(i)),0,"png",folderName);
            	String[] namePatern = files_list.get(i).toString().split("_");
            	String resource_id = namePatern[0];
            	FileResource resource_filtered = fileData.stream().filter(
            				(r) -> r.getId().toString().equals(resource_id)
            			).collect(Collectors.toList()).get(0);
            	resource_filtered.setThumbnail(thumbnail);
            	resourceService.saveSingleThumbs(resource_filtered);
            	toUpdate.add(resource_filtered);
            	//return_data.add(thumbnail);
            }
            System.out.println("Resource Controller - genThumb [end] : " + (System.currentTimeMillis() - begin));
            //resourceService.saveAllThumbs(toUpdate);
            return JsonResult.ok(toUpdate.stream().map(
            			(r) -> {return r.getThumbnail();}
            		).collect(Collectors.toList()),session);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
	}
	
	@RequestMapping("/favourites/getList/{userId}")
		public ResponseEntity<JsonResult> ListFavourites (@PathVariable Long userId, HttpSession session){
		long begin = System.currentTimeMillis();
		String stringToConvert = String.valueOf(session.getAttribute("channel"));
        Long access_channel = Long.parseLong(stringToConvert);
		
		List<Favorites> return_data =  new ArrayList<>();
		//System.out.println("--------------------");
		return_data = favoritesService.find(userId);
		//System.out.println("--------------------");
		List<Object> test= new ArrayList<>();
		//System.out.println("Return data in favorites = "+return_data.size());
		
		if(return_data.size()==0) {
			System.out.println("Resource Controller - ListFavourites [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(0,"OK",session);
		}else {
			for(Integer i =0; i<return_data.size();i++) {
				HashMap <String, Object> return_object = new HashMap<>();
				Optional<FileResource> resource = resourceService.findById(return_data.get(i).getResourceId(), access_channel);
			   if (resource.isPresent()) {
				return_object.put("ref", return_data.get(i).getId());
				return_object.put("id", resource.get().getId());
				return_object.put("titleEn",resource.get().getTitleEn());
				return_object.put("titleTc", resource.get().getTitleTc());
				return_object.put("createdAt", return_data.get(i).getCreatedAt());
				test.add(return_object);
			   }
		}
			System.out.println("Resource Controller - ListFavourites [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(test,"OK",session);
		
		}
			
	}
	
	@RequestMapping("/favourites/delete/{userId}")
		public ResponseEntity<JsonResult> DeleteFavorites(@PathVariable Long userId, @RequestBody JsonNode jsonNode,
				HttpSession session){
		long begin = System.currentTimeMillis();
		 Integer size = jsonNode.get("refs").size();
		 String stringToConvert = String.valueOf(session.getAttribute("channel"));
	        Long access_channel = Long.parseLong(stringToConvert);
			
	      
		for(Integer i =0; i<size ; i++) {
			//System.out.println("refs id = "+jsonNode.get("refs").get(i).asLong());
			Optional<Favorites> data ;
			data = favoritesService.findById(jsonNode.get("refs").get(i).asLong(),userId);
			//System.out.println("Reutrn data = "+data);
			Favorites new_data = new Favorites();
			new_data.setId(data.get().getId());
			new_data.setCreatedAt(data.get().getCreatedAt());
			new_data.setCreatedBy(data.get().getCreatedBy());
			new_data.setResourceId(data.get().getResourceId());
			
			new_data.setIsDeleted((long)1);
			new_data.setDeletedAt(new Date());
			new_data.setDeletedBy(userId);
			favoritesService.save(new_data);
		
		}
		
		System.out.println("Resource Controller - DeleteFavorites [end] : " + (System.currentTimeMillis() - begin));
		return JsonResult.ok("OK", session);
		
	}
		
		
	
	@RequestMapping("/favourites/add")
		public ResponseEntity<JsonResult> addFavourites(
				@RequestBody JsonNode jsonNode, HttpSession session){
//		//System.out.println("What we get "+ jsonNode );
		long begin = System.currentTimeMillis();
		Optional<Favorites> data ;
		data = favoritesService.findById(jsonNode.get("resourceId").asLong(),jsonNode.get("userId").asLong());
		
		//System.out.println("Reosurce Id - "+ jsonNode.get("resourceId").asLong()+ ("User Id -")+ jsonNode.get("userId"));
		Optional<User> return_data =  userService.findById(jsonNode.get("userId").asLong());
		//System.out.println(" User Info = "+ return_data.get().getRankId());
		User user = (User) session.getAttribute("user_session");
		Integer is_admin = common.checkAdmin(user, session);
		//System.out.println("**Session Channel - " +session.getAttribute("channel") );
		String stringToConvert = String.valueOf(session.getAttribute("channel"));
        Long access_channel = Long.parseLong(stringToConvert);
    	Favorites new_data = new Favorites();
		Integer check_result = resourceService.checkAccessRule(accessRuleService.getIdByUser(return_data.get().getSectionId(), return_data.get().getInstitutionId(), return_data.get().getRankId()),jsonNode.get("resourceId").asLong(),return_data.get().getStaffNo(),is_admin, access_channel);
		//System.out.println("-------------------");
        if(check_result>0)
        {   
        	
        	
		if(data.isPresent()) {
			
			
			
			new_data.setId(data.get().getId());
			new_data.setCreatedAt(new Date());
			new_data.setCreatedBy(data.get().getCreatedBy());
			new_data.setResourceId(data.get().getResourceId());
			
			new_data.setIsDeleted(0L);
			new_data.setDeletedAt(new Date());
			new_data.setDeletedBy(jsonNode.get("userId").asLong());
			favoritesService.save(new_data);
			System.out.println("Resource Controller - addFavourites [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok("OK", session);
		
		
		} else {
			new_data.setCreatedBy(jsonNode.get("userId").asLong());
			new_data.setCreatedAt(new Date());
			new_data.setResourceId(jsonNode.get("resourceId").asLong());
			new_data.setDeletedBy(null);
			new_data.setIsDeleted(0L);
		
			favoritesService.save(new_data);
			System.out.println("Resource Controller - addFavourites [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok("OK", session);
		
		}
	   } else {
		   System.out.println("Resource Controller - addFavourites [end] : " + (System.currentTimeMillis() - begin));
		   return JsonResult.errorMsg("No right");
	   }
	}
	
	

	
	
	

	@SuppressWarnings("unchecked")
	@RequestMapping("/share/search/{userId}/{page}")
	  public ResponseEntity<JsonResult> shareList(@PathVariable Long userId,@PathVariable Integer page,
			  @RequestBody JsonNode jsonNode, HttpSession session) {
			long begin = System.currentTimeMillis();
			List<User> return_data = new ArrayList<User>();
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			//System.out.println("**Session Channel - " +session.getAttribute("channel") );
			String stringToConvert = String.valueOf(session.getAttribute("channel"));
	        Long access_channel = Long.parseLong(stringToConvert);
			
			String staff =jsonNode.get("staffNo").asText();
			Integer limitStart = ((page - 1) * 10);
			Integer limitEnd = page * 10;
			if (staff.length()>2) {
				
			return_data.add(userService.findByStaffNo(jsonNode.get("staffNo").asText())) ;
			} else {
				return_data.addAll(userService.findByStaffName(jsonNode.get("fullname").asText(),limitStart,limitEnd));
				
			}
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
				temp_user.setIsBlogger(null);
				temp_user.setProfilePhoto(temp_user.getProfilePhoto());
				temp_user.setAliasPhoto(temp_user.getAliasPhoto());
				temp_user.setAlias(null);
				temp_user.setScore(null);
				temp_user.setSex(null);
				Integer is_admin = common.checkAdmin(temp_user, session);
				Integer check_result = resourceService.checkAccessRule(accessRuleService.getIdByUser(temp_user.getSectionId(), temp_user.getInstitutionId(), temp_user.getRankId()),jsonNode.get("resourceId").asLong(),jsonNode.get("staffNo").asText(),is_admin, access_channel);
	            if(check_result>0)
	            	check_result=1;
	            temp_user.setifAccess(check_result);
				return temp_user;
	        }).collect(Collectors.toList());
			
			System.out.println("Resource Controller - shareList [end] : " + (System.currentTimeMillis() - begin));
			return JsonResult.ok(return_data,"OK",session);
      
   }

	
	@RequestMapping("/checkSession")
	public ResponseEntity<JsonResult> getSessionAccessRule(HttpServletRequest request){
		HttpSession session = request.getSession();
		HashMap<String, Object> session_data = getSessions(request);
		Object sessionBrowser = session.getAttribute("browser");
        if (sessionBrowser == null) {
            //System.out.println("No session，set browser=" + "Chrome");
             
        } else {
            //System.out.println("Session exist，browser=" + sessionBrowser.toString());
        }
		return JsonResult.ok(session_data,session);
	}
	
	
	
	
	//--------------------------------Testing Video--------------------------------------------------
		
		@RequestMapping("/video")
		public ResponseEntity<JsonResult> getVideo(HttpSession session){
				
			String video = resourceService.getVideo().toString();		
			String[] name = video.split("/KMS/");
			String param1 ="https://ams.csd.gov.hk/api/vodapi.php?";
			String param2 =	"file="+name[1]+"&folder=/KMS/&key=test1";
			//System.out.println(" Should use this "+param2);
			String param = param1 +param2;
			String videoLink =getUrlContents(param);
			//System.out.println("Video Link "+ videoLink);
			return JsonResult.ok(videoLink,session);
		}
		
		@RequestMapping("/video_wowza")
		public ResponseEntity<JsonResult> getVideoWowza(HttpSession session){
				
			String video = resourceService.getVideo().toString();		
			String serverUrl = "http://192.168.1.32/wowza/";
	        String sharedSecret = "12345";
	        String hashPrefix = "wowzatoken";
	        String videoPath;
	        String clientIP = null;
	        if(video.startsWith("https://ams.csd.gov.hk/KMS/")) {
				String[] name = video.split("/KMS/");
		        videoPath = name[1];
			} else {
		        videoPath = video;
			}
	        clientIP = null;
			String videoLink = WowzaTokenGenerator.gen(videoPath, serverUrl, sharedSecret, hashPrefix, clientIP);
			return JsonResult.ok(videoLink,session);
		}
	
		
		
	//------------------------------------------------------------------------------------------------------		
	
	private HashMap<String, Object> getSessions(HttpServletRequest request){
		//Get Session  
		HttpSession session = request.getSession();
		//Get Session Keys 
		Enumeration<String> attrs = session.getAttributeNames();  
		//Loop all  Session
		HashMap<String, Object> session_data = new HashMap<>();
		//List<HashMap<String, Object>> session_data_list = new ArrayList<HashMap<String, Object>>();
		while(attrs.hasMoreElements()){
		// Get session Keys 
			String name = attrs.nextElement().toString();
			// Get Key - Values
			Object value = session.getAttribute(name);
			session_data.put(name, value);
			// Print
			////System.out.println("------" + name + ":" + value +"--------\n");
		}
		return session_data;
	 }
	
	/*
	 * file_name jsonNode.get(i).get("nfilename").asText();
	 * 
	 * */
	private FileResource moveToFolder(String file_name, String os, String folder_name,FileResource added_resource) throws IOException,DocumentException {
		//Move file to folder
		//System.out.println("Check update resource get to move to folder = "+file_name);
		Integer file_id = Integer.parseInt(String.valueOf(added_resource.getId()));
		Path files = storageService.load(file_name);
		String fileType = file_name.substring(file_name.lastIndexOf(".") + 1,file_name.length()).toLowerCase();
		String[] imgTypeArray = new String[]{"jpg","png","jpeg","gif","svg","mp4"};
		String file_path ="";
		String file_path_in_db="";
		String wfilename = "";
		List<String> imgTypes = Arrays.asList(imgTypeArray);
		
		//System.out.println("fileType: " + fileType);
		//System.out.println("imgTypes: " + imgTypes);
		if(os.indexOf("Mac") > -1) {
			 file_path = storageService.getResourceLocation() + "/pdf/" + folder_name;
			 file_path_in_db = storageService.getResourceFolderName() + "/pdf/" + folder_name;
			 //System.out.println("File Path toString: " + files.toString());
			 //System.out.println("New file path name: " + files.toString().replace(storageService.getTempFolderName() + "/", 
//						storageService.getResourceFolderName() + "/" + file_id + "_"));
			 //System.out.println("New file Name in TEMP = "+ files.toString().replace(storageService.getTempFolderName() + "\\", file_id + "_"));
			 wfilename = files.toString().replace(storageService.getTempFolderName() + "\\", file_id + "_");
		}else {
			System.out.print("Get Resource Location "+storageService.getResourceLocation().toString());
			file_path = storageService.getResourceLocation() + "\\pdf\\" + folder_name;
			file_path_in_db = storageService.getResourceFolderName() + "/pdf/" + folder_name;
			//System.out.println("File Path toString: " + files.toString());
			//System.out.println("New file path name: " + files.toString().replace(storageService.getTempFolderName() + "\\", 
//					storageService.getResourceFolderName() + "\\" + file_id + "_"));
			//System.out.println("New file Name in TEMP = "+ files.toString().replace(storageService.getTempFolderName() + "\\", file_id + "_"));
			wfilename = files.toString().replace(storageService.getTempFolderName() + "\\", file_id + "_");
		}
		
		try {
            Files.createDirectories(Paths.get(file_path));
            Path path_name1= Paths.get(file_path);
            //System.out.println("IN create files? " + path_name1);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
		if(file_name.indexOf("_") > -1) {
			//System.out.println("old file ");
		} else {
			//System.out.println("new file or reupload file ");
			file_name = added_resource.getId() + "_" + file_name;
		}
		
		//System.out.println("TempFolder Name: " + storageService.getTempFolderName());
		//System.out.println("ResourceFolder Name: " + storageService.getResourceFolderName());
		
		if(imgTypes.contains(fileType)) {
			if(os.indexOf("Mac") > -1) {
				//System.out.println("User using Mac");
				FileCopy.copy(
						files.toString(), 
						files.toString().replace(storageService.getTempFolderName() + "/", 
								storageService.getResourceFolderName() + "/" + file_id + "_"		
						),//"upload-dir", "kms-resource/pdf"
						files.toString());
			}
			else {
			//System.out.println("Is image ");
				FileCopy.copy(
					files.toString(),  
					files.toString().replace(storageService.getTempFolderName() + "\\", 
							storageService.getResourceFolderName() + "\\" + file_id + "_"
					),//"upload-dir", "kms-resource/pdf"
					files.toString());}
			String pdfFileName = imgToPDF(file_name,fileType,folder_name);
			added_resource.setFilepath(file_path_in_db);
//			added_resource.setThumbnail("resources/" + file_name);
			added_resource.setThumbnail("resources\\" + file_name);
			added_resource.setNfilename(pdfFileName);
			
			
			
			//System.out.println("File path for image :  "+ added_resource);
		}else {
			//Other document to pdf
			if(fileType.toLowerCase().equals("pdf")) {
				if(os.indexOf("Mac") > -1) {
					FileCopy.copy(
							files.toString(), 
							files.toString().replace(storageService.getTempFolderName() + "/",
//							files.toString().replace(storageService.getTempFolderName() + "\\",
									file_path_in_db + "/" + file_id + "_"
//									file_path_in_db + "\\" + file_id + "_"
							),//"upload-dir", "kms-resource/pdf"
							files.toString());
					String thumbnail = "resources/" + pdf2Img(file_name,0,"png",folder_name);
					added_resource.setFilepath(file_path_in_db);
					added_resource.setNfilename(file_name);
					added_resource.setThumbnail(thumbnail);
					//System.out.println("File path for pdf :  "+ added_resource);
				}else {
					FileCopy.copy(
							files.toString(), 
//							files.toString().replace(storageService.getTempFolderName() + "/",
							files.toString().replace(storageService.getTempFolderName() + "\\",
//									file_path_in_db + "/" + file_id + "_"
									file_path_in_db + "\\" + file_id + "_"
							),//"upload-dir", "kms-resource/pdf"
							files.toString());
					String thumbnail = "resources/" + pdf2Img(file_name,0,"png",folder_name);
					added_resource.setFilepath(file_path_in_db);
					added_resource.setNfilename(file_name);
					added_resource.setThumbnail(thumbnail);
					//System.out.println("File path for pdf :  "+ added_resource);
				}
			}else {
				System.out.println("--  Microsoft  to PDF -----");
				//pdfUseCMD
				FileCopy.copy(
						files.toString(), 
						files.toString().replace(storageService.getTempFolderName() + "\\",
								storageService.getResourceFolderName() + "\\" + file_id + "_"
						),//"upload-dir", "kms-resource/pdf"
						files.toString());
				String pdfFile = pdfUseCMD(file_name,fileType,folder_name);
				//System.out.println("--  After pdfUserCMD ---   ");
				if(pdfFile == "") {
					return new FileResource();//JsonResult.errorMsg("File Convert Error");
				}
				
				String thumbnail = "resources/" + pdf2Img(pdfFile,0,"png",folder_name);
				added_resource.setFilepath(file_path_in_db);
				added_resource.setNfilename(pdfFile);
				added_resource.setThumbnail(thumbnail);
			
				
				//System.out.println("File path for document :  "+ added_resource);
			}
			
		}
		return added_resource;
	}
	
	
	
	
	
	
	
}

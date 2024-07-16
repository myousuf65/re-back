package com.hkmci.csdkms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.CatAllModel;
import com.hkmci.csdkms.model.ForumCategoryModel;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.PageNameModel;
import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.model.ResourceAccessRuleModel;
import com.hkmci.csdkms.model.ResourceReoportModel;
import com.hkmci.csdkms.model.ScoreReportModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.report.ReportService;
import com.hkmci.csdkms.repository.AccessRuleRepository;
import com.hkmci.csdkms.repository.CatAllRepository;
import com.hkmci.csdkms.repository.ScoreLogRepository;
import com.hkmci.csdkms.repository.UserRepository;
import com.hkmci.csdkms.service.LogService;

@CrossOrigin
@RestController
@RequestMapping("/report")
public class ReportController {
	
    private LogService logger;
    private Common common;
    private ReportService reportService;
   
	
	@Autowired
	public ReportController(LogService theLogger,Common theCommon,ReportService theReportService)
	{
		logger = theLogger;
		common = theCommon;
		reportService = theReportService;
	}
	
	@Autowired
	@Resource
	private CatAllRepository catAllRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccessRuleRepository accessRuleRepository;
	
	@Autowired
	private ScoreLogRepository scoreLogRepository;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/rank/{user_id}")
    public ResponseEntity<JsonResult> getRankReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
//		System.out.println("access channel "+user_check.get("access_channel"));
		
		user_check.forEach((key,value)->{
	        System.out.println(String.format(" Header '%s' = %s", key, value));
	    });
		
		
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			//Integer channel = (Integer) session.getAttribute("channel");
			Integer channel = 1;
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			Long rankId = jsonNode.get("rankId").asLong();
			Long instId = jsonNode.get("instId").asLong();
			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			
			
		    List<Long> instFinal = new ArrayList<>();
		    if(instId == 8 || instId == 9) {
		    	instFinal.add(8L);
		    	instFinal.add(9L);
		    } else if (instId == 32 || instId == 16) {
		    	instFinal.add(32L);
		    	instFinal.add(16L);
		    } else {
		    	instFinal.add(instId);
		    }
			
			
			List<Log> report_log = reportService.getByRank(rankId,instFinal,startDate,endDate);
			List<Log> report_view_page_log = reportService.getViewPageByRank(rankId,instFinal,startDate,endDate,instId);
			
			List<String> staffNoList = report_log.stream().map(
					(log) -> {
						return log.getStaffNo();
					}
				).distinct().collect(Collectors.toList());
			//System.out.println("Staff size: " + staffNoList.size());
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer i = 0; i < staffNoList.size(); i++) {
				String target_staffNo = staffNoList.get(i);
				ResourceReoportModel staff_info = new ResourceReoportModel();
				staff_info.setStaffNo(target_staffNo);
				staff_info.setFullname(user_list_session.stream().filter(
							(u) -> u.getStaffNo().equals(target_staffNo)
						).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
				staff_info.setHit_count(report_view_page_log.stream()
										    .filter((n) -> n.getStaffNo().equals(target_staffNo))
										    .collect(Collectors.toList())
										    .size());
				List<Log> report_log_valid = report_log.stream().filter(
							(log) -> log.getStaffNo().equals(target_staffNo)
						).collect(Collectors.toList());
				staff_info.setInstitution(institution_session.stream()
											  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUinstId()))
											  .map(InstitutionsModel::getInstName)
											  .collect(Collectors.toList())
											  .get(0));
				staff_info.setSection(section_session.stream()
										  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUsectionId()))
										  .map(SectionModel::getSectionName)
										  .collect(Collectors.toList())
										  .get(0));
				staff_info.setRank(rank_session.stream()
									   .filter((n) -> n.getId().equals(report_log_valid.get(0).getUrankId()))
									   .map(RanksModel::getRankName)
									   .collect(Collectors.toList())
									   .get(0));
				staff_info.setIntranet_count(report_log_valid.stream()
											     .filter((n) -> n.getChannel() == null || n.getChannel().equals(1))
											     .collect(Collectors.toList())
											     .size());
				staff_info.setInternet_count(report_log_valid.stream()
											     .filter((n) -> n.getChannel() != null && !n.getChannel().equals(1))
											     .collect(Collectors.toList())
											     .size());
				staff_info.setTotal_count(report_log_valid
					    					  .size());
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Rank", return_data, return_data.size(),session);
		}
    }
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/rank2/{user_id}")
    public ResponseEntity<JsonResult> getRank2Report(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		//System.out.println("access channel "+user_check.get("access_channel"));
		
		user_check.forEach((key,value)->{
	        //System.out.println(String.format(" Header '%s' = %s", key, value));
	    });
		
		
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			//Integer channel = (Integer) session.getAttribute("channel");
			Integer channel = 1;
			if(session.getAttribute("rank") == null) {
			
				common.checkRankSession(session);
			}
			
			logger.viewReport(user, 0L, "", "Success", channel);
			
			
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			Long rankId = jsonNode.get("rankId").asLong();
			Long instId = jsonNode.get("instId").asLong();
			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			
		    List<Long> instFinal = new ArrayList<>();
		    if(instId == 8 || instId == 9) {
		    	instFinal.add(8L);
		    	instFinal.add(9L);
		    } else if (instId == 32 || instId == 16) {
		    	instFinal.add(32L);
		    	instFinal.add(16L);
		    } else {
		    	instFinal.add(instId);
		    }
			
			
			
			
			//System.out.println("Rank Id = "+rankId);
			List<Log> report_log = reportService.getByRank(rankId,instFinal,startDate,endDate);
			List<Log> report_view_page_log = reportService.getViewPageByRank(rankId,instFinal,startDate,endDate,instId);
			
			List<Long> RankList = report_log.stream().map(
					(log) -> {
						return log.getUrankId();
					}
				).distinct().collect(Collectors.toList());
			//System.out.println("Staff size: " + RankList.size());
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer i = 0; i < RankList.size(); i++) {
				Long rank_list = RankList.get(i);
				ResourceReoportModel staff_info = new ResourceReoportModel();
				
				staff_info.setHit_count(report_view_page_log.stream()
										   // .filter((n) -> n.getStaffNo().equals(target_staffNo))
										   .filter((n)-> n.getUrankId().equals(rank_list))
											.collect(Collectors.toList())
										    .size());
				List<Log> report_log_valid = report_log.stream().filter(
					//		(log) -> log.getStaffNo().equals(target_staffNo)
						(log) -> log.getUrankId().equals(rank_list) && log.getResult().equals("Success")
						).collect(Collectors.toList());
				
				
				
				
				if(instId==0L) {
						staff_info.setInstitution("ALL");
				}else {			
					
				staff_info.setInstitution(institution_session.stream()
						  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUinstId()))
						  .map(InstitutionsModel::getInstName)
						  .collect(Collectors.toList())
						  .get(0));
				}
//				System.out.println("Report Controller, line 288 , report log valid size - "+report_log_valid.size());
//				System.out.println("Report Controller, line 289 , report log valid - " + report_log_valid.toString());
				
				if ( report_log_valid.size() ==0) {
					System.out.println("Report Controller, line 290 - report log valid is null");
					
//					System.out.println("Report Controller, line 289, staff_info get Section = "+ staff_info.getSection());
					staff_info.setRank(rank_session.stream()
							.filter((n) -> n.getId().equals(rank_list))
							.map(RanksModel::getRankName)
							.collect(Collectors.toList())
							.get(0));
					System.out.println("Report Controller , line 297 staff info Rank - "+ staff_info.getRank());
					staff_info.setIntranet_count(0);
					staff_info.setInternet_count(0);
					staff_info.setTotal_count(0);
				} else {
//					System.out.println("Report Controller 301, list is not null");
//					System.out.println("Report Controller , line 293 - log id = "+ report_log_valid.get(0).getId());
//					System.out.println("Report Controller , line 283, section id - " + report_log_valid.get(0).getUsectionId());
					staff_info.setSection(section_session.stream()
							.filter((n) -> n.getId().equals(report_log_valid.get(0).getUsectionId()))
							.map(SectionModel::getSectionName)
							.collect(Collectors.toList())
							.get(0));
					System.out.println("Report Controller, line 289, staff_info get Section = "+ staff_info.getSection());
					staff_info.setRank(rank_session.stream()
							.filter((n) -> n.getId().equals(report_log_valid.get(0).getUrankId()))
							.map(RanksModel::getRankName)
							.collect(Collectors.toList())
							.get(0));
					System.out.println("Report Controller , line 297 staff info Rank - "+ staff_info.getRank());
					staff_info.setIntranet_count(report_log_valid.stream()
												     .filter((n) -> (n.getChannel() == null || n.getChannel().equals(1)) && n.getResult().equals("Success"))
												     .collect(Collectors.toList())
												     .size());
					staff_info.setInternet_count(report_log_valid.stream()
												     .filter((n) -> (n.getChannel() != null && !n.getChannel().equals(1)) && n.getResult().equals("Success"))
												     .collect(Collectors.toList())
												     .size());
					staff_info.setTotal_count(report_log_valid
						    					  .size());

				}
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Rank", return_data, return_data.size(),session);
		}
    }
	
	
	
	
	
	
	
	
	/**
	 *Report of ViewPage By Rank
	 * 
	**/
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/rank/details/{user_id}")
	public ResponseEntity<JsonResult> getRankReportViewDetails(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<PageNameModel> page_name_session = (List<PageNameModel>) session.getAttribute("page_name");
			
			String target_staff_no = jsonNode.get("staffNo").asText();
			String staff_fullname = user_list_session.stream().filter(
					(u) -> u.getStaffNo().equals(target_staff_no)
				).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0);
			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
			
			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
		
			
			List<Log> detail_log = reportService.getRankDetailByUser(target_staff_no,startDate,endDate);
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer x = 0; x< detail_log.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer i = x;
				staff_info.setStaffNo(target_staff_no);
				staff_info.setFullname(staff_fullname);
				staff_info.setCreatedAt(detail_log.get(i).getCreatedAt());
				staff_info.setInstitution(institution_session.stream()
											  .filter((n) -> n.getId().equals(detail_log.get(i).getUinstId()))
											  .map(InstitutionsModel::getInstName)
											  .collect(Collectors.toList())
											  .get(0));
				staff_info.setSection(section_session.stream()
										  .filter((n) -> n.getId().equals(detail_log.get(i).getUsectionId()))
										  .map(SectionModel::getSectionName)
										  .collect(Collectors.toList())
										  .get(0));
				staff_info.setRank(rank_session.stream()
									   .filter((n) -> n.getId().equals(detail_log.get(i).getUrankId()))
									   .map(RanksModel::getRankName)
									   .collect(Collectors.toList())
									   .get(0));
				staff_info.setPageNameEN(page_name_session.stream()
											 .filter((n) -> n.getId().equals(detail_log.get(i).getTableId()))
											 .map((n) -> {return n.getPageNameEn() + detail_log.get(i).getPkid();})
											 .collect(Collectors.toList())
											 .get(0));
				staff_info.setPageNameTc(page_name_session.stream()
						 .filter((n) -> n.getId().equals(detail_log.get(i).getTableId()))
						 .map((n) -> {return n.getPageNameTc() + detail_log.get(i).getPkid();})
						 .collect(Collectors.toList())
						 .get(0));
				return_data.add(staff_info);
			}
			return JsonResult.listTotal("Detail Report By Rank", return_data, return_data.size(),session);
		}
	}
	
	@RequestMapping("/access/rank/login/{user_id}")
	public ResponseEntity<JsonResult> getLoginReportByRank(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			String target_staff_no = jsonNode.get("staffNo").asText();
//			//System.out.println("Staff NO - "+ target_staff_no);
			Integer report_channel = jsonNode.get("searchType").asInt();
			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
		
			
			List<Log> detail_log = reportService.getRankLoginByUser(target_staff_no,startDate,endDate,report_channel);
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer x = 0; x< detail_log.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer i = x;
				staff_info.setStaffNo(target_staff_no);
				staff_info.setCreatedAt(detail_log.get(i).getCreatedAt());
				return_data.add(staff_info);
			}
			return JsonResult.listTotal("Login Report By Rank", return_data, return_data.size(),session);
		}
	}

	/** 
	 * Score Report
	 * @param user_id
	 * @param jsonNode
	 * @param session
	 * @return
	 */
	

	@SuppressWarnings("unchecked")
	@RequestMapping ("/access/score/{user_id}")
	public ResponseEntity<JsonResult> getScoreReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String, String> user_check =common.checkReportUser(user_id, session);
		if(user_check.get("msg") !="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
			
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank")==null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long instId = jsonNode.get("instId").asLong();
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			//System.out.println("Score Report - ORG start = "+ start +" ORG end = "+end);
			Calendar rightNow2 = Calendar.getInstance();
			rightNow2.setTime(start);
			rightNow2.add(Calendar.DAY_OF_YEAR, 0);
			Date startDate = rightNow2.getTime();
			 List<Long> instFinal = new ArrayList<>();
			    if(instId == 8 || instId == 9) {
			    	instFinal.add(8L);
			    	instFinal.add(9L);
			    } else if (instId == 32 || instId == 16) {
			    	instFinal.add(32L);
			    	instFinal.add(16L);
			    } else {
			    	instFinal.add(instId);
			    }
				
				
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
			Date endDate = rightNow.getTime();
			
			List<ScoreLog> report_score = new ArrayList<>();
			List<Object []> report_data = reportService.getScoreReport1(instFinal, startDate, endDate);
			
			for(Integer i=0; i<report_data.size();i++) {
				ScoreLog temp = new ScoreLog();
				Object[] data = (Object[]) report_data.get(i);
				temp.setId(Long.valueOf(data[0].toString()));
				temp.setCreatedAt((Date) data[1]);
				temp.setScore(Integer.valueOf(data[2].toString()));
				temp.setUserId(Long.valueOf(data[4].toString()));
				//data[5] = data[5]==null? 1 :data[5];
				//System.out.println("User Id - "+ data[4].toString()+ " Inst Id = "+data[5].toString() );
				temp.setUinstId(Long.valueOf(data[5].toString()));
				
				
				report_score.add(temp);
			}
			
			
			
			
			List<Long> instList = report_score.stream().map(
					(log) ->{
						return log.getUinstId();
					}
			).distinct().collect(Collectors.toList());
			List<ScoreReportModel> return_data = new ArrayList<>();
			//System.out.println("Inst List Size " + instList.size());
			for(Integer i = 0; i< instList.size(); i++) {
				
				ScoreReportModel score_info = new ScoreReportModel();
				score_info.setLv1(0);
				score_info.setLv2(0);
				score_info.setLv3(0);
				score_info.setLv4(0);
				List<Long> target_inst = new ArrayList<>();

				
				Long target_instId = instList.get(i);
				target_inst.add(target_instId);
				
				Long second_instId = 0L;
				Long temp_instId = 0L;
				if (target_instId ==8L) {
					second_instId = 9L;
					target_inst.add(second_instId);
				} else if (target_instId==9L){
					second_instId =8L;
					target_inst.add(second_instId);
				} else if (target_instId == 32L) {
					second_instId= 16L;
					target_inst.add(second_instId);
				} else if (target_instId ==16L) {
					
					second_instId= 32L;
					target_inst.add(second_instId);
				}
				
				
				
				Long second = second_instId;
				
			Integer totalUser =userRepository.totalUser(target_inst, "RETIRE", "INVALIDING").size();
				//ScoreLog member = new ScoreLog();
			Map<Long, Integer> score_each = report_score.stream()
					.filter((n) -> n.getUinstId().equals(target_instId) ||n.getUinstId().equals(second) )
					.collect(
					Collectors.groupingBy(ScoreLog::getUserId, Collectors.summingInt(ScoreLog::getScore)));
			//System.out.println("-----target_instId-------"+ target_instId + " how many people"+ score_each.size() );
			score_each.forEach((k,v) -> {
				
				  if (v<500) {
					  score_info.setLv1(score_info.getLv1()+1);
				  } else if ( 499<v && v<1000) {
					  score_info.setLv2(score_info.getLv2()+1);
				  } else if ( 999< v && v<1800) {
					  score_info.setLv3(score_info.getLv3()+1);
				  } else if( 1799 < v) {
					  score_info.setLv4(score_info.getLv4()+1);
				  }
			});
			
			Integer zeroPoint =totalUser- score_each.size();
			//System.out.println("Zero Point = "+ zeroPoint +" Total user = "+ totalUser + " Have Score = "+ score_each.size());
			 score_info.setLv1(score_info.getLv1()+zeroPoint);
			 score_info.setTotal(totalUser);
			 score_info.setInstId(target_instId);
			 score_info.setInstitution(institution_session.stream()
						  .filter((n) -> n.getId().equals(target_instId))
						  .map(InstitutionsModel::getInstName)
						  .collect(Collectors.toList())
						  .get(0));
			return_data.add(score_info);
			
			 }
			
			return JsonResult.listTotal("Report By Score", return_data, return_data.size(),session);
			//return null;
		}
	}
	
	
	
	
	
	
//	@RequestMapping ("/access/score/{user_id}")
//	public ResponseEntity<JsonResult> getScoreReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
//		HashMap<String, String> user_check =common.checkReportUser(user_id, session);
//		if(user_check.get("msg") !="") {
//			return JsonResult.errorMsg(user_check.get("msg").toString());
//			
//		}else {
//			User user = (User) session.getAttribute("user_session");
//			Integer channel = (Integer) session.getAttribute("channel");
//			if(session.getAttribute("rank")==null) {
//				logger.viewReport(user, 0L, "", "Success", channel);
//				common.checkRankSession(session);
//			}
//					Long instId = jsonNode.get("instId").asLong();
//			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
//			
//			
//			
//			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
//			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
//			
//			Calendar rightNow = Calendar.getInstance();
//			rightNow.setTime(end);
//			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
//			Date endDate = rightNow.getTime();
//			
//			List<ScoreLog> report_score = reportService.getScoreReport(instId, startDate, endDate);
//
////			
//			List<Long> instList = report_score.stream().map(
//					(log) ->{
//						return log.getUinstId();
//					}
//			).distinct().collect(Collectors.toList());
//		
//			List<ScoreReportModel> return_data = new ArrayList<>();
//			
//			System.out.println("Inst List Size " + instList.size());
//			
//			
//			for(Integer i = 0; i< instList.size(); i++) {
//				ScoreReportModel score_info = new ScoreReportModel();
//				score_info.setLv1(0);
//				score_info.setLv2(0);
//				score_info.setLv3(0);
//				score_info.setLv4(0);
//				Long target_instId = instList.get(i);
//				
//				//ScoreLog member = new ScoreLog();
//			Map<Long, Integer> score_each = report_score.stream()
//					.filter((n) -> n.getUinstId().equals(target_instId))
//					.collect(
//					Collectors.groupingBy(ScoreLog::getUserId, Collectors.summingInt(ScoreLog::getScore)));
//			
//			score_each.forEach((k,v) -> {
//				  if (v<500) {
//					  score_info.setLv1(score_info.getLv1()+1);
//				  } else if ( 499<v && v<1000) {
//					  score_info.setLv2(score_info.getLv2()+1);
//				  } else if ( 999< v && v<1800) {
//					  score_info.setLv3(score_info.getLv3()+1);
//				  } else if( 1799 < v) {
//					  score_info.setLv4(score_info.getLv4()+1);
//				  }
//			});
//			score_info.setInstId(target_instId);
//			score_info.setTotal(score_each.size());
//			score_info.setInstitution(institution_session.stream()
//					  .filter((n) -> n.getId().equals(target_instId))
//					  .map(InstitutionsModel::getInstName)
//					  .collect(Collectors.toList())
//					  .get(0));
//			
//			return_data.add(score_info);
//			}
//			
//			return JsonResult.listTotal("Report By Score", return_data, return_data.size(),session);
//			//return null;
//		}
//	}
	
	
	

	
	/** 
	 * Score Detail Report
	 * @param user_id
	 * @param jsonNode
	 * @param session
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/score/details/{user_id}")
	public ResponseEntity<JsonResult> getScoreDetailReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		
		HashMap<String, String> user_check =common.checkReportUser(user_id, session);
		if(user_check.get("msg") !="") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
			
		}else {
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank")==null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long instId = jsonNode.get("instId").asLong();
			 List<Long> instFinal = new ArrayList<>();
			    if(instId == 8 || instId == 9) {
			    	instFinal.add(8L);
			    	instFinal.add(9L);
			    } else if (instId == 32 || instId == 16) {
			    	instFinal.add(32L);
			    	instFinal.add(16L);
			    } else {
			    	instFinal.add(instId);
			    }
				
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			//System.out.println("---Before User List -----");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			//System.out.println("User List - "+user_list_session.size() );
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			Calendar rightNow2 = Calendar.getInstance();
			rightNow2.setTime(start);
			rightNow2.add(Calendar.DAY_OF_YEAR, 0);//日子加1日
			Date startDate = rightNow2.getTime();
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
			Date endDate = rightNow.getTime();
			
			
			List<ScoreLog> report_score = new ArrayList<>();
			List<Object[]> report_data = reportService.getScoreReport1(instFinal, startDate, endDate);
			
			for(Integer i=0; i<report_data.size();i++) {
				ScoreLog temp = new ScoreLog();
				Object[] data = (Object[]) report_data.get(i);
				temp.setId(Long.valueOf(data[0].toString()));
				temp.setCreatedAt((Date) data[1]);
				temp.setScore(Integer.valueOf(data[2].toString()));
				temp.setUserId(Long.valueOf(data[4].toString()));
				data[5] = data[5]==null? 1 :data[5];
				temp.setUinstId(Long.valueOf(data[5].toString()));
				
				report_score.add(temp);
			}
			
			
			
			

			List<Long> instList = report_score.stream().map(
					(log) ->{
						return log.getUinstId();
					}
			).distinct().collect(Collectors.toList());
		
			List<ResourceReoportModel> return_data = new ArrayList<>();
		
			
			//System.out.println("Inst List Size " + instList.size());
			Integer level = jsonNode.get("level").asInt();
			
			for(Integer i = 0; i< instList.size(); i++) {
				ScoreReportModel score_info = new ScoreReportModel();
				score_info.setLv1(0);
				score_info.setLv2(0);
				score_info.setLv3(0);
				score_info.setLv4(0);
				Long target_instId = instList.get(i);
				
				//System.out.println(" I = " + i + " target Inst Id = " + target_instId );
				//ScoreLog member = new ScoreLog();
				Map<Long, Integer> score_each = report_score.stream()
					.filter((n) -> n.getUinstId().equals(target_instId))
					.collect(
					Collectors.groupingBy(ScoreLog::getUserId, Collectors.summingInt(ScoreLog::getScore)));
			
			//System.out.println(" Map value = " + score_each.values());
		//	List<ScoreLog> detail_list = reportService.getScoreDetailList(score_each.values(), startDate, endDate);
			
			
				
			score_each.forEach((k,v) -> {
					ResourceReoportModel staff_info = new ResourceReoportModel(); 
					

				  if (v<500 && (level ==1 ||level==0) ) {
					ScoreLog detail  = reportService.getScoreDetail(k, startDate, endDate);
					  Integer score = v;
					  //System.out.println("Level = " + level);
					  staff_info.setLevel(level);
					  staff_info.setUserScore(score);
					  staff_info.setStaffNo(detail.getStaffNo());
					  staff_info.setFullname(user_list_session.stream().filter(
								(u) -> u.getStaffNo().equals(detail.getStaffNo())
								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
					  staff_info.setInstitution(institution_session.stream()
							  .filter((n) -> n.getId().equals(target_instId))
							  .map(InstitutionsModel::getInstName)
							  .collect(Collectors.toList())
							  .get(0));
					
					  staff_info.setRank(rank_session.stream()
							   .filter((n) -> n.getId().equals(detail.getUrankId()))
							   .map(RanksModel::getRankName)
							   .collect(Collectors.toList())
							   .get(0));
					 
					  staff_info.setSection(section_session.stream()
							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
							  .map(SectionModel::getSectionName)
							  .collect(Collectors.toList())
							  .get(0));
					  return_data.add(staff_info);
					  score_info.setLv1(score_info.getLv1()+1);
				  } else if ( 499<v && v<1000 && ( level == 2 || level ==0 )) {
					ScoreLog detail  = reportService.getScoreDetail(k, startDate, endDate);
					  staff_info.setLevel(level);
					  staff_info.setUserScore(v);
					  staff_info.setStaffNo(detail.getStaffNo());
					  staff_info.setFullname(user_list_session.stream().filter(
								(u) -> u.getStaffNo().equals(detail.getStaffNo())
								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
					  staff_info.setInstitution(institution_session.stream()
							  .filter((n) -> n.getId().equals(target_instId))
							  .map(InstitutionsModel::getInstName)
							  .collect(Collectors.toList())
							  .get(0));
					
					  staff_info.setRank(rank_session.stream()
							   .filter((n) -> n.getId().equals(detail.getUrankId()))
							   .map(RanksModel::getRankName)
							   .collect(Collectors.toList())
							   .get(0));
					 
					  staff_info.setSection(section_session.stream()
							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
							  .map(SectionModel::getSectionName)
							  .collect(Collectors.toList())
							  .get(0));
					  return_data.add(staff_info);
					  score_info.setLv2(score_info.getLv2()+1);
				  } else if ( 999< v && v<1800 && ( level == 3 || level == 0) ) {
					ScoreLog detail  = reportService.getScoreDetail(k, startDate, endDate);
					  staff_info.setLevel(level);
					  staff_info.setUserScore(v);
					  staff_info.setStaffNo(detail.getStaffNo());
					  staff_info.setFullname(user_list_session.stream().filter(
								(u) -> u.getStaffNo().equals(detail.getStaffNo())
								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
					  staff_info.setInstitution(institution_session.stream()
							  .filter((n) -> n.getId().equals(target_instId))
							  .map(InstitutionsModel::getInstName)
							  .collect(Collectors.toList())
							  .get(0));
					
					  staff_info.setRank(rank_session.stream()
							   .filter((n) -> n.getId().equals(detail.getUrankId()))
							   .map(RanksModel::getRankName)
							   .collect(Collectors.toList())
							   .get(0));
					 
					  staff_info.setSection(section_session.stream()
							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
							  .map(SectionModel::getSectionName)
							  .collect(Collectors.toList())
							  .get(0));
					  return_data.add(staff_info);
					  score_info.setLv3(score_info.getLv3()+1);
				  } else if( 1799 < v && (level == 4 || level ==0) ) {
					  //System.out.println("---- "+ k+"---"+startDate+"---"+endDate);
				  ScoreLog detail  = reportService.getScoreDetail(k, startDate, endDate);
					  //System.out.println("Level = " + level);
					  staff_info.setLevel(level);
					  staff_info.setUserScore(v);
					  staff_info.setStaffNo(detail.getStaffNo());
					  staff_info.setFullname(user_list_session.stream().filter(
								(u) -> u.getStaffNo().equals(detail.getStaffNo())
								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
					  staff_info.setInstitution(institution_session.stream()
							  .filter((n) -> n.getId().equals(target_instId))
							  .map(InstitutionsModel::getInstName)
							  .collect(Collectors.toList())
							  .get(0));
					
					  staff_info.setRank(rank_session.stream()
							   .filter((n) -> n.getId().equals(detail.getUrankId()))
							   .map(RanksModel::getRankName)
							   .collect(Collectors.toList())
							   .get(0));
					 
					  staff_info.setSection(section_session.stream()
							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
							  .map(SectionModel::getSectionName)
							  .collect(Collectors.toList())
							  .get(0));
					  return_data.add(staff_info);
					  score_info.setLv4(score_info.getLv4()+1);
				  }
			});

			}
			
			return JsonResult.listTotal("Report By Score", return_data, return_data.size(),session);
			//return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/access/score/details/{user_id}")
//	public ResponseEntity<JsonResult> getScoreDetailReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
//		
//		HashMap<String, String> user_check =common.checkReportUser(user_id, session);
//		if(user_check.get("msg") !="") {
//			return JsonResult.errorMsg(user_check.get("msg").toString());
//			
//		}else {
//			User user = (User) session.getAttribute("user_session");
//			Integer channel = (Integer) session.getAttribute("channel");
//			if(session.getAttribute("rank")==null) {
//				logger.viewReport(user, 0L, "", "Success", channel);
//				common.checkRankSession(session);
//			}
//			Long instId = jsonNode.get("instId").asLong();
//			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
//			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
//			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
//			System.out.println("---Before User List -----");
//			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
//			System.out.println("User List - "+user_list_session.size() );
//			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
//			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
//			
//			Calendar rightNow = Calendar.getInstance();
//			rightNow.setTime(end);
//			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
//			Date endDate = rightNow.getTime();
//			
//			
//			List<ScoreLog> report_score = new ArrayList<>();
//			List<Object []> report_data = reportService.getScoreReport1(instId, startDate, endDate);
//			
//			for(Integer i=0; i<report_data.size();i++) {
//				ScoreLog temp = new ScoreLog();
//				Object[] data = (Object[]) report_data.get(i);
//				temp.setId(Long.valueOf(data[0].toString()));
//				temp.setCreatedAt((Date) data[1]);
//				temp.setScore(Integer.valueOf(data[2].toString()));
//				temp.setUserId(Long.valueOf(data[4].toString()));
//				data[5] = data[5]==null? 1 :data[5];
//				temp.setUinstId(Long.valueOf(data[5].toString()));
//				
//				report_score.add(temp);
//			}
//			
//			
//
//			List<Long> instList = report_score.stream().map(
//					(log) ->{
//						return log.getUinstId();
//					}
//			).distinct().collect(Collectors.toList());
//		
//			List<ResourceReoportModel> return_data = new ArrayList<>();
//		
//			
//			System.out.println("Inst List Size " + instList.size());
//			Integer level = jsonNode.get("level").asInt();
//			
//			for(Integer i = 0; i< instList.size(); i++) {
//				ScoreReportModel score_info = new ScoreReportModel();
//				score_info.setLv1(0);
//				score_info.setLv2(0);
//				score_info.setLv3(0);
//				score_info.setLv4(0);
//				Long target_instId = instList.get(i);
//				
//				System.out.println(" I = " + i + " target Inst Id = " + target_instId );
//				//ScoreLog member = new ScoreLog();
//				Map<Long, Integer> score_each = report_score.stream()
//					.filter((n) -> n.getUinstId().equals(target_instId))
//					.collect(
//					Collectors.groupingBy(ScoreLog::getUserId, Collectors.summingInt(ScoreLog::getScore)));
//			
//			System.out.println(" Map value = " + score_each.values());
//			List<ScoreLog> detail_list = reportService.getScoreDetailList(score_each.values(), startDate, endDate);
//			
//			
//				
//			score_each.forEach((k,v) -> {
//					ResourceReoportModel staff_info = new ResourceReoportModel();
//					 ScoreLog detail  = reportService.getScoreDetail(k, startDate, endDate);
//				  if (v<500 && (level ==1 ||level==0) ) {
//					 
//					  Integer score = v;
//					  System.out.println("Level = " + level);
//					  staff_info.setLevel(level);
//					  staff_info.setUserScore(score);
//					  staff_info.setStaffNo(detail.getStaffNo());
//					  staff_info.setFullname(user_list_session.stream().filter(
//								(u) -> u.getStaffNo().equals(detail.getStaffNo())
//								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
//					  staff_info.setInstitution(institution_session.stream()
//							  .filter((n) -> n.getId().equals(target_instId))
//							  .map(InstitutionsModel::getInstName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					
//					  staff_info.setRank(rank_session.stream()
//							   .filter((n) -> n.getId().equals(detail.getUrankId()))
//							   .map(RanksModel::getRankName)
//							   .collect(Collectors.toList())
//							   .get(0));
//					 
//					  staff_info.setSection(section_session.stream()
//							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
//							  .map(SectionModel::getSectionName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					  return_data.add(staff_info);
//					  score_info.setLv1(score_info.getLv1()+1);
//				  } else if ( 499<v && v<1000 && ( level == 2 || level ==0 )) {
//					
//					  staff_info.setLevel(level);
//					  staff_info.setUserScore(v);
//					  staff_info.setStaffNo(detail.getStaffNo());
//					  staff_info.setFullname(user_list_session.stream().filter(
//								(u) -> u.getStaffNo().equals(detail.getStaffNo())
//								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
//					  staff_info.setInstitution(institution_session.stream()
//							  .filter((n) -> n.getId().equals(target_instId))
//							  .map(InstitutionsModel::getInstName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					
//					  staff_info.setRank(rank_session.stream()
//							   .filter((n) -> n.getId().equals(detail.getUrankId()))
//							   .map(RanksModel::getRankName)
//							   .collect(Collectors.toList())
//							   .get(0));
//					 
//					  staff_info.setSection(section_session.stream()
//							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
//							  .map(SectionModel::getSectionName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					  return_data.add(staff_info);
//					  score_info.setLv2(score_info.getLv2()+1);
//				  } else if ( 999< v && v<1800 && ( level == 3 || level == 0) ) {
//					  staff_info.setLevel(level);
//					  staff_info.setUserScore(v);
//					  staff_info.setStaffNo(detail.getStaffNo());
//					  staff_info.setFullname(user_list_session.stream().filter(
//								(u) -> u.getStaffNo().equals(detail.getStaffNo())
//								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
//					  staff_info.setInstitution(institution_session.stream()
//							  .filter((n) -> n.getId().equals(target_instId))
//							  .map(InstitutionsModel::getInstName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					
//					  staff_info.setRank(rank_session.stream()
//							   .filter((n) -> n.getId().equals(detail.getUrankId()))
//							   .map(RanksModel::getRankName)
//							   .collect(Collectors.toList())
//							   .get(0));
//					 
//					  staff_info.setSection(section_session.stream()
//							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
//							  .map(SectionModel::getSectionName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					  return_data.add(staff_info);
//					  score_info.setLv3(score_info.getLv3()+1);
//				  } else if( 1799 < v && (level == 4 || level ==0) ) {
//				
//					  System.out.println("Level = " + level);
//					  staff_info.setLevel(level);
//					  staff_info.setUserScore(v);
//					  staff_info.setStaffNo(detail.getStaffNo());
//					  staff_info.setFullname(user_list_session.stream().filter(
//								(u) -> u.getStaffNo().equals(detail.getStaffNo())
//								).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
//					  staff_info.setInstitution(institution_session.stream()
//							  .filter((n) -> n.getId().equals(target_instId))
//							  .map(InstitutionsModel::getInstName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					
//					  staff_info.setRank(rank_session.stream()
//							   .filter((n) -> n.getId().equals(detail.getUrankId()))
//							   .map(RanksModel::getRankName)
//							   .collect(Collectors.toList())
//							   .get(0));
//					 
//					  staff_info.setSection(section_session.stream()
//							  .filter((n) -> n.getId().equals(detail.getUsectionId()))
//							  .map(SectionModel::getSectionName)
//							  .collect(Collectors.toList())
//							  .get(0));
//					  return_data.add(staff_info);
//					  score_info.setLv4(score_info.getLv4()+1);
//				  }
//			});
//
//			}
//			
//			return JsonResult.listTotal("Report By Score", return_data, return_data.size(),session);
//			//return null;
//		}
//	}
	
	
	
	
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/access/score/details/{user_id}")
//	public ResponseEntity<JsonResult> getScoreDetailReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
//		
//		HashMap<String, String> user_check =common.checkReportUser(user_id, session);
//		if(user_check.get("msg") !="") {
//			return JsonResult.errorMsg(user_check.get("msg").toString());
//			
//		}else {
//			User user = (User) session.getAttribute("user_session");
//			Integer channel = (Integer) session.getAttribute("channel");
//			if(session.getAttribute("rank")==null) {
//				logger.viewReport(user, 0L, "", "Success", channel);
//				common.checkRankSession(session);
//			}
//					Long instId = jsonNode.get("instId").asLong();
//			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
//			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
//			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
//			System.out.println("---Before User List -----");
//			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
//			System.out.println("User List - "+user_list_session.size() );
//			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
//			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
//			
//			Calendar rightNow = Calendar.getInstance();
//			rightNow.setTime(end);
//			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日子加1日
//			Date endDate = rightNow.getTime();
//			
//			List<ScoreLog> report_score = reportService.getScoreReport(instId, startDate, endDate);
//
//			List<Long> instList = report_score.stream().map(
//					(log) ->{
//						return log.getUinstId();
//					}
//			).distinct().collect(Collectors.toList());
//		
//			List<ResourceReoportModel> return_data = new ArrayList<>();
//		
//			
//			System.out.println("Inst List Size " + instList.size());
//			Integer level = jsonNode.get("level").asInt();
//			
//			for(Integer i = 0; i< instList.size(); i++) {
//				ScoreReportModel score_info = new ScoreReportModel();
//				score_info.setLv1(0);
//				score_info.setLv2(0);
//				score_info.setLv3(0);
//				score_info.setLv4(0);
//				Long target_instId = instList.get(i);
//				
//				System.out.println(" I = " + i + " target Inst Id = " + target_instId );
//				//ScoreLog member = new ScoreLog();
//				Map<String, List<ScoreLog>> score_each = report_score.stream()
//					.filter((n) -> n.getUinstId().equals(target_instId))
//					.collect(
//					Collectors.groupingBy(ScoreLog::getStaffNo,Collectors.toList()
//							
//					));
//			
//			System.out.println(" Map value = " + score_each.size());
//				
//			
//
//			}
//			
//			return JsonResult.listTotal("Report By Score", return_data, return_data.size(),session);
//			//return null;
//		}
//	}
//	
	
	Integer staff_number =0;
	Integer total_intranet=0;
	Integer total_internet=0;
	Integer total_mobile =0;
	Integer total_hit=0;
	Integer total_login=0;
	List<String> all_staff = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/download/management/{user_id}")
	public ResponseEntity<JsonResult> DownloadManagementReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			//Integer channel = (Integer) session.getAttribute("channel");
			Integer channel = 1;
			if(session.getAttribute("rank") == null) {
			
				common.checkRankSession(session);
			}
			
			logger.viewReport(user, 0L, "", "Success", channel);
			
			
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			Long target_instId = jsonNode.get("instId").asLong();
			Long rankId =0L;
			Long instId = jsonNode.get("instId").asLong();
			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
			System.out.println("Report Controller, line 1434 : inst Id = "+ instId);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			 List<Long> instAll = new ArrayList<>();
			 instAll.add(0L);
		    List<Long> instFinal = new ArrayList<>();
		    if(instId == 8 || instId == 9) {
		    	instFinal.add(8L);
		    	instFinal.add(9L);
		    } else if (instId == 32 || instId == 16) {
		    	instFinal.add(32L);
		    	instFinal.add(16L);
		    } else {
		    	instFinal.add(instId);
		    }
		    
		    
		    
			Long second_instId = 0L;
			if (target_instId ==8L) {
				second_instId = 9L;
			} else if (target_instId==9L){
				second_instId =8L;
			} else if (target_instId == 32L) {
				second_instId= 16L;
			} else if (target_instId ==16L) {
				second_instId= 32L;
			}
			Long second = second_instId;
			System.out.println("Report Controller , line 1450 : inst Final" + instFinal);
			List<Log> report_log = reportService.getByRank(rankId,instFinal,startDate,endDate);
			List<Log> report_view_page_log = reportService.getViewPageByRank(rankId,instFinal,startDate,endDate,instId);
			List<Long> RankList = new ArrayList<>();
			RankList = report_log.stream().map(
					(log) -> {
						return log.getUrankId();
					}
				)
			.sorted().distinct().collect(Collectors.toList());
			
		
			List<Long> Total_RankList = user_list_session.stream()
					.filter( (n)-> n.getIsDeleted() == 0 && (n.getInstitutionId().equals(target_instId) || n.getInstitutionId().equals(second)))
					.map(
					(User) -> {
						return User.getRankId();
					}
				)
			.sorted().distinct().collect(Collectors.toList());
			
			
			List<HashMap<String,Object>> return_list = new ArrayList<>();
			
			HashMap<String,Object> return_object = new HashMap<>();
				for(Integer i = 0; i < Total_RankList.size(); i++) {
					Long target_staffNo = Total_RankList.get(i);
					List<ResourceReoportModel> return_data = new ArrayList<>();
					
					
					
					String rankName = (rank_session.stream()
							.filter((n) -> n.getId().equals(target_staffNo))
							.map(RanksModel::getRankName)
							.sorted().distinct().collect(Collectors.toList())
							.get(0));
					if(rankName.equals("DTO")) {
						rankName = "ESG";
					}
					
					staff_number =0;
					total_intranet=0;
					total_internet=0;
					total_mobile=0;
					total_hit=0;
					total_login=0;
					
					
				String rankNameFinal= rankName;
					Map<String, Long> HitRate = report_view_page_log.stream()
							.filter((n) -> n.getUrankId().equals(target_staffNo) && (n.getUinstId().equals(target_instId) || n.getUinstId().equals(second)))
							.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
					
					Map<String,Long> dpLogin = report_log.stream()
							.filter((n) -> n.getUrankId().equals(target_staffNo)&& n.getChannel()==1  && (n.getUinstId().equals(target_instId) || n.getUinstId().equals(second)))
							.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
					Map<String,Long> RemoteLogin = report_log.stream()
							.filter((n) -> n.getUrankId().equals(target_staffNo)&& n.getChannel()==3  && (n.getUinstId().equals(target_instId) || n.getUinstId().equals(second)))
							.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
					Map<String,Long> MobileLogin = report_log.stream()
							.filter((n) -> n.getUrankId().equals(target_staffNo)&& n.getChannel()==4  && (n.getUinstId().equals(target_instId) || n.getUinstId().equals(second)))
							.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
					 Map<String,Long> HitRate1 = new TreeMap<String,Long>(HitRate);
					HitRate1.forEach((k,v) -> {
						ResourceReoportModel staff_info = new ResourceReoportModel();
						staff_info.setStaffNo(k);
						staff_info.setRank(rankNameFinal);
						staff_info.setTotal_hit_rate(v.intValue());
						staff_info.setInstitution(institution_session.stream()
								  .filter((n) -> n.getId().equals(target_instId))
								  .map(InstitutionsModel::getInstName)
								  .collect(Collectors.toList())
								  .get(0));
						if (dpLogin.get(k) == null) {
							staff_info.setIntranet_login(0);
						} else {
							staff_info.setIntranet_login(  dpLogin.get(k).intValue());
						}
						if (RemoteLogin.get(k) == null) {
							staff_info.setInternet_login(0);
							
						} else {
							staff_info.setInternet_login(RemoteLogin.get(k).intValue());
						}
						if (MobileLogin.get(k) == null) {
							staff_info.setMobile_login(0);
							
						} else {
							staff_info.setMobile_login(MobileLogin.get(k).intValue());
						}
						
						
						staff_info.setFullname(user_list_session.stream().filter(
						(u) -> u.getStaffNo().equals(k)
						).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
						
						staff_info.setTotal_login(staff_info.getIntranet_login() + staff_info.getInternet_login()+staff_info.getMobile_login());
												
						staff_number =staff_number+1;
						 total_intranet=total_intranet+staff_info.getIntranet_login();
						 total_internet=total_internet+staff_info.getInternet_login();
						 total_mobile = total_mobile+ staff_info.getMobile_login();
						 total_hit= total_hit+staff_info.getTotal_hit_rate();
						
						
						return_data.add(staff_info);
					});
					
//					Start to count non login staff
					
					List<String> all_login_staff = new ArrayList<>();
					all_staff = user_list_session.stream().filter( (n)-> n.getRankId().equals(target_staffNo) && n.getIsDeleted() == 0 && (n.getInstitutionId().equals(target_instId) || n.getInstitutionId().equals(second)))
							.map(User::getStaffNo)
					.sorted().distinct().collect(Collectors.toList());
					
					
					Map<String, Long> temp_all_staff = report_log.stream()
							.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
					temp_all_staff.forEach((k,v) -> {
						all_login_staff.add(k);
						
					});
			
					List<String> differences = all_staff;
					
					differences.removeAll(all_login_staff);
			
					if(differences != null) {
						for(Integer x =0 ;x<differences.size();x++ ) {
							ResourceReoportModel staff_info = new ResourceReoportModel();
							
							staff_info.setStaffNo(differences.get(x));
							staff_info.setRank(rankName);
							staff_info.setTotal_hit_rate(0);
							staff_info.setInstitution(institution_session.stream()
									  .filter((n) -> n.getId().equals(target_instId))
									  .map(InstitutionsModel::getInstName)
									  .collect(Collectors.toList())
									  .get(0));
							
								staff_info.setIntranet_login(0);
						
								staff_info.setInternet_login(0);
								staff_info.setMobile_login(0);
							staff_info.setFullname(user_list_session.stream().filter(
							(u) -> u.getStaffNo().equals(staff_info.getStaffNo())
							).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
							
							staff_info.setTotal_login(0);
							return_data.add(staff_info);
							staff_number =staff_number+1;
						}
					
					}
					
					
					
					List<ResourceReoportModel> new_return_data = new ArrayList<>();
					new_return_data = return_data.stream()
							.sorted(Comparator.comparing(ResourceReoportModel::getStaffNo))
							.collect(Collectors.toList());
					
					
					
					
					ResourceReoportModel staff_info = new ResourceReoportModel();
					staff_info.setInstitution("Total : ");
					staff_info.setRank("");
					staff_info.setFullname("");
					staff_info.setStaffNo(staff_number.toString());
					staff_info.setInternet_login(total_internet);
					staff_info.setIntranet_login(total_intranet);
				
					staff_info.setMobile_login(total_mobile);
					staff_info.setTotal_login(total_internet+total_intranet+total_mobile);
					staff_info.setTotal_hit_rate(total_hit);
					new_return_data.add(staff_info);
					
					
					return_object.put(rankName, new_return_data);	
				}
				HashMap<String,Object> new_return_object = new HashMap<>();
				 TreeMap<String, Object> sorted = new TreeMap<>();
				  sorted.putAll(return_object);
				  for (Map.Entry<String, Object> entry : sorted.entrySet()) {
//					   System.out.println("Key = " + entry.getKey() +
//		                         ", Value = " + entry.getValue());    
					   new_return_object.put(entry.getKey(),entry.getValue());
				  }
			
				return JsonResult.listTotal("Download Management Report", new_return_object, 0,session);
			}

	}
	@SuppressWarnings("unchecked")
	@RequestMapping("/download/inst/{user_id}")
    public ResponseEntity<JsonResult> DownloadInstReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long target_instId = jsonNode.get("instId").asLong();
			Integer report_channel = 1;
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			

			 List<Long> instFinal = new ArrayList<>();
			    if(target_instId == 8 || target_instId == 9) {
			    	instFinal.add(8L);
			    	instFinal.add(9L);
			    } else if (target_instId == 32 || target_instId == 16) {
			    	instFinal.add(32L);
			    	instFinal.add(16L);
			    } else {
			    	instFinal.add(target_instId);
			    }
			    List<Integer> access_channel = new ArrayList<>() ;
			    //System.out.println("Report Channel "+ report_channel.toString());
			    if(String.valueOf(report_channel).equals("1")) {
			    	access_channel.add(0);
			    	access_channel.add(1);
			    	
		        }else {
		        	access_channel.add(2);
		        	access_channel.add(3);
		        	access_channel.add(4);
		        }
			
			 List<Integer> internet_channel = new ArrayList<>();
			 internet_channel.add(2);
			 internet_channel.add(3);
			 internet_channel.add(4);
			//System.out.println("Before report serveice  get inst login ");
			List<Log> detail_log = reportService.getInstLoginByUser(instFinal,start,endDate,access_channel);
			List<Log> internet_log = reportService.getInstLoginByUser(instFinal,start,endDate,internet_channel);
			List<Log> view_detail_log = reportService.getInstDetail(instFinal,start,endDate);
			List<String> staffNoList = view_detail_log.stream().map(
					(log) -> {
						return log.getStaffNo();
					}
				).distinct().collect(Collectors.toList());
			
		
			List<HashMap<String,Object>> return_list = new ArrayList<>();
			
			HashMap<String,Object> return_object = new HashMap<>();
			List<ResourceReoportModel> return_data = new ArrayList<>();
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			//System.out.println("After report serveice  get inst login , detail_log size - "+ detail_log.size());
			for(Integer x = 0; x< detail_log.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer i = x;
				staff_info.setStaffNo(detail_log.get(i).getStaffNo());
				//System.out.println("Staff no ");
				staff_info.setFullname(user_list_session.stream().filter(
						(u) -> u.getStaffNo().equals(detail_log.get(i).getStaffNo())
						).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
				//System.out.println("Full name ");
				staff_info.setCreatedAt(detail_log.get(i).getCreatedAt());
				//System.out.println("Created At  ");
				staff_info.setInstitution(institution_session.stream()
						  .filter((n) -> n.getId().equals(detail_log.get(i).getUinstId()))
						  .map(InstitutionsModel::getInstName)
						  .collect(Collectors.toList())
						  .get(0));
				//System.out.println(" Institution");
				staff_info.setSection(section_session.stream()
									  .filter((n) -> n.getId().equals(detail_log.get(i).getUsectionId()))
									  .map(SectionModel::getSectionName)
									  .collect(Collectors.toList())
									  .get(0));
				//System.out.println(" Section ");
				
				staff_info.setRank(rank_session.stream()
								   .filter((n) -> n.getId().equals(detail_log.get(i).getUrankId()))
								   .map(RanksModel::getRankName)
								   .collect(Collectors.toList())
								   .get(0));
				
				return_data.add(staff_info);
			}
			return_object.put("intranet", return_data);
			//return_object.put("intranet count ", return_data.size());
		
			List<ResourceReoportModel> return_data2 = new ArrayList<>();
			//System.out.println("After report serveice  get inst login , detail_log size - "+ detail_log.size());
			for(Integer x = 0; x< internet_log.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer i = x;
				staff_info.setStaffNo(internet_log.get(i).getStaffNo());
				//System.out.println("Staff no ");
				staff_info.setFullname(user_list_session.stream().filter(
						(u) -> u.getStaffNo().equals(internet_log.get(i).getStaffNo())
						).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
				//System.out.println("Full name ");
				staff_info.setCreatedAt(internet_log.get(i).getCreatedAt());
				//System.out.println("Created At  ");
				staff_info.setInstitution(institution_session.stream()
						  .filter((n) -> n.getId().equals(internet_log.get(i).getUinstId()))
						  .map(InstitutionsModel::getInstName)
						  .collect(Collectors.toList())
						  .get(0));
				//System.out.println(" Institution");
				staff_info.setSection(section_session.stream()
									  .filter((n) -> n.getId().equals(internet_log.get(i).getUsectionId()))
									  .map(SectionModel::getSectionName)
									  .collect(Collectors.toList())
									  .get(0));
				//System.out.println(" Section ");
				
				staff_info.setRank(rank_session.stream()
								   .filter((n) -> n.getId().equals(internet_log.get(i).getUrankId()))
								   .map(RanksModel::getRankName)
								   .collect(Collectors.toList())
								   .get(0));
				
				return_data2.add(staff_info);
			}
			return_object.put("internet", return_data2);
			//return_object.put("internet count", return_data2.size());
			List<ResourceReoportModel> return_data3 = new ArrayList<>();
			for(Integer x = 0; x < staffNoList.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer i = x;

				User target_user = user_list_session.stream().filter(
						(u) -> u.getStaffNo().equals(staffNoList.get(i))
						).collect(Collectors.toList()).get(0);
				
				staff_info.setStaffNo(staffNoList.get(i));
				staff_info.setFullname(target_user.getFullname());
				//staff_info.put("createdAt", detail_log.get(i).getCreatedAt().toString());
				staff_info.setInstitution(institution_session.stream()
											  .filter((n) -> n.getId().equals(target_user.getInstitutionId()))
											  .map(InstitutionsModel::getInstName)
											  .collect(Collectors.toList())
											  .get(0));
				
				staff_info.setSection(section_session.stream()
										  .filter((n) -> n.getId().equals(target_user.getSectionId()))
										  .map(SectionModel::getSectionName)
										  .collect(Collectors.toList())
										  .get(0));
				staff_info.setRank(rank_session.stream()
									   .filter((n) -> n.getId().equals(target_user.getRankId()))
									   .map(RanksModel::getRankName)
									   .collect(Collectors.toList())
									   .get(0));
				staff_info.setHits(detail_log.stream()
						    .filter((n) -> n.getStaffNo().equals(staffNoList.get(i)))
						    .collect(Collectors.toList()).size());
				return_data3.add(staff_info);
			}
			
			return_object.put("page hit", return_data3 );
			return_list.add(return_object);
			return JsonResult.listTotal("Download Report", return_object, 0,session);
		}
	}
	
	Integer total_login_staff=0;
	Integer total_login_mobile=0;
	Integer count_login_staff=0;
	Integer total_zero_login =0;
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/management/{user_id}")
	public ResponseEntity<JsonResult> getManagementReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		//System.out.println("access channel "+user_check.get("access_channel"));
		
		user_check.forEach((key,value)->{
	        //System.out.println(String.format(" Header '%s' = %s", key, value));
	    });
		
		
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			//Integer channel = (Integer) session.getAttribute("channel");
			Integer channel = 1;
			if(session.getAttribute("rank") == null) {
			
				common.checkRankSession(session);
			}
			
			logger.viewReport(user, 0L, "", "Success", channel);
			
			
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			Long rankId =0L;
			Long instId = jsonNode.get("instId").asLong();
			Date startDate = common.textToDateDate(jsonNode.get("startDate").asText());
			Date end = common.textToDateDate(jsonNode.get("endDate").asText());
			System.out.println("Report Controller, line 1434 : inst Id = "+ instId);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			
		    List<Long> instFinal = new ArrayList<>();
		    if(instId == 8 || instId == 9) {
		    	instFinal.add(8L);
		    	instFinal.add(9L);
		    } else if (instId == 32 || instId == 16) {
		    	instFinal.add(32L);
		    	instFinal.add(16L);
		    } else {
		    	instFinal.add(instId);
		    }
			System.out.println("Report Controller , line 1450 : inst Final" + instFinal);
			List<Log> report_log = reportService.getByRank(rankId,instFinal,startDate,endDate);
			List<Log> report_view_page_log = reportService.getViewPageByRank(rankId,instFinal,startDate,endDate,instId);
			
			List<Long> RankList = report_log.stream().map(
					(log) -> {
						return log.getUrankId();
					}
				)
			.sorted().distinct().collect(Collectors.toList());
	
			System.out.println("Report Controller line 1465: "+RankList);
			List<ResourceReoportModel> return_data = new ArrayList<>();
			Long target_instId = instFinal.get(0);
			Long second_instId = 0L;
			if (target_instId ==8L) {
				second_instId = 9L;
			} else if (target_instId==9L){
				second_instId =8L;
			} else if (target_instId == 32L) {
				second_instId= 16L;
			} else if (target_instId ==16L) {
				second_instId= 32L;
			}
			Long second = second_instId;
			
			List<Long> Total_RankList = new ArrayList<>();
			if (target_instId ==0) {
				Total_RankList = user_list_session.stream()
						.filter( (n)-> n.getIsDeleted() == 0 )
						.map(
						(User) -> {
							return User.getRankId();
						}
					)
				.sorted().distinct().collect(Collectors.toList());
				
			} else {
			Total_RankList = user_list_session.stream()
						.filter( (n)-> n.getIsDeleted() == 0 && (n.getInstitutionId().equals(target_instId) || n.getInstitutionId().equals(second)))
						.map(
						(User) -> {
							return User.getRankId();
						}
					)
				.sorted().distinct().collect(Collectors.toList());
				
			}
			
		
	
			
			
			
			
			staff_number =0;
			total_intranet=0;
			total_internet=0;
			total_mobile = 0;
			total_hit=0;
			total_login=0;
			total_login_staff=0;
			total_login_mobile=0;
			total_zero_login=0;
			for(Integer i = 0; i < Total_RankList.size(); i++) {
				Long target_staffNo = Total_RankList.get(i);
				
				
				
				ResourceReoportModel staff_info = new ResourceReoportModel();
				
				if(RankList.contains(target_staffNo) ) {
					staff_info.setTotal_hit_rate(report_view_page_log.stream()
							   // .filter((n) -> n.getStaffNo().equals(target_staffNo))
							   .filter((n)-> n.getUrankId().equals(target_staffNo))
								.collect(Collectors.toList())
							    .size());
						List<Log> report_log_valid = report_log.stream().filter(
							//		(log) -> log.getStaffNo().equals(target_staffNo)
								(log) -> log.getUrankId().equals(target_staffNo) && log.getResult().equals("Success")
								).collect(Collectors.toList());
						
						System.out.println("Report Controller , line 1827 , mobile login size "+ report_log_valid.size());
						List<Log> mobile_login_log = report_view_page_log.stream().filter(
								(log) -> log.getUrankId().equals(target_staffNo) && log.getTableId() ==1 && log.getChannel() ==4 && log.getResult().equals("Success") 
								).collect(Collectors.toList());
						System.out.println("Report Controller , line 1832 , mobile login size "+ mobile_login_log.size());
						
						if(instId==0L) {
								staff_info.setInstitution("ALL");
						}else {
						staff_info.setInstitution(institution_session.stream()
								  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUinstId()))
								  .map(InstitutionsModel::getInstName)
								  .collect(Collectors.toList())
								  .get(0));
						}
					
					//	staff_info.setSection(section_session.stream()
					//			.filter((n) -> n.getId().equals(report_log_valid.get(0).getUsectionId()))
					//			.map(SectionModel::getSectionName)
					//			.collect(Collectors.toList())
					//			.get(0));
						staff_info.setRank(rank_session.stream()
								.filter((n) -> n.getId().equals(report_log_valid.get(0).getUrankId()))
								.map(RanksModel::getRankName)
								.collect(Collectors.toList())
								.get(0));
						if(instId==0L) {
							
					
						staff_info.setNo_of_Staff_Logged_in(rank_session.stream()
								.filter((n) -> n.getId().equals(report_log_valid.get(0).getUrankId()))
								.map(RanksModel::getRankName)
								.collect(Collectors.toList()).size());
						
						List<String> mobile_login_staff = new ArrayList<>();
						
						System.out.println("Report Controller , line 1863 : rank id = " + target_staffNo + " inst id = " +target_instId);
						Map<String, Long> temp_mobile_staff = report_log.stream().filter((n) -> n.getUrankId().equals(target_staffNo)&& n.getChannel() == 4 )
						.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
						
						temp_mobile_staff.forEach((k,v) -> {
							mobile_login_staff.add(k);
									});
						System.out.println("Report Controller, line 1870: mobile login list = "+mobile_login_staff);
						
					
							
							   Integer temp_final_mobile  = mobile_login_staff.size();
//					
							   System.out.println("Report Controller, line 1909 -- mobile login staff ; "+mobile_login_staff);
				
							   staff_info.setNo_of_mobile_staff_login(temp_final_mobile);
						
						staff_info.setStaff_strength(user_list_session.stream()
								.filter((n) -> n.getRankId().equals(report_log_valid.get(0).getUrankId()))
								.collect(Collectors.toList()).size());
						} else {
							System.out.println("Report Controller, line 1525 :  target_staffNo "+ target_staffNo + " inst id = "+ report_log_valid.get(0).getUinstId());
							Map<String, Long> no_staff_log = report_log_valid.stream()
									.filter((n) -> n.getUrankId().equals(target_staffNo) && (n.getUinstId().equals(target_instId) || n.getUinstId().equals(second)))
									.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
							count_login_staff=0;
							no_staff_log.forEach((k,v) -> {
								count_login_staff =count_login_staff+1 ;
							});
					//			
							List<String> all_login_staff = new ArrayList<>();
							Map<String, Long> temp_all_staff = report_log.stream().filter((n) -> n.getUrankId().equals(target_staffNo) && (n.getUinstId().equals(target_instId) || n.getUinstId().equals(second)))
							.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
							
							temp_all_staff.forEach((k,v) -> {
									all_login_staff.add(k);
										});
							
							
							List<String> mobile_login_staff = new ArrayList<>();
					
							System.out.println("Report Controller , line 1892 : rank id = " + target_staffNo + " inst id = " +target_instId);
							Map<String, Long> temp_mobile_staff = report_log.stream().filter((n) -> n.getUrankId().equals(target_staffNo)&& n.getChannel() == 4 &&(n.getUinstId().equals(target_instId) || n.getUinstId().equals(second)))
							.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
							
							temp_mobile_staff.forEach((k,v) -> {
								mobile_login_staff.add(k);
										});
							System.out.println("Report Controller , line 1898 " + all_login_staff.size() + " Mobile log staff - "+ mobile_login_staff.size());
							staff_info.setNo_of_Staff_Logged_in(all_login_staff.size() );
							System.out.println("Report Controller, line 1647: mobile login list = "+mobile_login_staff);
							
						
								
//							   if (mobile_login_staff.size()>0) {
								   Integer temp_final_mobile  = mobile_login_staff.size();
//								staff_info.setNo_of_mobile_staff_login(temp_final_mobile.toString());
//							   } else {
								   System.out.println("Report Controller, line 1909 -- mobile login staff ; "+mobile_login_staff);
								   staff_info.setNo_of_mobile_staff_login(0);
								   staff_info.setNo_of_mobile_staff_login(temp_final_mobile);
//							   }
							
							System.out.println("Report Controller, line 1884 , No mobile login " + staff_info.getNo_of_mobile_staff_login());
							all_staff = user_list_session.stream().filter( (n)-> n.getRankId().equals(target_staffNo) && n.getIsDeleted() == 0 && (n.getInstitutionId().equals(target_instId) || n.getInstitutionId().equals(second)))
									.map(User::getStaffNo)
							.sorted().distinct().collect(Collectors.toList());
							
							staff_info.setStaff_strength(all_staff.size());
							
						}
						
						if (staff_info.getStaff_strength() - staff_info.getNo_of_Staff_Logged_in() < 0 ) {
							staff_info.setZeroLogin(0);
						} else {
							staff_info.setZeroLogin(staff_info.getStaff_strength() - staff_info.getNo_of_Staff_Logged_in() );;
						}
						staff_info.setIntranet_login(report_log_valid.stream()
													     .filter((n) -> (n.getChannel() == null || n.getChannel().equals(1)) && n.getResult().equals("Success"))
													     .collect(Collectors.toList())
													     .size());
						staff_info.setInternet_login(report_log_valid.stream()
													     .filter((n) -> (n.getChannel() != null && n.getChannel().equals(3)) && n.getResult().equals("Success"))
													     .collect(Collectors.toList())
													     .size());
						staff_info.setMobile_login(report_log_valid.stream()
													     .filter((n) -> (n.getChannel() != null && n.getChannel().equals(4)) && n.getResult().equals("Success"))
													     .collect(Collectors.toList())
													     .size());
						staff_info.setTotal_login(report_log_valid
							    					  .size());
						
						if (staff_info.getNo_of_Staff_Logged_in() == null ||staff_info.getStaff_strength()==null) {
							staff_info.setLogin_percentage("0%");
						} else {
						float percentage = staff_info.getNo_of_Staff_Logged_in().floatValue() * 100 /  staff_info.getStaff_strength().floatValue();
						staff_info.setLogin_percentage( String.format("%.02f", percentage) +"%");
						}
						if ((staff_info.getIntranet_login()+staff_info.getInternet_login() == 0 )|| staff_info.getStaff_strength() ==0){
							staff_info.setLogin_per_staff_strength("0");
						} else {
							float per_staff = (staff_info.getIntranet_login().floatValue()+staff_info.getInternet_login().floatValue()+staff_info.getMobile_login().floatValue()) /staff_info.getStaff_strength().floatValue();
						staff_info.setLogin_per_staff_strength(String.format("%.02f", per_staff));
						}
						return_data.add(staff_info);
						
						List<String> all_login_staff = new ArrayList<>();
						all_staff = user_list_session.stream().filter( (n)-> n.getRankId().equals(target_staffNo) && n.getIsDeleted() == 0 && (n.getInstitutionId().equals(target_instId) || n.getInstitutionId().equals(second)))
								.map(User::getStaffNo)
						.sorted().distinct().collect(Collectors.toList());
						
						
						Map<String, Long> temp_all_staff = report_log.stream()
								.collect(Collectors.groupingBy(Log::getStaffNo,Collectors.counting()));
						temp_all_staff.forEach((k,v) -> {
							all_login_staff.add(k);
							
						});
				
						List<String> differences = all_staff;
						
						differences.removeAll(all_login_staff);
						System.out.println("Report Controller, line 1879 : difference size = "+ differences.size());
						
						staff_number = staff_number+staff_info.getStaff_strength()+ differences.size();
						total_zero_login = 	total_zero_login + staff_info.getZeroLogin();
						total_login_mobile = total_login_mobile + staff_info.getNo_of_mobile_staff_login();
						total_mobile = total_mobile + staff_info.getMobile_login();
						total_intranet=total_intranet+ staff_info.getIntranet_login();
						total_internet=total_internet + staff_info.getInternet_login();
						total_hit = total_hit+staff_info.getTotal_hit_rate() ;
						total_login= total_login + staff_info.getTotal_login();
						if (staff_info.getNo_of_Staff_Logged_in() ==null) {
							
						} else {
						total_login_staff= total_login_staff +	staff_info.getNo_of_Staff_Logged_in();
						}

				} else {
					staff_info.setTotal_hit_rate(0);
					System.out.println("Report Controller , line 1900 : target inst Id = " +target_instId);
					if (target_instId == 0) {
						staff_info.setInstitution("All");
						
					} else { 
						staff_info.setInstitution(institution_session.stream()
								  .filter((n) -> n.getId().equals(target_instId))
								  .map(InstitutionsModel::getInstName)
								  .collect(Collectors.toList())
								  .get(0));
					}
					
					System.out.println("Report Controller , line 1866 : target_staffNo = "+ target_staffNo);
					staff_info.setRank(rank_session.stream()
							.filter((n) -> n.getId().equals(target_staffNo))
							.map(RanksModel::getRankName)
							.collect(Collectors.toList())
							.get(0));
					all_staff = user_list_session.stream().filter( (n)-> n.getRankId().equals(target_staffNo) && n.getIsDeleted() == 0 && (n.getInstitutionId().equals(target_instId) || n.getInstitutionId().equals(second)))
					.map(User::getStaffNo)
					.sorted().distinct().collect(Collectors.toList());

					staff_info.setStaff_strength(all_staff.size());
					staff_info.setNo_of_Staff_Logged_in(0);
					staff_info.setNo_of_mobile_staff_login(0);
					staff_info.setZeroLogin(staff_info.getStaff_strength());
					staff_info.setMobile_login(0);
					staff_info.setIntranet_login(0);
					staff_info.setInternet_login(0);
					staff_info.setTotal_login(0);
					staff_info.setLogin_percentage( "0%");
					staff_info.setLogin_per_staff_strength("0");
					total_zero_login = 	total_zero_login + staff_info.getZeroLogin();
					staff_number = staff_number+staff_info.getStaff_strength();
					return_data.add(staff_info);
				}
				
							}
			List<ResourceReoportModel> new_return_data = new ArrayList<>();
			new_return_data= return_data.stream().sorted(Comparator.comparing(ResourceReoportModel::getRank)).collect(Collectors.toList());
			
			
			
			
			
			ResourceReoportModel staff_info = new ResourceReoportModel();
						
			staff_info.setInstitution("");
			staff_info.setRank("Total : ");
			
			staff_info.setNo_of_Staff_Logged_in(total_login_staff);
			staff_info.setNo_of_mobile_staff_login(total_login_mobile);
		
			staff_info.setMobile_login(total_mobile);
			staff_info.setIntranet_login(total_intranet);
			staff_info.setInternet_login(total_internet);
			staff_info.setTotal_login(total_login);
			staff_info.setTotal_hit_rate(total_hit);
			staff_info.setZeroLogin(total_zero_login);
			staff_info.setStaff_strength(total_login_staff+total_zero_login );
			float percentage = staff_info.getNo_of_Staff_Logged_in().floatValue() * 100 /  staff_info.getStaff_strength().floatValue();
			if(percentage >100) {
				staff_info.setLogin_percentage("100%");
			} else {
				staff_info.setLogin_percentage( String.format("%.02f", percentage) +"%");
			}
			float per_staff = (staff_info.getIntranet_login().floatValue()+staff_info.getInternet_login().floatValue()) /staff_info.getStaff_strength().floatValue();
			staff_info.setLogin_per_staff_strength(String.format("%.02f", per_staff));
			new_return_data.add(staff_info);
			return JsonResult.listTotal("Report By Management", new_return_data, new_return_data.size(),session);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/inst/{user_id}")
    public ResponseEntity<JsonResult> getInstReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			System.out.println("User Group = "+ user_check.get("user_group"));
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			
			Long instId = jsonNode.get("instId").asLong();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			System.out.println("(Inst Report From String ) Start Date = "+ startDate1 + " End Date = "+ endDate1);
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			List<User> exist_user = (List<User>) session.getAttribute("exist_user");
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			System.out.println("(Inst Report ) Start Date = "+ start + " End Date = "+ endDate);
			
			 List<Long> instFinal = new ArrayList<>();
			    if(instId == 8 || instId == 9) {
			    	instFinal.add(8L);
			    	instFinal.add(9L);
			    } else if (instId == 32 || instId == 16) {
			    	instFinal.add(32L);
			    	instFinal.add(16L);
			    } else {
			    	instFinal.add(instId);
			    }
			
			
			List<Log> report_log = reportService.getByInst(instFinal,start,endDate);
			//System.out.println("Inst Id = ".gr+ instId+ "Start Date = "+ startDate+ "End Date = "+endDate);
			//List<Integer> login_user =report_log.stream().collect(Collectors.groupingBy(Log::getCreatedBy));
			List<Long> login_user = report_log.stream().map(
					(log) -> {
						return log.getCreatedBy();
					}
				).distinct().collect(Collectors.toList());
			List<Long> login_id = login_user.stream().distinct().collect(Collectors.toList());
			
			List<Log> report_view_page_log = reportService.getViewPageByInst(instFinal,start,endDate);
		
			List<Long> instList = new ArrayList<>();
			//System.out.println("Inst Id = "+ instId+ "Start Date = "+ startDate+ "End Date = "+endDate);
			if( user_check.get("user_group").equals("5")) {
			 instList = report_log.stream().map(
						(log) -> {
							return log.getUinstId();
						}
					).distinct().collect(Collectors.toList());
			} else {
			 instList = report_log.stream().map(
					(log) -> {
						return log.getUinstId();
					}
				).distinct().collect(Collectors.toList());
			}
			System.out.println("Inst size: " + instList.size());
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			
			
			for(Integer i = 0; i < instList.size(); i++) {
				Long target_instId = instList.get(i);
				Long second_instId = 0L;
				if (target_instId ==8L) {
					second_instId = 9L;
				} else if (target_instId==9L){
					second_instId =8L;
				} else if (target_instId == 32L) {
					second_instId= 16L;
				} else if (target_instId ==16L) {
					second_instId= 32L;
				}
				Long second = second_instId;
				if (target_instId==8L ||  target_instId==16L){
					
				} else {
				ResourceReoportModel inst_info = new ResourceReoportModel();
				
				
				List<User> inst_user = exist_user.stream().filter( 
						(n) -> (n.getInstitutionId().equals(target_instId) || n.getInstitutionId().equals(second)))
						.collect(Collectors.toList());
				
				
				
				
				System.out.println("Report Controller : Line 1495 = I = "+ i + " target_instId = " + target_instId + " Second = " +  second);
				inst_info.setHit_count(report_view_page_log.stream()
										    .filter((n) -> n.getUinstId().equals(target_instId) || n.getUinstId().equals(second))
										    .collect(Collectors.toList())
										    .size());
				List<Log> report_log_valid = report_log.stream().filter(
							(log) -> (log.getUinstId().equals(target_instId) || log.getUinstId().equals(second) ) && log.getResult().equals("Success")
						).collect(Collectors.toList());
				inst_info.setInstitution(institution_session.stream()
											  .filter((n) -> n.getId().equals(target_instId))
											  .map(InstitutionsModel::getInstName)
											  .collect(Collectors.toList())
											  .get(0));
				inst_info.setInstitutionId(institution_session.stream()
											  .filter((n) -> n.getId().equals(target_instId))
											  .map(InstitutionsModel::getId)
											  .collect(Collectors.toList())
											  .get(0));
				inst_info.setIntranet_count(report_log_valid.stream()
											     .filter((n) -> (n.getChannel() == null || n.getChannel().equals(1) && n.getResult().equals("Success")) )
											     .collect(Collectors.toList())
											     .size());
				inst_info.setInternet_count(report_log_valid.stream()
											     .filter((n) ->( n.getChannel() != null && !n.getChannel().equals(1)) && n.getResult().equals("Success"))
											     .collect(Collectors.toList())
											     .size());
				inst_info.setTotal_count(report_log_valid
					    					  .size());
				return_data.add(inst_info);
				
				}
			}
			
			return JsonResult.listTotal("Report By Inst", return_data, return_data.size(),session);
		}
    }
	
	/**
	 *Report of ViewPage By Inst
	 * 
	**/
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/inst/details/{user_id}")
	public ResponseEntity<JsonResult> getInstReportViewDetails(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			Long target_instId = jsonNode.get("instId").asLong();
			
			 List<Long> instFinal = new ArrayList<>();
			    if(target_instId == 8 || target_instId == 9) {
			    	instFinal.add(8L);
			    	instFinal.add(9L);
			    } else if (target_instId == 32 || target_instId == 16) {
			    	instFinal.add(32L);
			    	instFinal.add(16L);
			    } else {
			    	instFinal.add(target_instId);
			    }
			
			
				String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
				String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
				
				Date start = common.textToDateDate(startDate1);
				Date end = common.textToDateDate(endDate1);
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			System.out.println("(Inst Report Detail ) Start Date = "+ start + " End Date = "+ endDate);
			System.out.println("=====  Before report service get inst detail ====== Inst id = "+instFinal);
			List<Log> detail_log = reportService.getInstDetail(instFinal,start,endDate);
			List<ResourceReoportModel> return_data = new ArrayList<>();
			List<String> staffNoList = detail_log.stream().map(
					(log) -> {
						return log.getStaffNo();
					}
				).distinct().collect(Collectors.toList());
			//System.out.println("Staff size: " + staffNoList.size());
			
			for(Integer x = 0; x < staffNoList.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer i = x;

				User target_user = user_list_session.stream().filter(
						(u) -> u.getStaffNo().equals(staffNoList.get(i))
						).collect(Collectors.toList()).get(0);
				
				staff_info.setStaffNo(staffNoList.get(i));
				staff_info.setFullname(target_user.getFullname());
				//staff_info.put("createdAt", detail_log.get(i).getCreatedAt().toString());
//				staff_info.setInstitution(institution_session.stream()
//											  .filter((n) -> n.getId().equals(target_user.getInstitutionId()))
//											  .map(InstitutionsModel::getInstName)
//											  .collect(Collectors.toList())
//											  .get(0));
				
				
				staff_info.setInstitution(institution_session.stream()
						  .filter((n) -> n.getId().equals(target_instId))
						  .map(InstitutionsModel::getInstName)
						  .collect(Collectors.toList())
						  .get(0));
				
				staff_info.setSection(section_session.stream()
										  .filter((n) -> n.getId().equals(target_user.getSectionId()))
										  .map(SectionModel::getSectionName)
										  .collect(Collectors.toList())
										  .get(0));
				staff_info.setRank(rank_session.stream()
									   .filter((n) -> n.getId().equals(target_user.getRankId()))
									   .map(RanksModel::getRankName)
									   .collect(Collectors.toList())
									   .get(0));
				staff_info.setHits(detail_log.stream()
						    .filter((n) -> n.getStaffNo().equals(staffNoList.get(i)))
						    .collect(Collectors.toList()).size());
				return_data.add(staff_info);
			}
			return JsonResult.listTotal("Detail Report By Inst", return_data, return_data.size(),session);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/inst/login/{user_id}")
	public ResponseEntity<JsonResult> getLoginReportByInst(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session){
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long target_instId = jsonNode.get("instId").asLong();
			Integer report_channel = jsonNode.get("searchType").asInt();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			

			 List<Long> instFinal = new ArrayList<>();
			    if(target_instId == 8 || target_instId == 9) {
			    	instFinal.add(8L);
			    	instFinal.add(9L);
			    } else if (target_instId == 32 || target_instId == 16) {
			    	instFinal.add(32L);
			    	instFinal.add(16L);
			    } else {
			    	instFinal.add(target_instId);
			    }
			    List<Integer> access_channel = new ArrayList<>() ;
			    //System.out.println("Report Channel "+ report_channel.toString());
			    if(String.valueOf(report_channel).equals("1")) {
			    	access_channel.add(0);
			    	access_channel.add(1);
			    	
		        }else {
		        	access_channel.add(2);
		        	access_channel.add(3);
		        	access_channel.add(4);
		        }
			
			
		
			//System.out.println("Before report serveice  get inst login ");
			List<Log> detail_log = reportService.getInstLoginByUser(instFinal,start,endDate,access_channel);
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			//System.out.println("After report serveice  get inst login , detail_log size - "+ detail_log.size());
			for(Integer x = 0; x< detail_log.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer i = x;
				staff_info.setStaffNo(detail_log.get(i).getStaffNo());
				//System.out.println("Staff no ");
				staff_info.setFullname(user_list_session.stream().filter(
						(u) -> u.getStaffNo().equals(detail_log.get(i).getStaffNo())
						).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
				//System.out.println("Full name ");
				staff_info.setCreatedAt(detail_log.get(i).getCreatedAt());
				//System.out.println("Created At  ");
				staff_info.setInstitution(institution_session.stream()
						  .filter((n) -> n.getId().equals(detail_log.get(i).getUinstId()))
						  .map(InstitutionsModel::getInstName)
						  .collect(Collectors.toList())
						  .get(0));
				//System.out.println(" Institution");
				staff_info.setSection(section_session.stream()
									  .filter((n) -> n.getId().equals(detail_log.get(i).getUsectionId()))
									  .map(SectionModel::getSectionName)
									  .collect(Collectors.toList())
									  .get(0));
				//System.out.println(" Section ");
				
				staff_info.setRank(rank_session.stream()
								   .filter((n) -> n.getId().equals(detail_log.get(i).getUrankId()))
								   .map(RanksModel::getRankName)
								   .collect(Collectors.toList())
								   .get(0));
				
				return_data.add(staff_info);
			}
			return JsonResult.listTotal("Login Report By Rank", return_data, return_data.size(),session);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/staff/{user_id}")
    public ResponseEntity<JsonResult> getStaffReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		
		//System.out.println("session user group "+session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			
			//System.out.println("User Inst ="+user_check.get("user_institution"));
			Long user_inst =  Long.parseLong(user_check.get("user_institution"));
			//System.out.println("User Group = "+ user_check.get("user_group"));
			Long sub_inst =0L;
			if(user_inst==8) {
				sub_inst=9L;
			}else if( user_inst==9) {
				sub_inst=8L;
			}else if( user_inst==32) {
				sub_inst=16L;
			}else if( user_inst==16) {
				sub_inst=32L;
			}
			
			Long final_sub = sub_inst;
			
			
			
			
			
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			String staffNo = jsonNode.get("staffNo") == null ? "" : jsonNode.get("staffNo").asText();
			String fullname = jsonNode.get("fullname") == null ? "" : jsonNode.get("fullname").asText();
			//System.out.println("fullname = "+ fullname.length());
			Integer fullLength = fullname.length();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			
			//System.out.println("End Date : "+endDate);
		
			List<Log> report_log_full = reportService.getByStaffInfo(staffNo,fullname,start,endDate);
			//List<Log> report_view_page_log = reportService.getViewPageByStaffInfo(staffNo,fullname,startDate,endDate);
			List<String> staffList = report_log_full.stream()
					.map(
					(log) -> {
						return log.getStaffNo();
					}
				).distinct().collect(Collectors.toList());
			
			
			//System.out.println("User group = "+ user_check.get("user_group").toString()  +"staffListOrg "+staffList.size());
			//System.out.println("user Inst = "+ user_inst+" final_sub "+final_sub);
			
			List<String> staffList2 = report_log_full.stream()
					.filter((n)-> n.getUinstId().equals(user_inst) || n.getUinstId().equals(final_sub) ).map(
					(log) -> {
						return log.getStaffNo();
					}
				).distinct().collect(Collectors.toList());
//			List<Log> report_view_page_log = report_log_full.stream()
//					 .filter((n) -> n.getLogtype().equals(2L)|| n.getLogtype().equals(1L))
//					 .collect(Collectors.toList());
			
			//System.out.println("staffList2 "+ staffList2.size());
			Integer is_match = staffList.size()-staffList2.size();
			
			
			if(user_check.get("user_group").equals("5") ) {
				
			//System.out.println("Can get staff value Super Admin ");
			List<Log> report_view_page_log = report_log_full;
			
			List<Log> report_log = report_log_full.stream()
					 .filter((n) -> n.getLogtype().equals(1L))
					 .collect(Collectors.toList());
			//System.out.println("Inst size: " + staffList.size());
			
			List<Log> report_log_resource = report_log_full.stream()
					 .filter((n) -> n.getLogtype().equals(2L) && n.getTableId().equals(2L))
					 .collect(Collectors.toList());
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer i = 0; i < staffList.size(); i++) {
				String target_staffNo = staffList.get(i);
				ResourceReoportModel staff_info = new ResourceReoportModel();
				
				staff_info.setStaffNo(target_staffNo);
				
				staff_info.setFullname(user_list_session.stream().filter(
						(u) -> u.getStaffNo().equals(target_staffNo)
					).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
				
				staff_info.setHit_count(report_view_page_log.stream()
										    .filter((n) -> n.getStaffNo().equals(target_staffNo))
										    .collect(Collectors.toList())
										    .size());
				staff_info.setResource_count(report_log_resource.stream()
											    .filter((n) -> n.getStaffNo().equals(target_staffNo))
											    .collect(Collectors.toList())
											    .size());
				List<Log> report_log_valid = report_log.stream().filter(
							(log) -> log.getStaffNo().equals(target_staffNo)
						).collect(Collectors.toList());
				staff_info.setInstitution(institution_session.stream()
											  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUinstId()))
											  .map(InstitutionsModel::getInstName)
											  .collect(Collectors.toList())
											  .get(0));
				staff_info.setSection(section_session.stream()
						  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUsectionId()))
						  .map(SectionModel::getSectionName)
						  .collect(Collectors.toList())
						  .get(0));
				staff_info.setRank(rank_session.stream()
									   .filter((n) -> n.getId().equals(report_log_valid.get(0).getUrankId()))
									   .map(RanksModel::getRankName)
									   .collect(Collectors.toList())
									   .get(0));
				staff_info.setIntranet_count(report_log_valid.stream()
											     .filter((n) ->( n.getChannel() == null || n.getChannel().equals(1)) &&  n.getResult().equals("Success") )
											     .collect(Collectors.toList())
											     .size());
				staff_info.setInternet_count(report_log_valid.stream()
											     .filter((n) ->( n.getChannel() != null && !n.getChannel().equals(1)) && n.getResult().equals("Success"))
											     .collect(Collectors.toList())
											     .size());
				staff_info.setTotal_count(report_log_valid.stream()
					     					    .filter((n)->n.getResult().equals("Success"))
					     						.collect(Collectors.toList())
					     						.size());
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Inst", return_data, return_data.size(),session);
			} else if( (staffList.size() == staffList2.size()|| fullLength>1 )  &&  staffList.size()!=0  ) {
			
				//System.out.println("FullName != null && staffList.size()!=0 || staffList.size() == staffList2.size() &&  staffList.size()!=0");
				List<Log> report_view_page_log = report_log_full;
				
				List<Log> report_log = report_log_full.stream()
						 .filter((n) -> n.getLogtype().equals(1L))
						 .collect(Collectors.toList());
				//System.out.println("Inst size: " + staffList2.size());
				
				List<Log> report_log_resource = report_log_full.stream()
						 .filter((n) -> n.getLogtype().equals(2L) && n.getTableId().equals(2L))
						 .collect(Collectors.toList());
				
				List<ResourceReoportModel> return_data = new ArrayList<>();
				
				for(Integer i = 0; i < staffList2.size(); i++) {
					String target_staffNo = staffList2.get(i);
					ResourceReoportModel staff_info = new ResourceReoportModel();
					
					staff_info.setStaffNo(target_staffNo);
					
					staff_info.setFullname(user_list_session.stream().filter(
							(u) -> u.getStaffNo().equals(target_staffNo)
						).map((u) -> {return u.getFullname();}).collect(Collectors.toList()).get(0));
					
					staff_info.setHit_count(report_view_page_log.stream()
											    .filter((n) -> n.getStaffNo().equals(target_staffNo))
											    .collect(Collectors.toList())
											    .size());
					staff_info.setResource_count(report_log_resource.stream()
												    .filter((n) -> n.getStaffNo().equals(target_staffNo))
												    .collect(Collectors.toList())
												    .size());
					List<Log> report_log_valid = report_log.stream().filter(
								(log) -> log.getStaffNo().equals(target_staffNo)
							).collect(Collectors.toList());
					staff_info.setInstitution(institution_session.stream()
												  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUinstId()))
												  .map(InstitutionsModel::getInstName)
												  .collect(Collectors.toList())
												  .get(0));
					staff_info.setSection(section_session.stream()
							  .filter((n) -> n.getId().equals(report_log_valid.get(0).getUsectionId()))
							  .map(SectionModel::getSectionName)
							  .collect(Collectors.toList())
							  .get(0));
					staff_info.setRank(rank_session.stream()
										   .filter((n) -> n.getId().equals(report_log_valid.get(0).getUrankId()))
										   .map(RanksModel::getRankName)
										   .collect(Collectors.toList())
										   .get(0));
					staff_info.setIntranet_count(report_log_valid.stream()
												     .filter((n) -> n.getChannel() == null || n.getChannel().equals(1))
												     .collect(Collectors.toList())
												     .size());
					staff_info.setInternet_count(report_log_valid.stream()
												     .filter((n) -> n.getChannel() != null && !n.getChannel().equals(1))
												     .collect(Collectors.toList())
												     .size());
					staff_info.setTotal_count(report_log_valid.size());
					return_data.add(staff_info);
				}
				
				return JsonResult.listTotal("Report By Inst", return_data, return_data.size(),session);
			
			
			
			} else if (staffList.size()==0){
			//System.out.println("No data");
				return JsonResult.errorMsgNodata("No data ");
			}else  {
				return JsonResult.errorMsg22("Inst not match ");
				
			}
		}
    }
	
	@RequestMapping("/access/staff/login/{user_id}")
    public ResponseEntity<JsonResult> getStaffLoginReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			String staffNo = jsonNode.get("staffNo").asText();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			//System.out.println("Start Date - "+start +"End Date - "+ endDate);
			
			
			Integer report_channel = jsonNode.get("searchType").asInt();
			   //System.out.println("Report Channel "+ report_channel.toString());
			   List<Integer> access_channel = new ArrayList<>();
			    if(String.valueOf(report_channel).equals("1")) {
			    	access_channel.add(0);
			    	access_channel.add(1);
			    	
		        }else {
		        	access_channel.add(2);
		        	access_channel.add(3);
		        	access_channel.add(4);
		        }
			
			
		
			List<Log> report_log = reportService.getLoginByStaffNo(staffNo,start,endDate,access_channel);
			//System.out.println("Inst size: " + report_log.size());
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer i = 0; i < report_log.size(); i++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				staff_info.setCreatedAt(report_log.get(i).getCreatedAt());
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Inst", return_data, return_data.size(),session);
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/staff/details/{user_id}")
    public ResponseEntity<JsonResult> getStaffDetailReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			List<PageNameModel> page_name_session = (List<PageNameModel>) session.getAttribute("page_name");
			
			String staffNo = jsonNode.get("staffNo").asText();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
		
			List<Log> report_log_full = reportService.getViewByStaffNo(staffNo,start,endDate);
			//List<Log> report_view_page_log = reportService.getViewPageByStaffInfo(staffNo,fullname,startDate,endDate);
			
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer x = 0; x < report_log_full.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				
				Integer i = x;
				
				staff_info.setCreatedAt(report_log_full.get(i).getCreatedAt());
				//System.out.println("Can get create at ");
				staff_info.setPageNameEN(page_name_session.stream()
						 .filter((n) -> n.getId().equals(report_log_full.get(i).getTableId()))
						 .map((n) -> {return n.getPageNameEn() + report_log_full.get(i).getPkid();})
						 .collect(Collectors.toList())
						 .get(0));
				//System.out.println("can get page name en ");
				staff_info.setPageNameTc(page_name_session.stream()
					 .filter((n) -> n.getId().equals(report_log_full.get(i).getTableId()))
					 .map((n) -> {return n.getPageNameTc() + report_log_full.get(i).getPkid();})
					 .collect(Collectors.toList())
					 .get(0));
				//System.out.println("can get page name tc ");
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Inst", return_data, return_data.size(),session);
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/staff/resource/{user_id}")
    public ResponseEntity<JsonResult> getStaffResourceReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			List<PageNameModel> page_name_session = (List<PageNameModel>) session.getAttribute("page_name");
			
			String staffNo = jsonNode.get("staffNo").asText();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
		
			List<Log> report_log_full = reportService.getResourceByStaffNo(staffNo,start,endDate);
			//List<Log> report_view_page_log = reportService.getViewPageByStaffInfo(staffNo,fullname,startDate,endDate);
			
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer x = 0; x < report_log_full.size(); x++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				
				Integer i = x;
				
				staff_info.setCreatedAt(report_log_full.get(i).getCreatedAt());
				staff_info.setPageNameEN(page_name_session.stream()
						 .filter((n) -> n.getId().equals(report_log_full.get(i).getTableId()))
						 .map((n) -> {return n.getPageNameEn() + report_log_full.get(i).getPkid();})
						 .collect(Collectors.toList())
						 .get(0));
				staff_info.setPageNameTc(page_name_session.stream()
					 .filter((n) -> n.getId().equals(report_log_full.get(i).getTableId()))
					 .map((n) -> {return n.getPageNameTc() + report_log_full.get(i).getPkid();})
					 .collect(Collectors.toList())
					 .get(0));
				
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Staff", return_data, return_data.size(),session);
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/resource/{user_id}")
    public ResponseEntity<JsonResult> getResourceReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long categoryId =  jsonNode.get("categoryId") == null? 0L: jsonNode.get("categoryId").asLong();
			Integer is_subcategory = jsonNode.get("subcategory") == null? 0: jsonNode.get("subcategory").asInt();
			//String resourceId = jsonNode.get("resource_id").asText();
			List<Long> resourceId = new ArrayList<>();
			resourceId.add(0L);
			for(Integer a = 0; a<jsonNode.get("resourceId").size();a++ ) {
				resourceId.add(jsonNode.get("resourceId").get(a).asLong());
				
			}
			//System.out.println("Report Controller : line 2164 : resource id = "+resourceId);
			
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			//System.out.println("Start Date : "+start+" End Date : "+endDate);
		
			
			
			
			Long km = jsonNode.get("km") == null ? -1L : 1L;
			Long ks = jsonNode.get("ks") == null ? -1L : 2L;
			Long wg = jsonNode.get("wg") == null ? -1L : 3L;
			
			List<CatAllModel> all_category = catAllRepository.findAll();
			List<Long> subcategory_list = new ArrayList<Long>();
			if(is_subcategory == 0) {
				subcategory_list.add(categoryId);
			}else {
				List<Long> all_with_sub = getSubcategoryList(categoryId,categoryId,all_category);
				if(all_with_sub == null || all_with_sub.size() == 0) {
					all_with_sub.add(0L);
				}
				subcategory_list.addAll(all_with_sub);
			}
			
			List<HashMap<String, Object>> getResourceModel = reportService.getResourceForReport(km,ks,wg,subcategory_list,resourceId);
			List<Log> report_log_full = reportService.getResourceByDate(start,endDate);
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<User> exists_user_list_session = (List<User>) session.getAttribute("exist_user");
			List<ResourceReoportModel> return_data = new ArrayList<ResourceReoportModel>();
			
			
			//System.out.println("getResourceModel Size - " +getResourceModel.size() );
			for(Integer j = 0; j < getResourceModel.size(); j++) {
				List<ResourceAccessRuleModel> resourceAccessRuleModel = (List<ResourceAccessRuleModel>) getResourceModel.get(j).get("resourceList");
				Long target_category = (Long) getResourceModel.get(j).get("categoryId");
				String _path_string_ = getParentName(target_category,all_category);
				String _path_string_tc_ = getParentTcName(target_category,all_category);
				String path_string = _path_string_.substring(0,_path_string_.length() - 4);
				String path_string_tc = _path_string_tc_.substring(0,_path_string_tc_.length() - 4);
				//System.out.println("Report Controller : Line 2216:  category string ="+ path_string + " category TC = "+ path_string_tc);
				for(Integer x = 0; x < resourceAccessRuleModel.size(); x++) {
					Integer i = x;
					for(Integer q = 0 ; q< return_data.size() ;q++) {
						//System.out.println("Report Controller : line 2219 : Return data = "+ return_data.get(q).getResourceId());
					}
					//System.out.println("Report Controller : Line 2222: Resource Access Rule Model get Resource id = "+ resourceAccessRuleModel.get(i).getResource().getId());
					//For same resource, add new category path
					if(return_data.stream()
//							.map((n) -> {return n.getResource();})
							.map((n) -> {return n.getResourceId() ;})
							.collect(Collectors.toList())
							.contains(resourceAccessRuleModel.get(i).getResource().getId())) {
						//System.out.println("Report Controller : line 2229 : resource existing category id "+
//							return_data.stream()
//						.map((n)->{return n.getCategoryTc();}).collect(Collectors.toList()));
						
						return_data = return_data.stream()
								   .filter((n) -> n.getResourceId().equals(resourceAccessRuleModel.get(i).getResource().getId()))
								   .map((n) -> {
									   List<String> category_new = n.getCategory();
									   List<String> categoryTc_new = n.getCategoryTc();
									   category_new.add(path_string);
									   categoryTc_new.add(path_string_tc);
									   n.setCategory(category_new);
									   n.setCategoryTc(categoryTc_new);
									   return n;
									   })
								   .collect(Collectors.toList());
					}else {
//						//System.out.println("Report Controller : Line 2242: return data include = "+ return_data.stream()
//							.map((n) -> {return n.getResourceId() ;})
//							.collect(Collectors.toList()));
						
						
						
						ResourceReoportModel resource_info = new ResourceReoportModel();
						List<Long> user_id_in_session = user_list_session.stream().map((u) -> {return u.getId();}).collect(Collectors.toList());
						
						List<User> createdByList = user_list_session.stream().filter(
								(u) -> u.getId().equals(resourceAccessRuleModel.get(i).getResource().getCreatedBy())
							).map((u) -> {return u;}).collect(Collectors.toList());
						
						List<User> modifiedByList = user_list_session.stream().filter(
								(u) -> u.getId().equals(resourceAccessRuleModel.get(i).getResource().getModifiedBy())
							).map((u) -> {return u;}).collect(Collectors.toList());
						
						List<String> cate_path = new ArrayList<>();
						List<String> cateTc_path = new ArrayList<>();
						cate_path.add(path_string);
						cateTc_path.add(path_string_tc);
						resource_info.setResourceId(resourceAccessRuleModel.get(i).getResource().getId());
						resource_info.setCategory(cate_path);
						resource_info.setCategoryTc(cateTc_path);
						
						resource_info.setTitleEn(resourceAccessRuleModel.get(i).getResource().getTitleEn());
						resource_info.setTitleTc(resourceAccessRuleModel.get(i).getResource().getTitleTc());
						resource_info.setActivated(resourceAccessRuleModel.get(i).getResource().getActivated());
						resource_info.setCreatedAt(resourceAccessRuleModel.get(i).getResource().getCreatedAt());
						
						List<User> resource_total_user = reportService.getResourceTotalUserByDate(resourceAccessRuleModel.get(i).getResource().getId(), exists_user_list_session,resourceAccessRuleModel.get(i).getAccessRuleList());
						
						if(createdByList == null || createdByList.size() == 0 || !user_id_in_session.contains(createdByList.get(0).getId())) {
							resource_info.setCreatedBy(null, null, null, null);
						}else {
							User createdBy = createdByList.get(0);
//							System.out.println(">>>>>>>>>>>User Info>>>>>>>>>>>");
//							System.out.println(createdBy.getId());
							resource_info.setCreatedBy( createdBy.getFullname(),
									section_session.stream()
										.filter((n) -> n.getId().equals(createdBy.getSectionId()))
										.map(SectionModel::getSectionName)
										.collect(Collectors.toList())
										.get(0),
								    institution_session.stream()
								   		.filter((n) -> n.getId().equals(createdBy.getInstitutionId()))
								   		.map(InstitutionsModel::getInstName)
								   		.collect(Collectors.toList())
								   		.get(0), 
							   		rank_session.stream()
									    .filter((n) -> n.getId().equals(createdBy.getRankId()))
									    .map(RanksModel::getRankName)
									    .collect(Collectors.toList())
									    .get(0)
								   	);
						}
						
						resource_info.setModifiedAt(resourceAccessRuleModel.get(i).getResource().getModifiedAt());
						if(modifiedByList == null || modifiedByList.size() == 0 || !user_id_in_session.contains(modifiedByList.get(0).getId())) {
							resource_info.setModifiedBy(null,null,null,null);
						}else {
							User modifiedBy = modifiedByList.get(0);
							resource_info.setModifiedBy(modifiedBy.getFullname(), 
									section_session.stream()
										.filter((n) -> n.getId().equals(modifiedBy.getSectionId()))
										.map(SectionModel::getSectionName)
										.collect(Collectors.toList())
										.get(0), 
									institution_session.stream()
										.filter((n) -> n.getId().equals(modifiedBy.getInstitutionId()))
										.map(InstitutionsModel::getInstName)
										.collect(Collectors.toList())
										.get(0), 
									rank_session.stream()
									    .filter((n) -> n.getId().equals(modifiedBy.getRankId()))
									    .map(RanksModel::getRankName)
									    .collect(Collectors.toList())
									    .get(0)
									);
						}
						resource_info.setAvailableUsers(resource_total_user.size());
						resource_info.setHits(report_log_full.stream()
								 .filter((n) -> n.getLogtype().equals(2L) && n.getTableId().equals(2L) && n.getPkid().equals(resourceAccessRuleModel.get(i).getResource().getId()))
								 .collect(Collectors.toList()).size());
						resource_info.setShared(report_log_full.stream()
								 .filter((n) -> n.getLogtype().equals(17L) && n.getTableId().equals(2L) && n.getPkid().equals(resourceAccessRuleModel.get(i).getResource().getId()))
								 .collect(Collectors.toList()).size());
						resource_info.setDownload(report_log_full.stream()
								 .filter((n) -> n.getLogtype().equals(3L) && n.getTableId().equals(2L) && n.getPkid().equals(resourceAccessRuleModel.get(i).getResource().getId()))
								 .collect(Collectors.toList()).size());
						
						return_data.add(resource_info);
					}
				}
				//System.out.println("J is = "+ j);
			}
			
			 
			return JsonResult.listTotal("Report By Resource", return_data, return_data.size(),session);
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/resource/actions/{user_id}")
    public ResponseEntity<JsonResult> getResourceActionReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long resourceId = jsonNode.get("resourceId").asLong();
			Integer action = jsonNode.get("action") == null ? 1 : jsonNode.get("action").asInt();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
		
			List<Log> report_log_full = reportService.getResourceLogByDate(resourceId,start,endDate,action);
			
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer i = 0; i < report_log_full.size(); i++) {
				Integer x = i;
				ResourceReoportModel staff_info = new ResourceReoportModel();
				User staff = user_list_session.stream()
						   .filter((n) -> n.getStaffNo().equals(report_log_full.get(x).getStaffNo()))
						   .collect(Collectors.toList()).get(0);
				staff_info.setStaffNo(staff.getStaffNo());
				staff_info.setCreatedAt(report_log_full.get(i).getCreatedAt());
				staff_info.setFullname(staff.getFullname());
				staff_info.setInstitution(institution_session.stream()
									 .filter((n) -> n.getId().equals(staff.getInstitutionId()))
									 .map((n) -> {return n.getInstName();})
									 .collect(Collectors.toList())
									 .get(0));
				staff_info.setSection(section_session.stream()
						 .filter((n) -> n.getId().equals(staff.getSectionId()))
						 .map((n) -> {return n.getSectionName();})
						 .collect(Collectors.toList())
						 .get(0));
				staff_info.setRank(rank_session.stream()
						 .filter((n) -> n.getId().equals(staff.getRankId()))
						 .map((n) -> {return n.getRankName();})
						 .collect(Collectors.toList())
						 .get(0));
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Resource", return_data, return_data.size(),session);
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/resource/total_user/{user_id}")
    public ResponseEntity<JsonResult> getResourceTotalUserReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long resourceId = jsonNode.get("resourceId").asLong();
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<AccessRule> resource_access_rule = accessRuleRepository.getAccessRuleByResourceId(resourceId);
			List<User> resource_total_user = reportService.getResourceTotalUserByDate(resourceId, user_list_session,resource_access_rule);
			System.out.println("Report Controller, line 2615  : "+ resource_total_user.size());
			
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer i = 0; i < resource_total_user.size(); i++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				User staff = resource_total_user.get(i);
				staff_info.setStaffNo(staff.getStaffNo());
				staff_info.setFullname(staff.getFullname());
				staff_info.setInstitution(institution_session.stream()
									 .filter((n) -> n.getId().equals(staff.getInstitutionId()))
									 .map((n) -> {return n.getInstName();})
									 .collect(Collectors.toList())
									 .get(0));
				staff_info.setSection(section_session.stream()
						 .filter((n) -> n.getId().equals(staff.getSectionId()))
						 .map((n) -> {return n.getSectionName();})
						 .collect(Collectors.toList())
						 .get(0));
				staff_info.setRank(rank_session.stream()
						 .filter((n) -> n.getId().equals(staff.getRankId()))
						 .map((n) -> {return n.getRankName();})
						 .collect(Collectors.toList())
						 .get(0));
				return_data.add(staff_info);
			}
		
			return JsonResult.listTotal("Report By Resource", return_data, return_data.size(),session);
		}
    }
	
	@RequestMapping("/access/blog/{user_id}")
    public ResponseEntity<JsonResult> getBlogReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long categoryId = jsonNode.get("categoryId").asLong();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
		
			
			List<Log> report_blog_full = reportService.getBlogByCategory(categoryId,start,endDate);
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			List<Long> blog_id = report_blog_full.stream().map(Log::getPkid).distinct().collect(Collectors.toList());
			List<Blog> blog_list = reportService.getBlogByIds(blog_id);
			for(Integer i = 0; i < blog_list.size(); i++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer x = i;
				staff_info.setBlogId(blog_list.get(i).getId());
				staff_info.setTitle(blog_list.get(i).getPostTitle());
				staff_info.setOriginalCreator(blog_list.get(i).getOriginalCreator().getStaffNo(), blog_list.get(i).getOriginalCreator().getFullname());
				staff_info.setPublishAt(blog_list.get(i).getPublishAt());
				staff_info.setBlogCategory(categoryId.toString(), categoryId.toString());
				staff_info.setIsDeleted(blog_list.get(i).getIsDeleted());
				staff_info.setIsPublic(blog_list.get(i).getIs_public());
				staff_info.setHits(report_blog_full.stream()
								   .filter((n) -> n.getPkid().equals(blog_list.get(x).getId()) && n.getLogtype().equals(2L))
								   .collect(Collectors.toList()).size());
				staff_info.setLikes(report_blog_full.stream()
						   .filter((n) -> n.getPkid().equals(blog_list.get(x).getId()) && n.getLogtype().equals(13L))
						   .collect(Collectors.toList()).size());
				staff_info.setComments(report_blog_full.stream()
						   .filter((n) -> n.getPkid().equals(blog_list.get(x).getId()) && n.getLogtype().equals(14L))
						   .collect(Collectors.toList()).size());
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Blog", return_data, return_data.size(),session);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/access/blog/details/{user_id}")
    public ResponseEntity<JsonResult> getBlogDetailsReport(@PathVariable Long user_id, @RequestBody JsonNode jsonNode, HttpSession session) {
		
		HashMap<String, String> user_check = common.checkReportUser(user_id,session);
		if(user_check.get("msg") != "") {
			return JsonResult.errorMsg(user_check.get("msg").toString());
		}else{
			User user = (User) session.getAttribute("user_session");
			Integer channel = (Integer) session.getAttribute("channel");
			if(session.getAttribute("rank") == null) {
				
				common.checkRankSession(session);
			}
			logger.viewReport(user, 0L, "", "Success", channel);
			Long blogId = jsonNode.get("blogId").asLong();
			Integer report_type = jsonNode.get("action").asInt();
			String startDate1 = jsonNode.get("startDate").asText().substring(0, 10);
			String endDate1 = jsonNode.get("endDate").asText().substring(0,10);
			
			Date start = common.textToDateDate(startDate1);
			Date end = common.textToDateDate(endDate1);
			
			
			
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
			Date endDate=rightNow.getTime();
			
			
			List<RanksModel> rank_session = (List<RanksModel>) session.getAttribute("rank");
			List<SectionModel> section_session = (List<SectionModel>) session.getAttribute("section");
			List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
			List<User> user_list_session = (List<User>) session.getAttribute("user_list");
			List<Log> report_blog_full = reportService.getBlogDetailById(blogId,start,endDate,report_type);
			List<ResourceReoportModel> return_data = new ArrayList<>();
			
			for(Integer i = 0; i < report_blog_full.size(); i++) {
				ResourceReoportModel staff_info = new ResourceReoportModel();
				Integer x = i;
				User staff = user_list_session.stream()
							 .filter((n) -> n.getStaffNo().equals(report_blog_full.get(x).getStaffNo()))
							 .collect(Collectors.toList())
							 .get(0);
				staff_info.setStaffNo(staff.getStaffNo());
				staff_info.setCreatedAt(report_blog_full.get(i).getCreatedAt());
				staff_info.setFullname(staff.getFullname());
				staff_info.setInstitution(institution_session.stream()
									 .filter((n) -> n.getId().equals(staff.getInstitutionId()))
									 .map((n) -> {return n.getInstName();})
									 .collect(Collectors.toList())
									 .get(0));
				staff_info.setSection(section_session.stream()
						 .filter((n) -> n.getId().equals(staff.getSectionId()))
						 .map((n) -> {return n.getSectionName();})
						 .collect(Collectors.toList())
						 .get(0));
				staff_info.setRank(rank_session.stream()
						 .filter((n) -> n.getId().equals(staff.getRankId()))
						 .map((n) -> {return n.getRankName();})
						 .collect(Collectors.toList())
						 .get(0));
				return_data.add(staff_info);
			}
			
			return JsonResult.listTotal("Report By Blog", return_data, return_data.size(),session);
		}
	}
	
	private List<Long> getSubcategoryList(Long categoryId,Long parentId,List<CatAllModel> all_category) {
		List<CatAllModel> entity_list = all_category.stream()
										.filter( 
											(n) -> n.getId().equals(categoryId)
										).collect(Collectors.toList());
		////System.out.println("Node ID is: " + categoryId);
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
			System.out.println("Category ID is not existed.");
			back_data.add(0L);
			return back_data; 
		} 
	}		
	
	public String getParentName(Long parentId,List<CatAllModel> all_category){ 
		List<CatAllModel> entity_list = all_category.stream()
										.filter( 
											(n) -> n.getId().equals(parentId)
										).collect(Collectors.toList());
		//System.out.println("Node ID is: " + parentId);
		String back_string = "";
		if(entity_list.size() != 0){
			CatAllModel entity = entity_list.get(0);
			if(entity.getParentCatId() != 0) {
				String configName = entity.getNameEn() + " -> ";

				String returnConfigName = getParentName(Long.parseLong(String.valueOf(entity.getParentCatId())),all_category);
				back_string = returnConfigName+configName;
//				return returnConfigName+configName;
			//	System.out.println("Get Parent Name = "+ back_string);
				return back_string;
			}else {
				String configName = entity.getNameEn() + " -> ";
				return back_string+configName;
			}
		}else{
			return "";  
		}  
	}
	
	public String getParentTcName(Long parentId,List<CatAllModel> all_category){ 
		List<CatAllModel> entity_list = all_category.stream()
										.filter( 
											(n) -> n.getId().equals(parentId)
										).collect(Collectors.toList());
		//System.out.println("Node ID is: " + parentId);
		String back_string = "";
		if(entity_list.size() != 0){
			CatAllModel entity = entity_list.get(0);
			if(entity.getParentCatId() != 0) {
				String configName = entity.	getNameTc() + " -> ";

				String returnConfigName = getParentTcName(Long.parseLong(String.valueOf(entity.getParentCatId())),all_category);
				back_string = returnConfigName+configName;
//				return returnConfigName+configName;
				return back_string;
			}else {
				String configName = entity.getNameTc() + " -> ";
				return back_string+configName;
			}
		}else{
			return "";  
		}  
	}



	public void summarizeScore() {
		// TODO Auto-generated method stub
	
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
		System.out.println("Summarize Score Yesterday = "+ yesterday);
		Date queryDate1 = new Date();
		Calendar calendar1 = Calendar.getInstance(); //Calendar
		calendar1.setTime(new Date());//Calendar's Current Date
		calendar1.add(Calendar.DAY_OF_YEAR,1);  
		queryDate1 = calendar1.getTime();   
		SimpleDateFormat ymdFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date today;
		try {
			today = ymdFormat.parse(ymdFormat1.format(new Date(queryDate1.getTime())));
		} catch (Exception e) {
			today = null;
		}
		
		
		

//		List<ScoreLog> report_score = new ArrayList<>();
//		List<Object []> report_data = reportService.getScoreReport1(0L, yesterday, today);
		
		List<Long> userId = userRepository.findIdByIsDeleted(0);
		for(Integer i = 0; i<userId.size();i++) {
			Integer score =0;
			List<Object[]> detail  = scoreLogRepository.getScoreDetail1(userId.get(i), yesterday, today);
				for(Integer j =0 ; j <detail.size();j++) {
					Object[] data = (Object[]) detail.get(i);
					score += Integer.valueOf(data[2].toString());
				}
		 if(score> 30) {
			 //System.out.println("User Id = "+ userId.get(i)+" Score = "+ score);
			 Integer minus = 30-score;
			 ScoreLog scoreLog = new ScoreLog(0L,userId.get(i),minus);
				logger.saveScoreLog(scoreLog);
			  
		 }
			
		}
		
		
		
		
	}
	
}

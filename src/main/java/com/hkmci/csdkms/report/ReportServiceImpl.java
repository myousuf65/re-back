package com.hkmci.csdkms.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.entity.Blog;
import com.hkmci.csdkms.entity.FileResource;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ResourceAccessRule;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.ResourceAccessRuleModel;
import com.hkmci.csdkms.model.ResourceCategoryModel;

import com.hkmci.csdkms.repository.AccessRuleRepository;
import com.hkmci.csdkms.repository.BlogRepository;
import com.hkmci.csdkms.repository.LogRepository;
import com.hkmci.csdkms.repository.ResourceAccessRuleRepository;
import com.hkmci.csdkms.repository.ResourceCategoryRespository;
import com.hkmci.csdkms.repository.ResourceRepository;
import com.hkmci.csdkms.repository.ResourceSpecialGroupRepository;
import com.hkmci.csdkms.repository.ResourceSpecialUserRepository;
import com.hkmci.csdkms.repository.ScoreLogRepository;




@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private LogRepository logRepository;
	
	@Autowired
	private ScoreLogRepository scoreLogRepository;
	
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private ResourceRepository resourceRepository;
	
	@Autowired
	private AccessRuleRepository accessRuleRepository;
	
	@Autowired
	private ResourceCategoryRespository resourceCategoryRespository;
	
	@Autowired
	private ResourceAccessRuleRepository resourceAccessRuleRepository;
	
	@Autowired
	private ResourceSpecialUserRepository resourceSpecialUserRepository;
	
	@Autowired
	private ResourceSpecialGroupRepository resourceSpecialGroupRepository;
	
	
	@Override
	public List<Log> findAll() {
		return logRepository.findAll();
	}

	@Override
	public Log newLog(Log log){
		return logRepository.saveAndFlush(log);
	}
	
	@Override
	public Optional<Log> findById(Long id){
		return logRepository.findById(id);
	}

	@Override
	public List<Log> getByRank(Long rankId, List<Long> instId, Date startDate, Date endDate) {
		// TODO Report By Rank
		List<Log> return_data = logRepository.getByRank(rankId,startDate,endDate);
		//System.out.println("Log size: " + return_data.size());
		if(instId.get(0).equals(0L)){
			return return_data;
		}else {
			
			List<Log> return_total = new ArrayList<>();
				for(Integer i =0 ; i<instId.size() ; i++) {
					List<Log> return_list = new ArrayList<>();
					Long result = instId.get(i);
					System.out.println("result - "+result);
					return_list=return_data.stream().filter(
						(log) -> log.getUinstId().equals(result)
					).collect(Collectors.toList());
					for(Integer j =0 ;j<return_list.size();j++) {
						return_total.add(return_list.get(j));
					}
				}
				return return_total;
		}
	}
	
	@Override
	public List<Log> getViewPageByRank(Long rankId, List<Long> instIdList, Date startDate, Date endDate, Long instId) {
		// TODO Report By Rank
		System.out.println("Inst id = "+ instId);
		List<Log> return_data = logRepository.getViewPageByRank(rankId,startDate,endDate,instIdList,Integer.valueOf(instId.toString()));
		//System.out.println("Log size: " + return_data.size());
	    
		Long inst =0L;
		Long subInst =0L;
		if(instIdList.size()==2) {
			inst = instIdList.get(0);
			subInst=instIdList.get(1);
		}
			
			Long first = inst;
			Long second = subInst;
			
			
		if(first==0L){
			return return_data;
		}else {
			return return_data.stream().filter(
						(log) -> log.getUinstId().equals(first) ||log.getUinstId().equals(second)
					).collect(Collectors.toList());
		}
	}
	
	@Override
	public List<Log> getRankDetailByUser(String staffNo, Date startDate, Date endDate) {
		// TODO Report By Rank
		List<Log> return_data = logRepository.getRankDetailByUser(staffNo,startDate,endDate);
		//System.out.println("Log size: " + return_data.size());
		return return_data;
	}
	
	@Override
	public List<Log> getRankLoginByUser(String staffNo, Date startDate, Date endDate, Integer channel) {
		// TODO Report By Rank
		List<Log> return_data = logRepository.getRankLoginByUser(staffNo,startDate,endDate, channel);
		//System.out.println("Log size: " + return_data.size());
		return return_data;
	}

	

	@Override
	public List<Log> getViewPageByInst(List<Long> instId, Date startDate, Date endDate) {
		Integer all =1;
		if(instId.get(0) ==0) {
			all=0;
		}
		
		
		List<Log> return_data = logRepository.getViewPageByInst(instId,startDate,endDate,all);
		return return_data;
		
	}

	@Override
	public List<Log> getInstDetail(List<Long> target_instId, Date startDate, Date endDate) {
		Integer all =1;
		if(target_instId.get(0) == 0) {
			all =0;
		}
		
		List<Log> return_data = logRepository.getInstDetail(target_instId,startDate,endDate,all);
		return return_data;
	}

	@Override
	public List<Log> getInstLoginByUser(List<Long> target_instId, Date startDate, Date endDate, List<Integer> report_channel) {
		
		Integer All = target_instId.get(0).intValue();
		System.out.println("Is All ?" + All);
		List<Log> return_data = logRepository.getInstLoginByUser(target_instId,startDate,endDate, report_channel, "Success",All);
		//System.out.println("Log size: " + return_data.size());
		return return_data;
	}

	@Override
	public List<Log> getByStaffInfo(String staffNo, String fullname, Date startDate, Date endDate) {
		List<Log> return_data = new ArrayList<>();
		if(staffNo.equals("") || staffNo == null) {
			return_data = logRepository.getByFullname(fullname,startDate,endDate);
		}else {
			return_data = logRepository.getByStaffNo(staffNo,startDate,endDate);
		}
		
		return return_data;
	}

	
	
	@Override
	public List<Log> getLoginByStaffNo(String staffNo, Date startDate, Date endDate, List<Integer>report_channel) {
		
		String Success = "Success";
		List<Log> return_data = logRepository.getLoginByStaffNo(staffNo,startDate,endDate, report_channel,Success);
		
		return return_data;
	}

	@Override
	public List<Log> getViewByStaffNo(String staffNo, Date startDate, Date endDate) {
		List<Log> return_data = logRepository.getViewByStaffNo(staffNo,startDate,endDate);
		return return_data;
	}

	@Override
	public List<Log> getResourceByStaffNo(String staffNo, Date startDate, Date endDate) {
		List<Log> return_data = logRepository.getResourceByStaffNo(staffNo,startDate,endDate);
		return return_data;
	}

	@Override
	public List<HashMap<String,Object>> getResourceForReport(Long km, Long ks, Long wg, List<Long> subcategory_list,List<Long> resource_list) {
		System.out.println("Category List: " + subcategory_list +" Resource List : "+ resource_list);
		List<Long> resource_for_area = resourceRepository.getAreaForReport(km,ks,wg).stream().map((n) -> {return n.getId();}).collect(Collectors.toList());
		//List<FileResource> resource_for_category = resourceRepository.getCategoryForReport(subcategory_list);
		
		System.out.println("resource_for_area (Resource Report) : " + resource_for_area.size());
		return getResourceByCategory(subcategory_list,resource_for_area,resource_list);
	}

	@Override
	public List<Log> getResourceByDate(Date startDate, Date endDate) {
		List<Log> return_data = logRepository.getResourceByDate(startDate,endDate);
		return return_data;
	}
	
	private List<HashMap<String,Object>> getResourceByCategory(List<Long> subcategory_list, List<Long> resource_for_area,List<Long> resource_list) {
		// TODO Auto-generated method stub
		List<HashMap<String,Object>> return_data = new ArrayList<>();
		List<FileResource> target_resource = resourceRepository.getCategoryForReport(subcategory_list,resource_list)
											 .stream().filter((n) -> resource_for_area.contains(n.getId()))
											 .collect(Collectors.toList());
		List<Long> target_resource_id = resourceRepository.getCategoryForReport(subcategory_list,resource_list)
				 .stream().filter((n) -> resource_for_area.contains(n.getId()))
				 .map(FileResource::getId)
				 .collect(Collectors.toList());
		
		
		if (target_resource_id.size() == 0) {
			System.out.println("Can not resource under this cat or find by resource id ");
			
			target_resource_id=resource_list;
		}
		
		System.out.println("Report Services Imp : Line 243 : target_resource_id = "+target_resource_id);
		
		List<ResourceAccessRule> all_resource_access_rule = resourceAccessRuleRepository.getAccessRuleByResourceIds(target_resource_id);
		List<Long> access_rule_ids =  all_resource_access_rule.stream().map(ResourceAccessRule::getAccessRule).distinct().collect(Collectors.toList());
		
		System.out.println("Report Services Imp : Line 260 : access_rule_ids " +access_rule_ids);
		List<AccessRule> access_rules = accessRuleRepository.getByIds(access_rule_ids);
		List<ResourceCategoryModel> target_cat_resource = new ArrayList<>();

		System.out.println("Report Services Imp : Line 249 : subcategory_list size = "+subcategory_list.size() );
		
		if (subcategory_list.size() >1) {
			target_cat_resource = resourceCategoryRespository.findByCategoryList(subcategory_list);
		} else {
			target_cat_resource =resourceCategoryRespository.findByResourceList(target_resource_id);
			System.out.println("Report Services Imp : Line 252 : target_cat_resource = "+target_cat_resource.size());
			subcategory_list.remove(0L);
			
			for(Integer j =0 ; j<target_cat_resource.size();j++ ) {
				subcategory_list.add(target_cat_resource.get(j).getCategoryId());
			}
		}
		
		System.out.println("Report Services Imp : Line 256: subcategory_list = " + subcategory_list);
		List<Long> subcategory_list_remove = subcategory_list.stream().distinct().collect(Collectors.toList());
		System.out.println("Report Services Imp : Line 283: subcategory_list after remove "+ subcategory_list_remove);
		for(Integer i = 0; i < subcategory_list_remove.size(); i++) {
			Integer x = i;
			HashMap<String,Object> new_data = new HashMap<>();
			List<Long> temp_resource_id_org = target_cat_resource.stream()
										  .filter((n) -> n.getCategoryId().equals(subcategory_list_remove.get(x)))
										  .map((n)->{return n.getResourceId();})
										  .collect(Collectors.toList());
			System.out.println("Report Services Imp : Line 289: temp resource id ORG = "+ temp_resource_id_org);
			
			List<Long>temp_resource_id = temp_resource_id_org.stream()
				     .distinct()
				     .collect(Collectors.toList());
			System.out.println("Report Services Imp : Line 294 : temp resource id = "+ temp_resource_id);
			
			List<ResourceAccessRuleModel> temp_resource_data = target_resource.stream()
													.filter((n) -> temp_resource_id.contains(n.getId()))
													.map(
															(r) -> {
																ResourceAccessRuleModel temp = new ResourceAccessRuleModel();
																List<Long> temp_access_rule_ids = all_resource_access_rule.stream()
																								  .filter((n)->n.getResource().equals(r.getId()))
																								  .map(ResourceAccessRule::getAccessRule)
																								  .collect(Collectors.toList());
																List<AccessRule> temp_access_rule = access_rules.stream()
																									.filter((n) -> temp_access_rule_ids.contains(n.getId()))
																									.collect(Collectors.toList());
																temp.setResource(r);
																temp.setAccessRuleList(temp_access_rule);
																return temp;
															}
													)
													.collect(Collectors.toList());
			
			
		     System.out.println("Report Services Imp : line 300 :"+ subcategory_list_remove.get(i) );
			new_data.put("categoryId",subcategory_list_remove.get(i));
			new_data.put("resourceList", temp_resource_data);
			return_data.add(new_data);
			
		}
		
		return return_data;
	}

	@Override
	public List<Log> getResourceLogByDate(Long resourcceId, Date startDate, Date endDate, Integer action) {
		Long logtype = 2L;
		if(action.equals(1)) {
			logtype = 2L;
		}
		if(action.equals(2)) {
			logtype = 3L;
		}
		if(action.equals(3)) {
			logtype = 17L;
		}
		List<Log> return_data = logRepository.getResourceLogByDate(resourcceId,startDate,endDate, logtype);
		return return_data;
	}

	@Override
	public List<User> getResourceTotalUserByDate(Long resourceId, List<User> user_session,List<AccessRule> resource_access_rule) {
//		List<AccessRule> resource_access_rule = accessRuleRepository.getAccessRuleByResourceId(resourceId);
//		List<String> staff_no = resourceSpecialUserRepository.getUserIdByResourceIds(resourceId);
//		List<String> staff_no2 = resourceSpecialGroupRepository.getUserIdbyResourceId(resourceId);
//		
		List<User> return_data = new ArrayList<>();
		for(Integer i = 0; i < resource_access_rule.size(); i++) {
			AccessRule car = resource_access_rule.get(i);
			List<User> user_access = user_session.stream()
									 .filter(
										 (u) -> {
				    						List<String> section_list = Arrays.asList(car.getSectionId().split(","));
				    						List<String> inst_list = Arrays.asList(car.getInstId().split(","));
				    						List<String> rank_list = Arrays.asList(car.getRankId().split(","));
				    						//System.out.println("rank_list is: " + rank_list);
				    						Boolean result_section = section_list.contains(u.getSectionId().toString()) || section_list.contains("0");
				    						Boolean result_rank = rank_list.contains(u.getRankId().toString()) || rank_list.contains("0");
				    						Boolean result_inst = inst_list.contains(u.getInstitutionId().toString()) || inst_list.contains("0");
				    						return  result_section && result_rank && result_inst;
					    				 }
									 )
									 .collect(Collectors.toList());
			return_data.addAll(user_access);
		}
//		for (Integer i =0 ; i< staff_no.size() ; i++) {
//			String staff = staff_no.get(i);
//			List<User> user = user_session.stream()
//							 .filter( (n)-> n.getStaffNo().equals(staff))
//							 .collect(Collectors.toList());
//			return_data.addAll(user);
//		}
		
		
//		for (Integer i =0 ; i< staff_no2.size() ; i++) {
//			String staff = staff_no2.get(i);
//			List<User> user = user_session.stream()
//							 .filter( (n)-> n.getStaffNo().equals(staff))
//							 .collect(Collectors.toList());
//			return_data.addAll(user);
//		}
		return return_data.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<Log> getBlogByCategory(Long categoryId, Date startDate, Date endDate) {
		List<Log> return_data = logRepository.getBlogByCategory(categoryId.toString(),startDate,endDate);
		return return_data;
	}

	@Override
	public List<Blog> getBlogByIds(List<Long> blog_id) {
		List<Blog> return_data = blogRepository.getBlogByIds(blog_id);
		return return_data;
	}

	@Override
	public List<Log> getBlogDetailById(Long blogId, Date startDate, Date endDate, Integer report_type) {
		Long logtype = 2L;
		if(report_type.equals(1)) {
			logtype = 2L;
		}
		if(report_type.equals(2)) {
			logtype = 13L;
		}
		if(report_type.equals(3)) {
			logtype = 14L;
		}
		List<Log> return_data = logRepository.getBlogDetailById(blogId,startDate,endDate,logtype);
		return return_data;
	}

	@Override
	public List<ScoreLog> getScoreReport(Long instId,Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		System.out.println("Inst Id = "+ instId + " Start Date = "+ startDate + " End Date = "+ endDate);
	
		List<ScoreLog> return_data = scoreLogRepository.getScoreReport(instId, startDate, endDate);
		return return_data;
	}
	
	
	@Override
	public List<Object[]> getScoreReport1(List<Long> instId, Date startDate, Date endDate){
		
		System.out.println("Start Date (Score Report 1) = "+ startDate + " EndDate = " + endDate);
		
		 List<Object []> return_data = new ArrayList<>();
		 for(Integer i =0 ; i <instId.size() ;i++) {
			 return_data.addAll( scoreLogRepository.getScoreReport1(instId.get(i), startDate, endDate));
		
		 }
		 
		return return_data;
	}
	
	@Override
	public ScoreLog getScoreDetail(Long userId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		ScoreLog return_data = new ScoreLog();
		
		List<Object[]> query_data =scoreLogRepository.getScoreDetail1(userId, startDate, endDate);
		Object[] data =(Object []) query_data.get(0);
		System.out.println("Staff = "+ data[8].toString());
		return_data.setStaffNo(data[8].toString());
		return_data.setUinstId(Long.valueOf(data[5].toString()));
		return_data.setUrankId(Long.valueOf(data[7].toString()));
		return_data.setUsectionId(Long.valueOf(data[6].toString()));
		return_data.setUserId(Long.valueOf(data[4].toString()));
		
		return return_data;
	}
	
	
	@Override
	public ScoreLog getScoreDetail(String staff, Date startDate, Date endDate) {
		ScoreLog return_data = new ScoreLog();
		List<Object[]> query_data = scoreLogRepository.getScoreDetail2 (staff, startDate, endDate);
		
		//return_data.setCreatedAt((Date) data[1]); 
		//return_data.setId(Long.valueOf(data[0].toString()));
		//return_data.setLogId(logId);
	
		//return_data.setScore(Integer.valueOf(data[2].toString()));
		
		Object[] data =(Object []) query_data.get(0);
		System.out.println("Staff = "+ data[8].toString());
		return_data.setStaffNo(data[8].toString());
		return_data.setUinstId(Long.valueOf(data[5].toString()));
		return_data.setUrankId(Long.valueOf(data[7].toString()));
		return_data.setUsectionId(Long.valueOf(data[6].toString()));
		return_data.setUserId(Long.valueOf(data[4].toString()));
		
		return return_data;
	}
	
	@Override
	public List<Object []> getUserMenuScoreByPeriod (Long userId, Date startDate, Date endDate) {
		System.out.println("----Get User Menu Score------------" +userId  + "  - " + startDate +" - "+ endDate );
		
		
	//	List<ScoreLog> return_data = scoreLogRepository.getUserMenuScore(userId, startDate,endDate);
		List<ScoreLog> return_data = new ArrayList<>();
		List<Object []> return_list = scoreLogRepository.getUserMenuScore2ByPeriod(userId, startDate, endDate);
		System.out.println("Result Return - "+ return_list.size());
//		for(Integer i = 0 ; i<return_list.size() ; i++) {
//			ScoreLog temp = new ScoreLog();
//			Object[] data = (Object []) return_list.get(i);
//			temp.setScore(Integer.valueOf(data[2].toString()));
//			temp.setCreatedAt((Date) data[0]);
//			return_data.add(temp);
//		}

		return return_list;
	}
	

	@Override
	public List<Object []> getUserMenuScore(Long userId, Date startDate, Date endDate) {
		System.out.println("----Get User Menu Score------------" +userId  + "  - " + startDate +" - "+ endDate );
		
		
	//	List<ScoreLog> return_data = scoreLogRepository.getUserMenuScore(userId, startDate,endDate);
		List<ScoreLog> return_data = new ArrayList<>();
		List<Object []> return_list = scoreLogRepository.getUserMenuScore2(userId, startDate, endDate);
		System.out.println("Result Return - "+ return_list.size());
//		for(Integer i = 0 ; i<return_list.size() ; i++) {
//			ScoreLog temp = new ScoreLog();
//			Object[] data = (Object []) return_list.get(i);
//			temp.setScore(Integer.valueOf(data[2].toString()));
//			temp.setCreatedAt((Date) data[0]);
//			return_data.add(temp);
//		}

		return return_list;
	}
	
	
	@Override
	public Integer getRankLoginStaff(Long instId,Long rankId,Date startDate, Date endDate) {
		return logRepository.getRankLoginStaff(instId,rankId,startDate,endDate).size();
		
	}
	
	
	@Override
	public List<Log> getByInst(List<Long> instId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		Integer all =1;
		if(instId.get(0)==0) {
			all=0;
		}
		System.out.println("Inst id = "+instId);
		List<Log> return_data = logRepository.getByInst(instId,startDate,endDate,all);
		return return_data;
	}



	@Override
	public List<ScoreLog> getScoreDetailList(Collection<Integer> values, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
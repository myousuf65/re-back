package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.AccessRule;
import com.hkmci.csdkms.repository.AccessRuleRepository;
import com.hkmci.csdkms.repository.UserAccessRuleRepository;;

@Service
public class AccessRuleServiceImpl implements AccessRuleService{
	@Autowired
    private AccessRuleRepository accessRuleRepository;
	
	
	@Autowired
	private UserAccessRuleRepository userAccessRuleRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
    
    /**
     * Fetch ALL
     * @return
     */
    public List<AccessRule> findAll(){
        List<AccessRule> CollectionAccessRuleList = new ArrayList<AccessRule>();
        CollectionAccessRuleList = accessRuleRepository.findByIsDeleted(0);
        return  CollectionAccessRuleList;
    }
    

    public Optional<AccessRule> getById(Long Id){
    	Optional<AccessRule> CollectionAccessRule = accessRuleRepository.getById(Id);
        return  CollectionAccessRule;
    }
    
    public AccessRule deleteById(Long Id){
    	
    	AccessRule new_CollectionAccessRule = new AccessRule();
    	Optional<AccessRule> CollectionAccessRule = accessRuleRepository.getById(Id);
    	if(CollectionAccessRule.isPresent()) {
    		return  null;
    	}else {
    		new_CollectionAccessRule = CollectionAccessRule.get();
        	new_CollectionAccessRule.setIsDeleted(1);
        	new_CollectionAccessRule.setDeletedAt(new Date());
        	new_CollectionAccessRule.setDeletedBy((long) 1);
            return  accessRuleRepository.saveAndFlush(new_CollectionAccessRule);
    	}
    }
    
    public AccessRule update(AccessRule CollectionAccessRule){
//    	CollectionAccessRule old_data = accessRuleRepository.getById(CollectionAccessRule.getId()).get();
//    	if(old_data.getCreatedAt() == CollectionAccessRule.getCreatedAt() && old_data.getCreatedAt() != null) {
//    		System.out.println("in the delete  service Imp");
    		return accessRuleRepository.saveAndFlush(CollectionAccessRule);
//    	}else {
    	//    		return null;
//    	}
//    	
    }
    
    public List<Long> getIdByUser(Long sectionId, Long instId, Long rankId){
    	Integer inst_Id = Integer.parseInt(instId.toString());
    	List<AccessRule> CollectionAccessRuleId = accessRuleRepository.getIdByUser(sectionId, inst_Id, rankId);
//    	System.out.println("CollectionAccessRuleId size:" + CollectionAccessRuleId.stream().map((car) -> {
//    			return car.getId();
//    	}).collect(Collectors.toList()));
    	if(CollectionAccessRuleId.size() == 0) {
    		return null;
    	}else {
    		List<Long> return_data = CollectionAccessRuleId.stream().filter(
    					(car) -> {
    						List<String> section_list = Arrays.asList(car.getSectionId().split(","));
    						List<String> inst_list = Arrays.asList(car.getInstId().split(","));
    						List<String> rank_list = Arrays.asList(car.getRankId().split(","));
    						//System.out.println("rank_list is: " + rank_list);
    						Boolean result_section = section_list.stream().map((n) -> {return Long.parseLong(n);}).collect(Collectors.toList()).contains(sectionId) || section_list.contains("0");
    						Boolean result_rank = rank_list.stream().map((n) -> {return Long.parseLong(n);}).collect(Collectors.toList()).contains(rankId) || rank_list.contains("0");
    						Boolean result_inst = inst_list.stream().map((n) -> {return Long.parseLong(n);}).collect(Collectors.toList()).contains(instId) || inst_list.contains("0");
    						return  result_section && result_rank && result_inst;
    					}
    				).map(
    				car -> {
    					//accessRule.
    					return car.getId();
    				}).collect(Collectors.toList());
    		return return_data;
    	}
    	
    }
    
    public AccessRule save(AccessRule CollectionAccessRule){
    	
    	
    	
    	System.out.println("Access Rule Service Impl, line 109 = Id "+ CollectionAccessRule.getId() +" Inst Id = "+CollectionAccessRule.getInstId());
        return accessRuleRepository.saveAndFlush(CollectionAccessRule); 
    }
    
    public List<AccessRule> test(Integer id){
    	
    	String sql = "select new CollectionAccessRule(id,description) from CollectionAccessRule ar where ar.id = ?1 and ar.isDeleted = 0";
    	TypedQuery<AccessRule> query = entityManager.createQuery(sql, AccessRule.class);
    	query.setParameter(1,id);
    	List<AccessRule> result = query.getResultList();
    	
    	return result;
    }
    


	@Override
	public List<AccessRule> test(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
//	public List<CollectionAccessRule> Search(Long Id, Date startdate, Date enddate) {
	public List<AccessRule> Search(Long Id,Date startDate, Date endDate, Long km,Long ks,Long wg, Long page,List<Long> user_access_rule, Integer is_admin, String description) {
		Integer start_num = (Integer.parseInt(page.toString()) - 1) * 50;
		Integer end_num = 50;
//		System.out.println("accessRuleId For Query DB" + user_access_rule);
		List<AccessRule> result = accessRuleRepository.searchFunction(Id,startDate,endDate, km, ks,wg,start_num,end_num,user_access_rule,is_admin, description);
		result.stream().map(
					(ar) -> {
						List<Integer> instIdList = Arrays.asList(ar.getInstId().split(",")).stream().map((n) -> {
							return  Integer.parseInt(n.toString());
						}).collect(Collectors.toList());
						List<Integer> section_list = Arrays.asList(ar.getSectionId().split(",")).stream().map((n) -> {
							return  Integer.parseInt(n.toString());
						}).collect(Collectors.toList());
						List<Integer> rank_list = Arrays.asList(ar.getRankId().split(",")).stream().map((n) -> {
							return  Integer.parseInt(n.toString());
						}).collect(Collectors.toList());
						ar.setInstIdList(instIdList);
						ar.setSectionIdList(section_list);
						ar.setRankIdList(rank_list);
						return ar;
					}
				).collect(Collectors.toList());
		
		return result;
	}


	@Override
	public List<Object[]> customeized_test(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deleteAR(List<Long> deleted_ids, Long deleted_by) {
		accessRuleRepository.deleteAR(deleted_ids,deleted_by, new Date());
		return;
		
	}


	@Override
	public List<AccessRule> getAllWithRsource(List<Long> deleted_ids) {
		List<AccessRule> return_data = accessRuleRepository.getAllWithRsource(deleted_ids);
		return return_data;
	}


	@Override
	public Integer getTotal(Long id, Date startDate, Date endDate, Long km, Long ks, Long wg, Long page,
			List<Long> accessRuleId, Integer is_admin, String description) {
		Integer total = accessRuleRepository.getTotal(id,startDate,endDate, km, ks,wg,accessRuleId,is_admin,description);
		return total;
	}



	@Override
	public List<AccessRule> getIdByUserId(Long section, Long institution, Long rank, Long user_id) {
		
		Integer inst_Id = Integer.parseInt(institution.toString());
//    	List<AccessRule> accessRuleId = accessRuleRepository.getIdByUserId(section, inst_Id, rank,user_id);
		List<AccessRule> accessRuleId =null;
    	List<AccessRule> specialAccessRule = accessRuleRepository.getIdBySpecial(user_id).stream().distinct().collect(Collectors.toList());
//    	System.out.println("CollectionAccessRuleId size:" + accessRuleId.stream().distinct().map((car) -> {
//    			return car.getId();
//    	}).collect(Collectors.toList()));
    	List<AccessRule> return_data = new ArrayList<>();
//    	if(accessRuleId.size() == 0) {
//    		if(specialAccessRule != null && specialAccessRule.size() != 0) {
//    			return specialAccessRule;
//    		}else {
//    			return null;
//    		}
//    	}else {
//    		List<AccessRule> return_data = accessRuleId.stream().filter(
//    					(car) -> {
//    						List<String> section_list = Arrays.asList(car.getSectionId().split(","));
//    						//System.out.println("section_list is: " + section_list);
//    						List<String> rank_list = Arrays.asList(car.getRankId().split(","));
//    						//System.out.println("rank_list is: " + rank_list);
//    						Boolean result_section = section_list.contains(section.toString()) || section_list.contains("0");
//    						Boolean result_rank = rank_list.contains(rank.toString()) || rank_list.contains("0");
//    						return  result_section && result_rank;
//    					}
//    				).distinct().map(
//    				car -> {
//    					//accessRule.
//    					return car;
//    				}).collect(Collectors.toList());
    		if(specialAccessRule != null && specialAccessRule.size() != 0) {
    			return_data.addAll(specialAccessRule);
    			return return_data.stream().distinct().collect(Collectors.toList());
    		}else {
    			return return_data;
    		}
    	}


	@Override
	public List<Long> specialAccessRule(Long userId) {
		// TODO Auto-generated method stub
		return userAccessRuleRepository.getByUserId(userId);
	}
	}


	


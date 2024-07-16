package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.entity.SpecialCollection;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.PostReturnModel;
import com.hkmci.csdkms.repository.SpecialCollectionRepository;

@Service
public class SpecialCollectionServiceImpl implements SpecialCollectionService{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Resource
    private Common common;
	
	@Autowired
	@Resource
	private SpecialCollectionRepository specialCollectionRepository;
	
	@Override
	public List<PostReturnModel> searchSpecialCollectionByCategoryId(Integer page,List<Long> accessRuleId, Long accessChannel) {
		List<String> channel = new ArrayList<>();
		if(String.valueOf(accessChannel).equals("1")) {
	      	channel.add("0");
	      	channel.add("1");
	      	channel.add("2");
	    }else {
	    	channel.add("2");
	      	channel.add("4");
	    }  
      	
		List<SpecialCollection> query_data = searchSpecialCollectionByCategoryId(accessRuleId,channel);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
		for (int i = 0; i < query_data.size(); i++) {
			PostReturnModel tmp = new PostReturnModel();
			tmp.setSpecialCollection(query_data.get(i));
			return_data.add(tmp);
		}
		return return_data;
	}
	
	public List<SpecialCollection> searchSpecialCollectionByCategoryId(List<Long> accessRuleId, List<String> channels) {
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select b.*, u.fullname"
    			+ " from special_collection_post b "
				+ " Left Join user u ON u.id=b.original_creator"
    			+ " where b.is_deleted = 0 and b.is_public = 1 and access_channel IN (:channels) and ("+accessRuleIdString
    			+ ") order by b.publish_at DESC";
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("channels",channels);
		for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		List<Object[]> result = query.getResultList(); 
		System.out.println("SpecialCollectionSqlList:"+sql);
		List<SpecialCollection> specialCollections = new ArrayList<>();
		for (Object[] row : result) {
			User user = new User();
			user.setFullname((String) row[17]);
		    SpecialCollection specialCollection = new SpecialCollection();
		    specialCollection.setId(((Integer) row[0]).longValue());
		    specialCollection.setPostTitle((String) row[1]);
		    specialCollection.setPostTitleZh((String) row[2]);
		    specialCollection.setCreatedAt((Date) row[3]);
		    specialCollection.setCreatedBy(((Integer) row[4]).longValue());
		    specialCollection.setPublishAt((Date) row[10]);
		    specialCollection.setIs_public(Byte.valueOf((byte) row[11]).longValue());
		    specialCollection.setOriginalCreator(user);
		    specialCollection.setShowAsAlias(Byte.valueOf((byte) row[13]).intValue());
		    specialCollection.setAlias((String) row[14]);
		    specialCollection.setLink((String) row[15]);
		    specialCollection.setAccessRuleId((String) row[16]);
		    specialCollection.setAccessChannel((String) row[17]);
		    specialCollections.add(specialCollection);
		}
    	
    	return specialCollections;
	}

	@Override
	public Integer getTotalSearchSpecialCollectionByCategoryId(List<Long> accessRuleId, Long accessChannel) {
		List<String> channel = new ArrayList<>();
//      System.out.println("Access Channel = "+ access_channel+ "Access Rule Id =  "+ accessRuleId);
	    if(String.valueOf(accessChannel).equals("1")) {
	      	channel.add("0");
	      	channel.add("1");
	      	channel.add("2");
	    }else {
	    	channel.add("2");
	      	channel.add("4");
	    } 
		
		Integer query_data = getSpecialCollectionTotal(accessRuleId,channel);
		return query_data;
	}
	
	@Override
	public List<PostReturnModel> getMyPosts(Long userId) {		
		List<SpecialCollection> query_data = getMyPostsSQL(userId);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
		for (int i = 0; i < query_data.size(); i++) {
			PostReturnModel tmp = new PostReturnModel();
			tmp.setSpecialCollection(query_data.get(i));
			return_data.add(tmp);
		}
		return return_data;
	}
	
	public List<SpecialCollection> getMyPostsSQL(Long userId) {
		String sql = "select b.*, u.fullname"
    			+ " from special_collection_post b "
				+ " Left Join user u ON u.id=b.original_creator"
    			+ " where (b.original_creator = "+userId+" or created_by = "+userId+") and b.is_deleted = 0 "
    			+ " order by b.publish_at DESC";
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		List<Object[]> result = query.getResultList(); 
		System.out.println("getMyPostsList:"+sql);
		List<SpecialCollection> specialCollections = new ArrayList<>();
		for (Object[] row : result) {
			User user = new User();
			user.setFullname((String) row[18]);
			user.setId(userId);
		    SpecialCollection specialCollection = new SpecialCollection();
		    specialCollection.setId(((Integer) row[0]).longValue());
		    specialCollection.setPostTitle((String) row[1]);
		    specialCollection.setPostTitleZh((String) row[2]);
		    specialCollection.setCreatedAt((Date) row[3]);
		    specialCollection.setCreatedBy(((Integer) row[4]).longValue());
		    specialCollection.setPublishAt((Date) row[10]);
		    specialCollection.setIs_public(Byte.valueOf((byte) row[11]).longValue());
		    specialCollection.setOriginalCreator(user);
		    specialCollection.setShowAsAlias(Byte.valueOf((byte) row[13]).intValue());
		    specialCollection.setAlias((String) row[14]);
		    specialCollection.setLink((String) row[15]);
		    specialCollection.setAccessRuleId((String) row[16]);
		    specialCollection.setAccessChannel((String) row[17]);
		    specialCollections.add(specialCollection);
		}
    	
    	return specialCollections;
	}
	
	
	public Integer getSpecialCollectionTotal(List<Long> accessRuleId,List<String> channels){
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select b.id as total"
    			+ " from special_collection_post b"
    			+ " where b.is_deleted = 0 and b.is_public = 1  and access_channel IN (:channels) and ("+accessRuleIdString+")";
    	@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("channels",channels);
		for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		List<Object[]> result = query.getResultList(); 
    	
    	return result.size();
    }
	
	@Override
	public List<PostReturnModel> searchSpecialCollectionByLatest(List<Long> accessRuleId, Long access_channel) {
		List<String> channel = new ArrayList<>();
		if(String.valueOf(access_channel).equals("1")) {
	      	channel.add("0");
	      	channel.add("1");
	      	channel.add("2");
	    }else {
	    	channel.add("2");
	      	channel.add("4");
	    } 
		//no (internet key 4)
		
		List<SpecialCollection> query_data = searchSpecialCollectionByLatest(accessRuleId,channel);
		List<PostReturnModel> return_data = new ArrayList<PostReturnModel>(); 
		for (int i = 0; i < query_data.size(); i++) {
			PostReturnModel tmp = new PostReturnModel();
			tmp.setSpecialCollection(query_data.get(i));
			return_data.add(tmp);
		}
		return return_data;
	};
	
	public List<SpecialCollection> searchSpecialCollectionByLatest(List<Long> accessRuleId,List<String> channels) {
		String accessRuleIdString = common.specialCollectionAccessRuleSql(accessRuleId.size());
		String sql = "select b.*, u.fullname"
    			+ " from special_collection_post b "
				+ " Left Join user u ON u.id=b.original_creator"
    			+ " where b.is_deleted = 0 and b.is_public = 1  and publish_at < CURRENT_TIMESTAMP and access_channel IN (:channels) and ("+accessRuleIdString+")"
				+ " order by b.publish_at DESC";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("channels",channels);
		for (int i = 0; i < accessRuleId.size(); i++) {
    	    query.setParameter("accessRuleId" + i, accessRuleId.get(i));
    	}
		List<Object[]> result = query.getResultList(); 
		
		System.out.println("searchSpecialCollectionByLatest:"+sql);
		List<SpecialCollection> specialCollections = new ArrayList<>();
		for (Object[] row : result) {
			User user = new User();
			user.setFullname((String) row[17]);
		    SpecialCollection specialCollection = new SpecialCollection();
		    specialCollection.setId(((Integer) row[0]).longValue());
		    specialCollection.setPostTitle((String) row[1]);
		    specialCollection.setPostTitleZh((String) row[2]);
		    specialCollection.setCreatedAt((Date) row[3]);
		    specialCollection.setCreatedBy(((Integer) row[4]).longValue());
		    specialCollection.setPublishAt((Date) row[10]);
		    specialCollection.setIs_public(Byte.valueOf((byte) row[11]).longValue());
		    specialCollection.setOriginalCreator(user);
		    specialCollection.setShowAsAlias(Byte.valueOf((byte) row[13]).intValue());
		    specialCollection.setAlias((String) row[14]);
		    specialCollection.setLink((String) row[15]);
		    specialCollection.setAccessRuleId((String) row[16]);
		    specialCollection.setAccessChannel((String) row[17]);
		    specialCollections.add(specialCollection);
		}
    	
    	return specialCollections;
	}
	
	@Override
	public SpecialCollection getSpecialCollectionById(Long specialCollectionId, Long userId) {
		return (SpecialCollection) getSpecialCollectionByIdSQL(specialCollectionId,userId);
	}
	
	public SpecialCollection getSpecialCollectionByIdSQL(Long specialCollectionId,Long userId) {		
		String sql = "select b.*, u.fullname"
				+ " from special_collection_post b "
				+ " Left Join user u ON u.id=b.original_creator"
				+ " where b.id = :specialCollectionId and b.is_deleted = 0 and (b.is_public = 1 or (b.original_creator = :userId or b.created_by = "+userId+"))  and b.publish_at < CURRENT_TIMESTAMP";
		@SuppressWarnings("unchecked")
		TypedQuery<Object[]> query = (TypedQuery<Object[]>) entityManager.createNativeQuery(sql);
		query.setParameter("specialCollectionId", specialCollectionId);
		query.setParameter("userId", userId);
		List<Object[]> result = query.getResultList(); 
		System.out.println("getSpecialCollectionById:"+sql);
		SpecialCollection specialCollection = new SpecialCollection();
		for (Object[] row : result) {
			User user = new User();
			user.setId(userId);
			user.setFullname((String) row[18]);
		    specialCollection.setId(((Integer) row[0]).longValue());
		    specialCollection.setPostTitle((String) row[1]);
		    specialCollection.setPostTitleZh((String) row[2]);
		    specialCollection.setCreatedAt((Date) row[3]);
		    specialCollection.setCreatedBy(((Integer) row[4]).longValue());
		    specialCollection.setPublishAt((Date) row[10]);
		    specialCollection.setIs_public(Byte.valueOf((byte) row[11]).longValue());
		    specialCollection.setOriginalCreator(user);
		    specialCollection.setShowAsAlias(Byte.valueOf((byte) row[13]).intValue());
		    specialCollection.setAlias((String) row[14]);
		    specialCollection.setLink((String) row[15]);
		    specialCollection.setAccessRuleId((String) row[16]);
		    specialCollection.setAccessChannel((String) row[17]);
		}
		
		return specialCollection;
	}
	
	@Override
	public SpecialCollection add(SpecialCollection specialCollection) {
		return specialCollectionRepository.saveAndFlush(specialCollection);
	}
	
	@Override
	public Optional<SpecialCollection> findById(Long specialCollectionId) {
		return specialCollectionRepository.findByIdAndIsDeleted(specialCollectionId,0);
	}
	
	@Override
	public SpecialCollection update(SpecialCollection specialCollection) {
		return specialCollectionRepository.saveAndFlush(specialCollection);
	}
}

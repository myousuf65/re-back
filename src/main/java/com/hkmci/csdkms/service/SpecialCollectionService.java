package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.hkmci.csdkms.entity.SpecialCollection;
import com.hkmci.csdkms.model.PostReturnModel;

public interface SpecialCollectionService {
	public List<PostReturnModel> searchSpecialCollectionByCategoryId(Integer page,List<Long> accessRuleId ,Long accessChannel);
	public Integer getTotalSearchSpecialCollectionByCategoryId(List<Long> accessRuleId, Long accessChannel);
	public List<PostReturnModel> getMyPosts(Long userId);
	public List<PostReturnModel> searchSpecialCollectionByLatest(List<Long> accessRuleId,Long accessChannel);
	public SpecialCollection getSpecialCollectionById(Long specialCollectionId, Long userId);
	public SpecialCollection add(SpecialCollection specialCollection);
	public Optional<SpecialCollection> findById(Long specialCollectionId);
	public SpecialCollection update(SpecialCollection specialCollection);
}

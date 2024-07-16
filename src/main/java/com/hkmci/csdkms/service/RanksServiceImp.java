package com.hkmci.csdkms.service;

import java.util.List;

import javax.persistence.TypedQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.RanksModel;
import com.hkmci.csdkms.repository.RanksRepository;


@Service
public class RanksServiceImp implements RanksService {
	@Autowired
	private RanksRepository ranksRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public List<RanksModel> getAll() {
		String sql ="select new RanksModel(id,rankName,rankTypeId) from RanksModel rm order by rm.rankTypeId";
		TypedQuery<RanksModel> query = entityManager.createQuery(sql, RanksModel.class);
		List<RanksModel> result = query.getResultList();
		//System.out.println(" sql - "+ result);
		return result;
	}


	@Override
	public RanksModel getById(Long id) {
		return ranksRepository.findById(id);
	}

}

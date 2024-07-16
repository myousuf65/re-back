package com.hkmci.csdkms.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.SectionModel;



@Service
public class SectionServiceImp implements SectionService {

	@Autowired
	//private SectionRepository sectionRepository;
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<SectionModel> getAll() {
		// TODO Auto-generated method stub
		String sql ="select new SectionModel(id,sectionName) from SectionModel sm";
		TypedQuery<SectionModel> query =entityManager.createQuery(sql, SectionModel.class);
		List<SectionModel> result = query.getResultList();
		return result;
	}

}

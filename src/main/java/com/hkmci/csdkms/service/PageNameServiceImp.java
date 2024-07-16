package com.hkmci.csdkms.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.hkmci.csdkms.model.PageNameModel;



@Service
public class PageNameServiceImp implements PageNameService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<PageNameModel> getAll() {
		// TODO Auto-generated method stub
		String sql ="select new PageNameModel(id,pageNameEn,pageNameTc) from PageNameModel pnm";
		TypedQuery<PageNameModel> query =entityManager.createQuery(sql, PageNameModel.class);
		List<PageNameModel> result = query.getResultList();
		return result;
	}

}

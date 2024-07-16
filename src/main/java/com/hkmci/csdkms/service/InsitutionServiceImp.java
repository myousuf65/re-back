package com.hkmci.csdkms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.InstitutionsModel;


@Service
public class InsitutionServiceImp implements InstitutionService {

	@Autowired
	//private InstitutionService insitutionService;
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public List<InstitutionsModel> getAll() {
		// TODO Auto-generated method stub
		
		String sql = "select new InstitutionsModel(id, instName) from InstitutionsModel im";
		TypedQuery<InstitutionsModel> query = entityManager.createQuery(sql,InstitutionsModel.class);
		List<InstitutionsModel> result= query.getResultList();
		return result;
	}


	@Override
	public List<InstitutionsModel> getAllByUser(User user, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<InstitutionsModel> institution_session = (List<InstitutionsModel>) session.getAttribute("institution");
		
//		System.out.println("Insitution from Session size: " + institution_session.size());
//		System.out.println("Insitution from Session Ids: " + institution_session.stream().map(InstitutionsModel::getId).collect(Collectors.toList()));
		
		if(user.getUsergroup().equals(Long.parseLong("2"))) {
			String instName = institution_session.stream().filter((n) -> n.getId().equals(user.getInstitutionId())).map(InstitutionsModel::getInstName).collect(Collectors.toList()).get(0);
			List<InstitutionsModel> return_data = new ArrayList<>();
			return_data.add(new InstitutionsModel(user.getInstitutionId(), instName));
			return return_data;
		}
		else {
			List<InstitutionsModel> return_data = institution_session.stream()
				.filter((n)-> !n.getId().equals(8L) && !n.getId().equals(16L))	
				.map((n) -> {
				InstitutionsModel temp = new InstitutionsModel(n.getId(), n.getInstName());
				return temp;
			}).collect(Collectors.toList());
//			String sql = "select new InstitutionsModel(id, instName) from InstitutionsModel im";
//			TypedQuery<InstitutionsModel> query = entityManager.createQuery(sql,InstitutionsModel.class);
//			List<InstitutionsModel> result= query.getResultList();
			
			
			return return_data;
		}
	}

}

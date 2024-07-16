package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkmci.csdkms.entity.Favorites;
import com.hkmci.csdkms.repository.FavoritesRepository;

@Service
public class FavoritesServiceImp implements FavoritesService {

	@Autowired
	private FavoritesRepository favoritesRepository;
	
	
	@Override
	public Favorites save(Favorites new_data) {
		// TODO Auto-generated method stub
		return favoritesRepository.saveAndFlush(new_data);
	}


	@Override
	public List<Favorites> find(Long userId) {
		// TODO Auto-generated method stub
		return favoritesRepository.findByUserId(userId);
	}


	@Override
	public Optional<Favorites> findById(long ref,long userId) {
		// TODO Auto-generated method stub
//		System.out.println("Delete id = "+ref+" USER ID "+ userId);
		return favoritesRepository.findById(ref, userId);
	}
	

}

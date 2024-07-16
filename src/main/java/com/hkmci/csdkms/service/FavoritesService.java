package com.hkmci.csdkms.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.entity.Favorites;

public interface FavoritesService {

	Favorites save(Favorites new_data);

	List<Favorites> find(Long userId);

	Optional<Favorites> findById(long asLong, long userId);

	

}

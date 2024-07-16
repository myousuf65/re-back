package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.Favorites;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

	
	@Query("select f from Favorites f where f.createdBy =?1 and f.isDeleted =0 ")
	List<Favorites> findByUserId(Long userId);

	@Query("select f from Favorites f where f.resourceId=?1 and f.createdBy =?2  ")
	Optional<Favorites> findById(Long ref, Long userId);

}

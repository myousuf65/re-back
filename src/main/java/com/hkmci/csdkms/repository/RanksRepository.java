package com.hkmci.csdkms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkmci.csdkms.entity.Ranks;
import com.hkmci.csdkms.model.RanksModel;

public interface RanksRepository extends JpaRepository<RanksModel, Integer> {

	RanksModel findById(Long id);

	Optional<RanksModel> findAllById(Long rankId);

}

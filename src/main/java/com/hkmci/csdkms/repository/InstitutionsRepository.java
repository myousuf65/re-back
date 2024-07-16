package com.hkmci.csdkms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkmci.csdkms.entity.Institutions;
import com.hkmci.csdkms.model.InstitutionsModel;

public interface InstitutionsRepository extends JpaRepository<InstitutionsModel, Integer> {

	Optional<InstitutionsModel> findById(Long institutionId);

}

package com.hkmci.csdkms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hkmci.csdkms.entity.Sections;
import com.hkmci.csdkms.model.SectionModel;

public interface SectionRepository extends JpaRepository<SectionModel, Integer> {

	Optional<SectionModel> findById(Long sectionId);

}

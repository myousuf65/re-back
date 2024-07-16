package com.hkmci.csdkms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.SpecialCollection;

public interface SpecialCollectionRepository extends JpaRepository<SpecialCollection, Long> {

	
	@Query(value = "select b.* from special_collection_post b"
			+ " where b.id = ?1 and b.is_deleted = ?2 and publish_at < CURRENT_TIMESTAMP "
			+ " order by publish_at desc limit 0 , 10" , nativeQuery = true)
	Optional<SpecialCollection> findByIdAndIsDeleted(Long specialCollectionId,Integer isDeleted);
}

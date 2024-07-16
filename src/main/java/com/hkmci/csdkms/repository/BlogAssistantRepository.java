package com.hkmci.csdkms.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.BlogAssistant;

public interface BlogAssistantRepository extends JpaRepository<BlogAssistant, Long> {

	@Query("Select ba.assistantId from BlogAssistant ba where ba.userId = ?1 and ba.isDeleted = 0")
	List<Long> findAssistantIdByUserIdAndIsDeleted(Long userId, Integer isDeleted);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update BlogAssistant ba set "
			+ " ba.isDeleted = 1 "
			+ " where ba.isDeleted = 0 "
			+ " and ba.userId = ?1 "
			+ " and ba.assistantId = ?2 ")
	void deleteAssistant(Long bloggerId, Long assistantId, Long userId);

	List<BlogAssistant> findByUserId(Long bloggerId);

	@Query("Select ba.userId from BlogAssistant ba where ba.assistantId = ?1 and ba.isDeleted = 0")
	List<Long> findOriginalIdByUserIdAndIsDeleted(Long user_id, Integer isDeleted);
	
	
}

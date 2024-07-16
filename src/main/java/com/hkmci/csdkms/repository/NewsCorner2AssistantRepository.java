package com.hkmci.csdkms.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.NewsCorner2Assistant;

public interface NewsCorner2AssistantRepository extends JpaRepository<NewsCorner2Assistant, Long> {

	@Query("Select ba.assistantId from NewsCorner2Assistant ba where ba.userId = ?1 and ba.isDeleted = 0")
	List<Long> findAssistantIdByUserIdAndIsDeleted(Long userId, Integer isDeleted);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update NewsCorner2Assistant ba set "
			+ " ba.isDeleted = 1 "
			+ " where ba.isDeleted = 0 "
			+ " and ba.userId = ?1 "
			+ " and ba.assistantId = ?2 ")
	void deleteAssistant(Long newscorner2Id, Long assistantId, Long userId);

	List<NewsCorner2Assistant> findByUserId(Long newscorner2Id);

	@Query("Select ba.userId from NewsCorner2Assistant ba where ba.assistantId = ?1 and ba.isDeleted = 0")
	List<Long> findOriginalIdByUserIdAndIsDeleted(Long user_id, Integer isDeleted);
	
	
}

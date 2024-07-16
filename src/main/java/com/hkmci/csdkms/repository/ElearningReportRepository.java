package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningQuestion;
import com.hkmci.csdkms.entity.ElearningQuiz;
import com.hkmci.csdkms.entity.ElearningReport;

public interface ElearningReportRepository extends JpaRepository<ElearningReport, Long>{
	@Query(value = "select * from elearning_report"
			+ " where quiz_id = :quizId AND created_by = :clientId AND quiz_conduct_code = :quizConductCode order by created_at DESC, id ASC" , nativeQuery = true)
	List<ElearningReport> reportSearch(Long clientId,Long quizId, String quizConductCode);

	List<ElearningReport> findByQuizConductCode(String quizConductCode);
}

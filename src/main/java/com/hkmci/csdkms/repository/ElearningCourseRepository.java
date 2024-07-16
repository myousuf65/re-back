package com.hkmci.csdkms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.ElearningCourse;

public interface ElearningCourseRepository extends JpaRepository<ElearningCourse, Long>{
	@Query(value = "select * from elearning_course"
			+ " where is_deleted = 0"
			+ " AND (course_name LIKE CONCAT('%', :search, '%')  OR :search = '') ORDER BY id desc LIMIT :start_num , :end_num " , nativeQuery = true)
	List<ElearningCourse> courseSearch(Integer start_num, Integer end_num, String search);

	@Query(value = "select * from elearning_course"
			+ " where is_deleted = :isDeleted " , nativeQuery = true)
	List<ElearningCourse> findByIsDeleted(int isDeleted);
	
	
	@Query(value = "select * from elearning_course"
			+ " where is_deleted = 0 and is_Publish = 1 "
			+ " and id= :id and FIND_IN_SET(:staffNo, REPLACE(staff_no_list, '\\n', ',')) > 0"
			+ " AND DATE(start_date) <= CURDATE() "
			+ " AND DATE(end_date) >= CURDATE()", nativeQuery = true)
	Optional<ElearningCourse> findByIdAndStaffList(Long id, String staffNo);

	@Query(value = "select * from elearning_course"
			+ " where course_name = :courseName AND is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningCourse> findByCourseName(String courseName);

	@Query(value = "select * from elearning_course"
			+ " where id!= :elearningCourseId AND course_name = :courseName AND is_deleted = 0 " , nativeQuery = true)
	Optional<ElearningCourse> findDuplicateByIdCourseName(Long elearningCourseId, String courseName);
}

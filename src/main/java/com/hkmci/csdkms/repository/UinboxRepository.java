package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.Uinbox;

public interface UinboxRepository extends JpaRepository<Uinbox, Long> {



	
	
	@Query (value="select count(u.id) from uinbox u where u.sent_to = ?1 and u.is_read =0 and u.is_deleted = 0 ",nativeQuery = true )
	Integer countmail(Long userId);
	
//	@Query(value = " select u.id, r.id as resource_id , u.created_at,u.sent_by, u.sent_to , u.is_read , r.title_tc, r.title_en , b.id as blog_id, b.post_title,u.mail_type "
//			+ " from uinbox u "
//			+ " left join resource r on u.resource_id = r.id and u.mail_type =1 "
//			+ " left join blog_post b on u.resource_id = b.id and u.mail_type = 2 "
	@Query(value = " select u.id, r.id as resource_id , u.created_at,u.sent_by, u.sent_to , u.is_read , r.title_tc, r.title_en , COALESCE(b.id, 0) as blog_id, COALESCE(b.post_title,'') as post_title,u.mail_type "
	+ " from uinbox u "
	+ " left join resource r on u.resource_id = r.id and u.mail_type =1 "
	+ " left join blog_post b on u.resource_id = b.id and u.mail_type = 2 "
			+ " where u.sent_to = ?1 and u.is_deleted = 0 ",nativeQuery = true )
	List<Object []> getMail(Long userId);

	
	
	@Query("Select u from Uinbox u where id = ?1 and isDeleted = 0 ")
	Uinbox getInbox(Long id);

}

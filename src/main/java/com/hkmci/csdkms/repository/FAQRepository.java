package com.hkmci.csdkms.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.entity.Faq;

public interface FAQRepository extends JpaRepository<Faq, Long> {
	@Query(value="select faq1.chinese, faq1.english, dd.chinese as question_chinese, dd.english as  question_englist , dd.id as question_id " + 
			"From csdkms.faq faq1 " + 
			"left join csdkms.faq  dd on dd.id = faq1.link_to " + 
			"where faq1.link_to !=0 and faq1.is_deleted =0 and dd.is_deleted = 0", nativeQuery = true)
	List<Object []> FaqReturn();
	
	
	
	
	@Query(value="select faq.* from csdkms.faq faq where faq.id = ?1", nativeQuery = true)
	Faq findFaq(Long id);
	
	@Query(value="select faq.* from csdkms.faq faq where faq.parent_id =?1 and is_deleted=0", nativeQuery = true)
	Faq findFaqAnswer(Long id);
	
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update csdkms.faq set is_deleted = 1 ,deleted_at =?1 , deleted_by =?2 where id in ?3 "
	,nativeQuery = true)
	void deleteFaq(Date deletedAt, Long deletedBy, List<Long> faqId);
	
	@Query(value = "with recursive cte (id, " + 
			"             chinese,english, qa,is_deleted, parent_id, title_level ) as ( " + 
			"  select     id, " + 
			"             chinese,english, qa,is_deleted, parent_id, title_level " + 
			"  from       csdkms.faq " + 
			"  where      id = ?1 "+ 
			"  union all " + 
			"  select     p.id, " + 
			"             p.chinese,p.english, p.qa,p.is_deleted, p.parent_id, p.title_level " + 
			"  from        csdkms.faq p " + 
			"  inner join cte " + 
			"          on p.parent_id = cte.id " + 
			")" + 
			"select * from cte",nativeQuery = true )
	List<Object []> findFaqList(Long faqId);
	
	
	@Query(value = "with recursive cte (id, " + 
			"             chinese,english, qa,is_deleted, parent_id, title_level ) as ( " + 
			"  select     id, " + 
			"             chinese,english, qa,is_deleted, parent_id, title_level " + 
			"  from       csdkms.faq " + 
			"  where      id = ?1 "+ 
			"  union all " + 
			"  select     p.id, " + 
			"             p.chinese,p.english, p.qa,p.is_deleted, p.parent_id, p.title_level " + 
			"  from        csdkms.faq p " + 
			"  inner join cte " + 
			"          on p.parent_id = cte.id " + 
			")" + 
			"select id from cte",nativeQuery = true )
	List<Object []> finddeleteFaqList(Long faqId);
	
	@Query(value="select faq.* from csdkms.faq faq "
			+ " where faq.is_deleted= 0 ",nativeQuery = true)
	List<Faq> FaqParent();
	
}

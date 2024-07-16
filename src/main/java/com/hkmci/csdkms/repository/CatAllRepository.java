package com.hkmci.csdkms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.CatAllModel;


public interface CatAllRepository extends JpaRepository<CatAllModel, Integer>{
	
	
	@Query("select c from CatAllModel c"+
			   " where c.id =?1")
	List<CatAllModel> searchParentId(Long parentId);

	@Query("select c from CatAllModel c"+
			   " where c.id =?1")
	CatAllModel getById(Long id);

	@Query("select c from CatAllModel c "+
			   " order by c.NameEn")
	List<CatAllModel> findAllByOrderNameEn();

	@Query( value ="WITH RECURSIVE cte (id, parent_cat_id) as ( " + 
			"	select id ,parent_cat_id " + 
			"    from csdkms.category " + 
			"    where id = ?1 " + 
			"union all " + 
			"	select c.id,c.parent_cat_id " + 
			"    from csdkms.category c " + 
			"    inner join cte " + 
			"		on c.id = cte.parent_cat_id " + 
			"    )" + 
			"    select parent_cat_id from cte" , nativeQuery = true)
	List<Long> findChildren(Long id);
	
	
}

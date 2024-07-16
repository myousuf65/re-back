package com.hkmci.csdkms.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hkmci.csdkms.model.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, Integer> {
	
	
	@Query( value =" select c.id from  CategoryModel c where c.NameEn = ?1 or c.NameTc = ?1")
	Integer SearchParentId(String Name);
	
	@Query(" select c from CategoryModel c"+
			   " where c.id =?1")
	CategoryModel searchParentId(Long parentId);
	
	@Query( " select c from CategoryModel c"+
	 " where( c.NameEn =?1 or c.NameTc =?1 or ?1 IS NULL)"+
	 "and (parentCatId =?2 or ?2 Is NULL)")
	List<CategoryModel> searchFunction(String Name, Integer parentId);

	@Transactional
	@Modifying
	@Query("delete from CategoryModel c where c.id = ?1")
	void deleteCategory(Long Id);

	
	@Query(value =" WITH RECURSIVE cte_cat AS( " + 
			"    SELECT c.id, c.name_en, c.parent_cat_Id,c.show_info, c.name_tc, c.order_cat, c.level  FROM category as c " + 
			"    WHERE c.parent_cat_Id =0 AND c.show_info=1 " + 
			"    UNION " + 
			"	SELECT c.id, c.name_en, c.parent_cat_Id,c.show_info, c.name_tc, c.order_cat, c.level  FROM category as c" + 
			"    INNER JOIN cte_cat a" + 
			"		ON a.id =  c.parent_cat_Id " + 
			"        )" + 
			"    SELECT *" + 
			"FROM " + 
			"   cte_cat " , nativeQuery = true)
	List<CategoryModel> findAllTrue();
	
	
	

	@Query( value =" select c.* from category c where c.id = (select c.id from  category c where c.parent_cat_id = ?1) and c.is_deleted = 0 ", nativeQuery = true)
	List<CategoryModel> seachById(Long id);
	

	@Query( value =" select c from  CategoryModel c where c.parentCatId = 0 and c.showInfo=1")
	List<CategoryModel> findAllParent();

	@Query(" select c from CategoryModel c"+
			   " where c.id =?1 or c.parentCatId = ?1 and c.showInfo=1 order by c.NameEn")
	List<CategoryModel> searchParentIdWithChildren(Long parentId);
	
	
	
	
	//@Query(value = "select c.* from  category c where FIND_IN_SET (?1,c.name_en)", nativeQuery = true)

	@Query(value = "select c.* from  category c where  c.is_deleted =0 and ( Locate(?1,c.name_en) > 0  or Locate(?1, c.name_tc)>0 )", nativeQuery = true)
	List<CategoryModel> getName(String cat);

	
	@Query(value ="select c.* from category c where c.is_deleted =0 order by c.name_en ASC", nativeQuery =true)
	List<CategoryModel> findByIsDeleted(Integer isDeleted);
	
	@Query(value = "WITH RECURSIVE CTE AS (" + 
			"	Select id , parent_cat_id FROM csdkms.category WHERE parent_cat_id = ?1 " + 
			"    UNION ALL " + 
			"    Select p.id, p.parent_cat_id FROM csdkms.category p JOIN CTE as c on p.parent_cat_id =c.id " + 
			") " + 
			"Select id FROM CTE", nativeQuery = true)
	List<Long> getSubCatId(Long categoryId);
	
	
	
}
 
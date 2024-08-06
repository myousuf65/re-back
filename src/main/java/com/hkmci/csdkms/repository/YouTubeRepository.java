package com.hkmci.csdkms.repository;

import com.hkmci.csdkms.entity.NewsCorner2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface YouTubeRepository extends JpaRepository<NewsCorner2, Long> {

    @Query(value = "select b.* from newscorner2_post b"
            + " where b.category_id=3 "
            + " order by 1 desc limit 0 , 3" , nativeQuery = true)
    List<NewsCorner2> getHomePage();
}

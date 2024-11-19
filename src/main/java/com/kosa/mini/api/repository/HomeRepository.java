package com.kosa.mini.api.repository;

import com.kosa.mini.api.dto.home.HomeStoreDTO;
import com.kosa.mini.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HomeRepository extends JpaRepository<Store, Integer> {

    @Query("select new com.kosa.mini.api.dto.home.HomeStoreDTO(" +
            "   s.storeId, " +
            "   s.storeName, " +
            "   s.storePhoto, " +
            "   s.storeDescription, " +
            "   COALESCE(round(avg(r.rating),1), 0) as ratingAvg, " + // COALESCE로 null 처리
            "   c.categoryName ) " +
            "from " +
            "   Store s " +
            "left join " +
            "   s.reviews r " +
            "left join " +
            "   s.category c " +
            "group by " +
            "   s.storeId, s.storeName, s.storePhoto, s.storeDescription, c.categoryName")
    List<HomeStoreDTO> index();
}


package com.kosa.mini.api.repository;

import com.kosa.mini.api.domain.store.StoreContentDTO;
import com.kosa.mini.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT new com.kosa.mini.api.domain.store.StoreContentDTO(" +
            "s.storeId, " +
            "s.storeName, " +
            "s.postcode, " +
            "s.roadAddress, " +
            "s.detailAddress, " +
            "s.extraAddress, " +
            "s.storeDescription, " +
            "s.websiteInfo, " +
            "s.openingTime, " +
            "s.closingTime, " +
            "s.contactNumber, " +
            "s.storePhoto, " +
            "s.owner.memberId, " +
            "COALESCE(AVG(r.rating), 0), " +
            "COUNT(r)) " +
            "FROM Store s LEFT JOIN s.reviews r " +
            "WHERE s.storeId = :id " +
            "GROUP BY s.storeId, s.storeName, s.postcode, s.roadAddress, s.detailAddress, s.extraAddress, " +
            "s.storeDescription, s.websiteInfo, s.openingTime, s.closingTime, s.contactNumber, " +
            "s.storePhoto, s.owner.memberId")
    StoreContentDTO findStoreWithContent(@Param("id") Long id);
}

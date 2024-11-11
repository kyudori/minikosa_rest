package com.kosa.mini.api.repository;

import com.kosa.mini.api.domain.store.StoreContentDTO;
import com.kosa.mini.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT new com.kosa.mini.api.domain.store.StoreContentDTO(" +
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
            "COALESCE(AVG(r.rating), 0) AS ratingAvg, " +
            "COUNT(r) AS countReview ) " +
            "FROM Store s LEFT JOIN s.reviews r " +
            "WHERE s.storeId = :storeId ")
    StoreContentDTO findStoreWithContent(Long storeId);
}

package com.kosa.mini.api.repository;

import com.kosa.mini.api.domain.store.StoreContentDTO;
import com.kosa.mini.api.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT " +
            "s.store_id AS storeId, " +
            "s.owner_id AS ownerId, " +
            "s.store_name AS storeName, " +
            "s.postcode, " +
            "s.road_address AS roadAddress, " +
            "s.detail_address AS detailAddress, " +
            "s.extra_address AS extraAddress, " +
            "s.store_description AS storeDescription, " +
            "s.opening_time AS openingTime, " +
            "s.closing_time AS closingTime, " +
            "s.website_info AS websiteInfo, " +
            "s.store_photo AS storePhoto, " +
            "s.contact_number AS contactNumber, " +
            "COALESCE(AVG(r.rating), 0) AS ratingAvg, " +
            "COUNT(r.review_id) AS countReview " +
            "FROM stores s " +
            "LEFT JOIN reviews r ON s.store_id = r.store_id " +
            "WHERE s.store_id = :storeId ",
            nativeQuery = true)
    public StoreContentDTO findStoreWithContent(Long storeId);
}

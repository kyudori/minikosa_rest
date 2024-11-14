package com.kosa.mini.api.repository;

import com.kosa.mini.api.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByStoreStoreId(Integer storeId);
}

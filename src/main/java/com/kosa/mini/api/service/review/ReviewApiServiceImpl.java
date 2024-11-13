package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReviewSaveDTO;
import com.kosa.mini.api.dto.review.StoreReviewDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewApiServiceImpl implements ReviewApiService {

    @Autowired
    private ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<StoreReviewDTO> getReply(Integer storeId) {
        List<Review> reviewEntity = reviewRepository.findByStoreStoreId(storeId);
        List<StoreReviewDTO> reviewList = reviewEntity.stream()
                .map(review -> StoreReviewDTO.builder()
                        .reviewText(review.getReviewText())
                        .rating(review.getRating())
                        .reviewId(review.getReviewId())
                        .storeName(review.getStore().getStoreName())
                        .memberNickname(review.getMember().getNickname())
                        .build()).collect(Collectors.toList());
        return reviewList;


    }

    @Override
    public Review createReview(ReviewSaveDTO reviewDTO) {
        Review reviewEntity = reviewDTO.toEntity();

        // 엔티티 매니저로 프록시 객체 생성하여 설정 (DB 조회 없이 ID만으로 객체 생성)
        reviewEntity.setMember(entityManager.getReference(Member.class, reviewDTO.getMemberId()));
        reviewEntity.setStore(entityManager.getReference(Store.class, reviewDTO.getStoreId()));

        System.out.println("서비스 : " + reviewEntity.toString());
        return reviewRepository.save(reviewEntity);
    }
}

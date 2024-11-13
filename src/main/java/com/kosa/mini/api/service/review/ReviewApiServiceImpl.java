package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReviewSaveDTO;
import com.kosa.mini.api.dto.review.StoreReviewDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.ReviewRepository;
import com.kosa.mini.api.repository.StoreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewApiServiceImpl implements ReviewApiService {

    @Autowired
    private ReviewRepository reviewRepository;
    private StoreRepository storeRepository;
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public List<StoreReviewDTO> getReply(Integer storeId) {
        List<Review> reviews = reviewRepository.findByStoreStoreId(storeId);
        return reviews.stream()
                .map(review -> StoreReviewDTO.builder()
                        .storeId(review.getStore().getStoreId())
                        .memberId(review.getMember().getMemberId())
                        .reviewText(review.getReviewText())
                        .rating(review.getRating())
                        .reviewId(review.getReviewId())
                        .storeName(review.getStore().getStoreName())
                        .memberNickname(review.getMember().getNickname())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Review createReview(ReviewSaveDTO reviewSaveDTO) {
        Store store = storeRepository.findById(reviewSaveDTO.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + reviewSaveDTO.getStoreId()));

        Member member = memberRepository.findById(reviewSaveDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + reviewSaveDTO.getMemberId()));

        Review review = reviewSaveDTO.toEntity(store, member);

        return reviewRepository.save(review);
    }
}

package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReviewSaveDTO;
import com.kosa.mini.api.dto.review.ReviewResponseDTO;
import com.kosa.mini.api.dto.review.StoreReviewDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.MemberNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.ReviewRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewApiServiceImpl implements ReviewApiService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<StoreReviewDTO> getReplies(Integer storeId) {
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

    @Override
    @Transactional
    public ReviewResponseDTO createReview(ReviewSaveDTO reviewSaveDTO) {
        // Store 조회
        Store store = storeRepository.findById(reviewSaveDTO.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + reviewSaveDTO.getStoreId()));

        // Member 조회
        Member member = memberRepository.findById(reviewSaveDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + reviewSaveDTO.getMemberId()));

        // Review 엔티티 생성
        Review review = reviewSaveDTO.toEntity(store, member);

        // 리뷰 저장
        Review savedReview = reviewRepository.save(review);

        // DTO로 변환하여 반환
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return ReviewResponseDTO.builder()
                .reviewId(savedReview.getReviewId())
                .storeId(savedReview.getStore().getStoreId())
                .memberId(savedReview.getMember().getMemberId())
                .reviewText(savedReview.getReviewText())
                .rating(savedReview.getRating())
                .storeName(savedReview.getStore().getStoreName())
                .memberNickname(savedReview.getMember().getNickname())
                .createdAt(formatter.format(savedReview.getCreatedAt()))
                .updatedAt(savedReview.getUpdatedAt() != null ? formatter.format(savedReview.getUpdatedAt()) : null)
                .build();
    }
}

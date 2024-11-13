package com.kosa.mini.api.dto.review;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewSaveDTO {
    private Integer storeId;    // 변경: Store -> Integer
    private Integer memberId;   // 변경: Member -> Integer
    private String reviewText;
    private Integer rating;
    private Integer reviewId;
    private String storeName;

    public Review toEntity(Store store, Member member) {
        return Review.builder()
                .store(store)
                .member(member)
                .reviewText(reviewText)
                .rating(rating)
                .reviewId(reviewId)
                .build();
    }
}

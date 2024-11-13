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
    private Store storeId;
    private Member memberId;
    private String reviewText;
    private Integer rating;
    private Integer reviewId;
    private String storeName;


    public Review toEntity() {
        return Review.builder()
                .store(storeId)
                .member(memberId)
                .reviewText(reviewText)
                .rating(rating)
                .reviewId(reviewId)
                .build();
    }
}

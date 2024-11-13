package com.kosa.mini.api.dto.review;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StoreReviewDTO {
    private Integer storeId;
    private Integer memberId;
    private String reviewText;
    private Integer rating;
    private Integer reviewId;
    private String storeName;
    private String memberNickname;

    public Review toEntity(Store store, Member member){
        return Review.builder()
                .member(member)
                .store(store)
                .reviewText(reviewText)
                .rating(rating)
                .reviewId(reviewId)
                .build();
    }
}

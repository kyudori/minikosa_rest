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
    private Store storeId;
    private Member memberId;
    private String reviewText;
    private Integer rating;
    private Integer reviewId;

//    private Date createdAt;
//    private Date updatedAt;
    private String storeName;
    private String memberNickname;


    public Review toEntity(){
        return Review.builder()
                .member(memberId)
                .store(storeId)
                .reviewText(reviewText)
                .rating(rating)
                .reviewId(reviewId)
                .build();
    }
}

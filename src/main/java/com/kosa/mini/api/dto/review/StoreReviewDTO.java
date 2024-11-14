package com.kosa.mini.api.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreReviewDTO {
    private Integer storeId;
    private Integer memberId;
    private String reviewText;
    private Integer rating;
    private Integer reviewId;
    private String storeName;
    private String memberNickname;
}

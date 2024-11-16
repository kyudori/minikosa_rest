package com.kosa.mini.api.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StoreReviewDTO {
    private Integer storeId;
    private Integer memberId;
    private Integer reviewId;
    private String storeName;
    private String memberNickname;
    private String reviewText;
    private Integer rating;
    private Date createdAt;
    private Date updatedAt;
}

package com.kosa.mini.api.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDTO {
    private Integer reviewId;
    private Integer storeId;
    private Integer memberId;
    private String reviewText;
    private Integer rating;
    private String storeName;
    private String memberNickname;
    private String createdAt;
    private String updatedAt;
}

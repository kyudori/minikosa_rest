package com.kosa.mini.api.dto.store;

import lombok.Data;

import java.util.Date;

@Data
public class StoreReviewDTO {
    private int storeId;
    private int memberId;
    private String reviewText;
    private int rating;
    private int reviewId;

    private Date createdAt;
    private Date updatedAt;
    private String storeName;
    private String memberNickname;
}

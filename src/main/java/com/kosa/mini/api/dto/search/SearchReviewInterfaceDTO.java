package com.kosa.mini.api.dto.search;

import java.util.Date;

public interface SearchReviewInterfaceDTO {
    Integer getStoreId();
    Integer getMemberId();
    Integer getReviewId();
    String getStoreName();
    String getMemberNickname();
    String getReviewText();
    Integer getRating();
    Date getCreatedAt();
    Date getUpdatedAt();
}

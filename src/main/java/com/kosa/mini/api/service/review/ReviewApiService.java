package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReviewSaveDTO;
import com.kosa.mini.api.dto.review.StoreReviewDTO;
import com.kosa.mini.api.entity.Review;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewApiService {
    List<StoreReviewDTO> getReply(Integer storeId);
    Review createReview(ReviewSaveDTO dto);
}

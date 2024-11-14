package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReviewReplyDTO;
import com.kosa.mini.api.dto.review.ReviewResponseDTO;
import com.kosa.mini.api.dto.review.ReviewSaveDTO;

import java.util.List;

public interface ReviewApiService {
    List<ReviewReplyDTO> getReviews(Integer storeId);
    ReviewResponseDTO createReview(ReviewSaveDTO dto);
    Boolean deleteReview(Integer reviewId);
}

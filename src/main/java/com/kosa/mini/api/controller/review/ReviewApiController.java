package com.kosa.mini.api.controller.review;

import com.kosa.mini.api.dto.review.ReviewSaveDTO;
import com.kosa.mini.api.dto.review.StoreReviewDTO;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.service.review.ReviewApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewApiController {

    private final ReviewApiService reviewApiService;

    @GetMapping("/review/{storeId}")
    public ResponseEntity<List<StoreReviewDTO>> showReply(@PathVariable("storeId") Integer storeId) {
        List<StoreReviewDTO> replyList = reviewApiService.getReply(storeId);
        return new ResponseEntity<>(replyList, HttpStatus.OK);
    }

    @PostMapping("/review/{storeId}")
    public ResponseEntity<Review> createReview(@PathVariable("storeId") Integer storeId, @RequestBody ReviewSaveDTO reviewSaveDTO) {
        reviewSaveDTO.setStoreId(storeId);
        System.out.println("컨트롤러 : " + reviewSaveDTO.toString());
        Review created = reviewApiService.createReview(reviewSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}

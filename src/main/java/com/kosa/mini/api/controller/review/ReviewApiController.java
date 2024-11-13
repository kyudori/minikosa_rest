package com.kosa.mini.api.controller.review;

import com.kosa.mini.api.dto.review.ReviewSaveDTO;
import com.kosa.mini.api.dto.review.ReviewResponseDTO;
import com.kosa.mini.api.exception.MemberNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.service.review.ReviewApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ReviewApiController {

    private final ReviewApiService reviewApiService;

    @Autowired
    public ReviewApiController(ReviewApiService reviewApiService) {
        this.reviewApiService = reviewApiService;
    }

    @PostMapping("/review/{storeId}")
    public ResponseEntity<ReviewResponseDTO> createReview(
            @PathVariable("storeId") Integer storeId,
            @Valid @RequestBody ReviewSaveDTO reviewSaveDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // UserDetails에서 username (memberId) 가져오기
        String memberIdStr = userDetails.getUsername(); // CustomUserDetailsService에서 username을 memberId로 설정했음
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format: {}", memberIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // ReviewSaveDTO에 memberId 및 storeId 설정
        reviewSaveDTO.setMemberId(memberId);
        reviewSaveDTO.setStoreId(storeId);

        log.debug("컨트롤러 : " + reviewSaveDTO.toString());

        try {
            ReviewResponseDTO created = reviewApiService.createReview(reviewSaveDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (StoreNotFoundException | MemberNotFoundException e) {
            log.error("리뷰 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("리뷰 생성 중 예상치 못한 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 리뷰 조회 예시 (필요 시 추가)
    @GetMapping("/reviews/{storeId}")
    public ResponseEntity<?> getReviews(@PathVariable("storeId") Integer storeId) {
        try {
            return ResponseEntity.ok(reviewApiService.getReplies(storeId));
        } catch (Exception e) {
            log.error("리뷰 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 조회 중 오류가 발생했습니다.");
        }
    }
}

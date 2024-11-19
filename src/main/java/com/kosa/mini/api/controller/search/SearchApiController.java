package com.kosa.mini.api.controller.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchReviewResultDTO;
import com.kosa.mini.api.dto.search.SearchStoreResultDTO;
import com.kosa.mini.api.service.search.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchApiController {

    private final SearchApiService searchApiService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "q", defaultValue = "") String q,
                                    @RequestParam(name = "sort", defaultValue = "latest") String sort,
                                    @RequestParam(name = "type", defaultValue = "store") String type,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            if ("store".equalsIgnoreCase(type)) {
                SearchStoreResultDTO searchResultDTO = searchApiService.searchStore(q, sort);
                return ResponseEntity.status(HttpStatus.OK).body(searchResultDTO);
            } else if ("review".equalsIgnoreCase(type)) {
                SearchReviewResultDTO searchResultDTO = searchApiService.searchReviews(q, sort);
                return ResponseEntity.status(HttpStatus.OK).body(searchResultDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid type parameter. Allowed values are 'store' or 'review'.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 로그에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}

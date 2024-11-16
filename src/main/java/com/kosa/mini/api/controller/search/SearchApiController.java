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
    public ResponseEntity<?> searchStore(@RequestBody SearchDTO searchDTO,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        SearchStoreResultDTO searchResultDTO;
        try {
            searchResultDTO = searchApiService.searchStore(searchDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(searchResultDTO);
    }

    @GetMapping("/search/reviews")
    public ResponseEntity<?> searchReviews(@RequestBody SearchDTO searchDTO,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        SearchReviewResultDTO searchResultDTO;
        try{
            searchResultDTO = searchApiService.searchReviews(searchDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(searchResultDTO);
    }
}

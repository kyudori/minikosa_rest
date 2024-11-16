package com.kosa.mini.api.service.search;

import com.kosa.mini.api.dto.review.StoreReviewDTO;
import com.kosa.mini.api.dto.search.*;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.repository.HomeRepository;
import com.kosa.mini.api.repository.ReviewRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchApiServiceImpl implements SearchApiService {
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public SearchStoreResultDTO searchStore(SearchDTO searchDTO) {
        SearchStoreResultDTO storeResultDTO = new SearchStoreResultDTO();
        storeResultDTO.setQuery(searchDTO.getQuery());

        String storeSort = "s.updated_at DESC";  // 기본 정렬 기준

        // "rating"일 경우, "ratingAvg"로 정렬
        if ("rating".equals(searchDTO.getSort())) {
            storeSort = "ratingAvg DESC";  // 정렬 기준을 ratingAvg로 설정
        }

        List<SearchStoreInterfaceDTO> storeInterfaceList = storeRepository.findByStoreSearch(searchDTO.getQuery(), storeSort);
        List<SearchStoreDTO> storeList = storeInterfaceList.stream()
                .map(storeInterface -> new SearchStoreDTO(
                        storeInterface.getStoreId(),
                        storeInterface.getStoreName(),
                        storeInterface.getStorePhoto(),
                        storeInterface.getStoreDescription(),
                        storeInterface.getRatingAvg()))
                .collect(Collectors.toList());
        storeResultDTO.setStoreResults(storeList);
        storeResultDTO.setStoreCount(storeList.size());
        return storeResultDTO;
    }

    @Override
    public SearchReviewResultDTO searchReviews(SearchDTO searchDTO) {
        SearchReviewResultDTO reviewResultDTO = new SearchReviewResultDTO();
        reviewResultDTO.setQuery(searchDTO.getQuery());

        String reviewSort = "rv.updated_at DESC";

        // "rating"일 경우, "ratingAvg"로 정렬
        if ("rating".equals(searchDTO.getSort())) {
            reviewSort = "rv.rating DESC";
        }

        List<SearchReviewInterfaceDTO> reviewInterfaceList = reviewRepository.findByReviewSearch(searchDTO.getQuery(), reviewSort);
        List<StoreReviewDTO> reviewList = reviewInterfaceList.stream()
                .map(reviewInterface -> new StoreReviewDTO(
                        reviewInterface.getStoreId(),
                        reviewInterface.getMemberId(),
                        reviewInterface.getReviewId(),
                        reviewInterface.getStoreName(),
                        reviewInterface.getMemberNickname(),
                        reviewInterface.getReviewText(),
                        reviewInterface.getRating(),
                        reviewInterface.getCreatedAt(),
                        reviewInterface.getUpdatedAt()))
                .collect(Collectors.toList());
        reviewResultDTO.setReviewResults(reviewList);
        reviewResultDTO.setReviewCount(reviewList.size());
        return reviewResultDTO;
    }
}

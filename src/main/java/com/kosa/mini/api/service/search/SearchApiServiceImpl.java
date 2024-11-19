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
    public SearchStoreResultDTO searchStore(String q, String sort) {
        SearchStoreResultDTO storeResultDTO = new SearchStoreResultDTO();
        storeResultDTO.setQuery(q);

        List<SearchStoreInterfaceDTO> storeInterfaceList;
        boolean isQueryEmpty = (q == null || q.trim().isEmpty());

        if ("rating".equals(sort)) {
            if (isQueryEmpty) {
                storeInterfaceList = storeRepository.findAllStoresOrderByRating();
            } else {
                storeInterfaceList = storeRepository.findByStoreSearchOrderByRating(q);
            }
        } else {
            if (isQueryEmpty) {
                storeInterfaceList = storeRepository.findAllStoresOrderByUpdatedAt();
            } else {
                storeInterfaceList = storeRepository.findByStoreSearchOrderByUpdatedAt(q);
            }
        }

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
    public SearchReviewResultDTO searchReviews(String q, String sort) {
        SearchReviewResultDTO reviewResultDTO = new SearchReviewResultDTO();
        reviewResultDTO.setQuery(q);

        List<SearchReviewInterfaceDTO> reviewInterfaceList;
        if ("rating".equals(sort)) {
            reviewInterfaceList = reviewRepository.findByReviewSearchOrderByRating(q);
        } else {
            reviewInterfaceList = reviewRepository.findByReviewSearchOrderByUpdatedAt(q);
        }

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

package com.kosa.mini.api.service.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchReviewResultDTO;
import com.kosa.mini.api.dto.search.SearchStoreResultDTO;

public interface SearchApiService {
    SearchStoreResultDTO searchStore(String q, String sort);
    SearchReviewResultDTO searchReviews(String q, String sort);
}

package com.kosa.mini.api.service.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchReviewResultDTO;
import com.kosa.mini.api.dto.search.SearchStoreResultDTO;

public interface SearchApiService {
    SearchStoreResultDTO searchStore(SearchDTO searchDTO);
    SearchReviewResultDTO searchReviews(SearchDTO searchDTO);
}

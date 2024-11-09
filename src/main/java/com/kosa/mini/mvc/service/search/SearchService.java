package com.kosa.mini.mvc.service.search;

import com.kosa.mini.mvc.domain.search.SearchResultDTO;

public interface SearchService {
    SearchResultDTO search(String query, String sort, String type);
}

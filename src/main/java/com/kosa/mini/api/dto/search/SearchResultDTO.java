package com.kosa.mini.api.dto.search;

import com.kosa.mini.api.dto.home.StoreDTO;
import com.kosa.mini.mvc.domain.store.StoreReviewDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultDTO {
    private String query;
    private List<StoreDTO> storeResults;
    private List<StoreReviewDTO> reviewResults;
    private int storeCount;
    private int reviewCount;
}

package com.kosa.mini.api.dto.search;


import com.kosa.mini.api.dto.review.StoreReviewDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchStoreResultDTO {
    private String query;
    private List<SearchStoreDTO> storeResults;
    private int storeCount;
}

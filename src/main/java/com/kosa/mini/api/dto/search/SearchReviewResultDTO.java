package com.kosa.mini.api.dto.search;


import com.kosa.mini.api.dto.review.StoreReviewDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchReviewResultDTO {
    private String query;
    private List<StoreReviewDTO> reviewResults;
    private int reviewCount;
}

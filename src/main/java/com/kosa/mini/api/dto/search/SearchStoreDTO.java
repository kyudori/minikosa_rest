package com.kosa.mini.api.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchStoreDTO {
    private Integer storeId;
    private String storeName;
    private String storePhoto;
    private String storeDescription;
    private Double ratingAvg;
}

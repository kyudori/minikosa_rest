package com.kosa.mini.api.domain.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private Long storeId;
    private String storeName;
    private String storePhoto;
    private String storeDescription;
    private double ratingAvg;
    private String categoryName;

}

package com.kosa.mini.api.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public interface SearchStoreInterfaceDTO {
    Integer getStoreId();
    String getStoreName();
    String getStorePhoto();
    String getStoreDescription();
    Double getRatingAvg();
}

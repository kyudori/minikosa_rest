package com.kosa.mini.api.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreContentCategoryDTO {
   private Integer storeId;
   private String storeName;
   private String postcode;
   private String roadAddress;
   private String detailAddress;
   private String extraAddress;
   private String storeDescription;
   private String websiteInfo;
   private LocalTime openingTime;
   private LocalTime closingTime;
   private String contactNumber;
   private String storePhoto;
   private Integer ownerMemberId;
   private Double ratingAvg;
   private Long countReview;
   private Integer categoryId;
}

package com.kosa.mini.api.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class StoreContentDTO {
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
}

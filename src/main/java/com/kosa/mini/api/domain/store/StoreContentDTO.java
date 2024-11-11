package com.kosa.mini.api.domain.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreContentDTO {
   private Long storeId;
   private String storeName;
   private String postcode;
   private String roadAddress;
   private String detailAddress;
   private String extraAddress;
   private String storeDescription;
   private String webSiteInfo;
   private Date openingTime;
   private Date closingTime;
   private String contactNumber;
   private String storePhoto;
   private Long ownerId;
   private double ratingAvg;
   private Long countReview;
//   private List<MenuDTO> menuDTO;
}

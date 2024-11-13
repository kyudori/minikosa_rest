package com.kosa.mini.api.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreContentDTO {
   private Integer storeId;
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
   private Integer ownerId;
   private double ratingAvg;
   private Long countReview;
//   private List<MenuDTO> menuDTO;
}

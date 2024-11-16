package com.kosa.mini.api.dto.store;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCreateDTO {
    private Integer storeId;
    private String storeName;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
    private String extraAddress;
    private Integer categoryId;
    private MultipartFile storePhoto;
    private String storePhotoUrl;
    private String storeDescription;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String websiteInfo;
    private String contactNumber;
    private List<MenuCreateDTO> menus;
}

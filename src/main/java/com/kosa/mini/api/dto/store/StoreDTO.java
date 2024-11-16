package com.kosa.mini.api.dto.store;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Data
public class StoreDTO {
    private Integer storeId;
    private String storeName;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
    private String extraAddress;
    private Integer categoryId;
    private MultipartFile storePhoto; // 가게 사진 업로드
    private String storePhotoPath; // 가게 사진 이름
    private String storeDescription;
    private Integer ownerId;

    @DateTimeFormat(pattern = "H:mm")
    private LocalTime openingTime;

    @DateTimeFormat(pattern = "H:mm")
    private LocalTime closingTime;

    private String websiteInfo;
    private String contactNumber;

    // 메뉴 리스트
    private List<MenuUploadDTO> menuUploadDTOS;
}

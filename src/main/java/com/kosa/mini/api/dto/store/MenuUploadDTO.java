package com.kosa.mini.api.dto.store;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MenuUploadDTO {
        private Integer menuId;
        private Integer storeId;
        private String menuName;
        private int price;
        private MultipartFile menuPhoto;
}

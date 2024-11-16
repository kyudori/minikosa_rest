package com.kosa.mini.api.dto.store;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCreateDTO {
    private String menuName;
    private Integer price;
    private MultipartFile menuPhoto;
    private String menuPhotoUrl;
}

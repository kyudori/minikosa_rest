package com.kosa.mini.api.dto.store;

import com.kosa.mini.api.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MenuDTO {
    private Integer menuId;
    private Integer storeId;
    private String menuName;
    private Integer price;
    private String menuPhoto;

    public Menu toEntity() {
        return Menu.builder()
                .menuId(menuId)
                .menuName(menuName)
                .menuPhoto(menuPhoto)
                .build();
    }

}

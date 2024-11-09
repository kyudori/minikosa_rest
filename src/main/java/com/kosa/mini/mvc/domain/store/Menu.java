package com.kosa.mini.mvc.domain.store;

import lombok.Data;

@Data
public class Menu {
    private Long menuId;
    private Long storeId;
    private String menuName;
    private Integer price;
    private String menuPhoto;
}

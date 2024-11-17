package com.kosa.mini.api.dto.store;

import com.kosa.mini.api.entity.Menu;
import com.kosa.mini.api.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuAdminDTO {
        private Integer storeId;
        private String menuName;
        private int price;
        private String menuPhoto;

        public Menu toEntity(Store store) {
                return Menu.builder()
                        .store(store)
                        .menuName(menuName)
                        .price(price)
                        .menuPhoto(menuPhoto)
                        .build();
        }

        public MenuAdminDTO fromEntity(Menu menu) {
                return new MenuAdminDTO(
                        menu.getStore().getStoreId(),
                        menu.getMenuName(),
                        menu.getPrice(),
                        menu.getMenuPhoto());
        }
}

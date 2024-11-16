package com.kosa.mini.api.dto.store;

import com.kosa.mini.api.entity.Menu;
import com.kosa.mini.api.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuCreateDTO {
        private Integer storeId;
        private String menuName;
        private int price;

        public Menu toEntity(Store store) {
                return Menu.builder()
                        .store(store)
                        .menuName(menuName)
                        .price(price)
                        .build();
        }

        public MenuCreateDTO fromEntity(Menu menu) {
                return new MenuCreateDTO(
                        menu.getStore().getStoreId(),
                        menu.getMenuName(),
                        menu.getPrice());
        }
}

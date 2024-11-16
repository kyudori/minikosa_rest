package com.kosa.mini.api.service.menu;

import com.kosa.mini.api.dto.store.MenuCreateDTO;
import com.kosa.mini.api.dto.store.MenuDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuApiService {
    List<MenuDTO> getMenu(Integer storeId);
    String getMenusImages(MultipartFile menuPhoto, Integer storeId) throws Exception;
    MenuCreateDTO createMenu(MenuCreateDTO menuCreateDTO, Integer storeId);
}

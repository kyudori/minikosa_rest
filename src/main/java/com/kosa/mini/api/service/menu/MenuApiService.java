package com.kosa.mini.api.service.menu;

import com.kosa.mini.api.dto.store.MenuAdminDTO;
import com.kosa.mini.api.dto.store.MenuDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuApiService {
    List<MenuDTO> getMenu(Integer storeId);
    String getMenusImages(MultipartFile menuPhoto, Integer menuId) throws Exception;
    MenuAdminDTO createMenu(Integer storeId, MultipartFile menuPhoto, MenuAdminDTO menuCreateDTO) throws Exception;
    MenuAdminDTO updateStoreMenus(Integer menuId, MultipartFile menuPhoto, MenuAdminDTO menuAdminDTO) throws Exception;
    Boolean deleteStoreMenus(Integer menuId);
}

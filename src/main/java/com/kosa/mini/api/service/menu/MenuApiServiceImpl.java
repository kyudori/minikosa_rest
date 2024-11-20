package com.kosa.mini.api.service.menu;


import com.kosa.mini.api.dto.store.MenuAdminDTO;
import com.kosa.mini.api.dto.store.MenuDTO;
import com.kosa.mini.api.entity.Menu;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.repository.MenuRepository;
import com.kosa.mini.api.repository.StoreRepository;
import com.kosa.mini.api.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuApiServiceImpl implements MenuApiService {
    private final int NO_IMAGE = 400;
    private final String DIRECTORY_NAME = "menu";

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getMenu(Integer storeId) {
        List<Menu> entity = menuRepository.findByStoreStoreId(storeId);
        List<MenuDTO> list = entity.stream()
                .map(menu -> MenuDTO.builder()
                        .menuId(menu.getMenuId())
                        .storeId(menu.getStore().getStoreId())
                        .price(menu.getPrice())
                        .menuName(menu.getMenuName())
                        .menuPhoto(menu.getMenuPhoto())
                        .build()
                ).collect(Collectors.toList());
        return list;
    }

    @Override
    public String getMenusImages(MultipartFile menuPhoto, Integer menuId) throws Exception {
        String oldPath = menuRepository.getMenuPhoto(menuId);
        Menu menu;
        if (oldPath != null) {
            String newFileName = fileStorageService.findByFile(oldPath, menuPhoto, DIRECTORY_NAME);
            menu = menuRepository.findById(menuId).get();
            menu.setMenuPhoto(newFileName);
            menuRepository.save(menu);
            return newFileName;
        } else {
            String newPath = fileStorageService.storeFile(menuPhoto, DIRECTORY_NAME);
            menu = menuRepository.findById(menuId).get();
            menu.setMenuPhoto(menuPhoto.getName());
            menuRepository.save(menu);
            return newPath;
        }
    }

    @Override
    public MenuAdminDTO createMenu(Integer storeId,
                                   MultipartFile menuPhoto,
                                   MenuAdminDTO menuCreateDTO) throws Exception {
        String newPhotoName = fileStorageService.storeFile(menuPhoto, DIRECTORY_NAME);
        Store store = new Store();
        store.setStoreId(storeId);
        menuCreateDTO.setMenuPhoto("/uploads/menus/"+ newPhotoName);
        Menu menu = menuCreateDTO.toEntity(store);
        menuRepository.save(menu);
        menuCreateDTO.fromEntity(menu);

        return menuCreateDTO;
    }

    @Override
    public MenuAdminDTO updateStoreMenus(Integer menuId,
                                         MultipartFile menuPhoto,
                                         MenuAdminDTO menuAdminDTO) throws Exception {

        String newPhotoName = getMenusImages(menuPhoto, menuId);
        menuAdminDTO.setMenuPhoto(newPhotoName);
        Menu menu = menuRepository.findById(menuId).get();
        Store store = menu.getStore();
        menu = menuAdminDTO.toEntity(store);
        menuRepository.save(menu);
        menuAdminDTO.fromEntity(menu);

        return menuAdminDTO;
    }

    @Override
    public Boolean deleteStoreMenus(Integer menuId) {

        Menu menu = menuRepository.findById(menuId).get();
        String getMenuPhoto = menu.getMenuPhoto();
        boolean result;
        if (getMenuPhoto != null) {
            result = fileStorageService.deleteImage(getMenuPhoto, DIRECTORY_NAME);
            if(result == false) {
                return result;
            }
        }
        menuRepository.deleteById(menuId);
        return true;
    }
}

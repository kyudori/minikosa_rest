package com.kosa.mini.api.service.menu;


import com.kosa.mini.api.dto.store.MenuCreateDTO;
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
//        store 찾는 것
//        String oldPath = storeRepository.getStorePhoto(storeId);
        String oldPath = menuRepository.getMenuPhoto(menuId);
        String directoryName = "menu";
        Menu menu;
        if (oldPath != null) {
            String newFileName = fileStorageService.findByFile(oldPath, menuPhoto, directoryName);
            menu = menuRepository.findById(menuId).get();
            menu.setMenuPhoto(newFileName);
            menuRepository.save(menu);
            return newFileName;
        } else {
            String newPath = fileStorageService.storeFile(menuPhoto, directoryName);
            menu = menuRepository.findById(menuId).get();
            menu.setMenuPhoto(menuPhoto.getName());
            menuRepository.save(menu);
            return "DB에 처음 이미지 저장";
        }
    }

    @Override
    public MenuCreateDTO createMenu(MenuCreateDTO menuCreateDTO, Integer storeId) {
        Store store = new Store();
        store.setStoreId(storeId);
        Menu menu = menuCreateDTO.toEntity(store);
        menuRepository.save(menu);
        menuCreateDTO.fromEntity(menu);

        return menuCreateDTO;
    }
}

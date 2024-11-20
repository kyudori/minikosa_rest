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
//        String oldPath = menuRepository.getMenuPhoto(menuId);
//        Menu menu;
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new Exception("메뉴를 찾을 수 없습니다."));
        // 새로운 사진이 업로드된 경우
        if (menuPhoto != null && !menuPhoto.isEmpty()) {
            // 기존 사진 삭제
            String oldPhotoPath = menu.getMenuPhoto();
            if (oldPhotoPath != null) {
                fileStorageService.deleteImage(oldPhotoPath, DIRECTORY_NAME);
            }
            // 새로운 사진 저장
            String newFileName = fileStorageService.storeFile(menuPhoto, DIRECTORY_NAME);
            menu.setMenuPhoto("/uploads/menus/" + newFileName);
            menuRepository.save(menu);
            return menu.getMenuPhoto();
        }  else {
            // 사진을 변경하지 않는 경우
            return menu.getMenuPhoto();
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
        // 메뉴 엔티티 가져오기
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new Exception("메뉴를 찾을 수 없습니다."));
        Store store = menu.getStore();

        // 메뉴 정보 업데이트
        menu.setMenuName(menuAdminDTO.getMenuName());
        menu.setPrice(menuAdminDTO.getPrice());

        // 사진 처리
        if (menuPhoto != null && !menuPhoto.isEmpty()) {
            // 기존 사진 삭제
            String oldPhotoPath = menu.getMenuPhoto();
            if (oldPhotoPath != null) {
                fileStorageService.deleteImage(oldPhotoPath, DIRECTORY_NAME);
            }
            // 새로운 사진 저장
            String newFileName = fileStorageService.storeFile(menuPhoto, DIRECTORY_NAME);
            menu.setMenuPhoto("/uploads/menus/" + newFileName);
        }

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

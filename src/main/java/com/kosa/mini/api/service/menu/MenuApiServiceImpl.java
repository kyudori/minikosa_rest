package com.kosa.mini.api.service.menu;


import com.kosa.mini.api.dto.store.MenuDTO;
import com.kosa.mini.api.entity.Menu;
import com.kosa.mini.api.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuApiServiceImpl implements MenuApiService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<MenuDTO> getMenu(Integer storeId) {
        List<Menu> entity = menuRepository.findByStoreStoreId(storeId);
        List<MenuDTO> list = entity.stream()
                .map(menu -> MenuDTO.builder()
                        .menuId(menu.getMenuId())
                        .price(menu.getPrice())
                        .menuName(menu.getMenuName())
                        .menuPhoto(menu.getMenuPhoto())
                        .build()
                ).collect(Collectors.toList());
        return list;
    }
}

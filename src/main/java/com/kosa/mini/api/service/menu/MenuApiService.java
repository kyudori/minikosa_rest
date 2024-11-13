package com.kosa.mini.api.service.menu;

import com.kosa.mini.api.dto.store.MenuDTO;
import com.kosa.mini.api.entity.Menu;

import java.util.List;

public interface MenuApiService {
    List<MenuDTO> getMenu(Integer storeId);
}

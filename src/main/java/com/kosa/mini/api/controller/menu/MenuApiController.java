package com.kosa.mini.api.controller.menu;


import com.kosa.mini.api.dto.store.MenuDTO;
import com.kosa.mini.api.entity.Menu;
import com.kosa.mini.api.service.menu.MenuApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MenuApiController {

    private final MenuApiService menuService;

    @GetMapping("/menu/{storeId}")
    public ResponseEntity<List<MenuDTO>> getStoreMenus(@PathVariable Integer storeId){
            List<MenuDTO> menuList = menuService.getMenu(storeId);
        return new ResponseEntity<>(menuList, HttpStatus.OK);
    }


}

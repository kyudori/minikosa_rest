package com.kosa.mini.api.service.menu;

import com.kosa.mini.api.dto.store.MenuDTO;
import com.kosa.mini.api.entity.Menu;
import com.kosa.mini.api.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MenuApiServiceImplTest {

    @Autowired
    MenuRepository menuRepository;


    @Test
    void getMenu() {
        List<Menu> list = menuRepository.findByStoreStoreId(33);
        List<MenuDTO> menu;
        list.stream().forEach(System.out::println);
    }
}
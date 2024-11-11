package com.kosa.mini.api.service.store;

import com.kosa.mini.api.domain.store.StoreContentDTO;
import com.kosa.mini.api.entity.Category;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.repository.StoreRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreApiServiceTest {

    @Autowired
    StoreRepository storeRepository;

    //@Test
    void findStoreWithContent() {
        Long id = 30L;
        StoreContentDTO  storeContentDTO = storeRepository.findStoreWithContent(id);
        System.out.println(storeContentDTO.toString());
    }
    @Test
    void findStore() {
        Long id = 33L;

        Optional<Store> store = storeRepository.findById(id);
        Category category = store.getCategory();
        Hibernate.initialize(category);
        StoreContentDTO dto  = new StoreContentDTO();

        System.out.println(store.toString());
    }

}
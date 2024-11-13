package com.kosa.mini.api.repository;

import com.kosa.mini.api.dto.store.MenuDTO;
import com.kosa.mini.api.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MenuRepository extends JpaRepository<Menu,Integer> {
    List<Menu> findByStoreStoreId(Integer id);

}

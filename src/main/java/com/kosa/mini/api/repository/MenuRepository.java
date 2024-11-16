package com.kosa.mini.api.repository;

import com.kosa.mini.api.dto.store.MenuDTO;
import com.kosa.mini.api.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MenuRepository extends JpaRepository<Menu,Integer> {
    List<Menu> findByStoreStoreId(Integer id);
    @Query("SELECT m.menuPhoto FROM Menu m WHERE m.menuId = :menuId")
    String getMenuPhoto(Integer menuId);
}

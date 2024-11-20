package com.kosa.mini.api.controller.store;

import com.kosa.mini.api.dto.store.StoreContentCategoryDTO;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.service.store.StoreApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StoreContentApiController {
    private final StoreApiService storeApiService;

    @GetMapping("/store/{id}")
    public ResponseEntity<StoreContentCategoryDTO> storeInfo(@PathVariable Integer id){
        try {
            StoreContentCategoryDTO storeContentDTO = storeApiService.storeInfo(id);
            return ResponseEntity.ok(storeContentDTO);
        } catch (StoreNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}

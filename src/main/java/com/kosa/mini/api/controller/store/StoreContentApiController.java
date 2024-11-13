package com.kosa.mini.api.controller.store;

import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.service.store.StoreApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StoreContentApiController {
    @Autowired
    private StoreApiService storeApiService;


    @GetMapping("/store/{id}")
    public StoreContentDTO storeInfo(@PathVariable Integer id){
        return storeApiService.storeInfo(id);
    }
}

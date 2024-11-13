package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreApiServiceImpl implements StoreApiService {

    @Autowired
    StoreRepository storeRepository;

    public StoreContentDTO storeInfo(Integer id) {
        StoreContentDTO storeContentDTO = storeRepository.findStoreWithContent(id);
        return storeContentDTO;
    }
}

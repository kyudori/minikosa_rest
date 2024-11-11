package com.kosa.mini.api.service.store;

import com.kosa.mini.api.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreApiService {
    @Autowired
    StoreRepository storeRepository;
}

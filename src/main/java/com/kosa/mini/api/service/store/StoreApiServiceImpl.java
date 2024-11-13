package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreApiServiceImpl implements StoreApiService {

    @Autowired
    StoreRepository storeRepository;

    @Override
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정
    public StoreContentDTO storeInfo(Integer id) {
        StoreContentDTO storeContentDTO = storeRepository.findStoreWithContent(id);
        if (storeContentDTO == null) {
            throw new StoreNotFoundException("Store not found with id: " + id);
        }
        return storeContentDTO;
    }
}

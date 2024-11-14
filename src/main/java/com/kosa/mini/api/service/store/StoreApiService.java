package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.StoreNotFoundException;

import java.util.List;


public interface StoreApiService {

    public StoreContentDTO storeInfo(Integer id);
    List<String> searchEmailsByPartialEmail(String email);
    List<String> searchStoreNamesByPartialName(String storeName);
    void deleteStore(Integer storeId) throws StoreNotFoundException;
}

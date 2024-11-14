package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.dto.store.StoreSearchDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.StoreNotFoundException;

import java.util.List;


public interface StoreApiService {

    public StoreContentDTO storeInfo(Integer id);
    List<UserSearchDTO> searchUsersByEmail(String email);

    List<StoreSearchDTO> searchStoresByName(String storeName);
    void deleteStore(Integer storeId) throws StoreNotFoundException;
}

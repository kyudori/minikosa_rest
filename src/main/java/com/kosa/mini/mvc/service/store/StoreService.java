package com.kosa.mini.mvc.service.store;

import com.kosa.mini.mvc.domain.member.Member;
import com.kosa.mini.mvc.domain.store.MenuDTO;
import com.kosa.mini.mvc.domain.store.Store;
import com.kosa.mini.mvc.domain.store.StoreDTO;

import java.util.List;

public interface StoreService {
    void createStore(StoreDTO storeDTO, List<MenuDTO> menuDTOs) throws Exception;

    void updateStore(StoreDTO storeDTO, List<MenuDTO> menuDTOs) throws Exception;

    void assignOwnerAndUpdateRole(Long storeId, Long ownerId);

    List<Member> searchMembersByEmail(String email);

    List<StoreDTO> searchStoresByName(String storeName);

    Store getStoreByOwnerId(Long owerId);

    StoreDTO storeInfo(long storeId);
}

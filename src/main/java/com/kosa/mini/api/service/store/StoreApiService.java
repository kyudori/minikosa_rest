package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.request.AssignOwnerRequest;
import com.kosa.mini.api.dto.response.AssignOwnerResponse;
import com.kosa.mini.api.dto.store.StoreContentCategoryDTO;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.dto.store.StoreCreateDTO;
import com.kosa.mini.api.dto.store.StoreSearchDTO;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface StoreApiService {

    StoreContentCategoryDTO storeInfo(Integer id);
    List<UserSearchDTO> searchUsersByEmail(String email);

    List<StoreSearchDTO> searchStoresByName(String storeName);
    void deleteStore(Integer storeId) throws StoreNotFoundException;

    AssignOwnerResponse assignOwnerToStore(AssignOwnerRequest request) throws ResourceNotFoundException, IllegalArgumentException;

    StoreCreateDTO createStore(StoreCreateDTO storeDTO, MultipartFile storePhoto) throws Exception;
    StoreCreateDTO updateStore(Integer storeId, StoreCreateDTO storeDTO, MultipartFile storePhoto) throws Exception;

}

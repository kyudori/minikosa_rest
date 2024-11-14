package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.member.MemberDTO;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.dto.store.StoreDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreApiServiceImpl implements StoreApiService {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정
    public StoreContentDTO storeInfo(Integer id) {
        StoreContentDTO storeContentDTO = storeRepository.findStoreWithContent(id);
        if (storeContentDTO == null) {
            throw new StoreNotFoundException("Store not found with id: " + id);
        }
        return storeContentDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> searchEmailsByPartialEmail(String email) {
        return memberRepository.findEmailsByEmailContainingIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> searchStoreNamesByPartialName(String storeName) {
        return storeRepository.findStoreNamesByStoreNameContainingIgnoreCase(storeName);
    }

    @Override
    @Transactional
    public void deleteStore(Integer storeId) throws StoreNotFoundException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + storeId));
        storeRepository.delete(store);
    }
}

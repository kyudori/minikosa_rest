package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.member.MemberDTO;
import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.dto.store.StoreDTO;
import com.kosa.mini.api.dto.store.StoreSearchDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserSearchDTO> searchUsersByEmail(String email) {
        List<Member> members = memberRepository.findByEmailContainingIgnoreCase(email);
        return members.stream()
                .map(member -> UserSearchDTO.builder()
                        .name(member.getName())
                        .nickname(member.getNickname())
                        .email(member.getEmail())
                        .phoneNumber(member.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreSearchDTO> searchStoresByName(String storeName) {
        List<Store> stores = storeRepository.findByStoreNameContainingIgnoreCase(storeName);
        return stores.stream()
                .map(store -> StoreSearchDTO.builder()
                        .storeName(store.getStoreName())
                        .roadAddress(store.getRoadAddress())
                        .contactNumber(store.getContactNumber())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void deleteStore(Integer storeId) throws StoreNotFoundException {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + storeId));
        storeRepository.delete(store);
    }
}

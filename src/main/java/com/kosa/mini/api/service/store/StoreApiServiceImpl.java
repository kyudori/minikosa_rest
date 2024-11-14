package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.request.AssignOwnerRequest;
import com.kosa.mini.api.dto.response.AssignOwnerResponse;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.dto.store.StoreSearchDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Role;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RoleRepository;
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
    @Autowired
    private RoleRepository roleRepository;

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
                        .memberId(member.getMemberId())
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
                        .storeId(store.getStoreId())
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

    @Override
    @Transactional
    public AssignOwnerResponse assignOwnerToStore(AssignOwnerRequest request) throws ResourceNotFoundException, IllegalArgumentException {
        // 가게 조회
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + request.getStoreId()));

        // 이미 사장이 있는지 확인
        //if (store.getOwner() != null) {
        //    throw new IllegalArgumentException("이미 사장님이 등록된 가게입니다.");
        //}

        // 회원 조회
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + request.getMemberId()));

        // 회원의 현재 역할 조회
        Role ownerRole = roleRepository.findByRoleName("ROLE_OWNER")
                .orElseThrow(() -> new ResourceNotFoundException("ROLE_OWNER를 찾을 수 없습니다."));

        // 회원의 역할이 이미 ROLE_OWNER인지 확인
        //if (member.getRole().getRoleName().equals("ROLE_OWNER")) {
        //    throw new IllegalArgumentException("이미 ROLE_OWNER 권한을 가진 사용자입니다.");
        //}

        // 회원의 역할이 ROLE_ADMIN인지 확인
        if (member.getRole().getRoleName().equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("ROLE_ADMIN 권한을 가진 사용자는 사장으로 등록할 수 없습니다.");
        }


        // 회원의 역할을 ROLE_OWNER로 변경
        member.setRole(ownerRole);
        memberRepository.save(member);

        // 가게의 소유주로 회원을 지정
        store.setOwner(member);
        storeRepository.save(store);

        // 응답 DTO 생성
        AssignOwnerResponse response = AssignOwnerResponse.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .ownerId(member.getMemberId())
                .ownerName(member.getName())
                .message("사장님이 성공적으로 등록되었습니다.")
                .build();

        return response;
    }
}

package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.request.AssignOwnerRequest;
import com.kosa.mini.api.dto.response.AssignOwnerResponse;
import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.dto.store.StoreCreateDTO;
import com.kosa.mini.api.dto.store.StoreSearchDTO;
import com.kosa.mini.api.entity.*;
import com.kosa.mini.api.exception.FileStorageException;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.CategoryRepository;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RoleRepository;
import com.kosa.mini.api.repository.StoreRepository;
import com.kosa.mini.api.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileStorageService fileStorageService;

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

    @Override
    @Transactional
    public StoreCreateDTO createStore(StoreCreateDTO storeDTO, MultipartFile storePhoto) throws Exception {
        // 가게 정보 저장
        Store store = new Store();
        store.setStoreName(storeDTO.getStoreName());
        store.setPostcode(storeDTO.getPostcode());
        store.setRoadAddress(storeDTO.getRoadAddress());
        store.setDetailAddress(storeDTO.getDetailAddress());
        store.setExtraAddress(storeDTO.getExtraAddress());
        store.setStoreDescription(storeDTO.getStoreDescription());
        store.setWebsiteInfo(storeDTO.getWebsiteInfo());
        store.setContactNumber(storeDTO.getContactNumber());

        // 카테고리 설정
        Category category = categoryRepository.findById(storeDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + storeDTO.getCategoryId()));
        store.setCategory(category);

        // 영업 시간 설정
        store.setOpeningTime(storeDTO.getOpeningTime());
        store.setClosingTime(storeDTO.getClosingTime());

        // 가게 사진 저장
        if (storePhoto != null && !storePhoto.isEmpty()) {
            try {
                String storePhotoName = fileStorageService.storeFile(storePhoto, "store");
                store.setStorePhoto("/uploads/stores/"+storePhotoName);
            } catch (Exception e) {
                throw new FileStorageException("가게 사진 저장 실패: " + e.getMessage(), e);
            }
        }

        // 소유자 설정 (초기에는 null 또는 특정 관리자)
        store.setOwner(null);

        // 저장 시간 설정
        store.setCreatedAt(java.time.LocalDateTime.now());
        store.setUpdatedAt(java.time.LocalDateTime.now());
        store.setIsModified(false);

        // 저장
        Store savedStore = storeRepository.save(store);

        // StoreCreateDTO에 저장된 storeId와 storePhotoPath 설정
        StoreCreateDTO responseDTO = new StoreCreateDTO();
        responseDTO.setStoreId(savedStore.getStoreId());
        responseDTO.setStoreName(savedStore.getStoreName());
        responseDTO.setPostcode(savedStore.getPostcode());
        responseDTO.setRoadAddress(savedStore.getRoadAddress());
        responseDTO.setDetailAddress(savedStore.getDetailAddress());
        responseDTO.setExtraAddress(savedStore.getExtraAddress());
        responseDTO.setCategoryId(savedStore.getCategory().getCategoryId());
        responseDTO.setStorePhotoPath(savedStore.getStorePhoto());
        responseDTO.setStoreDescription(savedStore.getStoreDescription());
        responseDTO.setOwnerId(savedStore.getOwner() != null ? savedStore.getOwner().getMemberId() : null);
        responseDTO.setOpeningTime(savedStore.getOpeningTime());
        responseDTO.setClosingTime(savedStore.getClosingTime());
        responseDTO.setWebsiteInfo(savedStore.getWebsiteInfo());
        responseDTO.setContactNumber(savedStore.getContactNumber());

        return responseDTO;
    }

    @Override
    @Transactional
    public StoreCreateDTO updateStore(Integer storeId, StoreCreateDTO storeDTO, MultipartFile storePhoto) throws Exception {
        // 기존 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("Store not found with id: " + storeId));

        // 가게 정보 업데이트
        store.setStoreName(storeDTO.getStoreName());
        store.setPostcode(storeDTO.getPostcode());
        store.setRoadAddress(storeDTO.getRoadAddress());
        store.setDetailAddress(storeDTO.getDetailAddress());
        store.setExtraAddress(storeDTO.getExtraAddress());
        store.setStoreDescription(storeDTO.getStoreDescription());
        store.setWebsiteInfo(storeDTO.getWebsiteInfo());
        store.setContactNumber(storeDTO.getContactNumber());

        // 카테고리 업데이트
        Category category = categoryRepository.findById(storeDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + storeDTO.getCategoryId()));
        store.setCategory(category);

        // 영업 시간 업데이트
        store.setOpeningTime(storeDTO.getOpeningTime());
        store.setClosingTime(storeDTO.getClosingTime());

        // 가게 사진 업데이트
        if (storePhoto != null && !storePhoto.isEmpty()) {
            try {
                String dbImage = storeRepository.getStorePhoto(storeId);
                String storePhotoName = fileStorageService.findByFile(dbImage, storePhoto, "store");
                store.setStorePhoto("/uploads/stores/"+storePhotoName);
            } catch (Exception e) {
                throw new FileStorageException("가게 사진 저장 실패: " + e.getMessage(), e);
            }
        }

        // 업데이트 시간 설정
        store.setUpdatedAt(java.time.LocalDateTime.now());
        store.setIsModified(true);

        // 메뉴 업데이트
        // 기존 메뉴를 삭제하고 새로 추가하는 방식
//        store.getMenus().clear();
//
//        if (storeDTO.getMenuUploadDTOS() != null) {
//            List<Menu> menus = storeDTO.getMenuUploadDTOS().stream().map(menuDTO -> {
//                Menu menu = new Menu();
//                menu.setMenuName(menuDTO.getMenuName());
//                menu.setPrice(menuDTO.getPrice());
//                menu.setStore(store);
//
//                MultipartFile menuPhoto = menuDTO.getMenuPhoto();
//                if (menuPhoto != null && !menuPhoto.isEmpty()) {
//                    try {
//                        String menuPhotoName = fileStorageService.storeFile(menuPhoto, "menu");
//                        menu.setMenuPhoto(menuPhotoName);
//                    } catch (Exception e) {
//                        throw new RuntimeException("메뉴 사진 저장 실패: " + e.getMessage());
//                    }
//                }
//
//                return menu;
//            }).collect(Collectors.toList());
//
//            store.setMenus(menus.stream().collect(Collectors.toSet()));
//        }

        // 저장
        Store updatedStore = storeRepository.save(store);

        return storeDTO;
    }
}

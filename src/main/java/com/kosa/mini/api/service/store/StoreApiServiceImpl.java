package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.request.AssignOwnerRequest;
import com.kosa.mini.api.dto.response.AssignOwnerResponse;
import com.kosa.mini.api.dto.store.*;
import com.kosa.mini.api.entity.*;
import com.kosa.mini.api.exception.FileStorageException;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
import com.kosa.mini.api.repository.*;
import com.kosa.mini.api.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StoreApiServiceImpl implements StoreApiService {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MemberRepository memberRepository;
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
    public StoreCreateDTO createStore(StoreCreateDTO storeCreateDTO) throws Exception {
        // 카테고리 조회
        Category category = categoryRepository.findById(storeCreateDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID: " + storeCreateDTO.getCategoryId()));

        // 가게 엔티티 생성
        Store store = Store.builder()
                .storeName(storeCreateDTO.getStoreName())
                .postcode(storeCreateDTO.getPostcode())
                .roadAddress(storeCreateDTO.getRoadAddress())
                .detailAddress(storeCreateDTO.getDetailAddress())
                .extraAddress(storeCreateDTO.getExtraAddress())
                .category(category)
                .storeDescription(storeCreateDTO.getStoreDescription())
                .openingTime(storeCreateDTO.getOpeningTime())
                .closingTime(storeCreateDTO.getClosingTime())
                .websiteInfo(storeCreateDTO.getWebsiteInfo())
                .contactNumber(storeCreateDTO.getContactNumber())
                .isModified(false)
                .build();

        // 가게 사진 저장
        if (storeCreateDTO.getStorePhoto() != null && !storeCreateDTO.getStorePhoto().isEmpty()) {
            try {
                String storePhotoFilename = fileStorageService.saveStorePhoto(storeCreateDTO.getStorePhoto());
                store.setStorePhoto(storePhotoFilename);
                storeCreateDTO.setStorePhotoUrl("/uploads/stores/" + storePhotoFilename);
            } catch (Exception e) {
                throw new FileStorageException("가게 사진 저장 실패: " + e.getMessage());
            }
        }

        // 메뉴 저장
        Set<Menu> menus = new HashSet<>();
        if (storeCreateDTO.getMenus() != null && !storeCreateDTO.getMenus().isEmpty()) {
            for (MenuCreateDTO menuCreateDTO : storeCreateDTO.getMenus()) {
                Menu menu = Menu.builder()
                        .menuName(menuCreateDTO.getMenuName())
                        .price(menuCreateDTO.getPrice())
                        .store(store)
                        .build();

                // 메뉴 사진 저장
                if (menuCreateDTO.getMenuPhoto() != null && !menuCreateDTO.getMenuPhoto().isEmpty()) {
                    try {
                        String menuPhotoFilename = fileStorageService.saveMenuPhoto(menuCreateDTO.getMenuPhoto());
                        menu.setMenuPhoto(menuPhotoFilename);
                    } catch (Exception e) {
                        throw new FileStorageException("메뉴 사진 저장 실패: " + e.getMessage());
                    }
                }

                menus.add(menu);
            }
        }
        store.setMenus(menus);

        // 가게 저장
        Store savedStore = storeRepository.save(store);

        // 저장된 가게 정보를 DTO로 반환
        storeCreateDTO.setStoreId(savedStore.getStoreId());
        return storeCreateDTO;
    }

    @Override
    @Transactional
    public StoreCreateDTO editStore(Integer storeId, StoreCreateDTO storeCreateDTO) throws Exception {
        // 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다. ID: " + storeId));

        // 업데이트할 필드 설정
        store.setStoreName(storeCreateDTO.getStoreName());
        store.setPostcode(storeCreateDTO.getPostcode());
        store.setRoadAddress(storeCreateDTO.getRoadAddress());
        store.setDetailAddress(storeCreateDTO.getDetailAddress());
        store.setExtraAddress(storeCreateDTO.getExtraAddress());

        // 카테고리 업데이트
        if (storeCreateDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(storeCreateDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID: " + storeCreateDTO.getCategoryId()));
            store.setCategory(category);
        }

        store.setStoreDescription(storeCreateDTO.getStoreDescription());
        store.setOpeningTime(storeCreateDTO.getOpeningTime());
        store.setClosingTime(storeCreateDTO.getClosingTime());
        store.setWebsiteInfo(storeCreateDTO.getWebsiteInfo());
        store.setContactNumber(storeCreateDTO.getContactNumber());
        store.setIsModified(true);

        // 가게 사진 업데이트
        if (storeCreateDTO.getStorePhoto() != null && !storeCreateDTO.getStorePhoto().isEmpty()) {
            try {
                String storePhotoFilename = fileStorageService.saveStorePhoto(storeCreateDTO.getStorePhoto());
                store.setStorePhoto(storePhotoFilename);
                storeCreateDTO.setStorePhotoUrl("/uploads/stores/" + storePhotoFilename);
            } catch (Exception e) {
                throw new FileStorageException("가게 사진 업데이트 실패: " + e.getMessage());
            }
        }

        // 메뉴 업데이트
        // 기존 메뉴 삭제
        menuRepository.deleteAll(store.getMenus());

        Set<Menu> updatedMenus = new HashSet<>();
        if (storeCreateDTO.getMenus() != null && !storeCreateDTO.getMenus().isEmpty()) {
            for (MenuCreateDTO menuCreateDTO : storeCreateDTO.getMenus()) {
                Menu menu = Menu.builder()
                        .menuName(menuCreateDTO.getMenuName())
                        .price(menuCreateDTO.getPrice())
                        .store(store)
                        .build();

                // 메뉴 사진 저장
                if (menuCreateDTO.getMenuPhoto() != null && !menuCreateDTO.getMenuPhoto().isEmpty()) {
                    try {
                        String menuPhotoFilename = fileStorageService.saveMenuPhoto(menuCreateDTO.getMenuPhoto());
                        menu.setMenuPhoto(menuPhotoFilename);
                        menuCreateDTO.setMenuPhotoUrl("/uploads/menus/" + menuPhotoFilename);
                    } catch (Exception e) {
                        throw new FileStorageException("메뉴 사진 업데이트 실패: " + e.getMessage());
                    }
                }

                updatedMenus.add(menu);
            }
        }
        store.setMenus(updatedMenus);

        // 가게 업데이트
        Store updatedStore = storeRepository.save(store);

        // 업데이트된 가게 정보를 DTO로 반환
        storeCreateDTO.setStoreId(updatedStore.getStoreId());
        return storeCreateDTO;
    }
}

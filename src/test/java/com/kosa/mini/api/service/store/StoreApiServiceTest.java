package com.kosa.mini.api.service.store;

import com.kosa.mini.api.domain.store.StoreContentDTO;
import com.kosa.mini.api.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreApiServiceTest {

    @Autowired
    StoreRepository storeRepository;

    @Test
    void findStoreWithContent() {
        Long id = 31L; // 테스트할 storeId를 설정하세요.
        StoreContentDTO storeContentDTO = storeRepository.findStoreWithContent(id);

        // DTO가 null이 아닌지 확인
        assertNotNull(storeContentDTO, "없는가게");

        // DTO의 주요 필드 출력
        System.out.println("Store ID: " + storeContentDTO.getStoreId());
        System.out.println("Store Name: " + storeContentDTO.getStoreName());
        System.out.println("Postcode: " + storeContentDTO.getPostcode());
        System.out.println("Road Address: " + storeContentDTO.getRoadAddress());
        System.out.println("Detail Address: " + storeContentDTO.getDetailAddress());
        System.out.println("Extra Address: " + storeContentDTO.getExtraAddress());
        System.out.println("Store Description: " + storeContentDTO.getStoreDescription());
        System.out.println("Website Info: " + storeContentDTO.getWebSiteInfo());
        System.out.println("Opening Time: " + storeContentDTO.getOpeningTime());
        System.out.println("Closing Time: " + storeContentDTO.getClosingTime());
        System.out.println("Contact Number: " + storeContentDTO.getContactNumber());
        System.out.println("Store Photo: " + storeContentDTO.getStorePhoto());
        System.out.println("Owner ID: " + storeContentDTO.getOwnerId());
        System.out.println("Rating Average: " + storeContentDTO.getRatingAvg());
        System.out.println("Review Count: " + storeContentDTO.getCountReview());
    }

    @Test
    @Transactional
    void findStore() {
        Long id = 31L;

        // findById를 사용하여 Optional<Store> 가져오기
        var storeOptional = storeRepository.findById(id);

        // 어설션 수정: store가 존재해야 함
        assertTrue(storeOptional.isPresent(), "Store should exist");

        // Store 객체 추출
        var store = storeOptional.get();

        // 리뷰를 통해 ratingAvg와 countReview 계산
        double ratingAvg = store.getReviews().stream()
                .mapToDouble(review -> review.getRating() != null ? review.getRating() : 0.0)
                .average()
                .orElse(0.0);
        long countReview = store.getReviews().size();

        // StoreContentDTO 출력
        System.out.println("Store ID: " + store.getStoreId());
        System.out.println("Store Name: " + store.getStoreName());
        System.out.println("Rating Average: " + ratingAvg);
        System.out.println("Review Count: " + countReview);

        // 추가적인 필드 검증이 필요하다면 여기에 추가
        // 예:
        // assertEquals("Expected Store Name", store.getStoreName());
    }
}

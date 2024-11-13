package com.kosa.mini.api.service.store;

import com.kosa.mini.api.dto.store.StoreContentDTO;
import com.kosa.mini.api.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreApiServiceTest {

    @Autowired
    StoreRepository storeRepository;

    @Test
    void findStoreWithContent() {
        Integer id = 33; // 테스트할 storeId를 설정하세요.
        StoreContentDTO storeContentDTO = storeRepository.findStoreWithContent(id);

        // DTO가 null이 아닌지 확인
        assertNotNull(storeContentDTO, "없는가게");

        System.out.println(storeContentDTO.toString());
        // DTO의 주요 필드 출력
//        System.out.println("Store ID: " + storeContentDTO.getStoreId());
//        System.out.println("Store Name: " + storeContentDTO.getStoreName());
//        System.out.println("Postcode: " + storeContentDTO.getPostcode());
//        System.out.println("Road Address: " + storeContentDTO.getRoadAddress());
//        System.out.println("Detail Address: " + storeContentDTO.getDetailAddress());
//        System.out.println("Extra Address: " + storeContentDTO.getExtraAddress());
//        System.out.println("Store Description: " + storeContentDTO.getStoreDescription());
//        System.out.println("Website Info: " + storeContentDTO.getWebSiteInfo());
//        System.out.println("Opening Time: " + storeContentDTO.getOpeningTime());
//        System.out.println("Closing Time: " + storeContentDTO.getClosingTime());
//        System.out.println("Contact Number: " + storeContentDTO.getContactNumber());
//        System.out.println("Store Photo: " + storeContentDTO.getStorePhoto());
//        System.out.println("Owner ID: " + storeContentDTO.getOwnerId());
//        System.out.println("Rating Average: " + storeContentDTO.getRatingAvg());
//        System.out.println("Review Count: " + storeContentDTO.getCountReview());
    }

}

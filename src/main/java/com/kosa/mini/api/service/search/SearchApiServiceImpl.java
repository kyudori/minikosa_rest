package com.kosa.mini.api.service.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchStoreDTO;
import com.kosa.mini.api.dto.search.SearchStoreResultDTO;
import com.kosa.mini.api.repository.HomeRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchApiServiceImpl implements SearchApiService {
    private final StoreRepository storeRepository;
    private final HomeRepository homeRepository;

    @Override
    public SearchStoreResultDTO searchStore(SearchDTO searchDTO) {
        SearchStoreResultDTO storeResultDTO = new SearchStoreResultDTO();
        storeResultDTO.setQuery(searchDTO.getQuery());


        // Initialize default sorts
//        Sort sort;
//        sort = Sort.by(Sort.Direction.DESC, "s.updated_at");
//        if ("rating".equals(searchDTO.getSort())) {
//            sort = Sort.by(Sort.Direction.DESC, "ratingAvg");
//        }
        // Initialize default sorts
        String storeSort = "s.updated_at DESC";  // 기본 정렬 기준

        // "rating"일 경우, "ratingAvg"로 정렬
        if ("rating".equals(searchDTO.getSort())) {
            storeSort = "ratingAvg DESC";  // 정렬 기준을 ratingAvg로 설정
        }

        System.out.println("searchDTO :" + searchDTO.toString());


        List<SearchStoreDTO> storeList = storeRepository.findByStoreSearch(searchDTO.getQuery(), storeSort);
        System.out.println(storeList.toString() + "=========================");
        storeResultDTO.setStoreResults(storeList);
        return storeResultDTO;
    }
}

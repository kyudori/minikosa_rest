package com.kosa.mini.api.service.search;

import com.kosa.mini.api.dto.search.SearchDTO;
import com.kosa.mini.api.dto.search.SearchStoreDTO;
import com.kosa.mini.api.dto.search.SearchStoreInterfaceDTO;
import com.kosa.mini.api.dto.search.SearchStoreResultDTO;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.repository.HomeRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchApiServiceImpl implements SearchApiService {
    private final StoreRepository storeRepository;
    private final HomeRepository homeRepository;

    @Override
    public SearchStoreResultDTO searchStore(SearchDTO searchDTO) {
        SearchStoreResultDTO storeResultDTO = new SearchStoreResultDTO();
        storeResultDTO.setQuery(searchDTO.getQuery());

        String storeSort = "s.updated_at DESC";  // 기본 정렬 기준

        // "rating"일 경우, "ratingAvg"로 정렬
        if ("rating".equals(searchDTO.getSort())) {
            storeSort = "ratingAvg DESC";  // 정렬 기준을 ratingAvg로 설정
        }

        List<SearchStoreInterfaceDTO> storeInterfaceList = storeRepository.findByStoreSearch(searchDTO.getQuery(), storeSort);
        List<SearchStoreDTO> storeList = storeInterfaceList.stream()
                .map(storeInterface -> new SearchStoreDTO(
                        storeInterface.getStoreId(),
                        storeInterface.getStoreName(),
                        storeInterface.getStorePhoto(),
                        storeInterface.getStoreDescription(),
                        storeInterface.getRatingAvg()))
                .collect(Collectors.toList());
        storeResultDTO.setStoreResults(storeList);
        storeResultDTO.setStoreCount(storeList.size());
        return storeResultDTO;
    }
}

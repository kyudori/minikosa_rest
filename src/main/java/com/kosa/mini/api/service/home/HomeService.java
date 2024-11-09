package com.kosa.mini.api.service.home;

import com.kosa.mini.api.domain.home.StoreDTO;
import com.kosa.mini.api.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    @Autowired
    HomeRepository homeRepository;

    public List<StoreDTO> home() {
        return homeRepository.index();
    }
}

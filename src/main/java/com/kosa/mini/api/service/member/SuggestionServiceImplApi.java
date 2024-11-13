package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.repository.SuggestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SuggestionServiceImplApi {

    @Autowired
    private final SuggestionRepository suggestionRepository; // 게시글 리퍼지토리 객체 주입

    public SuggestionServiceImplApi(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }
    public ContactUs create(ContactUsDTO dto) {
        ContactUs contactUs = ContactUsDTO.toEntity();
        log.info("서비스 엔티티: "+ contactUs.toString());
        return suggestionRepository.save(contactUs);
    }
    }


package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuggestionService {
    ContactUs create(ContactUsDTO dto);
    Page<ContactUs> getSuggestions(String type, String keyword, Pageable pageable);
}

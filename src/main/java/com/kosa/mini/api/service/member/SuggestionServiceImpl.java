package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.SuggestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public SuggestionServiceImpl(SuggestionRepository suggestionRepository, MemberRepository memberRepository) {
        this.suggestionRepository = suggestionRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public ContactUs create(ContactUsDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member ID " + dto.getMemberId() + " 존재하지 않음"));

        ContactUs contactUs = dto.toEntity(member);
        log.info("서비스 엔티티: " + contactUs.toString());
        return suggestionRepository.save(contactUs);
    }

    @Override
    public Page<ContactUs> getSuggestions(String type, String keyword, Pageable pageable) {
        if (type.equals("title")){
            return suggestionRepository.findByTitleContaining(keyword, pageable);
        }
        else {
            return suggestionRepository.findByContentContaining(keyword, pageable);
        }
    }
}

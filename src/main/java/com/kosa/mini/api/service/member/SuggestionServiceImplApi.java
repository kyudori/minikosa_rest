package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.SuggestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SuggestionServiceImplApi {

    @Autowired
    private final SuggestionRepository suggestionRepository; // 게시글 리퍼지토리 객체 주입
    @Autowired
    private final MemberRepository memberRepository;

    public SuggestionServiceImplApi(SuggestionRepository suggestionRepository, MemberRepository memberRepository) {
        this.suggestionRepository = suggestionRepository;
        this.memberRepository = memberRepository;
    }

    public ContactUs create(ContactUsDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member ID " + dto.getMemberId() + "존재하지 않음"));


        ContactUs contactUs = dto.toEntity(member);
        log.info("서비스 엔티티: "+ contactUs.toString());
        return suggestionRepository.save(contactUs);
    }
    }


package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RoleRepository;
import com.kosa.mini.api.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MemberEditProfileServiceImpl {

    @Autowired
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberEditProfileServiceImpl(MemberRepository memberRepository,
                                        RoleRepository roleRepository,
                                        PasswordEncoder passwordEncoder,
                                        StoreRepository storeRepository) {

        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member update(Integer memberId, MemberEditProfileDTO dto) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            log.info("잘못된 요청! id : {}", memberId);
            return null;
        }

        // 현재 비밀번호 확인
        if (dto.getCurrentPassword() == null || dto.getCurrentPassword().isEmpty()) {
            log.info("현재 비밀번호가 제공되지 않았습니다.");
            return null;
        }

        boolean isPasswordValid = passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword());
        if (!isPasswordValid) {
            log.info("현재 비밀번호가 일치하지 않습니다.");
            return null;
        }

        // 비밀번호 변경이 필요한 경우
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            member.setPassword(encodedPassword);
        }

        // 기타 정보 업데이트
        if (dto.getName() != null) {
            member.setName(dto.getName());
        }
        if (dto.getNickname() != null) {
            member.setNickname(dto.getNickname());
        }
        if (dto.getPhoneNumber() != null) {
            member.setPhoneNumber(dto.getPhoneNumber());
        }

        // 업데이트 및 저장
        Member updated = memberRepository.save(member);
        return updated;
    }

    // 비밀번호 확인 메소드 (회원 탈퇴 시 사용)
    public boolean verifyPassword(Integer memberId, String rawPassword) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, member.getPassword());
    }

    // 회원 탈퇴 메소드
    @Transactional
    public boolean deleteMember(Integer memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return false;
        }
        memberRepository.delete(member);
        return true;
    }

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}

package com.kosa.mini.api.service.member;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // 기존 이메일 기반 메서드
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Attempting to load user by email: {}", email);
        Member member = memberRepository.findByEmailWithRole(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        log.debug("User found: {}", member.getEmail());

        return new org.springframework.security.core.userdetails.User(
                member.getEmail(),
                member.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRoleName()))
        );
    }

    // memberId 기반으로 사용자 로드하는 메서드
    @Transactional
    public UserDetails loadUserById(Integer memberId) throws UsernameNotFoundException {
        log.debug("Attempting to load user by memberId: {}", memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("User not found with memberId: {}", memberId);
                    return new UsernameNotFoundException("User not found with memberId: " + memberId);
                });

        log.debug("User found: {}", member.getMemberId());

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(member.getMemberId()), // memberId를 username으로 사용
                member.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getRoleName()))
        );
    }
}

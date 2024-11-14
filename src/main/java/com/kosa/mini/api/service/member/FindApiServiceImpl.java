package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.PasswordResetRequest;
import com.kosa.mini.api.dto.member.PasswordResetResponse;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.exception.InvalidPasswordResetException;
import com.kosa.mini.api.exception.MemberNotFoundException;
import com.kosa.mini.api.exception.PasswordMismatchException;
import com.kosa.mini.api.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FindApiServiceImpl implements FindApiService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String findEmailByNicknameAndPhone(String nickname, String phoneNumber) throws MemberNotFoundException {
        // 닉네임과 연락처로 회원 찾기
        Member member = memberRepository.findByNicknameAndPhoneNumber(nickname, phoneNumber)
                .orElseThrow(() -> new MemberNotFoundException("일치하는 회원을 찾을 수 없습니다."));
        return member.getEmail();
    }

    @Override
    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetRequest request)
            throws MemberNotFoundException, PasswordMismatchException, InvalidPasswordResetException {

        List<String> errors = new ArrayList<>();

        // 닉네임으로 회원 찾기
        Optional<Member> memberOpt = memberRepository.findByNicknameAndPhoneNumber(request.getNickname(), request.getPhoneNumber());
        if (!memberOpt.isPresent()) {
            errors.add("닉네임과 연락처가 일치하는 회원을 찾을 수 없습니다.");
        } else {
            Member member = memberOpt.get();
            // 이메일 일치 확인
            if (!member.getEmail().equalsIgnoreCase(request.getEmail())) {
                errors.add("이메일이 일치하지 않습니다.");
            }
        }

        if (!errors.isEmpty()) {
            throw new InvalidPasswordResetException(errors);
        }

        // 비밀번호 일치 확인
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = memberRepository.findByNicknameAndPhoneNumberAndEmail(
                request.getNickname(),
                request.getPhoneNumber(),
                request.getEmail()
        ).orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        // 비밀번호 인코딩 후 설정
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);

        return new PasswordResetResponse("비밀번호가 성공적으로 변경되었습니다.");
    }
}

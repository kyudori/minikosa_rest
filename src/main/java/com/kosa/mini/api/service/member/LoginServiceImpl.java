package com.kosa.mini.api.service.member;

import com.kosa.mini.api.domain.member.LoginDTO;
import com.kosa.mini.api.domain.member.UserSessionDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.exception.LoginException;
import com.kosa.mini.api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;

    @Autowired
    public LoginServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserSessionDTO authenticate(LoginDTO loginDTO) throws LoginException {
        Optional<Member> optionalMember = memberRepository.findByEmail(loginDTO.getEmail());
        if (optionalMember.isEmpty()) {
            throw new LoginException("존재하지 않는 이메일입니다.");
        }

        Member member = optionalMember.get();

        // 비밀번호 비교 (보안을 위해 실제로는 암호화된 비밀번호를 비교해야 합니다)
        if (!member.getPassword().equals(loginDTO.getPassword())) {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }

        // UserSessionDTO로 매핑
        UserSessionDTO userSession = new UserSessionDTO();
        userSession.setMemberId(member.getMemberId());
        userSession.setName(member.getName());
        userSession.setEmail(member.getEmail());
        userSession.setNickname(member.getNickname());
        userSession.setRoleId(member.getRole().getRoleId());

        return userSession;
    }
}

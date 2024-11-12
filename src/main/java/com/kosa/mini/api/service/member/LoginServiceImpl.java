package com.kosa.mini.api.service.member;

import com.kosa.mini.api.domain.member.LoginDTO;
import com.kosa.mini.api.domain.member.UserSessionDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.RefreshToken;
import com.kosa.mini.api.exception.LoginException;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.repository.RefreshTokenRepository;
import com.kosa.mini.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            JwtTokenProvider tokenProvider,
                            RefreshTokenRepository refreshTokenRepository,
                            MemberRepository memberRepository,
                            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserSessionDTO authenticate(LoginDTO loginDTO) throws LoginException {
        try {
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );

            // 인증 성공 후 사용자 정보 조회
            Member member = memberRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new LoginException("사용자를 찾을 수 없습니다."));

            // Access Token 및 Refresh Token 생성
            String accessToken = tokenProvider.generateAccessToken(member.getEmail(), member.getRole().getRoleName());
            String refreshToken = tokenProvider.generateRefreshToken(member.getEmail());

            // Refresh Token 저장
            RefreshToken tokenEntity = new RefreshToken();
            tokenEntity.setToken(refreshToken);
            tokenEntity.setEmail(member.getEmail());
            tokenEntity.setExpiryDate(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000));
            refreshTokenRepository.save(tokenEntity);

            return UserSessionDTO.builder()
                    .memberId(member.getMemberId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .roleId(member.getRole().getRoleId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception ex) {
            throw new LoginException("로그인에 실패했습니다.", ex);
        }
    }

    @Override
    public UserSessionDTO refreshToken(String refreshToken) throws LoginException {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshToken);

        if (!tokenOpt.isPresent() || tokenOpt.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new LoginException("유효하지 않은 Refresh Token입니다.");
        }

        Member member = memberRepository.findByEmail(tokenOpt.get().getEmail())
                .orElseThrow(() -> new LoginException("사용자를 찾을 수 없습니다."));

        String newAccessToken = tokenProvider.generateAccessToken(member.getEmail(), member.getRole().getRoleName());

        return UserSessionDTO.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .roleId(member.getRole().getRoleId())
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String email) {
        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setEmail(email);
        tokenEntity.setExpiryDate(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000));
        refreshTokenRepository.save(tokenEntity);
    }
}

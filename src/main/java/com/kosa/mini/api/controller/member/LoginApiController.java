package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.domain.member.LoginDTO;
import com.kosa.mini.api.domain.member.UserSessionDTO;
import com.kosa.mini.api.service.member.LoginService;
import com.kosa.mini.api.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class LoginApiController {

    private final LoginService loginService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public LoginApiController(LoginService loginService, JwtTokenProvider tokenProvider) {
        this.loginService = loginService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            UserSessionDTO userSession = loginService.authenticate(loginDTO);

            // Refresh Token을 HttpOnly 쿠키에 설정 (SameSite 속성 포함)
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", userSession.getRefreshToken())
                    .httpOnly(true)
                    .secure(false) // 개발 환경에서는 false, 배포 시 true
                    .path("/api/v1/refresh-token")
                    .maxAge(tokenProvider.getRefreshTokenExpiration() / 1000)
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", refreshCookie.toString());

            // Access Token은 JSON 응답으로 전달
            UserSessionDTO responseBody = UserSessionDTO.builder()
                    .memberId(userSession.getMemberId())
                    .name(userSession.getName())
                    .email(userSession.getEmail())
                    .nickname(userSession.getNickname())
                    .roleId(userSession.getRoleId())
                    .accessToken(userSession.getAccessToken())
                    .build();

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("로그인 실패: ", e); // 예외의 스택 트레이스를 함께 로그에 남김
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패했습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null && !refreshToken.isEmpty()) {
            loginService.logout(refreshToken);

            // Refresh Token 쿠키 삭제 (SameSite 속성 포함)
            ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                    .httpOnly(true)
                    .secure(true) // HTTPS 사용 시 true
                    .path("/api/v1/refresh-token")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", deleteCookie.toString());

            return ResponseEntity.ok().body("로그아웃 되었습니다.");
        }
        return ResponseEntity.badRequest().body("Refresh Token이 필요합니다.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh Token이 필요합니다.");
        }

        try {
            UserSessionDTO userSession = loginService.refreshToken(refreshToken);

            // 새로운 Refresh Token 생성 및 업데이트
            String newRefreshToken = tokenProvider.generateRefreshToken(userSession.getEmail());
            loginService.logout(refreshToken); // 기존 Refresh Token 삭제
            loginService.saveRefreshToken(newRefreshToken, userSession.getEmail()); // 새로운 Refresh Token 저장

            // 새로운 Refresh Token을 HttpOnly 쿠키에 설정 (SameSite 속성 포함)
            ResponseCookie newRefreshCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .secure(true) // HTTPS 사용 시 true
                    .path("/api/v1/refresh-token")
                    .maxAge(tokenProvider.getRefreshTokenExpiration() / 1000)
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", newRefreshCookie.toString());

            // Access Token은 JSON 응답으로 전달
            UserSessionDTO responseBody = UserSessionDTO.builder()
                    .memberId(userSession.getMemberId())
                    .name(userSession.getName())
                    .email(userSession.getEmail())
                    .nickname(userSession.getNickname())
                    .roleId(userSession.getRoleId())
                    .accessToken(userSession.getAccessToken())
                    .build();

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("토큰 갱신 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

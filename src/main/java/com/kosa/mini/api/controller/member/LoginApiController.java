package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.AccessTokenDTO;
import com.kosa.mini.api.dto.member.LoginDTO;
import com.kosa.mini.api.dto.member.MemberInfoDTO;
import com.kosa.mini.api.dto.member.TokenResponseDTO;
import com.kosa.mini.api.exception.LoginException;
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

    @GetMapping("/info")
    public ResponseEntity<MemberInfoDTO> userInfo(@RequestBody AccessTokenDTO accessTokenDTO, HttpServletRequest request) {
        MemberInfoDTO memberInfoDTO = loginService.userInfo(accessTokenDTO);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try{
            TokenResponseDTO tokenResponseDTO = loginService.authenticate(loginDTO);

            // Refresh Token을 HttpOnly 쿠키에 설정 (SameSite 속성 포함)
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokenResponseDTO.getRefreshToken())
                    .httpOnly(true)
                    .secure(false) // 개발 환경에서는 false, 배포 시 true
                    .path("/")
                    .maxAge(tokenProvider.getRefreshTokenExpiration() / 1000)
                    .sameSite("Lax")
                    .build();
            response.addHeader("Set-Cookie", refreshCookie.toString());

            // Access Token은 JSON 응답으로 전달
            TokenResponseDTO responseBody = TokenResponseDTO.builder()
                    .memberId(tokenResponseDTO.getMemberId())
                    .name(tokenResponseDTO.getName())
                    .email(tokenResponseDTO.getEmail())
                    .nickname(tokenResponseDTO.getNickname())
                    .roleId(tokenResponseDTO.getRoleId())
                    .accessToken(tokenResponseDTO.getAccessToken())
                    .build();

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("로그인 실패: ", e); // 예외의 스택 트레이스를 함께 로그에 남김
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패했습니다.");
        }
    }

    /**
     * 로그아웃 엔드포인트
     */
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
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
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
            return ResponseEntity.badRequest().body("회원정보가 존재하지 않습니다.");
        }

        try {
            TokenResponseDTO tokenResponseDTO = loginService.refreshToken(refreshToken);

            // 새로운 Refresh Token 생성 및 업데이트
            String newRefreshToken = tokenProvider.generateRefreshToken(tokenResponseDTO.getMemberId());
            loginService.logout(refreshToken); // 기존 Refresh Token 삭제
            loginService.saveRefreshToken(newRefreshToken, tokenResponseDTO.getMemberId()); // 새로운 Refresh Token 저장

            // 새로운 Refresh Token을 HttpOnly 쿠키에 설정 (SameSite 속성 포함)
            ResponseCookie newRefreshCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .secure(false) // HTTPS 사용 시 true, 현재는 HTTP 사용
                    .path("/")
                    .maxAge(tokenProvider.getRefreshTokenExpiration() / 1000)
                    .sameSite("Lax")
                    .build();
            response.addHeader("Set-Cookie", newRefreshCookie.toString());

            // Access Token은 JSON 응답으로 전달
            TokenResponseDTO responseBody = TokenResponseDTO.builder()
                    .memberId(tokenResponseDTO.getMemberId())
                    .name(tokenResponseDTO.getName())
                    .email(tokenResponseDTO.getEmail())
                    .nickname(tokenResponseDTO.getNickname())
                    .roleId(tokenResponseDTO.getRoleId())
                    .accessToken(tokenResponseDTO.getAccessToken())
                    .build();

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("토큰 갱신 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}


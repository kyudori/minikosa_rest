package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.LoginDTO;
import com.kosa.mini.api.dto.member.TokenResponseDTO;
import com.kosa.mini.api.exception.LoginException;
import io.jsonwebtoken.Claims;

public interface LoginService {
    TokenResponseDTO authenticate(LoginDTO loginDTO) throws LoginException;
    TokenResponseDTO refreshToken(String refreshToken) throws LoginException;
    void logout(String refreshToken);
    void saveRefreshToken(String refreshToken, Integer memberId);
}

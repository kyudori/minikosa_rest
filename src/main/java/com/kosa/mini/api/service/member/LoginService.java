package com.kosa.mini.api.service.member;

import com.kosa.mini.api.domain.member.LoginDTO;
import com.kosa.mini.api.domain.member.TokenResponseDTO;
import com.kosa.mini.api.exception.LoginException;

public interface LoginService {
    TokenResponseDTO authenticate(LoginDTO loginDTO) throws LoginException;
    TokenResponseDTO refreshToken(String refreshToken) throws LoginException;
    void logout(String refreshToken);
    void saveRefreshToken(String refreshToken, String email);
}

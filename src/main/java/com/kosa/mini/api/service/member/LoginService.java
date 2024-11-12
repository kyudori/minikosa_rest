package com.kosa.mini.api.service.member;

import com.kosa.mini.api.domain.member.LoginDTO;
import com.kosa.mini.api.domain.member.UserSessionDTO;
import com.kosa.mini.api.exception.LoginException;

public interface LoginService {
    UserSessionDTO authenticate(LoginDTO loginDTO) throws LoginException;
    UserSessionDTO refreshToken(String refreshToken) throws LoginException;
    void logout(String refreshToken);
    void saveRefreshToken(String refreshToken, String email);
}

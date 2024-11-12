package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.domain.member.LoginDTO;
import com.kosa.mini.api.domain.member.UserSessionDTO;
import com.kosa.mini.api.service.member.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LoginApiController {

    private final LoginService loginService;

    @Autowired
    public LoginApiController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserSessionDTO login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        UserSessionDTO userSession = loginService.authenticate(loginDTO);
        session.setAttribute("user", userSession); // 세션에 사용자 정보 저장
        return userSession;
    }
}

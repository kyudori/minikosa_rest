package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.domain.member.LoginDTO;
import com.kosa.mini.api.domain.member.UserSessionDTO;
import com.kosa.mini.api.service.member.LoginService;
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
    public UserSessionDTO login(@RequestBody LoginDTO loginDTO) {
        return loginService.authenticate(loginDTO);
    }
}

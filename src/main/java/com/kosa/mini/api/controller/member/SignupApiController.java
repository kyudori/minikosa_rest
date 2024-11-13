package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.domain.member.SignupDTO;
import com.kosa.mini.api.exception.DuplicateEmailException;
import com.kosa.mini.api.exception.DuplicateNicknameException;
import com.kosa.mini.api.exception.SignupException;
import com.kosa.mini.api.service.member.SignUpService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/signup")
public class SignupApiController {

    private final SignUpService signUpService;

    @Autowired
    public SignupApiController(SignUpService signUpService) {

        this.signUpService = signUpService;
    }

    // 회원가입 요청
    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupDTO dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            boolean success = signUpService.signUp(dto);
            Map<String, String> response = new HashMap<>();
            if(success) {
                response.put("message", "회원가입이 성공적으로 완료되었습니다.");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("error", "회원가입에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (DuplicateEmailException | DuplicateNicknameException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (SignupException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "회원가입 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 이메일 중복 검사
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) {
        boolean exists = signUpService.isEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 닉네임 중복 검사
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = signUpService.isNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}

package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.*;
import com.kosa.mini.api.exception.InvalidPasswordResetException;
import com.kosa.mini.api.exception.MemberNotFoundException;
import com.kosa.mini.api.exception.PasswordMismatchException;
import com.kosa.mini.api.service.member.FindApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Validated
public class FindApiController {

    @Autowired
    private FindApiService findApiService;

    /**
     * 이메일 찾기 API
     */
    @PostMapping("/find/email")
    public ResponseEntity<EmailFindResponse> findEmail(@Valid @RequestBody EmailFindRequest request) {
        try {
            String email = findApiService.findEmailByNicknameAndPhone(request.getNickname(), request.getPhoneNumber());
            return ResponseEntity.ok(new EmailFindResponse(email));
        } catch (MemberNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 비밀번호 초기화
     */
    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        try {
            PasswordResetResponse response = findApiService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (InvalidPasswordResetException ex) {
            throw ex;
        } catch (PasswordMismatchException ex) {
            throw ex;
        } catch (MemberNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }
}

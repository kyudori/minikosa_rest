package com.kosa.mini.api.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(LoginException.class)
  public ResponseEntity<String> handleLoginException(LoginException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(SignupException.class)
  public ResponseEntity<String> handleSignupException(SignupException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DuplicateNicknameException.class)
  public ResponseEntity<String> handleDuplicateNicknameException(DuplicateNicknameException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
  }

  // JWT 관련 예외 처리
  @ExceptionHandler(JwtException.class)
  public ResponseEntity<String> handleJwtException(JwtException ex) {
    return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
  }

  // 기타 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGlobalException(Exception ex) {
    return new ResponseEntity<>("내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

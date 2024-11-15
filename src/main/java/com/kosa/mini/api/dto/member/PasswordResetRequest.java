package com.kosa.mini.api.dto.member;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class PasswordResetRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String phoneNumber;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String confirmPassword;
}

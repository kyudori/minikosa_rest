package com.kosa.mini.api.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSessionDTO {
    private Long memberId;
    private String name;
    private String email;
    private String nickname;
    private Long roleId;
    private String accessToken;
    private String refreshToken;
}

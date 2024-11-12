package com.kosa.mini.api.domain.member;

import lombok.Data;

@Data
public class UserSessionDTO {
    private Long memberId;
    private String name;
    private String email;
    private String nickname;
    private Long roleId;
}

package com.kosa.mini.api.dto.member;

import lombok.Data;

@Data
public class UserSessionDTO {
    private Integer memberId;
    private String name;
    private String email;
    private String nickname;
    private Integer roleId;
}

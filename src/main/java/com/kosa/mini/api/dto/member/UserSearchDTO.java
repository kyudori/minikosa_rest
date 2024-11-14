package com.kosa.mini.api.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchDTO {
    private String name;
    private String nickname;
    private String email;
    private String phoneNumber;
}

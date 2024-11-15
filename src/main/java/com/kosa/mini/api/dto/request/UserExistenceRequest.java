package com.kosa.mini.api.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExistenceRequest {
    private String email;
}

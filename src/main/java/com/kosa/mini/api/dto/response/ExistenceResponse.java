package com.kosa.mini.api.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExistenceResponse {
    private boolean exists;
}

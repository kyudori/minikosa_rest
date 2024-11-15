package com.kosa.mini.api.dto.request;

import lombok.Data;


@Data
public class AssignOwnerRequest {
    private Integer storeId;
    private Integer memberId;
}

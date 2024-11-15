package com.kosa.mini.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignOwnerResponse {
    private Integer storeId;
    private String storeName;
    private Integer ownerId;
    private String ownerName;
    private String message;
}

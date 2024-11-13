package com.kosa.mini.api.dto.store;

import lombok.Data;

@Data
public class ReplyDTO {
    private int storeId;
    private int reviewId;
    private int ownerId;
    private String replyText;
}

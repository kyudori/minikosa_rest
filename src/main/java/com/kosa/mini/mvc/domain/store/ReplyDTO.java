package com.kosa.mini.mvc.domain.store;

import lombok.Data;

@Data
public class ReplyDTO {
    private int storeId;
    private int reviewId;
    private int ownerId;
    private String replyText;
}

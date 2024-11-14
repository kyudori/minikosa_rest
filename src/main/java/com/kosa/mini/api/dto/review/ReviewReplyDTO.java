package com.kosa.mini.api.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewReplyDTO {
    private Integer storeId;
    private Integer memberId;
    private String reviewText;
    private Integer rating;
    private Date createdAt;
    private Integer replyId;
    private Integer reviewId;
    private Integer ownerId;
    private String replyText;
    private String nickname;
}

package com.kosa.mini.api.dto.review;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.ReviewReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReplySaveDTO {
    private Integer reviewId;
    private Integer ownerId;
    private String replyText;

    public ReplySaveDTO fromEntity(ReviewReply reviewReply) {
        return new ReplySaveDTO(
                reviewReply.getReplyId(),
                reviewReply.getOwner().getMemberId(),
                reviewReply.getReplyText());
    }

    public ReviewReply toEntity(Review review, Member member){
        return ReviewReply.builder()
                .review(review)
                .owner(member)
                .replyText(replyText)
                .build();
    }

}

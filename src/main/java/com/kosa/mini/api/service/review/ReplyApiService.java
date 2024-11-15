package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReplySaveDTO;
import com.kosa.mini.api.entity.ReviewReply;

public interface ReplyApiService {
    boolean deleteReply(Integer replyId);
    ReplySaveDTO createReview(ReplySaveDTO replySaveDTO, Integer ownerId);
}

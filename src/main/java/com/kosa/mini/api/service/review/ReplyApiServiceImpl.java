package com.kosa.mini.api.service.review;

import com.kosa.mini.api.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReplyApiServiceImpl implements ReplyApiService {

    private final ReplyRepository replyRepository;

    @Override
    public boolean deleteReply(Integer replyId) {
        boolean replyResult = replyRepository.existsById(replyId);
        if(replyResult) {
            replyRepository.deleteById(replyId);
            return true;
        } else {
            return false;
        }
    }
}

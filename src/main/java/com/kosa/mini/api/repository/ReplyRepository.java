package com.kosa.mini.api.repository;

import com.kosa.mini.api.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReviewReply, Integer> {
}

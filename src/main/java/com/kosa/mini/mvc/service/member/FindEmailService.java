package com.kosa.mini.mvc.service.member;

import com.kosa.mini.mvc.domain.member.FindEmailDTO;

public interface FindEmailService {
    String findEmail(FindEmailDTO findEmailDTO) throws Exception;
}

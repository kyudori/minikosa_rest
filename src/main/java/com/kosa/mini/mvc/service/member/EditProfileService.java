package com.kosa.mini.mvc.service.member;

import com.kosa.mini.mvc.domain.member.EditProfileDTO;
import com.kosa.mini.mvc.domain.member.Member;

public interface EditProfileService {
    EditProfileDTO getMemberById(Long memberId);
    void updateMember(Member member);
    boolean isNicknameExists(String nickname);
}

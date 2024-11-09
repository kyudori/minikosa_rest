package com.kosa.mini.mvc.service.member;

import com.kosa.mini.mvc.domain.member.ResetPasswordDTO;

public interface ResetPasswordService {

    boolean resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception;

    ResetPasswordDTO searchMember(String name, String phoneNumber, String email);
}

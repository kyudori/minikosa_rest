package com.kosa.mini.api.service.member;

import com.kosa.mini.api.domain.member.SignupDTO;
import com.kosa.mini.api.exception.DuplicateEmailException;
import com.kosa.mini.api.exception.DuplicateNicknameException;
import com.kosa.mini.api.exception.SignupException;

public interface SignUpService {
    boolean signUp(SignupDTO dto) throws DuplicateEmailException, DuplicateNicknameException, SignupException;
    boolean isEmailExists(String email);
    boolean isNicknameExists(String nickname);
}

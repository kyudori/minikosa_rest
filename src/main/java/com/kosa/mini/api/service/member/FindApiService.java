package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.PasswordResetRequest;
import com.kosa.mini.api.dto.member.PasswordResetResponse;
import com.kosa.mini.api.exception.InvalidPasswordResetException;
import com.kosa.mini.api.exception.MemberNotFoundException;
import com.kosa.mini.api.exception.PasswordMismatchException;

public interface FindApiService {
    boolean findMemberByNicknameAndPhoneAndEmail(String nickname, String phoneNumber, String email) throws MemberNotFoundException;
    String findEmailByNicknameAndPhone(String nickname, String phoneNumber) throws MemberNotFoundException;
    PasswordResetResponse resetPassword(PasswordResetRequest request) throws MemberNotFoundException, PasswordMismatchException, InvalidPasswordResetException;
}

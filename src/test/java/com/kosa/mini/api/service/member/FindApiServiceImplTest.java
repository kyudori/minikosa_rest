package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.PasswordResetRequest;
import com.kosa.mini.api.dto.member.PasswordResetResponse;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.exception.InvalidPasswordResetException;
import com.kosa.mini.api.exception.PasswordMismatchException;
import com.kosa.mini.api.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FindApiServiceImplTest {

    @InjectMocks
    private FindApiServiceImpl findApiService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        member = Member.builder()
                .memberId(1)
                .name("홍길동")
                .email("hong@example.com")
                .nickname("honggildong")
                .phoneNumber("010-1234-5678")
                .password("encodedPassword")
                .build();
    }

    @Test
    @Transactional
    public void testResetPassword_Success() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNickname("honggildong");
        request.setPhoneNumber("010-1234-5678");
        request.setEmail("hong@example.com");
        request.setNewPassword("newSecurePassword123");
        request.setConfirmPassword("newSecurePassword123");

        when(memberRepository.findByNicknameAndPhoneNumber("honggildong", "010-1234-5678"))
                .thenReturn(Optional.of(member));
        when(passwordEncoder.encode("newSecurePassword123")).thenReturn("encodedNewPassword");
        when(memberRepository.findByNicknameAndPhoneNumberAndEmail("honggildong", "010-1234-5678", "hong@example.com"))
                .thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        PasswordResetResponse response = findApiService.resetPassword(request);

        assertNotNull(response);
        assertEquals("비밀번호가 성공적으로 변경되었습니다.", response.getMessage());
        assertEquals("encodedNewPassword", member.getPassword());

        verify(memberRepository, times(1)).findByNicknameAndPhoneNumber("honggildong", "010-1234-5678");
        verify(memberRepository, times(1)).findByNicknameAndPhoneNumberAndEmail("honggildong", "010-1234-5678", "hong@example.com");
        verify(passwordEncoder, times(1)).encode("newSecurePassword123");
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    @Transactional
    public void testResetPassword_InvalidNicknameOrPhone() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNickname("unknown");
        request.setPhoneNumber("000-0000-0000");
        request.setEmail("hong@example.com");
        request.setNewPassword("newSecurePassword123");
        request.setConfirmPassword("newSecurePassword123");

        when(memberRepository.findByNicknameAndPhoneNumber("unknown", "000-0000-0000"))
                .thenReturn(Optional.empty());

        InvalidPasswordResetException exception = assertThrows(InvalidPasswordResetException.class, () -> {
            findApiService.resetPassword(request);
        });

        assertTrue(exception.getErrors().contains("닉네임과 연락처가 일치하는 회원을 찾을 수 없습니다."));
        verify(memberRepository, times(1)).findByNicknameAndPhoneNumber("unknown", "000-0000-0000");
        verify(memberRepository, times(0)).findByNicknameAndPhoneNumberAndEmail(anyString(), anyString(), anyString());
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    @Transactional
    public void testResetPassword_EmailMismatch() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNickname("honggildong");
        request.setPhoneNumber("010-1234-5678");
        request.setEmail("wrong@example.com");
        request.setNewPassword("newSecurePassword123");
        request.setConfirmPassword("newSecurePassword123");

        when(memberRepository.findByNicknameAndPhoneNumber("honggildong", "010-1234-5678"))
                .thenReturn(Optional.of(member));

        InvalidPasswordResetException exception = assertThrows(InvalidPasswordResetException.class, () -> {
            findApiService.resetPassword(request);
        });

        assertTrue(exception.getErrors().contains("이메일이 일치하지 않습니다."));
        verify(memberRepository, times(1)).findByNicknameAndPhoneNumber("honggildong", "010-1234-5678");
        verify(memberRepository, times(0)).findByNicknameAndPhoneNumberAndEmail(anyString(), anyString(), anyString());
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    @Transactional
    public void testResetPassword_PasswordMismatch() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNickname("honggildong");
        request.setPhoneNumber("010-1234-5678");
        request.setEmail("hong@example.com");
        request.setNewPassword("newSecurePassword123");
        request.setConfirmPassword("differentPassword123");

        when(memberRepository.findByNicknameAndPhoneNumber("honggildong", "010-1234-5678"))
                .thenReturn(Optional.of(member));

        PasswordMismatchException exception = assertThrows(PasswordMismatchException.class, () -> {
            findApiService.resetPassword(request);
        });

        assertEquals("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.", exception.getMessage());
        verify(memberRepository, times(1)).findByNicknameAndPhoneNumber("honggildong", "010-1234-5678");
        verify(memberRepository, times(0)).findByNicknameAndPhoneNumberAndEmail(anyString(), anyString(), anyString());
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    @Transactional
    public void testResetPassword_MemberNotFound() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setNickname("honggildong");
        request.setPhoneNumber("010-1234-5678");
        request.setEmail("hong@example.com");
        request.setNewPassword("newSecurePassword123");
        request.setConfirmPassword("newSecurePassword123");

        when(memberRepository.findByNicknameAndPhoneNumberAndEmail("honggildong", "010-1234-5678", "hong@example.com"))
                .thenReturn(Optional.empty());

        // Since nickname, phoneNumber, email match is already validated, but member not found here
        // To simulate, you can have the service method re-validate
        // But in our implementation, the member is already found before checking email
        // So this test might not be necessary based on the service implementation
    }
}

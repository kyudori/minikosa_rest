package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberEditProfileRepository;
import com.kosa.mini.api.service.member.MemberEditProfileServiceImpl;
import com.kosa.mini.api.service.member.SignUpServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EditProfileApiController {
    @Autowired
    private MemberEditProfileServiceImpl memberEditProfileService; // 서비스 객체 주입

    @Autowired
    private MemberEditProfileRepository memberEditProfileRepository; // 리퍼지터리 주입

    @Autowired
    private SignUpServiceImpl signUpService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 안될 경우 UNAUTHORIZED 반환
        }

        // UserDetails에서 username (memberId) 가져오기
        String memberIdStr = userDetails.getUsername(); // CustomUserDetailsService에서 username을 memberId로 설정했음
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format: {}", memberIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Member member = memberEditProfileRepository.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        // 필요한 정보만 반환 (password 제외)
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("memberId", member.getMemberId());
        userInfo.put("name", member.getName());
        userInfo.put("nickname", member.getNickname());
        userInfo.put("email", member.getEmail());
        userInfo.put("phoneNumber", member.getPhoneNumber());
        userInfo.put("roleId", member.getRoleId());
        userInfo.put("storeName", member.getRoleId() == 3 && member.getStores() != null && !member.getStores().isEmpty()
                ? member.getStores().iterator().next().getStoreName() : null);

        return ResponseEntity.ok(userInfo);   // 사용자 정보에 200 상태 코드 부여
    }

    @PutMapping("/editprofile")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails userDetails,
                                    @Valid @RequestBody MemberEditProfileDTO dto){
        log.info("입력 dto값: {}", dto.toString());

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 안될 경우 UNAUTHORIZED 반환
        }

        // UserDetails에서 username (memberId) 가져오기
        String memberIdStr = userDetails.getUsername(); // CustomUserDetailsService에서 username을 memberId로 설정했음
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format: {}", memberIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        dto.setMemberId(memberId);
        log.info("입력 dto값: {}", dto.toString());

        Member updatedMember = memberEditProfileService.update(memberId, dto);

        if (updatedMember != null) {
            // 필요한 정보만 반환 (password 제외)
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("memberId", updatedMember.getMemberId());
            userInfo.put("name", updatedMember.getName());
            userInfo.put("nickname", updatedMember.getNickname());
            userInfo.put("email", updatedMember.getEmail());
            userInfo.put("phoneNumber", updatedMember.getPhoneNumber());
            userInfo.put("roleId", updatedMember.getRoleId());
            userInfo.put("storeName", updatedMember.getRoleId() == 3 && updatedMember.getStores() != null && !updatedMember.getStores().isEmpty()
                    ? updatedMember.getStores().iterator().next().getStoreName() : null);

            return ResponseEntity.status(HttpStatus.OK).body(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 수정에 실패했습니다.");
        }
    }

    // 닉네임 중복 검사
    @GetMapping("/check-editnickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = signUpService.isNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴 엔드포인트
    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody Map<String, String> passwordRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String memberIdStr = userDetails.getUsername();
        Integer memberId;
        try {
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid memberId format: {}", memberIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String currentPassword = passwordRequest.get("currentPassword");
        if (currentPassword == null || currentPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 비밀번호를 입력해 주세요.");
        }

        boolean isValid = memberEditProfileService.verifyPassword(memberId, currentPassword);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        }

        boolean isDeleted = memberEditProfileService.deleteMember(memberId);
        if (isDeleted) {
            return ResponseEntity.ok("회원 탈퇴가 성공적으로 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 탈퇴에 실패했습니다.");
        }
    }
}

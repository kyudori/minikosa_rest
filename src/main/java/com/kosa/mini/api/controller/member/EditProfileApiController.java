package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberEditProfileRepository;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.service.member.MemberEditProfileServiceImpl;
import com.kosa.mini.api.service.member.SignUpService;
import com.kosa.mini.api.service.member.SignUpServiceImpl;
import com.kosa.mini.mvc.domain.store.Store;
import com.kosa.mini.mvc.service.store.StoreService;
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //로그인 안될경우 UNAUTHORIZED 반환 뭔진 잘 모르겠...
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

            memberEditProfileRepository.findById(memberId); //멤버 리퍼지토리로 findId 조회     //멤버 리퍼지토리로 findId 조회
            return ResponseEntity.ok(memberIdStr);   // 멤버아이디에 반환값 ResponseEntity에 200상태코드 부여
    }

    @PutMapping("/editprofile")
    public ResponseEntity<Member> update( @AuthenticationPrincipal UserDetails userDetails,
                                          @Valid @RequestBody MemberEditProfileDTO dto){
        log.info("입력 dto값"+dto.toString());

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //로그인 안될경우 UNAUTHORIZED 반환 뭔진 잘 모르겠...
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
        log.info("입력 dto값"+dto.toString());

        Member member = memberEditProfileService.update(memberId, dto);

        return (member != null) ?
                ResponseEntity.status(HttpStatus.OK).body(member) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 닉네임 중복 검사
    @GetMapping("/check-editnickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = signUpService.isNicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

}

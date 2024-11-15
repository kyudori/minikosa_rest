package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.MemberEditProfileDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.repository.MemberRepository;
import com.kosa.mini.api.service.member.MemberEditProfileServiceImpl;
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

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EditProfileApiController {
    @Autowired
    private MemberEditProfileServiceImpl memberEditProfileService; // 서비스 객체 주입

    @Autowired
    private MemberRepository memberRepository;


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

        memberRepository.findById(memberId);     //멤버 리퍼지토리로 findId 조회
        return ResponseEntity.ok(memberIdStr);   // 멤버아이디에 반환값 ResponseEntity에 200상태코드 부여
    }

    @PutMapping("/editprofile/{memberId}")
    public ResponseEntity<Member> update(@PathVariable(name = "memberId") Integer memberId,
                                         @RequestBody @Valid MemberEditProfileDTO dto){
        Member updated = memberEditProfileService.update(memberId, dto);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    // nickname 중복검사
    @GetMapping("/member/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = memberEditProfileService.existsByNickname(nickname);
        return ResponseEntity.ok().body("{\"exists\": " + exists + "}");
    }


}

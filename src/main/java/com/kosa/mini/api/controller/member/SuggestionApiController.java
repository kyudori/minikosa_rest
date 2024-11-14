package com.kosa.mini.api.controller.member;

import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.service.member.SuggestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SuggestionApiController {
    @Autowired
    private SuggestionService suggestionService; //서비스 객체 주입

    // POST 생성
    @PostMapping("/suggestion")
    public ResponseEntity<ContactUsDTO> create(@RequestBody ContactUsDTO dto,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        dto.setMemberId(memberId);
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);  // content가 없으면 BadRequest 응답
        }
        try {
            ContactUs contactUs = suggestionService.create(dto);
            dto.setCreatedAt(contactUs.getCreatedAt());
            dto.setViews(contactUs.getViews());
            dto.setIsModified(contactUs.getIsModified());
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
        }
    }

}

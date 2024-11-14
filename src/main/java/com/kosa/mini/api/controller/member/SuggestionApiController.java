package com.kosa.mini.api.controller.member;


import com.kosa.mini.api.dto.member.ContactUsDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.service.member.SuggestionServiceImplApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SuggestionApiController {
    @Autowired
    private SuggestionServiceImplApi suggestionService; //서비스 객체 주입

//    // POST 생성
//    @PostMapping("/suggestion")
//    public ResponseEntity<ContactUs> create(@RequestBody ContactUsDTO dto) {
//        Integer memberId = 1;
//        dto.setMemberId(memberId);
//        log.info("받은 DTO: " + dto.toString() + "memberId(임시)" + memberId);  // DTO에 들어온 데이터 확인
//        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // content가 없으면 BadRequest 응답
//        }
//        try {
//            ContactUs created = suggestionService.create(dto);
//            return ResponseEntity.status(HttpStatus.OK).body(created);
//        } catch (IllegalArgumentException e) {
//            log.error("Error creating contact us entry: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }

    // POST 생성
    @PostMapping("/suggestion")
    public ResponseEntity<ContactUsDTO> create(@RequestBody ContactUsDTO dto) {
        Integer memberId = 1;
        dto.setMemberId(memberId);
        log.info("받은 DTO: " + dto.toString() + "memberId(임시)" + memberId);  // DTO에 들어온 데이터 확인
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

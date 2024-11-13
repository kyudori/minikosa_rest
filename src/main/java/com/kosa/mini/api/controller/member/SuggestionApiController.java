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

   // POST 생성
  @PostMapping("/suggestion")
    public ResponseEntity<ContactUs> create(@RequestBody ContactUsDTO dto) {
      log.info("컨트롤러 dto: "+ dto.toString());
      ContactUs created = suggestionService.create(dto);
      return (created != null) ?
              ResponseEntity.status(HttpStatus.OK).body(created) :
              ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

}

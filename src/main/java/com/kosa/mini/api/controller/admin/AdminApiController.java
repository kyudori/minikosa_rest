package com.kosa.mini.api.controller.admin;

import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.service.member.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RestController
public class AdminApiController {

    @Autowired
    private SuggestionService suggestionService;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/suggestion")
    public ResponseEntity<Page<ContactUs>> auth(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam(name = "type", defaultValue = "title") String type,
                                  @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // UserDetails에서 username (memberId) 가져오기
        Integer memberId = Integer.valueOf(userDetails.getUsername()); // CustomUserDetailsService에서 username을 memberId로 설정했음
        System.out.println(memberId);

        Pageable pageable = PageRequest.of(page, size);
        Page<ContactUs> suggestions = suggestionService.getSuggestions(type, keyword, pageable);
        return ResponseEntity.ok(suggestions);
    }
}

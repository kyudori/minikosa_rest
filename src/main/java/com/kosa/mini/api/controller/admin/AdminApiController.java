package com.kosa.mini.api.controller.admin;

import com.kosa.mini.api.dto.member.MemberDTO;
import com.kosa.mini.api.dto.request.StoreExistenceRequest;
import com.kosa.mini.api.dto.request.UserExistenceRequest;
import com.kosa.mini.api.dto.store.StoreDTO;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.service.member.SuggestionService;
import com.kosa.mini.api.service.store.StoreApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/admin")
@RestController
public class AdminApiController {

    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private StoreApiService storeApiService;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/suggestion/list")
    public ResponseEntity<Page<ContactUs>> getListSuggestion(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam(name = "type", defaultValue = "title") String type,
                                  @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ContactUs> suggestions = suggestionService.getSuggestions(type, keyword, pageable);

        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/suggestion/{contactId}")
    public ResponseEntity<ContactUs> getSuggestion(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable Integer contactId) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            suggestionService.incrementViews(contactId);
            ContactUs suggestion = suggestionService.getSuggestionById(contactId);
            return ResponseEntity.ok(suggestion);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/suggestion/{contactId}")
    public ResponseEntity<Void> deleteSuggestion(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Integer contactId) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            // Delete the ContactUs entry
            suggestionService.deleteSuggestion(contactId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 사용자 검색 API
    @GetMapping("/search/users")
    public ResponseEntity<List<String>> searchUsers(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody UserExistenceRequest userExistenceRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<String> users = storeApiService.searchEmailsByPartialEmail(userExistenceRequest.getEmail());
        return ResponseEntity.ok(users);
    }

    // 가게 검색 API
    @GetMapping("/search/stores")
    public ResponseEntity<List<String>> searchStores(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody StoreExistenceRequest storeExistenceRequest) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer memberId;
        try {
            memberId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<String> stores = storeApiService.searchStoreNamesByPartialName(storeExistenceRequest.getStoreName());
        return ResponseEntity.ok(stores);
    }
}

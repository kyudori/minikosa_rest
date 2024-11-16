package com.kosa.mini.api.controller.admin;

import com.kosa.mini.api.dto.member.UserSearchDTO;
import com.kosa.mini.api.dto.request.AssignOwnerRequest;
import com.kosa.mini.api.dto.request.StoreExistenceRequest;
import com.kosa.mini.api.dto.request.UserExistenceRequest;
import com.kosa.mini.api.dto.response.AssignOwnerResponse;
import com.kosa.mini.api.dto.store.*;
import com.kosa.mini.api.entity.ContactUs;
import com.kosa.mini.api.exception.FileStorageException;
import com.kosa.mini.api.exception.ResourceNotFoundException;
import com.kosa.mini.api.exception.StoreNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
            suggestionService.deleteSuggestion(contactId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer id) {
        try {
            storeApiService.deleteStore(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (StoreNotFoundException ex) {
            return ResponseEntity.status(404).build(); // 404 Not Found
        }
    }

    // 사용자 검색 API
    @GetMapping("/search/users")
    public ResponseEntity<List<UserSearchDTO>> searchUsers(@AuthenticationPrincipal UserDetails userDetails,
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

        List<UserSearchDTO> users = storeApiService.searchUsersByEmail(userExistenceRequest.getEmail());
        return ResponseEntity.ok(users);
    }

    // 가게 검색 API
    @GetMapping("/search/stores")
    public ResponseEntity<List<StoreSearchDTO>> searchStores(@AuthenticationPrincipal UserDetails userDetails,
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

        List<StoreSearchDTO> stores = storeApiService.searchStoresByName(storeExistenceRequest.getStoreName());
        return ResponseEntity.ok(stores);
    }

    @PostMapping("/assign/owner")
    public ResponseEntity<AssignOwnerResponse> assignOwner(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AssignOwnerRequest assignOwnerRequest) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer adminId;
        try {
            adminId = Integer.valueOf(userDetails.getUsername());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            AssignOwnerResponse response = storeApiService.assignOwnerToStore(assignOwnerRequest);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException | StoreNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 가게 등록
    @PostMapping("stores")
    //@PreAuthorize("hasRole('ADMIN')") // ADMIN 권한이 있는 사용자만 접근 가능
    public ResponseEntity<StoreCreateDTO> createStore(
            @RequestPart("store") StoreCreateDTO storeCreateDTO,
            @RequestPart("storePhoto")MultipartFile storePhoto) {
        try {
            storeCreateDTO.setStorePhoto(storePhoto);
            // Store 생성 로직
            StoreCreateDTO createdStore = storeApiService.createStore(storeCreateDTO);
            return new ResponseEntity<>(createdStore, HttpStatus.CREATED);
        } catch (Exception e) {
            // 예외는 GlobalExceptionHandler에서 처리
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 가게 수정
    @PutMapping("/stores/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StoreCreateDTO> editStore(
            @PathVariable("id") Integer storeId,
            @RequestPart("store") StoreCreateDTO storeCreateDTO) {
        try {
            StoreCreateDTO updatedStore = storeApiService.editStore(storeId, storeCreateDTO);
            return new ResponseEntity<>(updatedStore, HttpStatus.OK);
        } catch (Exception e) {
            // 예외는 GlobalExceptionHandler에서 처리
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

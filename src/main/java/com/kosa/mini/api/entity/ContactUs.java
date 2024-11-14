package com.kosa.mini.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_us")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Integer contactId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore                      //멤버id 나중에 하드코딩 말고 dto로 가져와서 수정해야함!
    private Member member;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_modified")
    private Boolean isModified;

    @Column(name = "views")
    private Integer views;

}

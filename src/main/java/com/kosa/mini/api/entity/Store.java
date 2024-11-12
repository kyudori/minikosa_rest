package com.kosa.mini.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "postcode", nullable = false, length = 10)
    private String postcode;

    @Column(name = "road_address", nullable = false, length = 255)
    private String roadAddress;

    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    @Column(name = "extra_address", length = 255)
    private String extraAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "store_photo", length = 255)
    private String storePhoto;

    @Column(name = "store_description", columnDefinition = "TEXT")
    private String storeDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @Column(name = "opening_time")
    private Date openingTime;

    @Column(name = "closing_time")
    private Date closingTime;

    @Column(name = "website_info", length = 255)
    private String websiteInfo;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_modified")
    private Boolean isModified;

    // 관계 매핑: 가게에 속한 메뉴들
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Menu> menus;

    // 관계 매핑: 가게에 속한 리뷰들
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews;
}




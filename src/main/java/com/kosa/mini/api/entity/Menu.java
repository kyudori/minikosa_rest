package com.kosa.mini.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kosa.mini.api.dto.store.MenuDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer menuId;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "menu_photo", length = 255)
    private String menuPhoto;

}


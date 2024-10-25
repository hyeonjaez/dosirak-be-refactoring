package com.example.dosirakbe.domain.menu.entity;


import com.example.dosirakbe.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "menus")
@Entity
@Getter
public class Menu {

    @Id
    @Column(name = "menu_id",unique = true,updatable = false)
    private Long menuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_img")
    private String menuImg;

    @Column(name = "menu_price")
    private String menuPrice;

    @Column(name = "menu_pack_size")
    private String menuPackSize;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;


}

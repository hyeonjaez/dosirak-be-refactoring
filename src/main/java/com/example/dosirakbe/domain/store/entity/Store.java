package com.example.dosirakbe.domain.store.entity;

import com.example.dosirakbe.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "stores")
@Entity
@Getter
@Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
public class Store {


    @Id
    @Column(name = "store_id",unique = true,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "tel_number")
    private String telNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "operation_time")
    private String operationTime;

    @Column(name = "store_category")
    private String storeCategory;

    @Column(name = "store_img")
    private String storeImg;

    @Column(name = "map_x")
    private double mapX;

    @Column(name = "map_y")
    private double mapY;

    @Column(name = "if_valid")
    private String ifValid;

    @Column(name = "if_reward")
    private String ifReward;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menus;


}

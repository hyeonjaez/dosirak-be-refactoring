package com.example.dosirakbe.domain.store.entity;

import com.example.dosirakbe.domain.menu.entity.Menu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

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

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "tel_number", nullable = false)
    private String telNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "operation_time", nullable = false)
    private String operationTime;

    @Column(name = "store_category")
    private String storeCategory;

    @Column(name = "store_img", nullable = false)
    private String storeImg;

    @Column(name = "map_x", nullable = false)
    private double mapX;

    @Column(name = "map_y", nullable = false)
    private double mapY;

    @Column(name = "if_valid", nullable = false)
    private String ifValid;

    @Column(name = "if_reward", nullable = false)
    private String ifReward;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menus;

    public List<Map<String, String>> changeOperationTime() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(operationTime, new TypeReference<List<Map<String, String>>>() {});
    }



}

package com.example.dosirakbe.domain.salestore.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "salestores")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class SaleStore {

    @Id
    @Column(name = "sale_store_id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleStoreId;

    @Column(name = "sale_store_name", nullable = false)
    private String saleStoreName;

    @Column(name = "sale_store_img", nullable = false)
    private String saleStoreImg;

    @Column(name = "sale_store_address", nullable = false)
    private String saleStoreAddress;

    @Column(name = "sale_map_x", nullable = false)
    private String saleMapX;

    @Column(name = "sale_map_y", nullable = false)
    private String saleMapY;

    @Column(name = "sale_operation_time", nullable = false)
    private String saleOperationTime;

    @Column(name = "sale_discount", nullable = false)
    private String saleDiscount;

}

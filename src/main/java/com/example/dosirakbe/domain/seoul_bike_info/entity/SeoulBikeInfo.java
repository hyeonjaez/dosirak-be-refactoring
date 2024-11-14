package com.example.dosirakbe.domain.seoul_bike_info.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "seoul_bike_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본생성자
@AllArgsConstructor
public class SeoulBikeInfo {

    @Id
    @Column(name = "대여소_ID")
    private String id;

    @Column(name = "주소1")
    private String addressLevelOne;

    @Column(name = "주소2")
    private String addressLevelTwo;

    @Column(name = "위도")
    private BigDecimal latitude;

    @Column(name = "경도")
    private BigDecimal longitude;
}

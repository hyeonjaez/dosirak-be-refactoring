package com.example.dosirakbe.domain.zone_category.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //기본생성자
@Table(name = "zone_category")
public class ZoneCategory {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @JoinColumn(name = "parent_zone_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ZoneCategory parentZoneCategory;
}

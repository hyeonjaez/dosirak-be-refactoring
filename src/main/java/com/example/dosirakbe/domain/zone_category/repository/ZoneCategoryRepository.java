package com.example.dosirakbe.domain.zone_category.repository;

import com.example.dosirakbe.domain.zone_category.entity.ZoneCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZoneCategoryRepository extends JpaRepository<ZoneCategory, Long> {
    Optional<ZoneCategory> findByName(String name);
}

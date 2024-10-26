package com.example.dosirakbe.domain.store.repository;


import com.example.dosirakbe.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword%")
    List<Store> searchStoresByKeyword(@Param("keyword") String keyword);

    List<Store> findByStoreCategory(String storeCategory);

    @Query("SELECT s, " +
            "(6371 * acos(cos(radians(:mapX)) * cos(radians(s.mapX)) " +
            "* cos(radians(s.mapY) - radians(:mapY)) + sin(radians(:mapX)) * sin(radians(s.mapX)))) " +
            "AS distance " +
            "FROM Store s " +
            "WHERE (6371 * acos(cos(radians(:mapX)) * cos(radians(s.mapX)) " +
            "* cos(radians(s.mapY) - radians(:mapY)) + sin(radians(:mapX)) * sin(radians(s.mapX)))) <= 1 " +
            "ORDER BY distance ASC")
    List<Store> findStoresIn1Km(@Param("mapX") double mapX, @Param("mapY") double mapY);

}

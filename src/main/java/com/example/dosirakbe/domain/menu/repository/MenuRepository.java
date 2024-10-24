package com.example.dosirakbe.domain.menu.repository;

import com.example.dosirakbe.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository  extends JpaRepository<Menu, Long> {

    List<Menu> findByStore_StoreId(Long storeId);
}
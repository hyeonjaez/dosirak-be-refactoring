package com.example.dosirakbe.domain.salestore.repository;

import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleStoreRepository extends JpaRepository<SaleStore, Long> {

    List<SaleStore> findBySaleStoreAddressContaining(String saleStoreAddress);
}

package com.example.dosirakbe.domain.salestore.repository;

import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleStoreRepository extends JpaRepository<SaleStore, Long> {

    List<SaleStore> findBySaleStoreAddressContainingOrderBySaleDiscountDesc(String saleStoreAddress);

    Optional<SaleStore> findBySaleStoreName(String saleStoreName);
}

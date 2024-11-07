package com.example.dosirakbe.domain.salestore.service;


import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import com.example.dosirakbe.domain.salestore.repository.SaleStoreRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SaleStoreService {

    @Autowired
    private SaleStoreRepository saleStoreRepository;

    public List<SaleStore> getSaleStoresByAddress(String saleStoreAddress) {

        if (saleStoreAddress.endsWith("동")) {
            saleStoreAddress = saleStoreAddress.replace("동", "");
        }


        List<SaleStore> saleStores = saleStoreRepository.findBySaleStoreAddressContaining(saleStoreAddress);

        if (saleStores.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        return saleStores;
    }
}

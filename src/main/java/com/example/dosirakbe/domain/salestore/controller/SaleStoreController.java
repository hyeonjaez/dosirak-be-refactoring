package com.example.dosirakbe.domain.salestore.controller;

import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import com.example.dosirakbe.domain.salestore.service.SaleStoreService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SaleStoreController {

    @Autowired
    private SaleStoreService saleStoreService;

    @GetMapping("/api/salestores")
    public ResponseEntity<ApiResult<List<SaleStore>>> getStoresByAddress(@RequestParam("address") String address) {
        List<SaleStore> stores = saleStoreService.getSaleStoresByAddress(address);
        return ResponseEntity.ok(
                ApiResult.<List<SaleStore>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("마감음식 판매 가게 반환")
                        .data(stores)
                        .build()
        );
    }


}
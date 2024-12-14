package com.example.dosirakbe.domain.store.controller;


import com.example.dosirakbe.domain.store.dto.request.StoreRequest;
import com.example.dosirakbe.domain.store.dto.response.StoreDetailResponse;
import com.example.dosirakbe.domain.store.dto.response.StoreResponse;
import com.example.dosirakbe.domain.store.service.StoreService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guide/stores")
public class StoreController {

    private final StoreService storeService;


    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResult<StoreDetailResponse>> getStoreDetail(@PathVariable("storeId") Long storeId){
        StoreDetailResponse storeDetailResponse = storeService.getStoreDetail(storeId);
        return ResponseEntity.ok(
                ApiResult.<StoreDetailResponse>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("스토어 상세 페이지 반환")
                        .data(storeDetailResponse)
                        .build()
        );
    }

    @GetMapping(params = "keyword")
    public ResponseEntity<ApiResult<List<StoreResponse>>> searchStores(@RequestParam("keyword") String keyword) {
        List<StoreResponse> stores = storeService.searchStores(keyword);
        return ResponseEntity.ok(
                ApiResult.<List<StoreResponse>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("스토어 검색 결과 반환")
                        .data(stores)
                        .build()
        );
    }

    @GetMapping(params = "category")
    public ResponseEntity<ApiResult<List<StoreResponse>>> getStoresByCategory(@RequestParam("category") String category) {
        List<StoreResponse> stores = storeService.storesByCategory(category);
        return ResponseEntity.ok(
                ApiResult.<List<StoreResponse>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("카테고리별 스토어 반환")
                        .data(stores)
                        .build()
        );
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResult<List<StoreResponse>>> getNearbyStores(@RequestBody StoreRequest storeRequest) {
        List<StoreResponse> stores = storeService.getStoresWithinRadius(storeRequest);
        return ResponseEntity.ok(
                ApiResult.<List<StoreResponse>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("근처 스토어 반환")
                        .data(stores)
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResult<List<StoreResponse>>> getAllStores() {
        List<StoreResponse> stores = storeService.getAllStores();

        return ResponseEntity.ok(
                ApiResult.<List<StoreResponse>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("모든 스토어 반환")
                        .data(stores)
                        .build()
        );
    }



}
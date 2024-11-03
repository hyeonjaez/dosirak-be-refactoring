package com.example.dosirakbe.domain.store.controller;


import com.example.dosirakbe.domain.store.dto.request.StoreRequest;
import com.example.dosirakbe.domain.store.dto.response.StoreDetailResponse;
import com.example.dosirakbe.domain.store.dto.response.StoreResponse;
import com.example.dosirakbe.domain.store.entity.Store;
import com.example.dosirakbe.domain.store.service.StoreService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/api/guide/stores/{storeId}")
    public ResponseEntity<ApiResult<StoreDetailResponse>> getStoreDetail(@PathVariable("storeId") Long storeId) {
        StoreDetailResponse storeDetailResponse = storeService.getStoreDetail(storeId);
        return ResponseEntity.ok(
                ApiResult.<StoreDetailResponse>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("스토어 상세 페이지 반환")
                        .data(storeDetailResponse)
                        .build()
        );
    }

    @GetMapping("/api/guide/stores/search")
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

    @GetMapping("/api/guide/stores/filter")
    public ResponseEntity<ApiResult<List<StoreResponse>>> getStoresByCategory(@RequestParam("storeCategory") String storeCategory) {
        List<StoreResponse> stores = storeService.storesByCategory(storeCategory);
        return ResponseEntity.ok(
                ApiResult.<List<StoreResponse>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("카테고리별 스토어 반환")
                        .data(stores)
                        .build()
        );
    }

    @GetMapping("/api/guide/stores/nearby")
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

    @GetMapping("/api/guide/stores/all")
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

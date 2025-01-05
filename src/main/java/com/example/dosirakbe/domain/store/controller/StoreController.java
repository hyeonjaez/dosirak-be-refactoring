package com.example.dosirakbe.domain.store.controller;


import com.example.dosirakbe.domain.store.dto.request.StoreRequest;
import com.example.dosirakbe.domain.store.dto.response.StoreDetailResponse;
import com.example.dosirakbe.domain.store.dto.response.StoreResponse;
import com.example.dosirakbe.domain.store.service.StoreService;
import com.example.dosirakbe.global.util.ApiResult;
import com.example.dosirakbe.global.util.StatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.store.controller<br>
 * fileName       : StoreController<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 가게 관련 정보를 처리하는 CRUD controller 클래스 입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guide/stores")
public class StoreController {

    private final StoreService storeService;

    /**
     * 특정 가게의 상세 정보를 반환합니다.
     *
     * <p>
     * 이 메서드는 고유한 가게 ID를 기반으로 가게의 상세 정보를 조회합니다.
     * </p>
     *
     * @param storeId 조회할 가게의 고유 ID
     * @return 가게의 상세 정보를 포함한 {@link ApiResult} 형태의 {@link StoreDetailResponse}
     */


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

    /**
     * 키워드로 가게를 검색합니다.
     *
     * <p>
     * 이 메서드는 사용자가 입력한 키워드를 기반으로 가게를 검색하여 결과를 반환합니다.
     * </p>
     *
     * @param keyword 검색할 키워드
     * @return 검색 결과를 포함한 {@link ApiResult} 형태의 {@link List} 객체
     */

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

    /**
     * 카테고리로 가게를 조회합니다.
     *
     * <p>
     * 이 메서드는 특정 카테고리에 속한 가게 목록을 반환합니다.
     * </p>
     *
     * @param category 검색할 카테고리
     * @return 카테고리에 해당하는 가게 목록을 포함한 {@link ApiResult} 형태의 {@link List} 객체
     */

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

    /**
     * 특정 반경 내의 가게를 조회합니다.
     *
     * <p>
     * 이 메서드는 사용자가 현재 위치를 기반으로 근처 가게 목록을 반환합니다.
     * </p>
     *
     * @param currentMapX, currentMapY 현재 사용자의 위도 경도 값
     * @return 사용자의 현재 위치 기반 반경 내 스토어 목록을 포함한 {@link ApiResult} 형태의 {@link List} 객체
     */

    @GetMapping("/nearby")
    public ResponseEntity<ApiResult<List<StoreResponse>>> getNearbyStores(
            @RequestParam(name = "currentMapX") double currentMapX,
            @RequestParam(name = "currentMapY") double currentMapY) {

        List<StoreResponse> stores = storeService.getStoresWithinRadius(currentMapX, currentMapY);
        return ResponseEntity.ok(
                ApiResult.<List<StoreResponse>>builder()
                        .status(StatusEnum.SUCCESS)
                        .message("근처 스토어 반환")
                        .data(stores)
                        .build()
        );
    }


    /**
     * 모든 스토어 정보를 반환합니다.
     *
     * <p>
     * 이 메서드는 데이터베이스에 저장된 모든 가게의 목록을 반환합니다.
     * </p>
     *
     * @return 전체 스토어 목록을 포함한 {@link ApiResult} 형태의 {@link List} 객체
     */

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
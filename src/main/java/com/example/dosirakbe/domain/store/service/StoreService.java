package com.example.dosirakbe.domain.store.service;

import com.example.dosirakbe.domain.menu.dto.response.MenuResponse;
import com.example.dosirakbe.domain.menu.entity.Menu;
import com.example.dosirakbe.domain.menu.repository.MenuRepository;
import com.example.dosirakbe.domain.store.dto.request.StoreRequest;
import com.example.dosirakbe.domain.store.dto.response.StoreDetailResponse;
import com.example.dosirakbe.domain.store.dto.response.StoreResponse;
import com.example.dosirakbe.domain.store.entity.Store;
import com.example.dosirakbe.domain.store.repository.StoreRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public List<StoreResponse> searchStores(String keyword) {
        List<Store> stores = storeRepository.searchStoresByKeyword(keyword);

        if (stores.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        return stores.stream()
                .map(this::changeToStoreResponse)
                .collect(Collectors.toList());
    }

    public List<StoreResponse> storesByCategory(String storeCategory) {
        List<Store> stores = storeRepository.findByStoreCategory(storeCategory);

        if (stores.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        return stores.stream()
                .map(this::changeToStoreResponse)
                .collect(Collectors.toList());
    }

    private StoreResponse changeToStoreResponse(Store store) {
        return new StoreResponse(
                store.getStoreId().toString(),
                store.getStoreName(),
                store.getStoreImg(),
                store.getStoreCategory(),
                store.getIfValid(),
                store.getIfReward(),
                store.getOperationTime(),
                store.getMapX(),
                store.getMapY()
        );
    }

    @Transactional(readOnly = true)
    public List<Store> getStoresWithinRadius(StoreRequest storeRequest) {
        double latitude = storeRequest.getCurrentMapX();
        double longitude = storeRequest.getCurrentMapY();

        List<Store> stores = storeRepository.findStoresIn1Km(latitude, longitude);

        if (stores.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        return stores;
    }

    public StoreDetailResponse getStoreDetail(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        List<Menu> menuList = menuRepository.findByStore_StoreId(storeId);

        List<MenuResponse> menuResponses = menuList.stream()
                .map(this::changeToMenuResponse)
                .collect(Collectors.toList());


        return new StoreDetailResponse(
                store.getStoreId().toString(),
                store.getStoreName(),
                store.getStoreCategory(),
                store.getStoreImg(),
                store.getMapX(),
                store.getMapY(),
                store.getOperationTime(),
                store.getTelNumber(),
                store.getIfValid(),
                store.getIfReward(),
                menuResponses
        );
    }

    private MenuResponse changeToMenuResponse(Menu menu) {
        return new MenuResponse(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuImg(),
                menu.getMenuPrice(),
                menu.getMenuPackSize()
        );
    }

    public List<StoreResponse> getAllStores() {
        List<Store> stores = storeRepository.findAll();

        if (stores.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        return stores.stream()
                .map(this::changeToStoreResponse)
                .collect(Collectors.toList());
    }
}








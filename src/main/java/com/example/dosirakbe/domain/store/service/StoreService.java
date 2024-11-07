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
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public List<StoreResponse> searchStores(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }
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

    @Transactional(readOnly = true)
    public List<StoreResponse> getStoresWithinRadius(StoreRequest storeRequest) {
        double latitude = storeRequest.getCurrentMapX();
        double longitude = storeRequest.getCurrentMapY();

        List<Store> stores = storeRepository.findStoresIn1Km(latitude, longitude);
        if (stores.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        return stores.stream()
                .map(this::changeToStoreResponse)
                .collect(Collectors.toList());
    }

    private StoreResponse changeToStoreResponse(Store store) {
        boolean Operating = isStoreOpen(store.getStoreId());
        return new StoreResponse(
                store.getStoreId(),
                store.getStoreName(),
                store.getStoreCategory(),
                store.getStoreImg(),
                store.getIfValid(),
                store.getIfReward(),
                store.getMapX(),
                store.getMapY(),
                Operating
        );
    }



    public StoreDetailResponse getStoreDetail(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        List<Menu> menuList = menuRepository.findByStore_StoreId(storeId);

        List<MenuResponse> menuResponses = menuList.stream()
                .map(this::changeToMenuResponse)
                .collect(Collectors.toList());


        return new StoreDetailResponse(
                store.getStoreId(),
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

    public boolean isStoreOpen(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        try {
            List<Map<String, String>> operationHourList = store.changeOperationTime();
            LocalDateTime now = LocalDateTime.now();

            String currentDay = getDay(now.getDayOfWeek().getValue());
            String operatingHours = "정보없음";

            for (Map<String, String> dayInfo : operationHourList) {
                if (dayInfo.containsKey(currentDay)) {
                    operatingHours = dayInfo.get(currentDay);
                    break;
                }
            }

            if (operatingHours.contains("정보없음") || operatingHours.contains("정기휴무")) {
                return false;
            }

            String[] hours = operatingHours.split(" - ");
            LocalTime openTime = LocalTime.parse(hours[0], DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime closeTime = hours[1].equals("24:00")
                    ? LocalTime.of(23, 59, 59)
                    : LocalTime.parse(hours[1], DateTimeFormatter.ofPattern("HH:mm"));

            if (closeTime.isBefore(openTime)) {
                return LocalTime.now().isAfter(openTime) || LocalTime.now().isBefore(closeTime);
            } else {
                return LocalTime.now().isAfter(openTime) && LocalTime.now().isBefore(closeTime);
            }

        } catch (JsonProcessingException e) {
            throw new ApiException(ExceptionEnum.INTERNAL_SERVER_ERROR);
        }
    }

    private String getDay(int day) {
        switch (day) {
            case 1: return "월";
            case 2: return "화";
            case 3: return "수";
            case 4: return "목";
            case 5: return "금";
            case 6: return "토";
            case 7: return "일";
            default: throw new IllegalArgumentException("없는 요일");
        }
    }





}








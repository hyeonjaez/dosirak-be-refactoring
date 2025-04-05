//package com.example.dosirakbe.domain.store.service;
//
//import com.example.dosirakbe.domain.menu.dto.response.MenuResponse;
//import com.example.dosirakbe.domain.menu.entity.Menu;
//import com.example.dosirakbe.domain.menu.repository.MenuRepository;
//import com.example.dosirakbe.domain.store.dto.response.StoreDetailResponse;
//import com.example.dosirakbe.domain.store.dto.response.StoreResponse;
//import com.example.dosirakbe.domain.store.entity.Store;
//import com.example.dosirakbe.domain.store.repository.StoreRepository;
//import com.example.dosirakbe.global.openai.OpenAiService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * packageName    : com.example.dosirakbe.domain.store.service<br>
// * fileName       : StoreService<br>
// * author         : yyujin1231<br>
// * date           : 10/25/24<br>
// * description    : 가게 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
// * ===========================================================<br>
// * DATE              AUTHOR             NOTE<br>
// * -----------------------------------------------------------<br>
// * 10/25/24        yyujin1231                최초 생성<br>
// */
//
//@Service
//@RequiredArgsConstructor
//public class StoreService {
//
//    private final StoreRepository storeRepository;
//    private final MenuRepository menuRepository;
//    private final OpenAiService openAiService;
//
//    /**
//     * 키워드를 기반으로 가게를 검색합니다.
//     * <p>
//     * 사용자가 입력한 키워드를 기반으로 데이터베이스에서 가게 이름에 해당 키워드가 포함된 가게 목록을 검색합니다.
//     * </p>
//     * @param keyword 검색할 키워드
//     * @return 검색된 가게 목록
//     * @throws ApiException {@link ExceptionEnum#INVALID_REQUEST} 예외 발생 시
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
//     */
//    public List<StoreResponse> searchStores(String keyword) {
//
//        if (keyword == null || keyword.trim().isEmpty()) {
//            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
//        }
//        List<Store> stores = storeRepository.searchStoresByKeyword(keyword);
//        if (stores.isEmpty()) {
//            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
//        }
//        return stores.stream()
//                .map(this::changeToStoreResponse)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 특정 카테고리에 속한 가게 목록을 조회합니다.
//     * <p>
//     * 입력받은 카테고리 이름을 기준으로 데이터베이스에서 해당 카테고리에 속하는
//     * 모든 가게 정보를 조회합니다. 조회 결과가 없으면 예외를 발생시킵니다.
//     * </p>
//     * @param storeCategory 카테고리 이름
//     * @return 해당 카테고리에 속한 가게 목록
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
//     */
//
//    public List<StoreResponse> storesByCategory(String storeCategory) {
//        List<Store> stores = storeRepository.findByStoreCategory(storeCategory);
//
//        if (stores.isEmpty()) {
//            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
//        }
//
//        return stores.stream()
//                .map(this::changeToStoreResponse)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 사용자의 위치를 기준으로 반경 내의 가게를 조회합니다.
//     * <p>
//     * 사용자의 현재 위치를 기준으로 1km 반경 내에 위치한 가게 목록을 조회합니다.
//     * </p>
//     * @param currentMapX 사용자의 현재 위치 X좌표
//     * @param currentMapY 사용자의 현재 위치 Y정보
//     * @return 반경 내의 가게 목록
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
//     */
//
//    @Transactional(readOnly = true)
//    public List<StoreResponse> getStoresWithinRadius(double currentMapX, double currentMapY) {
//
//        List<Store> stores = storeRepository.findStoresIn1Km(currentMapX, currentMapY);
//        if (stores.isEmpty()) {
//            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
//        }
//
//        return stores.stream()
//                .map(this::changeToStoreResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    /**
//     * Store 엔티티를 StoreResponse 객체로 변환합니다.
//     * <p>
//     * 데이터베이스에서 조회된 Store 엔티티를 API 응답용 StoreResponse DTO로 변환합니다.
//     * </p>
//     * @param store 변환할 Store 엔티티
//     * @return 변환된 StoreResponse 객체
//     */
//
//
//    private StoreResponse changeToStoreResponse(Store store) {
//        boolean operating = isStoreOpen(store.getStoreId());
//        return new StoreResponse(
//                store.getStoreId(),
//                store.getStoreName(),
//                store.getStoreCategory(),
//                store.getStoreImg(),
//                store.getIfValid(),
//                store.getIfReward(),
//                store.getMapX(),
//                store.getMapY(),
//                operating
//        );
//    }
//
//    /**
//     * 특정 가게의 상세 정보를 조회합니다.
//     * <p>
//     * 가게 ID를 기반으로 데이터베이스에서 가게 정보를 조회하고,
//     * 해당 가게에 연결된 메뉴 목록을 함께 포함하는 상세 정보를 반환합니다.
//     * </p>
//     * @param storeId 조회할 가게의 ID
//     * @return 가게의 상세 정보
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
//     */
//
//    public StoreDetailResponse getStoreDetail(Long storeId) {
//        Store store = storeRepository.findById(storeId)
//                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
//
//        List<Menu> menuList = menuRepository.findByStore_StoreId(storeId);
//        List<MenuResponse> menuResponses = menuList.stream()
//                .map(menu -> {
//                    try {
//                        return changeToMenuResponse(menu);
//                    } catch (Exception e) {
//                        return new MenuResponse(
//                                menu.getMenuId(),
//                                menu.getMenuName(),
//                                menu.getMenuImg(),
//                                menu.getMenuPrice(),
//                                "Unknown size"
//                        );
//                    }
//                })
//                .collect(Collectors.toList());
//
//        return new StoreDetailResponse(
//                store.getStoreId(),
//                store.getStoreName(),
//                store.getStoreCategory(),
//                store.getStoreImg(),
//                store.getMapX(),
//                store.getMapY(),
//                store.getOperationTime(),
//                store.getTelNumber(),
//                store.getIfValid(),
//                store.getIfReward(),
//                menuResponses
//        );
//    }
//
//    /**
//     * Menu 엔티티를 MenuResponse 객체로 변환합니다.
//     * <p>
//     * 데이터베이스에서 조회된 Menu 엔티티를 API 응답용 MenuResponse DTO로 변환하며,
//     * OpenAI API를 호출하여 다회용기 추천 정보를 추가합니다.
//     * </p>
//     * @param menu 변환할 Menu 엔티티
//     * @return 변환된 MenuResponse 객체
//     * @throws Exception OpenAI API 호출 중 예외 발생 시 처리
//     */
//
//    private MenuResponse changeToMenuResponse(Menu menu) throws Exception {
//        return new MenuResponse(
//                menu.getMenuId(),
//                menu.getMenuName(),
//                menu.getMenuImg(),
//                menu.getMenuPrice(),
//                openAiService.extractReusableContainerData(menu.getMenuName())
//        );
//    }
//
//    /**
//     * 데이터베이스의 모든 가게 목록을 조회합니다.
//     * <p>
//     * 데이터베이스에 저장된 모든 가게 정보를 조회하여 반환합니다.
//     * </p>
//     * @return 모든 가게 목록
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
//     */
//
//    public List<StoreResponse> getAllStores() {
//        List<Store> stores = storeRepository.findAll();
//
//        if (stores.isEmpty()) {
//            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
//        }
//
//        return stores.stream()
//                .map(this::changeToStoreResponse)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 가게의 현재 운영 상태를 확인합니다.
//     * <p>
//     * 가게 ID를 기준으로 데이터베이스에서 운영 시간을 조회하고,
//     * 현재 시간과 비교하여 가게가 운영 중인지 여부를 반환합니다.
//     * </p>
//     * @param storeId 가게의 ID
//     * @return 운영 중이면 true, 아니면 false
//     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
//     */
//
//    public boolean isStoreOpen(Long storeId) {
//        Store store = storeRepository.findById(storeId)
//                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
//
//        try {
//            List<Map<String, String>> operationHourList = store.changeOperationTime();
//            LocalDateTime now = LocalDateTime.now();
//
//            String currentDay = getDay(now.getDayOfWeek().getValue());
//            String operatingHours = "정보없음";
//
//            for (Map<String, String> dayInfo : operationHourList) {
//                if (dayInfo.containsKey(currentDay)) {
//                    operatingHours = dayInfo.get(currentDay);
//                    break;
//                }
//            }
//
//            if (operatingHours.contains("정보없음") || operatingHours.contains("정기휴무")) {
//                return false;
//            }
//
//            String[] hours = operatingHours.split(" - ");
//            LocalTime openTime = LocalTime.parse(hours[0], DateTimeFormatter.ofPattern("HH:mm"));
//            LocalTime closeTime = hours[1].equals("24:00")
//                    ? LocalTime.of(23, 59, 59)
//                    : LocalTime.parse(hours[1], DateTimeFormatter.ofPattern("HH:mm"));
//
//            if (closeTime.isBefore(openTime)) {
//                return LocalTime.now().isAfter(openTime) || LocalTime.now().isBefore(closeTime);
//            } else {
//                return LocalTime.now().isAfter(openTime) && LocalTime.now().isBefore(closeTime);
//            }
//
//        } catch (JsonProcessingException e) {
//            throw new ApiException(ExceptionEnum.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    /**
//     * 요일 정보를 반환합니다.
//     * <p>
//     * 입력된 숫자 값을 요일 이름으로 변환합니다.
//     * 1: 월, 2: 화, ..., 7: 일 순으로 변환되며,
//     * 잘못된 값이 입력된 경우 예외를 발생시킵니다.
//     * </p>
//     * @param day 요일 숫자 값 (1: 월, 2: 화, ... , 7: 일)
//     * @return 요일 이름 ("월", "화", ... , "일")
//     * @throws IllegalArgumentException 잘못된 요일 값이 입력된 경우 발생
//     */
//
//    private String getDay(int day) {
//        switch (day) {
//            case 1: return "월";
//            case 2: return "화";
//            case 3: return "수";
//            case 4: return "목";
//            case 5: return "금";
//            case 6: return "토";
//            case 7: return "일";
//            default: throw new IllegalArgumentException("없는 요일");
//        }
//    }
//
//
//
//
//
//}
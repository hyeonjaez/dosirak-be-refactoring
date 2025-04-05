//package com.example.dosirakbe.domain.store.service;
//
//import com.example.dosirakbe.domain.menu.entity.Menu;
//import com.example.dosirakbe.domain.menu.repository.MenuRepository;
//import com.example.dosirakbe.domain.store.dto.response.StoreDetailResponse;
//import com.example.dosirakbe.domain.store.dto.response.StoreResponse;
//import com.example.dosirakbe.domain.store.entity.Store;
//import com.example.dosirakbe.domain.store.repository.StoreRepository;
//import com.example.dosirakbe.global.openai.OpenAiService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class StoreServiceTest {
//
//    @Spy
//    @InjectMocks
//    private StoreService storeService;
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @Mock
//    private MenuRepository menuRepository;
//
//    @Mock
//    private OpenAiService openAiService;
//
//    private Store testStore;
//    private Menu testMenu;
//
//    @BeforeEach
//    void setUp() {
//        testStore = new Store();
//        ReflectionTestUtils.setField(testStore, "storeId", 1L);
//        ReflectionTestUtils.setField(testStore, "storeName", "storeName");
//        ReflectionTestUtils.setField(testStore, "storeCategory", "한식");
//        ReflectionTestUtils.setField(testStore, "mapX", 127.0);
//        ReflectionTestUtils.setField(testStore, "mapY", 37.0);
//        ReflectionTestUtils.setField(testStore, "operationTime", "[{\"월\":\"09:00 - 21:00\"}]");
//        ReflectionTestUtils.setField(testStore, "storeImg", "storeImg");
//        ReflectionTestUtils.setField(testStore, "ifValid", "Yes");
//        ReflectionTestUtils.setField(testStore, "ifReward", "Yes");
//
//        testMenu = new Menu();
//        ReflectionTestUtils.setField(testMenu, "menuId", 1L);
//        ReflectionTestUtils.setField(testMenu, "menuName", "menuName");
//        ReflectionTestUtils.setField(testMenu, "menuPrice", 10000);
//        ReflectionTestUtils.setField(testMenu, "menuImg", "menuImg");
//        ReflectionTestUtils.setField(testMenu, "store", testStore);
//    }
//
//    @Test
//    @DisplayName("searchStores - 성공")
//    void searchStores_Success() {
//
//        when(storeRepository.searchStoresByKeyword("store"))
//                .thenReturn(Collections.singletonList(testStore));
//        when(storeRepository.findById(1L))
//                .thenReturn(Optional.of(testStore));
//
//        List<StoreResponse> responses = storeService.searchStores("store");
//
//
//        assertThat(responses).hasSize(1);
//        assertThat(responses.get(0).getStoreName()).isEqualTo("storeName");
//        verify(storeRepository, times(1)).searchStoresByKeyword("store");
//        verify(storeRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("searchStores - 실패 : 데이터 not found")
//    void searchStores_Failure_NoData() {
//        when(storeRepository.searchStoresByKeyword("Invalid")).thenReturn(Collections.emptyList());
//
//        assertThatThrownBy(() -> storeService.searchStores("Invalid"))
//                .isInstanceOf(ApiException.class)
//                .hasMessage(ExceptionEnum.DATA_NOT_FOUND.getMessage());
//        verify(storeRepository, times(1)).searchStoresByKeyword("Invalid");
//    }
//
//    @Test
//    @DisplayName("getStoreDetail - 성공")
//    void getStoreDetail_Success() {
//        when(storeRepository.findById(1L)).thenReturn(Optional.of(testStore));
//        when(menuRepository.findByStore_StoreId(1L)).thenReturn(Collections.singletonList(testMenu));
//
//        StoreDetailResponse response = storeService.getStoreDetail(1L);
//
//        assertThat(response.getStoreName()).isEqualTo("storeName");
//        assertThat(response.getMenus()).hasSize(1);
//        assertThat(response.getMenus().get(0).getMenuName()).isEqualTo("menuName");
//        verify(storeRepository, times(1)).findById(1L);
//        verify(menuRepository, times(1)).findByStore_StoreId(1L);
//    }
//
//    @Test
//    @DisplayName("getStoreDetail - 실패 : 데이터 not found")
//    void getStoreDetail_NoData() {
//        when(storeRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> storeService.getStoreDetail(99L))
//                .isInstanceOf(ApiException.class)
//                .hasMessage(ExceptionEnum.DATA_NOT_FOUND.getMessage());
//        verify(storeRepository, times(1)).findById(99L);
//    }
//
//    @Test
//    @DisplayName("isStoreOpen - 운영 중")
//    void isStoreOpen_Operating() {
//        when(storeRepository.findById(1L)).thenReturn(Optional.of(testStore));
//        ReflectionTestUtils.setField(testStore, "operationTime",
//                "[{\"월\":\"00:00 - 23:59\"}, {\"화\":\"00:00 - 23:59\"}, {\"수\":\"00:00 - 23:59\"}, {\"목\":\"00:00 - 23:59\"}, {\"금\":\"00:00 - 23:59\"}, {\"토\":\"00:00 - 23:59\"}, {\"일\":\"00:00 - 23:59\"}]");
//
//
//        boolean isOpen = storeService.isStoreOpen(1L);
//
//        assertThat(isOpen).isTrue();
//        verify(storeRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("isStoreOpen - 실패: 데이터 not found")
//    void isStoreOpen_Failure_NoStore() {
//
//        when(storeRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> storeService.isStoreOpen(99L))
//                .isInstanceOf(ApiException.class)
//                .hasMessage(ExceptionEnum.DATA_NOT_FOUND.getMessage());
//
//        verify(storeRepository, times(1)).findById(99L);
//    }
//
//    @Test
//    @DisplayName("isStoreOpen - 실패: 운영 시간이 '정보없음'")
//    void isStoreOpen_Failure_OperatingHoursNotAvailable() {
//
//        ReflectionTestUtils.setField(testStore, "operationTime", "[{\"월\":\"정보없음\"}]");
//        when(storeRepository.findById(1L)).thenReturn(Optional.of(testStore));
//
//
//        boolean isOpen = storeService.isStoreOpen(1L);
//
//        assertThat(isOpen).isFalse();
//        verify(storeRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("isStoreOpen - 실패: 운영 시간이 정기휴무")
//    void isStoreOpen_Failure_RegularHoliday() {
//
//        ReflectionTestUtils.setField(testStore, "operationTime", "[{\"월\":\"정기휴무\"}]");
//        when(storeRepository.findById(1L)).thenReturn(Optional.of(testStore));
//
//        boolean isOpen = storeService.isStoreOpen(1L);
//
//
//        assertThat(isOpen).isFalse();
//        verify(storeRepository, times(1)).findById(1L);
//    }
//
//
//    @Test
//    @DisplayName("storesByCategory - 성공")
//    void storesByCategory_Success() {
//        when(storeRepository.findByStoreCategory("한식")).thenReturn(Collections.singletonList(testStore));
//        doReturn(false).when(storeService).isStoreOpen(anyLong());
//
//        List<StoreResponse> responses = storeService.storesByCategory("한식");
//
//        assertThat(responses).hasSize(1);
//        assertThat(responses.get(0).getStoreName()).isEqualTo("storeName");
//        verify(storeRepository, times(1)).findByStoreCategory("한식");
//    }
//
//    @Test
//    @DisplayName("storesByCategory - 실패 : 데이터 not found")
//    void storesByCategory_Failure_NoData() {
//        when(storeRepository.findByStoreCategory("Invalid")).thenReturn(Collections.emptyList());
//
//        assertThatThrownBy(() -> storeService.storesByCategory("Invalid"))
//                .isInstanceOf(ApiException.class)
//                .hasMessage(ExceptionEnum.DATA_NOT_FOUND.getMessage());
//        verify(storeRepository, times(1)).findByStoreCategory("Invalid");
//    }
//
//    @Test
//    @DisplayName("getStoresWithinRadius - 성공")
//    void getStoresWithinRadius_Success() {
//
//        when(storeRepository.findStoresIn1Km(127.0, 37.0)).thenReturn(Collections.singletonList(testStore));
//        when(storeRepository.findById(1L)).thenReturn(Optional.of(testStore));
//
//        List<StoreResponse> responses = storeService.getStoresWithinRadius(127.0, 37.0);
//
//        assertThat(responses).hasSize(1);
//        assertThat(responses.get(0).getStoreName()).isEqualTo("storeName");
//        verify(storeRepository, times(1)).findStoresIn1Km(127.0, 37.0);
//        verify(storeRepository, times(1)).findById(1L);
//    }
//
//
//    @Test
//    @DisplayName("getStoresWithinRadius - 실패 : 데이터 not found")
//    void getStoresWithinRadius_Failure_NoData() {
//
//        when(storeRepository.findStoresIn1Km(127.0, 37.0)).thenReturn(Collections.emptyList());
//
//
//        assertThatThrownBy(() -> storeService.getStoresWithinRadius(127.0, 37.0))
//                .isInstanceOf(ApiException.class)
//                .hasMessage(ExceptionEnum.DATA_NOT_FOUND.getMessage());
//        verify(storeRepository, times(1)).findStoresIn1Km(127.0, 37.0);
//    }
//
//
//
//    @Test
//    @DisplayName("모든 가게 조회 성공")
//    void getAllStores_Success() {
//        when(storeRepository.findAll()).thenReturn(Collections.singletonList(testStore));
//        doReturn(false).when(storeService).isStoreOpen(anyLong());
//
//        List<StoreResponse> responses = storeService.getAllStores();
//
//        assertThat(responses).hasSize(1);
//        assertThat(responses.get(0).getStoreName()).isEqualTo("storeName");
//        verify(storeRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("getAllStores - 실패 : 데이터 not found")
//    void getAllStores_Failure_NoData() {
//        when(storeRepository.findAll()).thenReturn(Collections.emptyList());
//
//        assertThatThrownBy(() -> storeService.getAllStores())
//                .isInstanceOf(ApiException.class)
//                .hasMessage(ExceptionEnum.DATA_NOT_FOUND.getMessage());
//        verify(storeRepository, times(1)).findAll();
//    }
//
//
//}

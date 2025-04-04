package com.example.dosirakbe.domain.salestore.service;

import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import com.example.dosirakbe.domain.salestore.repository.SaleStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleStoreServiceTest {

    @InjectMocks
    private SaleStoreService saleStoreService;

    @Mock
    private SaleStoreRepository saleStoreRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getSaleStoresByAddress - 성공")
    void getSaleStoresByAddress_Success() {

        List<SaleStore> mockStores = Arrays.asList(
                new SaleStore(1L, "saleStoreName1", "saleStoreImg1.png", "saleStoreAddress1", "127.0", "37.0", "09:00-21:00", "50%"),
                new SaleStore(2L, "saleStoreName2", "saleStoreImg2.png", "saleStoreAddress2", "128.0", "38.0", "10:00-22:00", "30%")
        );

        when(saleStoreRepository.findBySaleStoreAddressContainingOrderBySaleDiscountDesc("Address"))
                .thenReturn(mockStores);


        List<SaleStore> result = saleStoreService.getSaleStoresByAddress("Address");


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("saleStoreName1", result.get(0).getSaleStoreName());
        assertEquals("50%", result.get(0).getSaleDiscount());
    }

    @Test
    @DisplayName("getSaleStoresByAddress - 실패 : 데이터 not found")
    void getSaleStoresByAddress_Failure_DataNotFound() {

        when(saleStoreRepository.findBySaleStoreAddressContainingOrderBySaleDiscountDesc("InvalidAddress"))
                .thenReturn(Arrays.asList());

        ApiException exception = assertThrows(ApiException.class, () ->
                saleStoreService.getSaleStoresByAddress("InvalidAddress"));

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());
    }
}

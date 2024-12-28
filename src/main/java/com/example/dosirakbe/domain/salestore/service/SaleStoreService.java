package com.example.dosirakbe.domain.salestore.service;


import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import com.example.dosirakbe.domain.salestore.repository.SaleStoreRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.salestore.service<br>
 * fileName       : SaleStoreService<br>
 * author         : yyujin1231<br>
 * date           : 11/08/24<br>
 * description    : 마감음식을 판매하는 가게와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/08/24        yyujin1231                최초 생성<br>
 */

@Service
public class SaleStoreService {

    @Autowired
    private SaleStoreRepository saleStoreRepository;

    /**
     * 특정 주소를 포함하는 마감음식 판매 가게 목록을 조회합니다.
     * <p>
     * 사용자가 입력한 주소가 "동"으로 끝날 경우 이를 제거한 후 검색을 수행합니다.
     * 검색된 가게 목록은 할인율 순으로 정렬됩니다.
     * </p>
     * @param saleStoreAddress 검색할 주소 문자열
     * @return 해당 주소를 포함하며 할인율 순으로 정렬된 가게 목록
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 예외 발생 시
     */

    public List<SaleStore> getSaleStoresByAddress(String saleStoreAddress) {

        if (saleStoreAddress.endsWith("동")) {
            saleStoreAddress = saleStoreAddress.replace("동", "");
        }


        List<SaleStore> saleStores = saleStoreRepository.findBySaleStoreAddressContainingOrderBySaleDiscountDesc(saleStoreAddress);

        if (saleStores.isEmpty()) {
            throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
        }

        return saleStores;
    }
}
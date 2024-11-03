package com.example.dosirakbe.domain.store.dto.response;

import com.example.dosirakbe.domain.menu.dto.response.MenuResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class StoreDetailResponse {

    private Long storeId;
    private String storeName;
    private String storeCategory;
    private String storeImg;
    private double mapX;
    private double mapY;
    private String operationTime;
    private String telNumber;
    private String ifValid;
    private String ifReward;

    private List<MenuResponse> menus;
}

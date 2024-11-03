package com.example.dosirakbe.domain.store.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StoreResponse {

    private Long storeId;
    private String storeName;
    private String storeCategory;
    private String storeImg;
    private String ifValid;
    private String ifReward;
    private String operationTime;
    private double mapX;
    private double mapY;


    public StoreResponse(Long storeId, String storeName, String storeCategory, String storeImg, String ifValid, String ifReward, String operationTime, double mapX, double mapY) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeCategory = storeCategory;
        this.storeImg = storeImg;
        this.ifValid = ifValid ;
        this.ifReward = ifReward;
        this.operationTime = operationTime;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}

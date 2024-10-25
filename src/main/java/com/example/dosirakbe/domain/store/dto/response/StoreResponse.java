package com.example.dosirakbe.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StoreResponse {

    private String storeId;
    private String storeName;
    private String storeCategory;
    private String storeImg;
    private String ifValid;
    private String ifReward;
    private String operationTime;
    private double mapX;
    private double mapY;


    public StoreResponse(String storeId, String storeName, String storeCategory, String storeImg, String ifValid, String ifReward, String operationTime, double mapX, double mapY) {
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

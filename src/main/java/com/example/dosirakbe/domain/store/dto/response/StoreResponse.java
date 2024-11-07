package com.example.dosirakbe.domain.store.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private double mapX;
    private double mapY;
    private boolean Operating;


    public StoreResponse(Long storeId, String storeName, String storeCategory, String storeImg, String ifValid, String ifReward, double mapX, double mapY, boolean Operating) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeCategory = storeCategory;
        this.storeImg = storeImg;
        this.ifValid = ifValid ;
        this.ifReward = ifReward;
        this.mapX = mapX;
        this.mapY = mapY;
        this.Operating = Operating;
    }
}

package com.example.dosirakbe.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponse {

    private Long menuId;
    private String menuName;
    private String menuImg;
    private Integer menuPrice;
    // menuPackSize > 에 다회용기 용량 들어가야함.
    private String menuPackSize;
}
package com.example.dosirakbe.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuResponse {

    private Long menuId;
    private String menuName;
    private String menuImg;
    private String menuPrice;
    private String menuPackSize;
}
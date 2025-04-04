package com.example.dosirakbe.domain.store.controller;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.store.dto.response.StoreDetailResponse;
import com.example.dosirakbe.domain.store.dto.response.StoreResponse;
import com.example.dosirakbe.domain.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StoreController.class)
@WithMockCustomUser
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("특정 가게 상세 정보 조회 api test")
    void getStoreDetail_Success() throws Exception {
        StoreDetailResponse response = new StoreDetailResponse(1L, "라이브볼", "한식", "storeImg", 127.0, 37.0, "[{\"월\": \"10:00 - 20:00\"}, {\"화\": \"10:00 - 20:00\"}, {\"수\": \"10:00 - 20:00\"}, {\"목\": \"10:00 - 20:00\"}, {\"금\": \"10:00 - 20:00\"}, {\"토\": \"정보없음\"}, {\"일\": \"정보없음\"}]", "010-1234-5678", "Yes", "Yes", Collections.emptyList());

        when(storeService.getStoreDetail(1L)).thenReturn(response);

        mockMvc.perform(get("/api/guide/stores/{storeId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.toString()))
                .andExpect(jsonPath("$.data.storeName").value("라이브볼"))
                .andDo(document("get-store-detail-success",
                        pathParameters(
                                parameterWithName("storeId").description("가게 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지"),
                                fieldWithPath("data.storeId").description("가게 ID"),
                                fieldWithPath("data.storeName").description("가게 이름"),
                                fieldWithPath("data.storeCategory").description("가게 카테고리"),
                                fieldWithPath("data.storeImg").description("가게 이미지 URL"),
                                fieldWithPath("data.mapX").description("가게 경도"),
                                fieldWithPath("data.mapY").description("가게 위도"),
                                fieldWithPath("data.operationTime").description("가게 운영 시간"),
                                fieldWithPath("data.telNumber").description("가게 전화번호"),
                                fieldWithPath("data.ifValid").description("다회용기 혜택 여부"),
                                fieldWithPath("data.ifReward").description("리워드 혜택 여부"),
                                fieldWithPath("data.menus").description("가게 메뉴 목록")
                        )
                ));
    }

    @Test
    @DisplayName("키워드로 가게 검색 api test")
    void searchStores_Success() throws Exception {
        StoreResponse store1 = new StoreResponse(1L, "storeName1", "한식", "storeImg1", "Yes", "Yes", 127.0, 37.0, true);
        StoreResponse store2 = new StoreResponse(2L, "storeName2", "일식", "storeImg2", "Yes", "Yes", 128.0, 38.0, false);

        when(storeService.searchStores("store"))
                .thenReturn(Arrays.asList(store1, store2));

        mockMvc.perform(get("/api/guide/stores").param("keyword", "store"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.toString()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andDo(document("search-stores-success",
                        queryParameters(
                                parameterWithName("keyword").description("검색 키워드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지"),
                                fieldWithPath("data[].storeId").description("가게 ID"),
                                fieldWithPath("data[].storeName").description("가게 이름"),
                                fieldWithPath("data[].storeCategory").description("가게 카테고리"),
                                fieldWithPath("data[].storeImg").description("가게 이미지 URL"),
                                fieldWithPath("data[].ifValid").description("다회용기 혜택 여부"),
                                fieldWithPath("data[].ifReward").description("리워드 혜택 여부"),
                                fieldWithPath("data[].mapX").description("가게 경도"),
                                fieldWithPath("data[].mapY").description("가게 위도"),
                                fieldWithPath("data[].operating").description("가게 운영 여부")
                        )
                ));

    }

    @Test
    @DisplayName("카테고리별 가게 조회 api test")
    void getStoresByCategory_Success() throws Exception {
        StoreResponse store = new StoreResponse(1L, "라이브볼", "한식", "storeImg", "Yes", "Yes", 127.0, 37.0, true);

        when(storeService.storesByCategory("한식"))
                .thenReturn(Collections.singletonList(store));

        mockMvc.perform(get("/api/guide/stores").param("category", "한식"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.toString()))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andDo(document("get-stores-by-category-success",
                        queryParameters(
                                parameterWithName("category").description("가게 카테고리")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지"),
                                fieldWithPath("data[].storeId").description("가게 ID"),
                                fieldWithPath("data[].storeName").description("가게 이름"),
                                fieldWithPath("data[].storeCategory").description("가게 카테고리"),
                                fieldWithPath("data[].storeImg").description("가게 이미지 URL"),
                                fieldWithPath("data[].ifValid").description("다회용기 혜택 여부"),
                                fieldWithPath("data[].ifReward").description("리워드 혜택 여부"),
                                fieldWithPath("data[].mapX").description("가게 경도"),
                                fieldWithPath("data[].mapY").description("가게 위도"),
                                fieldWithPath("data[].operating").description("가게 운영 여부")
                        )
                ));

    }

    @Test
    @DisplayName("반경 1km 내 가게 조회 api test")
    void getNearbyStores_Success() throws Exception {

        double currentMapX = 127.0;
        double currentMapY = 37.0;

        List<StoreResponse> stores = Arrays.asList(
                new StoreResponse(1L, "storeName1", "한식", "storeImg1", "Yes", "Yes", 127.0, 37.0, true),
                new StoreResponse(2L, "storeName2", "일식", "storeImg2", "No", "Yes", 127.5, 37.5, false)
        );


        when(storeService.getStoresWithinRadius(eq(currentMapX), eq(currentMapY))).thenReturn(stores);


        mockMvc.perform(get("/api/guide/stores/nearby")
                        .param("currentMapX", String.valueOf(currentMapX))
                        .param("currentMapY", String.valueOf(currentMapY))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.toString()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andDo(document("get-nearby-stores-success",
                        queryParameters(
                                parameterWithName("currentMapX").description("사용자의 현재 위치의 X 좌표"),
                                parameterWithName("currentMapY").description("사용자의 현재 위치의 Y 좌표")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지").optional(),
                                fieldWithPath("data[].storeId").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("data[].storeName").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("data[].storeCategory").type(JsonFieldType.STRING).description("가게 카테고리"),
                                fieldWithPath("data[].storeImg").type(JsonFieldType.STRING).description("가게 이미지 URL"),
                                fieldWithPath("data[].ifValid").type(JsonFieldType.STRING).description("다회용기 혜택 여부"),
                                fieldWithPath("data[].ifReward").type(JsonFieldType.STRING).description("리워드 혜택 여부"),
                                fieldWithPath("data[].mapX").type(JsonFieldType.NUMBER).description("가게 경도"),
                                fieldWithPath("data[].mapY").type(JsonFieldType.NUMBER).description("가게 위도"),
                                fieldWithPath("data[].operating").type(JsonFieldType.BOOLEAN).description("가게 운영 여부")
                        )
                ));
    }


    @Test
    @DisplayName("전체 가게 조회 api test")
    void getAllStores_Success() throws Exception {
        StoreResponse store1 = new StoreResponse(1L, "storeName1", "한식", "storeImg1", "Yes", "Yes", 127.0, 37.0, true);
        StoreResponse store2 = new StoreResponse(2L, "storeName2", "일식", "storeImg2", "Yes", "Yes", 128.0, 38.0, false);

        when(storeService.getAllStores()).thenReturn(Arrays.asList(store1, store2));

        mockMvc.perform(get("/api/guide/stores/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.toString()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andDo(document("get-all-stores-success",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 (SUCCESS)"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("exception").description("에러 메시지"),
                                fieldWithPath("data[].storeId").description("가게 ID"),
                                fieldWithPath("data[].storeName").description("가게 이름"),
                                fieldWithPath("data[].storeCategory").description("가게 카테고리"),
                                fieldWithPath("data[].storeImg").description("가게 이미지 URL"),
                                fieldWithPath("data[].ifValid").description("다회용기 혜택 여부"),
                                fieldWithPath("data[].ifReward").description("리워드 혜택 여부"),
                                fieldWithPath("data[].mapX").description("가게 경도"),
                                fieldWithPath("data[].mapY").description("가게 위도"),
                                fieldWithPath("data[].operating").description("가게 운영 여부")
                        )
                ));
    }

}
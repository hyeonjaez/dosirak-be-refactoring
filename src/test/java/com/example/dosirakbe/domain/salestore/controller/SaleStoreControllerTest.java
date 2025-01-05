package com.example.dosirakbe.domain.salestore.controller;

import com.example.dosirakbe.annotations.WithMockCustomUser;
import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import com.example.dosirakbe.domain.salestore.service.SaleStoreService;
import com.example.dosirakbe.global.util.StatusEnum;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SaleStoreController.class)
@WithMockCustomUser
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class SaleStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleStoreService saleStoreService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("주소로 마감음식 판매 가게 목록 조회 성공 api test")
    void getStoresByAddress_Success() throws Exception {

        List<SaleStore> mockStores = Arrays.asList(
                new SaleStore(1L, "saleStoreName1", "saleStoreImg1.png", "saldStoreAddress1", "127.0", "37.0", "09:00-21:00", "50%"),
                new SaleStore(2L, "saleStoreName2", "saleStoreImg2.png", "saldStoreAddress2", "128.0", "38.0", "10:00-22:00", "30%")
        );

        when(saleStoreService.getSaleStoresByAddress("saleStoreAddress")).thenReturn(mockStores);

        mockMvc.perform(get("/api/salestores")
                        .param("address", "saleStoreAddress")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusEnum.SUCCESS.name()))
                .andExpect(jsonPath("$.message").value("마감음식 판매 가게 반환"))
                .andExpect(jsonPath("$.data[0].saleStoreName").value("saleStoreName1"))
                .andExpect(jsonPath("$.data[0].saleDiscount").value("50%"))
                .andDo(document("salestores/get-by-address",
                        queryParameters(
                                parameterWithName("address").description("조회할 가게 주소")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("exception").type(JsonFieldType.NULL).description("에러 메시지 (항상 null)").optional(),
                                fieldWithPath("data[].saleStoreId").type(JsonFieldType.NUMBER).description("가게 ID"),
                                fieldWithPath("data[].saleStoreName").type(JsonFieldType.STRING).description("가게 이름"),
                                fieldWithPath("data[].saleStoreImg").type(JsonFieldType.STRING).description("가게 이미지"),
                                fieldWithPath("data[].saleStoreAddress").type(JsonFieldType.STRING).description("가게 주소"),
                                fieldWithPath("data[].saleMapX").type(JsonFieldType.STRING).description("가게 경도"),
                                fieldWithPath("data[].saleMapY").type(JsonFieldType.STRING).description("가게 위도"),
                                fieldWithPath("data[].saleOperationTime").type(JsonFieldType.STRING).description("가게 영업 시간"),
                                fieldWithPath("data[].saleDiscount").type(JsonFieldType.STRING).description("가게 할인율")
                        )));
    }
}

package com.fastcampus.befinal.presentation.controller;

import com.fastcampus.befinal.application.facade.SameAdFacade;
import com.fastcampus.befinal.common.config.SecurityConfig;
import com.fastcampus.befinal.domain.service.JwtAuthService;
import com.fastcampus.befinal.presentation.dto.SameAdDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.fastcampus.befinal.common.contant.AuthConstant.USER_AUTHORITY;
import static com.fastcampus.befinal.common.response.success.info.SameAdSuccessCode.FIND_SIMILARITY_DETAIL_SUCCESS;
import static com.fastcampus.befinal.common.response.success.info.SameAdSuccessCode.FIND_SIMILARITY_LIST_SUCCESS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SameAdController 테스트")
@WebMvcTest(SameAdController.class)
@Import(SecurityConfig.class)
public class SameAdControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SameAdFacade sameAdFacade;

    @MockBean
    private JwtAuthService jwtAuthService;

    @MockBean
    private AccessDeniedHandler accessDeniedHandler;

    @Test
    @WithMockUser(authorities = USER_AUTHORITY)
    @DisplayName("동일 광고 유사율 리스트 조회 요청 시, 200 OK 및 정상 응답을 반환")
    void findSimilarityListTest() throws Exception {
        //given
        SameAdDto.InspectionAdInfo inspectionAdInfo = SameAdDto.InspectionAdInfo.builder()
            .id("202407A00001")
            .product("상품명_1")
            .advertiser("광고주_1")
            .category("의류")
            .postDate("2024-06-20")
            .content("어쩌구. 저쩌구.")
            .build();

        SameAdDto.AdSimilarityInfo adSimilarityInfo = SameAdDto.AdSimilarityInfo.builder()
            .id("202312A00001")
            .product("상품명_2")
            .advertiser("광고주_2")
            .category("의류")
            .postDate("2023-11-11")
            .similarityPercent(80)
            .sameSentenceCount(7)
            .build();

        SameAdDto.FindSimilarityListResponse response = SameAdDto.FindSimilarityListResponse.builder()
            .inspectionAdInfo(inspectionAdInfo)
            .adSimilarityInfoList(List.of(adSimilarityInfo))
            .build();

        doReturn(response)
            .when(sameAdFacade)
            .findSimilarityList(anyString());

        //when
        ResultActions perform = mockMvc.perform(get("/api/v1/same-ad/result/202407A00001")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        perform.andExpect(status().is(FIND_SIMILARITY_LIST_SUCCESS.getHttpStatus().value()))
            .andExpect(jsonPath("code").value(FIND_SIMILARITY_LIST_SUCCESS.getCode()))
            .andExpect(jsonPath("message").value(FIND_SIMILARITY_LIST_SUCCESS.getMessage()));
    }

    @Test
    @WithMockUser(authorities = USER_AUTHORITY)
    @DisplayName("동일 광고 유사율 상세보기 조회 요청 시, 200 OK 및 정상 응답을 반환")
    void findSimilarityDetailTest() throws Exception {
        //given
        SameAdDto.FindSimilarityDetailResponse response = SameAdDto.FindSimilarityDetailResponse.builder()
            .content("어쩌구. 저쩌구.")
            .sameSentence("어쩌구.")
            .build();

        doReturn(response)
            .when(sameAdFacade)
            .findSimilarityDetail(anyString(), anyString());

        //when
        ResultActions perform = mockMvc.perform(get("/api/v1/same-ad/result/202407A00001/detail/202312A00001")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        perform.andExpect(status().is(FIND_SIMILARITY_DETAIL_SUCCESS.getHttpStatus().value()))
            .andExpect(jsonPath("code").value(FIND_SIMILARITY_DETAIL_SUCCESS.getCode()))
            .andExpect(jsonPath("message").value(FIND_SIMILARITY_DETAIL_SUCCESS.getMessage()));
    }
}

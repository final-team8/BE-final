package com.fastcampus.befinal.application.service;

import com.fastcampus.befinal.domain.dataprovider.*;
import com.fastcampus.befinal.domain.entity.AdDecision;
import com.fastcampus.befinal.domain.entity.AdProvision;
import com.fastcampus.befinal.domain.entity.Advertisement;
import com.fastcampus.befinal.domain.info.IssueAdInfo;
import com.fastcampus.befinal.presentation.dto.IssueAdDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("IssueAdService 테스트")
@ExtendWith(MockitoExtension.class)
public class IssueAdServiceImplTest {
    @InjectMocks
    private IssueAdServiceImpl issueAdService;

    @Mock
    private AdvertisementReader advertisementReader;

    @Mock
    private AdvertisementStore advertisementStore;

    @Mock
    private AdReviewReader adReviewReader;

    @Mock
    private AdProvisionReader adProvisionReader;

    @Mock
    private AdReviewStore adReviewStore;

    @Mock
    private AdDecisionReader adDecisionReader;

    @Test
    @DisplayName("지적광고 상세조회 성공 테스트")
    void getIssueAdDetailTest(){
        //given
        String advertisementId ="A0001";
        IssueAdInfo.IssueAdDetailInfo detailInfo = IssueAdInfo.IssueAdDetailInfo.builder().build();
        List<IssueAdInfo.IssueAdReviewInfo> reviewInfoList = List.of(IssueAdInfo.IssueAdReviewInfo.builder().build());

        doReturn(detailInfo)
            .when(advertisementReader)
            .findIssueAdDetail(advertisementId);

        doReturn(reviewInfoList)
            .when(adReviewReader)
            .findIssueAdReviewList(advertisementId);

        //when
        issueAdService.findIssueAdDetail(advertisementId);

        //verify
        verify(advertisementReader, times(1)).findIssueAdDetail(advertisementId);
        verify(adReviewReader, times(1)).findIssueAdReviewList(advertisementId);

    }

    @Test
    @DisplayName("지적광고 검수 리뷰 저장 성공 테스트")
    void saveIssueAdReviewsTest(){
        //given
        Long reviewId = (long) 1;
        String advertisementId = "202409A0001";
        Integer provisionId = 1;
        String sentence = "내용1";
        String opinion = "의견1";

        IssueAdDto.IssueAdReview createRequest = IssueAdDto.IssueAdReview.builder()
            .operationType("Create")
            .advertisementId(advertisementId)
            .provisionId(provisionId)
            .sentence(sentence)
            .opinion(opinion)
            .build();

        IssueAdDto.IssueAdReview updateRequest = IssueAdDto.IssueAdReview.builder()
            .operationType("Update")
            .reviewId(reviewId)
            .provisionId(provisionId)
            .sentence(sentence)
            .opinion(opinion)
            .build();

        IssueAdDto.IssueAdReview deleteRequest = IssueAdDto.IssueAdReview.builder()
            .operationType("Delete")
            .reviewId(reviewId)
            .build();

        List<IssueAdDto.IssueAdReview> commandList = List.of(createRequest, updateRequest, deleteRequest);
        IssueAdDto.IssueAdReviewRequest commands = IssueAdDto.IssueAdReviewRequest.builder()
            .reviewList(commandList).build();

        Advertisement advertisement = Advertisement.builder()
            .id(advertisementId).build();

        AdProvision adProvision = AdProvision.builder()
            .id(provisionId).build();

        doReturn(advertisement)
            .when(advertisementReader)
            .findAdvertisementById(anyString());

        doReturn(adProvision)
            .when(adProvisionReader)
            .findAdProvisionById(any());

        doNothing()
            .when(adReviewStore)
            .saveAdReview(any());

        doNothing()
            .when(adReviewStore)
            .updateAdReview(any());

        doNothing()
            .when(adReviewStore)
            .deleteAdReview(any());

        //when
        issueAdService.saveIssueAdReviews(commands);

        //verify
        verify(advertisementReader, times(1)).findAdvertisementById(advertisementId);
        verify(adProvisionReader, times(2)).findAdProvisionById(provisionId);
        verify(adReviewStore, times(1)).saveAdReview(any());
        verify(adReviewStore, times(1)).updateAdReview(any());
        verify(adReviewStore, times(1)).deleteAdReview(any());

    }

    @Test
    @DisplayName("지적광고 심의결정 완료 저장 성공 테스트")
    void saveIssueAdResultDecisionTest(){
        //given
        String advertisementId = "202409A0001";
        Long decisionId = (long) 1;

        IssueAdDto.IssueAdResultDecisionRequest command = IssueAdDto.IssueAdResultDecisionRequest.builder()
            .advertisementId(advertisementId)
            .decisionId(decisionId)
            .build();

        Advertisement advertisement = Advertisement.builder()
            .id(advertisementId).build();

        AdDecision adDecision = AdDecision.builder()
            .id(decisionId).build();

        doReturn(advertisement)
            .when(advertisementReader)
            .findAdvertisementById(advertisementId);

        doReturn(adDecision)
            .when(adDecisionReader)
            .findAdDecisionById(decisionId);

        doNothing()
            .when(advertisementStore)
            .saveIssueAdDecision(any());

        //when
        issueAdService.saveIssueAdResultDecision(command);

        //verify
        verify(adDecisionReader, times(1)).findAdDecisionById(decisionId);
        verify(advertisementReader, times(1)).findAdvertisementById(advertisementId);
        verify(advertisementStore, times(1)).saveIssueAdDecision(any());
    }

    @Test
    @DisplayName("조항 리스트 조회 성공 테스트")
    void findProvisionListTest(){
        //given
        IssueAdInfo.IssueAdProvisionInfo info = IssueAdInfo.IssueAdProvisionInfo.builder()
            .id(1)
            .article(1)
            .content("1항")
            .build();

        IssueAdInfo.IssueAdProvisionListInfo infos = IssueAdInfo.IssueAdProvisionListInfo.builder()
            .provisionList(List.of(info)).build();

        doReturn(infos)
            .when(adProvisionReader)
            .findIssueAdProvisionList();

        //when
        IssueAdInfo.IssueAdProvisionListInfo result = issueAdService.findProvisionList();

        //then
        assertNotNull(result);
        assertEquals(infos.provisionList().getFirst().id(), result.provisionList().getFirst().id());
        assertEquals(infos.provisionList().getFirst().article(), result.provisionList().getFirst().article());
        assertEquals(infos.provisionList().getFirst().content(), result.provisionList().getFirst().content());

        verify(adProvisionReader, times(1)).findIssueAdProvisionList();
    }

    @Test
    @DisplayName("심의결정 리스트 조회 성공 테스트")
    void findDecisionListTest(){
        //given
        IssueAdInfo.IssueAdDecisionInfo info = IssueAdInfo.IssueAdDecisionInfo.builder()
            .id((long)1)
            .decision("의견 1")
            .build();

        IssueAdInfo.IssueAdDecisionListInfo infos = IssueAdInfo.IssueAdDecisionListInfo.builder()
            .decisionList(List.of(info)).build();

        doReturn(infos)
            .when(adDecisionReader)
            .findIssueAdDecisionList();

        //when
        IssueAdInfo.IssueAdDecisionListInfo result = issueAdService.findDecisionList();

        //then
        assertNotNull(result);
        assertEquals(infos.decisionList().getFirst().id(), result.decisionList().getFirst().id());
        assertEquals(infos.decisionList().getFirst().decision(), result.decisionList().getFirst().decision());

        verify(adDecisionReader, times(1)).findIssueAdDecisionList();
    }
}

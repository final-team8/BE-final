package com.fastcampus.befinal.domain.info;

import com.fastcampus.befinal.common.util.ScrollPagination;
import com.fastcampus.befinal.presentation.dto.TaskDto;
import lombok.Builder;

import java.util.List;

public class TaskInfo {

    @Builder
    public record CursorInfo(
            Boolean cursorState,
            String cursorId
    ) {}

    @Builder
    public record TaskResponse (
            AdCountInfo adCount,
            TaskListInfo taskList
    ) {
        public static TaskResponse of(AdCountInfo adCount, TaskListInfo taskList) {
            return TaskResponse.builder()
                    .adCount(adCount)
                    .taskList(taskList)
                    .build();
        }
    }

    @Builder
    public record AdCountInfo (
            Integer myTotalAd,
            Integer myDoneAd,
            Integer myNotDoneAd
    ) {}

    @Builder
    public record AdvertisementListInfo (
            String adId,
            String media,
            String category,
            String product,
            String advertiser,
            Boolean state,
            Boolean issue
    ) {}

    @Builder
    public record SameAdvertisementListInfo(
        String adId,
        String media,
        String category,
        String product,
        String advertiser,
        Boolean same
    ) {}

    @Builder
    public record TaskListInfo(
            Long totalElements,
            CursorInfo cursorInfo,
            List<AdvertisementListInfo> advertisementList
    ){
        public static TaskListInfo of(ScrollPagination<CursorInfo, AdvertisementListInfo> scrollPagination) {
            return TaskListInfo.builder()
                    .totalElements(scrollPagination.totalElements())
                    .cursorInfo(scrollPagination.currentCursorId())
                    .advertisementList(scrollPagination.contents())
                    .build();
        }
    }

    @Builder
    public record SameTaskListInfo(
        Long totalElements,
        String cursorId,
        List<SameAdvertisementListInfo> sameAdvertisementList
    ) {
        public static SameTaskListInfo of(ScrollPagination<String, SameAdvertisementListInfo> scrollPagination) {
            return SameTaskListInfo.builder()
                .totalElements(scrollPagination.totalElements())
                .cursorId(scrollPagination.currentCursorId())
                .sameAdvertisementList(scrollPagination.contents())
                .build();
        }
    }
}

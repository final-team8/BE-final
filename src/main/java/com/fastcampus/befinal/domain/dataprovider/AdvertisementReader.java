package com.fastcampus.befinal.domain.dataprovider;

import com.fastcampus.befinal.domain.entity.Advertisement;
import com.fastcampus.befinal.common.util.ScrollPagination;
import com.fastcampus.befinal.domain.command.TaskCommand;
import com.fastcampus.befinal.domain.info.AdminInfo;
import com.fastcampus.befinal.domain.info.DashboardInfo;
import com.fastcampus.befinal.domain.info.IssueAdInfo;
import com.fastcampus.befinal.domain.info.TaskInfo;

import java.util.List;

public interface AdvertisementReader {
    DashboardInfo.AdCount findAdCount(String userId);
    List<DashboardInfo.DailyDone> findDailyDone(String userId);
    List<DashboardInfo.RecentDone> findRecentDone(String userId);
    ScrollPagination<TaskInfo.CursorInfo, TaskInfo.AdvertisementListInfo> findIssueAdList(TaskCommand.FilterConditionRequest command);
    IssueAdInfo.IssueAdDetailInfo findIssueAdDetail(String advertisementId);
    Advertisement findAdvertisementById(String advertisementId);
    ScrollPagination<String, AdminInfo.UnassignedAdInfo> findUnassignedAdScroll(String cursorId);
    Long countUnassigned();
    List<AdminInfo.UnassignedAdIdInfo> findAllUnassignedAdId(Long amount);
    ScrollPagination<String, TaskInfo.SameAdvertisementListInfo> findSameAdList(TaskCommand.SameAdFilterConditionRequest command);
}

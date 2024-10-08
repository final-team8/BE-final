package com.fastcampus.befinal.application.mapper;

import com.fastcampus.befinal.domain.command.TaskCommand;
import com.fastcampus.befinal.domain.info.TaskInfo;
import com.fastcampus.befinal.presentation.dto.TaskDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserTaskDtoMapper {
    TaskDto.TaskResponse from(TaskInfo.TaskResponse myTask);
    TaskDto.AdCountInfo from(TaskInfo.AdCountInfo adCount);
    TaskDto.AdvertisementListInfo from(TaskInfo.AdvertisementListInfo advertisementList);
    TaskDto.TaskListInfo from(TaskInfo.TaskListInfo findMyTaskListResponse);
    TaskDto.IssueAdvertisementListInfo from(TaskInfo.IssueAdvertisementListInfo advertisementList);
    TaskDto.IssueAdListInfo from(TaskInfo.IssueAdListInfo findMyTaskListResponse);

    TaskCommand.FilterConditionRequest toTaskCommand(TaskDto.FilterConditionRequest request);
}

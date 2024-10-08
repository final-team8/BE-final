package com.fastcampus.befinal.application.facade;

import com.fastcampus.befinal.application.mapper.FilterDtoMapper;
import com.fastcampus.befinal.common.annotation.Facade;
import com.fastcampus.befinal.domain.info.FilterInfo;
import com.fastcampus.befinal.domain.service.FilterService;
import com.fastcampus.befinal.presentation.dto.FilterDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class FilterFacade {
    private final FilterService filterService;
    private final FilterDtoMapper filterDtoMapper;

    public List<FilterDto.FilterOptionResponse> searchMediaOptions(FilterDto.ConditionRequest request) {
        List<FilterInfo.FilterOptionInfo> filterInfo = filterService.searchMediaOptions(filterDtoMapper.toFilterCommand(request));
        return filterDtoMapper.from(filterInfo);
    }

    public List<FilterDto.FilterOptionResponse> searchCategoryOptions(FilterDto.ConditionRequest request) {
        List<FilterInfo.FilterOptionInfo> filterInfo = filterService.searchCategoryOptions(filterDtoMapper.toFilterCommand(request));
        return filterDtoMapper.from(filterInfo);
    }
}

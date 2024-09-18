package com.fastcampus.befinal.common.response.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum IssueAdCode implements Code{
    //success
    GET_ADVERTISEMENT_DETAIL_SUCCESS(3400),
    GET_ISSUE_ADVERTISEMENT_LIST_SUCCESS(3405),
    //error
    NOT_FOUND_ADVERTISEMENT_ID(3450);

    private final Integer code;

    @Override
    public Integer getCode() { return code;}
}

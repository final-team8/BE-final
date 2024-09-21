package com.fastcampus.befinal.common.response.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SameAdCode implements Code{
    //success
    GET_SAME_ADVERTISEMENT_LIST_SUCCESS(3700);

    private final Integer code;

    @Override
    public Integer getCode() { return code;}
}

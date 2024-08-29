package com.fastcampus.befinal.common.util;

import jakarta.validation.GroupSequence;

@GroupSequence({RequestValidationGroups.NotEmptyGroup.class, RequestValidationGroups.NotBlankGroup.class,
    RequestValidationGroups.SizeGroup.class, RequestValidationGroups.PatternGroup.class})
public interface DefaultGroupSequence {
}

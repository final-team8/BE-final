package com.fastcampus.befinal.domain.command;

import com.fastcampus.befinal.common.type.CertificationType;
import lombok.Builder;

public class AuthCommand {
    @Builder
    public record SignUpRequest(
        String name,
        String phoneNumber,
        String id,
        String password,
        String empNo,
        String email
    ) {}

    @Builder
    public record CheckIdDuplicationRequest(
        String id
    ) {}

    @Builder
    public record UpdateCheckListRequest(
        CertificationType certificationType,
        String phoneNumber
    ) {}

    @Builder
    public record CheckCertificationNumberRequest(
        String phoneNumber,
        String certificationNumber
    ) {}

    public record SignInRequest(
        String userId,
        String password
    ) {}
}

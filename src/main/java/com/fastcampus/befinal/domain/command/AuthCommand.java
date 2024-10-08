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
        String email,
        String idCheckToken,
        String certificationNumberCheckToken
    ) {}

    @Builder
    public record CheckIdDuplicationRequest(
        String id
    ) {}

    @Builder
    public record CheckCertificationNumberRequest(
        CertificationType type,
        String phoneNumber,
        String certificationNumber
    ) {}

    @Builder
    public record SignInRequest(
        String id,
        String password
    ) {}

    @Builder
    public record FindIdRequest(
        String name,
        String phoneNumber,
        String certNoCheckToken
    ) {}

    @Builder
    public record FindPasswordRequest(
        String userId,
        String name,
        String phoneNumber,
        String certNoCheckToken
    ) {}

    @Builder
    public record EditPasswordRequest(
        String password,
        String passwordResetToken
    ) {}
}

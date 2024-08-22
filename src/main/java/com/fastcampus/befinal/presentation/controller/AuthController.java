package com.fastcampus.befinal.presentation.controller;

import com.fastcampus.befinal.application.facade.AuthFacade;
import com.fastcampus.befinal.common.annotation.SwaggerErrorCodeExamples;
import com.fastcampus.befinal.common.response.AppApiResponse;
import com.fastcampus.befinal.common.response.ResponseEntityFactory;
import com.fastcampus.befinal.common.response.error.info.JwtErrorCode;
import com.fastcampus.befinal.presentation.dto.AuthDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fastcampus.befinal.common.contant.AuthConstant.SWAGGER_REISSUE_RESPONSE_ACCESSTOKEN;
import static com.fastcampus.befinal.common.contant.AuthConstant.SWAGGER_REISSUE_RESPONSE_REFRESHTOKEN;
import static com.fastcampus.befinal.common.response.success.info.AuthSuccessCode.REISSUE_JWT;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping("/reissue")
    @Operation(summary = "JWT 토큰 재발급")
    @ApiResponse(responseCode = "200", description = "JWT 재발급되었습니다.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ " +
                                    "\"code\": 1100, " +
                                    "\"message\": \"JWT 재발급되었습니다.\", " +
                                    "\"data\": { " +
                                        "\"accessToken\": \"" + SWAGGER_REISSUE_RESPONSE_ACCESSTOKEN + "\", " +
                                        "\"refreshToken\": \"" + SWAGGER_REISSUE_RESPONSE_REFRESHTOKEN + "\" " +
                                    "} " +
                                "}"
                )
            )
    )
    @SwaggerErrorCodeExamples({JwtErrorCode.EXPIRED_JWT, JwtErrorCode.ILLEGAL_JWT})
    public ResponseEntity<AppApiResponse<AuthDto.ReissueJwtResponse>> reissueAccessToken(
        @RequestBody
        @Validated
        AuthDto.ReissueJwtRequest request) {
        AuthDto.ReissueJwtResponse response = authFacade.reissueJwt(request);
        return ResponseEntityFactory.toResponseEntity(REISSUE_JWT, response);
    }
}

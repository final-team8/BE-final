package com.fastcampus.befinal.common.config;

import com.fastcampus.befinal.common.annotation.SwaggerErrorCodeExamples;
import com.fastcampus.befinal.common.response.AppApiResponse;
import com.fastcampus.befinal.common.response.error.info.ErrorCode;
import com.fastcampus.befinal.common.util.ExampleHolder;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@OpenAPIDefinition(
        info = @Info(
                title = "Final API Docs",
                description = "Implemented API Specification",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
                .addSecuritySchemes(jwt, new SecurityScheme()
                        .name(jwt)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat(jwt));

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    @Bean
    public OperationCustomizer customizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            SwaggerErrorCodeExamples swaggerErrorCodeExamples = handlerMethod.getMethodAnnotation(SwaggerErrorCodeExamples.class);

            //SwaggerErrorCodeExamples 어노테이션이 붙은 경우
            if (swaggerErrorCodeExamples != null) {
                generateErrorCodeResponseExample(operation, swaggerErrorCodeExamples.value());
//            } else {
//                SwaggerErrorCodeExample swaggerErrorCodeExample = handlerMethod.getMethodAnnotation(SwaggerErrorCodeExample.class);
//
//                //SwaggerErrorCodeExample 어노테이션만 붙어 있는 경우
//                if (swaggerErrorCodeExample != null) {
//                    generateErrorCodeResponseExample(operation, swaggerErrorCodeExample.value());
//                }
            }

            return operation;
        };
    }

//    @Getter
//    @Builder
//    public class ExampleHolder {
//        // 스웨거의 Example 객체입니다. 위 스웨거 분석의 Example Object 참고.
//        private Example holder;
//        private String name;
//        private int code;
//    }

    // 여러 응답이 존재하는 경우
    private void generateErrorCodeResponseExample(Operation operation, ErrorCode[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorCodes)
            .map(
                errorCode -> ExampleHolder.builder()
                    .holder(getSwaggerExample(errorCode))
                    .name(errorCode.getCode().toString())
                    .code(errorCode.getHttpStatus().value())
                    .build()
            )
            .collect(Collectors.groupingBy(ExampleHolder::getCode));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

//    // 단일 응답이 존재하는 경우
//    private void generateErrorCodeResponseExample(Operation operation, JwtErrorCode authErrorCode) {
//        ApiResponses responses = operation.getResponses();
//
//        // ExampleHolder 객체 생성 및 ApiResponses에 추가
//        ExampleHolder exampleHolder = ExampleHolder.builder()
//            .holder(getSwaggerExample(authErrorCode))
//            .name(authErrorCode.name())
//            .code(authErrorCode.getHttpStatus().value())
//            .build();
//
//        addExamplesToResponses(responses, exampleHolder);
//    }

    // ErrorResponseDto 형태의 예시 객체 생성
    private Example getSwaggerExample(ErrorCode errorCode) {
        AppApiResponse appApiResponse = AppApiResponse.of(errorCode);
        Example example = new Example();
        example.setValue(appApiResponse);

        return example;
    }

    // exampleHolder를 ApiResponses에 추가 - 여러 응답
    private void addExamplesToResponses(ApiResponses responses,
                                        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
            (status, v) -> {
                Content content = new Content();
                MediaType mediaType = new MediaType();
                ApiResponse apiResponse = new ApiResponse();

                v.forEach(
                    exampleHolder -> mediaType.addExamples(
                        exampleHolder.getName(),
                        exampleHolder.getHolder()
                    )
                );
                content.addMediaType("application/json", mediaType);
                apiResponse.setContent(content);
                responses.addApiResponse(String.valueOf(status), apiResponse);
            }
        );
    }

//    // exampleHolder를 ApiResponses에 추가 - 단일 응답
//    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
//        Content content = new Content();
//        MediaType mediaType = new MediaType();
//        ApiResponse apiResponse = new ApiResponse();
//
//        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
//        content.addMediaType("application/json", mediaType);
//        apiResponse.content(content);
//        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
//    }
}
package com.fastcampus.befinal.common.config;

import co.elastic.clients.elasticsearch.nodes.Http;
import com.fastcampus.befinal.common.filter.JwtAuthenticationFilter;
import com.fastcampus.befinal.domain.service.JwtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

import static com.fastcampus.befinal.common.contant.AuthConstant.ADMIN_AUTHORITY;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthService jwtAuthService;
    private final AccessDeniedHandler accessDeniedHandler;

    private final List<String> SWAGGER = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)-> web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .requestMatchers(SWAGGER.toArray(new String[0]));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(configurer -> configurer.disable());

        http.authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
            // auth
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/signup").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/check-id").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/cert-no").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/check-cert-no").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/signin").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/v1/auth/reissue").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/find-id").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/find-password").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/edit-password").permitAll()
            // dashboard
            .requestMatchers(HttpMethod.GET, "/api/v1/dashboard").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/v1/dashboard/admin").hasAuthority(ADMIN_AUTHORITY)
            .requestMatchers(HttpMethod.GET, "/api/v1/dashboard/username-list").authenticated()
            .requestMatchers(HttpMethod.GET, "api/v1/dashboard/daily-list/**").authenticated()
            // my-task
            .requestMatchers(HttpMethod.POST, "api/v1/user/my-task").authenticated()
            // admin
            .requestMatchers("/api/v1/admin/**").hasAuthority(ADMIN_AUTHORITY)
            // user
            .requestMatchers(HttpMethod.PUT, "/api/v1/user/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/user/**").authenticated()
            // issue-ad
            .requestMatchers(HttpMethod.GET,"/api/v1/issue-ad/result/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/issue-ad").authenticated()
            // same-ad
            .requestMatchers(HttpMethod.GET, "/api/v1/same-ad/result/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/same-ad").authenticated()
            // filter-options
            .requestMatchers(HttpMethod.GET, "/api/v1/filter-options/**").authenticated()
            .requestMatchers(HttpMethod.GET,"/api/v1/issue-ad/options/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/issue-ad/save-task").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/v1/issue-ad/result/decision").authenticated()
            // health-check
            .requestMatchers(HttpMethod.GET, "/api/health-check").permitAll()
            // monitoring
            .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll());

        http.addFilterBefore(new JwtAuthenticationFilter(jwtAuthService), BasicAuthenticationFilter.class);

        http.exceptionHandling(configurer -> configurer
            .accessDeniedHandler(accessDeniedHandler));

        http.sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

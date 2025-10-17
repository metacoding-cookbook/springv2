package com.metacoding.springv2.core.config;

import com.metacoding.springv2.core.filter.JwtAuthorizationFilter;
import com.metacoding.springv2.core.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedOriginPatterns(List.of(allowedOrigins.split(","))); // 허용할 출처를 명시적으로 지정
        // 이 설정이 없으면, CORS 요청 시 브라우저는 쿠키, HTTP 인증 헤더(Authorization 등)를 함께 보내지 않습니다.
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config); // 모든 /api/** 경로에 CORS 설정 적용
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. iframe 허용 설정: 동일 출처(sameOrigin)의 iframe만 허용
        // H2 Console이나 다른 iframe 기반 기능을 사용하기 위해 필요합니다.
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // 2. 폼 로그인 비활성화: JWT를 사용하므로 form 태그를 통한 로그인 방식을 사용하지 않습니다.
        http.formLogin(form -> form.disable());

        // 3. HTTP 기본 인증 비활성화: 폼 로그인을 비활성화하면 기본으로 설정되는 HTTP Basic 인증을 끕니다.
        // JWT를 통해 인증하므로 Basic 인증도 필요하지 않습니다.
        http.httpBasic(basic -> basic.disable());

        // 4. CSRF 비활성화: RESTful API이므로 CSRF 공격을 방어할 필요가 적어 비활성화합니다.
        // 클라이언트(React, Vue 등)가 CSRF 토큰 없이도 요청할 수 있게 됩니다.
        http.csrf(configure -> configure.disable());

        // 5. 세션 관리 정책 설정: JWT를 사용하므로 세션을 사용하지 않도록 STATELESS로 설정합니다.
        // SessionCreationPolicy의 기본값은 IF_REQUIRED입니다. (종류 : Thread방식, Session방식)
        // 서버가 클라이언트의 상태를 유지하지 않게 됩니다.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 6. 필터 추가: JWT 인증 필터를 UsernamePasswordAuthenticationFilter 이전에 등록하여 JWT 유효성 검사를 수행합니다.
        http.addFilterBefore(new JwtAuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        // 7. CORS 필터 설정
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 8. 예외 처리 핸들러 설정: 인증 및 권한 오류 발생 시 커스텀 응답을 반환합니다.
        // 401 (인증 실패)와 403 (권한 부족) 에러에 대한 응답을 처리합니다.
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> RespFilter.fail(response, 401, "로그인 후 이용해주세요"))
                .accessDeniedHandler((request, response, accessDeniedException) -> RespFilter.fail(response, 403, "권한이 없습니다"))
        );

        // 9. 권한 부여 설정: URL 패턴에 따라 접근 권한을 정의합니다. (AuthorizationFilter)
        // 가장 구체적인 경로를 먼저 설정해야 올바르게 동작합니다.
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**", "/api/boards/**", "/api/replies/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll()
        );

        return http.build();
    }
}
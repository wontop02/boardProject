package com.wontop.board_project.config;

import com.wontop.board_project.jwt.JwtTokenFilter;
import com.wontop.board_project.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (JWT 사용할 땐 보통 비활성화)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/login", "/api/user/join", "/api/posts").permitAll() // 로그인 & 회원가입, 게시물 목록은 누구나 접근 가능
                //requestMatchers("/api/post/create").hasRole("ROLE_USER") // USER 역할 필요
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("http://localhost:3000"); // React 앱 도메인
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 요청 메소드
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "*")); // 요청 헤더

        // CORS 설정을 UrlBasedCorsConfigurationSource에 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // 모든 엔드포인트에 CORS 설정 적용

        return source;
    }
}
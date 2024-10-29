package com.example.breakApp.common.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

// JWT 인증 방식을 사용하기 위해 세션을 비활성화하고, 특정 경로에 대해 권한을 설정하는 클래스


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 기본 로그인 화면 비활성화
            .httpBasic { it.disable() }
            // CSRF 보안 비활성화 (JWT를 사용할 경우 필요 없음)
            .csrf { it.disable() }
            // 세션 사용 안 함 (JWT로 상태 비저장 방식 사용)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                // 회원가입과 로그인 요청은 인증 없이 접근 허용
                it.requestMatchers("/api/member/signup", "/api/member/login").anonymous()
                    // '/api/member/**' 경로는 MEMBER 권한이 필요
                    .requestMatchers("/api/member/**").hasRole("MEMBER")
                    // 나머지 요청은 모두 허용
                    .anyRequest().permitAll()
            }
            // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
            // 필터 통과 시 뒷 필터는 사용 안함
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    // 비밀번호 암호화에 사용할 PasswordEncoder 빈 설정
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
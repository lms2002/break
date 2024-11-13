package com.example.breakApp.common.authority

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

/**
 * 들어오는 요청에서 JWT 토큰을 추출하여 유효성을 검사한 후 인증 정보를 SecurityContext에 저장 후
 * doFilter 메서드를 통해 각 요청마다 이 필터를 거치게 하여 인증을 유지하는 역할
 */

// JWT 인증 필터 클래스 정의
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {

    // 필터의 핵심 메서드로, 요청이 들어올 때마다 호출됨
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        // 요청에서 JWT 토큰을 추출
        val token = resolveToken(request as HttpServletRequest)

        // 토큰이 유효한 경우, 인증 정보를 SecurityContext에 저장
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        // 다음 필터로 요청과 응답 전달
        chain?.doFilter(request, response)
    }

    // 요청 헤더에서 JWT 토큰을 추출
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")

        // 토큰이 "Bearer "로 시작하는지 확인하고, 맞으면 "Bearer "를 제외한 토큰 반환
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}
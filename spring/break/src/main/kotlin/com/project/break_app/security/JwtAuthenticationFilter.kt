package com.project.break_app.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * JWT 인증 필터
 * 요청에서 JWT 토큰을 추출하고 검증 후 인증 정보를 SecurityContext에 저장
 */
class JwtAuthenticationFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val authenticationManager: AuthenticationManager
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val jwt = getJwtFromRequest(request) // 요청에서 JWT 추출

        if (jwt != null && jwtTokenUtil.validateToken(jwt)) {
            val username = jwtTokenUtil.getUsernameFromJWT(jwt) // JWT에서 사용자명 추출

            // 인증된 사용자 정보 생성
            val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList())
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response) // 다음 필터로 요청 전달
    }

    /**
     * Authorization 헤더에서 JWT 추출
     */
    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7) // "Bearer " 부분을 제외한 JWT 토큰 반환
        } else null
    }
}

package com.example.breakApp.common.authority

import com.example.breakApp.common.dto.CustomUser
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import kotlin.RuntimeException

// 토큰 만료 기간 30분
const val EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30;

// Refresh Token 만료 기간 (예: 30일)
const val REFRESH_EXPIRATION_MILLISECONDS: Long = 1000L * 60 * 60 * 24 * 30

@Component
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    // 기존 createToken 메서드 (Access Token 생성)
    fun createToken(authentication: Authentication): TokenInfo {
        val authorities: String = authentication
            .authorities
            .joinToString(",", transform = GrantedAuthority::getAuthority)

        val now = Date()
        val accessExpiration = Date(now.time + EXPIRATION_MILLISECONDS)
        val refreshExpiration = Date(now.time + REFRESH_EXPIRATION_MILLISECONDS)

        // Access Token 생성
        val accessToken = Jwts.builder()
            .setSubject(authentication.name)
            .claim("auth", authorities)
            .claim("userId", (authentication.principal as CustomUser).userId)
            .setIssuedAt(now)
            .setExpiration(accessExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        // Refresh Token 생성
        val refreshToken = Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(refreshExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        // Access Token과 Refresh Token을 모두 포함하여 반환
        return TokenInfo("Bearer", accessToken, refreshToken)
    }

    // Refresh Token 생성 메서드 추가
    fun createRefreshToken(): String {
        val now = Date()
        val refreshExpiration = Date(now.time + REFRESH_EXPIRATION_MILLISECONDS)

        // Refresh Token 생성
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(refreshExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * Token 정보 추출
     */
    fun getAuthentication(token: String): Authentication {
        val claims: Claims = getClaims(token)

        val userId = claims["userId"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val auth = claims["auth"] as? String ?: ""

        // 권한 정보가 없을 때 빈 리스트 사용
        val authorities: Collection<GrantedAuthority> = if (auth.isNotEmpty()) {
            auth.split(",").map { SimpleGrantedAuthority(it) }
        } else {
            emptyList()
        }

        val principal: UserDetails = CustomUser(userId.toString().toLong(), claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    /**
     * Token 검증
     */
    fun validateToken(token: String): Boolean {
        try {
            getClaims(token) // 클레임 정보 가져오기
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> {}  // Invalid JWT Token
                is MalformedJwtException -> {}  // Invalid JWT Token
                is ExpiredJwtException -> {}    // Expired JWT Token
                is UnsupportedJwtException -> {}    // Unsupported JWT Token
                is IllegalArgumentException -> {}   // JWT claims string is empty
                else -> {}  // else
            }
            println(e.message)
        }
        return false
    }

    /**
     * 토큰에서 사용자 ID 추출
     */
    fun getUserIdFromToken(token: String): Long {
        val claims = getClaims(token)
        return try {
            claims["userId"].toString().toLong() // userId 클레임에서 사용자 ID를 숫자로 변환하여 가져옴
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("유효하지 않은 토큰입니다. 사용자 ID가 숫자 형식이 아닙니다.")
        }
    }

    /**
     * JWT 토큰을 파싱하여 토큰의 본문(Claims) 정보를 반환
     */
    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}
package com.project.break_app.security

import io.jsonwebtoken.*
import org.springframework.stereotype.Component
import java.util.*
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

/**
 * JWT 토큰 생성 및 검증 유틸리티 클래스
 */
@Component
class JwtTokenUtil {

    // 비밀키 생성 (HMAC-SHA512 알고리즘 사용)
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)
    private val jwtExpirationInMs: Long = 3600000 // 1시간 만료

    /**
     * 사용자명으로 JWT 생성
     */
    fun generateToken(userName: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .setSubject(userName)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    /**
     * JWT에서 사용자명 추출
     */
    fun getUsernameFromJWT(token: String): String {
        val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        return claims.subject
    }

    /**
     * JWT 유효성 검증
     */
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            return true
        } catch (ex: Exception) {
            println("Invalid JWT token: ${ex.message}")
        }
        return false
    }
}

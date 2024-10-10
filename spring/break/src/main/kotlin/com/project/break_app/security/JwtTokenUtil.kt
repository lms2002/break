package com.project.break_app.security

import io.jsonwebtoken.*
import org.springframework.stereotype.Component
import java.util.*
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

@Component
class JwtTokenUtil {
    // JWT 서명에 사용할 비밀 키 (보안 강화를 위해 환경 변수로 설정 가능)
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    // 토큰의 유효 시간 (예: 1시간)
    private val jwtExpirationInMs: Long = 3600000 // 1시간

    // JWT 토큰 생성
    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .setSubject(username) // 토큰의 subject에 username 설정
            .setIssuedAt(Date()) // 토큰 발행 시간
            .setExpiration(expiryDate) // 만료 시간 설정
            .signWith(secretKey) // 서명할 때 secretKey 사용
            .compact()
    }

    // JWT 토큰에서 사용자 이름 추출
    fun getUsernameFromJWT(token: String): String {
        val claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        return claims.subject
    }

    // 토큰 유효성 확인
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            return true
        } catch (ex: ExpiredJwtException) {
            println("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            println("Unsupported JWT token")
        } catch (ex: MalformedJwtException) {
            println("Invalid JWT token")
        } catch (ex: SignatureException) {
            println("Invalid JWT signature")
        } catch (ex: IllegalArgumentException) {
            println("JWT claims string is empty.")
        }
        return false
    }
}

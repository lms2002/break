package com.example.breakApp.auth

import com.example.breakApp.common.dto.BaseResponse
import com.example.breakApp.common.authority.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtTokenProvider: JwtTokenProvider
) {
    /**
     * 토큰 유효성 검증 엔드포인트
     * @param token 클라이언트에서 전달한 Authorization 헤더 값 (Bearer 접두사 포함)
     * @return 토큰이 유효한지 여부를 반환
     */
    @PostMapping("/validate-token")
    fun validateToken(@RequestHeader("Authorization") token: String): ResponseEntity<BaseResponse<Boolean>> {
        val jwtToken = token.removePrefix("Bearer ").trim()
        val isValid = jwtTokenProvider.validateToken(jwtToken)
        return ResponseEntity.ok(
            BaseResponse(
                data = isValid,
                message = if (isValid) "Valid Token" else "Invalid Token"
            )
        )
    }
}
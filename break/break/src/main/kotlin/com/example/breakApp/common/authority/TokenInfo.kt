package com.example.breakApp.common.authority

data class TokenInfo(
    val grantType: String, // jwt 인증 권한 타입
    val accessToken: String, // 실제 검증 할 토큰
    val refreshToken: String // Refresh Token 필드 추가
)
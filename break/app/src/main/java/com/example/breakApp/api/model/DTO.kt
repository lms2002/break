package com.example.breakApp.api.model

import java.time.LocalDate

// 아이디 찾기 요청 DTO
data class FindIdRequest(
    val email: String
)

// 비밀번호 재설정 요청 DTO
data class ResetPasswordRequest(
    val email: String
)

// 회원가입 요청 DTO
data class MemberDtoRequest(
    val loginId: String,
    val password: String,
    val userName: String,
    val email: String,
    val gender: String
)

// 로그인 요청 DTO
data class LoginDto(
    val loginId: String,
    val password: String
)

// 회원 정보 조회 응답 DTO
data class MemberDtoResponse(
    val userId: Long,
    val loginId: String,
    val userName: String,
    val email: String,
    val gender: String,
    val createdAt: String,
    val updatedAt: String
)

// 내 정보 수정 요청 DTO
data class UpdateDtoRequest(
    val password: String?,
    val userName: String?,
    val email: String?,
    val gender: String?
)

// 이메일 인증 요청 DTO
data class EmailRequest(
    val email: String
)

// 이메일 인증 확인 요청 DTO
data class VerifyEmailRequest(
    val email: String,
    val token: String
)

// 로그인 응답 DTO
data class TokenInfo(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)

// Refresh Token 요청 DTO
data class RefreshTokenRequest(
    val refreshToken: String
)

// 인바디 생성 요청 DTO
data class CreateInBodyDto(
    val measurementDate: LocalDate?,
    val weight: Double?,
    val bodyFatPercentage: Double?,
    val muscleMass: Double?,
    val bmi: Double?,
    val visceralFatLevel: Double?, // 내장 지방 수준
    val basalMetabolicRate: Double? // 기초 대사량
)

// 인바디 응답 DTO
data class InBodyResponse(
    val inBodyId: Long,
    val measurementDate: LocalDate?,
    val weight: Double?,
    val bodyFatPercentage: Double?,
    val muscleMass: Double?,
    val bmi: Double?,
    val visceralFatLevel: Double?, // 내장 지방 수준
    val basalMetabolicRate: Double?, // 기초 대사량
    val userId: Long // 소유 사용자 ID
)

// 인바디 수정 요청 DTO
data class UpdateInBodyDto(
    val measurementDate: LocalDate?,
    val weight: Double?,
    val bodyFatPercentage: Double?,
    val muscleMass: Double?,
    val bmi: Double?,
    val visceralFatLevel: Double?, // 내장 지방 수준
    val basalMetabolicRate: Double? // 기초 대사량
)


// 이메일 인증 코드 확인 요청 DTO
data class VerifyCodeRequest(
    val email: String,           // 인증할 사용자 이메일
    val verificationCode: String  // 6자리 인증 코드
)

// TokenValidationResponse.kt
data class TokenValidationResponse(
    val data: Boolean,
    val message: String
)
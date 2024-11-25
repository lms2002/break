package com.example.breakApp.api.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

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
    val measurementDate: String?,
    val age: Int?,
    val weight: Double?,
    val bodyFatPercentage: Double?,
    val muscleMass: Double?,
    val bmi: Double?,
    val visceralFatLevel: Double?, // 내장 지방 수준
    val basalMetabolicRate: Double? // 기초 대사량
)

data class MemberResponseDto(
    val userName: String,  // 사용자 이름
    val gender: String     // 사용자 성별
)

// 인바디 응답 DTO
data class InBodyResponse(
    val inBodyId: Long,
    val member: MemberResponseDto,
    val measurementDate: String?,
    val age: Int?,
    val height: Float?,
    val weight: Double?,
    val bodyFatPercentage: Double?,
    val muscleMass: Double?,
    val bmi: Double?
)

// 인바디 수정 요청 DTO
data class UpdateInBodyDto(
    val measurementDate: String?,
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

data class Exercise(
    val exerciseId: Long,
    val name: String,                 // 운동 이름
    val instructions: List<String>?,  // 운동 설명 (여러 단계로 구성된 리스트)
    val category: String,             // 운동 카테고리 (기본값: General)
    val targetArea: String            // 타겟 부위 (기본값: Full Body)
)


data class RoutineDto(
    val routineId: Long? = null,
    val userId: Long,
    val name: String,
    val createdAt: String = getCurrentTimestamp(), // String으로 타임스탬프 저장
    val updatedAt: String = getCurrentTimestamp()
)

data class CreateRoutineExerciseRequest(
    @SerializedName("routine_id")
    val routineId: Long,
    @SerializedName("exercises")
    val exercises: List<ExerciseRequest>
)

data class ExerciseRequest(
    @SerializedName("exercise_id")
    val exerciseId: Long
)



// TokenValidationResponse.kt
data class TokenValidationResponse(
    val data: Boolean,
    val message: String
)

fun getCurrentTimestamp(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date()) // 현재 시간을 포맷
}
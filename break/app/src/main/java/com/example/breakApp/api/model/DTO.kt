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
    val password: String? = null,
    val userName: String? = null,
    val email: String? = null,
    val gender: String? = null
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
    val height: Double?,
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
    val exerciseId: Long,            // 운동 ID
    val name: String,               // 운동 이름
    val bodyPart: String = "other",  // 운동 부위
    val target: String = "other",    // 타겟 부위
    val equipment: String?,          // 사용 장비
    val gifUrl: String?,             // 운동 GIF URL
    val instructions: String?  // 운동 설명
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

data class ExerciseSetDto(
    val setId: Long? = null,
    val routineId: Long,
    val exerciseId: Long,  // 여전히 Long으로 유지
    val setNumber: Int,
    val repetitions: Int,
    val weight: Float,
    val isCompleted: Boolean,
    val createdAt: String
)

data class WorkoutLogDto(
    val logId: Long,
    val routine: RoutineDto,
    val startTime: String, // 문자열로 처리
    val endTime: String?, // Nullable 문자열로 처리
    val duration: Long? // Nullable, 끝난 후에만 계산
)

data class CompletedWorkoutDto(
    val routineId: Long,
    val completedExercises: List<CompletedExerciseDto>
)

data class CompletedExerciseDto(
    val exerciseId: Long,
    val sets: List<CompletedSetDto>
)

data class CompletedSetDto(
    val setId: Long,
    val setNumber: Int,
    val repetitions: Int,
    val weight: Float
)

data class StartWorkoutRequest(
    val routineId: Long
)

data class NotificationDto(
    val notificationId: Long, // 알림 ID
    val message: String, // 알림 메시지
    val type: String, // 알림 유형
    val isRead: Boolean, // 읽음 여부
    val createdAt: String, // 생성 시간 (ISO 8601 형식)
    val deletedAt: String? // 삭제된 시간 (ISO 8601 형식, nullable)
)

fun getCurrentTimestamp(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date()) // 현재 시간을 포맷
}
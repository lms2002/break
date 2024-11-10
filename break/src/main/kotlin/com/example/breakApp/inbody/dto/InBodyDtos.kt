package com.example.breakApp.inbody.dto

import java.time.LocalDate

// InBody 엔티티를 위한 Member 엔티티의 일부 정보만 포함 된 Dto
data class MemberResponseDto(
    val userName: String,  // 사용자 이름
    val gender: String     // 성별
)
// InBody 엔티티의 응답에 필요한 Dto
data class InBodyResponseDto(
    val inBodyId: Long,
    val member: MemberResponseDto,
    val measurementDate: LocalDate?,
    val weight: Double?,
    val bodyFatPercentage: Double?,
    val muscleMass: Double?,
    val bmi: Double?,
    val visceralFatLevel: Double?,
    val basalMetabolicRate: Double?
)
// 인바디 생성 Dto
data class CreateInBodyDto(
    val measurementDate: LocalDate?,         // 측정 날짜
    val weight: Double?,                     // 체중
    val bodyFatPercentage: Double?,          // 체지방률
    val muscleMass: Double?,                 // 근육량
    val bmi: Double?,                        // BMI
    val visceralFatLevel: Double?,           // 내장 지방 수치
    val basalMetabolicRate: Double?          // 기초 대사량
)
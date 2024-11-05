package com.example.breakApp.inbody.dto

import java.time.LocalDate

data class MemberIdDto(
    val userId: Long // 사용자의 ID만 포함
)
data class CreateInBodyDto(
    val userId: Long,                       // 사용자 ID
    val measurementDate: LocalDate,        // 측정 날짜
    val weight: Double,                     // 체중
    val bodyFatPercentage: Double,          // 체지방률
    val muscleMass: Double                  // 근육량
)

data class InBodyResponseDto(
    val inBodyId: Long?,                   // 인바디 ID
    val measurementDate: LocalDate?,       // 측정 날짜
    val weight: Double?,                    // 체중
    val bodyFatPercentage: Double?,         // 체지방률
    val muscleMass: Double?,                // 근육량
    val bmi: Double?,                       // BMI
    val visceralFatLevel: Double?,          // 내장 지방 수치
    val basalMetabolicRate: Double?        // 기초 대사량
)
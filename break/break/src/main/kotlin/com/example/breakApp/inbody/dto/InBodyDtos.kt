package com.example.breakApp.inbody.dto

import java.time.LocalDate

data class MemberIdDto(
    val userId: Long // 사용자의 ID만 포함
)
// 인바디 등록 dto
data class CreateInBodyDto(
    val userId: Long,                       // 사용자 ID (non-null)
    val measurementDate: LocalDate?,        // 측정 날짜 (nullable)
    val weight: Double?,                     // 체중 (nullable)
    val bodyFatPercentage: Double?,          // 체지방률 (nullable)
    val muscleMass: Double?,                 // 근육량 (nullable)
    val bmi: Double?,                        // BMI (nullable)
    val visceralFatLevel: Double?,           // 내장 지방 수치 (nullable)
    val basalMetabolicRate: Double?          // 기초 대사량 (nullable)
)

// 데이터 조회 dto
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
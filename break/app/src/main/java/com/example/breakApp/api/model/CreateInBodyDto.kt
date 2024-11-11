package com.example.breakApp.api.model

data class CreateInBodyDto(
    val userId: Long,
    val measurementDate: String,  // 예시로 String으로 처리 (날짜 포맷을 다르게 처리할 수 있음)
    val weight: Double?,
    val bodyFatPercentage: Double?,
    val muscleMass: Double?,
    val bmi: Double?,
    val visceralFatLevel: Int?,
    val basalMetabolicRate: Double?
)

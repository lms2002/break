package com.example.breakApp.inbody.dto

import java.time.LocalDate

// InBody 엔티티를 위한 Member 엔티티의 일부 정보만 포함된 Dto
data class MemberResponseDto(
    val userName: String,  // 사용자 이름
    val gender: String     // 사용자 성별
)

// InBody 엔티티의 응답에 필요한 Dto
data class InBodyResponseDto(
    val inBodyId: Long,                // 인바디 데이터의 고유 ID
    val member: MemberResponseDto,     // 사용자 정보 (이름 및 성별)
    val measurementDate: LocalDate?,   // 측정 날짜
    val age: Int?,                     // 사용자 나이
    val height: Float?,                // 사용자 키 (cm) - Float로 설정
    val weight: Float?,                // 사용자 체중 (kg) - Float로 설정
    val bodyFatPercentage: Float?,     // 체지방률 (%) - Float로 설정
    val muscleMass: Float?,            // 근육량 (kg) - Float로 설정
    val bmi: Float?                    // 체질량지수 (BMI) - Float로 설정
)

// 인바디 생성 요청 DTO
data class CreateInBodyDto(
    val measurementDate: LocalDate?,  // 측정 날짜
    val age: Int?,                    // 사용자 나이
    val height: Float?,               // 사용자 키 (cm) - Float로 설정
    val weight: Float?,               // 사용자 체중 (kg) - Float로 설정
    val bodyFatPercentage: Float?,    // 체지방률 (%) - Float로 설정
    val muscleMass: Float?,           // 근육량 (kg) - Float로 설정
    val bmi: Float?                   // 체질량지수 (BMI) - Float로 설정
)

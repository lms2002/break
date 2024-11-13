package com.example.breakApp.inbody.entity

import com.example.breakApp.inbody.dto.CreateInBodyDto
import com.example.breakApp.member.entity.Member
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "Inbody")
class InBody(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "in_body_id")
    val inBodyId: Long? = null,                     // 인바디 ID, null 허용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Member의 user_id 외래 키 설정
    val member: Member,                              // 회원 엔티티와 매핑

    @Column(name = "measurement_date")
    var measurementDate: LocalDate? = null,          // 측정 날짜, null 허용

    @Column(name = "weight")
    var weight: Double? = null,                      // 체중, null 허용

    @Column(name = "body_fat_percentage")
    var bodyFatPercentage: Double? = null,           // 체지방률, null 허용

    @Column(name = "muscle_mass")
    var muscleMass: Double? = null,                  // 근육량, null 허용

    @Column(name = "bmi")
    var bmi: Double? = null,                         // BMI, null 허용

    @Column(name = "visceral_fat_level")
    var visceralFatLevel: Double? = null,            // 내장 지방 수치, null 허용

    @Column(name = "basal_metabolic_rate")
    var basalMetabolicRate: Double? = null           // 기초 대사량, null 허용
) {
    fun updateWithDto(dto: CreateInBodyDto) {
        measurementDate = dto.measurementDate
        weight = dto.weight
        bodyFatPercentage = dto.bodyFatPercentage
        muscleMass = dto.muscleMass
        bmi = dto.bmi
        visceralFatLevel = dto.visceralFatLevel
        basalMetabolicRate = dto.basalMetabolicRate
    }
}
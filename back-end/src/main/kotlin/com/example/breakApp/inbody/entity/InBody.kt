package com.example.breakApp.inbody.entity

import com.example.breakApp.inbody.dto.CreateInBodyDto
import com.example.breakApp.member.entity.Member
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "in_body")
class InBody(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "in_body_id")
    val inBodyId: Long? = null, // 인바디 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Member의 user_id 외래 키 설정
    val member: Member, // 회원 엔티티와 매핑

    @Column(name = "measurement_date")
    var measurementDate: LocalDate? = null, // 측정 날짜

    @Column(name = "age")
    var age: Int? = null, // 나이

    @Column(name = "height")
    var height: Float? = null, // 키(cm)

    @Column(name = "weight")
    var weight: Float? = null, // 체중(kg)

    @Column(name = "muscle_mass")
    var muscleMass: Float? = null, // 근육량

    @Column(name = "bmi")
    var bmi: Float? = null, // BMI

    @Column(name = "body_fat_percentage")
    var bodyFatPercentage: Float? = null // 체지방률 - Float로 수정
) {
    fun updateWithDto(dto: CreateInBodyDto) {
        measurementDate = dto.measurementDate
        age = dto.age
        height = dto.height
        weight = dto.weight
        bodyFatPercentage = dto.bodyFatPercentage
        muscleMass = dto.muscleMass
        bmi = dto.bmi
    }
}
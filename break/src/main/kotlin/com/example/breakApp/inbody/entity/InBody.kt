package com.example.breakApp.inbody.entity

import com.example.breakApp.member.entity.Member
import jakarta.persistence.*
import java.time.LocalDate

@Entity // 이 클래스가 JPA 엔티티임을 나타냅니다.
@Table(name = "Inbody") // 이 엔티티가 매핑될 데이터베이스 테이블 이름을 지정합니다.
class InBody(
    @Id // 기본 키를 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 전략을 사용하여 ID를 생성합니다.
    @Column(name = "in_body_id") // 데이터베이스의 in_body_id 컬럼과 매핑
    val inBodyId: Long? = null, // 인바디 ID, null 허용

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 사용자 정보를 가져옵니다.
    @JoinColumn(name = "user_id", nullable = false) // user_id 컬럼과 매핑하여 외래 키 설정
    val member: Member, // 회원 객체를 나타내는 변수

    @Column(name = "measurement_date") // 측정 날짜를 나타내는 컬럼
    val measurementDate: LocalDate? = null, // 측정 날짜, null 허용

    @Column(name = "weight") // 체중을 나타내는 컬럼
    val weight: Double? = null, // 체중, null 허용

    @Column(name = "body_fat_percentage") // 체지방률을 나타내는 컬럼
    val bodyFatPercentage: Double? = null, // 체지방률, null 허용

    @Column(name = "muscle_mass") // 근육량을 나타내는 컬럼
    val muscleMass: Double? = null, // 근육량, null 허용

    @Column(name = "bmi") // BMI를 나타내는 컬럼 추가
    val bmi: Double? = null, // BMI, null 허용

    @Column(name = "visceral_fat_level") // 내장 지방 수치를 나타내는 컬럼 추가
    val visceralFatLevel: Double? = null, // 내장 지방 수치, null 허용

    @Column(name = "basal_metabolic_rate") // 기초 대사량을 나타내는 컬럼 추가
    val basalMetabolicRate: Double? = null // 기초 대사량, null 허용
)

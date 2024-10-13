package com.project.break_app.domain

import jakarta.persistence.*

// User 테이블에 매핑되는 JPA 엔티티
@Entity
@Table(name = "user") // 데이터베이스 테이블 이름을 "user"로 지정
data class User(
    @Id // userId는 기본 키로 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가(AUTO_INCREMENT) 설정
    val userId: Long = 0, // 기본 키로 자동 증가하는 값. 초기값은 0

    @Column(nullable = false, length = 20) // 이름 필드는 NULL일 수 없고 최대 20자
    val userName: String, // 사용자 이름

    @Column(nullable = false, length = 30, unique = true) // 이메일 필드는 NULL일 수 없고, 고유한 값이어야 하며, 최대 30자
    val email: String, // 사용자 이메일. 중복 허용되지 않음.

    @Column(nullable = false) // 나이 필드는 NULL일 수 없음
    val age: Int, // 사용자 나이

    @Enumerated(EnumType.STRING) // 성별 필드를 ENUM으로 저장하고, 문자열로 DB에 저장
    @Column(nullable = false) // 성별 필드는 NULL일 수 없음
    val gender: Gender // 성별 (남성, 여성, 기타)
)

// 성별을 정의하는 ENUM 클래스
enum class Gender {
    Male, Female, Other // 남성, 여성, 기타로 제한
}

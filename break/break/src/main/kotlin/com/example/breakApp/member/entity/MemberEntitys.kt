package com.example.breakApp.member.entity

import com.example.breakApp.common.status.Gender
import jakarta.persistence.*
import java.time.LocalDate

// users 테이블과 매핑되는 Member 엔티티
@Entity
@Table(name = "users")  // 테이블 이름을 users로 설정
class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO(AI) 대신 IDENTITY 사용
    @Column(name = "user_id")  // 테이블의 user_id 필드와 매핑, 기본 키
    var userId: Long? = null, // ?는 not null을 의미

    @Column(name = "login_id", nullable = false, length = 30, updatable = false)
    val loginId: String,  // 로그인 ID 필드, 변경 불가능

    @Column(nullable = false)
    val password: String,  // 비밀번호 필드 추가

    @Column(name = "user_name", nullable = false, length = 50)
    var userName: String,  // 사용자 이름 필드, 최대 50자

    @Column(name = "email", nullable = false, length = 100)
    var email: String,  // 이메일 필드, 최대 100자

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    var gender: Gender,  // 성별 필드, Enum으로 저장

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDate = LocalDate.now(),  // 계정 생성 날짜, 기본값은 현재 날짜

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDate = LocalDate.now()  // 계정 정보 수정 날짜, 기본값은 현재 날짜
)
package com.project.break_app.domain

import jakarta.persistence.*

// PasswordManager 테이블에 매핑되는 JPA 엔티티
@Entity
@Table(name = "PasswordManager") // 데이터베이스 테이블 이름을 "PasswordManager"로 지정
data class PasswordManager(
    @Id // userId는 기본 키로 사용
    @Column(name = "user_id")
    val userId: Long, // User 테이블의 userId와 동일하게 사용

    @Column(nullable = false) // 비밀번호는 NULL일 수 없음
    val password: String // 해시된 형태로 저장될 비밀번호
) {

    // User 엔티티와 1:1 관계 설정
    @OneToOne
    @MapsId // userId를 공유함 (User 테이블의 userId와 동일)
    @JoinColumn(name = "userId") // 외래 키로 User 테이블의 userId를 참조
    lateinit var user: User // User 테이블과 일대일 관계
}

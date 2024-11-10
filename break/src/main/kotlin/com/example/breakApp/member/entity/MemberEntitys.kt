package com.example.breakApp.member.entity

import com.example.breakApp.common.status.Gender
import com.example.breakApp.member.dto.MemberDtoResponse
import java.time.LocalDate
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// users 테이블과 매핑되는 Member 엔티티
@Entity
@Table(name = "users")
class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO(AI) 대신 IDENTITY 사용
    @Column(name = "user_id")  // 테이블의 user_id 필드와 매핑, 기본 키
    var userId: Long? = null,

    @Column(name = "login_id", nullable = false, length = 30, updatable = false)
    val loginId: String,  // 로그인 ID 필드, 변경 불가능

    @Column(nullable = false)
    var password: String,  // 비밀번호 필드 추가

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
    var updatedAt: LocalDate = LocalDate.now(),  // 계정 정보 수정 날짜, 기본값은 현재 날짜

    @Column(name = "refresh_token")  // 리프레시 토큰 필드 매핑 추가
    var refreshToken: String? = null,  // 리프레시 토큰 필드

    // 이메일 인증 여부를 나타내는 필드, 기본값은 false
    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false
) {
    private fun LocalDate.formatData(): String =
        this.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    fun toDto(): MemberDtoResponse =
        MemberDtoResponse(userId!! ,loginId, userName, email, gender.desc, createdAt.formatData(), updatedAt.formatData())
}


@Entity
class VerificationToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 6)
    val token: String,

    @OneToOne
    @JoinColumn(name = "pending_member_id", referencedColumnName = "id")
    val pendingMember: PendingMember,

    @Column(nullable = false)
    val expiryDate: LocalDateTime
) {
    // 만료 여부 확인 메서드
    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(expiryDate)
    }
}
@Entity
class PendingMember(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val loginId: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val userName: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val gender: Gender
)

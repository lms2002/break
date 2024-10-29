package com.example.breakApp.member.entity

import com.example.breakApp.common.status.Gender
import com.example.breakApp.common.status.ROLE
import jakarta.persistence.*
import java.time.LocalDate

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.GrantedAuthority

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
    var updatedAt: LocalDate = LocalDate.now(),  // 계정 정보 수정 날짜, 기본값은 현재 날짜

    @Column(name = "refresh_token")  // 리프레시 토큰 필드 매핑 추가
    var refreshToken: String? = null  // 리프레시 토큰 필드
) {
    // member(users) 엔티티와 1:n 관계 (한명의 멤버는 여러 권한을 갖을 수 있음)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    val memberRole: List<MemberRole>? = null
    /**
     * Member 객체를 Authentication 객체로 변환
     * Member 객체를 Spring Security의 Authentication 객체로 변환하기 위해 멤버 엔티티에 선언
     */
    fun toAuthentication(): UsernamePasswordAuthenticationToken {
        // 권한 리스트 생성
        val authorities: Collection<GrantedAuthority> = memberRole?.map { SimpleGrantedAuthority(it.role.name) }
            ?: listOf(SimpleGrantedAuthority("ROLE_MEMBER"))

        // Authentication 객체 반환
        return UsernamePasswordAuthenticationToken(this.userId, null, authorities)
    }
}

// 멤버의 권한 엔티티
@Entity
class MemberRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: ROLE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_member_id"))
    val member: Member,
    )
package com.example.breakApp.member.repository

import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.entity.MemberRole
import org.springframework.data.jpa.repository.JpaRepository

// JpaRepository를 상속받은 MemberRepositories
interface MemberRepository : JpaRepository<Member, Long>{
    // 아이디 중복검사를 위한 함수
    fun findByLoginId(loginId: String): Member?
    // 이메일을 기준으로 Member 엔티티를 찾는 메서드
    fun findByEmail(email: String): Member?
}
// 회원 가입 시 DB에 권한을 저장 하기 위해 저장소 추가
interface MemberRoleRepository : JpaRepository<MemberRole, Long>
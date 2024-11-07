package com.example.breakApp.member.repository

import com.example.breakApp.member.entity.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<VerificationToken, Long> {
    // 6자리 인증 코드를 기반으로 VerificationToken 엔티티를 찾는 메서드
    fun findByToken(token: String): VerificationToken?
}
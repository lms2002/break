package com.example.breakApp.member.repository

import com.example.breakApp.member.entity.VerifiedEmail
import org.springframework.data.jpa.repository.JpaRepository

interface VerifiedEmailRepository : JpaRepository<VerifiedEmail, Long> {
    fun findByEmail(email: String): VerifiedEmail? // 추가된 메서드
}
package com.example.breakApp.member.repository

import com.example.breakApp.member.entity.PendingRegistration
import org.springframework.data.jpa.repository.JpaRepository

interface PendingRegistrationRepository : JpaRepository<PendingRegistration, Long> {
    fun existsByEmail(email: String): Boolean
    fun existsByLoginId(loginId: String): Boolean
    fun findByEmailAndLoginId(email: String, loginId: String): PendingRegistration?
}

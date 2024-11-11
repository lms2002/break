package com.example.breakApp.member.repository

import com.example.breakApp.member.entity.PendingMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PendingMemberRepository : JpaRepository<PendingMember, Long> {
    fun findByEmail(email: String): PendingMember?
}
